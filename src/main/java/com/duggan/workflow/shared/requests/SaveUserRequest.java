package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.responses.SaveUserResponse;
import com.wira.commons.shared.models.HTUser;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

/**
 * 
 * @author duggan
 *
 */
public class SaveUserRequest extends BaseRequest<SaveUserResponse> {

	private HTUser user;
	private boolean isSendActivationEmail;
	private boolean isDelete=false;

	@SuppressWarnings("unused")
	private SaveUserRequest() {
	}

	public SaveUserRequest(HTUser user) {
		this.user = user;
	}
	
	public SaveUserRequest(HTUser user, boolean isSendActivationEmail) {
		this.user = user;
		this.isSendActivationEmail = isSendActivationEmail;
	}

	public HTUser getUser() {
		return user;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
	
		return new SaveUserResponse();
	}

	public boolean isDelete() {
		return isDelete;
	}

	public void setDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}

	public boolean isSendActivationEmail() {
		return isSendActivationEmail;
	}

	public void setSendActivationEmail(boolean isSendActivationEmail) {
		this.isSendActivationEmail = isSendActivationEmail;
	}
}
