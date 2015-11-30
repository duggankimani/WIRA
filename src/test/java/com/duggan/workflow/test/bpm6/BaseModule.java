package com.duggan.workflow.test.bpm6;

import com.duggan.workflow.server.db.DBTrxProviderImpl;
import com.duggan.workflow.server.guice.DatabaseModule;
import com.duggan.workflow.server.guice.ServerModule;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.gwtplatform.dispatch.rpc.shared.DispatchService;

public class BaseModule extends AbstractModule {


	@Override
	protected void configure() {
		// get the bootstrapping Properties file
		DBTrxProviderImpl.init();
		install(new DatabaseModule());
		install(new DatabaseInitModule());
		install(new ServerModule());
		bind(DispatchService.class).to(StandardDispatchService.class).in(
				Singleton.class);
	}

}
