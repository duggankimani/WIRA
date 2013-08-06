package xtension.workitems;

import org.drools.runtime.process.WorkItem;
import org.drools.runtime.process.WorkItemHandler;
import org.drools.runtime.process.WorkItemManager;

import com.duggan.workflow.server.helper.dao.DocumentDaoHelper;

/**
 * This work item handler updates the approval status
 * of the local document. It should be called if the
 * document is completed(approved to the end) or 
 * rejected by any of the approvers in the process
 * 
 * @author duggan
 *
 */
public class UpdateApprovalStatusWorkItemHandler implements WorkItemHandler{

	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		Long documentId = new Long((String)workItem.getParameter("DocumentId"));
		
		Object isApproved = workItem.getParameter("isApproved");
		
		if(documentId==null || isApproved==null){
			throw new IllegalArgumentException("DocumentID and isApproved cannot be null");			
		}
		
		/*
		 * This work item is meant to primarily record premature
		 * end of process by an approver rejecting a document
		 * 
		 */
		if(workItem.getParameter("PROCESS_COMPLETED")==null || !(Boolean)isApproved){
			DocumentDaoHelper.saveApproval(new Long(documentId.toString()), (Boolean)isApproved);
			
		}else if((Boolean)isApproved){
			Object completed =workItem.getParameter("PROCESS_COMPLETED"); 
			if(completed!=null && completed.toString().equalsIgnoreCase("true")){
				DocumentDaoHelper.saveApproval(new Long(documentId.toString()), (Boolean)isApproved);
			}
		}
		
		manager.completeWorkItem(workItem.getId(), workItem.getParameters());
	}

	@Override
	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		
	}

	
}
