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

import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.transaction.SystemException;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.jbpm.executor.api.Command;
import org.jbpm.executor.api.CommandCallback;
import org.jbpm.executor.api.CommandCode;
import org.jbpm.executor.api.CommandCodes;
import org.jbpm.executor.api.CommandContext;
import org.jbpm.executor.api.ExecutionResults;
import org.jbpm.executor.entities.RequestInfo;

import com.duggan.workflow.server.dao.model.ErrorLog;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.shared.model.ExecutionStatus;

public class ExecutorUtil {

	private Logger logger = Logger.getLogger(ExecutorUtil.class
			.getCanonicalName());

	public void execute(RequestInfo requestInfo) {

		Throwable exception = null;
		EntityManager em = DB.getEntityManager();
		try {

			requestInfo.setStatus(ExecutionStatus.RUNNING);
			em.merge(requestInfo);
			logger.log(Level.INFO, " >> Processing Request Id: {0}",
					requestInfo.getId());
			logger.log(Level.INFO, " >> Request Status ={0}",
					requestInfo.getStatus());
			logger.log(Level.INFO, " >> Command Name to execute = {0}",
					requestInfo.getCommandName());

			String className=CommandCodes.class.getName();
			if(requestInfo.getCommandClass()!=null){
				className = requestInfo.getCommandClass();
			}
			Command cmd = this.findCommand(className,requestInfo.getCommandName());

			CommandContext ctx = null;
			byte[] reqData = requestInfo.getRequestData();
			if (reqData != null) {
				try {
					ObjectInputStream in = new ObjectInputStream(
							new ByteArrayInputStream(reqData));
					ctx = (CommandContext) in.readObject();
				} catch (IOException e) {
					ctx = null;
					e.printStackTrace();
				}
			}

			// Duggan-2 cents: if an exception happens here
			// rollback anything saved by processes called
			// by command .: this means the process should run in its own trx?
			ExecutionResults results = cmd.execute(ctx);
			if (ctx != null && ctx.getData("callbacks") != null) {
				logger.log(Level.INFO, " ### Callback: {0}",
						ctx.getData("callbacks"));
				String[] callbacksArray = ((String) ctx.getData("callbacks"))
						.split(",");
				;
				List<String> callbacks = (List<String>) Arrays
						.asList(callbacksArray);
				for (String callbackName : callbacks) {
					CommandCallback handler = this
							.findCommandCallback(CommandCodes
									.valueOf(callbackName));
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
					requestInfo.setResponseData(respData);
				} catch (IOException e) {
					requestInfo.setResponseData(null);
				}
			}

		} catch (Exception e) {
			// e.printStackTrace();
			exception = e;
		}

		if (exception != null) {
			try {
				// JBPM marks this/the current transaction for Rollback if an
				// error occurs -
				// This means that any command that starts off a process may
				// have to be
				// executed in its own transaction - However; embedded trx's are
				// not supported
				// further, mysql XA bug -during Suspend Resume
				// Starting off processes from async commands may have wait - -
				// -
				System.err.println("##########[Transaction status] - "
						+ DB.getUserTrx().getStatus());
			} catch (SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			logger.log(
					Level.SEVERE,
					"{0} >>> Before - Error Handling!!!{1}",
					new Object[] { System.currentTimeMillis(),
							exception.getMessage() });

			ErrorLog errorInfo = new ErrorLog(exception.getMessage(),
					ExceptionUtils.getFullStackTrace(exception
							.fillInStackTrace()));
			errorInfo.setRequestInfo(requestInfo);
			em.persist(errorInfo);
			em.merge(errorInfo);
			requestInfo.getErrorInfo().add(errorInfo);
			logger.log(Level.SEVERE, " >>> Error Number: {0}", requestInfo
					.getErrorInfo().size());
			if (requestInfo.getRetries() > 0) {
				requestInfo.setStatus(ExecutionStatus.RETRYING);
				requestInfo.setRetries(requestInfo.getRetries() - 1);
				requestInfo.setExecutions(requestInfo.getExecutions() + 1);
				logger.log(Level.SEVERE,
						" >>> Retrying ({0}) still available!",
						requestInfo.getRetries());
			} else {
				logger.severe(" >>> Error no retries left!");
				requestInfo.setStatus(ExecutionStatus.ERROR);
				requestInfo.setExecutions(requestInfo.getExecutions() + 1);
			}

			em.merge(requestInfo);

			logger.severe(" >>> After - Error Handling!!!");
		} else {

			requestInfo.setStatus(ExecutionStatus.DONE);
			em.merge(requestInfo);

		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Command findCommand(String enumClass,String commandName) {

		Class<?> handler = null;
		try {

			Class clazz = Class.forName(enumClass);
			CommandCode code = (CommandCode)Enum.valueOf(clazz, commandName);
			handler = code.getHandlerClass();
			return (Command) handler.newInstance();
		} catch (Exception e) {

			throw new IllegalArgumentException(
					"Unknown Command implemenation with name '" + enumClass+"."+commandName + "'");
		}

	}

	private CommandCallback findCommandCallback(CommandCodes name) {

		CommandCallback callback = null;

		try {
			callback = (CommandCallback) name.getHandlerClass().newInstance();
		} catch (Exception e) {
			throw new IllegalArgumentException(
					"Unknown CommandCallback implemenation with name '" + name
							+ "'");
		}

		return callback;
	}
}
