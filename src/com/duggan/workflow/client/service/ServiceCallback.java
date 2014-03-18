package com.duggan.workflow.client.service;

import com.duggan.workflow.client.ui.events.ClientDisconnectionEvent;
import com.duggan.workflow.client.ui.events.ErrorEvent;
import com.duggan.workflow.client.ui.events.ProcessingCompletedEvent;
import com.duggan.workflow.client.ui.events.ProcessingEvent;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.exceptions.InvalidSessionException;
import com.google.gwt.http.client.RequestTimeoutException;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;
import com.gwtplatform.dispatch.shared.ServiceException;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;

public abstract class ServiceCallback<T> implements AsyncCallback<T>{


	@Override
	public void onFailure(Throwable caught) {	
		if(caught instanceof InvalidSessionException){
			AppContext.destroy();
			AppContext.getPlaceManager().revealPlace(new PlaceRequest("login"));
			return;
		}
		
		if(caught instanceof ServiceException){
			String msg = "Cannot connect to server...";
			if(caught.getMessage()!=null && caught.getMessage().length()>5){
				msg = caught.getMessage();
			}
			AppContext.getEventBus().fireEvent(new ClientDisconnectionEvent(msg));
			return;
		}
		
		if(caught instanceof InvocationException){
			String msg = "Cannot connect to server...";
			if(caught.getMessage()!=null && caught.getMessage().length()>5){
				msg = caught.getMessage();
			}
			AppContext.getEventBus().fireEvent(new ProcessingCompletedEvent());
			AppContext.getEventBus().fireEvent(new ClientDisconnectionEvent(msg));
			return;
		}
		
		if(caught instanceof RequestTimeoutException){
			//HTTP Request Timeout
			AppContext.getEventBus().fireEvent(new ProcessingCompletedEvent());
			AppContext.getEventBus().fireEvent(new ClientDisconnectionEvent("Cannot connect to server..."));
		}
		
		//caught.printStackTrace();
		
		String message = caught.getMessage();
		
		if(caught.getCause()!=null){
			message = caught.getCause().getMessage();
		}
		
		AppContext.getEventBus().fireEvent(new ProcessingCompletedEvent());
		AppContext.getEventBus().fireEvent(new ErrorEvent(message, 0L));
	}

	@Override
	public void onSuccess(T aResponse) {
		processResult(aResponse);
	}
	
	public abstract void processResult(T aResponse);
	
}
