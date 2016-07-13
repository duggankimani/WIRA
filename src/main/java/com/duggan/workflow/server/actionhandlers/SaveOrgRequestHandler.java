package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.dao.helper.OrganizationDaoHelper;
import com.duggan.workflow.shared.requests.SaveOrgRequest;
import com.duggan.workflow.shared.responses.SaveOrgResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.models.Org;
import com.wira.commons.shared.response.BaseResponse;

public class SaveOrgRequestHandler extends
		AbstractActionHandler<SaveOrgRequest, SaveOrgResponse> {

	@Inject
	public SaveOrgRequestHandler() {
	}
	
	@Override
	public void execute(SaveOrgRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		Org org = action.getOrg();
		
		if(!action.isDelete()){	
			
			org = OrganizationDaoHelper.createOrgModel(org);
			
			//save
			SaveOrgResponse response = (SaveOrgResponse)actionResult;
			response.setOrg(org);
			
		}
		
		if(action.isDelete()){
			OrganizationDaoHelper.deleteOrgModel(org.getRefId());
		}
	}
	
	@Override
	public Class<SaveOrgRequest> getActionType() {
		return SaveOrgRequest.class;
	}
}
