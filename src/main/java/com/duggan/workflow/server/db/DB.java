package com.duggan.workflow.server.db;

import java.sql.Connection;

import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.apache.onami.persist.EntityManagerProvider;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.internal.SessionImpl;
import org.jbpm.bpmn2.xml.TaskHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.duggan.workflow.server.actionhandlers.AbstractActionHandler;
import com.duggan.workflow.server.dao.AttachmentDaoImpl;
import com.duggan.workflow.server.dao.CatalogDaoImpl;
import com.duggan.workflow.server.dao.CommentDaoImpl;
import com.duggan.workflow.server.dao.DSConfigDaoImpl;
import com.duggan.workflow.server.dao.DashboardDaoImpl;
import com.duggan.workflow.server.dao.DocumentDaoImpl;
import com.duggan.workflow.server.dao.ErrorDaoImpl;
import com.duggan.workflow.server.dao.FormDaoImpl;
import com.duggan.workflow.server.dao.NotificationDaoImpl;
import com.duggan.workflow.server.dao.OutputDocumentDao;
import com.duggan.workflow.server.dao.ProcessDaoImpl;
import com.duggan.workflow.server.dao.SettingsDaoImpl;
import com.duggan.workflow.server.dao.UserGroupDaoImpl;
import com.duggan.workflow.server.guice.UserTransactionProvider;
import com.duggan.workflow.server.guice.WiraPU;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * <p>
 * This class provides utility methods for beginning/committing & rolling back
 * user transactions
 * 
 * <p>
 * Further it provides utility methods for retrieving
 * {@link EntityManagerFactory} and the {@link EntityManager}
 * 
 * <P>
 * Whenever an entity manager is requested, a corresponding
 * {@link UserTransaction} has to have been started/ began
 * 
 * <p>
 * A problem scenario that arises from this is one where {@link JBPMHelper}
 * which initializes an {@link Environment} variable and {@link TaskHandler}
 * using a {@link EntityManagerFactory} generates an {@link EntityManager}
 * without a {@link UserTransaction} - since the UserTransaction is application
 * managed. In this case, an exception is thrown with a 'no active transaction'
 * message.
 * 
 * <p>
 * To mitigate the above error, a {@link UserTransaction} transaction will be
 * started for every request and committed at the end of the request -see
 * {@link AbstractActionHandler}
 * 
 * @author duggan
 *
 */
// Statically Injected
public class DB {

	private static Logger log = LoggerFactory.getLogger(DB.class);

	private static ThreadLocal<DaoFactory> daoFactory = new ThreadLocal<>();

	private static ThreadLocal<JDBCConnection> jdbcConnectionBot = new ThreadLocal<>();

	@WiraPU
	@Inject
	private static EntityManagerProvider emProvider;

	@WiraPU
	@Inject
	private static Provider<EntityManagerFactory> emfProvider;

	@Inject
	static UserTransactionProvider userTransactionProvider;

	private DB() {
	}

	public static EntityManager getEntityManager() {
		EntityManager em = emProvider.get();

		Session session = (Session) em.getDelegate();
		SessionImpl sessionImpl = (SessionImpl) session;
		try {
			
			/**
			 * A hack to fix LOB loading error : Cannot load LOB in autocommit mode - AttachmentDaoImpl
			 */
			boolean autoCommit = sessionImpl.connection().getAutoCommit();
			if(autoCommit){
				sessionImpl.connection().setAutoCommit(false);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return em;
	}

	/**
	 * This must be called before XA transaction is started
	 * 
	 * @return
	 */
	public static EntityManagerFactory getEntityManagerFactory() {
		return emfProvider.get();
	}

	public static void closeSession() {
		try {
			getJDBCBot().dispose();
			jdbcConnectionBot.set(null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		clearSession();

		closeFactory();

	}

	/**
	 * Close the single hibernate em instance.
	 *
	 * @throws HibernateException
	 */
	public static void clearSession() {
	}

	/**
	 * Begin a {@link UserTransaction}
	 * 
	 * <p>
	 * This is called whenever a new entity manager is requested
	 */
	public static void beginTransaction() {
		try {
			// getUserTrx().begin();
		} catch (Exception e) {

		}
		// getImpl().beginTransaction();
	}

	/**
	 * This method commits a {@link UserTransaction}
	 * <p>
	 * A transaction is always generated whenever an entity manager is requested
	 */
	public static void commitTransaction() {
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
				log.warn("Rolling Back Trx with status " + status);
				// getUserTrx().rollback();
			} else {
				log.debug("Commiting Back Trx with status " + status);
				// getUserTrx().commit();
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		// getImpl().
	}

	/**
	 * Rollback a {@link UserTransaction}
	 */
	public static void rollback() {
		try {
			// getUserTrx().rollback();
		} catch (Exception e) {

		}
	}

	public static UserTransaction getUserTrx() throws NamingException {
		return userTransactionProvider.get();
	}

	private static DaoFactory factory() {

		DaoFactory factory = daoFactory.get();

		if (factory == null) {
			factory = new DaoFactory();
			daoFactory.set(factory);
		}

		return factory;
	}

	public static DocumentDaoImpl getDocumentDao() {
		return factory().getDocumentDao();
	}

	public static ErrorDaoImpl getErrorDao() {

		return factory().getErrorDao();
	}

	public static NotificationDaoImpl getNotificationDao() {

		return factory().getNotificationDao();
	}

	public static CommentDaoImpl getCommentDao() {
		return factory().getCommentDao();
	}

	public static ProcessDaoImpl getProcessDao() {
		return factory().getProcessDao();
	}

	public static UserGroupDaoImpl getUserGroupDao() {
		return factory().getUserGroupDaoImpl();
	}

	public static FormDaoImpl getFormDao() {
		return factory().getFormDaoImpl();
	}

	private static void closeFactory() {
		if (daoFactory.get() == null)
			return;

		daoFactory.set(null);
	}

	public static boolean hasActiveTrx() throws SystemException,
			NamingException {
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

	public static AttachmentDaoImpl getAttachmentDao() {

		return factory().getAttachmentDaoImpl();
	}

	public static DSConfigDaoImpl getDSConfigDao() {

		return factory().getDSConfigDaoImpl();
	}

	public static Connection getConnection(String connectionName) {

		JDBCConnection bot = getJDBCBot();
		Connection conn = bot.getConnection(connectionName);
		assert conn != null;

		return conn;

	}

	private static JDBCConnection getJDBCBot() {

		JDBCConnection connection = jdbcConnectionBot.get();

		if (connection == null) {
			synchronized (jdbcConnectionBot) {
				if ((connection = jdbcConnectionBot.get()) == null) {
					connection = new JDBCConnection();
					jdbcConnectionBot.set(connection);
				}
			}
		}
		return connection;
	}

	public static DashboardDaoImpl getDashboardDao() {
		return factory().getDashboardDaoImpl();
	}

	public static SettingsDaoImpl getSettingsDao() {
		return factory().getSettingsDaoImpl();
	}

	public static OutputDocumentDao getOutputDocDao() {
		return factory().getOuputDocDaoImpl();
	}

	public static CatalogDaoImpl getCatalogDao() {
		return factory().getCatalogDaoImp();
	}

	public static String getTrxStatus() {
		try {
			return getUserTrx().getStatus() + "";
		} catch (Exception e) {
		}

		return null;
	}
}
