package com.duggan.workflow.server.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
	
	
	public static List<Object[]> getValues(final String sql, String connectionName){
		
		DBExecute<List<Object[]>> exec = new DBExecute<List<Object[]>>(connectionName) {
			
			@Override
			protected void setParameters() throws SQLException {
				
			}
			
			@Override
			protected List<Object[]> processResults(PreparedStatement pStmt, boolean hasResults)
					throws SQLException {
				List<Object[]> rows = new ArrayList<Object[]>();

				if(hasResults){
					
					ResultSet rs = getResultSet();
					int colCount = rs.getMetaData().getColumnCount();
					while(rs.next()){
						
						//Create an Array of size colCount
						Object[] columnValues = new Object[colCount];
						for(int i=0; i<colCount; i++){
							//copy resultset column values
							columnValues[i] = rs.getObject(i+1);
						}
						rows.add(columnValues);
					}
					
				}
				return rows;
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
