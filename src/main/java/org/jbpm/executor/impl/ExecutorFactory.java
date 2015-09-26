package org.jbpm.executor.impl;

import org.jbpm.executor.api.Executor;
import org.jbpm.executor.api.ExecutorQueryService;
import org.jbpm.executor.api.ExecutorRequestAdminService;

public class ExecutorFactory {

	private static Executor executor;
	private static ExecutorQueryService queryService;
	private static ExecutorRequestAdminService adminService;

	public static Executor getExecutor() {
		if (executor == null) {
			synchronized (ExecutorFactory.class) {
				if (executor == null) {
					executor = new ExecutorImpl();
					executor.init();
				}
			}
		}

		return executor;
	}

	public static ExecutorQueryService getExecutorQueryService() {
		queryService = new ExecutorQueryServiceImpl();
		return queryService;
	}

	public static ExecutorRequestAdminService getExecutorRequestAdminService() {
		if (adminService == null) {
			synchronized (ExecutorFactory.class) {
				if (adminService == null) {
					adminService = new ExecutorRequestAdminServiceImpl();
				}
			}
		}
		return adminService;
	}
	
	public static ExecutorRunnable getExecutorRunnable(){
		return new ExecutorRunnable();
	}
}
