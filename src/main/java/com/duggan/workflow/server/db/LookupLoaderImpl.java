package com.duggan.workflow.server.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.duggan.workflow.shared.model.form.KeyValuePair;

public class LookupLoaderImpl implements LookupLoader{

	Logger logger = Logger.getLogger(LookupLoaderImpl.class);
	
	@Override
	public ArrayList<KeyValuePair> getValuesByJNDIName(String jndiName, String sql) {

		return null;
	}

	@Override
	public ArrayList<KeyValuePair> getValuesByDataSourceName(final String dataSourceName,
			final String sql) {

		DBExecute<List<KeyValuePair>> exec = new DBExecute<List<KeyValuePair>>(dataSourceName) {
			
			@Override
			protected void setParameters() throws SQLException {
				
			}
			
			@Override
			protected List<KeyValuePair> processResults(PreparedStatement pStmt,
					boolean hasResults) throws SQLException {

				List<KeyValuePair> values = new ArrayList<>();
				if(hasResults){
					ResultSet rs = getResultSet();
					
					while(rs.next()){
						String key = rs.getString(1);
						String value = rs.getString(2);
						assert key!=null;
						
						if(value==null){
							value=key;
						}
						
						KeyValuePair pair = new KeyValuePair(key.toString(), value.toString());
						values.add(pair);
					}
				}
				return values;
			}
			
			@Override
			protected String getQueryString() {
				return sql;
			}
		}; 
		
		logger.debug("Execute SQL: "+sql);
		
		List<KeyValuePair> pair = exec.executeDbCall();
		
		return (ArrayList<KeyValuePair>) pair;
	}
	
	public boolean testDatasourceName(String dataSourceName){
		
		DBExecute<Boolean> exec = new DBExecute<Boolean>(dataSourceName){

			@Override
			protected String getQueryString() {
				
				return "Select 1";
			}

			@Override
			protected void setParameters() throws SQLException {
				
			}

			@Override
			protected Boolean processResults(PreparedStatement pStmt,
					boolean hasResults) throws SQLException {
				
				if(!hasResults)
					return false;
				
				ResultSet rs = getResultSet();
				if(rs.next()){
					return rs.getInt(1)==1;
				}
				
				return false;
			}

		};
		
		try{
			return exec.executeDbCall();
		}catch(Exception e){
			return false;
		}
	}
	

}
