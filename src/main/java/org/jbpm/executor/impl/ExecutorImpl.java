/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.jbpm.executor.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;

import org.jbpm.executor.ExecutorModule;
import org.jbpm.executor.api.CommandCodes;
import org.jbpm.executor.api.CommandContext;
import org.jbpm.executor.api.Executor;
import org.jbpm.executor.commands.SendMailCommand;
import org.jbpm.executor.entities.RequestInfo;

import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.shared.model.ExecutionStatus;
import com.duggan.workflow.shared.model.RequestInfoDto;

/**
 * Singleton
 *
 * @author salaboy
 */
public class ExecutorImpl implements Executor {

	private Logger logger = Logger.getLogger(ExecutorImpl.class
			.getCanonicalName());

	private ExecutorRunnable task;

	private ScheduledFuture<?> handle;
	private int threadPoolSize = 1;
	private int retries = 3;
	private int interval = 15;

	private ScheduledExecutorService scheduler;

	public ExecutorImpl() {
		task = ExecutorFactory.getExecutorRunnable();
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public int getRetries() {
		return retries;
	}

	public void setRetries(int retries) {
		this.retries = retries;
	}

	public int getThreadPoolSize() {
		return threadPoolSize;
	}

	public void setThreadPoolSize(int threadPoolSize) {
		this.threadPoolSize = threadPoolSize;
	}

	public void init() {

		logger.log(Level.INFO, " >>> Starting Executor Component ...\n"
				+ " \t - Thread Pool Size: {0}" + "\n" + " \t - Interval: {1}"
				+ " Seconds\n" + " \t - Retries per Request: {2}\n",
				new Object[] { threadPoolSize, interval, retries });

		scheduler = Executors.newScheduledThreadPool(threadPoolSize);
		handle = scheduler.scheduleAtFixedRate(task, 2, interval,
				TimeUnit.SECONDS);
	}
	
	public void executeNow(RequestInfo info){
		scheduler.execute(new ExecutorRunnable());
	}

	public synchronized Long scheduleRequest(CommandCodes commandId,
			CommandContext ctx) {
		EntityManager em = DB.getEntityManager();
		long start = System.currentTimeMillis();
		if (ctx == null) {
			throw new IllegalStateException("A Context Must Be Provided! ");
		}
		String businessKey = (String) ctx.getData("businessKey");
		RequestInfo requestInfo = new RequestInfo();
		requestInfo.setCommandName(commandId);
		requestInfo.setKey(businessKey);
		requestInfo.setStatus(ExecutionStatus.QUEUED);
		requestInfo.setMessage("Ready to execute");
		if (ctx.getData("retries") != null) {
			requestInfo.setRetries((Integer) ctx.getData("retries"));
		} else {
			requestInfo.setRetries(retries);
		}
		if (ctx != null) {
			try {
				ByteArrayOutputStream bout = new ByteArrayOutputStream();
				ObjectOutputStream oout = new ObjectOutputStream(bout);
				oout.writeObject(ctx);
				requestInfo.setRequestData(bout.toByteArray());
			} catch (IOException e) {
				e.printStackTrace();
				requestInfo.setRequestData(null);
			}
		}

		em.persist(requestInfo);
		// System.out.println("Before Persist.... "+(System.currentTimeMillis()-start)+"ms");
		// System.out.println("After Persist.... "+(System.currentTimeMillis()-start)+"ms");

		logger.log(
				Level.INFO,
				" >>> Scheduling request for Command: {0} - requestId: {1} with {2} retries",
				new Object[] { commandId, requestInfo.getId(),
						requestInfo.getRetries() });
		return requestInfo.getId();
	}

	@Override
	public synchronized Long updateRequest(RequestInfoDto dto, boolean resend) {

		RequestInfo requestInfo = ExecutorModule.getInstance()
				.getExecutorServiceEntryPoint().getRequestById(dto.getRefId());

		CommandContext ctx;
		
		try {
			EntityManager em = DB.getEntityManager();
			ObjectInputStream in = new ObjectInputStream(
					new ByteArrayInputStream(requestInfo.getRequestData()));
			ctx = (CommandContext) in.readObject();
			Map<String, Object> data = ctx.getData();

//			if(dto.getUsers()!=null && !dto.getUsers().isEmpty()){
//				data.put(SendMailCommand.RECIPIENTS, dto.getUsers());
//			}else{
//				data.put(SendMailCommand.RECIPIENTS, dto.getRecipients());
//			}
			
			logger.warning("#######ExecutorImpl.java >> UserList "+dto.getUsers()+", "
					+ "RecipientsStr "+dto.getRecipients()+"");
			
			data.put(SendMailCommand.SUBJECT, dto.getSubject());
			data.put(SendMailCommand.BODY, dto.getBody());

			requestInfo.setStatus(ExecutionStatus.QUEUED);
			requestInfo.setMessage("Ready to execute");
			if (ctx.getData("retries") != null) {
				requestInfo.setRetries((Integer) ctx.getData("retries"));
			} else {
				requestInfo.setRetries(retries);
			}

			//
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			ObjectOutputStream oout = new ObjectOutputStream(bout);
			oout.writeObject(ctx);
			requestInfo.setRequestData(bout.toByteArray());

			//Test - do not save
			//em.persist(requestInfo);
			
			if(resend){
				new ExecutorUtil().execute(requestInfo);
			}

		} catch (IOException e) {
			ctx = null;
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}

	public void cancelRequest(Long requestId) {
		logger.log(Level.INFO, " >>> Before - Cancelling Request with Id: {0}",
				requestId);
		EntityManager em = DB.getEntityManager();
		
		String eql = "Select r from RequestInfo as r where (r.status ='QUEUED' or r.status ='RETRYING') and id = :id";
		List<?> result = em.createQuery(eql).setParameter("id", requestId)
				.getResultList();
		if (result.isEmpty()) {
			return;
		}
		RequestInfo r = (RequestInfo) result.iterator().next();

		em.lock(r, LockModeType.WRITE);
		r.setStatus(ExecutionStatus.CANCELLED);
		em.merge(r);

		logger.log(Level.INFO, " >>> After - Cancelling Request with Id: {0}",
				requestId);
	}

	public void destroy() {
		logger.info(" >>>>> Destroying Executor !!!");
		if (handle != null)
			handle.cancel(true);
		if (scheduler != null) {
			scheduler.shutdownNow();
		}
	}

}
