package com.duggan.workflow.server.dao.hibernate;

import com.duggan.workflow.shared.model.form.Field;

public class JsonField extends JsonType{

	@Override
	public Class<Field> returnedClass() {
		return Field.class;
	}
}
