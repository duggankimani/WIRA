package com.duggan.workflow.client.service;

import com.duggan.workflow.client.ui.events.ClientDisconnectionEvent;
import com.duggan.workflow.client.ui.events.ErrorEvent;
import com.duggan.workflow.client.ui.events.ProcessingCompletedEvent;
import com.duggan.workflow.client.ui.util.StringUtils;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.exceptions.InvalidSessionException;
import com.google.gwt.http.client.RequestTimeoutException;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;
import com.gwtplatform.dispatch.rpc.shared.ServiceException;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

public abstract class ServiceCallback<T> implements AsyncCallback<T>{


	@Override
	public void onFailure(Throwable caught) {	
	
		if(caught instanceof InvalidSessionException){
			AppContext.destroy();
			AppContext.getPlaceManager().revealPlace(new PlaceRequest.Builder().nameToken("login").build());
			return;
		}
		
		if(caught instanceof ServiceException){
			String msg = "Cannot connect to server...";
			if(getBaseMessage(caught)!=null){
				msg = caught.getMessage();
			}
			AppContext.getEventBus().fireEvent(new ClientDisconnectionEvent(msg+" <i>[ServiceEx]</i>"));
			return;
		}
		
		if(caught instanceof InvocationException){
			String msg = "Cannot connect to server...";
//			if(getBaseMessage(caught)!=null ){
//				msg = StringUtils.isNullOrEmpty(caught.getMessage())? msg : msg+" - "+caught.getMessage();
//			}
			AppContext.getEventBus().fireEvent(new ProcessingCompletedEvent());
			AppContext.getEventBus().fireEvent(new ClientDisconnectionEvent(msg));
			return;
		}
		
		if(caught instanceof RequestTimeoutException){
			//HTTP Request Timeout
			AppContext.getEventBus().fireEvent(new ProcessingCompletedEvent());
			AppContext.getEventBus().fireEvent(new ClientDisconnectionEvent("Cannot connect to server... [RequestTimeoutEx]"));
		}
		
		//caught.printStackTrace();
		
		String message = caught.getMessage();
		
		if(caught.getCause()!=null){
			message = caught.getCause().getMessage();
		}
		
		AppContext.getEventBus().fireEvent(new ProcessingCompletedEvent());
		AppContext.getEventBus().fireEvent(new ErrorEvent(message+" <i>[GeneralException]</i>", 0L));
	}

	@Override
	public void onSuccess(T aResponse) {
		processResult(aResponse);
	}
	
	public abstract void processResult(T aResponse);
	
	public String getBaseMessage(Throwable caught){
		if(caught.getCause()!=null && caught.getCause().getMessage()!=null && !caught.getCause().getMessage().isEmpty()){
			return getBaseMessage(caught);
		}
		
		
		return caught.getMessage();
	}
}
