package com.wira.commons.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class ServiceCallback<T> implements AsyncCallback<T>{

	@Override
	public void onFailure(Throwable caught) {
		
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
