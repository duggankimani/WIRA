package com.duggan.workflow.server.dao.hibernate;

import com.duggan.workflow.shared.model.form.Form;

public class JsonForm extends JsonType {

	@Override
	public Class<Form> returnedClass() {
		return Form.class;
	}
}
