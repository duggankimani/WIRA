package xtension.workitems;

import java.util.HashMap;

import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;

import com.duggan.workflow.server.db.DB;

public class FormValidationWorkItemHandler implements WorkItemHandler {

	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		String subject = workItem.getParameter("subject").toString();
		String pin = workItem.getParameter("providedpin")==null? null :workItem.getParameter("providedpin").toString();
		if(pin==null){
			throw new RuntimeException("Applicant Pin is required for application submission");
		}
		
		Number number = (Number)DB.getEntityManager().createNativeQuery("select count(*) from sms where pin=? and subject=?")
		.setParameter(1, pin)
		.setParameter(2, subject).getSingleResult();
		
		if(number.intValue()==0){
			throw new RuntimeException("Pin "+pin+" is invalid for application "+subject);
		}
		
		manager.completeWorkItem(workItem.getId(), new HashMap<String, Object>());
	}

	@Override
	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		
	}
}
