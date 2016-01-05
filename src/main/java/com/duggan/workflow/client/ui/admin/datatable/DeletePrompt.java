package com.duggan.workflow.client.ui.admin.datatable;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;

public class DeletePrompt extends Composite {

	private static DeletePromptUiBinder uiBinder = GWT
			.create(DeletePromptUiBinder.class);

	interface DeletePromptUiBinder extends UiBinder<Widget, DeletePrompt> {
	}
	
	@UiField InlineLabel txtStatement;
	@UiField CheckBox chkAnswer;

	private DeletePrompt() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public DeletePrompt(String statement,String question,boolean defaultAnswer){
		this();
		txtStatement.setText(statement);
		chkAnswer.setValue(defaultAnswer);
		chkAnswer.setText(question);
	}

	public boolean getValue(){
		return chkAnswer.getValue();
	}
}
