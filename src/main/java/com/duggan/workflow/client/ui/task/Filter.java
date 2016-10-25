package com.duggan.workflow.client.ui.task;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class Filter extends Composite {

	private static FilterUiBinder uiBinder = GWT.create(FilterUiBinder.class);

	interface FilterUiBinder extends UiBinder<Widget, Filter> {
	}

	public Filter() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
