package com.duggan.workflow.client.ui.addDoc;

import static com.duggan.workflow.client.ui.addDoc.DocumentPopupPresenter.DOCITEM_SLOT;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class DocumentPopupView extends ViewImpl implements
		DocumentPopupPresenter.MyView {

	private final Widget widget;
	@UiField HTMLPanel panelDocTypes;
	


	public interface Binder extends UiBinder<Widget, DocumentPopupView> {
	}

	@Inject
	public DocumentPopupView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	@Override
	public void setInSlot(Object slot, IsWidget content) {
		if(slot == DOCITEM_SLOT){
			panelDocTypes.clear();
			if(content!=null){
				panelDocTypes.add(content);
			}
		}else{
			super.setInSlot(slot, content);
		}
		
	}
	
	@Override
	public void addToSlot(Object slot, IsWidget content) {
		if(slot == DOCITEM_SLOT){			
			if(content!=null){
				panelDocTypes.add(content);
			}
		}else{
			super.addToSlot(slot, content);
		}
		
	}

	@Override
	public void addSeparator(String category) {
		if(category==null){
			category= new String("");
		}
		
		HTML html = new HTML("<div>"+
		"<span style=\"padding-left:8px; font-size:12px;font-family:Arial Unicode MS,Arial,sans-serif;\">"+category+"</span>"+
		"<li style=\"margin:2px 0px;\" role=\"separator\" class=\"divider\"></li>"+
		"</div>");
		panelDocTypes.add(html);
	}

	@Override
	public void addContent(String content) {
		HTML html = new HTML("<div style=\"margin-left:8px;\">"+
				"<span class=\"label label-info\" "
				+ "style=\"padding-left:8px; font-size:12px;font-family:Arial Unicode MS,Arial,sans-serif;\">"+
				content+"</span>"+
				"</div>");
				panelDocTypes.add(html);
	}

}
