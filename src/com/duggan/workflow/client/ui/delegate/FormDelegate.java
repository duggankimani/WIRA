package com.duggan.workflow.client.ui.delegate;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.duggan.workflow.client.ui.admin.formbuilder.component.FieldWidget;
import com.duggan.workflow.client.ui.component.IssuesPanel;
import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.model.form.Field;
import com.duggan.workflow.shared.model.form.FormModel;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;

public class FormDelegate {

	public boolean isValid(IssuesPanel issues, ComplexPanel panelFields) {
		// txtDescription.getValue();
		boolean isValid = true;
		issues.clear();
		
		int fields = panelFields.getWidgetCount();
		
		for(int i=0; i<fields; i++){
			Widget widget = panelFields.getWidget(i);
			
			if(!(widget instanceof FieldWidget)){
				continue;
			}
			
			FieldWidget fieldWidget = (FieldWidget)widget;
			Field field = fieldWidget.getField();
			
			Object obj = fieldWidget.getValue(FieldWidget.MANDATORY);
			if(obj==null){
				continue;
			}
			
			Boolean mandatory = (Boolean)obj;
			if(mandatory){
				Value fieldValue = fieldWidget.getFieldValue();
				Object val = null;
				
				if(fieldValue!=null){
					val = fieldValue.getValue();
				}
				
				if(val==null){
					isValid=false;
					issues.addError("'"+field.getCaption()+"' is Mandatory");
				}
				
				if(val instanceof String){
					if(isNullOrEmpty(val.toString())){
						isValid=false;
						issues.addError("'"+field.getCaption()+"' is Mandatory");
					}
				}
				
			}
		}
		
		return isValid;
	}
	
	
	public Map<String, Value> getValues(ComplexPanel panelFields){
		Map<String,Value> values = new HashMap<String, Value>();
		
		int fields = panelFields.getWidgetCount();
		
		for(int i=0; i<fields; i++){
			Widget widget = panelFields.getWidget(i);
			if(!(widget instanceof FieldWidget)){
				continue;
			}
			
			FieldWidget fieldWidget = (FieldWidget)widget;
			Field field = fieldWidget.getField();
			if(fieldWidget.isReadOnly()){
				continue;
			}
			
			Value fieldValue = fieldWidget.getFieldValue();
			if(fieldValue!=null) {
				assert field.getName()!=null;
				assert !field.getName().isEmpty();
				fieldValue.setKey(field.getName());
			}			
			values.put(field.getName(), fieldValue);
		}
		
		return values;
		
	}
	
	public void setFields(List<Field> fields, ComplexPanel panelFields) {
		Collections.sort(fields, new Comparator<FormModel>() {
			public int compare(FormModel o1, FormModel o2) {
				Field field1 = (Field)o1;
				Field field2 = (Field)o2;
				
				Integer pos1 = field1.getPosition();
				Integer pos2 = field2.getPosition();
				
				return pos1.compareTo(pos2);
			};
			
		});
		for(Field field: fields){
			FieldWidget fieldWidget = FieldWidget.getWidget(field.getType(), field, false);
			panelFields.add(fieldWidget);
			
		}
	}

	boolean isNullOrEmpty(String value) {
		return value == null || value.trim().length() == 0;
	}


	public void setReadOnly(boolean isReadOnly, ComplexPanel panelFields) {
		int fields = panelFields.getWidgetCount();
		
		for(int i=0; i<fields; i++){
			Widget widget = panelFields.getWidget(i);
			if(!(widget instanceof FieldWidget)){
				continue;
			}
			
			FieldWidget fieldWidget = (FieldWidget)widget;
			fieldWidget.setReadOnly(isReadOnly);
		}	
	}

}
