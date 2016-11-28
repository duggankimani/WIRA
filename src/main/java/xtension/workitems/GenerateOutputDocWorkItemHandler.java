package xtension.workitems;

import java.util.HashMap;

import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;

import com.duggan.workflow.server.dao.helper.OutputDocumentDaoHelper;
import com.duggan.workflow.server.dao.model.LocalAttachment;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.export.HTMLToPDFConvertor;
import com.duggan.workflow.shared.model.Document;

public class GenerateOutputDocWorkItemHandler implements WorkItemHandler {

	@Override
	public void executeWorkItem(WorkItem workItem,
			WorkItemManager manager) {
		String path=workItem.getParameter("path")==null? null:
			(String)workItem.getParameter("path");
		Document document = workItem.getParameter("document")==null? null:
			(Document)workItem.getParameter("document");
		String templateName = workItem.getParameter("templateName")==null?null:
				(String)workItem.getParameter("templateName");
		String formFieldName = workItem.getParameter("formFieldName")==null? null : 
			(String) workItem.getParameter("formFieldName");
		
		checkParamNotNull(path, "Path");
		checkParamNotNull(document, "Document");
		checkParamNotNull(templateName, "TemplateName");
		String htmlTemplate = OutputDocumentDaoHelper.getHTMLTemplate(templateName);
		if(templateName==null){
			throw new RuntimeException("Output Document Generation Failed: Template '"+templateName+"' Not found. Kindly confirm that this template exists.");
		}
		
		HTMLToPDFConvertor convertor = new HTMLToPDFConvertor();
		try{
			byte[] pdf = convertor.convert(document, htmlTemplate);
			LocalAttachment attachment = new LocalAttachment(null, path, pdf);
			String contentType = "application/pdf";
			attachment.setArchived(false);
			attachment.setContentType(contentType);
			attachment.setId(null);
			attachment.setDocument(DB.getDocumentDao().getById(document.getId()));
			attachment.setName(path);
			attachment.setSize(pdf.length);
			attachment.setFieldName(formFieldName);
			DB.getDocumentDao().save(attachment);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		
		
		
		manager.completeWorkItem(workItem.getId(), new HashMap<String,Object>());
	}
	
	private void checkParamNotNull(Object variableValue, String variableName) {
		if(variableValue==null){
			throw new RuntimeException("Output Document Generation Failed: '"+variableName+"' Cannot be null");
		}
	}

	@Override
	public void abortWorkItem(WorkItem workItem,
			WorkItemManager manager) {
		manager.abortWorkItem(workItem.getId());
	}
}
