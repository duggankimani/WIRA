package com.duggan.workflow.test.bpm6;

import org.apache.onami.persist.AllPersistenceServices;
import org.apache.onami.persist.AllUnitsOfWork;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Singleton;

public class DatabaseInitModule extends AbstractModule {
	
	@Override
	protected void configure() {
		bind(JPAInitializer.class).asEagerSingleton();
	}
	
	@Singleton
	public static class JPAInitializer {
 
		@Inject 
		public JPAInitializer(AllPersistenceServices allPersistenceServices,AllUnitsOfWork allUnitsOfWork) {
			allPersistenceServices.startAllStoppedPersistenceServices();
			allUnitsOfWork.beginAllInactiveUnitsOfWork();
		}
		
	}
	
}