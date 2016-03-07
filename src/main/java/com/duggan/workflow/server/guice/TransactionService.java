package com.duggan.workflow.server.guice;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import com.google.inject.Inject;

public class TransactionService {

	private UserTransactionProvider userTransactionProvider;

	@Inject
	private TransactionService(UserTransactionProvider userTransactionProvider) {
		this.userTransactionProvider = userTransactionProvider;
	}

	public void begin() {
		try {
			userTransactionProvider.get().begin();
		} catch (NotSupportedException | SystemException e) {
			e.printStackTrace();
		}
	}

	public void end() {
		try {
			int status = userTransactionProvider.get().getStatus();

			// STATUS_ACTIVE 0
			// STATUS_COMMITTED 3
			// STATUS_COMMITTING 8
			// STATUS_MARKED_ROLLBACK 1
			// STATUS_NO_TRANSACTION 6
			// STATUS_PREPARED 2
			// STATUS_PREPARING 7
			// STATUS_ROLLEDBACK 4
			// STATUS_ROLLING_BACK 9
			// STATUS_UNKNOWN 5

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
	}

	public int getStatus() {
		int status = -1;
		try {
			status = userTransactionProvider.get().getStatus();
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return status;
	}

}
