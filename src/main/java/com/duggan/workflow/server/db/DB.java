package com.duggan.workflow.server.db;

import java.sql.Connection;

import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.drools.runtime.Environment;
import org.hibernate.HibernateException;
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
import com.duggan.workflow.server.dao.OrganizationDao;
import com.duggan.workflow.server.dao.OutputDocumentDao;
import com.duggan.workflow.server.dao.PermissionDao;
import com.duggan.workflow.server.dao.ProcessDaoImpl;
import com.duggan.workflow.server.dao.SettingsDaoImpl;
import com.duggan.workflow.server.dao.UserDaoImpl;
import com.duggan.workflow.server.dao.UserSessionDao;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;

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
public class DB {

	private static Logger log = LoggerFactory.getLogger(DB.class);

	private static ThreadLocal<DaoFactory> daoFactory = new ThreadLocal<>();

	private static ThreadLocal<JDBCConnection> jdbcConnectionBot = new ThreadLocal<>();

	private static DBImpl impl = null;
	

	private DB() {
	}
	
	public static DBImpl getImpl(){
		if(impl==null){
			String className= ApplicationSettings.getInstance().getProperty("db.impl.class");
			if(className!=null){
				try {
					Class<?> implClass = Class.forName(className);
					impl = (DBImpl)implClass.newInstance();
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
					return null;
				}
			}else{
				impl = new DBImpl();
			}
			
		}
		return impl;
	}

	public static EntityManager getEntityManager() {
		return getImpl().getEntityManager();
	}

	/**
	 * This must be called before XA transaction is started
	 * 
	 * @return
	 */
	public static EntityManagerFactory getEntityManagerFactory() {
		return getImpl().getEntityManagerFactory();
	}

	public static void closeSession() {
		try {
			getJDBCBot().dispose();
			jdbcConnectionBot.set(null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			if (!getImpl().isEntityManagerAvailable()) {
				return;
			}
		} catch (Exception e) {

			try {
				rollback();
			} catch (Exception ex) {
			}

			throw new RuntimeException(e);
		} finally {
			clearSession();

			closeFactory();
		}

	}

	/**
	 * Close the single hibernate em instance.
	 *
	 * @throws HibernateException
	 */
	public static void clearSession() {
		getImpl().clearSession();
	}

	/**
	 * Begin a {@link UserTransaction}
	 * 
	 * <p>
	 * This is called whenever a new entity manager is requested
	 */
	public static void beginTransaction() {
		getImpl().beginTransaction();
	}

	/**
	 * This method commits a {@link UserTransaction}
	 * <p>
	 * A transaction is always generated whenever an entity manager is requested
	 */
	public static void commitTransaction() {
		getImpl().commitTransaction();
	}

	/**
	 * Rollback a {@link UserTransaction}
	 */
	public static void rollback() {
		getImpl().rollback();
	}
	
	public static void setRollbackOnly() {
		try {
			getUserTrx().setRollbackOnly();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	public static UserTransaction getUserTrx() throws NamingException {
		return getImpl().getUserTrx();
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
		return factory().getDocumentDao(getEntityManager());
	}

	public static ErrorDaoImpl getErrorDao() {

		return factory().getErrorDao(getEntityManager());
	}

	public static NotificationDaoImpl getNotificationDao() {

		return factory().getNotificationDao(getEntityManager());
	}

	public static CommentDaoImpl getCommentDao() {
		return factory().getCommentDao(getEntityManager());
	}

	public static ProcessDaoImpl getProcessDao() {
		return factory().getProcessDao(getEntityManager());
	}

	public static UserDaoImpl getUserDao() {
		return factory().getUserGroupDaoImpl(getEntityManager());
	}

	public static FormDaoImpl getFormDao() {
		return factory().getFormDaoImpl(getEntityManager());
	}

	private static void closeFactory() {
		if (daoFactory.get() == null)
			return;

		daoFactory.set(null);
	}

	public static boolean hasActiveTrx() throws SystemException,
			NamingException {
		return getImpl().hasActiveTrx();
	}

	public static AttachmentDaoImpl getAttachmentDao() {

		return factory().getAttachmentDaoImpl(getEntityManager());
	}

	public static DSConfigDaoImpl getDSConfigDao() {

		return factory().getDSConfigDaoImpl(getEntityManager());
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
		return factory().getDashboardDaoImpl(getEntityManager());
	}

	public static SettingsDaoImpl getSettingsDao() {
		return factory().getSettingsDaoImpl(getEntityManager());
	}

	public static OutputDocumentDao getOutputDocDao() {
		return factory().getOuputDocDaoImpl(getEntityManager());
	}

	public static CatalogDaoImpl getCatalogDao() {
		return factory().getCatalogDaoImp(getEntityManager());
	}
	
	public static OrganizationDao getOrganizationDao() {
		return factory().getOrganizationDao(getEntityManager());
	}
	
	public static PermissionDao getPermissionDao() {
		return factory().getPermissionDao(getEntityManager());
	}
	
	public static UserSessionDao getUserSessionDao(){
		return factory().getUserSessionDao(getEntityManager());
	}
}
