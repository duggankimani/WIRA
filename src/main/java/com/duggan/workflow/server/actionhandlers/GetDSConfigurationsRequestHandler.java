package com.duggan.workflow.server.actionhandlers;

import java.util.ArrayList;
import java.util.List;

import com.duggan.workflow.server.dao.helper.DSConfigHelper;
import com.duggan.workflow.server.db.LookupLoaderImpl;
import com.duggan.workflow.shared.model.DSConfiguration;
import com.duggan.workflow.shared.model.Status;
import com.duggan.workflow.shared.requests.GetDSConfigurationsRequest;
import com.duggan.workflow.shared.responses.GetDSConfigurationsResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.response.BaseResponse;

public class GetDSConfigurationsRequestHandler
		extends
		AbstractActionHandler<GetDSConfigurationsRequest, GetDSConfigurationsResponse> {

	@Inject
	public GetDSConfigurationsRequestHandler() {
	}

	@Override
	public void execute(GetDSConfigurationsRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		GetDSConfigurationsResponse response = (GetDSConfigurationsResponse)actionResult;
		
		List<DSConfiguration> configs =  DSConfigHelper.getConfigurations();
		if(configs!=null){
			LookupLoaderImpl impl = new LookupLoaderImpl();
			for(DSConfiguration config: configs){
				boolean isActive = impl.testDatasourceName(config.getName());	
				config.setStatus(isActive?Status.RUNNING:Status.INACTIVE);
			}
		}
		response.setConfigurations((ArrayList<DSConfiguration>) configs);
	}
	
	@Override
	public Class<GetDSConfigurationsRequest> getActionType() {
		return GetDSConfigurationsRequest.class;
	}
}
