package com.duggan.workflow.server.dao.hibernate;

import java.sql.Types;

import org.hibernate.dialect.PostgreSQLDialect;

public class WiraPostgreSQL94Dialect extends PostgreSQLDialect{

	public WiraPostgreSQL94Dialect(){
		this.registerColumnType(Types.JAVA_OBJECT, "jsonb");
	}
}
