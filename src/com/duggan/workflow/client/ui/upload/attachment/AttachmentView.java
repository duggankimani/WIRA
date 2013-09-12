package com.duggan.workflow.client.ui.upload.attachment;

import com.duggan.workflow.client.model.UploadContext;
import com.duggan.workflow.client.ui.upload.attachment.AttachmentPresenter.IAttachmentView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class AttachmentView extends ViewImpl implements IAttachmentView{

	private final Widget widget;
	
	public interface Binder extends UiBinder<Widget, AttachmentView>{
		
	}
	
	@UiField Anchor aDownload;
	@UiField SpanElement spnName;
	@UiField SpanElement spnSize;
	
	String url = "";
	@Inject
	public AttachmentView(Binder binder){
		widget = binder.createAndBindUi(this);	
		aDownload.removeStyleName("gwt-Anchor");
		aDownload.getElement().getStyle().setDisplay(Display.INLINE);
		aDownload.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String moduleUrl = GWT.getModuleBaseURL().replace("/gwtht", "");
				if(moduleUrl.endsWith("/")){
					moduleUrl = moduleUrl.substring(0, moduleUrl.length()-1);
				}
				url = url.replace("/", "");
				moduleUrl =moduleUrl+"/"+url;
				Window.open(moduleUrl, spnName.getInnerText(), "");
			}
		});
	}
	
	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setValues(long id,String name, String size) {
		spnName.setInnerText(name);
		spnSize.setInnerText(""+size);
		
		UploadContext context = new UploadContext("getreport");
		context.setContext("attachmentId", id+"");
		context.setContext("ACTION", "GETATTACHMENT");
		url = context.toUrl();
	}

}
