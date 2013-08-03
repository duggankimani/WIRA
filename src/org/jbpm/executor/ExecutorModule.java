/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jbpm.executor;

import org.jbpm.executor.impl.ExecutorServiceEntryPointImpl;

import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProvider;


/**
 *
 */
public class ExecutorModule {
    private static ExecutorModule instance;
    private ExecutorServiceEntryPoint executorService;
    
    public static ExecutorModule getInstance(){
        if(instance == null){
            instance = new ExecutorModule();
            DBTrxProvider.init();
          //This is a hack to force generation of a persistence unit before any XA transactions are started/ Activated
            //Hibernate cannot perform DDL within an active XA trx
            DB.getEntityManagerFactory();  
        }
        return instance;
    }

    private ExecutorModule() {
        executorService = new ExecutorServiceEntryPointImpl();        
    	//Singleton.. that we need to instantiate
        //this.container.instance().select(TaskLifeCycleEventListener.class).get(); 
    }

	public ExecutorServiceEntryPoint getExecutorServiceEntryPoint() {
        return this.executorService;
    }

    
    public void dispose(){
        instance = null;
    }
    
    
}
