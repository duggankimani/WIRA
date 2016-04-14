package com.duggan.workflow.client.ui.admin.processes;

import java.util.List;

import com.duggan.workflow.client.event.CheckboxSelectionEvent;
import com.duggan.workflow.client.event.CheckboxSelectionEvent.CheckboxSelectionHandler;
import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.service.ServiceCallback;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.AppManager;
import com.duggan.workflow.client.ui.OnOptionSelected;
import com.duggan.workflow.client.ui.OptionControl;
import com.duggan.workflow.client.ui.admin.AdminHomePresenter;
import com.duggan.workflow.client.ui.admin.TabDataExt;
import com.duggan.workflow.client.ui.admin.processes.category.CreateCategoryPanel;
import com.duggan.workflow.client.ui.admin.processes.save.ProcessSavePresenter;
import com.duggan.workflow.client.ui.admin.processitem.ProcessItemPresenter;
import com.duggan.workflow.client.ui.admin.processitem.ProcessStepsPresenter;
import com.duggan.workflow.client.ui.admin.processmgt.BaseProcessPresenter;
import com.duggan.workflow.client.ui.events.EditProcessEvent;
import com.duggan.workflow.client.ui.events.EditProcessEvent.EditProcessHandler;
import com.duggan.workflow.client.ui.events.LoadProcessesEvent;
import com.duggan.workflow.client.ui.events.LoadProcessesEvent.LoadProcessesHandler;
import com.duggan.workflow.client.ui.events.ProcessingCompletedEvent;
import com.duggan.workflow.client.ui.events.ProcessingEvent;
import com.duggan.workflow.client.ui.security.AdminGateKeeper;
import com.duggan.workflow.shared.model.ManageProcessAction;
import com.duggan.workflow.shared.model.ProcessCategory;
import com.duggan.workflow.shared.model.ProcessDef;
import com.duggan.workflow.shared.requests.DeleteProcessRequest;
import com.duggan.workflow.shared.requests.GetProcessCategoriesRequest;
import com.duggan.workflow.shared.requests.GetProcessRequest;
import com.duggan.workflow.shared.requests.GetProcessesRequest;
import com.duggan.workflow.shared.requests.ManageKnowledgeBaseRequest;
import com.duggan.workflow.shared.requests.MultiRequestAction;
import com.duggan.workflow.shared.requests.SaveProcessCategoryRequest;
import com.duggan.workflow.shared.requests.StartAllProcessesRequest;
import com.duggan.workflow.shared.responses.DeleteProcessResponse;
import com.duggan.workflow.shared.responses.GetProcessCategoriesResponse;
import com.duggan.workflow.shared.responses.GetProcessResponse;
import com.duggan.workflow.shared.responses.GetProcessesResponse;
import com.duggan.workflow.shared.responses.ManageKnowledgeBaseResponse;
import com.duggan.workflow.shared.responses.MultiRequestActionResult;
import com.duggan.workflow.shared.responses.SaveProcessCategoryResponse;
import com.duggan.workflow.shared.responses.StartAllProcessesResponse;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.common.client.IndirectProvider;
import com.gwtplatform.common.client.StandardProvider;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.TabData;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.TabInfo;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

public class ProcessPresenter extends
		Presenter<ProcessPresenter.IProcessView, ProcessPresenter.MyProxy>
		implements LoadProcessesHandler, EditProcessHandler,
		CheckboxSelectionHandler {

	public interface IProcessView extends View {

		HasClickHandlers getaNewProcess();

		HasClickHandlers getStartAllProcesses();

		void setCategories(List<ProcessCategory> categories);

		HasClickHandlers getAddCategories();

		void bindProcesses(List<ProcessDef> processDefinitions);

		void setProcessEdit(ProcessDef model, boolean value);

		HasClickHandlers getActivateButton();

		HasClickHandlers getDeactivateButton();

		HasClickHandlers getRefreshButton();

		HasClickHandlers getEditButton();

		HasClickHandlers getDeleteButton();
		
		HasClickHandlers getConfigureButton();

		void setConfigState(boolean b);

		void setProcess(ProcessDef processDef);

	}

	public static final String ACTION_CONFIG = "config";
	
	@Inject
	DispatchAsync requestHelper;
	
	public static final Object TABLE_SLOT = new Object();
	
	@Inject PlaceManager placeManager;

	IndirectProvider<ProcessSavePresenter> processFactory;
	IndirectProvider<ProcessItemPresenter> processItemFactory;
	IndirectProvider<ProcessStepsPresenter> taskStepsFactory;

	private List<ProcessCategory> categories;

	private Object selectedModel;

	@ProxyCodeSplit
	@NameToken(NameTokens.processes)
	@UseGatekeeper(AdminGateKeeper.class)
	public interface MyProxy extends ProxyPlace<ProcessPresenter> {
	}

//	@TabInfo(container = AdminHomePresenter.class)
//	static TabData getTabLabel(AdminGateKeeper adminGatekeeper) {
//		return new TabDataExt("Processes", "icon-cogs", 2, adminGatekeeper);
//	}

	@Inject
	public ProcessPresenter(final EventBus eventBus, final IProcessView view,
			final MyProxy proxy,
			Provider<ProcessSavePresenter> addprocessProvider,
			Provider<ProcessItemPresenter> columnProvider,
			Provider<ProcessStepsPresenter> taskStepsProvider) {
//		super(eventBus, view, proxy, AdminHomePresenter.SLOT_SetTabContent);
		super(eventBus, view, proxy, BaseProcessPresenter.CONTENT_SLOT);
		processFactory = new StandardProvider<ProcessSavePresenter>(
				addprocessProvider);
		processItemFactory = new StandardProvider<ProcessItemPresenter>(
				columnProvider);
		taskStepsFactory = new StandardProvider<ProcessStepsPresenter>(
				taskStepsProvider);
	}

	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(LoadProcessesEvent.TYPE, this);
		addRegisteredHandler(EditProcessEvent.TYPE, this);
		addRegisteredHandler(CheckboxSelectionEvent.getType(), this);

		getView().getaNewProcess().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				showAddProcessPopup();
			}
		});

		getView().getAddCategories().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				final CreateCategoryPanel panel = new CreateCategoryPanel(
						categories);
				AppManager.showPopUp("Add/Edit Categories", panel,
						new OptionControl() {
							@Override
							public void onSelect(String name) {
								if (name.equals("Save")) {
									if (panel.isValid()) {
										saveCategory(panel.getCategory());
										hide();
									}
								} else {
									hide();
								}
							}

						}, "Save", "Cancel");
			}
		});

		getView().getStartAllProcesses().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				fireEvent(new ProcessingEvent("Starting processes"));
				requestHelper.execute(new StartAllProcessesRequest(),
						new TaskServiceCallback<StartAllProcessesResponse>() {
							@Override
							public void processResult(
									StartAllProcessesResponse aResponse) {
								loadProcesses();
							}
						});
			}
		});

		getView().getActivateButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ProcessDef processDef = (ProcessDef) selectedModel;
				ManageKnowledgeBaseRequest request = new ManageKnowledgeBaseRequest(
						processDef.getId(), ManageProcessAction.ACTIVATE, false);
				submit(request);
			}
		});

		getView().getDeactivateButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ProcessDef processDef = (ProcessDef) selectedModel;
				ManageKnowledgeBaseRequest request = new ManageKnowledgeBaseRequest(
						processDef.getId(), ManageProcessAction.DEACTIVATE,
						false);
				submit(request);
			}
		});
		

		getView().getConfigureButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				ProcessDef processDef = (ProcessDef) selectedModel;
				placeManager.revealPlace(new PlaceRequest.Builder().nameToken(NameTokens.processes)
						.with("a", ACTION_CONFIG)
						.with("pd", processDef.getRefId()).build());
			}
		});

		getView().getEditButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ProcessDef processDef = (ProcessDef) selectedModel;
				fireEvent(new EditProcessEvent(processDef.getId()));
			}
		});

		getView().getRefreshButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ProcessDef processDef = (ProcessDef) selectedModel;
				ManageKnowledgeBaseRequest request = new ManageKnowledgeBaseRequest(
						processDef.getId(), ManageProcessAction.ACTIVATE, true);
				submit(request);
			}
		});

		getView().getDeleteButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				final ProcessDef processDef = (ProcessDef) selectedModel;
				AppManager.showPopUp("Confirm Delete",
						new InlineLabel("Do you want to delete process '"
								+ processDef.getName() + "'"),
						new OnOptionSelected() {

							@Override
							public void onSelect(String name) {
								if (name.equals("Yes")) {

									DeleteProcessRequest request = new DeleteProcessRequest(
											processDef.getId());

									requestHelper
											.execute(
													request,
													new ServiceCallback<DeleteProcessResponse>() {
														@Override
														public void processResult(
																DeleteProcessResponse result) {
															loadProcesses();
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
		requestHelper.execute(request,
				new TaskServiceCallback<ManageKnowledgeBaseResponse>() {
					@Override
					public void processResult(ManageKnowledgeBaseResponse result) {
						fireEvent(new ProcessingCompletedEvent());
						loadProcesses();
					}
				});
	}

	private void saveCategory(ProcessCategory category) {
		fireEvent(new ProcessingEvent());
		MultiRequestAction action = new MultiRequestAction();
		action.addRequest(new SaveProcessCategoryRequest(category));
		action.addRequest(new GetProcessCategoriesRequest());

		requestHelper.execute(action,
				new TaskServiceCallback<MultiRequestActionResult>() {
					@Override
					public void processResult(MultiRequestActionResult result) {

						SaveProcessCategoryResponse processesResponse = (SaveProcessCategoryResponse) result
								.get(0);
						ProcessCategory category = processesResponse
								.getCategory();

						GetProcessCategoriesResponse response = (GetProcessCategoriesResponse) result
								.get(1);
						bindCategories(response.getCategories());
						fireEvent(new ProcessingCompletedEvent());

					}
				});
	}

	protected void bindCategories(List<ProcessCategory> categories) {
		this.categories = categories;
		getView().setCategories(categories);
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		
		String action = request.getParameter("a", null);
		String processRefId = request.getParameter("pd", null);
		
		if(action!=null && processRefId!=null){
			if(action.equals(ACTION_CONFIG)){
				getView().setConfigState(true);
				loadProcess(processRefId);
			}
		}else{
			getView().setConfigState(false);
			loadProcesses();
		}
		
		
	}

	private void loadProcess(String processRefId) {
		GetProcessRequest request  = new GetProcessRequest(processRefId);
		requestHelper.execute(request, new TaskServiceCallback<GetProcessResponse>() {
			@Override
			public void processResult(GetProcessResponse aResponse) {
				final ProcessDef processDef = aResponse.getProcessDef();
				getView().setProcess(processDef);

				// Task Steps
				taskStepsFactory
						.get(new ServiceCallback<ProcessStepsPresenter>() {
							@Override
							public void processResult(
									ProcessStepsPresenter aResponse) {
								aResponse.setProcess(processDef);
								aResponse.load();
								setInSlot(TABLE_SLOT, aResponse);
							}
						});
			}
		});
		
	}

	private void showAddProcessPopup() {
		showAddProcessPopup(null);
	}

	private void showAddProcessPopup(final Long processDefId) {
		processFactory.get(new ServiceCallback<ProcessSavePresenter>() {
			@Override
			public void processResult(ProcessSavePresenter result) {
				result.setProcessDefId(processDefId);
				addToPopupSlot(result, false);
			}
		});

	}

	public void loadProcesses() {

		fireEvent(new ProcessingEvent());
		MultiRequestAction action = new MultiRequestAction();
		action.addRequest(new GetProcessesRequest(true));
		action.addRequest(new GetProcessCategoriesRequest());

		requestHelper.execute(action,
				new TaskServiceCallback<MultiRequestActionResult>() {
					@Override
					public void processResult(MultiRequestActionResult result) {

						GetProcessesResponse processesResponse = (GetProcessesResponse) result
								.get(0);
						List<ProcessDef> processDefinitions = processesResponse
								.getProcesses();
						getView().bindProcesses(processDefinitions);

						GetProcessCategoriesResponse response = (GetProcessCategoriesResponse) result
								.get(1);
						bindCategories(response.getCategories());
						fireEvent(new ProcessingCompletedEvent());
						//deselect
						fireEvent(new CheckboxSelectionEvent(selectedModel, false));

					}
				});
	}

	@Override
	public void onLoadProcesses(LoadProcessesEvent event) {
		loadProcesses();
	}

	@Override
	public void onEditProcess(EditProcessEvent event) {
		Long processDefId = event.getProcessId();
		showAddProcessPopup(processDefId);
	}

	@Override
	public void onCheckboxSelection(CheckboxSelectionEvent event) {
		selectedModel = event.getModel();
		selectItem(selectedModel, event.getValue());

		if (!event.getValue()) {
			selectedModel = null;
		}
	}

	private void selectItem(Object model, boolean value) {

		if (model instanceof ProcessDef) {
			getView().setProcessEdit((ProcessDef) model, value);
		}

	}

}