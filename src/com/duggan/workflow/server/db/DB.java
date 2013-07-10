package com.duggan.workflow.server.db;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.duggan.workflow.server.dao.DocumentDaoImpl;

/**
 * 
 * @author duggan
 *
 *Bootstrap Hibernate
 */
public class DB{
	
	private static Logger log = LoggerFactory.getLogger( DB.class );

    private static final ThreadLocal<EntityManager> entityManagers = new ThreadLocal<EntityManager>();

	private static ThreadLocal<DaoFactory> factory = new ThreadLocal<>();
    
    private DB(){}
    

	private static EntityManagerFactory emf;
    
    private static EntityManager getEntityManager(){
    	
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
        	
            em = emf.createEntityManager();
  
            beginTransaction(em);
            entityManagers.set(em);
        }

        return em;
    }

	/**
	 * 
	 * @return
	 */
	public static EntityManagerFactory getEntityManagerFactory() {
		
		synchronized (log) {
			if(emf==null){
				emf = Persistence.createEntityManagerFactory("org.jbpm.task");
			}
		}
		
		return emf; 
	}


	public static void closeSession(){
		try{
			
			EntityManager em = (EntityManager) entityManagers.get();
	        if(em==null)
	        	return;
	        
	        commitTransaction(em);

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

    private static void beginTransaction(EntityManager em) {
		em.getTransaction().begin();
	}

	private static void commitTransaction(EntityManager em) {
		EntityTransaction trx = em.getTransaction();
		if(trx!=null){
			trx.commit();
		}
		
	}
	
	public static void rollback(){
		EntityManager em = entityManagers.get();
		
		if(em!=null){
			EntityTransaction trx = em.getTransaction();
			if(trx!=null)
				trx.rollback();
		}
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

	private static void closeFactory() {
		if(factory.get()==null)
			return;
		
		factory.set(null);
	}
}
