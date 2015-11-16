package com.duggan.workflow.server.guice;

import org.apache.onami.persist.PersistenceFilter;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;

public class GuiceServletConfig extends GuiceServletContextListener {

	@Override
	protected Injector getInjector() {
		return Guice.createInjector(new ServerModule(), new DatabaseModule(),trxMgtModule,
				new DispatchServletModule());
	}	

	/**
	 *  Enable Transaction Management through this filter
	 */
	ServletModule trxMgtModule = new ServletModule() {
		@Override
		protected void configureServlets() {
			filter("/*").through(PersistenceFilter.class);
		}
	};
	
	
	
	
	
	

	//Previous Mechanism 11/11/2015 - Installing new Initialization mechanism as Guice Modules
	
//	@Override
//	public void contextInitialized(ServletContextEvent servletContextEvent) {
//		super.contextInitialized(servletContextEvent);
//		DBTrxProviderImpl.init();
//		JBPMHelper.get();
//	}
//
//	@Override
//	public void contextDestroyed(ServletContextEvent servletContextEvent) {
//		super.contextDestroyed(servletContextEvent);
//		DBTrxProviderImpl.close();
//		try {
//			LoginHelper.get().close();
//		} catch (Exception e) {
//		}
//
//	}
}
