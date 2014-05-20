package com.duggan.workflow.client.ui.tasklistitem;


import static com.duggan.workflow.client.ui.tasklistitem.DateGroupPresenter.ITEM_SLOT;

import java.util.Date;

import com.duggan.workflow.client.ui.component.BulletListPanel;
import com.duggan.workflow.client.ui.util.DateUtils;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class DateGroupView extends ViewImpl implements
		DateGroupPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, DateGroupView> {
	}

	@UiField SpanElement spnDate;
	@UiField BulletListPanel ulItemsContainer;
	
	@Inject
	public DateGroupView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	@Override
	public void setInSlot(Object slot, IsWidget content) {
		if(slot==ITEM_SLOT){
			ulItemsContainer.clear();
			
			if(content!=null){
				ulItemsContainer.add(content);
			}
		}else{
			super.setInSlot(slot, content);
		}
		
	}

	
	@Override
	public void addToSlot(Object slot, IsWidget content) {
		if(slot==ITEM_SLOT){
			
			if(content!=null){
				ulItemsContainer.add(content);
			}
		}else{
			super.addToSlot(slot, content);
		}		
	}
	
	public void setDate(Date date){
		String dt = DateUtils.FULLDATEFORMAT.format(date);
		spnDate.setInnerText(dt);
	}
	
}
