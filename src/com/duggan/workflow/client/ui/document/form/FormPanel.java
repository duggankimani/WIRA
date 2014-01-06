package com.duggan.workflow.client.ui.document.form;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.duggan.workflow.client.model.MODE;
import com.duggan.workflow.client.ui.admin.formbuilder.HasProperties;
import com.duggan.workflow.client.ui.admin.formbuilder.component.FieldWidget;
import com.duggan.workflow.client.ui.admin.formbuilder.component.TextArea;
import com.duggan.workflow.client.ui.component.IssuesPanel;
import com.duggan.workflow.client.ui.delegate.FormDelegate;
import com.duggan.workflow.shared.model.StringValue;
import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.model.form.Field;
import com.duggan.workflow.shared.model.form.Form;
import com.duggan.workflow.shared.model.form.FormModel;
import com.duggan.workflow.shared.model.form.Property;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Runtime form
 * 
 * @author duggan
 *
 */
public class FormPanel extends Composite {

	private static FormPanelUiBinder uiBinder = GWT
			.create(FormPanelUiBinder.class);

	interface FormPanelUiBinder extends UiBinder<Widget, FormPanel> {
	}

	@UiField HTMLPanel panelFields;
	//@UiField InlineLabel panelLabel;
	@UiField HTMLPanel panelItem;
	@UiField DivElement divFormCaption;
	@UiField DivElement divFormHelp;
	@UiField IssuesPanel issues;
	
	FormDelegate formDelegate = new FormDelegate();
	MODE mode = MODE.VIEW;
	
	public FormPanel(Form form){		
		this(form, MODE.VIEW);
	}
	
	public FormPanel(Form form,MODE mode) {
		initWidget(uiBinder.createAndBindUi(this));
		this.mode = mode;
		
		form.getCaption();
		divFormHelp.setInnerText("");
		if(form.getProperties()!=null)
		for(Property prop: form.getProperties()){
			if(prop.getName()!=null){
				if(prop.getName().equals(HasProperties.CAPTION)){
					Value val = prop.getValue();
					if(val!=null){
						divFormCaption.setInnerText(((StringValue)val).getValue());
					}
					
				}
				if(prop.getName().equals(HasProperties.HELP)){
					Value val = prop.getValue();
					if(val!=null){
						divFormHelp.setInnerText(((StringValue)val).getValue());
					}
					
				}
			}
		}
		
		List<Field> fields = form.getFields();
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
			if(mode==MODE.VIEW){
				//set read only 
				fieldWidget.setReadOnly(true);
			}
			
			if(fieldWidget instanceof TextArea){
				((TextArea) fieldWidget).getContainer().removeStyleName("hidden");
			}
			
			//System.err.println("||| "+field.getCaption()+" :: "+
			//(field.getValue()==null? "null" : field.getValue().getValue()));
			panelFields.add(fieldWidget);
		}
		
		//formDelegate.setFields(form.getFields(), panelFields);
	}

	public boolean isValid(){
		return formDelegate.isValid(issues, panelFields);
	}
	
	public Map<String, Value> getValues(){
		return formDelegate.getValues(panelFields);		
	}
	
	public void setReadOnly(boolean readOnly){
		formDelegate.setReadOnly(readOnly, (ComplexPanel)panelFields);
	}
}
