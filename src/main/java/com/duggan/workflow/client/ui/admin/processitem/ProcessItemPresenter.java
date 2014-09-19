package com.duggan.workflow.client.ui.admin.processitem;

import java.util.Date;
import java.util.List;

import com.duggan.workflow.client.service.ServiceCallback;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.AppManager;
import com.duggan.workflow.client.ui.OnOptionSelected;
import com.duggan.workflow.client.ui.events.EditProcessEvent;
import com.duggan.workflow.client.ui.events.ProcessSelectedEvent;
import com.duggan.workflow.client.ui.events.ProcessingCompletedEvent;
import com.duggan.workflow.client.ui.events.ProcessingEvent;
import com.duggan.workflow.shared.model.DocumentType;
import com.duggan.workflow.shared.model.ManageProcessAction;
import com.duggan.workflow.shared.model.ProcessDef;
import com.duggan.workflow.shared.model.Status;
import com.duggan.workflow.shared.requests.DeleteProcessRequest;
import com.duggan.workflow.shared.requests.ManageKnowledgeBaseRequest;
import com.duggan.workflow.shared.responses.DeleteProcessResponse;
import com.duggan.workflow.shared.responses.ManageKnowledgeBaseResponse;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.web.bindery.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class ProcessItemPresenter extends
		PresenterWidget<ProcessItemPresenter.MyView> {

	public interface MyView extends View {

		HasClickHandlers getActivateButton();
		HasClickHandlers getDeactivateButton();
		HasClickHandlers getRefreshButton();
		HasClickHandlers getEditButton();
		HasClickHandlers getDeleteButton();
		void setValues(String name, String processId,String description, List<DocumentType> docTypes,
				Date lastModified, Long fileId, String fileName,
				Status status,  String imageName, Long imageId);

		CheckBox getSelectBox();
	}
	
	@Inject DispatchAsync requestHelper;

	ProcessDef processDef;
	
	@Inject
	public ProcessItemPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();

		getView().getSelectBox().addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if(processDef.getStatus()==Status.INACTIVE){
					Window.alert("Please activate this process to continue");
					((CheckBox)event.getSource()).setValue(false);//reset
				}else{
					fireEvent(new ProcessSelectedEvent(processDef, event.getValue()));
				}
				
			}
		});
		
		getView().getActivateButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
							
				ManageKnowledgeBaseRequest request = 
						new ManageKnowledgeBaseRequest(processDef.getId(), 
								ManageProcessAction.ACTIVATE, 
								false);
				submit(request);
			}
		});
		
		getView().getDeactivateButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				ManageKnowledgeBaseRequest request = 
						new ManageKnowledgeBaseRequest(processDef.getId(), 
								ManageProcessAction.DEACTIVATE, 
								false);
				submit(request);
			}
		});
		
		getView().getEditButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				fireEvent(new EditProcessEvent(processDef.getId()));
			}
		});
		
		getView().getRefreshButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				ManageKnowledgeBaseRequest request = 
						new ManageKnowledgeBaseRequest(processDef.getId(), 
								ManageProcessAction.ACTIVATE, 
								true);
				submit(request);
			}
		});
		
		getView().getDeleteButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				AppManager.showPopUp("Confirm Delete", 
						new InlineLabel("Do you want to delete process '"+processDef.getName()+"'"),
						new OnOptionSelected() {
							
							@Override
							public void onSelect(String name) {
								if(name.equals("Yes")){

									DeleteProcessRequest request = new DeleteProcessRequest(processDef.getId());
								
									requestHelper.execute(request, new ServiceCallback<DeleteProcessResponse>() {
										@Override
										public void processResult(DeleteProcessResponse result) {
											getView().asWidget().removeFromParent();
										}
									});
								}
							}
						}, "Yes", "Cancel");
			}				
		});
	}

	protected void submit(ManageKnowledgeBaseRequest request) {
		fireEvent(new ProcessingEvent());
		requestHelper.execute(request, new TaskServiceCallback<ManageKnowledgeBaseResponse>() {
			@Override
			public void processResult(ManageKnowledgeBaseResponse result) {
				setProcess(result.getProcessDef());
				fireEvent(new ProcessingCompletedEvent());
			}
		});
	}

	public void setProcess(ProcessDef def) {
		this.processDef = def;
		getView().setValues(def.getName(), def.getProcessId(), def.getDescription(),
				def.getDocTypes(), def.getLastModified(),
				def.getFileId(), def.getFileName(), def.getStatus(), def.getImageName(), def.getImageId());
	}
}
