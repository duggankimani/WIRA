package com.duggan.workflow.test.dao;

import com.duggan.workflow.server.guice.DatabaseModule;
import com.google.inject.AbstractModule;

public class BaseModule extends AbstractModule {

	@Override
	protected void configure() {
		//install(new ServerModule());
		install(new DatabaseModule());
		install(new PersistenceInitModule());
		//install(new DispatchServletModule());
	}
}
