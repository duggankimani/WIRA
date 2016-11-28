package org.jbpm.executor.api;

import org.jbpm.executor.callback.SendEmailCallback;
import org.jbpm.executor.commands.PrintOutCommand;
import org.jbpm.executor.commands.SendMailCommand;

public enum CommandCodes implements CommandCode{
	
	PrintOutCmd(PrintOutCommand.class),
	SendEmailCommand(SendMailCommand.class),
	SendEmailCallback(SendEmailCallback.class);
	
	Class<?> handlerClass;
	
	private CommandCodes(Class<?> handler){
		this.handlerClass = handler;
	}

	public Class<?> getHandlerClass() {
		return handlerClass;
	}
}
