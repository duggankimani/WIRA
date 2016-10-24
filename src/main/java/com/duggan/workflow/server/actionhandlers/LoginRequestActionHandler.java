package com.duggan.workflow.server.actionhandlers;

import java.util.ArrayList;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.duggan.workflow.server.ServerConstants;
import com.duggan.workflow.server.dao.helper.SettingsDaoHelper;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.helper.auth.LoginHelper;
import com.duggan.workflow.server.helper.auth.UserDaoHelper;
import com.duggan.workflow.server.helper.jbpm.VersionManager;
import com.duggan.workflow.shared.model.settings.SETTINGNAME;
import com.duggan.workflow.shared.model.settings.Setting;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.models.CurrentUserDto;
import com.wira.commons.shared.models.HTUser;
import com.wira.commons.shared.models.PermissionPOJO;
import com.wira.commons.shared.models.REPORTVIEWIMPL;
import com.wira.commons.shared.models.UserGroup;
import com.wira.commons.shared.response.BaseResponse;
import com.wira.login.shared.model.ActionType;
import com.wira.login.shared.request.LoginRequest;
import com.wira.login.shared.response.LoginRequestResult;

public class LoginRequestActionHandler extends
		AbstractActionHandler<LoginRequest, LoginRequestResult> {

	private final Provider<HttpServletRequest> httpRequest;

	Logger logger = Logger.getLogger(LoginRequestActionHandler.class);
	@Inject
	Provider<HttpServletRequest> request;

	@Inject
	Provider<HttpServletResponse> response;


	@Inject
	public LoginRequestActionHandler(
			final Provider<HttpServletRequest> httpRequest) {
		this.httpRequest = httpRequest;
	}

	@Override
	public void execute(LoginRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		LoginRequestResult result = (LoginRequestResult) actionResult;
		new UserDaoHelper().login(action,result);
	}

	@Override
	public void undo(LoginRequest action, LoginRequestResult result,
			ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<LoginRequest> getActionType() {
		return LoginRequest.class;
	}
}
