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
		Object documentId = workItem.getParameter("DocumentId");
		if(documentId==null){
			documentId = workItem.getParameter("documentId");
		}

		Object isApproved = workItem.getParameter("isApproved");
		
		if(documentId==null || isApproved==null){
			throw new IllegalArgumentException("DocumentID and isApproved cannot be null");			
		}
		
		DocumentDaoHelper.saveApproval(new Long(documentId.toString()), (Boolean)isApproved);
		
	}

	@Override
	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		
	}

	
}
