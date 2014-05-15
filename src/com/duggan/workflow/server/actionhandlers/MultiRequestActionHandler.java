package com.duggan.workflow.server.actionhandlers;

import java.util.List;

import com.duggan.workflow.shared.requests.BaseRequest;
import com.duggan.workflow.shared.requests.MultiRequestAction;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.MultiRequestActionResult;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

/**
 * This class will execute multiple requests/commands/actions
 * whilst ensuring that they all share the same session &
 * transaction 
 * 
 * @author duggan
 *
 */
public class MultiRequestActionHandler extends
		BaseActionHandler<MultiRequestAction, MultiRequestActionResult> {

	@Inject
	public MultiRequestActionHandler() {
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void execute(MultiRequestAction action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		List<BaseRequest<BaseResponse>> requests = action.getRequest();
		MultiRequestActionResult result = (MultiRequestActionResult)actionResult;
		
		//execution will follow the order of insertion
		for(BaseRequest request : requests){
			try{
				result.addResponse((BaseResponse)execContext.execute(request));
			}catch(Exception e){
				throw new RuntimeException(e);
			}
			
		}
	}

	@Override
	public Class<MultiRequestAction> getActionType() {
		return MultiRequestAction.class;
	}
}
