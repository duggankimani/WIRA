package com.duggan.workflow.client.ui.admin.formbuilder.component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class SelectBasic extends Composite {

	private static TextAreaUiBinder uiBinder = GWT
			.create(TextAreaUiBinder.class);

	interface TextAreaUiBinder extends UiBinder<Widget, SelectBasic> {
	}

	public SelectBasic() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
