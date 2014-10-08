package com.duggan.workflow.server.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class DBUtil {

	static Logger logger = Logger.getLogger(DBUtil.class);
	
	public static String getStringValue(final String sql){
		return getStringValue(sql, null);
	}
	
	public static String getStringValue(final String sql, String connectionName){
		
		DBExecute<String> exec = new DBExecute<String>(connectionName) {
			
			@Override
			protected void setParameters() throws SQLException {
				
			}
			
			@Override
			protected String processResults(PreparedStatement pStmt, boolean hasResults)
					throws SQLException {
				
				if(hasResults){
					ResultSet rs = getResultSet();
					if(rs.next()){
						return rs.getString(1);
					}
				}
				return "";
			}
			
			@Override
			protected String getQueryString() {
				logger.info("DBUtil.getStringValue sql = "+sql);
				return sql;
			}
		};
		
		return exec.executeDbCall();
	}
	
}
