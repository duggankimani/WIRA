package com.duggan.workflow.client.ui.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.duggan.workflow.client.model.TaskType;
import com.duggan.workflow.client.service.ServiceCallback;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.document.GenericDocumentPresenter;
import com.duggan.workflow.client.ui.events.AfterSaveEvent;
import com.duggan.workflow.client.ui.events.AfterSaveEvent.AfterSaveHandler;
import com.duggan.workflow.client.ui.events.AfterSearchEvent;
import com.duggan.workflow.client.ui.events.AlertLoadEvent;
import com.duggan.workflow.client.ui.events.AlertLoadEvent.AlertLoadHandler;
import com.duggan.workflow.client.ui.events.DocumentSelectionEvent;
import com.duggan.workflow.client.ui.events.DocumentSelectionEvent.DocumentSelectionHandler;
import com.duggan.workflow.client.ui.events.PresentTaskEvent;
import com.duggan.workflow.client.ui.events.ProcessingCompletedEvent;
import com.duggan.workflow.client.ui.events.ProcessingEvent;
import com.duggan.workflow.client.ui.events.ReloadEvent;
import com.duggan.workflow.client.ui.events.ReloadEvent.ReloadHandler;
import com.duggan.workflow.client.ui.events.SearchEvent;
import com.duggan.workflow.client.ui.events.SearchEvent.SearchHandler;
import com.duggan.workflow.client.ui.filter.FilterPresenter;
import com.duggan.workflow.client.ui.home.HomePresenter;
import com.duggan.workflow.client.ui.tasklistitem.DateGroupPresenter;
import com.duggan.workflow.client.ui.util.DateUtils;
import com.duggan.workflow.client.ui.util.DocMode;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.model.DocStatus;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.HTSummary;
import com.duggan.workflow.shared.model.SearchFilter;
import com.duggan.workflow.shared.requests.GetTaskList;
import com.duggan.workflow.shared.responses.GetTaskListResult;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.TextBox;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.common.client.IndirectProvider;
import com.gwtplatform.common.client.StandardProvider;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

public abstract class AbstractTaskPresenter<V extends AbstractTaskPresenter.ITaskView, Proxy_ extends Proxy<?>> 
		extends	Presenter<AbstractTaskPresenter.ITaskView, Proxy<?>> 
		implements AfterSaveHandler, DocumentSelectionHandler, ReloadHandler,SearchHandler
		//AlertLoadHandler,
		{

	public interface ITaskView extends View {
		void setHeading(String heading);
		HasClickHandlers getRefreshButton();
		public void setHasItems(boolean hasItems);
		void setTaskType(TaskType currentTaskType);
		public Anchor getaRefresh() ;
		TextBox getSearchBox();
		public void hideFilterDialog();
		public void setSearchBox(String text);
	}

	@ContentSlot
	public static final Type<RevealContentHandler<?>> DOCUMENT_SLOT = new Type<RevealContentHandler<?>>();
	@ContentSlot
	public static final Type<RevealContentHandler<?>> FILTER_SLOT = new Type<RevealContentHandler<?>>();
	@ContentSlot
	public static final Type<RevealContentHandler<?>> ACTIVITIES_SLOT = new Type<RevealContentHandler<?>>();
	
	@Inject DispatchAsync dispatcher;
	@Inject PlaceManager placeManager;
		
	public static final Object DATEGROUP_SLOT = new Object();
	private IndirectProvider<GenericDocumentPresenter> docViewFactory;
	private IndirectProvider<DateGroupPresenter> dateGroupFactory;
	
	protected static TaskType currentTaskType;
	
	/**
	 * on select documentId
	 */
	private Long selectedDocumentId;
	
	/**
	 * Url processInstanceId (pid) - required incase the use hits refresh
	 */
	private Long processInstanceId=null;
	
	/**
	 * Url documentId (did) - required incase the use hits refresh
	 */
	private Long documentId=null;
	
	String searchTerm="";
	
	@Inject FilterPresenter filterPresenter;
	Timer timer = new Timer() {
		
		@Override
		public void run() {
			search();
		}
	};
	
	public AbstractTaskPresenter(final EventBus eventBus, final ITaskView view,
			final Proxy_ proxy,
			Provider<GenericDocumentPresenter> docViewProvider,
			Provider<DateGroupPresenter> dateGroupProvider) {
		super(eventBus, view, proxy, HomePresenter.SLOT_SetTabContent);
		docViewFactory  = new StandardProvider<GenericDocumentPresenter>(docViewProvider);
		dateGroupFactory = new StandardProvider<DateGroupPresenter>(dateGroupProvider);
	}

	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(AfterSaveEvent.TYPE, this);
		addRegisteredHandler(DocumentSelectionEvent.TYPE, this);
		addRegisteredHandler(ReloadEvent.TYPE, this);
		//addRegisteredHandler(AlertLoadEvent.TYPE, this);
		//addRegisteredHandler(ActivitiesSelectedEvent.TYPE, this);
		
		addRegisteredHandler(SearchEvent.TYPE, this);
		
		
		getView().getSearchBox().addKeyUpHandler(new KeyUpHandler() {
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				String txt = getView().getSearchBox().getValue().trim();
				
				if(!txt.equals(searchTerm) || event.getNativeKeyCode()==KeyCodes.KEY_ENTER){
					searchTerm = txt;
					timer.cancel();
					timer.schedule(400);
				}
				
			}
		});
		
		getView().getRefreshButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {				
				loadTasks();
			}
		});
		
	}
	
	/**
	 * 
	 */
	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		
		//fireEvent(new LoadAlertsEvent());
		clear();		
		processInstanceId=null;
		documentId=null;
		
		String processInstID = request.getParameter("pid", null);
		String documentSearchID = request.getParameter("did", null);
		if(processInstID!=null){
			processInstanceId = Long.parseLong(processInstID);
		}
		if(documentSearchID!=null){
			documentId = Long.parseLong(documentSearchID);
		}
		loadTasks();
		
	}	

	
	protected void search() {
		timer.cancel();
		if(searchTerm.isEmpty()){
			loadTasks();
			return;
		}
		
		//fireEvent(new ProcessingEvent());
		SearchFilter filter = new SearchFilter();
		filter.setSubject(searchTerm);
		//filter.setPhrase(searchTerm);
		search(filter);
	}
	
	
	public void search(final SearchFilter filter){
		
		GetTaskList request = new GetTaskList(AppContext.getUserId(), filter);
		fireEvent(new ProcessingEvent());
		dispatcher.execute(request, new TaskServiceCallback<GetTaskListResult>(){
			@Override
			public void processResult(GetTaskListResult result) {		
				
				GetTaskListResult rst = (GetTaskListResult)result;
				List<Doc> tasks = rst.getTasks();
				loadLines(tasks);
				if(tasks.isEmpty())
					getView().setHasItems(false);
				else
					getView().setHasItems(true);
				
				fireEvent(new AfterSearchEvent(filter.getSubject(), filter.getPhrase()));
				fireEvent(new ProcessingCompletedEvent());
			}
		});		
	}
	
	private void clear() {		
		//clear document slot
		setInSlot(DATEGROUP_SLOT, null);
		setInSlot(DOCUMENT_SLOT, null);
	}
	
	private void loadTasks() {
		loadTasks(currentTaskType);
	}

	/**
	 * Load JBPM records
	 * @param type
	 */
	private void loadTasks(final TaskType type) {
		clear();
		
		getView().setHeading(type.getTitle());
		
		String userId = AppContext.getUserId();
		
		GetTaskList request = new GetTaskList(userId,currentTaskType);
		request.setProcessInstanceId(processInstanceId);
		request.setDocumentId(documentId);
		
		//System.err.println("###### Search:: did="+documentId+"; PID="+processInstanceId+"; TaskType="+type);
		
		fireEvent(new ProcessingEvent());
		dispatcher.execute(request, new TaskServiceCallback<GetTaskListResult>(){
			@Override
			public void processResult(GetTaskListResult result) {		
				
				GetTaskListResult rst = (GetTaskListResult)result;
				List<Doc> tasks = rst.getTasks();
				loadLines(tasks);
				
				if(tasks.size()>0){
					getView().setHasItems(true);
					
					Doc doc = tasks.get(0);
					Long docId=null;
					DocMode docMode = DocMode.READ;
					
					if(doc instanceof Document){
						docId = (Long)doc.getId();
						if(((Document)doc).getStatus()==DocStatus.DRAFTED){
							docMode = DocMode.READWRITE;
						}
						//Load document
						fireEvent(new DocumentSelectionEvent(docId,null,docMode));
					}else{
						docId = ((HTSummary)doc).getDocumentRef();
						long taskId = ((HTSummary)doc).getId(); 
						//Load Task
						fireEvent(new DocumentSelectionEvent(docId,taskId,docMode));
					}
					
				}else{
					getView().setHasItems(false);
				}
				
				fireEvent(new ProcessingCompletedEvent());
			}
			
		});
	}
	
	/**
	 * 
	 * @param tasks
	 */
	protected void loadLines(final List<Doc> tasks) {
		setInSlot(DATEGROUP_SLOT, null);
		final List<Date> dates=new ArrayList<Date>();
		
		for(int i=0; i< tasks.size(); i++){
			//final String dt = DateUtils.FULLDATEFORMAT.format(tasks.get(i).getCreated());
			final Doc doc = tasks.get(i);
			final String dt = DateUtils.DATEFORMAT.format(doc.getCreated());
			final Date date = DateUtils.DATEFORMAT.parse(dt);
			
			if(dates.contains(date)){
				fireEvent(new PresentTaskEvent(doc));
			}else{
				dateGroupFactory.get(new ServiceCallback<DateGroupPresenter>() {
					@Override
					public void processResult(DateGroupPresenter result) {
						result.setDate(doc.getCreated());
						addToSlot(DATEGROUP_SLOT, result);						
						fireEvent(new PresentTaskEvent(doc));						
						dates.add(date);
					}
				});
				
			}
		}
	
	}
	
	@Override
	protected void onReset() {
		super.onReset();
		setInSlot(FILTER_SLOT, filterPresenter);		
	}

	@Override
	public void onAfterSave(AfterSaveEvent event) {
		loadTasks();
	}

	/**
	 * This is fired 3 times - For each Child Presenter
	 * Add source
	 */
	@Override
	public void onDocumentSelection(DocumentSelectionEvent event) {
		this.selectedDocumentId=event.getDocumentId();		
		displayDocument(event.getDocumentId(), event.getTaskId());
		System.err.println("Called!! +"+this+" Document= "+event.getDocumentId()+
				" : Task="+event.getTaskId()+" :: "+event.getSource());
	}
	
	private void displayDocument(final Long documentId, final Long taskId) {
		if(documentId==null && taskId==null){
			setInSlot(DOCUMENT_SLOT, null);
			return;
		}
		
		docViewFactory.get(new ServiceCallback<GenericDocumentPresenter>() {
			@Override
			public void processResult(GenericDocumentPresenter result) {
				result.setDocId(documentId, taskId);
				setInSlot(DOCUMENT_SLOT, result);
			}
		});
	}
	
	@Override
	public void onReload(ReloadEvent event) {
		loadTasks();
	}

//	@Override
//	public void onAlertLoad(AlertLoadEvent event) {
//		//event.getAlerts();
//		Integer count = event.getAlerts().get(currentTaskType);
//		if(count==null) 
//			count=0;
//		
//		if(currentTaskType!=null)
//			Window.setTitle(currentTaskType.getTitle()+ (count==0? "" : " ("+count+")"));
//	}
	
	@Override
	public void onSearch(SearchEvent event) {
		SearchFilter filter= event.getFilter();
		search(filter);
	}

	@Override
	protected void onUnbind() {
		super.onUnbind();
		System.err.println("Unbind ########################################...... "+this);
	}

	@Override
	protected void onHide() {
		// TODO Auto-generated method stub
		super.onHide();
		System.err.println("Hide ########################################...... "+this);
	}

}
