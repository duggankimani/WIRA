package com.duggan.workflow.server.guice;

import javax.servlet.ServletContextEvent;

import org.apache.shiro.SecurityUtils;

import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBImpl;
import com.duggan.workflow.server.db.DBTrxProviderImpl;
import com.duggan.workflow.server.db.DBUtil;
import com.duggan.workflow.server.helper.auth.LoginHelper;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.server.helper.session.SessionHelper;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

public class GuiceServletConfig extends GuiceServletContextListener {

	@Override
	protected Injector getInjector() {
		Injector injector = Guice
				.createInjector(new ServerModule(), new BootstrapServletModule(),
				new AbstractModule() {
					@Override
					protected void configure() {
						requestInjection(DBImpl.class);
						requestStaticInjection(SessionHelper.class);
						requestStaticInjection(DB.class);
						requestStaticInjection(Utils.class);
					}
				});
		
		
		org.apache.shiro.mgt.SecurityManager securityManager = injector
				.getInstance(org.apache.shiro.mgt.SecurityManager.class);
		SecurityUtils.setSecurityManager(securityManager); 
		return injector;
	}
	
	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		DBTrxProviderImpl.init();
		super.contextInitialized(servletContextEvent);
		//JBPMHelper.get();
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		// TODO Auto-generated method stub
		super.contextDestroyed(servletContextEvent);
		
		//JBPMHelper.destroy();
		DBTrxProviderImpl.close();
		try{
			//close ldap connection
			LoginHelper.get().close();
		}catch(Exception e){}
		
	}
}
