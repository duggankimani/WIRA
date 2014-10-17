package com.duggan.workflow.server.dao.helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.duggan.workflow.client.util.ENV;
import com.duggan.workflow.server.dao.AttachmentDaoImpl;
import com.duggan.workflow.server.dao.OutputDocumentDao;
import com.duggan.workflow.server.dao.model.ADOutputDoc;
import com.duggan.workflow.server.dao.model.LocalAttachment;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.export.HTMLToPDFConvertor;
import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.DoubleValue;
import com.duggan.workflow.shared.model.HTSummary;
import com.duggan.workflow.shared.model.LongValue;
import com.duggan.workflow.shared.model.OutputDocument;
import com.duggan.workflow.shared.model.StringValue;
import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.model.form.Field;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.itextpdf.text.DocumentException;

public class OutputDocumentDaoHelper {

	public static OutputDocument saveOutputDoc(OutputDocument doc){
		ADOutputDoc output = get(doc);
		OutputDocumentDao dao = DB.getOutputDocDao();
		dao.save(output);
		
		return get(output);
	}

	private static OutputDocument get(ADOutputDoc output) {
		
		OutputDocument document = new OutputDocument();
		document.setCode(output.getCode());
		document.setDescription(output.getDescription());
		document.setId(output.getId());
		document.setName(output.getName());
		document.setPath(output.getPath());
		if(output.getAttachment()!=null){
			LocalAttachment attachment= output.getAttachment();
			document.setAttachmentName(attachment.getName());
			document.setAttachmentId(attachment.getId());
		}
		return document;
	}

	private static ADOutputDoc get(OutputDocument doc) {
		OutputDocumentDao dao = DB.getOutputDocDao();
		
		ADOutputDoc adDoc = new ADOutputDoc();
		if(doc.getId()!=null){
			adDoc=dao.getOuputDocument(doc.getId());
		}
		adDoc.setIsActive(doc.isActive()?1:0);
		adDoc.setCode(doc.getCode());
		adDoc.setName(doc.getName());
		adDoc.setDescription(doc.getDescription());
		adDoc.setPath(doc.getPath());
		
		return adDoc;
	}

	public static List<OutputDocument> getDocuments(Long documentId) {
		OutputDocumentDao dao = DB.getOutputDocDao();
		List<OutputDocument> documents = new ArrayList<>();
		if(documentId!=null){
			documents.add(get(dao.getOuputDocument(documentId)));
		}else{
			List<ADOutputDoc> docs  =dao.getOutputDocuments();
			for(ADOutputDoc doc: docs){
				documents.add(get(doc));
			}
		}
		return documents;
	}

	public static String getHTMLTemplate(String templateName) {

		OutputDocumentDao dao = DB.getOutputDocDao();
		byte[] bites= dao.getHTMLTemplate(templateName);
		return bites==null? null: new String(bites);
	}

	public static Value generateDoc(ADOutputDoc outTemplate, Doc document) {
		String path = generatePath(outTemplate.getPath(),document);
		String name = (path==null? outTemplate.getName(): 
			path.contains("/")? path.substring(path.lastIndexOf("/")+1,path.length())
					:path); 
		
		Long documentId = null;
		
		if(document instanceof HTSummary){
			documentId = ((HTSummary)document).getDocumentRef();
		}else{
			documentId = ((Document)document).getId();
		}
		
		if(documentId==null){
			throw new NullPointerException("Cannot generate output document - Document ID not found");
		}
		
		LocalAttachment attachment = new LocalAttachment();
		try {
			String template = new String(outTemplate.getAttachment().getAttachment());
			
			byte[] pdf = new HTMLToPDFConvertor().convert(document, template);
			
			
	
			AttachmentDaoImpl dao = DB.getAttachmentDao();		
			List<LocalAttachment> attachments = dao.getAttachmentsForDocument(documentId, name);
			if(!attachments.isEmpty()){
				attachment = attachments.get(0);
			}
			
			attachment.setArchived(false);
			attachment.setContentType("application/pdf");
			attachment.setDocument(DB.getDocumentDao().getById(documentId));
			attachment.setName(name+(name.endsWith(".pdf")? "": name));
			attachment.setPath(path);
			attachment.setSize(pdf.length);
			attachment.setAttachment(pdf);
			dao.save(attachment);
			
		} catch (IOException | SAXException | ParserConfigurationException
				| FactoryConfigurationError | DocumentException e) {
			
			e.printStackTrace();
			
		}
	
		LongValue value = new LongValue(null, "_"+outTemplate.getCode(), attachment.getId());
		
		return value;
	}
	
	private static String generatePath(String codedPath, Doc doc) {
		
		if(codedPath==null || codedPath.isEmpty()){
			return null;
		}
		
		String[] elements = codedPath.split("/");
		StringBuffer buffer = new StringBuffer();
		for(String el: elements){
			String decoded= decode(el, doc);
			decoded = decoded.replace("/", "-");
			buffer.append(decoded+"/");
		}
		
		String out = buffer.toString();
		return out.substring(0, out.length()-1);
	}

	/**
	 * We are looking for String containing @@ or @# followed by any set of characters
	 *  
	 * <p>
	 * @param pathEl
	 * @return
	 */
	private static String decode(String pathEl, Doc doc) {
		
		if(!pathEl.contains("@@") && !pathEl.contains("@#")){
			return pathEl;
		}
				
		Pattern regExp = Pattern.compile("@[@#]\\w+?\\b");
		
		Matcher matcher = regExp.matcher(new String(pathEl));
		while(matcher.find()){
			String group = matcher.group();

			String fieldName = group.substring(2, group.length());
			Object fieldValue = getFieldValue(fieldName,doc);		
			pathEl = pathEl.replaceAll(group, fieldValue==null? "": fieldValue.toString());
        }
	
		return pathEl;
	}

	private static Object getFieldValue(String fieldName, Doc doc) {
		
		Value value = doc.getValues().get(fieldName);
		if(value==null){
			if(fieldName.equals("subject")){
				return doc.getSubject();
			}else if(fieldName.equals("documentId")){
				return doc.getId();
			}
		}
		
		return value==null? null: value.getValue();
	}
}
