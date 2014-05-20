package com.duggan.workflow.test.dbin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.duggan.workflow.server.db.LookupLoaderImpl;
import com.duggan.workflow.shared.model.form.KeyValuePair;

public class DBIntegration {

	@Before
	public void setup(){
//		DBTrxProvider.init();
//		DB.beginTransaction();
	}
	
	@Test
	public void testConnection() throws Exception{
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		Connection connection = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=mobileBanking;integratedSecurity=true;");
		
		connection.createStatement().execute("Select 1");
		
		
	}
	
	@Ignore
	public void testIntegration(){
		LookupLoaderImpl loader = new LookupLoaderImpl();
		List<KeyValuePair> pairs = 
				loader.getValuesByDataSourceName("Postgresql-Local", "select userid, lastname from buser");
		pairs=loader.getValuesByDataSourceName("Postgresql-Local", "select id, name from bgroup");
		System.err.println(pairs);
	}
	
	@org.junit.After
	public void destroy() throws IOException{
//		DB.rollback();
//		DB.closeSession();
	}

}
