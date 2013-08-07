/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jbpm.executor;

import java.util.UUID;

import javax.persistence.EntityManager;

import org.jbpm.executor.api.CommandCodes;
import org.jbpm.executor.api.CommandContext;
import org.jbpm.executor.entities.RequestInfo;

import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProvider;

import static org.jbpm.executor.api.CommandCodes.*;

/**
 * 
 * @author salaboy
 */
public class ExecutorMain {

	public static void main(String[] args) {
		System.out.println("Starting Executor Service ...");
		final ExecutorServiceEntryPoint executorServiceEntryPoint = ExecutorModule
				.getInstance().getExecutorServiceEntryPoint();
		executorServiceEntryPoint.init();
		System.out.println("Executor Service Started!");
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				executorServiceEntryPoint.destroy();
			}
		});

		// executorServiceEntryPoint.scheduleRequest(CommandCodes.SendEmailCommand,
		// context);
		for (int i = 0; i < 1; i++) {
			
			try{
				//Thread.sleep(1000);
			}catch(Exception e){}

			Thread t = new Thread() {

				public void run() {

					CommandContext context = new CommandContext();
					context.setData("to", "mdkimani@gmail.com");
					context.setData("from", "ebpm.mgr@gmail.com");
					context.setData("body", "Document above approved");
					context.setData("subject", "Inv/JKUAT/002/2013");
					context.setData("businessKey", UUID.randomUUID().toString());
					context.setData("callbacks", SendEmailCallback.name());
					executorServiceEntryPoint.scheduleRequest(
							CommandCodes.SendEmailCommand, context);

				}

			};
			t.start();
		}

	}

	public static void main2(String[] args) {
		DBTrxProvider.init();

		DB.beginTransaction();

		EntityManager em = DB.getEntityManager();
		RequestInfo req = new RequestInfo();
		em.persist(req);

		DB.commitTransaction();
		DB.closeSession();
		DBTrxProvider.close();
	}
}
