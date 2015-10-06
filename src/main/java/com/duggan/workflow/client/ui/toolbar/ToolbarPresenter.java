package com.duggan.workflow.client.ui.toolbar;

import com.duggan.workflow.shared.event.ExecuteWorkflowEvent;
import com.duggan.workflow.shared.event.SetStatusEvent;
import com.duggan.workflow.shared.event.SetStatusEvent.SetStatusHandler;
import com.duggan.workflow.shared.model.Actions;
import com.duggan.workflow.shared.model.HTStatus;
import com.duggan.workflow.shared.requests.ExecuteWorkflow;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.web.bindery.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class ToolbarPresenter extends PresenterWidget<ToolbarPresenter.MyView> implements SetStatusHandler{

	public interface MyView extends View {

		void setTaskStatus(HTStatus status);
		
		HasClickHandlers getClaimLink();
		HasClickHandlers getStartLink();
		HasClickHandlers getSuspendLink();
		HasClickHandlers getResumeLink();
		HasClickHandlers getCompleteLink();
		HasClickHandlers getDelegateLink();
		HasClickHandlers getRevokeLink();
		HasClickHandlers getStopLink();
		HasClickHandlers getForwardLink();

	}

	@Inject DispatchAsync dispatcher;
	
	
	@Inject
	public ToolbarPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
		
	}

	@Override
	protected void onBind() {
		super.onBind();
		this.addRegisteredHandler(SetStatusEvent.TYPE, this);

		getView().getClaimLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				submitRequest(Actions.CLAIM);
				
			}
		});
		
		getView().getStartLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				submitRequest(Actions.START);
				
			}
		});
		
		getView().getSuspendLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				submitRequest(Actions.SUSPEND);
				
			}
		});
		
		getView().getResumeLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				submitRequest(Actions.RESUME);
				
			}
		});
		
		getView().getCompleteLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				submitRequest(Actions.COMPLETE);
				
			}
		});
		
		getView().getDelegateLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				submitRequest(Actions.DELEGATE);
				
			}
		});
		
		getView().getRevokeLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				submitRequest(Actions.REVOKE);
				
			}
		});
		
		getView().getStopLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				submitRequest(Actions.STOP);
				
			}
		});
		
		getView().getForwardLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				submitRequest(Actions.FORWARD);
				
			}
		});
		
	}

	protected void submitRequest(Actions action) {
		getEventBus().fireEvent(new ExecuteWorkflowEvent(action));
	}

	@Override
	public void onSetStatus(SetStatusEvent event) {
		
		getView().setTaskStatus(event.getStatus());
	}
}
