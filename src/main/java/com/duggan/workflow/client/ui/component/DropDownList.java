package com.duggan.workflow.client.ui.component;

import static com.duggan.workflow.client.ui.util.StringUtils.isNullOrEmpty;

import java.util.ArrayList;

import com.duggan.workflow.shared.model.form.KeyValuePair;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.DOM;
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

	private ArrayList<T> items;
	private String nullText = "--Select--";

	T value = null;

	public DropDownList() {
		super();
		initComponents();
	}

	public DropDownList(Element select) {
		super(select);
		initComponents();
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
					value = items.get(selectedIndex - 1);
				}

				ValueChangeEvent.fire(DropDownList.this, value);
			}
		});
	}

	public void setItems(ArrayList<T> items) {
		clear();
		this.items = items;
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
				values.add(items.get(i - 1));
			}
		}
		return values;
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
				this.value = items.get(i - 1);
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
				value = items.get(i - 1);
			}

		}
	}

	public void setReadOnly(boolean readOnly) {
		if (readOnly) {
			setEnabled(false);
		}
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
		int options = DOM.getChildCount(select);
		ArrayList<KeyValuePair> pairs = new ArrayList<KeyValuePair>();
		for (int i = 0; i < options; i++) {
			Element option = (Element) select.getChild(i);
			String text = option.getInnerText();
			pairs.add(new KeyValuePair(text, text));	
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
