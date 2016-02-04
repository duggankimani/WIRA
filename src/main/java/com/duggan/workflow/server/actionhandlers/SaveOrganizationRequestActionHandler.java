package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.helper.auth.LoginHelper;
import com.duggan.workflow.shared.model.Organization;
import com.duggan.workflow.shared.model.UserGroup;
import com.duggan.workflow.shared.requests.SaveGroupRequest;
import com.duggan.workflow.shared.requests.SaveOrganizationRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.SaveGroupResponse;
import com.duggan.workflow.shared.responses.SaveOrganizationResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class SaveOrganizationRequestActionHandler extends
		AbstractActionHandler<SaveOrganizationRequest, SaveOrganizationResponse> {

	@Inject
	public SaveOrganizationRequestActionHandler() {
	}
	
	@Override
	public void execute(SaveOrganizationRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		Organization organization = action.getOrganization();
		
		if(!action.isDelete()){	
			
			organization = LoginHelper.get().createOrganization(organization);
			
			//save
			SaveOrganizationResponse response = (SaveOrganizationResponse)actionResult;
			response.setOrganization(organization);
			
		}
		
		if(action.isDelete()){
			LoginHelper.get().deleteOrganizaton(organization);
		}
	}
	
	@Override
	public Class<SaveOrganizationRequest> getActionType() {
		return SaveOrganizationRequest.class;
	}
}
