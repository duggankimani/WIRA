package com.duggan.workflow.client.ui.admin.formbuilder.component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class TableLayout extends Composite {

	private static TableLayoutUiBinder uiBinder = GWT
			.create(TableLayoutUiBinder.class);

	interface TableLayoutUiBinder extends UiBinder<Widget, TableLayout> {
	}

	public TableLayout() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
