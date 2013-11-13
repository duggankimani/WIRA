package com.duggan.workflow.client.ui.task.personalreview;

import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.duggan.workflow.client.place.NameTokens;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.google.inject.Inject;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.MainPagePresenter;
import com.duggan.workflow.client.ui.login.LoginGateKeeper;
import com.duggan.workflow.client.ui.toolbar.ToolbarPresenter;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.event.ExecuteWorkflowEvent;
import com.duggan.workflow.shared.event.ExecuteWorkflowEvent.ExecuteWorkflowHandler;
import com.duggan.workflow.shared.event.SetStatusEvent;
import com.duggan.workflow.shared.model.Actions;
import com.duggan.workflow.shared.model.HTask;
import com.duggan.workflow.shared.requests.ExecuteWorkflow;
import com.duggan.workflow.shared.requests.GetItemRequest;
import com.duggan.workflow.shared.responses.ExecuteWorkflowResult;
import com.duggan.workflow.shared.responses.GetItemResult;

public class PersonalReviewPresenter
		extends
		Presenter<PersonalReviewPresenter.MyView, PersonalReviewPresenter.MyProxy> implements ExecuteWorkflowHandler{

	public interface MyView extends View {
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.personalreview)
	@UseGatekeeper(LoginGateKeeper.class)
	public interface MyProxy extends ProxyPlace<PersonalReviewPresenter> {
	}

	@ContentSlot
	public static final Type<RevealContentHandler<?>> TOOLBAR_content = new Type<RevealContentHandler<?>>();

	@Inject DispatchAsync dispatcher;
	
	@Inject ToolbarPresenter toolbarPresenter;
	
	HTask task;
	
	@Inject
	public PersonalReviewPresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy) {
		super(eventBus, view, proxy);
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		String taskIdStr = request.getParameter("taskId", "0l");
		
//		long taskId = new Long(taskIdStr);
//		dispatcher.execute(new GetTask(AppContext.getUserId(), taskId), new TaskServiceCallback<GetTaskResult>() {
//			@Override
//			public void processResult(GetTaskResult result) {
//				setTask(result.getTask());
//			}
//		});
				
	}
	
	protected void setTask(HTask task2) {
		task = task2;
		//getEventBus().fireEvent(new SetStatusEvent(task.getData().getStatus()));
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPagePresenter.CONTENT_SLOT, this);
	}

	@Override
	protected void onBind() {
		super.onBind();
		this.addRegisteredHandler(ExecuteWorkflowEvent.TYPE, this);
	}
	
	@Override
	protected void onReveal() {
		super.onReveal();
		setInSlot(TOOLBAR_content, toolbarPresenter);
	}
	
	protected void submitRequest(ExecuteWorkflow workflow) {
		dispatcher.execute(workflow, new TaskServiceCallback<ExecuteWorkflowResult>() {
			
			@Override
			public void processResult(ExecuteWorkflowResult result) {
				
			}
		});
	}

	@Override
	public void onExecuteWorkflow(ExecuteWorkflowEvent event) {
		Actions action = event.getAction();
		ExecuteWorkflow workflowRequest = new ExecuteWorkflow(task.getId(),AppContext.getUserId(), action);
		submitRequest(workflowRequest);
	}
	
}
