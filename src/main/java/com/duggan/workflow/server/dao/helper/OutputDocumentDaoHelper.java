package com.duggan.workflow.server.dao.helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import com.duggan.workflow.server.dao.AttachmentDaoImpl;
import com.duggan.workflow.server.dao.OutputDocumentDao;
import com.duggan.workflow.server.dao.model.ADOutputDoc;
import com.duggan.workflow.server.dao.model.LocalAttachment;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.export.HTMLToPDFConvertor;
import com.duggan.workflow.shared.model.AttachmentType;
import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.HTSummary;
import com.duggan.workflow.shared.model.LongValue;
import com.duggan.workflow.shared.model.OutputDocument;
import com.duggan.workflow.shared.model.Value;
import com.itextpdf.text.DocumentException;

public class OutputDocumentDaoHelper {

	private static Logger logger = Logger.getLogger(OutputDocumentDaoHelper.class);
	
	public static OutputDocument saveOutputDoc(OutputDocument doc) {
		ADOutputDoc output = get(doc);
		OutputDocumentDao dao = DB.getOutputDocDao();
		dao.save(output);

		return get(output);
	}

	private static OutputDocument get(ADOutputDoc output) {

		OutputDocument document = new OutputDocument();
		document.setRefId(output.getRefId());
		document.setCode(output.getCode());
		document.setDescription(output.getDescription());
		document.setId(output.getId());
		document.setName(output.getName());
		document.setPath(output.getPath());
		document.setProcessRefId(output.getProcessRefId());
		if (output.getAttachment() != null) {
			LocalAttachment attachment = output.getAttachment();
			document.setAttachmentName(attachment.getName());
			document.setAttachmentId(attachment.getId());
		}
		return document;
	}

	private static ADOutputDoc get(OutputDocument doc) {
		OutputDocumentDao dao = DB.getOutputDocDao();

		ADOutputDoc adDoc = new ADOutputDoc();
		if (doc.getId() != null) {
			adDoc = dao.getOuputDocument(doc.getId());
		}
		
		if(doc.getRefId()!=null){
			adDoc = dao.findByRefId(doc.getRefId(), ADOutputDoc.class);
		}
		
		if(adDoc==null){
			adDoc = new ADOutputDoc();
		}
		
		adDoc.setIsActive(doc.isActive() ? 1 : 0);
		adDoc.setCode(doc.getCode());
		adDoc.setName(doc.getName());
		adDoc.setDescription(doc.getDescription());
		adDoc.setPath(doc.getPath());
		adDoc.setProcessRefId(doc.getProcessRefId());
		
		if(doc.getTemplate()!=null && !doc.getTemplate().isEmpty()){
			LocalAttachment attachment  = new LocalAttachment();
			if(adDoc.getAttachment()!=null){
				attachment = adDoc.getAttachment();
			}
			attachment.setOutputDoc(adDoc);
			
			attachment.setContentType("text/html");					
			attachment.setName(doc.getName().endsWith(".html")? doc.getName(): doc.getName()+".html");
			attachment.setSize(doc.getTemplate().getBytes().length);
			attachment.setDirectory(false);
			attachment.setAttachment(doc.getTemplate().getBytes());					
			AttachmentDaoImpl impl = DB.getAttachmentDao();
			impl.save(attachment);
		}

		return adDoc;
	}

	public static List<OutputDocument> getDocuments(Long documentId) {
		OutputDocumentDao dao = DB.getOutputDocDao();
		List<OutputDocument> documents = new ArrayList<>();
		if (documentId != null) {
			documents.add(get(dao.getOuputDocument(documentId)));
		} else {
			List<ADOutputDoc> docs = dao.getOutputDocuments();
			for (ADOutputDoc doc : docs) {
				documents.add(get(doc));
			}
		}
		return documents;
	}

	public static List<OutputDocument> getDocuments(String processRefId) {
		return getDocuments(processRefId, null);
	}
	
	public static List<OutputDocument> getDocuments(String processRefId, String searchTerm) {
		OutputDocumentDao dao = DB.getOutputDocDao();
		List<OutputDocument> documents = new ArrayList<>();
		List<ADOutputDoc> docs = dao.getOutputDocuments(processRefId, searchTerm);
		for (ADOutputDoc doc : docs) {
			documents.add(get(doc));
		}
		
		return documents;
	}

	public static String getHTMLTemplate(String templateName) {

		OutputDocumentDao dao = DB.getOutputDocDao();
		byte[] bites = dao.getHTMLTemplate(templateName);
		return bites == null ? null : new String(bites);
	}
	
	public static String getHTMLTemplateByRefId(String outputRefId) {

		OutputDocumentDao dao = DB.getOutputDocDao();
		byte[] bites = dao.getHTMLTemplateByOutputId(outputRefId);
		return bites == null ? null : new String(bites);
	}

	public static Value generateDoc(ADOutputDoc outTemplate, Doc document) {
		String path = generatePath(outTemplate.getPath(), document);
		LocalAttachment parent = generateFolders(path);
		String name = (path == null ? outTemplate.getName() : path
				.contains("/") ? path.substring(path.lastIndexOf("/") + 1,
				path.length()) : path);
		
		name = name + (name.endsWith(".pdf") ? "" : name+".pdf");

		String docRefId = document.getRefId();

//		if (document instanceof HTSummary) {
//			documentId = ((HTSummary) document).getDocumentRef();
//		} else {
//			documentId = ((Document) document).getId();
//		}

		if (docRefId == null) {
			throw new NullPointerException(
					"Cannot generate output document - Document ID not found");
		}

		LocalAttachment attachment = new LocalAttachment();
		try {
			String template = new String(outTemplate.getAttachment()
					.getAttachment());
			
			template = tidyTemplate(template);

			byte[] pdf = new HTMLToPDFConvertor().convert(document, template);

			AttachmentDaoImpl dao = DB.getAttachmentDao();
			List<LocalAttachment> attachments = dao.getAttachmentsForDocument(
					docRefId, name);
			if (!attachments.isEmpty()) {
				attachment = attachments.get(0);
			}

			attachment.setArchived(false);
			attachment.setContentType("application/pdf");
			//attachment.setDocument(DB.getDocumentDao().getById(documentId));
			attachment.setDocRefId(docRefId);
			attachment.setName(name);
			attachment.setType(AttachmentType.GENERATED);
			attachment.setPath(path);
			attachment.setSize(pdf.length);
			attachment.setAttachment(pdf);
			attachment.setParent(parent);
			dao.save(attachment);

		} catch (IOException | SAXException | ParserConfigurationException
				| FactoryConfigurationError | DocumentException e) {

			e.printStackTrace();
			throw new RuntimeException("Failed to generate output document '"+name+"', Cause: "+e.getMessage(),e);
		}

		LongValue value = new LongValue(null, "_" + outTemplate.getCode(),
				attachment.getId());

		return value;
	}

	/**
	 * 
	 * <p>
	 * Flying Saucer iText library expects a fully-formed html document.
	 * <p>
	 * The Front-end uses tinymce, which at the moment strips off all html/head/body tags 
	 * which could be based on the assumption that the content will end up as part of an existing
	 * html page (Still researching)
	 * <p>
	 * As a hack, we will manually add these missing elements to make the html complete.  
	 * <p>
	 * @param template
	 * @return tidy HTML
	 * 
	 * @author Duggan - 6/09/2016
	 */
	public static String tidyTemplate(String template) {
		if(template.contains("<html>") && template.contains("<body>")){
			return template;
		}
		
		return "<html><head></head><body>"+template+"</body></html>";
	}

	public static String generatePath(String codedPath, Doc doc) {

		if (codedPath == null || codedPath.isEmpty()) {
			return null;
		}

		String[] elements = codedPath.split("/");
		StringBuffer buffer = new StringBuffer();
		for (String el : elements) {
			String decoded = decode(el, doc);
			decoded = decoded.replace("/", "-");
			buffer.append(decoded + "/");
		}

		String out = buffer.toString();
		return out.substring(0, out.length() - 1);
	}
	
	/**
	 * Returns the direct parent of the file whose path is provided
	 *  
	 * @param path
	 * @return Parent folder of the file whose path is provided
	 */
	public static LocalAttachment generateFolders(String path){
		AttachmentDaoImpl dao = DB.getAttachmentDao();
		if (path == null || path.isEmpty()) {
			return null;
		}

		String[] elements = path.split("/");
		
		//The last element is the file name
		LocalAttachment parent = null;
		for(int i=0; i<elements.length-1; i++){
			String directoryName = elements[i];
			LocalAttachment child = dao.getDirectory(parent,directoryName);
			logger.info("Child {"+child+":"+directoryName+"}, Parent={"+(parent==null? "Null" : parent.getName())+"}");
			if(child==null){
				child = new LocalAttachment();
			}
			child.setName(directoryName);
			child.setDirectory(true);
			child.setParent(parent);
			dao.save(child);
			
			parent = child;
		}

		return parent;
	}

	/**
	 * We are looking for String containing @@ or @# followed by any set of
	 * characters
	 * 
	 * <p>
	 * 
	 * @param pathEl
	 * @return
	 */
	private static String decode(String pathEl, Doc doc) {

		if (!pathEl.contains("@@") && !pathEl.contains("@#")) {
			return pathEl;
		}

		Pattern regExp = Pattern.compile("@[@#]\\w+?\\b");

		Matcher matcher = regExp.matcher(new String(pathEl));
		while (matcher.find()) {
			String group = matcher.group();

			String fieldName = group.substring(2, group.length());
			Object fieldValue = getFieldValue(fieldName, doc);
			pathEl = pathEl.replaceAll(group, fieldValue == null ? ""
					: fieldValue.toString());
		}

		return pathEl;
	}

	private static Object getFieldValue(String fieldName, Doc doc) {

		Value value = doc.getValues().get(fieldName);
		if (value == null) {
			if (fieldName.equals("subject") || fieldName.equals("caseNo")) {
				return doc.getCaseNo();
			} else if (fieldName.equals("documentId")) {
				return doc.getId();
			}
		}

		return value == null ? null : value.getValue();
	}

	public static OutputDocument getDocument(String outputRefId, boolean loadTemplate) {
		
		OutputDocumentDao dao = DB.getOutputDocDao();
		ADOutputDoc outDoc = dao.findByRefId(outputRefId, ADOutputDoc.class);
		
		OutputDocument document= get(outDoc);
		
		if(loadTemplate){
			document.setTemplate(getHTMLTemplateByRefId(outputRefId));
		}
		
		return document;
	}
}
