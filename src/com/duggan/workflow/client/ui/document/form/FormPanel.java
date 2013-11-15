package com.duggan.workflow.client.ui.document.form;

import java.util.Map;

import com.duggan.workflow.client.ui.admin.formbuilder.HasProperties;
import com.duggan.workflow.client.ui.component.IssuesPanel;
import com.duggan.workflow.client.ui.delegate.FormDelegate;
import com.duggan.workflow.shared.model.StringValue;
import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.model.form.Form;
import com.duggan.workflow.shared.model.form.Property;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class FormPanel extends Composite {

	private static FormPanelUiBinder uiBinder = GWT
			.create(FormPanelUiBinder.class);

	interface FormPanelUiBinder extends UiBinder<Widget, FormPanel> {
	}

	@UiField HTMLPanel panelFields;
	@UiField DivElement divFormCaption;
	@UiField DivElement divFormHelp;
	@UiField IssuesPanel issues;
	
	FormDelegate formDelegate = new FormDelegate();
	
	public FormPanel(Form form) {
		initWidget(uiBinder.createAndBindUi(this));
		
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
		
		formDelegate.setFields(form.getFields(), panelFields);
		
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
