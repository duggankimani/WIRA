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
        }
        return instance;
    }

    private ExecutorModule() {
        executorService = new ExecutorServiceEntryPointImpl(); 
        executorService.init();
    }

	public ExecutorServiceEntryPoint getExecutorServiceEntryPoint() {
        return this.executorService;
    }

    
    public void dispose(){
        instance = null;
    }
    
    
}