package com.duggan.workflow.client.service;

import com.duggan.workflow.client.ui.events.ErrorEvent;
import com.duggan.workflow.client.ui.events.ProcessingCompletedEvent;
import com.duggan.workflow.client.util.AppContext;
import com.google.gwt.user.client.Window;
import com.gwtplatform.dispatch.rpc.shared.Result;
import com.gwtplatform.dispatch.rpc.shared.ServiceException;
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
	public void onFailure(Throwable caught) {	
		if(caught instanceof ServiceException){
			 ServiceException e = (ServiceException)caught;
			 if(e.getMessage()!=null && e.getMessage().startsWith("Cookie provided by RPC doesn't match")){
				 Window.Location.assign("/login.html");
				//AppContext.getEventBus().fireEvent(new ProcessingCompletedEvent());
			 }
		}else{
			super.onFailure(caught);
		}
		

		AppContext.getEventBus().fireEvent(new ProcessingCompletedEvent());
	}
	
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
