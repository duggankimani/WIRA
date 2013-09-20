package com.duggan.workflow.client.service;

import com.duggan.workflow.client.ui.events.ErrorEvent;
import com.duggan.workflow.client.ui.events.ProcessingCompletedEvent;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.exceptions.InvalidSessionException;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;

public abstract class ServiceCallback<T> implements AsyncCallback<T>{


	@Override
	public void onFailure(Throwable caught) {	
		
		if(caught instanceof InvalidSessionException){
			AppContext.destroy();
			AppContext.getPlaceManager().revealPlace(new PlaceRequest("login"));
			return;
		}
		
		//caught.printStackTrace();
		
		String message = caught.getMessage();
		
		if(caught.getCause()!=null)
			message = caught.getCause().getMessage();
		AppContext.getEventBus().fireEvent(new ProcessingCompletedEvent());
		AppContext.getEventBus().fireEvent(new ErrorEvent("[C500] "+message, 0L));
	}

	@Override
	public void onSuccess(T result) {
		processResult(result);
	}
	
	public abstract void processResult(T result);
	
}
