package com.duggan.workflow.client.ui.component;

public class DateField extends DateInput{

	public DateField() {
		super();
		getWidget().getElement().getStyle().setProperty("width","initial");
		txtDate.getElement().getStyle().setProperty("width","initial");
	}
}
