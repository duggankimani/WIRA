package com.duggan.workflow.server.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class DBUtil {

	Logger logger = Logger.getLogger(DBUtil.class);
	
	public String getStringValue(final String sql, String connectionName){
		
		DBExecute<String> exec = new DBExecute<String>(connectionName) {
			
			@Override
			protected void setParameters() throws SQLException {
				
			}
			
			@Override
			protected String processResults(PreparedStatement pStmt, boolean hasResults)
					throws SQLException {
				
				if(hasResults){
					return getResultSet().getString(1); 
				}
				return null;
			}
			
			@Override
			protected String getQueryString() {
				
				return sql;
			}
		};
		
		return exec.executeDbCall();
	}
	
}
