@org.hibernate.annotations.TypeDefs({
	@org.hibernate.annotations.TypeDef(name = "JsonForm", typeClass = com.duggan.workflow.server.dao.hibernate.JsonForm.class),
	@org.hibernate.annotations.TypeDef(name = "JsonField", typeClass = com.duggan.workflow.server.dao.hibernate.JsonField.class)
})

package com.duggan.workflow.server.dao.model;