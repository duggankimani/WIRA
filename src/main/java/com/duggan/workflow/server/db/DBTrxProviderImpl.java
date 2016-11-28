package com.duggan.workflow.server.db;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

import javax.transaction.UserTransaction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import bitronix.tm.Configuration;
import bitronix.tm.TransactionManagerServices;
import bitronix.tm.resource.ResourceLoader;

/**
 * 
 * <p>
 * This class provides standalone JTA transactions to the workflow engine.
 * In addition, it creates a connection pooled datasource and
 * exposes it to the rest of the application through JNDI.  
 * </p>
 * 
 * <p>
 * The Bitronix Transaction manager is used for the JTA transactions. BTM also provides 
 * an embedded JNDI provider onto which the resources are bound
 * <p>
 * 
 * <p>
 * The datasource is initialised using the BTM {@link ResourceLoader} which looks for 
 * <b>db.propertiess</b> file from the root of the classpath   
 * The datasource is then bound onto the <b>JNDI Name</b> = <b>${datasource_uniqueName}</b>
 *  i.e The jndi name for the datasource is the <b>unique name</b> provided in the db.properties file
 * </p>
 * 
 * <p>
 * BTM also generates a <b>JTA {@link UserTransaction}</b> that is bound to the <b>JNDI name java:comp/UserTransaction</b>
 * </p>
 * 
 * <p>
 * For more details see <a href="http://docs.codehaus.org/display/BTM/Jndi2x">BTM</a>
 * </p>
 * @author duggan
 *
 */
public class DBTrxProviderImpl{

	static DBTrxProviderImpl provider;
	
	Log logger = LogFactory.getLog(DBTrxProviderImpl.class);
	
	static Properties dbProperties = new Properties();
	static Boolean isTomcatEnv = Boolean.TRUE;
	void loadProperties(){
		try{
			InputStream is = DBTrxProviderImpl.class.getResourceAsStream("/db.properties");
			dbProperties.load(is);
			String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
			if(path.contains("webapps") || path.toLowerCase().contains("tomcat")){
				isTomcatEnv = Boolean.TRUE;
			}else{
				isTomcatEnv = Boolean.FALSE;
			}
		}catch(Exception e){e.printStackTrace();}
	}
	
	/**
	 * Loads db values from db.properties
	 */
	private DBTrxProviderImpl(){
		loadProperties();
		
		if(!isTomcatEnv){
			logger.warn("No Global User Trx Found - Could you be running in development mode?");
			//non
			Configuration config = TransactionManagerServices.getConfiguration();
			
			//Data Importation - Please Remove - DUGGAN KIMANI 6/10/2016
			config.setDefaultTransactionTimeout(300);
			
			File resourceProperties = new File(Thread.currentThread().getContextClassLoader().getResource("").getPath());
			config.setResourceConfigurationFilename(resourceProperties.getPath()+"/db.properties");
			resourceProperties=null;
			TransactionManagerServices.getResourceLoader().init();
		}else{
			logger.warn("Global User Trx Found - Assuming Tomcat Runtime!!");
		}
		
	}
	
	/**
	 * Initialize EMF
	 */
	public static void init(){
		if(provider==null){
			
			synchronized(DBTrxProviderImpl.class){
				if(provider==null){
					provider = new DBTrxProviderImpl();
				}
			}
			
		}
	}

	/**
	 * Clean shutdown of Bitronix
	 */
	public static void close() {
		if(!isTomcatEnv){
			TransactionManagerServices.getTransactionManager().shutdown();
		}
		
	}
	
}
