package org.jbpm.executor.commands;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jbpm.executor.api.Command;
import org.jbpm.executor.api.CommandContext;
import org.jbpm.executor.api.ExecutionResults;

import com.duggan.workflow.server.helper.email.EmailServiceHelper;
import com.wira.commons.shared.models.HTUser;

public class SendMailCommand implements Command{


	public static final String SUBJECT = "emailSubject";
	public static final String BODY= "Body";
	public static final String RECIPIENTS = "To";
	static Logger log = Logger.getLogger(SendMailCommand.class);
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public ExecutionResults execute(CommandContext ctx) throws Exception {
		
		Map<String, Object> data = ctx.getData(); 
		String subject = data.get(SUBJECT)== null ? "": data.get(SUBJECT).toString();
		String body = data.get(BODY)==null? "": data.get(BODY).toString();
		Object recipients = data.get(RECIPIENTS)==null? "": data.get(RECIPIENTS);
		
		if(recipients instanceof List){
			log.debug("## Recipients List<HTUser> found !!!!! ");
			HTUser initiator = data.get("initiator")==null? (HTUser)data.get("ownerId"): (HTUser)data.get("initiator");
			
			EmailServiceHelper.sendEmail(body, subject, (List<HTUser>)recipients,initiator);
		}else if(recipients instanceof String){
			log.warn("Recipients assumed to be a comma separated String of emails !!!!! >>"+recipients);
			EmailServiceHelper.sendEmail(body, subject, recipients.toString(),
					data.get("ownerId")==null? null :data.get("ownerId").toString());
		}else{
			log.warn("Recipients Type cannot be resolved!!!!! >>"+recipients);
		}
		
		ExecutionResults result = new ExecutionResults();
		
		//ctx.setData(key, value);
		result.setData(ctx.getData());
		
		return result;
	}
}
