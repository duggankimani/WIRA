/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jbpm.executor.impl;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

import org.jbpm.executor.api.Command;
import org.jbpm.executor.api.CommandContext;
import org.jbpm.executor.api.ExecutionResults;
import org.jbpm.executor.entities.RequestInfo;

import com.duggan.workflow.server.db.DB;

/**
 *
 * @author salaboy
 */
public class ExecutorRunnable extends Thread {
    
	
    private Logger logger = Logger.getLogger(ExecutorRunnable.class.getCanonicalName());
    
    public ExecutorRunnable(){
    	//setDaemon(true);
    	logger.setLevel(Level.WARNING);
    }

    /**
     * BTM will rollback transactions that live for too
     * long, therefore it is important to reduce the number of 
     * requests sent/retried to an optimum number. 
     */
    public void run() {
    	executeInTrx();
    }

    private void executeInTrx() {
    	try{
    		DB.beginTransaction();
    		execute();
    		DB.commitTransaction();
    	}catch(Exception e){
    		logger.severe("############[1] Error : "+e.getMessage());
    		try{
    			DB.rollback();
    		}catch(Exception f){logger.severe("############ RollbackError : "+f.getMessage());}
    	}finally{
    		DB.closeSession();
    	}
	}

	private void execute() {
    	EntityManager em = DB.getEntityManager();
    	
        logger.log(Level.INFO, " >>> Executor Thread {0} Waking Up!!!", this.toString());
        List<?> resultList = em.createQuery("Select r from RequestInfo as r "
        		+ "where r.status ='QUEUED' "
        		+ "or r.status = 'RETRYING' ORDER BY r.time DESC")
        		.setMaxResults(3).getResultList();
        
        logger.log(Level.INFO, " >>> Pending Requests = {0}", resultList.size());
        if (resultList.size() > 0) {
            RequestInfo r = null;
            r = (RequestInfo) resultList.get(0);
            new ExecutorUtil().execute(r);
        }
        logger.log(Level.INFO, " >>> Executor Thread {0} Committing.....!!!", this.toString());
        logger.log(Level.INFO, " >>> Executor Thread {0} Committed . Closed Session.....!!!", this.toString());

	}

    /**
     * BTM does not support nested transactions!!
     * 
     * There's need to rollback processes initiated 
     * by the command execution in case of an error.
     * 
     * @param cmd
     * @param ctx
     * @return
     * @throws Exception
     */
	private ExecutionResults runInIndependentTrx(Command cmd, CommandContext ctx) throws Exception{

		//DB.beginTransaction();//resource local trx - Not JTA
		int status = DB.getUserTrx().getStatus();
		/*STATUS_ACTIVE               0
		STATUS_COMMITTED            3
		STATUS_COMMITTING           8
		STATUS_MARKED_ROLLBACK      1
		STATUS_NO_TRANSACTION       6
		STATUS_PREPARED             2
		STATUS_PREPARING            7
		STATUS_ROLLEDBACK           4
		STATUS_ROLLING_BACK         9
		STATUS_UNKNOWN              5*/
		
		return cmd.execute(ctx);
	}

}
