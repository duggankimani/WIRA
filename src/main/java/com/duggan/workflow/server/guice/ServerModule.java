package com.duggan.workflow.server.guice;

import com.duggan.workflow.server.ServerConstants;
import com.duggan.workflow.server.actionhandlers.ApprovalRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.CheckPasswordRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.CreateDocumentActionHandler;
import com.duggan.workflow.server.actionhandlers.CreateFieldRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.CreateFormRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.DeleteAttachmentRequestHandler;
import com.duggan.workflow.server.actionhandlers.DeleteDSConfigurationEventActionHandler;
import com.duggan.workflow.server.actionhandlers.DeleteDocumentRequestHandler;
import com.duggan.workflow.server.actionhandlers.DeleteFormModelRequestHandler;
import com.duggan.workflow.server.actionhandlers.DeleteLineRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.DeleteProcessRequestHandler;
import com.duggan.workflow.server.actionhandlers.ExecuteWorkflowActionHandler;
import com.duggan.workflow.server.actionhandlers.ExportFormRequestHandler;
import com.duggan.workflow.server.actionhandlers.GenericRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.GetActivitiesRequestHandler;
import com.duggan.workflow.server.actionhandlers.GetAlertCountActionHandler;
import com.duggan.workflow.server.actionhandlers.GetAttachmentsRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.GetCommentsRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.GetContextRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.GetDSConfigurationsRequestHandler;
import com.duggan.workflow.server.actionhandlers.GetDSStatusRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.GetDashBoardDataRequestHandler;
import com.duggan.workflow.server.actionhandlers.GetDocumentRequestHandler;
import com.duggan.workflow.server.actionhandlers.GetDocumentTypesRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.GetErrorRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.GetFormModelRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.GetFormsRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.GetGroupsRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.GetItemActionHandler;
import com.duggan.workflow.server.actionhandlers.GetLongTasksRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.GetNotificationsActionHandler;
import com.duggan.workflow.server.actionhandlers.GetOutputDocumentsRequestHandler;
import com.duggan.workflow.server.actionhandlers.GetProcessRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.GetProcessStatusRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.GetProcessesRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.GetSettingsRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.GetTaskCompletionDataActionHandler;
import com.duggan.workflow.server.actionhandlers.GetTaskListActionHandler;
import com.duggan.workflow.server.actionhandlers.GetUserRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.GetUsersRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.LoginRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.LogoutActionHandler;
import com.duggan.workflow.server.actionhandlers.ManageKnowledgeBaseResponseHandler;
import com.duggan.workflow.server.actionhandlers.MultiRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.SaveCommentRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.SaveDSConfigRequestHandler;
import com.duggan.workflow.server.actionhandlers.SaveGroupRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.SaveNotificationRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.SaveOuputDocumentRequestHandler;
import com.duggan.workflow.server.actionhandlers.SaveProcessRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.SaveSettingsRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.SaveUserRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.SearchDocumentRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.StartAllProcessesRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.UpdateNotificationRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.UpdatePasswordRequestActionHandler;
import com.duggan.workflow.server.actionvalidator.SessionValidator;
import com.duggan.workflow.shared.requests.ApprovalRequest;
import com.duggan.workflow.shared.requests.CheckPasswordRequest;
import com.duggan.workflow.shared.requests.CreateDocumentRequest;
import com.duggan.workflow.shared.requests.CreateFieldRequest;
import com.duggan.workflow.shared.requests.CreateFormRequest;
import com.duggan.workflow.shared.requests.DeleteAttachmentRequest;
import com.duggan.workflow.shared.requests.DeleteDSConfigurationEvent;
import com.duggan.workflow.shared.requests.DeleteDocumentRequest;
import com.duggan.workflow.shared.requests.DeleteFormModelRequest;
import com.duggan.workflow.shared.requests.DeleteLineRequest;
import com.duggan.workflow.shared.requests.DeleteProcessRequest;
import com.duggan.workflow.shared.requests.ExecuteWorkflow;
import com.duggan.workflow.shared.requests.ExportFormRequest;
import com.duggan.workflow.shared.requests.GenericRequest;
import com.duggan.workflow.shared.requests.GetActivitiesRequest;
import com.duggan.workflow.shared.requests.GetAlertCount;
import com.duggan.workflow.shared.requests.GetAttachmentsRequest;
import com.duggan.workflow.shared.requests.GetCommentsRequest;
import com.duggan.workflow.shared.requests.GetContextRequest;
import com.duggan.workflow.shared.requests.GetDSConfigurationsRequest;
import com.duggan.workflow.shared.requests.GetDSStatusRequest;
import com.duggan.workflow.shared.requests.GetDashBoardDataRequest;
import com.duggan.workflow.shared.requests.GetDocumentRequest;
import com.duggan.workflow.shared.requests.GetDocumentTypesRequest;
import com.duggan.workflow.shared.requests.GetErrorRequest;
import com.duggan.workflow.shared.requests.GetFormModelRequest;
import com.duggan.workflow.shared.requests.GetFormsRequest;
import com.duggan.workflow.shared.requests.GetGroupsRequest;
import com.duggan.workflow.shared.requests.GetItemRequest;
import com.duggan.workflow.shared.requests.GetLongTasksRequest;
import com.duggan.workflow.shared.requests.GetNotificationsAction;
import com.duggan.workflow.shared.requests.GetOutputDocumentsRequest;
import com.duggan.workflow.shared.requests.GetProcessRequest;
import com.duggan.workflow.shared.requests.GetProcessStatusRequest;
import com.duggan.workflow.shared.requests.GetProcessesRequest;
import com.duggan.workflow.shared.requests.GetSettingsRequest;
import com.duggan.workflow.shared.requests.GetTaskCompletionRequest;
import com.duggan.workflow.shared.requests.GetTaskList;
import com.duggan.workflow.shared.requests.GetUserRequest;
import com.duggan.workflow.shared.requests.GetUsersRequest;
import com.duggan.workflow.shared.requests.LoginRequest;
import com.duggan.workflow.shared.requests.LogoutAction;
import com.duggan.workflow.shared.requests.ManageKnowledgeBaseRequest;
import com.duggan.workflow.shared.requests.MultiRequestAction;
import com.duggan.workflow.shared.requests.SaveCommentRequest;
import com.duggan.workflow.shared.requests.SaveDSConfigRequest;
import com.duggan.workflow.shared.requests.SaveGroupRequest;
import com.duggan.workflow.shared.requests.SaveNotificationRequest;
import com.duggan.workflow.shared.requests.SaveOutputDocumentRequest;
import com.duggan.workflow.shared.requests.SaveProcessRequest;
import com.duggan.workflow.shared.requests.SaveSettingsRequest;
import com.duggan.workflow.shared.requests.SaveUserRequest;
import com.duggan.workflow.shared.requests.SearchDocumentRequest;
import com.duggan.workflow.shared.requests.StartAllProcessesRequest;
import com.duggan.workflow.shared.requests.UpdateNotificationRequest;
import com.duggan.workflow.shared.requests.UpdatePasswordRequest;
import com.duggan.workflow.shared.responses.SaveOutputDocumentResponse;
import com.gwtplatform.dispatch.rpc.server.guice.HandlerModule;
import com.gwtplatform.dispatch.shared.SecurityCookie;

public class ServerModule extends HandlerModule {

	@Override
	protected void configureHandlers() {

		bindHandler(ApprovalRequest.class, ApprovalRequestActionHandler.class, SessionValidator.class);

		bindHandler(GetTaskList.class, GetTaskListActionHandler.class, SessionValidator.class);
		
		bindConstant().annotatedWith(SecurityCookie.class).to(ServerConstants.AUTHENTICATIONCOOKIE);

		bindHandler(ExecuteWorkflow.class, ExecuteWorkflowActionHandler.class, SessionValidator.class);

		bindHandler(GetItemRequest.class, GetItemActionHandler.class, SessionValidator.class);

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

		bindHandler(GetNotificationsAction.class,
				GetNotificationsActionHandler.class,
				SessionValidator.class);

		bindHandler(SearchDocumentRequest.class,
				SearchDocumentRequestActionHandler.class,
				SessionValidator.class);

		bindHandler(UpdateNotificationRequest.class,
				UpdateNotificationRequestActionHandler.class,
				SessionValidator.class);

		bindHandler(SaveCommentRequest.class,
				SaveCommentRequestActionHandler.class, SessionValidator.class);

		bindHandler(MultiRequestAction.class,
				MultiRequestActionHandler.class, SessionValidator.class);

		bindHandler(GetCommentsRequest.class,
				GetCommentsRequestActionHandler.class, SessionValidator.class);

		bindHandler(GetProcessStatusRequest.class,
				GetProcessStatusRequestActionHandler.class,
				SessionValidator.class);

		bindHandler(GetAttachmentsRequest.class,
				GetAttachmentsRequestActionHandler.class,
				SessionValidator.class);

		bindHandler(GetActivitiesRequest.class,
				GetActivitiesRequestHandler.class, SessionValidator.class);

		bindHandler(SaveProcessRequest.class,
				SaveProcessRequestActionHandler.class, SessionValidator.class);

		bindHandler(DeleteProcessRequest.class,
				DeleteProcessRequestHandler.class, SessionValidator.class);

		bindHandler(GetProcessesRequest.class,
				GetProcessesRequestActionHandler.class, SessionValidator.class);

		bindHandler(GetProcessRequest.class,
				GetProcessRequestActionHandler.class, SessionValidator.class);

		bindHandler(ManageKnowledgeBaseRequest.class,
				ManageKnowledgeBaseResponseHandler.class,
				SessionValidator.class);

		bindHandler(SaveUserRequest.class, SaveUserRequestActionHandler.class,
				SessionValidator.class);

		bindHandler(SaveGroupRequest.class,
				SaveGroupRequestActionHandler.class, SessionValidator.class);

		bindHandler(GetUsersRequest.class, GetUsersRequestActionHandler.class,
				SessionValidator.class);

		bindHandler(GetGroupsRequest.class,
				GetGroupsRequestActionHandler.class, SessionValidator.class);

		bindHandler(GetFormModelRequest.class,
				GetFormModelRequestActionHandler.class, SessionValidator.class);

		bindHandler(GetDocumentTypesRequest.class,
				GetDocumentTypesRequestActionHandler.class,
				SessionValidator.class);

		bindHandler(CreateFormRequest.class,
				CreateFormRequestActionHandler.class, SessionValidator.class);

		bindHandler(CreateFieldRequest.class,
				CreateFieldRequestActionHandler.class, SessionValidator.class);

		bindHandler(GetFormsRequest.class, GetFormsRequestActionHandler.class,
				SessionValidator.class);

		bindHandler(DeleteFormModelRequest.class,
				DeleteFormModelRequestHandler.class,
				SessionValidator.class);

		bindHandler(DeleteLineRequest.class,
				DeleteLineRequestActionHandler.class, SessionValidator.class);

		bindHandler(SaveNotificationRequest.class,
				SaveNotificationRequestActionHandler.class,
				SessionValidator.class);

		bindHandler(DeleteAttachmentRequest.class,
				DeleteAttachmentRequestHandler.class,
				SessionValidator.class);

		bindHandler(StartAllProcessesRequest.class,
				StartAllProcessesRequestActionHandler.class,
				SessionValidator.class);

		bindHandler(DeleteDocumentRequest.class,
				DeleteDocumentRequestHandler.class,
				SessionValidator.class);

		bindHandler(SaveDSConfigRequest.class,
				SaveDSConfigRequestHandler.class, SessionValidator.class);

		bindHandler(GetDSConfigurationsRequest.class,
				GetDSConfigurationsRequestHandler.class,
				SessionValidator.class);

		bindHandler(DeleteDSConfigurationEvent.class,
				DeleteDSConfigurationEventActionHandler.class,
				SessionValidator.class);

		bindHandler(ExportFormRequest.class,
				ExportFormRequestHandler.class, SessionValidator.class);


		bindHandler(GetDSStatusRequest.class,
				GetDSStatusRequestActionHandler.class, SessionValidator.class);

		bindHandler(CheckPasswordRequest.class,
				CheckPasswordRequestActionHandler.class, SessionValidator.class);

		bindHandler(GetUserRequest.class, GetUserRequestActionHandler.class,
				SessionValidator.class);

		bindHandler(UpdatePasswordRequest.class,
				UpdatePasswordRequestActionHandler.class,
				SessionValidator.class);

		bindHandler(GetDashBoardDataRequest.class,
				GetDashBoardDataRequestHandler.class,
				SessionValidator.class);

		bindHandler(GetLongTasksRequest.class,
				GetLongTasksRequestActionHandler.class, SessionValidator.class);

		bindHandler(GetTaskCompletionRequest.class,
				GetTaskCompletionDataActionHandler.class,
				SessionValidator.class);

		bindHandler(GetSettingsRequest.class,
				GetSettingsRequestActionHandler.class, SessionValidator.class);

		bindHandler(SaveSettingsRequest.class,
				SaveSettingsRequestActionHandler.class, SessionValidator.class);

		bindHandler(GenericRequest.class, GenericRequestActionHandler.class,
				SessionValidator.class);
		
		bindHandler(SaveOutputDocumentRequest.class,SaveOuputDocumentRequestHandler.class, 
				SessionValidator.class);
		
		bindHandler(GetOutputDocumentsRequest.class, GetOutputDocumentsRequestHandler.class, SessionValidator.class);
	}
}
