package com.duggan.workflow.client.ui.admin.processes.category;

import java.util.List;

import com.duggan.workflow.client.ui.component.DropDownList;
import com.duggan.workflow.client.ui.component.IssuesPanel;
import com.duggan.workflow.client.ui.component.TextField;
import com.duggan.workflow.shared.model.ProcessCategory;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import static com.duggan.workflow.client.ui.util.StringUtils.*;

public class CreateCategoryPanel extends Composite {

	private static CreateCategoryPanelUiBinder uiBinder = GWT
			.create(CreateCategoryPanelUiBinder.class);

	interface CreateCategoryPanelUiBinder extends
			UiBinder<Widget, CreateCategoryPanel> {
	}

	@UiField DropDownList<ProcessCategory> lstCategories;
	@UiField TextField txtName;
	@UiField SpanElement lblAction;
	@UiField IssuesPanel issues;
	ProcessCategory category;
	
	public CreateCategoryPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		
		lstCategories.addValueChangeHandler(new ValueChangeHandler<ProcessCategory>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<ProcessCategory> event) {
				bindValue(event.getValue());
			}
		});
	}
	
	protected void bindValue(ProcessCategory category) {
		this.category = category;
		if(category!=null){
			txtName.setValue(category.getName());
			lstCategories.setValue(category);
			lblAction.setInnerText("Update Selected Category");
		}else{
			lblAction.setInnerText("Create New Category");
		}
	}
	

	public CreateCategoryPanel(List<ProcessCategory> categories) {
		this();
		lstCategories.setItems(categories);
	}

	public ProcessCategory getCategory() {
		ProcessCategory toSave = new ProcessCategory();
		if(category!=null){
			toSave = category;
		}
		toSave.setName(txtName.getValue());
		
		return toSave;
	}

	public boolean isValid() {
		
		boolean isValid = true;
		if(isNullOrEmpty(txtName.getValue())){
			isValid = false;
			issues.addError("Name is required");
		}
		
		return isValid;
	}

}
