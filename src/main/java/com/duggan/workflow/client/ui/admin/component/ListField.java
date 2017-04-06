package com.duggan.workflow.client.ui.admin.component;

import java.util.ArrayList;

import com.duggan.workflow.client.ui.admin.component.ListItem.OnSelectHandler;
import com.duggan.workflow.client.ui.component.BulletListPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Widget;
import com.wira.commons.shared.models.Listable;

public class ListField<T extends Listable> extends Composite {

	private static ListFieldUiBinder uiBinder = GWT
			.create(ListFieldUiBinder.class);

	interface ListFieldUiBinder extends UiBinder<Widget, ListField> {
	}
	
	@UiField BulletListPanel ulSelected;
	@UiField BulletListPanel ulOthers;
	@UiField FocusPanel sltDrop;
	@UiField DivElement sltBox;
	@UiField FocusPanel sltContainer;
	
	ArrayList<T> selected = new ArrayList<T>();
	ArrayList<T> others = new ArrayList<T>();

	public ListField() {
		initWidget(uiBinder.createAndBindUi(this));
		
		sltContainer.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				sltDrop.removeStyleName("hide");
				sltBox.addClassName("select2-dropdown-open");
				sltDrop.setFocus(true);
			}
		});
		
		sltDrop.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				sltDrop.addStyleName("hide");
			}
		});
		
	}
	
	public ListField(ArrayList<T> items) {
		this();
		addItems(items);
	}

	public void addItem(T item, boolean isSelected){
		
		if(isSelected){
			
			if(! selected.contains(item)){
				selected.add(item);
				createListItem(item, isSelected);
			}
			
			if(others.contains(item)){
				others.remove(item);
				
			}
			
		}else{
			
			if(!others.contains(item)){
				others.add(item);	
				createListItem(item, isSelected);
			}
			
			if(selected.contains(item)){
				selected.remove(item);				
			}
			
		}
		
	}

	private void createListItem(T item, boolean selected) {
		ListItem<T> listItem = new ListItem<T>(item, selected);	
		listItem.addSelectHandler(new OnSelectHandler() {
			@Override
			public void onItemSelected(ListItem source, Listable value,
					boolean selected) {
				addItem((T)value, selected);
			}
		});
		
		if(selected){
			ulSelected.add(listItem);
		}else{
			ulOthers.add(listItem);
		}
	}

	public void addItems(ArrayList<T> items){		
		items.removeAll(selected);
		//others
		for(T item: items){
			addItem(item, false);
		}
	}
	
	public void select(ArrayList<T> items){		
		others.removeAll(selected);
		
		//others
		if(items!=null)
		for(T item: items){			
			addItem(item, true);
		}
	}
	
	public ArrayList<T> getSelectedItems(){
		return selected;
	}
	
	
	
}
