package com.duggan.workflow.server.db;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.apache.log4j.Logger;
import org.apache.onami.persist.EntityManagerProvider;

import com.duggan.workflow.server.guice.UserTransactionProvider;
import com.duggan.workflow.server.guice.WiraPU;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class TransactionFilter implements Filter {

	@Inject
	@WiraPU
	private EntityManagerProvider emProvider;

	@Inject
	private UserTransactionProvider userTransactionProvider;

	Logger log = Logger.getLogger(this.getClass());

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		beginTrx();
		chain.doFilter(request, response);
		commitTrx();
	}

	private void commitTrx() {
		EntityManager em = emProvider.get();
		UserTransaction trx = userTransactionProvider.get();
		try {
			String trxStatus = userTransactionProvider.get().getStatus()
					+ "; em = " + emProvider.get() + "; UserTrx="
					+ userTransactionProvider.get();
			log.debug(": Executing Commit doFilterTrx - ; TrxStatus "
					+ trxStatus);

			// if(entityManagers.get()!=null)
			
			int status = trx.getStatus();
			
			/*
			 * STATUS_ACTIVE 0 STATUS_COMMITTED 3 STATUS_COMMITTING 8
			 * STATUS_MARKED_ROLLBACK 1 STATUS_NO_TRANSACTION 6 STATUS_PREPARED
			 * 2 STATUS_PREPARING 7 STATUS_ROLLEDBACK 4 STATUS_ROLLING_BACK 9
			 * STATUS_UNKNOWN 5
			 */

			// JBPM engine marks transactions for rollback everytime
			// something goes wrong - it does'nt necessarily throw an exception
			if (status == 1 || status == 4 || status == 9) {
				log.warn("Rolling Back Trx with status " + status);
				trx.rollback();
			} else {
				log.debug("Commiting Back Trx with status " + status);
				trx.commit();
			}

		} catch (SecurityException | IllegalStateException | RollbackException
				| HeuristicMixedException | HeuristicRollbackException
				| SystemException e) {
			e.printStackTrace();
		}

	}

	private void beginTrx() {
		try {

			
			UserTransaction trx = userTransactionProvider.get();
			String trxStatus = "";
			
			if (trx.getStatus() == Status.STATUS_NO_TRANSACTION) {
				trx.begin();
			}
			EntityManager em = emProvider.get();
			em.joinTransaction();
			trxStatus = trx.getStatus() + "; em = " + em 
					+ "[Open="+em.isOpen()+", joinedToTrx="+em.isJoinedToTransaction()+"]"+
					"; UserTrx=" + trx;
			log.debug(": Executing doFilterTrx ; TrxStatus " + trxStatus);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void destroy() {
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		log.debug("Initializing filter " + getClass());
	}
}
