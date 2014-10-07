package com.duggan.workflow.client.ui.admin.trigger.save;

import com.duggan.workflow.client.ui.component.IssuesPanel;
import com.duggan.workflow.client.ui.component.TextArea;
import com.duggan.workflow.client.ui.component.TextField;
import com.duggan.workflow.shared.model.Trigger;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class SaveTriggerView extends ViewImpl implements
		SaveTriggerPresenter.ISaveTriggerView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, SaveTriggerView> {
	}

	
	@UiField TextField txtName;
	@UiField TextField txtImports;
	@UiField TextArea txtScript;
	@UiField IssuesPanel issues;
	
	@Inject
	public SaveTriggerView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}
	
	@Override
	public Trigger getTrigger(){
		Trigger trigger = new Trigger();
		trigger.setName(txtName.getValue());
		trigger.setScript(txtScript.getValue());
		trigger.setImports(txtImports.getValue());
		return trigger;
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void clear() {
		txtName.setValue(null);
		txtScript.setValue(null);
		txtImports.setValue(null);
		issues.clear();
	}

	@Override
	public void setTrigger(Trigger doc) {
		txtName.setValue(doc.getName());
		txtScript.setValue(doc.getScript());	
		txtImports.setValue(doc.getImports());
	}

	@Override
	public boolean isValid() {
		issues.clear();
		boolean isValid = true;
		
		if(txtName.getValue()==null || txtName.getValue().isEmpty()){
			isValid=false;
			issues.addError("Name is mandatory");
		}
		
		return isValid;
	}
	
	boolean isNullOrEmpty(String value) {
		return value == null || value.trim().length() == 0;
	}
	
}
