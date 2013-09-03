package com.duggan.workflow.server.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

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
public class DBTrxProvider{

	static DBTrxProvider provider;
	
	Log logger = LogFactory.getLog(DBTrxProvider.class);
	
	static Properties dbProperties = new Properties();
	static Boolean isTomcatEnv = Boolean.TRUE;
	static{
		try{
			InputStream is = DBTrxProvider.class.getResourceAsStream("/db.properties");
			dbProperties.load(is);
			String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
			if(path.contains("webapps") || path.toLowerCase().contains("tomcat")){
				isTomcatEnv = Boolean.TRUE;
			}else{
				isTomcatEnv = Boolean.FALSE;
			}
		}catch(Exception e){e.printStackTrace();}
	}
	
	private DBTrxProvider(){
		
//		Object userTrx = null;
//		try{
//			userTrx = DB.getUserTrx();
//		}catch(Exception e){}
		
		if(!isTomcatEnv){
			logger.warn("No Global User Trx Found - Could you be running in development mode?");
			//non
			Configuration config = TransactionManagerServices.getConfiguration();
			File resourceProperties = new File(Thread.currentThread().getContextClassLoader().getResource("").getPath());
			config.setResourceConfigurationFilename(resourceProperties.getPath()+"/db.properties");
			resourceProperties=null;
			TransactionManagerServices.getResourceLoader().init();
		}else{
			logger.warn("Global User Trx Found - Assuming Tomcat Runtime!!");
		}
		
	}
	
	public static void init(){
		if(provider==null){
			
			synchronized(DBTrxProvider.class){
				if(provider==null){
					provider = new DBTrxProvider();
					DB.getEntityManagerFactory();//initialize emf
				}
			}
			
		}
	}

	public static void close() {
		TransactionManagerServices.getTransactionManager().shutdown();
	}
	
}
