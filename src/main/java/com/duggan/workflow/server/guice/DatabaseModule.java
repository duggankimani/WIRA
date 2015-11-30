package com.duggan.workflow.server.guice;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.onami.persist.PersistenceModule;

import com.duggan.workflow.server.db.DB;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class DatabaseModule extends PersistenceModule {

	@Override
	protected void configurePersistence() {
		// Entity Manager Factory
		bindContainerManagedPersistenceUnit(provideEntityManagerFactory())
				.annotatedWith(WiraPU.class).useGlobalTransactionProvidedBy(
						UserTransactionProvider.class);

		// DB Class
		requestStaticInjection(DB.class);
	}

	/**
	 * DATA ACCESS INITIALIZATION: EntityManagerFactory, EntityManager,
	 * UserTransaction
	 */

	private EntityManagerFactory emf;
	private static final ThreadLocal<EntityManager> ENTITY_MANAGER_CACHE = new ThreadLocal<EntityManager>();

	@Provides
	@WiraPU
	@Singleton
	public EntityManagerFactory provideEntityManagerFactory() {
		if (emf == null) {
			emf = Persistence
					.createEntityManagerFactory("org.jbpm.persistence.jpa");
		}

		return emf;
	}

	// @Provides
	// @WiraPU
	// public EntityManager provideEntityManager(
	// @WiraPU EntityManagerFactory entityManagerFactory) {
	//
	// EntityManager entityManager = ENTITY_MANAGER_CACHE.get();
	// if (entityManager == null) {
	// ENTITY_MANAGER_CACHE.set(entityManager = entityManagerFactory
	// .createEntityManager());
	// }
	// return entityManager;
	// }
}
