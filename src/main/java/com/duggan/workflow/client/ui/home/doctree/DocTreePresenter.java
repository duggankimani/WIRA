package com.duggan.workflow.client.ui.home.doctree;

import java.util.List;

import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.events.ContextLoadedEvent;
import com.duggan.workflow.client.ui.events.ContextLoadedEvent.ContextLoadedHandler;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.Attachment;
import com.duggan.workflow.shared.requests.GetAttachmentsRequest;
import com.duggan.workflow.shared.responses.GetAttachmentsResponse;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class DocTreePresenter extends PresenterWidget<DocTreePresenter.IDocTreeView> 
	implements ContextLoadedHandler{

	public interface IDocTreeView extends View {

		void display(List<Attachment> attachments);
		
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
	}
	
	@Override
	protected void onReset() {
		super.onReset();
	}
	
	public void loadTree(){
		requestHelper.execute(new GetAttachmentsRequest(AppContext.getUserId()),
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
	
	
}
