package com.duggan.workflow.client.ui.admin.formbuilder;

import java.util.ArrayList;

import com.duggan.workflow.client.event.ProcessChildLoadedEvent;
import com.duggan.workflow.client.model.UploadContext;
import com.duggan.workflow.client.model.UploadContext.UPLOADACTION;
import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.service.ServiceCallback;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.AppManager;
import com.duggan.workflow.client.ui.OnOptionSelected;
import com.duggan.workflow.client.ui.admin.formbuilder.upload.ImportView;
import com.duggan.workflow.client.ui.admin.processmgt.BaseProcessPresenter;
import com.duggan.workflow.client.ui.component.DropDownList;
import com.duggan.workflow.client.ui.events.PropertyChangedEvent;
import com.duggan.workflow.client.ui.events.PropertyChangedEvent.PropertyChangedHandler;
import com.duggan.workflow.client.ui.events.SaveFormDesignEvent;
import com.duggan.workflow.client.ui.events.SaveFormDesignEvent.SaveFormDesignHandler;
import com.duggan.workflow.client.ui.events.SavePropertiesEvent;
import com.duggan.workflow.client.ui.events.SavePropertiesEvent.SavePropertiesHandler;
import com.duggan.workflow.client.ui.security.AdminGateKeeper;
import com.duggan.workflow.client.ui.util.ArrayUtil;
import com.duggan.workflow.shared.model.ProcessDef;
import com.duggan.workflow.shared.model.form.Form;
import com.duggan.workflow.shared.requests.CreateFormRequest;
import com.duggan.workflow.shared.requests.DeleteFormModelRequest;
import com.duggan.workflow.shared.requests.ExportFormRequest;
import com.duggan.workflow.shared.requests.GetFormModelRequest;
import com.duggan.workflow.shared.requests.GetFormsRequest;
import com.duggan.workflow.shared.requests.GetProcessesRequest;
import com.duggan.workflow.shared.requests.MultiRequestAction;
import com.duggan.workflow.shared.responses.CreateFormResponse;
import com.duggan.workflow.shared.responses.ExportFormResponse;
import com.duggan.workflow.shared.responses.GetFormModelResponse;
import com.duggan.workflow.shared.responses.GetFormsResponse;
import com.duggan.workflow.shared.responses.GetProcessesResponse;
import com.duggan.workflow.shared.responses.MultiRequestActionResult;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

public class FormBuilderPresenter extends
		Presenter<FormBuilderPresenter.IFormBuilderView,FormBuilderPresenter.MyProxy> implements
		SavePropertiesHandler, PropertyChangedHandler, SaveFormDesignHandler {

	public interface IFormBuilderView extends View {
		HasClickHandlers getNewButton();
		HasClickHandlers getDeleteButton();
		HasClickHandlers getCloneButton();
		HasClickHandlers getExportButton();
		HasClickHandlers getImportButton();
		InlineLabel getFormLabel();
		Form getForm();
		HasValueChangeHandlers<Form> getFormDropDown();
		void setForm(Form form);
		void setProperty(String property, String value);
		void setForms(ArrayList<Form> forms);
		void activatePalette();
		void registerInputDrag();
		void clear();
		String getFormName();
		void setProcesses(ArrayList<ProcessDef> processes);
		DropDownList<ProcessDef> getProcessDropDown();
		void enableCreateForm(boolean isProcessSelected);
		
	}
	
	@ProxyCodeSplit
	@NameToken(NameTokens.formbuilder)
	@UseGatekeeper(AdminGateKeeper.class)
	public interface MyProxy extends ProxyPlace<FormBuilderPresenter> {
	}
//	public interface MyProxy extends TabContentProxyPlace<FormBuilderPresenter> {
//	}
	
//	@TabInfo(container = AdminHomePresenter.class)
//    static TabData getTabLabel(AdminGateKeeper adminGatekeeper) {
//        return new TabDataExt("Form Builder","icon-edit",4, adminGatekeeper);
//    }

	@Inject
	DispatchAsync dispatcher;
	
	@Inject PlaceManager placeManager;

	Long formId=null;
	String processRefId = null;
	
	@Inject
	public FormBuilderPresenter(final EventBus eventBus,
			final IFormBuilderView view, MyProxy proxy) {
		//super(eventBus, view, proxy,AdminHomePresenter.SLOT_SetTabContent);
		super(eventBus, view, proxy,BaseProcessPresenter.CONTENT_SLOT);
	}
	
	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(SavePropertiesEvent.TYPE, this);
		addRegisteredHandler(PropertyChangedEvent.TYPE, this);
		addRegisteredHandler(SaveFormDesignEvent.TYPE, this);
		getView().getFormDropDown().addValueChangeHandler(
				new ValueChangeHandler<Form>() {

					@Override
					public void onValueChange(ValueChangeEvent<Form> event) {
						getView().registerInputDrag();
						getView().activatePalette();
						getView().clear();

						Form form = event.getValue();

						if (form==null || form.getId() == null) {
							History.newItem("formbuilder;p="+processRefId);
							return;
						}
						
						History.newItem("formbuilder;p="+processRefId+";formid="+form.getId(), false);
						
						loadForm(form.getId());						
					}
				});

		getView().getNewButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				getView().clear();
				getView().registerInputDrag();
				getView().activatePalette();

				Form form = getView().getForm();
				form.setCaption(null);
				form.setName(null);
				form.setId(null);
				saveForm(form);
			}
		});

		getView().getDeleteButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				final Form form = getView().getForm();
				AppManager.showPopUp("Confirm Delete", new HTMLPanel(
						"Do you want to delete form \"" + form.getCaption()+"\""),
						new OnOptionSelected() {

							@Override
							public void onSelect(String button) {
								if (button.equals("Ok")) {
									delete(form);
								}
							}

						}, "Cancel", "Ok");
			}
		});
		
		getView().getCloneButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				final Form form = getView().getForm();				
				Form clone = form.clone();
				//System.err.println(">>>>> :: "+clone+" \n["+clone.getFields()+"] \n"+clone.getProperties());
				saveForm(clone);
			}
		});
		
		getView().getImportButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {

				UploadContext context = new UploadContext();
				context.setAction(UPLOADACTION.IMPORTFORM);
				ArrayList<String> values = ArrayUtil.asList("xml","json");
				context.setAccept(values);
				
				String message = "This will auto import the form, if the form already exists(check based on form name),"
						+ "the form will be renamed to {formName+formId}";
				final ImportView view = new ImportView(message,context);
				view.setAvoidRepeatFiles(false);
				AppManager.showPopUp("Import Form", view, 
						new OnOptionSelected() {
							
							@Override
							public void onSelect(String name) {
								if(name.equals("Save")){
									loadForms();
								}else{
									view.cancelImport();
								}
							}
						}, "Save", "Cancel");
			}
		});
		
		getView().getExportButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				assert formId!=null;
				dispatcher.execute(new ExportFormRequest(formId), 
						new TaskServiceCallback<ExportFormResponse>() {
							@Override
							public void processResult(
									ExportFormResponse aResponse) {
								
								String xml = aResponse.getXml();
								
								InlineLabel area = new InlineLabel(xml);
								
								AppManager.showPopUp("Export '"+getView().getFormName()+"'", 
										area,
										new OnOptionSelected() {
											
											@Override
											public void onSelect(String name) {
												if(name.equals("Save To File")){
													UploadContext context = new UploadContext("getreport");
													context.setContext("formId", formId+"");
													context.setContext("ACTION", "EXPORTFORM");
													String url = context.toUrl();
													
													String moduleUrl = GWT.getModuleBaseURL().replace("/gwtht", "");
													if(moduleUrl.endsWith("/")){
														moduleUrl = moduleUrl.substring(0, moduleUrl.length()-1);
													}
													url = url.replace("/", "");
													moduleUrl =moduleUrl+"/"+url;
													Window.open(moduleUrl, getView().getFormName(), "");
												}
											}
										}, "Save To File", "Done");
							}
						});
				
			}
		});
		
		
		getView().getProcessDropDown().addValueChangeHandler(new ValueChangeHandler<ProcessDef>() {
			@Override
			public void onValueChange(ValueChangeEvent<ProcessDef> event) {
				ProcessDef processDef = event.getValue();
				getView().clear();
				if(processDef==null){
					processChanged(null);
				}else{
					processChanged(processDef);
				}
			}
		});
	}

	protected void processChanged(ProcessDef def) {
		if(def==null){
			processRefId=null;
			placeManager.revealPlace(new PlaceRequest.Builder()
			.nameToken(NameTokens.formbuilder)
			.build());
			return;
		}
		
		
		processRefId = def.getRefId();
		placeManager.revealPlace(new PlaceRequest.Builder()
		.nameToken(NameTokens.formbuilder)
		.with("p", processRefId+"")
		.build());
	}

	private void delete(Form form) {
		MultiRequestAction action = new MultiRequestAction();
		DeleteFormModelRequest request = new DeleteFormModelRequest(form);
		action.addRequest(request);
		
		GetFormsRequest formsRequest = new GetFormsRequest(processRefId);
		action.addRequest(formsRequest);
		
		dispatcher.execute(action,
				new TaskServiceCallback<MultiRequestActionResult>() {
					@Override
					public void processResult(MultiRequestActionResult result) {
						getView().clear();
						getView().setForm(new Form());
						
						GetFormsResponse resp = (GetFormsResponse)result.get(1);
						getView().setForms(resp.getForms());
					}
				});
	}
	
	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		getView().clear();
		fireEvent(new ProcessChildLoadedEvent(this));
		processRefId = request.getParameter("p",null);
		
		String value = request.getParameter("formid", "");	
		Long formId=null;
		
		if(!value.isEmpty() && value.matches("[0-9]+")){
			formId = new Long(value);
		}
		setFormId(formId);
		
		boolean isProcessSelected = processRefId!=null || !processRefId.equals(0L);
		getView().enableCreateForm(isProcessSelected);		
		
		if(isProcessSelected){
			loadForms();
		}else{
			loadProcesses();
		}
	}

	protected void selectProcess(ProcessDef def) {
		getView().getProcessDropDown().setValue(def);
	}

	protected void loadForm(Long id) {
		GetFormModelRequest request = new GetFormModelRequest(Form.FORMMODEL,
				id, true);
		setFormId(id);
		dispatcher.execute(request,
				new TaskServiceCallback<GetFormModelResponse>() {
					@Override
					public void processResult(GetFormModelResponse result) {
						Form form = (Form) result.getFormModel().get(0);
						getView().setForm(form);
					}
				});
	}
	

	protected void saveForm(Form form) {
		form.setProcessRefId(processRefId);
		assert form.getProcessRefId()!=null;
		
		this.formId=null;
		MultiRequestAction action = new MultiRequestAction();
		action.addRequest(new CreateFormRequest(form));
		
		GetFormsRequest formsRequest = new GetFormsRequest(processRefId);
		action.addRequest(formsRequest);
		dispatcher.execute(action,
				new TaskServiceCallback<MultiRequestActionResult>() {
					@Override
					public void processResult(MultiRequestActionResult result) {
						CreateFormResponse createResp = (CreateFormResponse)result.get(0);
						Form form=createResp.getForm();
						setFormId(form.getId());
						getView().setForm(form);
						
						GetFormsResponse response = (GetFormsResponse)result.get(1);
						getView().setForms(response.getForms());
					}
				});
	}
	
	private void loadForms() {
		MultiRequestAction action = new MultiRequestAction();
		action.addRequest(new GetProcessesRequest(false));
		action.addRequest(new GetFormsRequest(processRefId));
		
		if(formId!=null){
			GetFormModelRequest request = new GetFormModelRequest(Form.FORMMODEL,
					formId, true);
			action.addRequest(request);
		}
		
		dispatcher.execute(action, new TaskServiceCallback<MultiRequestActionResult>() {
			@Override
			public void processResult(MultiRequestActionResult results) {
				GetProcessesResponse aResponse = (GetProcessesResponse) results.get(0);
				getView().setProcesses(aResponse.getProcesses());
				if(processRefId!=null)
				for(ProcessDef d: aResponse.getProcesses()){
					if(d.getId().equals(processRefId)){
						selectProcess(d);
					}
				}
				
				//Forms ArrayList
				getView().setForms(((GetFormsResponse)results.get(1)).getForms());
				
				//Form Models
				if(results.getReponses().size()>2){
					GetFormModelResponse response = (GetFormModelResponse)results.get(2);
					Form form = (Form) response.getFormModel().get(0);
					getView().setForm(form);
				}
			}
		});
	}
	
	private void loadProcesses() {
		GetProcessesRequest request = new GetProcessesRequest(false);
		dispatcher.execute(request, new ServiceCallback<GetProcessesResponse>() {
			@Override
			public void processResult(GetProcessesResponse aResponse) {
				getView().setProcesses(aResponse.getProcesses());
				if(processRefId==null && !aResponse.getProcesses().isEmpty()){
					ProcessDef process = aResponse.getProcesses().get(0);
					processChanged(process);
				}
			}
		});
	}
	
	@Override
	public void onSaveProperties(SavePropertiesEvent event) {
		if (event.getParent() != null
				&& event.getParent().equals(getView().getForm())) {
			saveForm(getView().getForm());
		}
	}

	@Override
	public void onPropertyChanged(PropertyChangedEvent event) {
		if (event.isForField())
			return;

		String property = event.getPropertyName();
		String value = event.getPropertyValue().toString();

		getView().setProperty(property, value);
		//saveForm(getView().getForm());
	}

	@Override
	public void onSaveFormDesign(SaveFormDesignEvent event) {
		saveForm(getView().getForm());
	}

	public void setFormId(Long formId) {
		this.formId = formId;
	}

}
