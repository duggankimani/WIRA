package com.duggan.workflow.client.service;

import com.duggan.workflow.client.ui.events.ErrorEvent;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.gwtplatform.dispatch.shared.Result;

/**
 * 
 * @author duggan
 *
 * @param <T>
 */
public abstract class TaskServiceCallback<T extends Result> extends ServiceCallback<T> {

	@Override
	public void onSuccess(T result) {
		BaseResponse baseResult = (BaseResponse)result;
		
		if(baseResult.getErrorCode()==0){
			processResult(result);
		}else{
			//throw error
			AppContext.getEventBus().fireEvent(new ErrorEvent(baseResult.getErrorMessage(), baseResult.getErrorId()));
		}
			
	}
}
