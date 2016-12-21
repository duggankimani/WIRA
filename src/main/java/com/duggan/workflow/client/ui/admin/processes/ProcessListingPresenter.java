package com.duggan.workflow.client.ui.admin.processes;

import java.util.ArrayList;

import com.duggan.workflow.client.event.CheckboxSelectionEvent;
import com.duggan.workflow.client.event.CheckboxSelectionEvent.CheckboxSelectionHandler;
import com.duggan.workflow.client.event.ProcessChildLoadedEvent;
import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.AppManager;
import com.duggan.workflow.client.ui.OnOptionSelected;
import com.duggan.workflow.client.ui.OptionControl;
import com.duggan.workflow.client.ui.admin.process.ProcessPresenter;
import com.duggan.workflow.client.ui.admin.processes.category.CreateCategoryPanel;
import com.duggan.workflow.client.ui.admin.processes.save.ProcessSavePanel;
import com.duggan.workflow.client.ui.admin.processmgt.BaseProcessPresenter;
import com.duggan.workflow.client.ui.events.EditProcessEvent;
import com.duggan.workflow.client.ui.events.EditProcessEvent.EditProcessHandler;
import com.duggan.workflow.client.ui.events.LoadProcessesEvent;
import com.duggan.workflow.client.ui.events.LoadProcessesEvent.LoadProcessesHandler;
import com.duggan.workflow.client.ui.events.ProcessingCompletedEvent;
import com.duggan.workflow.client.ui.events.ProcessingEvent;
import com.duggan.workflow.client.ui.events.SearchEvent;
import com.duggan.workflow.client.ui.events.SearchEvent.SearchHandler;
import com.duggan.workflow.client.ui.security.HasPermissionsGateKeeper;
import com.duggan.workflow.shared.model.ManageProcessAction;
import com.duggan.workflow.shared.model.ProcessCategory;
import com.duggan.workflow.shared.model.ProcessDef;
import com.duggan.workflow.shared.requests.DeleteProcessRequest;
import com.duggan.workflow.shared.requests.GetProcessCategoriesRequest;
import com.duggan.workflow.shared.requests.GetProcessesRequest;
import com.duggan.workflow.shared.requests.ManageKnowledgeBaseRequest;
import com.duggan.workflow.shared.requests.MultiRequestAction;
import com.duggan.workflow.shared.requests.SaveProcessCategoryRequest;
import com.duggan.workflow.shared.requests.SaveProcessRequest;
import com.duggan.workflow.shared.requests.StartAllProcessesRequest;
import com.duggan.workflow.shared.responses.DeleteProcessResponse;
import com.duggan.workflow.shared.responses.GetProcessCategoriesResponse;
import com.duggan.workflow.shared.responses.GetProcessesResponse;
import com.duggan.workflow.shared.responses.ManageKnowledgeBaseResponse;
import com.duggan.workflow.shared.responses.MultiRequestActionResult;
import com.duggan.workflow.shared.responses.SaveProcessCategoryResponse;
import com.duggan.workflow.shared.responses.SaveProcessResponse;
import com.duggan.workflow.shared.responses.StartAllProcessesResponse;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.GatekeeperParams;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.wira.commons.client.service.ServiceCallback;

public class ProcessListingPresenter
		extends
		Presenter<ProcessListingPresenter.MyView, ProcessListingPresenter.MyProxy>
		implements LoadProcessesHandler, EditProcessHandler,
		CheckboxSelectionHandler, SearchHandler {

	interface MyView extends View {
		HasClickHandlers getaNewProcess();

		HasClickHandlers getStartAllProcesses();
		
		HasClickHandlers getDownloadButton();

		void setCategories(ArrayList<ProcessCategory> categories);

		HasClickHandlers getAddCategories();

		void bindProcesses(ArrayList<ProcessDef> processDefinitions);

		void setProcessEdit(ProcessDef model, boolean value);

		HasClickHandlers getActivateButton();

		HasClickHandlers getDeactivateButton();

		HasClickHandlers getRefreshButton();

		HasClickHandlers getEditButton();

		HasClickHandlers getDeleteButton();

		HasClickHandlers getConfigureButton();

	}

	public static final String CAN_VIEW_PROCESSES = "PROCESSES_CAN_VIEW_PROCESSES";

	@NameToken(NameTokens.processlist)
	@ProxyStandard
	@UseGatekeeper(HasPermissionsGateKeeper.class)
	@GatekeeperParams({ CAN_VIEW_PROCESSES })
	interface MyProxy extends ProxyPlace<ProcessListingPresenter> {
	}

	@Inject
	DispatchAsync requestHelper;

	@Inject
	PlaceManager placeManager;

	private ArrayList<ProcessCategory> categories;

	private Object selectedModel;

	@Inject
	ProcessListingPresenter(EventBus eventBus, MyView view, MyProxy proxy) {
		super(eventBus, view, proxy, BaseProcessPresenter.CONTENT_SLOT);
	}

	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(LoadProcessesEvent.TYPE, this);
		addRegisteredHandler(EditProcessEvent.TYPE, this);
		addRegisteredHandler(CheckboxSelectionEvent.getType(), this);
		addRegisteredHandler(SearchEvent.getType(), this);

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
				placeManager.revealPlace(new PlaceRequest.Builder()
						.nameToken(NameTokens.processes)
						.with("a", ProcessPresenter.ACTION_PREVIEW)
						.with("p", processDef.getRefId()).build());
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
		
		getView().getDownloadButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				ProcessDef def = (ProcessDef)selectedModel;
				String url = "getreport?action=exportprocess&processRefId="+def.getRefId();
				
				String moduleUrl = GWT.getModuleBaseURL().replace("/gwtht", "");
				if(moduleUrl.endsWith("/")){
					moduleUrl = moduleUrl.substring(0, moduleUrl.length()-1);
				}
				url = url.replace("/", "");
				moduleUrl =moduleUrl+"/"+url;
				Window.open(moduleUrl, def.getName()+".zip", null);
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

	protected void bindCategories(ArrayList<ProcessCategory> categories) {
		this.categories = categories;
		getView().setCategories(categories);
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		fireEvent(new ProcessChildLoadedEvent(this));
		loadProcesses();
	}

	private void showAddProcessPopup() {
		showAddProcessPopup(null);
	}

	private void showAddProcessPopup(final Long processDefId) {
		final ProcessSavePanel view = new ProcessSavePanel(processDefId);

		String heading = processDefId == null ? "New Process" : "Edit Process";
		AppManager.showPopUp(heading, view, new OnOptionSelected() {

			@Override
			public void onSelect(String name) {
				if (name.equals("Save")) {
					if (view.isValid()) {
						
						ProcessDef process = view.getProcess();
						saveProcess(process);
					}
				}
			}
		}, "Cancel", "Save");

	}

	protected void saveProcess(ProcessDef process) {

		SaveProcessRequest request = new SaveProcessRequest(process);
		requestHelper.execute(request,
				new TaskServiceCallback<SaveProcessResponse>() {
					@Override
					public void processResult(SaveProcessResponse result) {
						loadProcesses();
					}
				});
	}

	public void loadProcesses() {
		loadProcesses("");
	}

	public void loadProcesses(String searchTerm) {

		fireEvent(new ProcessingEvent());
		MultiRequestAction action = new MultiRequestAction();
		action.addRequest(new GetProcessesRequest(searchTerm, true));
		action.addRequest(new GetProcessCategoriesRequest());

		requestHelper.execute(action,
				new TaskServiceCallback<MultiRequestActionResult>() {
					@Override
					public void processResult(MultiRequestActionResult result) {

						GetProcessesResponse processesResponse = (GetProcessesResponse) result
								.get(0);
						ArrayList<ProcessDef> processDefinitions = processesResponse
								.getProcesses();
						getView().bindProcesses(processDefinitions);

						GetProcessCategoriesResponse response = (GetProcessCategoriesResponse) result
								.get(1);
						bindCategories(response.getCategories());
						fireEvent(new ProcessingCompletedEvent());
						// deselect
						fireEvent(new CheckboxSelectionEvent(selectedModel,
								false));

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

	@Override
	public void onSearch(SearchEvent event) {
		if (isVisible()) {
			loadProcesses(event.getFilter().getPhrase());
		}
	}
}