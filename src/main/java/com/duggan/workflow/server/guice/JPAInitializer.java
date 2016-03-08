package com.duggan.workflow.server.guice;

import org.jbpm.executor.ExecutorModule;

import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.google.inject.Inject;
import com.google.inject.persist.PersistService;

public class JPAInitializer {

	@Inject
	public JPAInitializer(PersistService service) {
		service.start();
		//Init
		JBPMHelper.get();
		
		//Init Async Service
		ExecutorModule.getInstance();
	}
}
