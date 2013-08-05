/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jbpm.executor.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.jbpm.executor.api.Command;
import org.jbpm.executor.api.CommandCallback;
import org.jbpm.executor.api.CommandCodes;
import org.jbpm.executor.api.CommandContext;
import org.jbpm.executor.api.ExecutionResults;
import org.jbpm.executor.entities.ErrorInfo;
import org.jbpm.executor.entities.RequestInfo;
import org.jbpm.executor.entities.STATUS;

import com.duggan.workflow.server.db.DB;

/**
 *
 * @author salaboy
 */
public class ExecutorRunnable extends Thread {
    
	
    private Logger logger = Logger.getLogger(ExecutorRunnable.class.getCanonicalName());
    
    
    
    public ExecutorRunnable(){
    	setDaemon(true);
    }

    /**
     * BTM will rollback transactions that live for too
     * long, therefore it is important to reduce the number of 
     * requests sent/retried to an optimum number. 
     */
    public void run() {
    	
    	DB.beginTransaction();
    	EntityManager em = DB.getEntityManagerFactory().createEntityManager();
    	
        logger.log(Level.INFO, " >>> Executor Thread {0} Waking Up!!!", this.toString());
        List<?> resultList = em.createQuery("Select r from RequestInfo as r where r.status ='QUEUED' or r.status = 'RETRYING' ORDER BY r.time DESC").setMaxResults(5).getResultList();
        logger.log(Level.INFO, " >>> Pending Requests = {0}", resultList.size());
        if (resultList.size() > 0) {
            RequestInfo r = null;
            Throwable exception = null;
            try {
                r = (RequestInfo) resultList.get(0);
                r.setStatus(STATUS.RUNNING);
                em.merge(r);
                logger.log(Level.INFO, " >> Processing Request Id: {0}", r.getId());
                logger.log(Level.INFO, " >> Request Status ={0}", r.getStatus());
                logger.log(Level.INFO, " >> Command Name to execute = {0}", r.getCommandName());


                Command cmd = this.findCommand(r.getCommandName());

                CommandContext ctx = null;
                byte[] reqData = r.getRequestData();
                if (reqData != null) {
                    try {
                        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(reqData));
                        ctx = (CommandContext) in.readObject();
                        String subject = ctx.getData().get("Subject")==null? "Subject" : ctx.getData().get("Subject").toString();
                        
                        subject = subject.concat(" -ID "+r.getId());
                        ctx.setData("Subject", subject);
                        
                    } catch (IOException e) {
                        ctx = null;
                        e.printStackTrace();
                    }
                }
                ExecutionResults results = cmd.execute(ctx);
                if (ctx != null && ctx.getData("callbacks") != null) {
                    logger.log(Level.INFO, " ### Callback: {0}", ctx.getData("callbacks"));
                    String[] callbacksArray = ((String) ctx.getData("callbacks")).split(",");;
                    List<String> callbacks = (List<String>) Arrays.asList(callbacksArray);
                    for (String callbackName : callbacks) {
                        CommandCallback handler = this.findCommandCallback(CommandCodes.valueOf(callbackName));
                        handler.onCommandDone(ctx, results);
                    }
                } else {
                    logger.info(" ### Callbacks: NULL");
                }
                if (results != null) {
                    try {
                        ByteArrayOutputStream bout = new ByteArrayOutputStream();
                        ObjectOutputStream out = new ObjectOutputStream(bout);
                        out.writeObject(results);
                        byte[] respData = bout.toByteArray();
                        r.setResponseData(respData);
                    } catch (IOException e) {
                        r.setResponseData(null);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                exception = e;
            }
            if (exception != null) {
                logger.log(Level.SEVERE, "{0} >>> Before - Error Handling!!!{1}", new Object[]{System.currentTimeMillis(), exception.getMessage()});



                ErrorInfo errorInfo = new ErrorInfo(exception.getMessage(), ExceptionUtils.getFullStackTrace(exception.fillInStackTrace()));
                errorInfo.setRequestInfo(r);
                r.getErrorInfo().add(errorInfo);
                logger.log(Level.SEVERE, " >>> Error Number: {0}", r.getErrorInfo().size());
                if (r.getRetries() > 0) {
                    r.setStatus(STATUS.RETRYING);
                    r.setRetries(r.getRetries() - 1);
                    r.setExecutions(r.getExecutions() + 1);
                    logger.log(Level.SEVERE, " >>> Retrying ({0}) still available!", r.getRetries());
                } else {
                    logger.severe(" >>> Error no retries left!");
                    r.setStatus(STATUS.ERROR);
                    r.setExecutions(r.getExecutions() + 1);
                }

                em.merge(r);


                logger.severe(" >>> After - Error Handling!!!");


            } else {

                r.setStatus(STATUS.DONE);
                em.merge(r);

            }
            DB.commitTransaction();
            DB.closeSession();
        }
    }

    private Command findCommand(CommandCodes name) {

    	Class<?> handler = null;
    	try{
    		handler = name.getHandlerClass();
    		return (Command)handler.newInstance();
    	}catch(Exception e){
    		
    		 throw new IllegalArgumentException("Unknown Command implemenation with name '"+name+"'");
    	}
    	
    }
    
    private CommandCallback findCommandCallback(CommandCodes name) {

    	CommandCallback callback = null;
    	
    	try{
    		callback = (CommandCallback) name.getHandlerClass().newInstance();
    	}catch(Exception e){
    		 throw new IllegalArgumentException("Unknown CommandCallback implemenation with name '"+name+"'");
    	}
        
    	return callback;
    }
}
