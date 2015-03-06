package com.duggan.workflow.server.actionhandlers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.duggan.workflow.server.ServerConstants;
import com.duggan.workflow.server.dao.helper.SettingsDaoHelper;
import com.duggan.workflow.server.helper.auth.LoginHelper;
import com.duggan.workflow.server.helper.jbpm.VersionManager;
import com.duggan.workflow.shared.model.HTUser;
import com.duggan.workflow.shared.model.settings.REPORTVIEWIMPL;
import com.duggan.workflow.shared.model.settings.SETTINGNAME;
import com.duggan.workflow.shared.model.settings.Setting;
import com.duggan.workflow.shared.requests.GetContextRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetContextRequestResult;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetContextRequestActionHandler extends 
		BaseActionHandler<GetContextRequest, GetContextRequestResult> {

	private final Provider<HttpServletRequest> httpRequest;

	@Inject
	public GetContextRequestActionHandler(Provider<HttpServletRequest> httpRequest) {
		this.httpRequest= httpRequest;
	}

	@Override
	public void execute(GetContextRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		
		HttpSession session = httpRequest.get().getSession(false);
		
		//Object sessionid=session.getAttribute(ServerConstants.AUTHENTICATIONCOOKIE);
		Object user = session.getAttribute(ServerConstants.USER);

		GetContextRequestResult result = (GetContextRequestResult)actionResult;
		result.setIsValid(session!=null && user!=null);
		
		if(result.getIsValid()){
			result.setUser((HTUser)user);
			result.setGroups(LoginHelper.get().getGroupsForUser(result.getUser().getUserId()));
		}
		
		result.setVersion(VersionManager.getVersion());
		
		Setting setting = SettingsDaoHelper.getSetting(SETTINGNAME.ORGNAME);
		if(setting!=null){
			Object value = setting.getValue().getValue();
			result.setOrganizationName(value==null? null: value.toString());
		}
				
		Setting reportView = SettingsDaoHelper.getSetting(SETTINGNAME.REPORT_VIEW_IMPL);
		if(reportView!=null && reportView.getValue()!=null && reportView.getValue().getValue()!=null){
			result.setReportViewImpl(REPORTVIEWIMPL.valueOf(reportView.getValue().getValue().toString()));
		}
			
	}

	@Override
	public void undo(GetContextRequest action, GetContextRequestResult result,
			ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<GetContextRequest> getActionType() {
		return GetContextRequest.class;
	}
}
