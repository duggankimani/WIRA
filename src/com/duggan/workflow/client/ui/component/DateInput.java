package com.duggan.workflow.client.ui.component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;

public class DateInput extends Composite {

	private static DateInputUiBinder uiBinder = GWT
			.create(DateInputUiBinder.class);

	interface DateInputUiBinder extends UiBinder<Widget, DateInput> {
	}
	
	@UiField InlineLabel spnCalendar1;
	
	public DateInput() {
		initWidget(uiBinder.createAndBindUi(this));
		spnCalendar1.getElement().setInnerHTML("<i class='icon-calendar'/>");
	}

}
