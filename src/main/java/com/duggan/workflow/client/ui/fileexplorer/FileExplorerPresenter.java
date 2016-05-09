package com.duggan.workflow.client.ui.fileexplorer;

import java.util.List;

import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.admin.TabDataExt;
import com.duggan.workflow.client.ui.events.FileSelectedEvent;
import com.duggan.workflow.client.ui.events.FileSelectedEvent.FileSelectedHandler;
import com.duggan.workflow.client.ui.events.ProcessingCompletedEvent;
import com.duggan.workflow.client.ui.events.ProcessingEvent;
import com.duggan.workflow.client.ui.events.SearchEvent;
import com.duggan.workflow.client.ui.events.SearchEvent.SearchHandler;
import com.duggan.workflow.client.ui.home.HomePresenter;
import com.duggan.workflow.client.ui.security.AdminGateKeeper;
import com.duggan.workflow.client.ui.security.HasAllPermissionsGateKeeper;
import com.duggan.workflow.client.ui.security.LoginGateKeeper;
import com.duggan.workflow.shared.model.Attachment;
import com.duggan.workflow.shared.model.TreeType;
import com.duggan.workflow.shared.requests.GetAttachmentsRequest;
import com.duggan.workflow.shared.requests.GetFileTreeRequest;
import com.duggan.workflow.shared.responses.GetAttachmentsResponse;
import com.duggan.workflow.shared.responses.GetFileTreeResponse;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.TabData;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.GatekeeperParams;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.TabInfo;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.TabContentProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.sencha.gxt.widget.core.client.tree.Tree;

public class FileExplorerPresenter extends
		Presenter<FileExplorerPresenter.MyView, FileExplorerPresenter.MyProxy>
		implements FileSelectedHandler, SearchHandler {
	public interface MyView extends View {

		void bindAttachments(List<Attachment> attachments);

		Tree<Attachment, String> getTree();

		void setFolders(TreeType type, List<Attachment> folders);

		HasClickHandlers getFilesLink();

		HasClickHandlers getProcessesLink();

		HasClickHandlers getUserLink();
		
		void onLoad();
	}

//	public static final String CAN_VIEW_PROCESSES = "PROCESSES_CAN_VIEW_PROCESSES";
	
	@ProxyCodeSplit
	@NameToken({NameTokens.explorer})
//	@UseGatekeeper(HasAllPermissionsGateKeeper.class)
//	@GatekeeperParams({"PROCESSES_CAN_VIEW_PROCESSES"})
	@UseGatekeeper(AdminGateKeeper.class)
	public interface MyProxy extends TabContentProxyPlace<FileExplorerPresenter> {
	}
	
	public static final String TABLABEL = "File Explorer";

	@TabInfo(container = HomePresenter.class)
	static TabData getTabLabel(AdminGateKeeper adminGatekeeper) {
		TabDataExt data = new TabDataExt(TABLABEL, "icon-dashboard", 12,
				adminGatekeeper, false);
		return data;
	}

	@Inject
	DispatchAsync requestHelper;
	private TreeType type;
	private String refId;

	@Inject
	public FileExplorerPresenter(EventBus eventBus, MyView view, MyProxy proxy) {
		super(eventBus, view, proxy, HomePresenter.SLOT_SetTabContent);
	}

	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(FileSelectedEvent.getType(), this);
		addRegisteredHandler(SearchEvent.getType(), this);
		getView().getFilesLink().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				loadTree(TreeType.FILES);
				loadAttachments(TreeType.FILES, null);
			}
		});

		getView().getProcessesLink().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				loadTree(TreeType.PROCESSES);
				loadAttachments(TreeType.PROCESSES, null);
			}
		});

		getView().getUserLink().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				loadTree(TreeType.USERS);
				loadAttachments(TreeType.USERS, null);
			}
		});
	}

	protected void loadTree(final TreeType type) {
		requestHelper.execute(new GetFileTreeRequest().with(type),
				new TaskServiceCallback<GetFileTreeResponse>() {
					@Override
					public void processResult(GetFileTreeResponse aResponse) {
						getView().setFolders(type, aResponse.getAttachments());
					}
				});
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		loadTree(TreeType.FILES);
		loadAttachments(TreeType.FILES, null);
	}
	
	@Override
	protected void onReveal() {
		super.onReveal();
		getView().onLoad();
	}

	private void loadAttachments(TreeType type, String refId) {
		loadAttachments(type, refId, null);
	}
	
	private void loadAttachments(TreeType type, String refId, String searchTerm) {
		this.type = type;
		this.refId = refId;
		fireEvent(new ProcessingEvent());
		GetAttachmentsRequest request = new GetAttachmentsRequest(type, refId);
		request.setSearchTerm(searchTerm);
		requestHelper.execute(request,
				new TaskServiceCallback<GetAttachmentsResponse>() {
					@Override
					public void processResult(GetAttachmentsResponse aResponse) {
						fireEvent(new ProcessingCompletedEvent());
						List<Attachment> attachments = aResponse
								.getAttachments();
						getView().bindAttachments(attachments);
					}
				});
	}

	@Override
	public void onFileSelected(FileSelectedEvent event) {
		if(event.getRefId()==null){
			return;
		}
//		Window.alert(">> "+event.getTreeType()+" = "+event.getRefId());
		loadAttachments(event.getTreeType(), event.getRefId());
	}
	
	@Override
	public void onSearch(SearchEvent event) {
		if(isVisible()){
			loadAttachments(type,refId,event.getFilter().getPhrase());
		}
	}
}