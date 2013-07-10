package com.duggan.workflow.test;

import java.util.List;

import org.aspectj.lang.annotation.After;
import org.junit.Test;

import com.duggan.workflow.client.model.TaskType;
import com.duggan.workflow.server.helper.dao.DocumentDaoHelper;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.shared.model.DocSummary;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.HTSummary;

public class TestApprovalRequest {

	Integer documentId=5;
	String userId="calcacuervo";
	
	@Test
	public void submit(){
		//Document document = DocumentDaoHelper.getDocument(documentId);
		
		//HTSummary doc = document.toTask();
		Document doc = new Document();
		doc.setSubject("Inv/Fin/100/13");
		doc.setId(5);
		doc.setDescription("Invoice for the delivery of goat milk");
		doc.setPriority(5);
		
		
		System.err.println("Document>> "+doc.getId()+" : "+doc.getSubject()+" : "+doc.getDescription());
		
		JBPMHelper.get().createApprovalRequest(doc);
		
		List<HTSummary> lst = JBPMHelper.get().getTasksForUser(userId, TaskType.APPROVALREQUESTNEW);
		
		for(HTSummary summary: lst){
			System.err.println(summary.getDocumentRef()+" : "+summary.getSubject()+" : "+summary.getDescription());
		}
		
	}
	
	@org.junit.After
	public void destroy(){
		JBPMHelper.destroy();
	}
	
}
