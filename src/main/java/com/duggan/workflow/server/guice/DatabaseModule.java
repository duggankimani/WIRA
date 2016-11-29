package com.duggan.workflow.server.guice;

import org.jbpm.executor.impl.ExecutorFactory;
import org.jbpm.executor.impl.ExecutorTransactionManagementService;

import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProviderImpl;
import com.google.inject.AbstractModule;
import com.google.inject.persist.jpa.JpaPersistModule;

public class DatabaseModule extends AbstractModule {

	@Override
	protected void configure(){
		DBTrxProviderImpl.init();

		bind(UserTransactionProvider.class);
		bind(TransactionService.class);
		
		install(new JpaPersistModule("org.jbpm.persistence.jpa"));

		// DB Class
		requestStaticInjection(DB.class);
		
		bind(JPAInitializer.class).asEagerSingleton();
		bind(ExecutorTransactionManagementService.class).asEagerSingleton();
		requestStaticInjection(ExecutorFactory.class);
		
	}
}
