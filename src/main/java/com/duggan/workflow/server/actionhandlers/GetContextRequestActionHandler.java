package com.duggan.workflow.server.actionhandlers;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.duggan.workflow.server.ServerConstants;
import com.duggan.workflow.server.dao.helper.SettingsDaoHelper;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.helper.auth.LoginHelper;
import com.duggan.workflow.server.helper.jbpm.VersionManager;
import com.duggan.workflow.server.helper.session.SessionHelper;
import com.duggan.workflow.shared.model.settings.SETTINGNAME;
import com.duggan.workflow.shared.model.settings.Setting;
import com.duggan.workflow.shared.requests.GetContextRequest;
import com.duggan.workflow.shared.responses.GetContextRequestResult;
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

public class GetContextRequestActionHandler extends 
		AbstractActionHandler<GetContextRequest, GetContextRequestResult> {

	private final Provider<HttpServletRequest> httpRequest;

	@Inject
	public GetContextRequestActionHandler(Provider<HttpServletRequest> httpRequest) {
		this.httpRequest= httpRequest;
	}

	@Override
	public void execute(GetContextRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		
		Subject subject = SecurityUtils.getSubject();
		HTUser user = SessionHelper.getCurrentUser();

		GetContextRequestResult result = (GetContextRequestResult)actionResult;
		result.setIsValid(subject.isAuthenticated() || subject.isRemembered());
		
		if(result.getIsValid()){
			HTUser userDto = (HTUser)user;
			userDto.setGroups((ArrayList<UserGroup>) LoginHelper.get().getGroupsForUser(userDto.getUserId()));
			userDto.setPermissions((ArrayList<PermissionPOJO>) DB.getPermissionDao().getPermissionsForUser(userDto.getUserId()));
			
			CurrentUserDto currentUserDto = new CurrentUserDto(true, userDto);
			result.setCurrentUserDto(currentUserDto);
			result.setVersion(VersionManager.getVersion());
			
		}
		
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
