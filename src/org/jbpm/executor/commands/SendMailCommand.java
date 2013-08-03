package org.jbpm.executor.commands;

import org.jbpm.executor.api.Command;
import org.jbpm.executor.api.CommandContext;
import org.jbpm.executor.api.ExecutionResults;

import com.duggan.workflow.server.helper.jbpm.JBPMHelper;

public class SendMailCommand implements Command{

	@Override
	public ExecutionResults execute(CommandContext ctx) throws Exception {
		
		JBPMHelper.get().getSession().startProcess("sendemail",ctx.getData());
		
		ExecutionResults result = new ExecutionResults();
		result.setData(ctx.getData());
		
		return result;
	}
}
