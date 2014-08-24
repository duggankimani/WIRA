package xtension.workitems;

import java.util.HashMap;

import org.drools.process.instance.WorkItemHandler;
import org.drools.runtime.process.WorkItem;
import org.drools.runtime.process.WorkItemManager;

import com.duggan.workflow.server.dao.helper.OutputDocumentDaoHelper;
import com.duggan.workflow.server.export.HTMLToPDFConvertor;
import com.duggan.workflow.shared.model.Document;

public class GenerateOutputDocWorkItemHandler implements WorkItemHandler {

	@Override
	public void executeWorkItem(WorkItem workItem,
			WorkItemManager manager) {
		String path=(String)workItem.getParameter("PATH");
		Document document = (Document)workItem.getParameter("document");
		String templateName = (String)workItem.getParameter("TemplateName");
		String htmlTemplate = OutputDocumentDaoHelper.getHTMLTemplate(templateName);
		if(templateName==null){
			throw new RuntimeException("Output Document Generation Failed: Template '"+templateName+"' Not found. Kindly confirm that this template exists.");
		}
		
		HTMLToPDFConvertor convertor = new HTMLToPDFConvertor();
		try{
			convertor.convert(document, htmlTemplate);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		
		
		
		manager.completeWorkItem(workItem.getId(), new HashMap<String,Object>());
	}
	
	@Override
	public void abortWorkItem(WorkItem workItem,
			WorkItemManager manager) {
		manager.abortWorkItem(workItem.getId());
	}
}
