package com.duggan.workflow.server.guice;

import com.duggan.workflow.server.db.DBTrxProviderImpl;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;

public class GuiceServletConfig extends GuiceServletContextListener {

	@Override
	protected Injector getInjector() {
		return Guice.createInjector(new WiraServletModule(),new ServerModule(),new DatabaseModule(),
				new DispatchServletModule());
	}	
	
	public void contextDestroyed(javax.servlet.ServletContextEvent servletContextEvent) {
		DBTrxProviderImpl.close();
	};
}
