package com.duggan.workflow.client.ui.admin.trigger.save;

import java.util.ArrayList;

import com.duggan.workflow.client.ui.component.DropDownList;
import com.duggan.workflow.client.ui.component.IssuesPanel;
import com.duggan.workflow.client.ui.component.TextArea;
import com.duggan.workflow.client.ui.component.TextField;
import com.duggan.workflow.shared.model.Trigger;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class SaveTriggerView extends Composite {

	public interface Binder extends UiBinder<Widget, SaveTriggerView> {
	}

	private static Binder binder = GWT.create(Binder.class);

	@UiField
	TextField txtName;
	@UiField
	TextArea txtImports;
	@UiField
	TextArea txtScript;
	@UiField
	IssuesPanel issues;
	@UiField
	DropDownList<Trigger> lstTrigger;
	@UiField
	DivElement divTriggers;
	private Trigger trigger;

	public SaveTriggerView() {
		initWidget(binder.createAndBindUi(this));

		lstTrigger.addValueChangeHandler(new ValueChangeHandler<Trigger>() {

			@Override
			public void onValueChange(ValueChangeEvent<Trigger> event) {
				Trigger trigger = event.getValue();
				clear();
				if (trigger != null) {
					setTrigger(trigger);
				}
			}
		});
	}

	public SaveTriggerView(Trigger trigger) {
		this();
		setTrigger(trigger);
	}

	public Trigger getTrigger() {
		Trigger trigger = this.trigger;
		if (trigger == null) {
			trigger = new Trigger();
		}
		trigger.setName(txtName.getValue());
		trigger.setScript(txtScript.getValue());
		trigger.setImports(txtImports.getValue());

		return trigger;
	}

	public void clear() {
		txtName.setValue(null);
		txtScript.setValue(null);
		txtImports.setValue(null);
		issues.clear();
	}

	public void setTrigger(Trigger trigger) {
		this.trigger = trigger;
		if (trigger != null) {
			txtName.setValue(trigger.getName());
			txtScript.setValue(trigger.getScript());
			txtImports.setValue(trigger.getImports());
		}
	}

	public boolean isValid() {
		issues.clear();
		boolean isValid = true;

		if (txtName.getValue() == null || txtName.getValue().isEmpty()) {
			isValid = false;
			issues.addError("Name is mandatory");
		}

		return isValid;
	}

	boolean isNullOrEmpty(String value) {
		return value == null || value.trim().length() == 0;
	}

	public void setTriggers(ArrayList<Trigger> triggers) {
		showTriggers(true);
		lstTrigger.setItems(triggers);
	}

	public void showTriggers(boolean show) {
		if (show) {
			divTriggers.removeClassName("hide");
		} else {
			divTriggers.addClassName("hide");
		}
	}

}
