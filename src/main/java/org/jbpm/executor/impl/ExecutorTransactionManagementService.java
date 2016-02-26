package org.jbpm.executor.impl;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import org.apache.onami.persist.AllUnitsOfWork;

import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.guice.UserTransactionProvider;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * This service starts and closes persistence units for the server side threads
 * 
 * Its the equivalent of
 * 
 * @author duggan
 *
 */
@Singleton
public class ExecutorTransactionManagementService {

	ExecutorTransactionManagementService service = null;
	private AllUnitsOfWork allUnitsOfWork;
	private UserTransactionProvider userTransactionProvider;

	@Inject
	private ExecutorTransactionManagementService(AllUnitsOfWork allUnitsOfWork,
			UserTransactionProvider userTransactionProvider) {
		this.allUnitsOfWork = allUnitsOfWork;
		this.userTransactionProvider = userTransactionProvider;
	}

	public void begin() {
		allUnitsOfWork.beginAllInactiveUnitsOfWork();
		try {
			userTransactionProvider.get().begin();
			DB.getEntityManager().joinTransaction();
		} catch (NotSupportedException | SystemException e) {
			e.printStackTrace();
		}
	}

	public void end() {
		try {
			int status = userTransactionProvider.get().getStatus();
			/*
			 * STATUS_ACTIVE 0 STATUS_COMMITTED 3 STATUS_COMMITTING 8
			 * STATUS_MARKED_ROLLBACK 1 STATUS_NO_TRANSACTION 6 STATUS_PREPARED
			 * 2 STATUS_PREPARING 7 STATUS_ROLLEDBACK 4 STATUS_ROLLING_BACK 9
			 * STATUS_UNKNOWN 5
			 */

			// JBPM engine marks transactions for rollback everytime
			// something goes wrong - it does'nt necessarily throw an exception
			if (status == 1 || status == 4 || status == 9) {
				userTransactionProvider.get().rollback();
			} else {
				userTransactionProvider.get().commit();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		allUnitsOfWork.endAllUnitsOfWork();
	}
}
