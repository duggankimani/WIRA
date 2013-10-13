package com.duggan.workflow.client.ui.admin.formbuilder.component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

public class SelectBasic extends Field {

	private static SelectBasicUiBinder uiBinder = GWT
			.create(SelectBasicUiBinder.class);
	
	private final Widget widget;
	interface SelectBasicUiBinder extends UiBinder<Widget, SelectBasic> {
	}

	public SelectBasic() {
		super();
		widget= uiBinder.createAndBindUi(this);
		add(widget);
	}

	@Override
	public Field cloneWidget() {
		return new SelectBasic();
	}

}
