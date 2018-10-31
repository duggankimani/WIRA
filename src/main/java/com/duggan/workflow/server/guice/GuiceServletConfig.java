package com.duggan.workflow.server.guice;

import javax.servlet.ServletContextEvent;

import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProviderImpl;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.server.helper.jbpm.ProcessMigrationHelper;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

public class GuiceServletConfig extends GuiceServletContextListener {

	@Override
	protected Injector getInjector() {
		return Guice.createInjector(new ServerModule(), new DispatchServletModule());
	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		// TODO Auto-generated method stub
		super.contextInitialized(servletContextEvent);
		DBTrxProviderImpl.init();
		JBPMHelper.get();

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(5);
					DB.beginTransaction();
					ProcessMigrationHelper.init();
					DB.commitTransaction();
				} catch (Exception e) {
				} finally {
					DB.closeSession();
				}
			}
		}).start();
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		// TODO Auto-generated method stub
		super.contextDestroyed(servletContextEvent);

		// JBPMHelper.destroy();
		DBTrxProviderImpl.close();

	}
}
