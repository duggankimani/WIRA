package com.duggan.workflow.client.ui.upload.attachment;

import com.duggan.workflow.shared.model.Attachment;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class AttachmentPresenter extends PresenterWidget<AttachmentPresenter.IAttachmentView> {

	public interface IAttachmentView extends View{

		void setValues(long id, String name, Long size);
		
	}
	
	@Inject
	public AttachmentPresenter(final EventBus eventBus, final IAttachmentView view){
		super(eventBus, view);
	}
	
	@Override
	protected void onBind() {
		super.onBind();
	}

	public void setAttachment(Attachment attachment){
		getView().setValues(attachment.getId(), attachment.getName(), attachment.getSize());
	}
	
}
