package com.duggan.workflow.client.ui.task;

import java.util.ArrayList;
import java.util.HashMap;

import com.duggan.workflow.client.event.ShowMessageEvent;
import com.duggan.workflow.client.model.TaskType;
import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.AlertType;
import com.duggan.workflow.client.ui.document.GenericDocumentPresenter;
import com.duggan.workflow.client.ui.events.AfterSaveEvent;
import com.duggan.workflow.client.ui.events.AfterSaveEvent.AfterSaveHandler;
import com.duggan.workflow.client.ui.events.AfterSearchEvent;
import com.duggan.workflow.client.ui.events.AssignTaskEvent;
import com.duggan.workflow.client.ui.events.AssignTaskEvent.AssignTaskHandler;
import com.duggan.workflow.client.ui.events.DocumentSelectionEvent;
import com.duggan.workflow.client.ui.events.DocumentSelectionEvent.DocumentSelectionHandler;
import com.duggan.workflow.client.ui.events.ProcessingCompletedEvent;
import com.duggan.workflow.client.ui.events.ProcessingEvent;
import com.duggan.workflow.client.ui.events.ReloadDocumentEvent;
import com.duggan.workflow.client.ui.events.ReloadEvent;
import com.duggan.workflow.client.ui.events.ReloadEvent.ReloadHandler;
import com.duggan.workflow.client.ui.events.SearchEvent;
import com.duggan.workflow.client.ui.events.WorkflowProcessEvent;
import com.duggan.workflow.client.ui.events.SearchEvent.SearchHandler;
import com.duggan.workflow.client.ui.filter.FilterPresenter;
import com.duggan.workflow.client.ui.home.HomePresenter;
import com.duggan.workflow.client.ui.tasklistitem.DateGroupPresenter;
import com.duggan.workflow.client.ui.util.DocMode;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.event.LoadDataEvent;
import com.duggan.workflow.shared.event.LoadDataEvent.LoadDataHandler;
import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.model.DocStatus;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.HTSummary;
import com.duggan.workflow.shared.model.MODE;
import com.duggan.workflow.shared.model.ProcessDef;
import com.duggan.workflow.shared.model.Schema;
import com.duggan.workflow.shared.model.GenericFilter;
import com.duggan.workflow.shared.requests.AssignTaskRequest;
import com.duggan.workflow.shared.requests.GetProcessRequest;
import com.duggan.workflow.shared.requests.GetProcessSchemaRequest;
import com.duggan.workflow.shared.requests.GetTaskList;
import com.duggan.workflow.shared.requests.MultiRequestAction;
import com.duggan.workflow.shared.responses.GetProcessResponse;
import com.duggan.workflow.shared.responses.GetProcessSchemaResponse;
import com.duggan.workflow.shared.responses.GetTaskListResult;
import com.duggan.workflow.shared.responses.MultiRequestActionResult;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.common.client.IndirectProvider;
import com.gwtplatform.common.client.StandardProvider;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.presenter.slots.SingleSlot;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.wira.commons.client.service.ServiceCallback;
import com.wira.commons.shared.response.BaseResponse;

public abstract class AbstractTaskPresenter<V extends AbstractTaskPresenter.ITaskView, Proxy_ extends Proxy<?>>
		extends Presenter<AbstractTaskPresenter.ITaskView, Proxy<?>>
		implements AfterSaveHandler, DocumentSelectionHandler, ReloadHandler, 
		SearchHandler, AssignTaskHandler, LoadDataHandler {

	public interface ITaskView extends View {

		void setTaskType(TaskType currentTaskType);

		void addScrollHandler(ScrollHandler scrollHandler);

		void bindTasks(ArrayList<Doc> tasks, int currentPage, int totalCount, boolean isIncremental);

		void bindAlerts(HashMap<TaskType, Integer> alerts);

		void bindProcess(ProcessDef processDef);

		void setProcessRefId(String processRefId);

		void bindProcessSchema(ArrayList<Schema> schema);
	}

	public static final SingleSlot<GenericDocumentPresenter> DOCUMENT_SLOT = new SingleSlot<GenericDocumentPresenter>();

	public static final SingleSlot<GenericDocumentPresenter> FILTER_SLOT = new SingleSlot<GenericDocumentPresenter>();

	@Inject
	DispatchAsync dispatcher;
	@Inject
	PlaceManager placeManager;

	// public static final Object DATEGROUP_SLOT = new Object();
	private IndirectProvider<GenericDocumentPresenter> docViewFactory;
	// private IndirectProvider<DateGroupPresenter> dateGroupFactory;

	protected static TaskType currentTaskType;

	/**
	 * Url processInstanceId (pid) - required incase the use hits refresh
	 */
	private Long processInstanceId = null;

	/**
	 * Url documentId (did) - required incase the use hits refresh
	 */
	// private Long documentId=null;
	private String docRefId = null;

	private Long taskId = null;

	String searchTerm = "";

	// Form Mode - Edit/ View - Applicable for Drafts only
	MODE mode = null;

	@Inject
	FilterPresenter filterPresenter;

	Timer timer = new Timer() {

		@Override
		public void run() {
			search();
		}
	};

	protected static final int PAGE_SIZE = 10;

	protected int currentPage = 1;
	
	protected int CURPOS = 0;

	private boolean isLoadingData;

	private String processRefId;

	public AbstractTaskPresenter(final EventBus eventBus, final ITaskView view, final Proxy_ proxy,
			Provider<GenericDocumentPresenter> docViewProvider, Provider<DateGroupPresenter> dateGroupProvider) {
		super(eventBus, view, proxy, HomePresenter.SLOT_SetTabContent);
		docViewFactory = new StandardProvider<GenericDocumentPresenter>(docViewProvider);
		// dateGroupFactory = new StandardProvider<DateGroupPresenter>(
		// dateGroupProvider);
	}

	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(AfterSaveEvent.TYPE, this);
		addRegisteredHandler(DocumentSelectionEvent.TYPE, this);
		addRegisteredHandler(ReloadEvent.TYPE, this);
		addRegisteredHandler(AssignTaskEvent.TYPE, this);
		addRegisteredHandler(SearchEvent.TYPE, this);
		addRegisteredHandler(LoadDataEvent.TYPE, this);

		getView().addScrollHandler(new ScrollHandler() {
			@Override
			public void onScroll(ScrollEvent event) {
				// Window.alert("Scrolled!");
				/*ScrollPanel panel = (ScrollPanel) event.getSource();
				int max = panel.getMaximumVerticalScrollPosition();
				int currentPos = panel.getVerticalScrollPosition();
				if (currentPos == max && !isLoadingData) {
					loadTasks(currentTaskType, true);
				}*/
			}
		});

	}

	/**
	 * 
	 */
	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		processRefId = request.getParameter("processRefId", null);
		String taskIdStr = request.getParameter("tid", null);
		if (taskIdStr != null) {
			taskId = Long.parseLong(taskIdStr);
		} else {
			taskId = null;
		}

		getView().setTaskType(currentTaskType);
		getView().setProcessRefId(processRefId);

		resetPage();
		
		clear();
		processInstanceId = null;

		String processInstID = request.getParameter("pid", null);
		docRefId = request.getParameter("docRefId", null);

		if (processInstID != null) {
			processInstanceId = Long.parseLong(processInstID);
		}

		loadTasks();

	}

	private void resetPage() {
		CURPOS = 0;
		currentPage = 1;
	}

	protected void search() {
		timer.cancel();
		if (searchTerm.isEmpty()) {
			loadTasks();
			return;
		}

		GenericFilter filter = new GenericFilter();
		filter.setSubject(searchTerm);
		search(filter);
	}

	public void search(final GenericFilter filter) {

		resetPage();
		GetTaskList request = new GetTaskList(processRefId, AppContext.getUserId(), filter);
		request.setLength(PAGE_SIZE);
		request.setOffset(CURPOS = 0);
		fireEvent(new ProcessingEvent());
		dispatcher.execute(request, new TaskServiceCallback<GetTaskListResult>() {
			@Override
			public void processResult(GetTaskListResult result) {

				GetTaskListResult rst = (GetTaskListResult) result;
				ArrayList<Doc> tasks = rst.getTasks();
				loadLines(tasks, rst.getTotalCount());
				fireEvent(new AfterSearchEvent(filter.getSubject(), filter.getPhrase()));
				fireEvent(new ProcessingCompletedEvent());
			}
		});
	}

	private void clear() {
		// clear document slot
		// setInSlot(DATEGROUP_SLOT, null);
		setInSlot(DOCUMENT_SLOT, null);
	}

	private void loadTasks() {
		loadTasks(currentTaskType, false);
	}

	/**
	 * Load JBPM records
	 * 
	 * @param type
	 */
	private void loadTasks(final TaskType type, final boolean isIncremental) {
		beforeDataLoaded();

		clear();

		String userId = AppContext.getUserId();

		MultiRequestAction action = new MultiRequestAction();
		if (processRefId != null) {
			action.addRequest(new GetProcessRequest(processRefId));
			action.addRequest(new GetProcessSchemaRequest(processRefId));
		}

		GetTaskList request = new GetTaskList(processRefId, userId, currentTaskType);
		request.setOffset(CURPOS);
		request.setLength(PAGE_SIZE);
		request.setProcessInstanceId(processInstanceId);
		request.setTaskId(taskId);
		request.setDocRefId(docRefId);
		request.setLoadAsAdmin(isLoadAsAdmin());

		action.addRequest(request);

		fireEvent(new ProcessingEvent());
		dispatcher.execute(action, new TaskServiceCallback<MultiRequestActionResult>() {
			@Override
			public void processResult(MultiRequestActionResult result) {
				int i = 0;

				if (processRefId != null) {
					GetProcessResponse getProcess = (GetProcessResponse) result.get(i++);
					getView().bindProcess(getProcess.getProcessDef());

					GetProcessSchemaResponse getSchema = (GetProcessSchemaResponse) result.get(i++);
					getView().bindProcessSchema(getSchema.getSchema());
				} else {
					getView().bindProcess(null);
				}

				GetTaskListResult rst = (GetTaskListResult) result.get(i++);
				ArrayList<Doc> tasks = rst.getTasks();
				loadLines(tasks, rst.getTotalCount(), isIncremental);

				//if (tasks.size() > 0 && !isIncremental) {
				if (tasks.size() == 1 && !isIncremental) {

					Doc doc = tasks.get(0);
					String docRefId = null;
					DocMode docMode = DocMode.READ;

					if (doc instanceof Document) {
						docRefId = doc.getRefId();

						if (((Document) doc).getStatus() == DocStatus.DRAFTED) {
							docMode = DocMode.READWRITE;
						}
						// Load document
						if (currentTaskType == TaskType.SEARCH && (mode == MODE.EDIT || mode == MODE.CREATE)) {
							fireEvent(new DocumentSelectionEvent(docRefId, null, docMode));
						}
					} else {
						docRefId = ((HTSummary) doc).getRefId();
						long taskId = ((HTSummary) doc).getId();
						// Load Task
						fireEvent(new DocumentSelectionEvent(docRefId, taskId, docMode));
					}

				}
				afterDataLoaded();
				fireEvent(new ProcessingCompletedEvent());
			}

		});
	}

	protected void afterDataLoaded() {
		isLoadingData = false;
	}

	protected void beforeDataLoaded() {
		isLoadingData = true;
	}

	/**
	 * 
	 * @param tasks
	 */
	protected void loadLines(final ArrayList<Doc> tasks, int totalCount) {
		loadLines(tasks, totalCount, false);
	}

	protected void loadLines(final ArrayList<Doc> tasks, int totalCount, boolean isIncremental) {
		CURPOS = CURPOS + PAGE_SIZE;
		// if (!isIncremental) {
		// setInSlot(DATEGROUP_SLOT, null);
		// }
		getView().bindTasks(tasks, currentPage, totalCount, isIncremental);

		// final ArrayList<Date> dates = new ArrayList<Date>();
		//
		// for (int i = 0; i < tasks.size(); i++) {
		// // final String dt =
		// // DateUtils.FULLDATEFORMAT.format(tasks.get(i).getCreated());
		// final Doc doc = tasks.get(i);
		//
		// Date dateToUse = doc.getSortDate();
		//
		// final String dt = DateUtils.LONGDATEFORMAT.format(dateToUse);
		// final Date date = DateUtils.LONGDATEFORMAT.parse(dt);
		//
		// if (dates.contains(date)) {
		// fireEvent(new PresentTaskEvent(doc));
		// } else {
		// dateGroupFactory.get(new ServiceCallback<DateGroupPresenter>() {
		// @Override
		// public void processResult(DateGroupPresenter result) {
		//
		// result.setDate(date);
		//
		// addToSlot(DATEGROUP_SLOT, result);
		// fireEvent(new PresentTaskEvent(doc));
		// dates.add(date);
		// }
		// });
		//
		// }
		// }

	}

	@Override
	protected void onReset() {
		super.onReset();
		setInSlot(FILTER_SLOT, filterPresenter);
	}

	@Override
	public void onAfterSave(AfterSaveEvent event) {
		if (this.isVisible()) {
			resetPage();
			loadTasks();
		}
	}

	/**
	 * This is fired 3 times - For each Child Presenter Add source
	 */
	@Override
	public void onDocumentSelection(DocumentSelectionEvent event) {
		if (this.isVisible()) {
			displayDocument(event.getDocRefId(), event.getTaskId());
		}
	}

	protected void displayDocument(final String docRefId, final Long taskId) {
		if (docRefId == null && taskId == null) {
			setInSlot(DOCUMENT_SLOT, null);
			
			//Close Button Clicked
			if(currentTaskType==TaskType.CASEVIEW) {
				placeManager.revealPlace(new PlaceRequest.Builder()
						.nameToken(NameTokens.registry).build());
			}
			
			return;
		}

		docViewFactory.get(new ServiceCallback<GenericDocumentPresenter>() {
			@Override
			public void processResult(GenericDocumentPresenter result) {
				result.setDocId(processRefId, docRefId, taskId, isLoadAsAdmin());
				result.setGlobalFormMode(mode);

				if (currentTaskType == TaskType.UNASSIGNED) {
					result.setUnAssignedList(true);
				}
				
				if (currentTaskType == TaskType.CASEVIEW) {
					result.getView().setCaseViewDocRefId(docRefId);
				}
				
				// Window.alert("Display Doc >> "+docRefId+" - "+taskId+" - "+result);
				setInSlot(DOCUMENT_SLOT, result);
			}
		});
	}

	@Override
	public void onReload(ReloadEvent event) {
		if (this.isVisible()) {
			resetPage();
			loadTasks();
		}
	}

	@Override
	public void onSearch(SearchEvent event) {
		if (this.isVisible()) {
			resetPage();
			GenericFilter filter = event.getFilter();
			searchTerm = filter.getPhrase().trim();
			if (searchTerm == null || searchTerm.isEmpty()) {
				loadTasks();
				return;
			}
			search(filter);
		}
	}

	@Override
	public void onAssignTask(AssignTaskEvent event) {
		MultiRequestAction action = new MultiRequestAction();
		action.addRequest(new AssignTaskRequest(event.getTaskId(), event.getUserId()));
		GetTaskList listRequest = new GetTaskList(processRefId, AppContext.getUserId(), TaskType.UNASSIGNED);
		listRequest.setTaskId(taskId);
		listRequest.setDocRefId(docRefId);
		action.addRequest(listRequest);

		fireEvent(new ProcessingEvent("Processing..."));
		dispatcher.execute(action, new TaskServiceCallback<MultiRequestActionResult>() {
			@Override
			public void processResult(MultiRequestActionResult aResponse) {
				BaseResponse a = aResponse.get(0);
				GetTaskListResult listResult = (GetTaskListResult) aResponse.get(1);
				loadLines(listResult.getTasks(), listResult.getTotalCount());
				fireEvent(new ProcessingCompletedEvent());
				fireEvent(new ReloadDocumentEvent(docRefId));
				fireEvent(new ShowMessageEvent(AlertType.SUCCESS, "Assignment Successful!"));
			}
		});

	}

	/**
	 * <p>
	 * Tells the server to load a Task as Administrator[Business Admin] for Admin
	 * Overview & Management
	 * <p>
	 * Override it in inheriting presenters
	 * 
	 * @return
	 */
	boolean isLoadAsAdmin() {
		return false;
	}
	
	@Override
	public void onLoadData(LoadDataEvent event) {
		int page = event.getPage().intValue();
		CURPOS = (page-1)*PAGE_SIZE;
		currentPage = page;
		loadTasks();
	}
}
