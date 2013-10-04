package com.duggan.workflow.client.ui.admin.addprocess;

import java.util.List;

import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.events.LoadProcessesEvent;
import com.duggan.workflow.client.ui.events.UploadEndedEvent;
import com.duggan.workflow.client.ui.events.UploadEndedEvent.UploadEndedHandler;
import com.duggan.workflow.client.ui.events.UploadStartedEvent;
import com.duggan.workflow.client.ui.events.UploadStartedEvent.UploadStartedHandler;
import com.duggan.workflow.shared.model.DocumentType;
import com.duggan.workflow.shared.model.ProcessDef;
import com.duggan.workflow.shared.requests.DeleteProcessRequest;
import com.duggan.workflow.shared.requests.GetDocumentTypesRequest;
import com.duggan.workflow.shared.requests.GetProcessRequest;
import com.duggan.workflow.shared.requests.MultiRequestAction;
import com.duggan.workflow.shared.requests.SaveProcessRequest;
import com.duggan.workflow.shared.responses.DeleteProcessResponse;
import com.duggan.workflow.shared.responses.GetDocumentTypesResponse;
import com.duggan.workflow.shared.responses.GetProcessResponse;
import com.duggan.workflow.shared.responses.MultiRequestActionResult;
import com.duggan.workflow.shared.responses.SaveProcessResponse;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.PopupView;
import com.google.inject.Inject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;

public class AddProcessPresenter extends PresenterWidget<AddProcessPresenter.MyView> 
	implements UploadStartedHandler, UploadEndedHandler{

	public interface MyView extends PopupView {
		HasClickHandlers getNext();
		HasClickHandlers getFinish();
		HasClickHandlers getCancel();
		boolean isValid();
		ProcessDef getProcess();
		void setProcessId(Long id);
		void enable(boolean enableFinish, boolean Cancel);
		void setValues(Long processDefId,String name, String processId,String description, List<DocumentType> docTypes);
		void setDocumentTypes(List<DocumentType> documentTypes);

	}

	@Inject DispatchAsync requestHelper; 
	
	ProcessDef process = null;
	
	Long processDefId = null;
	
	@Inject
	public AddProcessPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(UploadStartedEvent.TYPE, this);
		addRegisteredHandler(UploadEndedEvent.TYPE, this);
		
		getView().getFinish().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				fireEvent(new LoadProcessesEvent());
			}
		});
		
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
							AddProcessPresenter.this.process = def;
							getView().setProcessId(def.getId());
						}
					});
				}
			}
		});
		
		getView().getCancel().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				//if processDefId is null, this was a new process
				//other wise, the we were editing existing info
				if(process!=null && processDefId==null){
					DeleteProcessRequest request = new DeleteProcessRequest(process.getId());
					requestHelper.execute(request, new TaskServiceCallback<DeleteProcessResponse>() {
						@Override
						public void processResult(DeleteProcessResponse result) {
							getView().hide();
						}
					});
				}else{
					getView().hide();
				}
			}
		});
		
		
	}
	
	private void loadProcess() {
		
		MultiRequestAction requests = new MultiRequestAction();
		requests.addRequest(new GetDocumentTypesRequest());
		if(processDefId!=null)
			requests.addRequest( new GetProcessRequest(processDefId));
		
		
		requestHelper.execute(requests, new TaskServiceCallback<MultiRequestActionResult>() {
			@Override
			public void processResult(MultiRequestActionResult results) {
				
				GetDocumentTypesResponse response = (GetDocumentTypesResponse)results.get(0);
				getView().setDocumentTypes(response.getDocumentTypes());
				
				if(processDefId!=null){
					GetProcessResponse result =  (GetProcessResponse)results.get(1);
					process = result.getProcessDef();
					getView().setValues(process.getId(),
							process.getName(),process.getProcessId(),
							process.getDescription(),
							process.getDocTypes());
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

	public void setProcessDefId(Long processDefId) {
		this.processDefId = processDefId;
	}
	
	
	@Override
	protected void onReveal() {
		super.onReveal();
		loadProcess();
	}

}
