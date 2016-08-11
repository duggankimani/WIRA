package com.duggan.workflow.server.dao.hibernate;

import com.duggan.workflow.shared.model.Document;

public class JsonDocument extends JsonType{

	@Override
	public Class<Document> returnedClass() {
		return Document.class;
	}
}
