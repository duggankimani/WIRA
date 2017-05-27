package com.duggan.workflow.server.guice;

import com.duggan.workflow.server.ServerConstants;
import com.duggan.workflow.server.actionhandlers.ActivateAccountActionHandler;
import com.duggan.workflow.server.actionhandlers.ApprovalRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.AssignTaskActionHandler;
import com.duggan.workflow.server.actionhandlers.CheckPasswordRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.CreateDocumentActionHandler;
import com.duggan.workflow.server.actionhandlers.CreateFieldRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.CreateFormRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.DeleteAttachmentRequestHandler;
import com.duggan.workflow.server.actionhandlers.DeleteCatalogRequestHandler;
import com.duggan.workflow.server.actionhandlers.DeleteDSConfigurationEventActionHandler;
import com.duggan.workflow.server.actionhandlers.DeleteDocumentRequestHandler;
import com.duggan.workflow.server.actionhandlers.DeleteFormModelRequestHandler;
import com.duggan.workflow.server.actionhandlers.DeleteLineRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.DeleteNotificationTemplateRequestHandler;
import com.duggan.workflow.server.actionhandlers.DeleteProcessRequestHandler;
import com.duggan.workflow.server.actionhandlers.DeleteTableRowActionHandler;
import com.duggan.workflow.server.actionhandlers.ExecuteTriggerActionHandler;
import com.duggan.workflow.server.actionhandlers.ExecuteTriggersActionHandler;
import com.duggan.workflow.server.actionhandlers.ExecuteWorkflowActionHandler;
import com.duggan.workflow.server.actionhandlers.ExportFormRequestHandler;
import com.duggan.workflow.server.actionhandlers.GenerateFilePathHandler;
import com.duggan.workflow.server.actionhandlers.GenericRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.GetActivitiesRequestHandler;
import com.duggan.workflow.server.actionhandlers.GetAlertCountActionHandler;
import com.duggan.workflow.server.actionhandlers.GetAssignmentRequestHandler;
import com.duggan.workflow.server.actionhandlers.GetAttachmentsRequestHandler;
import com.duggan.workflow.server.actionhandlers.GetCatalogsRequestHandler;
import com.duggan.workflow.server.actionhandlers.GetCommentsRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.GetContextRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.GetDSConfigurationsRequestHandler;
import com.duggan.workflow.server.actionhandlers.GetDSStatusRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.GetDashboardDataRequestHandler;
import com.duggan.workflow.server.actionhandlers.GetDashboardProcessTrendsRequestHandler;
import com.duggan.workflow.server.actionhandlers.GetDataRequestHandler;
import com.duggan.workflow.server.actionhandlers.GetDocumentRequestHandler;
import com.duggan.workflow.server.actionhandlers.GetDocumentTypesRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.GetErrorRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.GetFileTreeRequestHandler;
import com.duggan.workflow.server.actionhandlers.GetFormModelRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.GetFormsRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.GetGroupsRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.GetInitialDocumentRequestHandler;
import com.duggan.workflow.server.actionhandlers.GetItemActionHandler;
import com.duggan.workflow.server.actionhandlers.GetLongTasksRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.GetMessagesActionHandler;
import com.duggan.workflow.server.actionhandlers.GetNotificationTemplateRequestHandler;
import com.duggan.workflow.server.actionhandlers.GetNotificationsActionHandler;
import com.duggan.workflow.server.actionhandlers.GetOrgsRequestHandler;
import com.duggan.workflow.server.actionhandlers.GetOutputDocumentRequestHandler;
import com.duggan.workflow.server.actionhandlers.GetOutputDocumentsRequestHandler;
import com.duggan.workflow.server.actionhandlers.GetPermissionsActionHandler;
import com.duggan.workflow.server.actionhandlers.GetProcessCategoriesRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.GetProcessImportStatusActionHandler;
import com.duggan.workflow.server.actionhandlers.GetProcessInstancesRequestHandler;
import com.duggan.workflow.server.actionhandlers.GetProcessLogRequestHandler;
import com.duggan.workflow.server.actionhandlers.GetProcessRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.GetProcessSchemaActionHandler;
import com.duggan.workflow.server.actionhandlers.GetProcessStatusRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.GetProcessesRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.GetRecentTasksActionHandler;
import com.duggan.workflow.server.actionhandlers.GetSettingsRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.GetTaskCompletionDataActionHandler;
import com.duggan.workflow.server.actionhandlers.GetTaskListActionHandler;
import com.duggan.workflow.server.actionhandlers.GetTaskNodesActionHandler;
import com.duggan.workflow.server.actionhandlers.GetTaskStepTriggersRequestHandler;
import com.duggan.workflow.server.actionhandlers.GetTaskStepsRequestHandler;
import com.duggan.workflow.server.actionhandlers.GetTriggerCountActionHandler;
import com.duggan.workflow.server.actionhandlers.GetTriggersRequestHandler;
import com.duggan.workflow.server.actionhandlers.GetUserRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.GetUsersRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.InsertDataRequestHandler;
import com.duggan.workflow.server.actionhandlers.LoadDynamicFieldsRequestHandler;
import com.duggan.workflow.server.actionhandlers.LoginRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.LogoutActionHandler;
import com.duggan.workflow.server.actionhandlers.ManageKnowledgeBaseResponseHandler;
import com.duggan.workflow.server.actionhandlers.MultiRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.ResetAccountRequestHandler;
import com.duggan.workflow.server.actionhandlers.SaveAssignmentRequestHandler;
import com.duggan.workflow.server.actionhandlers.SaveCatalogRequestHandler;
import com.duggan.workflow.server.actionhandlers.SaveCommentRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.SaveDSConfigRequestHandler;
import com.duggan.workflow.server.actionhandlers.SaveGroupRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.SaveNotificationRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.SaveNotificationTemplateRequestHandler;
import com.duggan.workflow.server.actionhandlers.SaveOrgRequestHandler;
import com.duggan.workflow.server.actionhandlers.SaveOuputDocumentRequestHandler;
import com.duggan.workflow.server.actionhandlers.SaveProcessCategoryRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.SaveProcessRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.SaveSettingsRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.SaveTaskStepRequestHandler;
import com.duggan.workflow.server.actionhandlers.SaveTaskStepTriggerRequestHandler;
import com.duggan.workflow.server.actionhandlers.SaveTriggerRequestHandler;
import com.duggan.workflow.server.actionhandlers.SaveUserRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.SearchDocumentRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.SendMessageActionHandler;
import com.duggan.workflow.server.actionhandlers.StartAllProcessesRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.UpdateNotificationRequestActionHandler;
import com.duggan.workflow.server.actionhandlers.UpdatePasswordRequestActionHandler;
import com.duggan.workflow.server.actionvalidator.SessionValidator;
import com.duggan.workflow.shared.requests.ApprovalRequest;
import com.duggan.workflow.shared.requests.AssignTaskRequest;
import com.duggan.workflow.shared.requests.CheckPasswordRequest;
import com.duggan.workflow.shared.requests.CreateDocumentRequest;
import com.duggan.workflow.shared.requests.CreateFieldRequest;
import com.duggan.workflow.shared.requests.CreateFormRequest;
import com.duggan.workflow.shared.requests.DeleteAttachmentRequest;
import com.duggan.workflow.shared.requests.DeleteCatalogRequest;
import com.duggan.workflow.shared.requests.DeleteDSConfigurationEvent;
import com.duggan.workflow.shared.requests.DeleteDocumentRequest;
import com.duggan.workflow.shared.requests.DeleteFormModelRequest;
import com.duggan.workflow.shared.requests.DeleteLineRequest;
import com.duggan.workflow.shared.requests.DeleteNotificationTemplateRequest;
import com.duggan.workflow.shared.requests.DeleteProcessRequest;
import com.duggan.workflow.shared.requests.DeleteTableRowRequest;
import com.duggan.workflow.shared.requests.ExecuteTriggerRequest;
import com.duggan.workflow.shared.requests.ExecuteTriggersRequest;
import com.duggan.workflow.shared.requests.ExecuteWorkflow;
import com.duggan.workflow.shared.requests.ExportFormRequest;
import com.duggan.workflow.shared.requests.GenerateFilePathRequest;
import com.duggan.workflow.shared.requests.GenericRequest;
import com.duggan.workflow.shared.requests.GetActivitiesRequest;
import com.duggan.workflow.shared.requests.GetAlertCount;
import com.duggan.workflow.shared.requests.GetAssignmentRequest;
import com.duggan.workflow.shared.requests.GetAttachmentsRequest;
import com.duggan.workflow.shared.requests.GetCatalogsRequest;
import com.duggan.workflow.shared.requests.GetCommentsRequest;
import com.duggan.workflow.shared.requests.GetContextRequest;
import com.duggan.workflow.shared.requests.GetDSConfigurationsRequest;
import com.duggan.workflow.shared.requests.GetDSStatusRequest;
import com.duggan.workflow.shared.requests.GetDashboardDataRequest;
import com.duggan.workflow.shared.requests.GetDashboardProcessTrendsRequest;
import com.duggan.workflow.shared.requests.GetDataRequest;
import com.duggan.workflow.shared.requests.GetDocumentRequest;
import com.duggan.workflow.shared.requests.GetDocumentTypesRequest;
import com.duggan.workflow.shared.requests.GetErrorRequest;
import com.duggan.workflow.shared.requests.GetFileTreeRequest;
import com.duggan.workflow.shared.requests.GetFormModelRequest;
import com.duggan.workflow.shared.requests.GetFormsRequest;
import com.duggan.workflow.shared.requests.GetGroupsRequest;
import com.duggan.workflow.shared.requests.GetInitialDocumentRequest;
import com.duggan.workflow.shared.requests.GetItemRequest;
import com.duggan.workflow.shared.requests.GetLongTasksRequest;
import com.duggan.workflow.shared.requests.GetMessagesRequest;
import com.duggan.workflow.shared.requests.GetNotificationTemplateRequest;
import com.duggan.workflow.shared.requests.GetNotificationsAction;
import com.duggan.workflow.shared.requests.GetOrgsRequest;
import com.duggan.workflow.shared.requests.GetOutputDocumentRequest;
import com.duggan.workflow.shared.requests.GetOutputDocumentsRequest;
import com.duggan.workflow.shared.requests.GetPermissionsRequest;
import com.duggan.workflow.shared.requests.GetProcessCategoriesRequest;
import com.duggan.workflow.shared.requests.GetProcessImportStatus;
import com.duggan.workflow.shared.requests.GetProcessInstancesRequest;
import com.duggan.workflow.shared.requests.GetProcessLogRequest;
import com.duggan.workflow.shared.requests.GetProcessRequest;
import com.duggan.workflow.shared.requests.GetProcessSchemaRequest;
import com.duggan.workflow.shared.requests.GetProcessStatusRequest;
import com.duggan.workflow.shared.requests.GetProcessesRequest;
import com.duggan.workflow.shared.requests.GetRecentTasksRequest;
import com.duggan.workflow.shared.requests.GetSettingsRequest;
import com.duggan.workflow.shared.requests.GetTaskCompletionRequest;
import com.duggan.workflow.shared.requests.GetTaskList;
import com.duggan.workflow.shared.requests.GetTaskNodesRequest;
import com.duggan.workflow.shared.requests.GetTaskStepTriggersRequest;
import com.duggan.workflow.shared.requests.GetTaskStepsRequest;
import com.duggan.workflow.shared.requests.GetTriggerCountRequest;
import com.duggan.workflow.shared.requests.GetTriggersRequest;
import com.duggan.workflow.shared.requests.GetUsersRequest;
import com.duggan.workflow.shared.requests.InsertDataRequest;
import com.duggan.workflow.shared.requests.LoadDynamicFieldsRequest;
import com.duggan.workflow.shared.requests.LogoutAction;
import com.duggan.workflow.shared.requests.ManageKnowledgeBaseRequest;
import com.duggan.workflow.shared.requests.MultiRequestAction;
import com.duggan.workflow.shared.requests.SaveAssignmentRequest;
import com.duggan.workflow.shared.requests.SaveCatalogRequest;
import com.duggan.workflow.shared.requests.SaveCommentRequest;
import com.duggan.workflow.shared.requests.SaveDSConfigRequest;
import com.duggan.workflow.shared.requests.SaveGroupRequest;
import com.duggan.workflow.shared.requests.SaveNotificationRequest;
import com.duggan.workflow.shared.requests.SaveNotificationTemplateRequest;
import com.duggan.workflow.shared.requests.SaveOrgRequest;
import com.duggan.workflow.shared.requests.SaveOutputDocumentRequest;
import com.duggan.workflow.shared.requests.SaveProcessCategoryRequest;
import com.duggan.workflow.shared.requests.SaveProcessRequest;
import com.duggan.workflow.shared.requests.SaveSettingsRequest;
import com.duggan.workflow.shared.requests.SaveTaskStepRequest;
import com.duggan.workflow.shared.requests.SaveTaskStepTriggerRequest;
import com.duggan.workflow.shared.requests.SaveTriggerRequest;
import com.duggan.workflow.shared.requests.SaveUserRequest;
import com.duggan.workflow.shared.requests.SearchDocumentRequest;
import com.duggan.workflow.shared.requests.SendMessageRequest;
import com.duggan.workflow.shared.requests.StartAllProcessesRequest;
import com.duggan.workflow.shared.requests.UpdateNotificationRequest;
import com.duggan.workflow.shared.requests.UpdatePasswordRequest;
import com.gwtplatform.dispatch.rpc.server.guice.HandlerModule;
import com.gwtplatform.dispatch.shared.SecurityCookie;
import com.wira.commons.shared.request.GetUserRequest;
import com.wira.login.shared.request.ActivateAccountRequest;
import com.wira.login.shared.request.LoginRequest;
import com.wira.login.shared.request.ResetAccountRequest;

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
				GetAttachmentsRequestHandler.class,
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

		bindHandler(GetDashboardDataRequest.class,
				GetDashboardDataRequestHandler.class,
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
		
		bindHandler(GetTaskNodesRequest.class, GetTaskNodesActionHandler.class, SessionValidator.class);
		
		bindHandler(GetTaskStepsRequest.class, GetTaskStepsRequestHandler.class, SessionValidator.class);
		
		bindHandler(SaveTaskStepRequest.class, SaveTaskStepRequestHandler.class, SessionValidator.class);
		
		bindHandler(SaveTriggerRequest.class, SaveTriggerRequestHandler.class, SessionValidator.class);
		
		bindHandler(GetTriggersRequest.class, GetTriggersRequestHandler.class, SessionValidator.class);
		
		bindHandler(SaveTaskStepTriggerRequest.class, SaveTaskStepTriggerRequestHandler.class, SessionValidator.class);
		
		bindHandler(GetTaskStepTriggersRequest.class, GetTaskStepTriggersRequestHandler.class, SessionValidator.class);
		
		bindHandler(GetTriggerCountRequest.class, GetTriggerCountActionHandler.class, SessionValidator.class);
		
		bindHandler(ExecuteTriggersRequest.class, ExecuteTriggersActionHandler.class, SessionValidator.class);
		
		bindHandler(GetInitialDocumentRequest.class, GetInitialDocumentRequestHandler.class, SessionValidator.class);
		
		bindHandler(AssignTaskRequest.class, AssignTaskActionHandler.class, SessionValidator.class);
		
		bindHandler(GetNotificationTemplateRequest.class, GetNotificationTemplateRequestHandler.class,
				SessionValidator.class);
		
		bindHandler(SaveNotificationTemplateRequest.class, SaveNotificationTemplateRequestHandler.class,
				SessionValidator.class);
		
		bindHandler(GetProcessLogRequest.class, GetProcessLogRequestHandler.class,
				SessionValidator.class);
		bindHandler(GetProcessInstancesRequest.class, GetProcessInstancesRequestHandler.class,
				SessionValidator.class);
		
		bindHandler(SaveCatalogRequest.class, SaveCatalogRequestHandler.class, SessionValidator.class);
		
		bindHandler(GetCatalogsRequest.class, GetCatalogsRequestHandler.class, SessionValidator.class);
		
		bindHandler(DeleteCatalogRequest.class, DeleteCatalogRequestHandler.class, SessionValidator.class);
		
		bindHandler(InsertDataRequest.class, InsertDataRequestHandler.class, SessionValidator.class);
		
		bindHandler(GetDataRequest.class, GetDataRequestHandler.class, SessionValidator.class);
		
		bindHandler(GetProcessCategoriesRequest.class, GetProcessCategoriesRequestActionHandler.class, SessionValidator.class);
		
		bindHandler(SaveProcessCategoryRequest.class, SaveProcessCategoryRequestActionHandler.class, SessionValidator.class);
		
		bindHandler(ExecuteTriggerRequest.class, ExecuteTriggerActionHandler.class, SessionValidator.class);
		
		bindHandler(DeleteNotificationTemplateRequest.class, DeleteNotificationTemplateRequestHandler.class, SessionValidator.class);
		
		bindHandler(GetMessagesRequest.class, GetMessagesActionHandler.class, SessionValidator.class);
		
		bindHandler(SendMessageRequest.class, SendMessageActionHandler.class, SessionValidator.class);
		
		bindHandler(LoadDynamicFieldsRequest.class, LoadDynamicFieldsRequestHandler.class, SessionValidator.class);
		
		bindHandler(SaveOrgRequest.class,
				SaveOrgRequestHandler.class, SessionValidator.class);
		
		bindHandler(GetOrgsRequest.class,
				GetOrgsRequestHandler.class, SessionValidator.class);
		
		bindHandler(GetPermissionsRequest.class,
				GetPermissionsActionHandler.class, SessionValidator.class);

		bindHandler(GetAssignmentRequest.class,
				GetAssignmentRequestHandler.class, SessionValidator.class);
		
		bindHandler(SaveAssignmentRequest.class,
				SaveAssignmentRequestHandler.class, SessionValidator.class);
		
		bindHandler(GetFileTreeRequest.class, GetFileTreeRequestHandler.class, SessionValidator.class);
		
		bindHandler(GenerateFilePathRequest.class, GenerateFilePathHandler.class, SessionValidator.class);
		
		bindHandler(GetOutputDocumentRequest.class, GetOutputDocumentRequestHandler.class, SessionValidator.class);
		
		bindHandler(ResetAccountRequest.class, ResetAccountRequestHandler.class);
		
		bindHandler(ActivateAccountRequest.class, ActivateAccountActionHandler.class);
		
		bindHandler(GetRecentTasksRequest.class, GetRecentTasksActionHandler.class, SessionValidator.class);
		
		bindHandler(GetProcessSchemaRequest.class, GetProcessSchemaActionHandler.class, SessionValidator.class);
		
		bindHandler(GetProcessImportStatus.class, GetProcessImportStatusActionHandler.class, SessionValidator.class);
		
		bindHandler(DeleteTableRowRequest.class, DeleteTableRowActionHandler.class, SessionValidator.class);
		
		bindHandler(GetDashboardProcessTrendsRequest.class, GetDashboardProcessTrendsRequestHandler.class, SessionValidator.class);
	}
}
