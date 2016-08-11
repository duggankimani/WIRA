package com.duggan.workflow.server.dao.hibernate;

public class JsonDocValue extends JsonType{

	@Override
	public Class returnedClass() {
		return DocValues.class;
	}
}
