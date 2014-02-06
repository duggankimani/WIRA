package com.duggan.workflow.server.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

import com.duggan.workflow.server.dao.DSConfigDaoImpl;
import com.duggan.workflow.server.dao.model.DataSourceConfig;

public class JDBCConnection {

	Map<String, Connection> connections = new HashMap<>();
	
	public JDBCConnection() {
	}
	
	/**
	 * Get Connection based of name of the configuration
	 * 
	 * @param configName
	 * @return
	 */
	public Connection getConnection(String configName){
		
		if(connections.get(configName)!=null){
			return connections.get(configName);
		}
		
		DSConfigDaoImpl impl = DB.getDSConfigDao();
		
		DataSourceConfig config= impl.getConfigurationByName(configName);
		assert config!=null;
		
		String username = config.getUser();
		String password = config.getPassword();
		String url = config.getURL();
		String driver = config.getDriver();
		
		try{
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, username, password);		
			
			connections.put(configName, conn);
			
			return conn;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		 
	}
	
	public void dispose(){
		for(Connection con: connections.values()){
			try{
				con.commit();
			}catch(Exception e){
			}finally{
				try{
					con.close();
				}catch(Exception e){}
			}
		}
		connections.clear();
	}
}
