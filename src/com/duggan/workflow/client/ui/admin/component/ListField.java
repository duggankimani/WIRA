package com.duggan.workflow.client.ui.admin.component;

import java.util.ArrayList;
import java.util.List;

import com.duggan.workflow.client.ui.admin.component.ListItem.OnSelectHandler;
import com.duggan.workflow.client.ui.component.BulletListPanel;
import com.duggan.workflow.shared.model.Listable;
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

public class ListField<T extends Listable> extends Composite {

	private static ListFieldUiBinder uiBinder = GWT
			.create(ListFieldUiBinder.class);

	interface ListFieldUiBinder extends UiBinder<Widget, ListField> {
	}
	
	@UiField BulletListPanel ulSelected;
	@UiField BulletListPanel ulOthers;
	@UiField DivElement sltDrop;
	@UiField DivElement sltBox;
	@UiField FocusPanel sltContainer;
	
	List<T> selected = new ArrayList<T>();
	List<T> others = new ArrayList<T>();

	public ListField() {
		initWidget(uiBinder.createAndBindUi(this));
		
		sltContainer.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				sltDrop.removeClassName("hidden");
				sltBox.addClassName("select2-dropdown-open");
			}
		});
		
		sltContainer.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				//sltDrop.addClassName("hidden");
			}
		});
	}

	public ListField(List<T> items) {
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
			
			if(selected.contains(item)){
				selected.remove(item);
			}
			
			if(!others.contains(item)){
				others.add(item);	
				createListItem(item, isSelected);
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

	public void addItems(List<T> items){
		for(T item: items){
			addItem(item, false);
		}
	}
	
	public void select(List<T> items){
		for(T item: items){
			addItem(item, true);
		}
	}
	
	public List<T> getSelectedItems(){
		return selected;
	}
	
	
	
}
