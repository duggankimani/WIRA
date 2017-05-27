package com.duggan.workflow.server.dao;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.duggan.workflow.server.dao.model.ADDocType;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.shared.model.DocStatus;
import com.duggan.workflow.shared.model.dashboard.Data;
import com.duggan.workflow.shared.model.dashboard.EmployeeWorkload;
import com.duggan.workflow.shared.model.dashboard.LongTask;
import com.duggan.workflow.shared.model.dashboard.ProcessTrend;
import com.duggan.workflow.shared.model.dashboard.ProcessesSummary;
import com.duggan.workflow.shared.model.dashboard.TaskAging;

public class DashboardDaoImpl extends BaseDaoImpl{

	public DashboardDaoImpl(EntityManager em) {
		super(em);
	}

	public Integer getRequestCount(DocStatus status){
		return getRequestCount(true, status); 
	}
	
	public ArrayList<ProcessesSummary> getProcessesSummary(String processId, Date startDate, Date endDate){
		String sql = "select refId,processId,name,inprogress,completed,overdue,avgtot,targetDays "
				+ "from fun_GetProcessSummary(:startDate, :endDate) order by inprogress desc,completed desc";
		
		if(processId!=null){
			sql = "select refId,processId,name,inprogress,completed,overdue,avgtot,targetDays "
					+ "from fun_GetProcessSummaryPerProcess(:processId, :startDate, :endDate) order by inprogress desc,completed desc";
		}
		
		Query query = getEntityManager().createNativeQuery(sql)
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate);
		
		if(processId!=null){
			query.setParameter("processId", processId);
		}
		
		List<Object[]> rows = getResultList(query);
		
		ArrayList<ProcessesSummary> summaries = new ArrayList<ProcessesSummary>();
		for(Object[] row: rows){
			int i=0;
			Object value = null;
			String refId = (value=row[i++])==null? null : value.toString();
			String aProcessId = (value=row[i++])==null? null : value.toString();
			String name = (value=row[i++])==null? null : value.toString();
			Integer inprogress = (value=row[i++])==null? null : (Integer)value;
			Integer completed = (value=row[i++])==null? null : (Integer)value;
			Integer overdue = (value=row[i++])==null? null : (Integer)value;
			Double avgtot = (value=row[i++])==null? null : ((Number)value).doubleValue();
			Integer targetDay = (value=row[i++])==null? null : (Integer)value;
			summaries.add(new ProcessesSummary(refId, aProcessId, name, inprogress, completed, overdue, avgtot, targetDay));
		}
		
		return summaries;
	}
	
	public ArrayList<EmployeeWorkload> getTasksSummary(String processId, Date startDate, Date endDate){
		
		String sql = "select refId, ownerid, fullName, inprogress, completed, total, "
				+ "overdue, avg from fun_GetTaskSummary(:startDate, :endDate) order by total desc, completed desc limit 5"; 
		
		if(processId!=null){
			sql = "select refId, ownerid, fullName, inprogress, completed, total, "
					+ "overdue, avg from fun_GetTaskSummaryPerProcess(:processId, :startDate, :endDate) order by total desc, completed desc limit 5";
		}
		
		Query query = getEntityManager().createNativeQuery(sql)
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate);
		
		if(processId!=null){
			query.setParameter("processId", processId);
		}
		
		List<Object[]> rows = getResultList(query);
		
		ArrayList<EmployeeWorkload> workloads = new ArrayList<EmployeeWorkload>();
		for(Object[] row: rows){
			int i=0;
			Object value = null;
			String refId = (value=row[i++])==null? null : value.toString();
			String ownerid = (value=row[i++])==null? null : value.toString();
			String fullName = (value=row[i++])==null? null : value.toString();
			Integer inprogress = (value=row[i++])==null? null : (Integer)value;
			Integer completed = (value=row[i++])==null? null : (Integer)value;
			Integer total = (value=row[i++])==null? null : (Integer)value;
			Integer overdue = (value=row[i++])==null? null : (Integer)value;
			Double avg = (value=row[i++])==null? null : ((Number)value).doubleValue();
			
			workloads.add(new EmployeeWorkload(refId, ownerid, fullName, inprogress, completed, total, overdue, avg));
		}
		
		return workloads;
	}
	
	public Integer getRequestCount(boolean is, DocStatus status){
		StringBuffer sql = new StringBuffer("select count(*) from documentjson where ");
		
		if(is){
			sql.append("status=?");
		}else{
			sql.append("status!=?");
		}
		
		Query query = em.createNativeQuery(sql.toString())
				.setParameter(1, status.name());
		
		Number number = (Number)query.getSingleResult();
		
		return number.intValue();
	}
	
	public List<Data> getRequestAging(){
		List<Data> dataLst = new ArrayList<>();
		
		StringBuffer sql = new StringBuffer("select days,count(*) from "+
			"(select "+
			"case when (extract(day from current_timestamp-created)<8)then '0-7' "+
			"when (extract(day from current_timestamp-created)<15)then '8-14' "+
			"when (extract(day from current_timestamp-created)<31)then '15-30' "+
			"when (extract(day from current_timestamp-created)<61)then '31-60' "+
			"else '60 +' "+
			"END days "+
			", "+
			"case when (extract(day from current_timestamp-created)<8)then 7 "+
			"when (extract(day from current_timestamp-created)<15)then 14 "+
			"when (extract(day from current_timestamp-created)<31)then 30 "+
			"when (extract(day from current_timestamp-created)<61)then 60 "+
			"else 61 "+
			"END cnt "+
			"from processinstancelog where status==0 "+
			")as docdays "+
			"group by days,cnt order by cnt;");
		
		Query query = em.createNativeQuery(sql.toString());
		List<?> list= query.getResultList();
		
		for(Object c: list){
			Object[] row = (Object[])c;
			Data data = new Data((String)row[0], ((Number)row[1]).doubleValue());
			dataLst.add(data);
		}
		
		return dataLst;
	}
	
	public List<Data> getDocumentCounts(){
		List<Data> dataLst = new ArrayList<>();
		StringBuffer sql = new StringBuffer("select (select display from ADDocType where id=doctype) doctype,ct " +
				"from (select count(*) ct,doctype from LocalDocument d where status!='DRAFTED' group by doctype) as papa order by ct desc");
		
		Query query = em.createNativeQuery(sql.toString());
		List<?> list = query.getResultList();
		for(Object c: list){
			Object[] row = (Object[])c;
			Data data = new Data((String)row[0], ((Number)row[1]).doubleValue());
			dataLst.add(data);
		}
		
		return dataLst;
	}
	
	/**
	 * Top 10 Long Tasks that take the longest time
	 * 
	 * @return
	 */
	public List<LongTask> getLongLivingTasks(){
		List<LongTask> longTasks  = new ArrayList<>();
		StringBuffer sql = new StringBuffer("select no_of_tasks,avg, taskname from " +
				"(select count(*) no_of_tasks,sum(days)/count(*) avg,taskname from "+
				"(select extract(day from " +
				"case when completedon is null " +
				"then current_timestamp " +
				"else completedon end " +
				"- createdon) days," +
				"i.shorttext taskname from task t i" +
				"nner join i18ntext i on (t.id=i.task_names_id)) " +
				"as q1 where days!=0 group by taskname) as avgtasktimes "+
				"order by avg desc limit 10");
		
		Query query = em.createNativeQuery(sql.toString());
		List<?> list = query.getResultList();
		for(Object c: list){
			Object[] row = (Object[])c;
			LongTask longTask = new LongTask();
			longTask.setNoOfTasks(((Number)row[0]).intValue());
			
			String taskName = row[2].toString();
			String docType = getDocumentTypeNameByTaskName(taskName);
			
			String taskDisplayName = taskName;
			
			try{
				taskDisplayName = JBPMHelper.get().getDisplayName(getLastTaskId(taskName));
				//Will throw an exception if the Knowledgebase cannot be loaded
			}catch(Exception e){}
			longTask.setTaskName(taskDisplayName);
			
			longTasks.add(longTask);
		}
		
		return longTasks;
	}
	
	private Long getLastTaskId(String taskName) {
		StringBuffer buffer = new StringBuffer("select max(t.id) from task t inner join i18ntext i on (t.id=i.task_names_id) " +
				"where i.shorttext=?");
		
		
		Query query = em.createNativeQuery(buffer.toString()).setParameter(1, taskName);
		Long taskId = ((Number)query.getSingleResult()).longValue();
		
		return taskId;
	}

	public String getDocumentTypeNameByTaskName(String taskName){
		StringBuffer buffer = new StringBuffer("select display from addoctype " +
				"where id=(select doctype from localdocument where processinstanceid=" +
				"(select processinstanceid from task inner join i18ntext on " +
				"(task.id=i18ntext.task_names_id) where shorttext=? limit 1))");
		Query query = em.createNativeQuery(buffer.toString()).setParameter(1, taskName);
		String docType = getSingleResultOrNull(query);
		
		return docType;
	}
	
	public List<Data> getTaskCompletionData(){
		List<Data> dataLst = new ArrayList<>();
		StringBuffer sql = new StringBuffer("select monthcr,taskscreatedcount,taskscompletedcount from "+
				"((select count(*) taskscreatedcount,monthcr " +
				"from (select extract(MONTH from createdon) monthcr from task) as monthcreated group by monthcr) as mcr " +
				"inner join " +
				"(select count(*) taskscompletedcount,monthcp from (select extract(MONTH from completedon) monthcp  " +
				"from task where completedon is not null) as monthcompleted group by monthcp) as mcp " +
				"on (mcr.monthcr = mcp.monthcp))");
		
		Query query = em.createNativeQuery(sql.toString());
		List<?> list = query.getResultList();
		String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
		for(Object c: list){
			Object[] row = (Object[])c;
			int month = ((Number)row[0]).intValue();
			Data data = new Data(monthNames[month-1], ((Number)row[1]).intValue());
			data.setData2(((Number)row[2]).intValue());
			dataLst.add(data);
		}
		
		return dataLst;
	}

	public ADDocType getDocumentTypeByProcessRef(String processRefId) {
		
		String hql = "FROM ADDocType t where t.processDef.refId=:processRefId";
		return getSingleResultOrNull(getEntityManager().createQuery(hql).setParameter("processRefId", processRefId));
	}

	public ArrayList<LongTask> getLongTasks(String processId,
			Date startDate, Date endDate) {
		
		String sql = "select taskname, taskcount, avgdays, peoplecount, peoplenames "
				+ "from func_GetTasksWorkfload(:processId,:startDate,:endDate) order by avgdays desc limit 5";
		
		Query query = getEntityManager().createNativeQuery(sql)
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.setParameter("processId", processId);
		
		List<Object[]> rows = getResultList(query);
		
		ArrayList<LongTask> longTasks = new ArrayList<LongTask>();
		for(Object[] row: rows){
			int i=0;
			Object value = null;
			String taskname = (value=row[i++])==null? null : value.toString();
			Integer taskCount = (value=row[i++])==null? null : (Integer)value;
			Double avgDays = (value=row[i++])==null? null : ((Number)value).doubleValue();
			Integer peopleCount = (value=row[i++])==null? null : (Integer)value;
			String peopleNames = (value=row[i++])==null? null : value.toString();
			
			longTasks.add(new LongTask(taskname, processId, avgDays,taskCount, peopleCount, peopleNames));
		}
		
		return longTasks;
	}

	public ArrayList<TaskAging> getProcessAging(String processId,
			Date startDate, Date endDate) {
		String sql = "select agingperiod, taskcount from func_GetTasksAging(:processId,:startDate,:endDate)";
		
		Query query = getEntityManager().createNativeQuery(sql)
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.setParameter("processId", processId);
		
		List<Object[]> rows = getResultList(query);
		
		ArrayList<TaskAging> aging = new ArrayList<TaskAging>();
		for(Object[] row: rows){
			int i=0;
			Object value = null;
			
			String period = (value=row[i++])==null? null : value.toString();
			int pos = 0;
			if(period.startsWith("0")){
				pos = 1;
			}else if(period.startsWith("7")){
				pos = 2;
			}else if(period.startsWith("15")){
				pos = 3;
			}else if(period.startsWith("31")){
				pos = 4;
			}else{
				pos = 5;
			}
			
			Integer count = (value=row[i++])==null? null : (Integer)value;
			aging.add(new TaskAging(period, count, pos));
		}
		
		aging.sort(new Comparator<TaskAging>() {
			@Override
			public int compare(TaskAging o1, TaskAging o2) {
				return ((Integer)o1.getPosition()).compareTo(((Integer)o2.getPosition()));
			}
		});
		return aging;
	}


	public ArrayList<ProcessTrend> getProcessTrend(String processId,
			Date startDate, Date endDate, String periodicity, int type) {
		
		String sql = "select period, taskcount from func_GetTasksTrend(:processId,:startDate, :endDate, :periodicity, :type) order by period";
		
		Query query = getEntityManager().createNativeQuery(sql)
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.setParameter("processId", processId)
				.setParameter("periodicity", periodicity)
				.setParameter("type", type);
		
		List<Object[]> rows = getResultList(query);
		
		ArrayList<ProcessTrend> trends = new ArrayList<ProcessTrend>();
		for(Object[] row: rows){
			int i=0;
			Object value = null;
			Integer period = (value=row[i++])==null? null : (Integer)value;
			Integer count = (value=row[i++])==null? null : (Integer)value;
			trends.add(new ProcessTrend(period, count));
		}
		
		return trends;
	}
	
}
