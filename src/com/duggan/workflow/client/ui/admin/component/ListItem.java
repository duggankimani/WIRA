package com.duggan.workflow.client.ui.admin.component;

import java.util.ArrayList;
import java.util.List;

import com.duggan.workflow.shared.model.Listable;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class ListItem<T extends Listable> extends Composite {

	private static ListItemUiBinder uiBinder = GWT
			.create(ListItemUiBinder.class);

	interface ListItemUiBinder extends UiBinder<Widget, ListItem> {
	}

	@UiField DivElement divName;
	
	@UiField Anchor aSelectedCancel;
	
	private final T value;
	
	public interface OnCloseHandler{
		public void onItemClosed(ListItem source, Listable value);
	}
	
	List<OnCloseHandler> handlers = new ArrayList<OnCloseHandler>();
	
	public ListItem(final T value) {
		initWidget(uiBinder.createAndBindUi(this));
		this.value = value; 
		
		divName.setInnerText(value.getName());
		
		
		aSelectedCancel.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				for(OnCloseHandler handler: handlers){
					handler.onItemClosed(ListItem.this, value);
				}
				ListItem.this.removeFromParent();
			}
		});
	}	
	
	public T getObject(){
		return value;
	}
	
	public void addOnCloseHandler(OnCloseHandler handler){
		handlers.add(handler);
	}

}
