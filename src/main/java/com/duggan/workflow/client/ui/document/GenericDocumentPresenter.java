package com.duggan.workflow.client.ui.document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.duggan.workflow.client.model.UploadContext;
import com.duggan.workflow.client.model.UploadContext.UPLOADACTION;
import com.duggan.workflow.client.service.ServiceCallback;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.AppManager;
import com.duggan.workflow.client.ui.OnOptionSelected;
import com.duggan.workflow.client.ui.comments.CommentPresenter;
import com.duggan.workflow.client.ui.component.TableView;
import com.duggan.workflow.client.ui.delegate.DelegateTaskView;
import com.duggan.workflow.client.ui.delegate.msg.DelegationMessageView;
import com.duggan.workflow.client.ui.events.ActivitiesLoadEvent;
import com.duggan.workflow.client.ui.events.ActivitiesLoadEvent.ActivitiesLoadHandler;
import com.duggan.workflow.client.ui.events.AfterAttachmentReloadedEvent;
import com.duggan.workflow.client.ui.events.AfterDocumentLoadEvent;
import com.duggan.workflow.client.ui.events.AfterSaveEvent;
import com.duggan.workflow.client.ui.events.AssignTaskEvent;
import com.duggan.workflow.client.ui.events.ButtonClickEvent;
import com.duggan.workflow.client.ui.events.ButtonClickEvent.ButtonClickHandler;
import com.duggan.workflow.client.ui.events.CompleteDocumentEvent;
import com.duggan.workflow.client.ui.events.DeleteAttachmentEvent;
import com.duggan.workflow.client.ui.events.DeleteAttachmentEvent.DeleteAttachmentHandler;
import com.duggan.workflow.client.ui.events.DeleteLineEvent;
import com.duggan.workflow.client.ui.events.DeleteLineEvent.DeleteLineHandler;
import com.duggan.workflow.client.ui.events.ExecTaskEvent;
import com.duggan.workflow.client.ui.events.FileLoadEvent;
import com.duggan.workflow.client.ui.events.ProcessingCompletedEvent;
import com.duggan.workflow.client.ui.events.ProcessingEvent;
import com.duggan.workflow.client.ui.events.ReloadAttachmentsEvent;
import com.duggan.workflow.client.ui.events.ReloadAttachmentsEvent.ReloadAttachmentsHandler;
import com.duggan.workflow.client.ui.events.ReloadDocumentEvent;
import com.duggan.workflow.client.ui.events.ReloadDocumentEvent.ReloadDocumentHandler;
import com.duggan.workflow.client.ui.events.ReloadEvent;
import com.duggan.workflow.client.ui.events.WorkflowProcessEvent;
import com.duggan.workflow.client.ui.notifications.note.NotePresenter;
import com.duggan.workflow.client.ui.popup.GenericPopupPresenter;
import com.duggan.workflow.client.ui.save.CreateDocPresenter;
import com.duggan.workflow.client.ui.upload.UploadDocumentPresenter;
import com.duggan.workflow.client.ui.upload.attachment.AttachmentPresenter;
import com.duggan.workflow.client.ui.upload.custom.Uploader;
import com.duggan.workflow.client.ui.util.DateUtils;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.client.util.ENV;
import com.duggan.workflow.shared.model.Actions;
import com.duggan.workflow.shared.model.Activity;
import com.duggan.workflow.shared.model.Attachment;
import com.duggan.workflow.shared.model.BooleanValue;
import com.duggan.workflow.shared.model.Comment;
import com.duggan.workflow.shared.model.Delegate;
import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.model.DocStatus;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.DocumentLine;
import com.duggan.workflow.shared.model.DocumentType;
import com.duggan.workflow.shared.model.GridValue;
import com.duggan.workflow.shared.model.HTStatus;
import com.duggan.workflow.shared.model.HTSummary;
import com.duggan.workflow.shared.model.HTUser;
import com.duggan.workflow.shared.model.HTask;
import com.duggan.workflow.shared.model.MODE;
import com.duggan.workflow.shared.model.Notification;
import com.duggan.workflow.shared.model.NotificationType;
import com.duggan.workflow.shared.model.OutputDocument;
import com.duggan.workflow.shared.model.StringValue;
import com.duggan.workflow.shared.model.TaskLog;
import com.duggan.workflow.shared.model.TaskStepDTO;
import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.model.form.Field;
import com.duggan.workflow.shared.model.form.Form;
import com.duggan.workflow.shared.model.form.FormModel;
import com.duggan.workflow.shared.model.settings.SETTINGNAME;
import com.duggan.workflow.shared.model.settings.Setting;
import com.duggan.workflow.shared.requests.ApprovalRequest;
import com.duggan.workflow.shared.requests.CreateDocumentRequest;
import com.duggan.workflow.shared.requests.DeleteAttachmentRequest;
import com.duggan.workflow.shared.requests.DeleteDocumentRequest;
import com.duggan.workflow.shared.requests.DeleteLineRequest;
import com.duggan.workflow.shared.requests.ExecuteTriggersRequest;
import com.duggan.workflow.shared.requests.GenericRequest;
import com.duggan.workflow.shared.requests.GetActivitiesRequest;
import com.duggan.workflow.shared.requests.GetAttachmentsRequest;
import com.duggan.workflow.shared.requests.GetCommentsRequest;
import com.duggan.workflow.shared.requests.GetFormModelRequest;
import com.duggan.workflow.shared.requests.GetInitialDocumentRequest;
import com.duggan.workflow.shared.requests.GetOutputDocumentsRequest;
import com.duggan.workflow.shared.requests.GetProcessLogRequest;
import com.duggan.workflow.shared.requests.GetSettingsRequest;
import com.duggan.workflow.shared.requests.GetUsersRequest;
import com.duggan.workflow.shared.requests.MultiRequestAction;
import com.duggan.workflow.shared.requests.SaveCommentRequest;
import com.duggan.workflow.shared.responses.ApprovalRequestResult;
import com.duggan.workflow.shared.responses.CreateDocumentResult;
import com.duggan.workflow.shared.responses.DeleteAttachmentResponse;
import com.duggan.workflow.shared.responses.DeleteDocumentResponse;
import com.duggan.workflow.shared.responses.DeleteLineResponse;
import com.duggan.workflow.shared.responses.ExecuteTriggersResponse;
import com.duggan.workflow.shared.responses.GenericResponse;
import com.duggan.workflow.shared.responses.GetActivitiesResponse;
import com.duggan.workflow.shared.responses.GetAttachmentsResponse;
import com.duggan.workflow.shared.responses.GetCommentsResponse;
import com.duggan.workflow.shared.responses.GetFormModelResponse;
import com.duggan.workflow.shared.responses.GetInitialDocumentResponse;
import com.duggan.workflow.shared.responses.GetOutputDocumentsResponse;
import com.duggan.workflow.shared.responses.GetProcessLogResponse;
import com.duggan.workflow.shared.responses.GetSettingsResponse;
import com.duggan.workflow.shared.responses.GetUsersResponse;
import com.duggan.workflow.shared.responses.MultiRequestActionResult;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.common.client.IndirectProvider;
import com.gwtplatform.common.client.StandardProvider;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.proxy.PlaceManager;

public class GenericDocumentPresenter extends
		PresenterWidget<GenericDocumentPresenter.MyView> 
		implements ReloadDocumentHandler, ActivitiesLoadHandler,
		ReloadAttachmentsHandler,DeleteAttachmentHandler,DeleteLineHandler, ButtonClickHandler{

	public interface MyView extends View {
		void setValues(HTUser createdBy, Date created, String type, String subject,
				Date docDate, String value, String partner, String description, 
				Integer priority,DocStatus status, Long id, String taskDisplayName);
		
		void showForward(boolean show);
		void setValidTaskActions(List<Actions> actions);
		void show(boolean IsShowapprovalLink, boolean IsShowRejectLink);
		void showEdit(boolean displayed);
//		void setStates(List<NodeDetail> states);
		HasClickHandlers getSimulationBtn();
		HasClickHandlers getSaveButton();
		HasClickHandlers getAssignLink();
		HasClickHandlers getDeleteButton();
		HasClickHandlers getForwardForApproval();		
		HasClickHandlers getClaimLink();
		HasClickHandlers getStartLink();
		HasClickHandlers getSuspendLink();
		HasClickHandlers getResumeLink();
		HasClickHandlers getCompleteLink();
		HasClickHandlers getDelegateLink();
		HasClickHandlers getRevokeLink();
		HasClickHandlers getStopLink();
		HasClickHandlers getApproveLink();
		HasClickHandlers getRejectLink();
		HasClickHandlers getSaveCommentButton();
		HasClickHandlers getLinkPrevious();
		HasClickHandlers getLinkNext();
		HasClickHandlers getLinkEnv();
		HasClickHandlers getLinkViewProcessLog();
		Anchor getLinkContinue();
		String getComment();
		Uploader getUploader();
		void setComment(String string);
		SpanElement getSpnAttachmentNo();
		SpanElement getSpnActivityNo();
		DivElement getDivAttachment();
		void setForm(Form form,Doc doc, MODE mode);
		boolean isValid();
		Map<String,Value> getValues(); //Task Data

		void showDefaultFields(boolean b);

		void setDelegate(Delegate delegate);

		HasClickHandlers getUploadLink2();

		void setDeadline(Date endDateDue);

		void overrideDefaultCompleteProcess();

		void overrideDefaultStartProcess();

		void show(Anchor linkNext, boolean isShowRejectLink);

		void showNavigation(boolean b);

		void setEditMode(boolean editMode);

		void setSteps(List<TaskStepDTO> steps, int currentStep);

		void showAssignLink(boolean show);

		void setUnAssignedList(boolean isUnassignedList);

		void setProcessUrl(Long processInstanceId);

		void bindProcessLog(List<TaskLog> logs);
	}
	
	Long taskId;
	Long documentId;
	
	Doc doc;
	Form form;
	private int currentStep=0;
	private List<TaskStepDTO> steps = new ArrayList<TaskStepDTO>();
	
	private Integer activities=0;
	
	@Inject DispatchAsync requestHelper;
	@Inject PlaceManager placeManager;
	
	private IndirectProvider<CreateDocPresenter> createDocProvider;
	private IndirectProvider<CommentPresenter> commentPresenterFactory;
	private IndirectProvider<AttachmentPresenter> attachmentPresenterFactory;
	private IndirectProvider<NotePresenter> notePresenterFactory;
	private IndirectProvider<UploadDocumentPresenter> uploaderFactory;
	private MODE formMode;//Form Mode; can be set on set
	
	//@Inject static MainPagePresenter mainPagePresenter;
	@Inject static GenericPopupPresenter popupPresenter;
	
	public static final Object ACTIVITY_SLOT = new Object();
	public static final Object ATTACHMENTS_SLOT = new Object();
	private List<TaskLog> logs = null;
	
	@Inject
	public GenericDocumentPresenter(final EventBus eventBus, final MyView view,
			Provider<CreateDocPresenter> docProvider,
			Provider<CommentPresenter> commentProvider,
			Provider<AttachmentPresenter> attachmentProvider,
			Provider<NotePresenter> noteProvider,
			Provider<UploadDocumentPresenter> uploaderProvider) {
		super(eventBus, view);		
		ENV.clear();
		createDocProvider = new StandardProvider<CreateDocPresenter>(docProvider);
		commentPresenterFactory = new StandardProvider<CommentPresenter>(commentProvider);
		attachmentPresenterFactory = new StandardProvider<AttachmentPresenter>(attachmentProvider);
		notePresenterFactory = new StandardProvider<NotePresenter>(noteProvider);
		uploaderFactory = new StandardProvider<UploadDocumentPresenter>(uploaderProvider);
	}

	@Override
	protected void onBind() {
		super.onBind();		
		
		addRegisteredHandler(ReloadDocumentEvent.TYPE, this);
		addRegisteredHandler(ActivitiesLoadEvent.TYPE, this);
		addRegisteredHandler(ReloadAttachmentsEvent.TYPE, this);
		addRegisteredHandler(DeleteLineEvent.TYPE, this);
		addRegisteredHandler(ButtonClickEvent.TYPE, this);
		addRegisteredHandler(DeleteAttachmentEvent.TYPE, this);
		
		getView().getUploadLink2().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				showUpload();
			  }
		});
		
		getView().getForwardForApproval().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				forwardForApproval();				
			}
		});
		
		getView().getLinkContinue().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(steps.size()>currentStep+1){
					navigateToView(true);
					//last step submits
				}else if(doc instanceof Document){
					forwardForApproval();
				}else{
					complete(null, true);
				}
			}
		});
		
		getView().getSaveButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				save((Document)doc);
			}
		});
		
		getView().getDeleteButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(doc instanceof Document)
					if(((Document)doc).getStatus()==DocStatus.DRAFTED){
						//showEditForm(MODE.EDIT);
						delete((Document)doc);
					}
			}
		});
		
		getView().getSaveCommentButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				String comment = getView().getComment();
				save(comment);
			}
		});

		getView().getClaimLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				//submitRequest(Actions.CLAIM);
				fireEvent(new ExecTaskEvent(taskId, Actions.CLAIM));
			}
		});
		
		getView().getStartLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				//submitRequest(Actions.START);
				assert taskId!=null;
				fireEvent(new ExecTaskEvent(taskId, Actions.START));
			}
		});
		
		getView().getSuspendLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				//submitRequest(Actions.SUSPEND);
				fireEvent(new ExecTaskEvent(taskId, Actions.SUSPEND));
			}
		});
		
		getView().getResumeLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				//submitRequest(Actions.RESUME);
				fireEvent(new ExecTaskEvent(taskId, Actions.RESUME));
			}
		});
		
		getView().getDelegateLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				//submitRequest(Actions.DELEGATE);
				//fireEvent(new ExecTaskEvent(taskId, Actions.DELEGATE));
				requestHelper.execute(new GetUsersRequest(), new TaskServiceCallback<GetUsersResponse>() {
					@Override
					public void processResult(GetUsersResponse result) {
						showDelegatePopup(result.getUsers());
					}
				});
			}
		});
		
		getView().getRevokeLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				//submitRequest(Actions.REVOKE);
				fireEvent(new ExecTaskEvent(taskId, Actions.REVOKE));
			}
		});
		
		getView().getStopLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				fireEvent(new ExecTaskEvent(taskId, Actions.STOP));				
			}
		});
		
		getView().getApproveLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				final ConfirmAction confirm = new ConfirmAction("Do you want to approve this request?",
						"Approval comments");
				
				AppManager.showPopUp("Approval Comments", confirm, new OnOptionSelected() {
					
					@Override
					public void onSelect(String name) {
						if(name.equals("Approve")){
							//create comment
							Map<String, Value> values= new HashMap<String, Value>();
							values.put("isApproved", new BooleanValue(true));
							complete(values, true);	
							if(confirm.getComment()!=null && !confirm.getComment().isEmpty()){
								save(confirm.getComment());
							}
						}						
					}
				}, "Approve", "Cancel");		
				
			}
		});
		
		getView().getRejectLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {							
				final ConfirmAction confirm = new ConfirmAction("Do you want to reject this request?",
						"Rejection reason or comments");
				
				AppManager.showPopUp("Rejection Comments", confirm, new OnOptionSelected() {
					
					@Override
					public void onSelect(String name) {
						if(name.equals("Reject")){
							//create comment
							Map<String, Value> values = new HashMap<String, Value>();
							values.put("isApproved", new BooleanValue(false));
							complete(values, false);		
							
							if(confirm.getComment()!=null && !confirm.getComment().isEmpty()){
								save(confirm.getComment());
							}
						}						
					}
				}, "Reject", "Cancel");		
				
			}
		});
		
		
		getView().getLinkNext().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				navigateToView(true);
			}
		});
		
		getView().getLinkPrevious().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				navigateToView(false);
			}
		});
		
		getView().getAssignLink().addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				requestHelper.execute(new GetUsersRequest(), new TaskServiceCallback<GetUsersResponse>() {
					@Override
					public void processResult(GetUsersResponse result) {
						showAssignPopup(result.getUsers());
					}
				});
			}
		});
		
		getView().getLinkEnv().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
			
				TableView tableEnv = new TableView();
				tableEnv.setHeaders(Arrays.asList("Key","Value"));
				
				List<String> keys = new ArrayList<String>();
				keys.addAll(ENV.getValues().keySet());
				Collections.sort(keys);
				
				String suffix = "";
				if(taskId!=null){
					suffix = Field.getSuffix(taskId+"");
				}else{
					suffix = Field.getSuffix(documentId+"");
				}
				
				for(String key: keys){
					Object value = ENV.getValue(key);
					tableEnv.addRow(new InlineLabel(key.replace(suffix, "")), 
							new InlineLabel(value==null? "": value.toString()));
				}
				
				AppManager.showPopUp("Environment", tableEnv, new OnOptionSelected() {
					@Override
					public void onSelect(String name) {}
				}, "Ok");
			}
		});
		
		getView().getLinkViewProcessLog().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				loadProcessLog();
			}
		});	
		
	}
	
	protected void loadProcessLog() {
		
		if(doc.getProcessInstanceId()!=null){
			fireEvent(new ProcessingEvent("Loading Log"));
			requestHelper.execute(new GetProcessLogRequest(doc.getProcessInstanceId()),
					new TaskServiceCallback<GetProcessLogResponse>() {
				@Override
				public void processResult(
						GetProcessLogResponse aResponse) {
					fireEvent(new ProcessingCompletedEvent());
					logs = aResponse.getLogs();
					getView().bindProcessLog(logs);
				}
			});
		}
	}

	protected void showAssignPopup(List<HTUser> users) {
		final DelegateTaskView view = new DelegateTaskView(users);
		
		AppManager.showPopUp("Assign Task", view,
				new OnOptionSelected() {
					
					@Override
					public void onSelect(String name) {
						if(name.equals("Ok")){
							final HTUser user = view.getSelectedUser();
							if(user!=null && user.getUserId()!=null){
								
								DelegationMessageView msgView = 
										new DelegationMessageView(user, doc.getCaseNo());
								
								AppManager.showPopUp("Assign Message",
										msgView,
										new OnOptionSelected() {
											
											@Override
											public void onSelect(String name) {
												
												if(name.equals("Back")){
													showDelegatePopup(view);
												}else{
													fireEvent(new AssignTaskEvent(taskId, user.getUserId()));
												}
												
											}

										}, "Back", "Done");
								
							}
						}
					}
				}, "Ok", "Cancel");
	}
	
	boolean navigateIndex(boolean isNavigateNext){
		if(isNavigateNext){
			if(steps.size()>currentStep+1){
				currentStep = currentStep+1;
				return true;
			}//else ignore
		}else{
			//Navigate Previous
			if(currentStep>0){
				currentStep = currentStep-1;
				return true;
			}
		}
		
		return false;
	}

	protected void navigateToView(boolean isNavigateNext) {
		boolean navigating = false;
		//Complete/ Submitted documents should not be validated
		boolean validate=false;
		if(doc instanceof HTSummary){
			HTSummary summ = (HTSummary)doc;
			validate = !summ.getStatus().equals(HTStatus.COMPLETED);
		}else{
			Document document = (Document)doc;
			validate = !document.getStatus().equals(DocStatus.COMPLETED);
		}
		
		if((!isNavigateNext || !validate || (validate && getView().isValid())) && steps.size()>1){

			// !isNavigateNext -> Going Back
			navigating = navigateIndex(isNavigateNext);
		}
		
		mergeFormValuesWithDoc();
		
		if(navigating){
			navigateToView(steps.get(currentStep),isNavigateNext);
		}
		
	}

	protected void navigateToView(TaskStepDTO taskStepDTO, final boolean isNavigateNext) {
		
		fireEvent(new ProcessingEvent("Loading form "+(currentStep+1)+"/"+steps.size()));
		
		Long previousStepId = -1L;
		Long nextStepId = -1L;
		
		
		if(isNavigateNext){
			//Navigating forward
			previousStepId = steps.get(currentStep-1).getId();
			nextStepId = taskStepDTO.getId();
		}
		
		if(taskStepDTO.getFormId()!=null){
			Long formId = taskStepDTO.getFormId();			
			
			MultiRequestAction requests = new MultiRequestAction();
			requests.addRequest(new GetFormModelRequest(FormModel.FORMMODEL, formId, true));
			
			if(isNavigateNext)
				requests.addRequest(new ExecuteTriggersRequest(previousStepId, nextStepId, doc));
			
			requests.addRequest(new GetAttachmentsRequest(documentId));
			
			requestHelper.execute(requests,
				new TaskServiceCallback<MultiRequestActionResult>() {
					public void processResult(MultiRequestActionResult results) {					
						int i=0;	
						GetFormModelResponse aResponse = (GetFormModelResponse)results.get(i++);
						
						Doc document=null;
						if(isNavigateNext){
							document=((ExecuteTriggersResponse)results.get(i++)).getDocument();
						}else{
							document=doc;
						}
						
						Form form = (Form) aResponse.getFormModel().get(0);
						bindForm(form, document);
						
						//Continue, Finish buttons alteration
						getView().setSteps(steps, currentStep);
						
						GetAttachmentsResponse attachmentsresponse = (GetAttachmentsResponse)results.get(i++);
						bindAttachments(attachmentsresponse);
						
						fireEvent(new ProcessingCompletedEvent());							
					}
					
					@Override
					public void onFailure(Throwable caught) {
						super.onFailure(caught);
						//Reverse Navigation
						navigateIndex(!isNavigateNext);
					}
			});
			
		}else if(taskStepDTO.getOutputDocId()!=null){
			
			MultiRequestAction requests = new MultiRequestAction();
			requests.addRequest(new GetOutputDocumentsRequest(taskStepDTO.getOutputDocId()));
			if(isNavigateNext){
				requests.addRequest(new ExecuteTriggersRequest(previousStepId, nextStepId, doc));
			}
			requests.addRequest(new GetAttachmentsRequest(documentId));			
			
			requestHelper.execute(requests,
				new TaskServiceCallback<MultiRequestActionResult>() {
					public void processResult(MultiRequestActionResult results) {					
						int i=0;	
						GetOutputDocumentsResponse aResponse = (GetOutputDocumentsResponse)results.get(i++);
						OutputDocument outDoc = aResponse.getDocuments().get(0);
						
						Doc document=null;
						if(isNavigateNext){
							document=((ExecuteTriggersResponse)results.get(i++)).getDocument();
						}else{
							document=doc;
						}
						
						Form form = GenericDocUtils.generateForm(outDoc,document);
						bindForm(form,document);
						
						getView().setSteps(steps, currentStep);
						
						GetAttachmentsResponse attachmentsresponse = (GetAttachmentsResponse)results.get(i++);
						bindAttachments(attachmentsresponse);
						
						fireEvent(new ProcessingCompletedEvent());							
					}
					
					@Override
					public void onFailure(Throwable caught) {
						super.onFailure(caught);
						//Reverse Navigation
						navigateIndex(!isNavigateNext);
					}
			});
		}else{
			fireEvent(new ProcessingCompletedEvent());
		}
	}

	public void complete(Map<String, Value> withValues, boolean validateForm){
		if(validateForm){
			if(getView().isValid()){
				completeIt(withValues);
			}
		}else{
			completeIt(withValues);
		}
	}

	private void mergeFormValuesWithDoc(){
		//Copy all previous values from the task object into the result/values
		Map<String, Value> values = doc.getValues();
		
		//Get Form Values
		Map<String, Value> formValues = getView().getValues();
		if(formValues==null){
			formValues = new HashMap<String, Value>();
		}		
		//Add form field values : to take care of new values
		for(String key: formValues.keySet()){
			Value val = formValues.get(key);
			
			if(val instanceof GridValue){
				List<DocumentLine> lines = new ArrayList<DocumentLine>();
				lines.addAll(((GridValue)val).getValue());
				
				doc.getDetails().remove(key);
				doc.getDetails().put(key, lines);
				
				/*Grid Value must be written too - otherwise it gets lost if the task is completed
				 *  Coz complete takes only form values >> Map<String, Value>
				 */
				values.put(key, val);
			}else{
				//GridValue is never written here 
				values.put(key, val);
			}
		}
		 
		
	}

	private void completeIt(Map<String, Value> withValues) {
		
		//Get document Values
		mergeFormValuesWithDoc();
		
		final Map<String, Value> values = doc.getValues();
		
		//Add any programmatic values (Button values e.g isApproved)
		if(withValues!=null)
			values.putAll(withValues);
		
		//Remove any null keys
		values.remove(null);
		
		final ConfirmAction confirm = new ConfirmAction("Are you ready to submit?",
				"Comments");
		AppManager.showPopUp("Confirm Action", confirm,
				new OnOptionSelected() {
					
					@Override
					public void onSelect(String name) {
						if(name.equals("Yes")){
							//Fire Event
							fireEvent(new CompleteDocumentEvent(taskId, values));
							
							if(confirm.getComment()!=null && !confirm.getComment().isEmpty()){
								save(confirm.getComment());
							}
						}
					}
				},"Yes", "Cancel");
		
	}

	protected void forwardForApproval() {
		mergeFormValuesWithDoc();
		if(getView().isValid()){
			
			final ConfirmAction confirm = new ConfirmAction("Are you ready to submit?",
					"Comments/ Processing Advice");
			AppManager.showPopUp("Confirm action", confirm,
					new OnOptionSelected() {
						
						@Override
						public void onSelect(String name) {
							
							if(name.equals("Yes")){
								fireEvent(new ProcessingEvent());
								requestHelper.execute(new ApprovalRequest(AppContext.getUserId(), (Document)doc),
										new TaskServiceCallback<ApprovalRequestResult>(){
									@Override
									public void processResult(ApprovalRequestResult result) {
										GenericDocumentPresenter.this.getView().asWidget().removeFromParent();
										fireEvent(new ProcessingCompletedEvent());
										//clear selected document
										fireEvent(new AfterSaveEvent());
										fireEvent(new WorkflowProcessEvent(doc.getCaseNo(), "You have forwarded for Approval",doc));
									}
								});
								
								//Save comment
								if(confirm.getComment()!=null && !confirm.getComment().isEmpty()){
									save(confirm.getComment());
								}
							}
						}
					}, "Yes", "Cancel");
			
		}
	}

	protected void showUpload() {
		uploaderFactory.get(new ServiceCallback<UploadDocumentPresenter>() {
			@Override
			public void processResult(UploadDocumentPresenter result) {
				
				UploadContext context = new UploadContext();
				context.setAction(UPLOADACTION.ATTACHDOCUMENT);
				context.setContext("documentId", documentId+"");
				context.setContext("userid", AppContext.getUserId());
				result.setContext(context);
				addToPopupSlot(result,false);
			}
		});
		
	}

	protected void delete(Document document) {
		AppManager.showPopUp("Confirm Delete", 
				new InlineLabel("Do you want to delete document '"+document.getCaseNo()+"'"),
				new OnOptionSelected() {
					
					@Override
					public void onSelect(String name) {
						if(name.equals("Yes")){
							requestHelper.execute(new DeleteDocumentRequest(documentId),
									new TaskServiceCallback<DeleteDocumentResponse>() {
										@Override
										public void processResult(
												DeleteDocumentResponse aResponse) {
											if(aResponse.isDelete()){
												fireEvent(new ReloadEvent());
											}
											
										}
									});
						}
					}
				}, "Yes","Cancel");
	}

	private void showDelegatePopup(final List<HTUser> users) {
		final DelegateTaskView view = new DelegateTaskView(users);
		showDelegatePopup(view);
		
	}
	
	private void showDelegatePopup(final DelegateTaskView view) {
		AppManager.showPopUp("Delegate Task", view,
				new OnOptionSelected() {
					
					@Override
					public void onSelect(String name) {
						if(name.equals("Ok")){
							final HTUser user = view.getSelectedUser();
							if(user!=null && user.getUserId()!=null){
								
								DelegationMessageView msgView = new DelegationMessageView(user, doc.getCaseNo());
								
								AppManager.showPopUp("Delegation Message",
										msgView,
										new OnOptionSelected() {
											
											@Override
											public void onSelect(String name) {
												
												if(name.equals("Back")){
													showDelegatePopup(view);
												}else{
													ExecTaskEvent event = new ExecTaskEvent(taskId, Actions.DELEGATE);
													
													Map<String, Value> values = new HashMap<String, Value>();
													
													StringValue userValue = new StringValue(null, "targetUserId",user.getUserId());
													values.put(userValue.getKey(), userValue);
													event.setValues(values);
													fireEvent(event);
												}
												
											}
										}, "Back", "Done");
								
							}
						}
					}
				}, "Ok", "Cancel");
	}

	protected void save(Document document) {
		
		//Incremental/ Page based additions
		mergeFormValuesWithDoc();
		
		if (getView().isValid()) {
			fireEvent(new ProcessingEvent());
			MultiRequestAction requests = new MultiRequestAction();
			requests.addRequest(new CreateDocumentRequest(document));
			requests.addRequest(new GetAttachmentsRequest(documentId));
			
			requestHelper.execute(requests,
				new TaskServiceCallback<MultiRequestActionResult>() {
					public void processResult(MultiRequestActionResult results) {					
						int i=0;	
						CreateDocumentResult aResponse = (CreateDocumentResult)results.get(i++);
						Document saved = aResponse.getDocument();
						assert saved.getId() != null;
						bindForm(form, saved);
						
						GetAttachmentsResponse attachmentsresponse = (GetAttachmentsResponse)results.get(i++);
						bindAttachments(attachmentsresponse);
						
						fireEvent(new ProcessingCompletedEvent());							
					}
			});
		}
		
	}

	protected void save(String commenttxt) {
		if(commenttxt==null || commenttxt.trim().isEmpty())
			return;
		
		getView().setComment("");
		Comment comment = new Comment();
		comment.setComment(commenttxt);
		comment.setDocumentId(documentId);
		comment.setParentId(null);
		comment.setUserId(AppContext.getUserId());
		//comment.setCreatedBy(AppContext.getUserId());
		
		MultiRequestAction action = new MultiRequestAction();
		action.addRequest(new SaveCommentRequest(comment));
		action.addRequest(new GetActivitiesRequest(documentId));
		
		requestHelper.execute(action,
				 new TaskServiceCallback<MultiRequestActionResult>(){
			@Override
			public void processResult(MultiRequestActionResult result) {
				result.get(0);
				bindActivities((GetActivitiesResponse)result.get(1));
			}
		});
		
	}

	protected void showEditForm(final MODE mode) {
		
		createDocProvider.get(new ServiceCallback<CreateDocPresenter>() {
			@Override
			public void processResult(CreateDocPresenter result) {
				if(mode.equals(MODE.EDIT)){
					result.setDocumentId(documentId);
				}
				
				addToPopupSlot(result, true);				
			}
		});
	}

	private void clear() {
		getView().showEdit(false);
	}
	
	@Override
	protected void onReveal() {
		super.onReveal();
		loadData();
		getView().show((Anchor)getView().getLinkEnv(), AppContext.isCurrentUserAdmin());
	}
	
	private void loadData() {
		MultiRequestAction requests = new MultiRequestAction();
		requests.addRequest(new GetInitialDocumentRequest(documentId, taskId));
		requests.addRequest(new GetFormModelRequest(FormModel.FORMMODEL,taskId,documentId));
		requests.addRequest(new GetCommentsRequest(documentId));
		requests.addRequest(new GetAttachmentsRequest(documentId));
		requests.addRequest(new GetActivitiesRequest(documentId));
		
		fireEvent(new ProcessingEvent());
		if(documentId != null){
			requestHelper.execute(requests, 
					new TaskServiceCallback<MultiRequestActionResult>() {
				
				public void processResult(MultiRequestActionResult results) {					
					int i=0;
					
					//Document and Task Steps & Trigger before Load
					GetInitialDocumentResponse compositeResp =  (GetInitialDocumentResponse)results.get(i++);
					setSteps(compositeResp.getSteps());
					bindDocumentResult(compositeResp.getDoc());
					
					//Form
					GetFormModelResponse response = (GetFormModelResponse)results.get(i++);					
					
					if(!response.getFormModel().isEmpty()){
						bindForm((Form)response.getFormModel().get(0), compositeResp.getDoc());
					}else{
						getView().showDefaultFields(true);
					}

					//Comments					
					GetCommentsResponse commentsResult = (GetCommentsResponse)results.get(i++);
					bindCommentsResult(commentsResult);
					
					//Attachments
					GetAttachmentsResponse attachmentsresponse = (GetAttachmentsResponse)results.get(i++);
					bindAttachments(attachmentsresponse);
					
					//Activities
					GetActivitiesResponse getActivities = (GetActivitiesResponse)results.get(i++);
					bindActivities(getActivities);
										
					fireEvent(new ProcessingCompletedEvent());
					
				}	
			});
		}
	}

	protected void executeTriggers(final Form formToBind, Doc loadedDocument) {
		if(steps==null || steps.isEmpty()){
			bindForm(formToBind, loadedDocument);
			return;
		}
		
		Long previousStepId = 0L;
		Long nextStepId = steps.get(0).getId();
		
		requestHelper.execute(new ExecuteTriggersRequest(previousStepId, nextStepId, loadedDocument), 
				new TaskServiceCallback<ExecuteTriggersResponse>() {
			@Override
			public void processResult(ExecuteTriggersResponse aResponse) {
				bindForm(formToBind, aResponse.getDocument());
			}
		});
	}

	protected void setSteps(List<TaskStepDTO> steps) {
		this.steps = steps;
		if(steps==null){
			return;
		}
		
		getView().setSteps(steps,currentStep);
		
	}

	protected void bindForm(Form form, Doc doc) {
		this.doc = doc;
		this.form = form;
		
		if(doc instanceof Document){
			documentId = (Long) doc.getId();
			taskId=null;
		}else{
			documentId = ((HTSummary)doc).getDocumentRef();
			taskId = ((HTSummary)doc).getId();
		}
		
		if(form.getFields()==null || form.getFields().isEmpty()){
			getView().showDefaultFields(true);
			return;
		}

		//Form Fields
		if(doc instanceof Document){
			DocStatus status = ((Document)doc).getStatus();
			if(status==DocStatus.DRAFTED){
				getView().showNavigation(true); //Continue button will always be available
			}
		}
		
		MODE stepMode = null;
		if(!steps.isEmpty()){
			TaskStepDTO dto = steps.get(currentStep);
			stepMode = dto.getMode();
		}
		
		if(formMode!=null && formMode.equals(MODE.EDIT) && doc instanceof Document){
			getView().setForm(form,doc,stepMode);
			getView().setEditMode(true && ((Document)doc).getStatus()==DocStatus.DRAFTED);
			
		}else{
			getView().setForm(form,doc,stepMode);
		}
	}

	protected void bindActivities(GetActivitiesResponse response) {
		Map<Activity, List<Activity>> activitiesMap = response.getActivityMap();
		bindActivities(activitiesMap);		
	}
	
	public void bindActivities(Map<Activity, List<Activity>> activitiesMap){
		setInSlot(ACTIVITY_SLOT, null);
		
		Set<Activity> keyset = activitiesMap.keySet();
		List<Activity> activities= new ArrayList<Activity>();
		activities.addAll(keyset);
		Collections.sort(activities);
		Collections.reverse(activities);
		
		for(Activity activity: activities){
			bind(activity,false);	
			List<Activity> children = activitiesMap.get(activity);	
			if(children!=null){
				for(Activity child: children){
					bind(child, true);
				}
				
			}
		}		
		this.activities +=activitiesMap.size();
	}

	private void bind(final Activity child, boolean isChild) {
				
		if(child instanceof Comment){
			commentPresenterFactory.get(new ServiceCallback<CommentPresenter>() {
				@Override
				public void processResult(CommentPresenter result) {
					result.setComment((Comment)child);
					
					addToSlot(ACTIVITY_SLOT,result);
				}
			});
			
		}else if(child instanceof Notification){
			if(((Notification)child).getNotificationType()==NotificationType.FILE_UPLOADED){
				return;
			}
		
			notePresenterFactory.get(new ServiceCallback<NotePresenter>() {				
				@Override
				public void processResult(NotePresenter result) {
					result.setNotification((Notification)child, false);
					addToSlot(ACTIVITY_SLOT, result);
				}
			});
		}
		
	}

	protected void bindAttachments(GetAttachmentsResponse attachmentsresponse) {
		
		List<Attachment> attachments = attachmentsresponse.getAttachments();
		
		if(attachments.size()>0){
			getView().getDivAttachment().removeClassName("hidden");
			getView().getSpnAttachmentNo().setInnerText("Attachments (" + attachments.size() +")");
			fireEvent(new AfterAttachmentReloadedEvent(documentId));
		}
		
		setInSlot(ATTACHMENTS_SLOT, null);//clear
		for(final Attachment attachment: attachments){
			if(attachment.getFieldName()!=null){
				fireEvent(new FileLoadEvent(attachment));
				continue;
			}
			attachmentPresenterFactory.get(new ServiceCallback<AttachmentPresenter>() {
				@Override
				public void processResult(AttachmentPresenter result) {
					result.setAttachment(attachment);
					addToSlot(ATTACHMENTS_SLOT, result);
				}
			});
		}
		
	}

	protected void bindCommentsResult(GetCommentsResponse commentsResult) {
		setInSlot(ACTIVITY_SLOT, null);
		List<Comment> comments = commentsResult.getComments();
		
		this.activities += comments.size();
		
		getView().getSpnActivityNo().setInnerText("Activity("+this.activities+")");
		for(final Comment comment: comments){
			commentPresenterFactory.get(new ServiceCallback<CommentPresenter>() {
				@Override
				public void processResult(CommentPresenter result) {
					result.setComment(comment);
					addToSlot(ACTIVITY_SLOT,result);
				}
			});
		}
	}

	protected void bindDocumentResult(Doc result) {

		this.doc = result;
		Map<String, Value> vals= doc.getValues();
		
		long docId=0l;
		
		String taskDisplayName="";
		if(doc instanceof Document){
			docId = (Long)doc.getId();
			
		}else{
			HTSummary task = ((HTSummary)doc); 
			docId = task.getDocumentRef();
			this.taskId = task.getId();
			
			if(task.getName()!=null)
				taskDisplayName = task.getName();
			
			HTask humantask = (HTask)task;
			Delegate delegate = humantask.getDelegate();
			if(delegate!=null){
				getView().setDelegate(delegate);
			}
			
			if(!(task.getStatus()==HTStatus.COMPLETED)){
				if(task.getEndDateDue()!=null){
					getView().setDeadline(task.getEndDateDue());
				}else{
					//default 1 day allowance				
					getView().setDeadline(DateUtils.addDays(task.getCreated(), 1));
					
				}
			}
		}
		
		this.documentId = docId;
		
		Date created = doc.getCreated();
		String subject = doc.getCaseNo();
		
		Date docDate = doc.getDocumentDate();					
		String description = doc.getDescription();
		Integer priority = doc.getPriority();	
		
		String partner = null;
		String value= null;
		DocumentType docType = null;
		DocStatus status = null;
		
		String type=null;
		
		if(doc instanceof Document){
			Document d = (Document)doc;
			value = d.getValue();
			partner = d.getPartner();
			docType= d.getType();
			type = docType.getDisplayName();
			
			status = d.getStatus();
		}else{
			HTask task = (HTask)doc;
			type = task.getName();	
			status = task.getDocStatus();
		}
		
		if(value==null){
			Value val = vals.get("value");
			if(val!=null){
				value = val.getValue()==null? null: val.getValue().toString();
			}
		}
		
		if(partner==null){
			Value val = vals.get("partner");
			if(val!=null)
				value = ((StringValue)val).getValue();
		}
		
		
		getView().setValues(doc.getOwner(),created,
				type, subject, docDate,  value, partner, description, priority,status, documentId,
				taskDisplayName);
		
		if(status==DocStatus.DRAFTED){
			getView().showEdit(true);
		}else{
			getView().showEdit(false);
			clear();
		}
		
		//get document actions - if any
		AfterDocumentLoadEvent e = new AfterDocumentLoadEvent(documentId, taskId);
		fireEvent(e);		
		if(e.getValidActions()!=null){
			getView().setValidTaskActions(e.getValidActions());
		}	
		
		Long processInstanceId = doc.getProcessInstanceId();
		if(processInstanceId!=null)
			getView().setProcessUrl(processInstanceId);
//		if(processInstanceId!=null){
//			//System.err.println("Loading activities for ProcessInstanceId = "+processInstanceId);
//			requestHelper.execute(new GetProcessStatusRequest(processInstanceId),
//					new TaskServiceCallback<GetProcessStatusRequestResult>() {
//				@Override
//				public void processResult(
//						GetProcessStatusRequestResult result) {
//					List<NodeDetail> details = result.getNodes();
//					setProcessState(details);
//				}
//			});
//		}
	}

//	public void setProcessState(List<NodeDetail> states){
//		getView().setStates(states);
//	}

	public void setDocId(Long documentId, Long taskId) {
		this.documentId=documentId;
		this.taskId = taskId;
	}
	
	@Override
	protected void onHide() {
		super.onHide();
		this.unbind();
	}

	@Override
	public void onReloadDocument(ReloadDocumentEvent event) {
		if(event.getDocumentId()==this.documentId){
			loadData();
		}
	}

	@Override
	public void onActivitiesLoad(ActivitiesLoadEvent event) {
		bindActivities(event.getActivitiesMap());
	}

	@Override
	public void onReloadAttachments(ReloadAttachmentsEvent event) {
		reloadAttachments();
	}

	private void reloadAttachments() {
		requestHelper.execute(new GetAttachmentsRequest(documentId),
				new TaskServiceCallback<GetAttachmentsResponse>() {
			@Override
			public void processResult(GetAttachmentsResponse result) {
				bindAttachments(result);
			}
		});
	}
	
	/**
	 * Runtime - Delete Row
	 * -Enable save/ edit mode
	 */
	@Override
	public void onDeleteLine(DeleteLineEvent event) {
		DocumentLine line = event.getLine();
		if(line.getId()==null){
			return;
		}
		AppContext.fireEvent(new ProcessingEvent("Deleting ..."));
		requestHelper.execute(new DeleteLineRequest(line), 
				new TaskServiceCallback<DeleteLineResponse>() {
			@Override
			public void processResult(DeleteLineResponse result) {
				AppContext.fireEvent(new ProcessingCompletedEvent());
			}
		});
	}

	@Override
	public void onButtonClick(ButtonClickEvent event) {
		String requestType = event.getRequestType();
		
		if(requestType==null && event.getCustomHandlerClass()==null){
			return;
		}
		
		if(requestType!=null){
			if(requestType.equals("StartProcess")){
				forwardForApproval();
			}else if(requestType.equals("CompleteProcess")){
				complete(event.getValues(),event.isValidateForm());
			}
		}
		else{
			executeGenericCode(event.getValues(), event.getCustomHandlerClass());
		}
	}

	private void executeGenericCode(Map<String, Value> values,
			String customHandlerClass) {
		requestHelper.execute(new GenericRequest(values, customHandlerClass), 
				new TaskServiceCallback<GenericResponse>() {
			@Override
			public void processResult(GenericResponse aResponse) {	
			}
		});
	}

	public void setFormMode(MODE mode) {
		this.formMode = mode;
	}

	/**
	 * 
	 * @param show
	 */
	public void setUnAssignedList(boolean isUnassignedList) {
		getView().setUnAssignedList(isUnassignedList);
	}

	@Override
	public void onDeleteAttachment(DeleteAttachmentEvent event) {
		Attachment attachment = event.getAttachment();
		
		DeleteAttachmentRequest request = null;
		if(attachment!=null){
			request = new DeleteAttachmentRequest(attachment.getId());
		}else{
			request = new DeleteAttachmentRequest(event.getAttachmentIds());
		}
		
		requestHelper.execute(request,
				new TaskServiceCallback<DeleteAttachmentResponse>() {
					@Override
					public void processResult(DeleteAttachmentResponse result) {
						
					}
				});
	}
}
