package com.duggan.workflow.server.actionhandlers;

import java.util.List;

import com.duggan.workflow.server.dao.helper.DSConfigHelper;
import com.duggan.workflow.server.db.LookupLoaderImpl;
import com.duggan.workflow.shared.model.DSConfiguration;
import com.duggan.workflow.shared.model.Status;
import com.duggan.workflow.shared.requests.GetDSStatusRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetDSStatusResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetDSStatusRequestActionHandler extends
		AbstractActionHandler<GetDSStatusRequest, GetDSStatusResponse> {

	@Inject
	public GetDSStatusRequestActionHandler() {
	}

	@Override
	public void execute(GetDSStatusRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		
		String configName = action.getConfigName();
		LookupLoaderImpl impl = new LookupLoaderImpl();
		
		GetDSStatusResponse response = (GetDSStatusResponse)actionResult;
		if(configName!=null){
			
			boolean isActive = impl.testDatasourceName(configName);
			DSConfiguration config = DSConfigHelper.getConfigurationByName(configName);
			config.setStatus(isActive?Status.RUNNING:Status.INACTIVE);
			response.addConfig(config);
			
		}else{
			List<DSConfiguration> configs =  DSConfigHelper.getConfigurations();
			if(configs!=null){
				for(DSConfiguration config: configs){
					boolean isActive = impl.testDatasourceName(config.getName());	
					config.setStatus(isActive?Status.RUNNING:Status.INACTIVE);
					response.addConfig(config);
				}
			}
		}		
		
	}
	
	@Override
	public Class<GetDSStatusRequest> getActionType() {
		return GetDSStatusRequest.class;
	}
}
