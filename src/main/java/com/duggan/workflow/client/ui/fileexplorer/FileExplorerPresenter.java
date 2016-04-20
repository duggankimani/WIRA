package com.duggan.workflow.client.ui.fileexplorer;

import java.util.List;

import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.admin.TabDataExt;
import com.duggan.workflow.client.ui.home.HomePresenter;
import com.duggan.workflow.client.ui.home.doctree.DocTreePresenter;
import com.duggan.workflow.client.ui.security.AdminGateKeeper;
import com.duggan.workflow.shared.model.Attachment;
import com.duggan.workflow.shared.requests.GetAttachmentsRequest;
import com.duggan.workflow.shared.responses.GetAttachmentsResponse;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.TabData;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.annotations.TabInfo;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.presenter.slots.SingleSlot;
import com.gwtplatform.mvp.client.proxy.TabContentProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
public class FileExplorerPresenter extends Presenter<FileExplorerPresenter.MyView, FileExplorerPresenter.MyProxy>  {
    interface MyView extends View  {

		void bindAttachments(List<Attachment> attachments);
    }

    
    @NameToken(NameTokens.explorer)
    @ProxyStandard
    @UseGatekeeper(AdminGateKeeper.class)
    interface MyProxy extends TabContentProxyPlace<FileExplorerPresenter> {
    }
    
    @TabInfo(container = HomePresenter.class)
    static TabData getTabLabel(AdminGateKeeper adminGatekeeper) {
		TabDataExt data = new TabDataExt("File Manager","icon-dashboard",12, adminGatekeeper,false);
        return data;
    }
    
    public static final SingleSlot<DocTreePresenter> FILES_SLOT = new SingleSlot<DocTreePresenter>();
    
	@Inject DocTreePresenter documentTreePresenter;

	@Inject DispatchAsync requestHelper;
	
    @Inject
    FileExplorerPresenter(
            EventBus eventBus,
            MyView view, 
            MyProxy proxy) {
        super(eventBus, view, proxy, HomePresenter.SLOT_SetTabContent);
    }
    
    @Override
    protected void onBind() {
    	super.onBind();
    	setInSlot(FILES_SLOT, documentTreePresenter);
    }
    
    @Override
    public void prepareFromRequest(PlaceRequest request) {
        super.prepareFromRequest(request);
        documentTreePresenter.initDocuments();
        loadAttachments();
    }

	private void loadAttachments() {
		GetAttachmentsRequest request = new GetAttachmentsRequest();
		requestHelper.execute(request, new TaskServiceCallback<GetAttachmentsResponse>(){
			@Override
			public void processResult(GetAttachmentsResponse aResponse) {
				List<Attachment> attachments = aResponse.getAttachments();
				getView().bindAttachments(attachments);
			}
		});
	}    
    
}