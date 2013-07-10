package com.duggan.workflow.client.ui.home;

import java.util.Date;
import java.util.List;

import org.jbpm.eventmessaging.EventKey;
import org.jbpm.eventmessaging.EventResponseHandler;
import org.jbpm.task.Attachment;
import org.jbpm.task.Comment;
import org.jbpm.task.Content;
import org.jbpm.task.OrganizationalEntity;
import org.jbpm.task.Status;
import org.jbpm.task.Task;
import org.jbpm.task.TaskService;
import org.jbpm.task.query.TaskSummary;
import org.jbpm.task.service.ContentData;
import org.jbpm.task.service.FaultData;

import com.gwtplatform.common.client.IndirectProvider;
import com.gwtplatform.common.client.StandardProvider;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.duggan.workflow.client.events.AfterSaveEvent;
import com.duggan.workflow.client.events.AfterSaveEvent.AfterSaveHandler;
import com.duggan.workflow.client.events.DocumentSelectionEvent;
import com.duggan.workflow.client.events.DocumentSelectionEvent.DocumentSelectionHandler;
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
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ClosingEvent;
import com.google.gwt.user.client.Window.ClosingHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.duggan.workflow.client.service.ServiceCallback;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.MainPagePresenter;
import com.duggan.workflow.client.ui.login.LoginGateKeeper;
import com.duggan.workflow.client.ui.save.CreateDocPresenter;
import com.duggan.workflow.client.ui.tasklistitem.TaskItemPresenter;
import com.duggan.workflow.client.ui.view.GenericDocumentPresenter;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.CurrentUser;
import com.duggan.workflow.shared.model.DocSummary;
import com.duggan.workflow.shared.model.DocType;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.requests.ApprovalRequest;
import com.duggan.workflow.shared.requests.GetTaskList;
import com.duggan.workflow.shared.requests.LogoutAction;
import com.duggan.workflow.shared.responses.ApprovalRequestResult;
import com.duggan.workflow.shared.responses.GetTaskListResult;
import com.duggan.workflow.shared.responses.LogoutActionResult;

public class HomePresenter extends
		Presenter<HomePresenter.MyView, HomePresenter.MyProxy> implements AfterSaveHandler,
		DocumentSelectionHandler{

	public interface MyView extends View {
		Button getSimulationBtn();
		Button getAddButton();
		Button getEditButton();
		void showEdit(boolean displayed);
		HasClickHandlers getLogout();
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.home)
	@UseGatekeeper(LoginGateKeeper.class)
	public interface MyProxy extends ProxyPlace<HomePresenter> {
	}

	public static final Object ITEM_SLOT = new Object();
	
	@ContentSlot
	public static final Type<RevealContentHandler<?>> DOCUMENT_SLOT = new Type<RevealContentHandler<?>>();
	
	@Inject DispatchAsync dispatcher;
	
	@Inject PlaceManager placeManager;
	
	private IndirectProvider<TaskItemPresenter> presenterProvider;
	
	private IndirectProvider<CreateDocPresenter> createDocProvider;
	
	private IndirectProvider<GenericDocumentPresenter> docViewFactory;
	
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
			final MyProxy proxy, Provider<TaskItemPresenter> provider,
			Provider<CreateDocPresenter> docProvider, Provider<GenericDocumentPresenter> docViewProvider) {
		super(eventBus, view, proxy);
		presenterProvider = new StandardProvider<TaskItemPresenter>(provider);
		createDocProvider = new StandardProvider<CreateDocPresenter>(docProvider);
		docViewFactory  = new StandardProvider<GenericDocumentPresenter>(docViewProvider);
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
		
		getView().getLogout().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				logout();
			}
		});
		
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
	
	protected void logout() {
		dispatcher.execute(new LogoutAction(), new TaskServiceCallback<LogoutActionResult>() {
			@Override
			public void processResult(LogoutActionResult result) {
				AppContext.destroy();
				placeManager.revealErrorPlace("login");
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

	/**
	 * Load JBPM records
	 * @param type
	 */
	private void loadTasks(final TaskType type) {
		
		String userId = user.getUserId();
		//System.err.println("##UserID = "+userId+" :: TaskType= "+currentTaskType);
		dispatcher.execute(new GetTaskList(userId,currentTaskType), new TaskServiceCallback<GetTaskListResult>(){

			@Override
			public void processResult(GetTaskListResult result) {
				
				Window.setTitle(type.getTitle());
				GetTaskListResult rst = (GetTaskListResult)result;
				List<DocSummary> tasks = rst.getTasks();
				loadLines(tasks);
			}
			
		});

	}
	
	/**
	 * 
	 * @param tasks
	 */
	protected void loadLines(final List<DocSummary> tasks) {
		HomePresenter.this.setInSlot(ITEM_SLOT, null);
		
		for(int i=0; i< tasks.size(); i++){
			
			final int row = i+1;
			presenterProvider.get(new ServiceCallback<TaskItemPresenter>() {
				
				@Override
				public void processResult(TaskItemPresenter result) {
					result.setRowNo(row);
					result.setDocSummary(tasks.get(row-1));
					HomePresenter.this.addToSlot(ITEM_SLOT, result);
				}
				
			});
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
}
