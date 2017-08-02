package com.duggan.workflow.server.helper.jbpm;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.drools.event.process.ProcessCompletedEvent;
import org.drools.event.process.ProcessEventListener;
import org.drools.event.process.ProcessNodeLeftEvent;
import org.drools.event.process.ProcessNodeTriggeredEvent;
import org.drools.event.process.ProcessStartedEvent;
import org.drools.event.process.ProcessVariableChangedEvent;
import org.jbpm.process.core.context.variable.VariableScope;
import org.jbpm.process.instance.context.variable.VariableScopeInstance;

import com.duggan.workflow.server.dao.helper.CatalogDaoHelper;
import com.duggan.workflow.server.dao.helper.DocumentDaoHelper;
import com.duggan.workflow.server.dao.model.DocumentModelJson;
import com.duggan.workflow.server.dao.model.User;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.helper.email.EmailUtil;
import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.catalog.Catalog;
import com.wira.commons.shared.models.HTUser;

public class WiraProcessEventListener implements ProcessEventListener{

	Logger logger = Logger.getLogger(WiraProcessEventListener.class);
	
	@Override
	public void beforeProcessStarted(ProcessStartedEvent event) {
		
	}

	@Override
	public void afterProcessStarted(ProcessStartedEvent event) {
		
	}

	@Override
	public void beforeProcessCompleted(ProcessCompletedEvent event) {
		logger.info("WiraProcessEventListener.beforeProcessCompleted called!");
		org.jbpm.process.instance.ProcessInstance instance = (org.jbpm.process.instance.ProcessInstance)event.getProcessInstance();
		VariableScopeInstance variableScope = (VariableScopeInstance) instance
				.getContextInstance(VariableScope.VARIABLE_SCOPE);
		
		String processId = instance.getProcessId();
		Map<String,Object> values = variableScope.getVariables();
		
		Doc doc = null;
		if(isDoc(values.get("document"))){
			doc = (Doc)values.get("document");
		}else{
			for(String key: values.keySet()){
				if(isDoc(values.get(key))){
					doc = (Doc)values.get(key);
					break;
				}
			}
		}
		
		String loggerPrefix = "WiraProcessEvent [ProcessId="+processId+",ProcessInstanceId="+instance.getId()+"]";
		if(doc==null){
			logger.warn(loggerPrefix+"- No Document Found, proceeding without dumping to report tables.");
			return;
		}
		
		
		List<Catalog> catalogs = CatalogDaoHelper.getCatalogsForProcess(processId);
		if(catalogs.isEmpty()){
			logger.debug(loggerPrefix+"- No Catalogs Found, proceeding without dumping to report tables.");
			return;
		}
		
		for(Catalog catalog: catalogs){
			try {
				CatalogDaoHelper.mapAndSaveFormData(catalog,doc);
			}catch (Exception e) {
				logger.fatal("Data dump for REPORT TABLE - "+catalog.getDescription()
				+" on case "+doc.getCaseNo()+" failed, please try again later"); 
				try {
					Collection<User> administrators = DB.getUserGroupDao().getAllUsersByGroupId("ADMIN");	
					HTUser[] users= new HTUser[administrators.size()];
					int i=0;
					for(User u: administrators) {
						users[i++] = u.toHTUser();
					}
					EmailUtil.sendEmail("Error Dumping REPORT TABLE - "+catalog.getDescription()+" - @@caseNo"
							, "Dear Admin,<p> There was an error dumping data to report table "+catalog.getDisplayName()+" cause: p<>"
									+ "</p> "
									+ ""+e.getMessage()+"<br/>"
									+ ExceptionUtils.getStackTrace(e)+"", doc, users);
				}catch(Exception e1) {
					e1.printStackTrace();
				}
			}
			
		}
		
		Long id = doc.getDocumentId();
		String refId = doc.getRefId();
		
		//Clear Current Task Information
		if(doc!=null && refId!=null && !refId.isEmpty()){
			System.out.println("WiraProcessEventListener.afterProcessCompleted - Update Document BY RefId "+doc.getRefId());
			try {
				doc = DocumentDaoHelper.getDocJson(doc.getRefId());
			}catch (Exception e) {
				System.err.print("WiraProcessEventListener.afterProcessCompleted ### Failed to GET Document - "+doc.getRefId()+": cause "+e.getMessage());
			}
		}
		
		if(doc==null && id!=null) {
			System.out.println("WiraProcessEventListener.afterProcessCompleted - Update Document By ID "+id);
			try {
				DocumentModelJson json = DB.getDocumentDao().getById(DocumentModelJson.class, id);
				doc = DB.getDocumentDao().getDocJson(json, true);
			}catch (Exception e) {
				System.err.print("WiraProcessEventListener.afterProcessCompleted ### Failed to GET Document BY ID- "+doc.getRefId()+": cause "+e.getMessage());
			}
		}
		
		if(doc!=null) {
			try {
				doc.setCurrentTaskId(null);
				doc.setCurrentTaskName(null);
				doc.setTaskActualOwner(null);
				DocumentDaoHelper.createJson((Document)doc);
			}catch (Exception e) {
				System.err.print("WiraProcessEventListener.afterProcessCompleted ###Could not update json for update, leaving inconsistent!- "+doc.getRefId()+" #### "+doc.getCaseNo()+": cause "+e.getMessage());
			}
			
		}
		
	}

	@Override
	public void afterProcessCompleted(ProcessCompletedEvent event) {
		logger.info("WiraProcessEventListener.afterProcessCompleted called!");
	}

	private boolean isDoc(Object obj) {
		
		return obj!=null && obj instanceof Doc;
	}

	@Override
	public void beforeNodeTriggered(ProcessNodeTriggeredEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterNodeTriggered(ProcessNodeTriggeredEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeNodeLeft(ProcessNodeLeftEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterNodeLeft(ProcessNodeLeftEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeVariableChanged(ProcessVariableChangedEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterVariableChanged(ProcessVariableChangedEvent event) {
		// TODO Auto-generated method stub
		
	}

}
