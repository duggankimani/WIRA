package com.duggan.workflow.shared.requests;


import com.duggan.workflow.shared.responses.BaseResult;
import com.gwtplatform.dispatch.shared.ActionImpl;

/**
 * 
 * @author duggan
 *
 * @param <T>
 */
public class BaseRequest<T extends BaseResult> extends ActionImpl<T>{
	

	public BaseResult createDefaultActionResponse(){
		return new BaseResult();
	}
	
	@Override
	public boolean isSecured() {
		return true;
	}
}
