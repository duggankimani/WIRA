package org.jbpm.executor.api;

public interface CommandCode {

	public Class<?> getHandlerClass();
	public String name();
}
