package com.duggan.workflow.shared.exceptions;

import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.shared.model.Document;
import com.gwtplatform.dispatch.shared.ActionException;

/**
 * 
 * @author duggan
 *
 */
public class IllegalApprovalRequestException extends ActionException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public IllegalApprovalRequestException(String message){
		super(message);
	}
	
	public IllegalApprovalRequestException(Document document){
		this("Cannot execute Approval Request - Document ["+document+"] is already attached to another process "+
				JBPMHelper.getProcessDetails(document.getProcessInstanceId()));
		
	}
}
