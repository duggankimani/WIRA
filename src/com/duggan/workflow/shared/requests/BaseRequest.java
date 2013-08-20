package com.duggan.workflow.shared.requests;


import com.duggan.workflow.shared.responses.BaseResponse;
import com.gwtplatform.dispatch.shared.ActionImpl;

/**
 * 
 * @author duggan
 *
 * @param <T>
 */
public class BaseRequest<T extends BaseResponse> extends ActionImpl<T>{
	

	public BaseResponse createDefaultActionResponse(){
		return new BaseResponse();
	}
	
	@Override
	public boolean isSecured() {
		return true;
	}
}
