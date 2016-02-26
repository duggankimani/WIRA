package com.duggan.workflow.client.ui.home.doctree;

import java.util.ArrayList;
import java.util.List;

import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.events.ContextLoadedEvent;
import com.duggan.workflow.shared.events.ContextLoadedEvent.ContextLoadedHandler;
import com.duggan.workflow.shared.model.Attachment;
import com.duggan.workflow.shared.requests.GetAttachmentsRequest;
import com.duggan.workflow.shared.responses.GetAttachmentsResponse;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.sencha.gxt.widget.core.client.event.ExpandItemEvent;
import com.sencha.gxt.widget.core.client.event.ExpandItemEvent.ExpandItemHandler;
import com.sencha.gxt.widget.core.client.tree.Tree;

public class DocTreePresenter extends PresenterWidget<DocTreePresenter.IDocTreeView> 
	implements ContextLoadedHandler{

	public interface IDocTreeView extends View {

		void display(List<Attachment> attachments);
		Tree<Attachment, String> getTree();
		void setFolders(ArrayList<Attachment> folders);
		
	}

	@Inject DispatchAsync requestHelper;
	
	@Inject
	public DocTreePresenter(final EventBus eventBus, final IDocTreeView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(ContextLoadedEvent.TYPE, this);
		getView().getTree().addExpandHandler(new ExpandItemHandler<Attachment>() {
			@Override
			public void onExpand(ExpandItemEvent<Attachment> event) {
				Attachment attachment = event.getItem();
				
//				if(attachment.getName().equals("Documents") && attachment.getId()==-1){
//					//root
//					System.err.println("Source >> "+event.getSource().getClass());
//					loadTree();
//				}
			}
		});
	}
	
	private void loadTree(){
		requestHelper.execute(new GetAttachmentsRequest().with(AppContext.getUserId()),
				new TaskServiceCallback<GetAttachmentsResponse>() {
			@Override
			public void processResult(GetAttachmentsResponse aResponse) {
				
				sortAndDisplay(aResponse.getAttachments());
			}
		});
	}

	protected void sortAndDisplay(List<Attachment> attachments) {
		getView().display(attachments);
	}

	@Override
	public void onContextLoaded(ContextLoadedEvent event) {
		loadTree();
	}

	public void initDocuments() {
		loadTree();
		//getView().setFolders(new ArrayList<Attachment>());
	}
	
	
}
