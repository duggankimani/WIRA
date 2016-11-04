package com.duggan.workflow.client.ui.component;

import static com.duggan.workflow.client.ui.util.StringUtils.isNullOrEmpty;

import java.util.ArrayList;

import com.duggan.workflow.client.ui.admin.formbuilder.component.HTMLSelectBasic;
import com.duggan.workflow.shared.model.form.KeyValuePair;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.wira.commons.shared.models.Listable;

public class DropDownList<T extends Listable> extends ListBox implements
		HasValueChangeHandlers<T>, HasValue<T> {

	private static DropDownListUiBinder uiBinder = GWT
			.create(DropDownListUiBinder.class);

	interface DropDownListUiBinder extends UiBinder<Widget, DropDownList> {
	}

	// @UiField
	// ListBox listBox;

	private ArrayList<T> items = new ArrayList<T>();
	private String nullText = "--Select--";

	T value = null;

	public DropDownList() {
		super();
		initComponents();
	}

	private DropDownList(Element select) {
		super(select);
		initComponents();
	}
	
	@Override
	protected void onAttach() {
		super.onAttach();
	}
	
	void initComponents() {
		addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				int selectedIndex = getSelectedIndex();

				String code = getValue(selectedIndex);
				value = null;
				if (code.isEmpty()) {
					// setting null
					value = null;
				} else {
					value = getItem(selectedIndex - 1);
				}
				
				ValueChangeEvent.fire(DropDownList.this, value);
			}
		});
	}

	public void setItems(ArrayList<T> items) {
		clear();
		this.items = items;
		if(nullText==null){
			//NULL TEXT FOR SQL BASED, HTMLSelect Field
			nullText="--Select--";
		}
		addItem(nullText, "");

		if (items != null) {
			for (T item : items) {
				addItem(item.getDisplayName(), item.getName());
			}
		}
	}

	/**
	 * For single selection ArrayList box
	 */
	public T getValue() {
		return value;
	}

	/**
	 * For multi-selection ArrayList box
	 * 
	 * @return
	 */
	public ArrayList<T> getValues() {
		ArrayList<T> values = new ArrayList<T>();

		// Start from one to avoid selecting --Select--
		for (int i = 1; i < getItemCount(); i++) {
			if (isItemSelected(i)) {
				String key = getValue(i);
				T item = getItem(i - 1);
				if(item!=null){
					values.add(item);
				}
			}
		}
		return values;
	}

	/**
	 * The Selected index in the ListBox is always one item ahead of List<T> items list
	 * due to the inclusion of the null --Select-- text option. i.e. ListBox[i] = items.get(i-1);
	 * <p>
	 * 
	 * As such, all calls to {@link #getItem(int)} provide the parameter idx as (selectedItemIdx-1) 
	 * where selectedItemIdx is the index of the clicked item on the ListBox.
	 * 
	 * <p>
	 * {@link HTMLSelectBasic} field may however wrap a DOM element (select) that does not include 
	 * the null option. i.e no option --Select--. In this case ListBox[i] = items.get(i);
	 * 
	 * @param idx
	 * @return
	 */
	private T getItem(int idx) {
		
		if(nullText==null){
			//DUGGAN 1/11/2016
			idx = idx+1;
		}
		
		if(idx<items.size() && idx>=0){
			return items.get(idx);
		}
		
		return null;
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<T> handler) {

		return this.addHandler(handler, ValueChangeEvent.getType());
	}

	public void setValue(T value) {
		if (value == null) {
			setSelectedIndex(0);
			this.value = null;
			return;
		}

		for (int i = 0; i < getItemCount(); i++) {

			if (getValue(i).equals(value.getName())) {
				setSelectedIndex(i);
				this.value = getItem(i - 1);
			}

		}

	}

	public void setValueByKey(String key) {
		if (isNullOrEmpty(key)) {
			setValue(null);
			return;
		}

		for (int i = 0; i < getItemCount(); i++) {
			if (getValue(i).equals(key)) {
				setSelectedIndex(i);
				value = getItem(i - 1);
			}

		}
	}

	public void setReadOnly(boolean readOnly) {
		setEnabled(!readOnly);
	}

	public ArrayList<T> values() {
		return items;
	}

	public String getNullText() {
		return nullText;
	}

	public void setNullText(String nullText) {
		this.nullText = nullText;
	}

	@Override
	public void setValue(T value, boolean fireEvents) {
		setValue(value);
	}

	public Widget getComponent() {
		return this;
	}

	public void setMultiple(boolean multiple) {
		setMultipleSelect(true);
		setHeight("200px");
	}

	public void clear() {
		value = null;
		super.clear();
		if (items != null) {
			items.clear();
		}
	}

	public static DropDownList<KeyValuePair> wrap(Element select,
			boolean ishtmlFormChild) {

		// Assert that the element is attached.
		assert Document.get().getBody().isOrHasChild(select);
		
		DropDownList<KeyValuePair> listBox = new DropDownList<KeyValuePair>(
				select);
		
		NodeList<Node> nodes = select.getChildNodes();
		ArrayList<KeyValuePair> pairs = new ArrayList<KeyValuePair>();
		int length = nodes.getLength();
		listBox.setNullText(null);
		for (int i = 0; i < length; i++) {
			Element option = (Element) nodes.getItem(i);
			String text = option.getInnerHTML();
			
			String optionStr = option.getTagName(); 
			if(optionStr!=null && optionStr.toLowerCase().equals("option")){
				if(text.startsWith("--")){
					listBox.setNullText(text);
				}else{
					pairs.add(new KeyValuePair(text, text));
				}
			}
		}
		listBox.items = pairs;
		
		// Mark it attached and remember it for cleanup.
		listBox.onAttach();

		if (!ishtmlFormChild){
			RootPanel.detachOnWindowClose(listBox);
		}

		return listBox;
	}

	public ArrayList<T> getItems() {
		return items;
	}
}
