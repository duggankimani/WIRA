package com.duggan.workflow.server.dao.hibernate;

import com.duggan.workflow.shared.model.DocumentLine;

public class JsonDocLine extends JsonType{

	@Override
	public Class<DocumentLine> returnedClass() {
		return DocumentLine.class;
	}
}
