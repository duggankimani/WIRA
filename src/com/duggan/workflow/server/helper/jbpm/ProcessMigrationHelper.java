package com.duggan.workflow.server.helper.jbpm;

import java.util.List;

import com.duggan.workflow.server.dao.model.LocalAttachment;
import com.duggan.workflow.server.dao.model.ProcessDefModel;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.shared.exceptions.ProcessInitializationException;

public class ProcessMigrationHelper {

	/**
	 * This method activates process
	 * 
	 * @param processDefId
	 */
	public static void start(Long processDefId){
		start(processDefId, false);
	}
	
	public static void start(Long processDefId, boolean force){
		ProcessDefModel model = DB.getProcessDao().getProcessDef(processDefId);
		start(model, force);
	}
	
	public static void start(ProcessDefModel model, boolean reloadIfAlreadyLoaded) {
		
		if(JBPMHelper.get().isProcessingRunning(model.getProcessId()) && !reloadIfAlreadyLoaded){
			return;
		}
		
		List<LocalAttachment> attachments = 
				DB.getAttachmentDao().getAttachmentsForProcessDef(model);
		
		if(attachments.size()==0){
			throw new ProcessInitializationException("Cannot start process ["+model+
					"]- No Process Definition Attachment Found. Please confirm you have attached the necessary process files");
		}
		
		if(attachments.size()>1){
			throw new ProcessInitializationException("Cannot start process ["+model+"]- More than one Process Definition Attachment Found");
		}
		
		LocalAttachment attachment = attachments.get(0);
		
		JBPMHelper.get().loadKnowledge(attachment.getAttachment(), model.getName());
		
	}

	
	public static void stop(Long processDefId){
		ProcessDefModel model = DB.getProcessDao().getProcessDef(processDefId);
		String processId = model.getProcessId();
		
		if(JBPMHelper.get().isProcessingRunning(model.getProcessId())){
			JBPMHelper.get().stop(processId);
		}
	}

}
