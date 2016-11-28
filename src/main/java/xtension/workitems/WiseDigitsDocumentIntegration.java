package xtension.workitems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;

import com.duggan.workflow.server.rest.model.Request;
import com.duggan.workflow.server.rest.service.impl.OutgoingRequestImpl;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.DocumentLine;
import com.duggan.workflow.shared.model.Value;

public class WiseDigitsDocumentIntegration implements WorkItemHandler {

	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		Object targetUrl= workItem.getParameter("url");
		Object document = workItem.getParameter("document");
		//document full
		
		Request request = new Request();
		HashMap<String, Object> context = new HashMap<String, Object>();
		context.put("url", targetUrl);
		context.put("document", document);
		request.setContext(context);
		
		new OutgoingRequestImpl().executePostCall(getUrlEncoding((Document)document));
	}
	
	private String getUrlEncoding(Document document) {
		
		HashMap<String, Value> values= document.getValues();
		
		StringBuffer buffer = new StringBuffer("?action=Save");
		for(Value value: values.values()){
			String key = value.getKey();
			String valueAsString = value.getValue()==null? "": value.getValue().toString();
			
			if(value.getKey().equals("subject")){
				key = "documentNo";
			}
			
			buffer.append("&"+key+"="+valueAsString);			
		}
		

		HashMap<String, ArrayList<DocumentLine>> linesMap = document.getDetails();
	
		buffer.append("&array(");
		if(linesMap.values()!=null && !linesMap.values().isEmpty()){

			Set<String> keyset = linesMap.keySet();
			
			for(String key: keyset){
				List<DocumentLine> lst = linesMap.get(key);
				
				for(DocumentLine line: lst){
					buffer.append("array(");
					for(Value value: line.getValues().values()){
						String valueAsString = value.getValue()==null? "": value.getValue().toString();
												
						buffer.append("'"+value.getKey()+"'=>\""+valueAsString+"\",");
					}
					buffer.replace(buffer.length()-1, buffer.length(), "");
					buffer.append("),");
				}
				
				buffer.replace(buffer.length()-1, buffer.length(), "");
				
			}
			
		}
		buffer.append(")");
		
		return buffer.toString();
	}


	@Override
	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {

	}

}
