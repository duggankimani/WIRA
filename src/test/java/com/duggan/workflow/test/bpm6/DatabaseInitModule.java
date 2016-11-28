package com.duggan.workflow.test.bpm6;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.persist.PersistService;

public class DatabaseInitModule extends AbstractModule {
	
	@Override
	protected void configure() {
		bind(JPAInitializer.class).asEagerSingleton();
	}
	
	@Singleton
	public static class JPAInitializer {
 
		@Inject 
		public JPAInitializer(PersistService service) {
			service.start();
		}
		
	}
	
}