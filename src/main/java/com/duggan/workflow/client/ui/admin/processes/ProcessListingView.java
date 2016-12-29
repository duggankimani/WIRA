package com.duggan.workflow.client.ui.admin.processes;

import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.AppManager;
import com.duggan.workflow.client.ui.OnOptionSelected;
import com.duggan.workflow.client.ui.upload.custom.Uploader;

import gwtupload.client.IUploader;
import gwtupload.client.SingleUploader;
import gwtupload.client.IUploader.OnFinishUploaderHandler;
import gwtupload.client.IUploader.OnStartUploaderHandler;
import gwtupload.client.IUploader.OnStatusChangedHandler;

import java.util.ArrayList;

import javax.inject.Inject;

import com.duggan.workflow.client.event.CheckboxSelectionEvent;
import com.duggan.workflow.client.model.UploadContext;
import com.duggan.workflow.client.model.UploadContext.UPLOADACTION;
import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.ui.admin.process.ProcessPresenter;
import com.duggan.workflow.client.ui.component.ActionLink;
import com.duggan.workflow.client.ui.component.Checkbox;
import com.duggan.workflow.client.ui.events.LoadProcessesEvent;
import com.duggan.workflow.client.ui.util.DateUtils;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.ProcessCategory;
import com.duggan.workflow.shared.model.ProcessDef;
import com.duggan.workflow.shared.model.Status;
import com.duggan.workflow.shared.requests.GetProcessImportStatus;
import com.duggan.workflow.shared.requests.GetProcessStatusRequest;
import com.duggan.workflow.shared.responses.GetProcessImportStatusResponse;
import com.duggan.workflow.shared.responses.GetProcessStatusRequestResult;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

class ProcessListingView extends ViewImpl implements ProcessListingPresenter.MyView {
    interface Binder extends UiBinder<Widget, ProcessListingView> {
    }

    @UiField
	Anchor aNewProcess;
	@UiField
	Anchor aStartProcesses;
	@UiField
	Anchor aAddCategory;

	@UiField
	Element divGeneralActions;
	@UiField
	Element divProcessInstanceActions;

	@UiField
	FlexTable tblProcesses;

	@UiField
	Anchor aActivate;
	@UiField
	Anchor aDeactivate;
	@UiField
	Anchor aRefresh;
	@UiField
	Anchor aEdit;
	@UiField
	Anchor aDelete;

	@UiField
	Anchor aConfigure;
	
	@UiField
	Anchor aExport;
	
	Uploader uploader;
	
	@UiField Anchor aImportProcess;
	HTMLPanel status = new HTMLPanel("");

    @Inject
    ProcessListingView(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
        uploader = new Uploader(true);
        final SingleUploader up = (SingleUploader)uploader.getIUploader();
        up.getElement().setId("processuploader");
        up.getElement().getStyle().setWidth(100, Unit.PCT);
        UploadContext ctx = new UploadContext();
        ctx.setAction(UPLOADACTION.IMPORTPROCESS);
        ArrayList<String> accept = new ArrayList<String>();
        accept.add("zip");
        ctx.setAccept(accept);
        uploader.setContext(ctx);
        
        //uploader.addStyleName("hide");
        //divGeneralActions.appendChild(uploader.getElement());
        
        final HTMLPanel parent = new HTMLPanel("");
        InlineLabel label = new InlineLabel("This may take a while, please be patient.");
        parent.add(label);
        parent.add(uploader);
        parent.add(status);
        
        uploader.getIUploader().addOnStatusChangedHandler(new OnStatusChangedHandler() {
			
			@Override
			public void onStatusChanged(IUploader uploader) {
				gwtupload.client.IUploadStatus.Status istatus = uploader.getStatusWidget().getStatus();
				GWT.log("Status = "+istatus);
				if(istatus==gwtupload.client.IUploadStatus.Status.SUCCESS){
					GWT.log("Status #Success");
					updateImportStatus(status,0);
				}
			}
		});
        
        uploader.getIUploader().addOnStartUploadHandler(new OnStartUploaderHandler() {
			
			@Override
			public void onStart(IUploader uploader) {
				
			}
		});
        
        aImportProcess.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				click(up.getElement());
				AppManager.showPopUp("Uploading Process", parent, new OnOptionSelected() {
					
					@Override
					public void onSelect(String name) {
						if(name.equals("Finish")){
							//Save
						}else{
							//Cancel
						}
						status.clear();
						AppContext.fireEvent(new LoadProcessesEvent());
						parent.removeFromParent();
					}
				}, "Finish", "Cancel");
				
			}
		});
    }
    
    protected void updateImportStatus(final HTMLPanel status, final int idx) {
		AppContext.getDispatcher().execute(new GetProcessImportStatus(), new TaskServiceCallback<GetProcessImportStatusResponse>() {
			@Override
			public void processResult(GetProcessImportStatusResponse aResponse) {
				ArrayList<String> importationStatus = aResponse.getStatus();
				for(int i=idx; i<importationStatus.size(); i++){
					String istatus = importationStatus.get(i);
					status.add(new HTMLPanel(""+istatus));
				}
				
				String finalState = importationStatus.get(importationStatus.size()-1);
				if(finalState.startsWith("Importation Succeeded!") || finalState.startsWith("Importation Failed!")){
					return;
				}else{
					updateImportStatus(status, importationStatus.size());
				}
			}
		});
	}

	protected native void click(Element source) /*-{
		$wnd.jQuery(source).find('input').trigger('click');
	}-*/;

	@Override
	public void bindProcesses(ArrayList<ProcessDef> processDefinitions) {
		tblProcesses.removeAllRows();
		setProcessHeaders(tblProcesses);

		int i = 1;
		for (ProcessDef user : processDefinitions) {
			int j = 0;
			Checkbox box = new Checkbox(user);
			box.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
				@Override
				public void onValueChange(ValueChangeEvent<Boolean> event) {
					Object model = ((Checkbox) (event.getSource())).getModel();
					AppContext.fireEvent(new CheckboxSelectionEvent(model,
							event.getValue()));
				}
			});

			tblProcesses.setWidget(i, j++, box);

			ActionLink link = new ActionLink(user);
			link.setText(user.getName());
			link.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					ProcessDef processDef = (ProcessDef) ((ActionLink) event
							.getSource()).getModel();
					AppContext.getPlaceManager().revealPlace(
							new PlaceRequest.Builder()
									.nameToken(NameTokens.processes)
									.with("a", ProcessPresenter.ACTION_PREVIEW)
									.with("p", processDef.getRefId()).build());
				}
			});
			HTMLPanel namePanel = new HTMLPanel("");
			namePanel.add(link);
			tblProcesses.setWidget(i, j++, namePanel);

			tblProcesses.setWidget(i, j++, new HTMLPanel(
					user.getCategory() == null ? "Other" : user.getCategory()
							.getDisplayName()));
//			tblProcesses.setWidget(i, j++, new HTMLPanel(user.getFileName()));
//			tblProcesses.setWidget(i, j++, new HTMLPanel(user.getImageName()));
			tblProcesses.setWidget(i, j++, new HTMLPanel(user.getProcessId()));
			tblProcesses.getFlexCellFormatter().setStyleName(i, (j-1), "truncate");
			String status = "<span class=\"label label-default arrowed-in\">INACTIVE</span>";
			if (user.getStatus() == Status.RUNNING) {
				status = "<span class=\"label label-success arrowed-in\">RUNNING</span>";
			}
			tblProcesses.setWidget(i, j++, new HTMLPanel(status));
			tblProcesses.setWidget(i, j++, new HTMLPanel(user.getInbox() + ""));
			tblProcesses.setWidget(i, j++, new HTMLPanel(user.getParticipated()
					+ ""));
			tblProcesses.setWidget(i, j++, new HTMLPanel(
					(user.getInbox() + user.getParticipated()) + ""));
			tblProcesses.setWidget(i, j++, new HTMLPanel(
					DateUtils.CREATEDFORMAT.format(user.getLastModified())));
			++i;
		}
	}

	private void setProcessHeaders(FlexTable table) {
		int j = 0;
		table.setWidget(0, j++, new HTMLPanel("<strong>#</strong>"));
		table.getFlexCellFormatter().setWidth(0, (j - 1), "20px");

		table.setWidget(0, j++, new HTMLPanel("<strong>Name</strong>"));
		table.getFlexCellFormatter().setWidth(0, (j - 1), "100px");
		table.setWidget(0, j++, new HTMLPanel("<strong>Category</strong>"));
		table.getFlexCellFormatter().setWidth(0, (j - 1), "110px");
//		table.setWidget(0, j++, new HTMLPanel("<strong>File Name</strong>"));
//		table.getFlexCellFormatter().setWidth(0, (j - 1), "100px");
//		table.setWidget(0, j++, new HTMLPanel("<strong>Image File</strong>"));
//		table.getFlexCellFormatter().setWidth(0, (j - 1), "100px");
		table.setWidget(0, j++, new HTMLPanel("<strong>Process ID</strong>"));
		table.getFlexCellFormatter().setWidth(0, (j - 1), "200px");
		table.setWidget(0, j++, new HTMLPanel("<strong>Status</strong>"));
		table.getFlexCellFormatter().setWidth(0, (j - 1), "60px");
		table.setWidget(0, j++, new HTMLPanel("<strong>Inbox</strong>"));
		table.getFlexCellFormatter().setWidth(0, (j - 1), "60px");
		table.setWidget(0, j++, new HTMLPanel("<strong>Completed</strong>"));
		table.getFlexCellFormatter().setWidth(0, (j - 1), "60px");
		table.setWidget(0, j++, new HTMLPanel("<strong>Total</strong>"));
		table.getFlexCellFormatter().setWidth(0, (j - 1), "60px");
		table.setWidget(0, j++, new HTMLPanel("<strong>Modified</strong>"));
		table.getFlexCellFormatter().setWidth(0, (j - 1), "100px");

		for (int i = 0; i < table.getCellCount(0); i++) {
			table.getFlexCellFormatter().setStyleName(0, i, "th");
		}
	}

	public HasClickHandlers getaNewProcess() {
		return aNewProcess;
	}

	@Override
	public HasClickHandlers getStartAllProcesses() {
		return aStartProcesses;
	}

	@Override
	public HasClickHandlers getAddCategories() {
		return aAddCategory;
	}

	@Override
	public void setCategories(ArrayList<ProcessCategory> categories) {
		// lstCategories.setItems(categories);
	}

	@Override
	public void setProcessEdit(ProcessDef process, boolean value) {
		if (value) {
			divProcessInstanceActions.removeClassName("hide");
			divGeneralActions.addClassName("hide");
		} else {
			divProcessInstanceActions.addClassName("hide");
			divGeneralActions.removeClassName("hide");
		}

		if (process != null) {
			switch (process.getStatus()) {
			case INACTIVE:
				aActivate.removeStyleName("hide");
				aDeactivate.addStyleName("hide");
				break;

			case RUNNING:
				aActivate.addStyleName("hide");
				aDeactivate.removeStyleName("hide");
				break;
			}
		}
	}

	public HasClickHandlers getActivateButton() {
		return aActivate;
	}

	public HasClickHandlers getDeactivateButton() {
		return aDeactivate;
	}

	public HasClickHandlers getRefreshButton() {
		return aRefresh;
	}

	public HasClickHandlers getEditButton() {
		return aEdit;
	}
	
	public HasClickHandlers getDownloadButton() {
		return aExport;
	}

	public HasClickHandlers getDeleteButton() {
		return aDelete;
	}

	public HasClickHandlers getConfigureButton() {
		return aConfigure;
	}

    
}