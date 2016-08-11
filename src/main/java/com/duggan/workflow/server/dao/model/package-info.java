@org.hibernate.annotations.TypeDefs({
	@org.hibernate.annotations.TypeDef(name = "JsonForm", typeClass = com.duggan.workflow.server.dao.hibernate.JsonForm.class),
	@org.hibernate.annotations.TypeDef(name = "JsonField", typeClass = com.duggan.workflow.server.dao.hibernate.JsonField.class),
	@org.hibernate.annotations.TypeDef(name = "JsonDocument", typeClass = com.duggan.workflow.server.dao.hibernate.JsonDocument.class),
	@org.hibernate.annotations.TypeDef(name = "JsonDocLine", typeClass = com.duggan.workflow.server.dao.hibernate.JsonDocLine.class),
	@org.hibernate.annotations.TypeDef(name = "JsonDocValue", typeClass = com.duggan.workflow.server.dao.hibernate.JsonDocValue.class)
})

package com.duggan.workflow.server.dao.model;