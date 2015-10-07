package com.duggan.workflow.shared.requests;


import com.duggan.workflow.shared.responses.BaseResponse;
import com.gwtplatform.dispatch.rpc.shared.ActionImpl;

/**
 * 
 * @author duggan
 *
 * @param <T>
 */
public class BaseRequest<T extends BaseResponse> extends ActionImpl<T>{

	protected long requestcode = System.currentTimeMillis();
	
	/*
	 * Embedded calls are calls executed in one command
	 * to execute another on the server side. As such, a transaction and
	 * all relevant session information have been initialized. 
	 * Initializing these again causes a deadline			 
	*/
	private boolean isEmbedded=false;
	
	public BaseResponse createDefaultActionResponse(){
		return new BaseResponse();
	}
	
	public long getRequestCode(){
		return requestcode;
	}
	
	public void setRequestCode(long requestCode){
		this.requestcode = requestCode;
	}
	
	@Override
	public boolean isSecured() {
		return true;
	}

	public boolean isEmbedded() {
		return isEmbedded;
	}

	public void setEmbedded(boolean isEmbedded) {
		this.isEmbedded = isEmbedded;
	}
}
