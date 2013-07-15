package com.duggan.workflow.client.ui.home;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gwtplatform.common.client.IndirectProvider;
import com.gwtplatform.common.client.StandardProvider;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.duggan.workflow.client.model.TaskType;
import com.duggan.workflow.client.place.NameTokens;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.duggan.workflow.client.service.ServiceCallback;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.MainPagePresenter;
import com.duggan.workflow.client.ui.events.AfterSaveEvent;
import com.duggan.workflow.client.ui.events.DocumentSelectionEvent;
import com.duggan.workflow.client.ui.events.PresentTaskEvent;
import com.duggan.workflow.client.ui.events.ReloadEvent;
import com.duggan.workflow.client.ui.events.AfterSaveEvent.AfterSaveHandler;
import com.duggan.workflow.client.ui.events.DocumentSelectionEvent.DocumentSelectionHandler;
import com.duggan.workflow.client.ui.events.ReloadEvent.ReloadHandler;
import com.duggan.workflow.client.ui.login.LoginGateKeeper;
import com.duggan.workflow.client.ui.save.CreateDocPresenter;
import com.duggan.workflow.client.ui.tasklistitem.DateGroupPresenter;
import com.duggan.workflow.client.ui.tasklistitem.TaskItemPresenter;
import com.duggan.workflow.client.ui.util.DateUtils;
import com.duggan.workflow.client.ui.util.DocMode;
import com.duggan.workflow.client.ui.view.GenericDocumentPresenter;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.CurrentUser;
import com.duggan.workflow.shared.model.DocStatus;
import com.duggan.workflow.shared.model.DocSummary;
import com.duggan.workflow.shared.model.DocType;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.HTSummary;
import com.duggan.workflow.shared.requests.ApprovalRequest;
import com.duggan.workflow.shared.requests.GetTaskList;
import com.duggan.workflow.shared.requests.LogoutAction;
import com.duggan.workflow.shared.responses.ApprovalRequestResult;
import com.duggan.workflow.shared.responses.GetTaskListResult;
import com.duggan.workflow.shared.responses.LogoutActionResult;

public class HomePresenter extends
		Presenter<HomePresenter.MyView, HomePresenter.MyProxy> implements AfterSaveHandler,
		DocumentSelectionHandler, ReloadHandler{

	public interface MyView extends View {
		HasClickHandlers getSimulationBtn();
		HasClickHandlers getAddButton();
		HasClickHandlers getEditButton();
		void showEdit(boolean displayed);	
		void setHeading(String heading);
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.home)
	@UseGatekeeper(LoginGateKeeper.class)
	public interface MyProxy extends ProxyPlace<HomePresenter> {
	}

	public static final Object DATEGROUP_SLOT = new Object();
	
	@ContentSlot
	public static final Type<RevealContentHandler<?>> DOCUMENT_SLOT = new Type<RevealContentHandler<?>>();
	
	@Inject DispatchAsync dispatcher;
	
	@Inject PlaceManager placeManager;
	
	private IndirectProvider<CreateDocPresenter> createDocProvider;
	
	private IndirectProvider<GenericDocumentPresenter> docViewFactory;
	
	private IndirectProvider<DateGroupPresenter> dateGroupFactory;
	
	private TaskType currentTaskType;
	
	private Integer selectedValue;
	
	final CurrentUser user;
	
	enum MODE{
		EDIT,
		CREATE
	}
	
	@Inject
	public HomePresenter(final EventBus eventBus, final MyView view,
			final CurrentUser user,
			final MyProxy proxy,
			Provider<CreateDocPresenter> docProvider,
			Provider<GenericDocumentPresenter> docViewProvider,
			Provider<DateGroupPresenter> dateGroupProvider) {
		super(eventBus, view, proxy);
		
		createDocProvider = new StandardProvider<CreateDocPresenter>(docProvider);
		docViewFactory  = new StandardProvider<GenericDocumentPresenter>(docViewProvider);
		dateGroupFactory = new StandardProvider<DateGroupPresenter>(dateGroupProvider);
		this.user = user;
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPagePresenter.CONTENT_SLOT, this);
	}

	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(AfterSaveEvent.TYPE, this);
		addRegisteredHandler(DocumentSelectionEvent.TYPE, this);
		addRegisteredHandler(ReloadEvent.TYPE, this);
				
		getView().getEditButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(selectedValue!=null){
					showEditForm(MODE.EDIT);
				}
			}
		});
		
		//testing code
		getView().getSimulationBtn().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Document doc = new Document();
				doc.setCreated(new Date());
				doc.setDateDue(new Date());
				doc.setSubject("CNT/B&C/01/2013");
				doc.setDescription("Contract for the constrution of Hall6");
				doc.setPartner("B&C Contactors");
				doc.setValue("5.5Mil");
				doc.setType(DocType.CONTRACT);
				
				dispatcher.execute(new ApprovalRequest(user.getUserId(), doc), new TaskServiceCallback<ApprovalRequestResult>(){
					@Override
					public void processResult(ApprovalRequestResult result) {						
						PlaceRequest request = new PlaceRequest("home").
								with("type", TaskType.APPROVALREQUESTNEW.getDisplayName());
						
						placeManager.revealPlace(request);
						
					}
				});
			}
		});
		
		getView().getAddButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				showEditForm(MODE.CREATE);
			}
		});
	}

	/**
	 * 
	 */
	@Override
	public void prepareFromRequest(PlaceRequest request) {
		
		clear();
		
		String name = request.getParameter("type", TaskType.DRAFT.getDisplayName());
		
		TaskType type = TaskType.getTaskType(name);
		this.currentTaskType=type;
		
		loadTasks(type);			
		
	}	

	private void clear() {
		getView().showEdit(false);
		//clear document slot
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
		getView().setHeading(type.getTitle());
		
		String userId = user.getUserId();
		//System.err.println("##UserID = "+userId+" :: TaskType= "+currentTaskType);
		dispatcher.execute(new GetTaskList(userId,currentTaskType), new TaskServiceCallback<GetTaskListResult>(){

			@Override
			public void processResult(GetTaskListResult result) {
				
				Window.setTitle(type.getTitle());
				GetTaskListResult rst = (GetTaskListResult)result;
				List<DocSummary> tasks = rst.getTasks();
				loadLines(tasks);
				
				if(tasks.size()>0){
					DocSummary doc = tasks.get(0);
					
					Integer docId=null;
					DocMode docMode = DocMode.READ;
					
					if(doc instanceof Document){
						docId = (Integer)doc.getId();
						if(((Document)doc).getStatus()==DocStatus.DRAFTED){
							docMode = DocMode.READWRITE;
						}
					}else{
						docId = ((HTSummary)doc).getDocumentRef();
					}
					
					fireEvent(new DocumentSelectionEvent(docId,docMode));
				}
				
			}
			
		});

	}
	
	/**
	 * 
	 * @param tasks
	 */
	protected void loadLines(final List<DocSummary> tasks) {
		HomePresenter.this.setInSlot(DATEGROUP_SLOT, null);
		final List<String> dates=new ArrayList<String>();
		
		for(int i=0; i< tasks.size(); i++){
			
			final String dt = DateUtils.DATEFORMAT.format(tasks.get(i).getCreated());
			final DocSummary doc = tasks.get(i);
			
			if(dates.contains(dt)){
				fireEvent(new PresentTaskEvent(tasks.get(i)));
				System.out.println("### [2] Doc -- "+doc);
			}else{
				System.out.println("### [1] Doc -- "+doc);
				dateGroupFactory.get(new ServiceCallback<DateGroupPresenter>() {
					@Override
					public void processResult(DateGroupPresenter result) {
						result.setDate(dt);
						HomePresenter.this.addToSlot(DATEGROUP_SLOT, result);						
						fireEvent(new PresentTaskEvent(doc));
						
						dates.add(dt);
					}
				});
				
			}
		}
		
	}
	
	protected void showEditForm(final MODE mode) {
		createDocProvider.get(new ServiceCallback<CreateDocPresenter>() {
			@Override
			public void processResult(CreateDocPresenter result) {
				if(mode.equals(MODE.EDIT) && selectedValue!=null){
					result.setDocumentId(selectedValue);
				}
				
				addToPopupSlot(result, true);				
			}
		});
	}

	@Override
	protected void onReset() {
		super.onReset();
		
	}

	@Override
	public void onAfterSave(AfterSaveEvent event) {
		loadTasks(TaskType.DRAFT);
	}

	@Override
	public void onDocumentSelection(DocumentSelectionEvent event) {
		Integer id = event.getDocumentId();
		this.selectedValue=id;
		
		if(selectedValue!=null){
			getView().showEdit(true);
		}
		displayDocument();
	}
	
	private void displayDocument() {
		if(selectedValue==null){
			setInSlot(DOCUMENT_SLOT, null);
			return;
		}
		
		docViewFactory.get(new ServiceCallback<GenericDocumentPresenter>() {
			@Override
			public void processResult(GenericDocumentPresenter result) {
				result.setDocumentId(selectedValue);
				setInSlot(DOCUMENT_SLOT, result);
			}
		});
	}

	@Override
	protected void onReveal() {
		super.onReveal();
	}

	@Override
	public void onReload(ReloadEvent event) {
		loadTasks();
	}
}
