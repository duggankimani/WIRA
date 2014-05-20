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
}
