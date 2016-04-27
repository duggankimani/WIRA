package com.duggan.workflow.server.helper.jbpm;

import java.util.List;
import java.util.Map;

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
import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.model.catalog.Catalog;

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
			CatalogDaoHelper.mapAndSaveFormData(catalog,doc);
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
