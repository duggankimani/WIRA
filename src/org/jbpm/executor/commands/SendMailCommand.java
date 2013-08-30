package org.jbpm.executor.commands;

import java.util.Map;

import org.jbpm.executor.api.Command;
import org.jbpm.executor.api.CommandContext;
import org.jbpm.executor.api.ExecutionResults;

import com.duggan.workflow.server.helper.email.EmailServiceHelper;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;

public class SendMailCommand implements Command{

	@Override
	public ExecutionResults execute(CommandContext ctx) throws Exception {
		
		Map<String, Object> data = ctx.getData(); 
		String subject = data.get("Subject")== null ? "": data.get("Subject").toString();
		String body = data.get("Body")==null? "": data.get("Body").toString();
		String recipients = data.get("To")==null? "": data.get("To").toString();
		
		EmailServiceHelper.sendEmail(body, subject, recipients);
		
		ExecutionResults result = new ExecutionResults();
		
		result.setData(ctx.getData());
		
		return result;
	}
}
