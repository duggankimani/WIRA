package com.duggan.workflow.client.ui.tasklistitem;

import java.util.HashMap;

import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.events.AfterSaveEvent;
import com.duggan.workflow.client.ui.events.CompleteDocumentEvent;
import com.duggan.workflow.client.ui.events.CompleteDocumentEvent.CompleteDocumentHandler;
import com.duggan.workflow.client.ui.events.ExecTaskEvent;
import com.duggan.workflow.client.ui.events.ExecTaskEvent.ExecTaskHandler;
import com.duggan.workflow.client.ui.events.ProcessingCompletedEvent;
import com.duggan.workflow.client.ui.events.ProcessingEvent;
import com.duggan.workflow.client.ui.events.WorkflowProcessEvent;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.Actions;
import com.duggan.workflow.shared.model.ApproverAction;
import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.HTSummary;
import com.duggan.workflow.shared.model.LongValue;
import com.duggan.workflow.shared.model.Notification;
import com.duggan.workflow.shared.model.NotificationType;
import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.requests.ExecuteWorkflow;
import com.duggan.workflow.shared.requests.MultiRequestAction;
import com.duggan.workflow.shared.requests.SaveNotificationRequest;
import com.duggan.workflow.shared.responses.ExecuteWorkflowResult;
import com.duggan.workflow.shared.responses.MultiRequestActionResult;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.wira.commons.shared.models.HTUser;

public abstract class IsTaskPresenter<V extends IsTaskPresenter.IBaseTaskItemView>
		extends PresenterWidget<IsTaskPresenter.IBaseTaskItemView> implements
		CompleteDocumentHandler, ExecTaskHandler {

	public interface IBaseTaskItemView extends View {
	}

	Doc task;

	@Inject
	DispatchAsync dispatcher;

	@Inject
	PlaceManager placeManager;

	public IsTaskPresenter(EventBus eventBus, IBaseTaskItemView view) {
		super(eventBus, view);
	}
	
	@Override
	public V getView() {
		return (V)super.getView();
	}

	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(CompleteDocumentEvent.TYPE, this);
		addRegisteredHandler(ExecTaskEvent.TYPE, this);
	}

	protected void submitRequest(final Actions action,
			final HashMap<String, Value> values) {

		fireEvent(new ProcessingEvent());
		ExecuteWorkflow workflow = new ExecuteWorkflow(0l,
				AppContext.getUserId(), Actions.START);
		workflow.setAction(action);
		workflow.setValues(values);

		if (task instanceof Document) {
			workflow.setTaskId(new Long((Integer) task.getId()));
		} else
			workflow.setTaskId((Long) task.getId());

		if (action == Actions.DELEGATE) {
			delegate(workflow, action, values);
			return;
		}

		dispatcher.execute(workflow,
				new TaskServiceCallback<ExecuteWorkflowResult>() {

					@Override
					public void processResult(ExecuteWorkflowResult result) {
						Doc doc = result.getDocument();

						// refresh ArrayList
						fireEvent(new ProcessingCompletedEvent());

						if (action == Actions.COMPLETE
								|| action == Actions.SUSPEND
								|| action == Actions.RESUME) {
							String out = "";
							if (action == Actions.COMPLETE) {
								Boolean isApproved = null;
								if (values != null) {
									Value value = values.get("isApproved");
									if (value != null) {
										isApproved = value.getValue() == null ? null
												: (Boolean) (value.getValue());
									}
								}

								out = isApproved == null ? "Review completed for "
										: isApproved ? "You have Approved "
												: "You have Rejected ";
							} else if (action == Actions.SUSPEND) {
								out = "You have suspended ";
							} else if (action == Actions.RESUME) {
								out = "You have resumed ";
							}

							// removeFromParent();
							fireEvent(new AfterSaveEvent());
							// reload((HTSummary)result.getDocument());
							fireEvent(new WorkflowProcessEvent(doc.getCaseNo(),
									out, doc));
						} else {
							// fireEvent(new ReloadEvent());
							reload((HTSummary) result.getDocument());
						}
						//
					}
				});
	}

	private void delegate(ExecuteWorkflow execWorkflow, Actions action,
			HashMap<String, Value> values) {
		Notification notification = new Notification();
		notification.setApproverAction(ApproverAction.DELEGATE);
		HTSummary summary = (HTSummary) task;
		notification.setDocumentId(summary.getDocumentRef());
		notification.setDocRefId(summary.getRefId());
		// notification.setDocumentType(summary.getDocStatus());
		// notification.setOwner(summary.getOwner());
		notification.setProcessInstanceId(summary.getProcessInstanceId());
		notification.setNotificationType(NotificationType.TASKDELEGATED);
		notification.setRead(false);
		notification.setSubject(summary.getCaseNo());

		HTUser user = new HTUser((String) values.get("targetUserId").getValue());
		notification.setTargetUserId(user);
		// Delegation Recipient Notification
		SaveNotificationRequest saveNoteRequest = new SaveNotificationRequest(
				notification);

		// Notification delegatorNote = notification.clone();
		// delegatorNote.setTargetUserId(AppContext.getContextUser().getUserId());
		// SaveNotificationRequest saveNoteRequest2 = new
		// SaveNotificationRequest(delegatorNote);

		MultiRequestAction requests = new MultiRequestAction();
		requests.addRequest(execWorkflow);
		requests.addRequest(saveNoteRequest);
		// requests.addRequest(saveNoteRequest2);

		dispatcher.execute(requests,
				new TaskServiceCallback<MultiRequestActionResult>() {

					@Override
					public void processResult(MultiRequestActionResult results) {
						// refresh ArrayList
						fireEvent(new ProcessingCompletedEvent());

						ExecuteWorkflowResult result = (ExecuteWorkflowResult) results
								.get(0);
						// fireEvent(new ReloadEvent());
						reload((HTSummary) result.getDocument());

					}
				});

	}

	protected void reload(HTSummary summary) {
	}


	@Override
	public void onCompleteDocument(CompleteDocumentEvent event) {
		if (task instanceof Document) {
			return;
		}

		if (task.getId().equals(event.getTaskId())) {
			HashMap<String, Value> arguments = event.getResults();
			arguments.put("documentId",
					new LongValue(((HTSummary) task).getDocumentRef()));
			submitRequest(Actions.COMPLETE, arguments);
		}
	}

	@Override
	public void onExecTask(ExecTaskEvent event) {
		if (task instanceof Document) {
			return;
		}

		if (task.getId().equals(event.getTaskId())) {
			// System.err.println("#####EXECUTING WF ACTION - "+event.getAction()+"; DOCUMENT :: "+documentId);
			submitRequest(event.getAction(), event.getValues());
		}
	}
}
