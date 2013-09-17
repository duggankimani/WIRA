package com.duggan.workflow.client.ui.admin.process;

import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.events.UploadEndedEvent;
import com.duggan.workflow.client.ui.events.UploadEndedEvent.UploadEndedHandler;
import com.duggan.workflow.client.ui.events.UploadStartedEvent;
import com.duggan.workflow.client.ui.events.UploadStartedEvent.UploadStartedHandler;
import com.duggan.workflow.shared.model.ProcessDef;
import com.duggan.workflow.shared.requests.DeleteProcessRequest;
import com.duggan.workflow.shared.requests.SaveProcessRequest;
import com.duggan.workflow.shared.responses.DeleteProcessResponse;
import com.duggan.workflow.shared.responses.SaveProcessResponse;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.PopupView;
import com.google.inject.Inject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;

public class ProcessPresenter extends PresenterWidget<ProcessPresenter.MyView> 
	implements UploadStartedHandler, UploadEndedHandler{

	public interface MyView extends PopupView {
		HasClickHandlers getNext();
		HasClickHandlers getFinish();
		HasClickHandlers getCancel();
		boolean isValid();
		ProcessDef getProcess();
		void setProcessId(Long id);
		void enable(boolean enableFinish, boolean Cancel);
	}

	@Inject DispatchAsync requestHelper; 
	
	ProcessDef process = null;
	
	@Inject
	public ProcessPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(UploadStartedEvent.TYPE, this);
		addRegisteredHandler(UploadEndedEvent.TYPE, this);
		
		getView().getNext().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(getView().isValid()){
					
					ProcessDef def = getView().getProcess();
					if(process!=null){
						def.setId(process.getId());
					}
					
					SaveProcessRequest request = new SaveProcessRequest(def);
					
					requestHelper.execute(request, new TaskServiceCallback<SaveProcessResponse>() {
						@Override
						public void processResult(SaveProcessResponse result) {
							ProcessDef def = result.getProcessDef();
							ProcessPresenter.this.process = def;
							getView().setProcessId(def.getId());
						}
					});
				}
			}
		});
		
		getView().getCancel().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(process!=null){
					DeleteProcessRequest request = new DeleteProcessRequest(process.getId());
					requestHelper.execute(request, new TaskServiceCallback<DeleteProcessResponse>() {
						@Override
						public void processResult(DeleteProcessResponse result) {
							getView().hide();
						}
					});
				}
			}
		});
		
		
	}

	@Override
	public void onUploadEnded(UploadEndedEvent event) {
		getView().enable(true, true);
	}

	@Override
	public void onUploadStarted(UploadStartedEvent event) {
		getView().enable(false, true);
	}
}
