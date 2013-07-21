package com.duggan.workflow.server.guice;

import com.gwtplatform.dispatch.server.guice.HandlerModule;
import com.gwtplatform.dispatch.shared.SecurityCookie;
import com.duggan.workflow.server.actionhandlers.ApprovalRequestActionHandler;
import com.duggan.workflow.shared.requests.ApprovalRequest;
import com.duggan.workflow.shared.requests.GetTask;
import com.duggan.workflow.shared.requests.GetTaskList;
import com.duggan.workflow.server.actionhandlers.GetTaskListActionHandler;
import com.duggan.workflow.shared.requests.ExecuteWorkflow;
import com.duggan.workflow.server.actionhandlers.ExecuteWorkflowActionHandler;
import com.duggan.workflow.server.actionhandlers.GetTaskActionHandler;
import com.duggan.workflow.server.actionhandlers.ServerConstants;
import com.duggan.workflow.shared.requests.CreateDocumentRequest;
import com.duggan.workflow.server.actionhandlers.CreateDocumentActionHandler;
import com.duggan.workflow.shared.requests.GetDocumentRequest;
import com.duggan.workflow.server.actionhandlers.GetDocumentRequestHandler;
import com.duggan.workflow.shared.requests.LoginRequest;
import com.duggan.workflow.server.actionhandlers.LoginRequestActionHandler;
import com.duggan.workflow.shared.requests.GetContextRequest;
import com.duggan.workflow.server.actionhandlers.GetContextRequestActionHandler;
import com.duggan.workflow.shared.requests.LogoutAction;
import com.duggan.workflow.server.actionhandlers.LogoutActionHandler;
import com.duggan.workflow.server.actionvalidator.SessionValidator;
import com.duggan.workflow.shared.requests.GetAlertCount;
import com.duggan.workflow.server.actionhandlers.GetAlertCountActionHandler;

public class ServerModule extends HandlerModule {

	@Override
	protected void configureHandlers() {

		bindHandler(ApprovalRequest.class, ApprovalRequestActionHandler.class, SessionValidator.class);

		bindHandler(GetTaskList.class, GetTaskListActionHandler.class, SessionValidator.class);
		
		bindConstant().annotatedWith(SecurityCookie.class).to(ServerConstants.AUTHENTICATIONCOOKIE);

		bindHandler(ExecuteWorkflow.class, ExecuteWorkflowActionHandler.class, SessionValidator.class);

		bindHandler(GetTask.class, GetTaskActionHandler.class, SessionValidator.class);

		bindHandler(CreateDocumentRequest.class,
				CreateDocumentActionHandler.class, SessionValidator.class);

		bindHandler(GetDocumentRequest.class,
				GetDocumentRequestHandler.class, SessionValidator.class);

		bindHandler(LoginRequest.class, LoginRequestActionHandler.class);

		bindHandler(GetContextRequest.class,
				GetContextRequestActionHandler.class, SessionValidator.class);

		bindHandler(LogoutAction.class, LogoutActionHandler.class, SessionValidator.class);

		bindHandler(GetAlertCount.class, GetAlertCountActionHandler.class,
				SessionValidator.class);
	}
}
