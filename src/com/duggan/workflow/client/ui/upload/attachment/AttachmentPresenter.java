package com.duggan.workflow.client.ui.upload.attachment;

import com.duggan.workflow.shared.model.Attachment;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class AttachmentPresenter extends PresenterWidget<AttachmentPresenter.IAttachmentView> {

	public interface IAttachmentView extends View{
		void setValues(long id, String name, String size);
		HasClickHandlers getDownloadLink();
	}
	
	Attachment attachment;
	
	@Inject
	public AttachmentPresenter(final EventBus eventBus, final IAttachmentView view){
		super(eventBus, view);
	}
	
	@Override
	protected void onBind() {
		super.onBind();
		getView().getDownloadLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				//fireEvent(new ShowIframeEvent("www.google.com"));
			}
		});
	}

	public void setAttachment(Attachment attachment){
		this.attachment = attachment;
		getView().setValues(attachment.getId(), attachment.getName(), attachment.getSizeAsStr());
	}
	
}
