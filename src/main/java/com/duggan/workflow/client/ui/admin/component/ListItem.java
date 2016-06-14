package com.duggan.workflow.client.ui.admin.component;

import java.util.ArrayList;
import java.util.ArrayList;

import com.duggan.workflow.client.ui.component.BulletPanel;
import com.duggan.workflow.shared.model.Listable;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
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
	@UiField DivElement divUnselectName;
	
	@UiField Anchor aUnselect;
	
	@UiField Anchor aSelect;
	
	@UiField BulletPanel bullet;
	
	private final T value;
	
	public interface OnSelectHandler{
		public void onItemSelected(ListItem source, Listable value, boolean selected);
	}
	
	ArrayList<OnSelectHandler> handlers = new ArrayList<OnSelectHandler>();
	
	public ListItem(final T value, boolean selected) {
		initWidget(uiBinder.createAndBindUi(this));
		
		if(selected){
			//selected
			aUnselect.removeStyleName("hidden");
			aSelect.addStyleName("hidden");			
			bullet.setStyleName("select2-search-choice");
			
		}else{
			//others
			aUnselect.addStyleName("hidden");
			aSelect.removeStyleName("hidden");
			divName.addClassName("select2-result-label");
			bullet.setStyleName("select2-results-dept-0 select2-result select2-result-selectable");
			divUnselectName.setClassName("hide");
		}
		
		
		this.value = value; 
		
		divName.setInnerText(value.getName());
		divUnselectName.setInnerText(value.getName());
		
		aSelect.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				removeMe(true);
			}
		});
		
		aSelect.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				bullet.addStyleName("select2-highlighted");
			}
		});
		
		aSelect.addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				bullet.removeStyleName("select2-highlighted");	
			}
		});
		
		aUnselect.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				removeMe(false);
			}
		});
	}	
	
	protected void removeMe(boolean selected) {
		for(OnSelectHandler handler: handlers){
			handler.onItemSelected(ListItem.this, value, selected);
		}
		ListItem.this.removeFromParent();
	}

	public T getObject(){
		return value;
	}
	
	public void addSelectHandler(OnSelectHandler handler){
		handlers.add(handler);
	}
}
