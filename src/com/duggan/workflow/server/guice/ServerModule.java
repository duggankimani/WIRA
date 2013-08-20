package com.duggan.workflow.server.guice;

import com.gwtplatform.dispatch.server.guice.HandlerModule;
import com.gwtplatform.dispatch.shared.SecurityCookie;
import com.duggan.workflow.server.ServerConstants;
import com.duggan.workflow.server.actionhandlers.ApprovalRequestActionHandler;
import com.duggan.workflow.shared.requests.ApprovalRequest;
import com.duggan.workflow.shared.requests.GetErrorRequest;
import com.duggan.workflow.shared.requests.GetTask;
import com.duggan.workflow.shared.requests.GetTaskList;
import com.duggan.workflow.server.actionhandlers.GetTaskListActionHandler;
import com.duggan.workflow.shared.requests.ExecuteWorkflow;
import com.duggan.workflow.server.actionhandlers.ExecuteWorkflowActionHandler;
import com.duggan.workflow.server.actionhandlers.GetTaskActionHandler;
import com.duggan.workflow.server.actionhandlers.MultiRequestActionHandler;
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
import com.duggan.workflow.server.actionhandlers.GetErrorRequestActionHandler;
import com.duggan.workflow.shared.requests.GetNotificationCount;
import com.duggan.workflow.server.actionhandlers.GetNotificationCountActionHandler;
import com.duggan.workflow.shared.requests.GetNotificationsAction;
import com.duggan.workflow.server.actionhandlers.GetNotificationsActionHandler;
import com.duggan.workflow.shared.requests.SearchDocumentRequest;
import com.duggan.workflow.server.actionhandlers.SearchDocumentRequestActionHandler;
import com.duggan.workflow.shared.requests.SaveNotificationRequest;
import com.duggan.workflow.server.actionhandlers.SaveNotificationRequestActionHandler;
import com.duggan.workflow.shared.requests.SaveCommentRequest;
import com.duggan.workflow.server.actionhandlers.SaveCommentRequestActionHandler;
import com.duggan.workflow.shared.requests.MultiRequestAction;
import com.duggan.workflow.shared.requests.GetCommentsRequest;
import com.duggan.workflow.server.actionhandlers.GetCommentsRequestActionHandler;

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

		bindHandler(GetErrorRequest.class, GetErrorRequestActionHandler.class,
				SessionValidator.class);

		bindHandler(GetNotificationCount.class,
				GetNotificationCountActionHandler.class, SessionValidator.class);

		bindHandler(GetNotificationsAction.class,
				GetNotificationsActionHandler.class,
				SessionValidator.class);

		bindHandler(SearchDocumentRequest.class,
				SearchDocumentRequestActionHandler.class,
				SessionValidator.class);

		bindHandler(SaveNotificationRequest.class,
				SaveNotificationRequestActionHandler.class,
				SessionValidator.class);

		bindHandler(SaveCommentRequest.class,
				SaveCommentRequestActionHandler.class, SessionValidator.class);

		bindHandler(MultiRequestAction.class,
				MultiRequestActionHandler.class, SessionValidator.class);

		bindHandler(GetCommentsRequest.class,
				GetCommentsRequestActionHandler.class, SessionValidator.class);
	}
}
