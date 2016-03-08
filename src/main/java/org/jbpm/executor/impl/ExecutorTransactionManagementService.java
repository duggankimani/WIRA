package org.jbpm.executor.impl;

import javax.transaction.SystemException;

import org.apache.log4j.Logger;

import com.duggan.workflow.server.guice.TransactionService;
import com.duggan.workflow.server.guice.UserTransactionProvider;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.persist.UnitOfWork;

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

	private Logger logger = Logger
			.getLogger(ExecutorTransactionManagementService.class);

	private TransactionService trxService;
	private UnitOfWork unitOfWork;

	@Inject
	private ExecutorTransactionManagementService(UnitOfWork unitOfWork,
			TransactionService trxService) {
		this.unitOfWork = unitOfWork;
		this.trxService = trxService;
		this.unitOfWork = unitOfWork;
	}

	public void begin() {
		trxService.begin();
		unitOfWork.begin();
		logger.debug("TrxBegin## Thread " + Thread.currentThread() + ";"
				+ unitOfWork + "- Trx Status= " + trxService.getStatus());
	}

	public void end() {
		trxService.end();
		unitOfWork.end();
		logger.debug("##TrxEnd Thread " + Thread.currentThread() + ";"
				+ unitOfWork + "- Trx Status= " + trxService.getStatus());
	}

}
