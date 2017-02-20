package com.duggan.workflow.client.ui.grid;

import java.util.ArrayList;
import java.util.Date;
import java.util.ArrayList;

import com.duggan.workflow.client.ui.component.DateInput;
import com.duggan.workflow.client.ui.component.DoubleField;
import com.duggan.workflow.client.ui.component.DropDownList;
import com.duggan.workflow.client.ui.component.IntegerField;
import com.duggan.workflow.client.ui.component.TextArea;
import com.duggan.workflow.client.ui.component.TextField;
import com.duggan.workflow.shared.model.BooleanValue;
import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.DateValue;
import com.duggan.workflow.shared.model.DoubleValue;
import com.duggan.workflow.shared.model.LongValue;
import com.duggan.workflow.shared.model.StringValue;
import com.duggan.workflow.shared.model.Value;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.wira.commons.shared.models.Listable;

public class ColumnConfig {

	private String key;
	private String displayName;
	private String placeHolder;
	private String styleName;
	private boolean isAggregationColumn;
	private boolean isMandatory;
	private boolean isEditable = true;
	private DataType type;
	private ArrayList<Listable> dropDownItems = new ArrayList<Listable>();

	public ColumnConfig(String key, String displayName, DataType type) {
		this.key = key;
		this.displayName = displayName;
		this.type = type;
	}

	public ColumnConfig(String key, String displayName, DataType type,
			String placeHolder) {
		this(key, displayName, type);
		this.placeHolder = placeHolder;
	}

	public ColumnConfig(String key, String displayName, DataType type,
			String placeHolder, String styleName) {
		this(key, displayName, type);
		this.placeHolder = placeHolder;
		this.styleName = styleName;
	}

	public ColumnConfig(String key, String displayName, DataType type,
			String placeHolder, String styleName, boolean isEditable) {
		this(key, displayName, type, placeHolder, styleName);
		this.isEditable = isEditable;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Widget createWidget(Object value) {

		HasValue widget = null;
		if (type == DataType.INTEGER) {
			IntegerField field = new IntegerField();
			field.setPlaceholder(placeHolder == null ? "" : placeHolder);
			field.setEnabled(isEditable);
			if (value != null) {
				value = ((Number) value).intValue();
			}
			widget = field;
		} else if (type == DataType.DOUBLE) {
			DoubleField field = new DoubleField();
			field.setPlaceholder(placeHolder == null ? "" : placeHolder);
			field.setEnabled(isEditable);
			widget = field;
		} else if (type == DataType.SELECTBASIC) {
			DropDownList dropDown = new DropDownList();
			dropDown.setItems(dropDownItems);
			widget = dropDown;
		} else if (type == DataType.STRINGLONG) {
			TextArea field = new TextArea();
			field.setPlaceholder(placeHolder == null ? "" : placeHolder);
			widget = field;
		} else if (type == DataType.BOOLEAN) {
			CheckBox field = new CheckBox();
			widget = field;
		} else if(type == DataType.DATE) {
			DateInput field = new DateInput();
			widget = field;
		}else {
			TextField field = new TextField();
			field.setPlaceholder(placeHolder == null ? "" : placeHolder);
			widget = field;
			if (value != null)
				value = value.toString();

		}
		if (styleName != null) {
			((Widget) widget).addStyleName(styleName);
		}

		widget.setValue(value);
		return (Widget) widget;
	}

	public static Value getValue(Long id, String key, Object obj, DataType type) {
		if (obj == null) {
			return null;
		}

		Value value = null;
		switch (type) {
		case BOOLEAN:
			value = new BooleanValue(id, key, (Boolean) obj);
			break;

		case DATE:
			value = new DateValue(id, key, (Date) obj);
			break;

		case DOUBLE:
			value = new DoubleValue(id, key, (obj == null || obj.toString()
					.isEmpty()) ? null : Double.parseDouble(obj.toString()));
			break;

		case INTEGER:
			value = new LongValue(id, key, (obj == null || obj.toString()
					.isEmpty()) ? null : Long.parseLong(obj.toString()));
			break;

		case STRING:
			value = new StringValue(id, key, obj.toString());
			break;

		case CHECKBOX:
			value = new BooleanValue(id, key, (Boolean) obj);
			break;

		case MULTIBUTTON:
			value = new StringValue(id, key, obj.toString());
			break;

		case SELECTBASIC:
			value = new StringValue(id, key, obj.toString());
			break;

		case SELECTMULTIPLE:
			value = new StringValue(id, key, obj.toString());
			break;

		case STRINGLONG:
			value = new StringValue(id, key, obj.toString());
			break;
		}

		return value;
	}

	public <T extends Listable> void setDropDownItems(ArrayList<T> items) {
		this.dropDownItems.clear();
		this.dropDownItems.addAll(items);
	}

	public Widget createHeaderWidget() {
		return new InlineLabel(displayName);
	}

	public boolean isAggregationColumn() {
		return isAggregationColumn;
	}

	public void setAggregationColumn(boolean isAggregationColumn) {
		this.isAggregationColumn = isAggregationColumn;
	}

	public DataType getType() {
		return type;
	}

	public void setType(DataType type) {
		this.type = type;
	}

	public String getPlaceHolder() {
		return placeHolder;
	}

	public void setPlaceHolder(String placeHolder) {
		this.placeHolder = placeHolder;
	}

	public boolean isMandatory() {
		return isMandatory;
	}

	public void setMandatory(boolean isMandatory) {
		this.isMandatory = isMandatory;
	}

	public String getStyleName() {
		return styleName;
	}

	public void setStyleName(String styleName) {
		this.styleName = styleName;
	}

	public boolean isEditable() {
		return isEditable;
	}

	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}

}
