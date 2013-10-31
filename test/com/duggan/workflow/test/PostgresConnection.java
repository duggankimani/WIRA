package com.duggan.workflow.test;

import java.sql.Connection;
import java.sql.DriverManager;

import org.postgresql.xa.PGXADataSource;

public class PostgresConnection {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		Class.forName("org.postgresql.xa.PGXADataSource");
		Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/workflowmgr","postgres", "");
		
		PGXADataSource ds;
		conn.createStatement();
	}

}
