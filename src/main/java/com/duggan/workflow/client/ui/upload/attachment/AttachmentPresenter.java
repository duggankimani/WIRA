package com.duggan.workflow.client.ui.upload.attachment;

import com.duggan.workflow.client.model.UploadContext;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.Attachment;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Window;
import com.google.web.bindery.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.wira.commons.shared.models.REPORTVIEWIMPL;

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
				UploadContext context = new UploadContext("getreport");
				context.setContext("attachmentId", attachment.getId()+"");
				context.setContext("ACTION", "GETATTACHMENT");
				String url = context.toUrl();
				
				String moduleUrl = GWT.getModuleBaseURL().replace("/gwtht", "");
				if(moduleUrl.endsWith("/")){
					moduleUrl = moduleUrl.substring(0, moduleUrl.length()-1);
				}
				
				url = url.replace("/", "");
				moduleUrl =moduleUrl+"/"+url;
				fireEvent(new ShowAttachmentEvent(moduleUrl,attachment.getName()));
			}
		});
	}

	public void setAttachment(Attachment attachment){
		this.attachment = attachment;
		getView().setValues(attachment.getId(), attachment.getName(), attachment.getSizeAsStr());
	}
	
}
