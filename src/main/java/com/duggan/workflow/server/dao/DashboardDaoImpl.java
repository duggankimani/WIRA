package com.duggan.workflow.server.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.shared.model.DocStatus;
import com.duggan.workflow.shared.model.dashboard.Data;
import com.duggan.workflow.shared.model.dashboard.LongTask;

public class DashboardDaoImpl extends BaseDaoImpl{
	
	public Integer getRequestCount(DocStatus status){
		return getRequestCount(true, status); 
	}
	
	public Integer getRequestCount(boolean is, DocStatus status){
		StringBuffer sql = new StringBuffer("select count(*) from localdocument where ");
		
		if(is){
			sql.append("status=?");
		}else{
			sql.append("status!=?");
		}
		
		Query query = getEntityManager().createNativeQuery(sql.toString())
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
			"from localdocument where status!='DRAFTED' "+
			")as docdays "+
			"group by days,cnt order by cnt;");
		
		Query query = getEntityManager().createNativeQuery(sql.toString());
		List<?> list= query.getResultList();
		
		for(Object c: list){
			Object[] row = (Object[])c;
			Data data = new Data((String)row[0], (Number)row[1]);
			dataLst.add(data);
		}
		
		return dataLst;
	}
	
	public List<Data> getDocumentCounts(){
		List<Data> dataLst = new ArrayList<>();
		StringBuffer sql = new StringBuffer("select (select display from ADDocType where id=doctype) doctype,ct " +
				"from (select count(*) ct,doctype from LocalDocument d where status!='DRAFTED' group by doctype) as papa order by ct desc");
		
		Query query = getEntityManager().createNativeQuery(sql.toString());
		List<?> list = query.getResultList();
		for(Object c: list){
			Object[] row = (Object[])c;
			Data data = new Data((String)row[0], (Number)row[1]);
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
		
		Query query = getEntityManager().createNativeQuery(sql.toString());
		List<?> list = query.getResultList();
		for(Object c: list){
			Object[] row = (Object[])c;
			LongTask longTask = new LongTask();
			longTask.setNoOfTasks(((Number)row[0]).intValue());
			longTask.setAverageTime(((Number)row[1]).intValue());
			
			String taskName = row[2].toString();
			String docType = getDocumentTypeNameByTaskName(taskName);
			longTask.setDocumentType(docType);
			
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
		
		
		Query query = getEntityManager().createNativeQuery(buffer.toString()).setParameter(1, taskName);
		Long taskId = ((Number)query.getSingleResult()).longValue();
		
		return taskId;
	}

	public String getDocumentTypeNameByTaskName(String taskName){
		StringBuffer buffer = new StringBuffer("select display from addoctype " +
				"where id=(select doctype from localdocument where processinstanceid=" +
				"(select processinstanceid from task inner join i18ntext on " +
				"(task.id=i18ntext.task_names_id) where shorttext=? limit 1))");
		Query query = getEntityManager().createNativeQuery(buffer.toString()).setParameter(1, taskName);
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
		
		Query query = getEntityManager().createNativeQuery(sql.toString());
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
	
}
