package com.duggan.workflow.client.service;

import com.duggan.workflow.client.ui.events.ErrorEvent;
import com.duggan.workflow.client.ui.events.ProcessingCompletedEvent;
import com.duggan.workflow.client.util.AppContext;
import com.gwtplatform.dispatch.rpc.shared.Result;
import com.wira.commons.client.service.ServiceCallback;
import com.wira.commons.shared.response.BaseResponse;

/**
 * 
 * @author duggan
 *
 * @param <T>
 */
public abstract class TaskServiceCallback<T extends Result> extends ServiceCallback<T> {

	@Override
	public void onSuccess(T aResponse) {
		BaseResponse baseResult = (BaseResponse)aResponse;
		
		if(baseResult.getErrorCode()==0){
			processResult(aResponse);
		}else{
			//throw error
			AppContext.getEventBus().fireEvent(new ProcessingCompletedEvent());
			AppContext.getEventBus().fireEvent(new ErrorEvent(baseResult.getErrorMessage(), baseResult.getErrorId()));
		}
			
	}
}
