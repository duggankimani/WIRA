package com.duggan.workflow.server.guice;

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

import com.duggan.workflow.server.guice.UserTransactionProvider;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.persist.PersistFilter;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.Transactional;
import com.google.inject.persist.UnitOfWork;

/**
 * 
 * @author duggan
 * 
 *         <p>
 *         This class is used in place of {@link PersistFilter} for JTA Global
 *         Trx's Support.
 * 
 *         <p>
 *         NOTE: The use of JTA denies us the capability to use
 *         {@link Transactional} annotation. This annotation is only supported
 *         for RESOURCE_LOCAL persistence units. Any attempt to use this
 *         annotation generates a <i>java.lang.IllegalStateException: A JTA
 *         EntityManager cannot use getTransaction()<i>
 *
 */
@Singleton
public class TransactionFilter implements Filter {
	private final UnitOfWork unitOfWork;
	private final PersistService persistService;
	private Provider<EntityManager> emProvider;
	private UserTransactionProvider userTransactionProvider;

	Logger log = Logger.getLogger(this.getClass());

	@Inject
	public TransactionFilter(UnitOfWork unitOfWork,
			PersistService persistService, Provider<EntityManager> emProvider,
			UserTransactionProvider userTransactionProvider) {
		this.unitOfWork = unitOfWork;
		this.persistService = persistService;
		this.emProvider = emProvider;
		this.userTransactionProvider = userTransactionProvider;
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		// persistService.start();
		// Initialized By DatabaseModuleEws - Coz of Server side Threads support
	}

	public void destroy() {
		persistService.stop();
	}

	public void doFilter(final ServletRequest servletRequest,
			final ServletResponse servletResponse, final FilterChain filterChain)
			throws IOException, ServletException {

		try {
			beginTrx();
			try {
				unitOfWork.begin();
			} catch (Exception e) {
			}

			filterChain.doFilter(servletRequest, servletResponse);
		} finally {
			commitTrx();
			unitOfWork.end();
		}
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

			trxStatus = trx.getStatus() + "; em = " + em + "[Open="
					+ em.isOpen()
					// +", joinedToTrx="+em.isJoinedToTransaction()+"]"
					+ "; UserTrx=" + trx;
			log.debug(": Executing beginTrx ; TrxStatus " + trxStatus);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
