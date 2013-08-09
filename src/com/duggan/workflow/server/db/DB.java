package com.duggan.workflow.server.db;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.drools.runtime.Environment;
import org.hibernate.HibernateException;
import org.jbpm.bpmn2.xml.TaskHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.duggan.workflow.server.actionhandlers.BaseActionHandler;
import com.duggan.workflow.server.dao.DocumentDaoImpl;
import com.duggan.workflow.server.dao.ErrorDaoImpl;
import com.duggan.workflow.server.dao.NotificationDaoImpl;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;

/**
 * <p>
 * This class provides utility methods for 
 * beginning/committing & rolling back user transactions
 * 
 * <p>
 * Further it provides utility methods for retrieving {@link EntityManagerFactory}
 *  and the {@link EntityManager} 
 * 
 * <P>
 * Whenever an entity manager is requested, a corresponding {@link UserTransaction} has to have been
 * started/ began
 * 
 * <p>
 * A problem scenario that arises from this is one where {@link JBPMHelper} which initializes
 * an {@link Environment} variable and {@link TaskHandler} using a {@link EntityManagerFactory}
 * generates an {@link EntityManager} without a {@link UserTransaction} - since the UserTransaction
 * is application managed. In this case, an exception is thrown with a 'no active transaction' message.
 * 
 * <p>
 * To mitigate the above error, a {@link UserTransaction} transaction will be started for every
 * request and committed at the end of the request -see {@link BaseActionHandler}  
 * 
 * @author duggan
 *
 */
public class DB{
	
	private static Logger log = LoggerFactory.getLogger( DB.class );

    private static final ThreadLocal<EntityManager> entityManagers = new ThreadLocal<EntityManager>();

	private static ThreadLocal<DaoFactory> factory = new ThreadLocal<>();
    
    private DB(){}
    

	private static EntityManagerFactory emf;
    
    public static EntityManager getEntityManager(){
    	
    	EntityManager em = entityManagers.get();
    	
        if (em != null && !em.isOpen()) {
            em = null;
        }
        if (em == null) {
        	
        	synchronized(entityManagers){        		
	            if (emf == null) {    
	            	emf = getEntityManagerFactory(); //(serviceRegistry);	            	
	            }
        	}
        	
        	//beginTransaction();
            em = emf.createEntityManager();
            entityManagers.set(em);
        }

        return em;
    }

	/**
	 * This must be called before XA transaction is started
	 * @return
	 */
	public static EntityManagerFactory getEntityManagerFactory() {
		
		synchronized (log) {
			if(emf==null){
				try{				
					emf = Persistence.createEntityManagerFactory("org.jbpm.persistence.jpa");
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		
		return emf; 
	}


	public static void closeSession(){
		try{
			
			EntityManager em = (EntityManager) entityManagers.get();
	        if(em==null)
	        	return;	        
	        
		}catch(Exception e){
			
			try{
				rollback();
			}catch(Exception ex){}
			
			throw new RuntimeException(e);
		}finally{
			clearSession();
			
			closeFactory();
		}
		
	}
    

	/**
     * Close the single hibernate em instance.
     *
     * @throws HibernateException
     */
    private static void clearSession(){
        EntityManager em = (EntityManager) entityManagers.get();
        if(em==null)
        	return;
        
        entityManagers.set(null);
       
        if (em != null) {
            em.close();
        }
        
    }

    /**
     * Begin a {@link UserTransaction}
     * 
     * <p>
     * This is called whenever a new entity manager is requested
     */
    public static void beginTransaction() {
    	try{    	
    		getUserTrx().begin();
    	}catch(Exception e){
    		throw new RuntimeException(e);
    	}
    	
	}

    /**
     * This method commits a {@link UserTransaction}
     * <p>
     * A transaction is always generated whenever an entity manager is requested
     */
	public static void commitTransaction() {
		try{						
			//if(entityManagers.get()!=null)
			getUserTrx().commit();			
			
    	}catch(Exception e){
    		throw new RuntimeException(e);
    	}
		
	}

	/**
	 * Rollback a {@link UserTransaction}
	 */
	public static void rollback(){
		try{
			
			//if(entityManagers.get()!=null)
			getUserTrx().rollback();
			
    	}catch(Exception e){
    		e.printStackTrace();
    		throw new RuntimeException(e);
    	}
	}
	
	public static UserTransaction getUserTrx() throws NamingException {
		Context ctx = new InitialContext();
		Object value = ctx.lookup("java:comp/UserTransaction");
		
		if(value==null){
			return null;
		}
		
		UserTransaction userTrx = (UserTransaction)value;
		return userTrx;
	}
	
	private static DaoFactory factory(){
		
		DaoFactory daoFactory=factory.get();
		
		if(daoFactory==null){
			daoFactory = new DaoFactory();
			factory.set(daoFactory);
		}
		
		return daoFactory;
	}
	
	public static DocumentDaoImpl getDocumentDao(){
		return factory().getDocumentDao(getEntityManager());
	}
	
	public static ErrorDaoImpl getErrorDao() {

		return factory().getErrorDao(getEntityManager());
	}
	
	public static NotificationDaoImpl getNotificationDao() {

		return factory().getNotificationDao(getEntityManager());
	}

	

	private static void closeFactory() {
		if(factory.get()==null)
			return;
		
		factory.set(null);
	}

	public static boolean hasActiveTrx() throws SystemException, NamingException {
		
		int status = getUserTrx().getStatus();
		
		boolean active = false;
		
		switch (status) {
		case Status.STATUS_NO_TRANSACTION:
			active=false;
			break;

		default:
			active=true;
			break;
		}
		
		return active;
	}
}
