package com.duggan.workflow.server.db;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBImpl {

	private Logger log = LoggerFactory.getLogger(DBImpl.class);

	private EntityManagerFactory emf;

	final ThreadLocal<EntityManager> entityManagers = new ThreadLocal<EntityManager>();

	public EntityManager getEntityManager() {

		EntityManager em = entityManagers.get();

		if (em != null && !em.isOpen()) {
			em = null;
		}
		if (em == null) {

			synchronized (entityManagers) {
				if (emf == null) {
					emf = getEntityManagerFactory(); // (serviceRegistry);
				}
			}

			// beginTransaction();
			em = emf.createEntityManager();
			entityManagers.set(em);
		}

		return em;
	}

	/**
	 * This must be called before XA transaction is started
	 * 
	 * @return
	 */
	public EntityManagerFactory getEntityManagerFactory() {

		synchronized (log) {
			if (emf == null) {
				try {
					emf = Persistence
							.createEntityManagerFactory("org.jbpm.persistence.jpa");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return emf;
	}

	/**
	 * Close the single hibernate em instance.
	 *
	 * @throws HibernateException
	 */
	public void clearSession() {
		EntityManager em = (EntityManager) entityManagers.get();
		if (em == null)
			return;

		entityManagers.set(null);

		if (em != null) {
			em.close();
		}

	}

	/**
	 * Begin a {@link UserTransaction}
	 * 
	 * <p>
	 * This is called whenever a new entity manager is requested
	 */
	public void beginTransaction() {
		try {
			getUserTrx().begin();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * This method commits a {@link UserTransaction}
	 * <p>
	 * A transaction is always generated whenever an entity manager is requested
	 */
	public void commitTransaction() {
		try {
			// if(entityManagers.get()!=null)

			int status = DB.getUserTrx().getStatus();
			/*
			 * STATUS_ACTIVE 0 STATUS_COMMITTED 3 STATUS_COMMITTING 8
			 * STATUS_MARKED_ROLLBACK 1 STATUS_NO_TRANSACTION 6 STATUS_PREPARED
			 * 2 STATUS_PREPARING 7 STATUS_ROLLEDBACK 4 STATUS_ROLLING_BACK 9
			 * STATUS_UNKNOWN 5
			 */

			// JBPM engine marks transactions for rollback everytime
			// something goes wrong - it does'nt necessarily throw an exception
			if (status == 1 || status == 4 || status == 9) {
				getUserTrx().rollback();
			} else {
				getUserTrx().commit();
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * Rollback a {@link UserTransaction}
	 */
	public void rollback() {
		try {

			// if(entityManagers.get()!=null)
			getUserTrx().rollback();

		} catch (Exception e) {
			e.printStackTrace();
			// throw new RuntimeException(e);
		}
	}

	public UserTransaction getUserTrx() throws NamingException {
		Context ctx = new InitialContext();
		Object value = ctx.lookup("java:comp/UserTransaction");

		if (value == null) {
			return null;
		}

		UserTransaction userTrx = (UserTransaction) value;
		return userTrx;
	}

	public boolean isEntityManagerAvailable() {

		EntityManager em = (EntityManager) entityManagers.get();
		if (em == null)
			return false;

		return true;
	}

	public boolean hasActiveTrx() throws SystemException, NamingException {

		int status = getUserTrx().getStatus();

		boolean active = false;

		switch (status) {
		case Status.STATUS_NO_TRANSACTION:
			active = false;
			break;

		default:
			active = true;
			break;
		}

		return active;
	}

}
