package com.duggan.workflow.client.ui.component;

import java.util.ArrayList;
import java.util.List;

import com.duggan.workflow.client.ui.AppManager;
import com.duggan.workflow.client.ui.OnOptionSelected;
import com.duggan.workflow.client.ui.events.DeleteAttachmentEvent;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.Attachment;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;

public class AttachmentsPanel extends Composite {

	private static AttachmentsPanelUiBinder uiBinder = GWT
			.create(AttachmentsPanelUiBinder.class);

	interface AttachmentsPanelUiBinder extends
			UiBinder<Widget, AttachmentsPanel> {
	}

	@UiField BulletListPanel lstAttachments;
	@UiField SpanElement spnTotal;
	@UiField Anchor aRemoveAll;
	@UiField DivElement attachmentsHeader;
	
	List<Attachment> attachments = new ArrayList<Attachment>();
	private boolean isReadOnly;
	public AttachmentsPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		aRemoveAll.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				if(attachments!=null && !attachments.isEmpty()){
					NumberListPanel ul = new NumberListPanel();
					ul.add(new InlineLabel("Are you sure you want to remove the following files?"));
					for(Attachment attachment: attachments){
						BulletPanel li = new BulletPanel();
						li.getElement().getStyle().setMarginLeft(20, Unit.PX);
						li.setText(attachment.getName());
						ul.add(li);
					}
					AppManager.showPopUp("Remove All",ul,
							new OnOptionSelected() {
								
								@Override
								public void onSelect(String name) {
									if(name.equals("Yes")){
										Long[] attachmentIds = new Long[attachments.size()];
										for(int i=0; i<attachments.size(); i++){
											attachmentIds[i] = attachments.get(i).getId();
										}
										
										AppContext.fireEvent(new DeleteAttachmentEvent(attachmentIds));
										clear();
									}
								}
							}, "Yes", "Cancel");
				}
			}
		});
	}
	
	public AttachmentsPanel(List<Attachment> attachments) {
		this();
		this.attachments = attachments;
		for(Attachment attachment: attachments){
			addAttachment(attachment);
		}
	}

	public void addAttachment(Attachment attachment) {
		attachments.add(attachment);
		spnTotal.setInnerText(attachments.size()+"");
		AttachmentItem item = new AttachmentItem(attachment);
		item.setReadOnly(isReadOnly);
		lstAttachments.add(item);
		
		if(attachments.size()>1){
			attachmentsHeader.removeClassName("hidden");
		}
	}

	public void clear() {
		attachmentsHeader.addClassName("hidden");
		spnTotal.setInnerText("0");
		attachments.clear();
		lstAttachments.clear();
	}
	
	public void setReadOnly(boolean isReadOnly){
		this.isReadOnly = isReadOnly;
		int size = lstAttachments.getWidgetCount();
		
		if(isReadOnly){
			aRemoveAll.addStyleName("hidden");
		}else{
			aRemoveAll.removeStyleName("hidden");
		}
		for(int i=0; i<size; i++){
			AttachmentItem item = ((AttachmentItem)lstAttachments.getWidget(i));
			item.setReadOnly(isReadOnly);
		}
	}
}
