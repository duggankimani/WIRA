package com.duggan.workflow.ouputdocs;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.duggan.workflow.server.dao.helper.DocumentDaoHelper;
import com.duggan.workflow.server.dao.helper.OutputDocumentDaoHelper;
import com.duggan.workflow.server.dao.model.ADOutputDoc;
import com.duggan.workflow.server.dao.model.LocalAttachment;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProviderImpl;
import com.duggan.workflow.server.export.DocumentHTMLMapper;
import com.duggan.workflow.shared.model.Attachment;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.TreeType;

public class TestOutputDocs {
	

	@Before
	public void setup(){
		DBTrxProviderImpl.init();
		DB.beginTransaction();
	}
	
	@Test
	public void generateOutput(){
		String docRefId = "";
		Document doc = DocumentDaoHelper.getDocJson(docRefId);
		
		String outputRefId = "";
		ADOutputDoc outputTemplate = DB.getOutputDocDao().findByRefId(outputRefId, ADOutputDoc.class);
		OutputDocumentDaoHelper.generateDoc(outputTemplate, doc);
		
	}

	@Ignore
	public void getAttachments(){
//		List<Attachment> attachments = DB.getAttachmentDao().getAttachments(TreeType.PROCESSES, "kim", null);
		List<Attachment> attachments = DB.getAttachmentDao().getAttachments(TreeType.FILES, "QAOExWBRU4nbfu7E", null);
//		List<Attachment> attachments = DB.getAttachmentDao().getAttachments(TreeType.USERS, null , null);
		print(attachments, 0);
	}
	
	@Ignore
	public void retireveFolders(){

		List<Attachment> attachments = DB.getAttachmentDao().getFileTree();
//		attachments = DB.getAttachmentDao().getFileTree();
//		attachments = DB.getAttachmentDao().getFileProcessTree();
		print(attachments, 0);
	}
	
	private void print(List<Attachment> attachments, int i) {
		if(attachments==null || attachments.isEmpty()){
			return;
		}
		
		for(Attachment a: attachments){
			System.out.println(i+" >> "+a.getRefId()+"; "+a.getName()+", "+" : "+a.getProcessRefId());
			print(a.getChildren(), i+1);
		}
	}

	@Ignore
	public void generateFolders(){
		LocalAttachment attachment = OutputDocumentDaoHelper.generateFolders("Claims Processing/mdkimani@gmail.com/claim123.pdf");
		
		Assert.assertNotNull(attachment);
		Assert.assertEquals("mdkimani@gmail.com", attachment.getName());
		
		attachment = OutputDocumentDaoHelper.generateFolders("BOQ Process/Duggan Kimani/BOQ456.pdf");
		
		attachment = OutputDocumentDaoHelper.generateFolders("Procure To Pay/Duggan Kimani/Invoices/Invoice 123.pdf");
		
	}
	
	@Ignore
	public void HashMap() throws FileNotFoundException, IOException{
		long documentId = 187L;
		Document doc = DocumentDaoHelper.getDocument(documentId);
		
		DocumentHTMLMapper mapper = new DocumentHTMLMapper();
		List<String> vals=IOUtils.readLines(new FileInputStream("/home/duggan/Downloads/PO (1).html"));
		StringBuffer input = new StringBuffer();
		for(String in:vals){
			input.append(in);
		}
		String out = mapper.map(doc, input.toString());
		IOUtils.copy(new ByteArrayInputStream(out.getBytes()), new FileOutputStream("PO.html"));
		
	}
	
	@After
	public void close(){
		DB.commitTransaction();
		DB.closeSession();
	}
}
