package com.duggan.workflow.server.servlets.upload;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.xml.sax.SAXException;

import com.duggan.workflow.server.dao.AttachmentDaoImpl;
import com.duggan.workflow.server.dao.helper.DocumentDaoHelper;
import com.duggan.workflow.server.dao.helper.FormDaoHelper;
import com.duggan.workflow.server.dao.helper.OutputDocumentDaoHelper;
import com.duggan.workflow.server.dao.model.ADForm;
import com.duggan.workflow.server.dao.model.LocalAttachment;
import com.duggan.workflow.server.dao.model.ProcessDefModel;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.export.HTMLToPDFConvertor;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.server.security.BaseServlet;
import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.settings.SETTINGNAME;
import com.itextpdf.text.DocumentException;

public class GetReport extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		initRequest(req, resp);
	}
	
	
	protected void executeRequest(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String action = req.getParameter("ACTION");
		log.debug("GetReport Action = "+action);

		if (action == null) {
			action = "GETATTACHMENT";
		}

		if(action.equals("GETDOCUMENTPROCESS")){
			//String docId = req.getParameter("did");
			String docRefId = req.getParameter("docRefId");
			if(docRefId==null){
				throw new IllegalStateException("DocumentId must not be null for action "+action);
			}
			//Long documentId = new Long(docId);
			
//			DocumentModel model = DB.getDocumentDao().findByRefId(docRefId, DocumentModel.class);
			Document model = DocumentDaoHelper.getDocJson(docRefId);
			assert model!=null;
			
			ProcessDefModel processDefnition = DB.getProcessDao().getProcessDef(model.getProcessId());
			
			if(processDefnition==null)
				return;
			
			assert processDefnition!=null;
			
			List<LocalAttachment> attachments = DB.getAttachmentDao().getAttachmentsForProcessDef(processDefnition, true);
			
			if(attachments.size()==0){
				return;
			}
			
			processAttachmentRequest(resp, attachments.get(0));
		}
		
		if (action.equals("GETATTACHMENT")) {
			processAttachmentRequest(req, resp);
		}
		
		if(action.equals("EXPORTFORM")){
			processExportFormRequest(req , resp);
		}
		
		if(action.equals("GetUser")){
			processUserImage(req, resp);
		}
		
		if(action.equals("GetLogo")){
			processSettingsImage(req, resp);
		}
		
		if(action.equalsIgnoreCase("GENERATEOUTPUT")){
			processOutputDoc(req,resp);
		}
		
		if(action.equalsIgnoreCase("PROCESSMAP")){
			processBPMProcessMap(req, resp);
		}

	}

	private void processBPMProcessMap(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		String pid = req.getParameter("pid");
		String did = req.getParameter("did");
		
		Long processInstanceId = null;
		
		if(pid!=null){
			processInstanceId = new Long(pid);
		}else if(did!=null){
			processInstanceId = DB.getDocumentDao().getProcessInstanceIdByDocumentId(new Long(did));
		}
		
		assert processInstanceId!=null;
		
		InputStream is = JBPMHelper.get().getProcessMap(processInstanceId);
		if(is==null){
			return;
		}
		
		byte[] data = IOUtils.toByteArray(is);
		IOUtils.closeQuietly(is);
		
		processAttachmentRequest(resp, data,"pid"+processInstanceId+".png");
	}

	private void processOutputDoc(HttpServletRequest req,
			HttpServletResponse resp) {
		String outdoc= req.getParameter("template");
		String name = req.getParameter("name");
		String doc = req.getParameter("doc");
		String path = req.getParameter("path");
		
		assert outdoc!=null && doc!=null;
		
		byte[] pdf;
		
		try {
			
			String template = OutputDocumentDaoHelper.getHTMLTemplate(outdoc);
			Long documentId = new Long(doc);
			Doc document  = DocumentDaoHelper.getDocument(documentId);
			pdf = new HTMLToPDFConvertor().convert(document, template);
			
			LocalAttachment attachment = new LocalAttachment();
	
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
			return;
		}
		
		processAttachmentRequest(resp, pdf, name);
	}

	private void processSettingsImage(HttpServletRequest req,
			HttpServletResponse resp) {
		
		String settingName = req.getParameter("settingName");
		log.debug("Logging- SettingName "+settingName);
		assert settingName!=null;
		
		String widthPx = req.getParameter("width");
		String heightPx = req.getParameter("height");
		
		if(widthPx!=null && heightPx==null){
			heightPx=widthPx;
		}
		
		Double width=null;
		Double height=null;
		
		if(widthPx!=null && widthPx.matches("[0-9]+(\\.[0-9][0-9]?)?")){
			width = new Double(widthPx);
			height = new Double(heightPx);
		}
		
		LocalAttachment attachment = DB.getAttachmentDao().getSettingImage(SETTINGNAME.valueOf(settingName));
		
		if(attachment==null){
			log.debug("No Attachment Found for Setting: ["+settingName+"]");
			return;
		}
		
		log.debug("Attachment found for setting: ["+settingName+"], FileName = "+attachment.getName());
		
		byte[] bites = attachment.getAttachment();
		
		if(width!=null){
			ImageUtils.resizeImage(resp, bites, width.intValue(), height.intValue());
		}else{
			ImageUtils.resizeImage(resp, bites);
		}
	}

	private void processUserImage(HttpServletRequest req,
			HttpServletResponse resp){		
		String userId = req.getParameter("userId");
		assert userId!=null;
		
		String widthPx = req.getParameter("width");
		String heightPx = req.getParameter("height");
		
		if(widthPx!=null && heightPx==null){
			heightPx=widthPx;
		}
		
		Double width=null;
		Double height=null;
		
		if(widthPx!=null && widthPx.matches("[0-9]+(\\.[0-9][0-9]?)?")){
			width = new Double(widthPx);
			height = new Double(heightPx);
		}
		
		LocalAttachment attachment = DB.getAttachmentDao().getUserImage(userId);
		
		if(attachment==null)
			return;
		
		byte[] bites = attachment.getAttachment();
		
		if(width!=null){
			ImageUtils.resizeImage(resp, bites, width.intValue(), height.intValue());
		}else{
			ImageUtils.resizeImage(resp, bites);
		}
		
	}

	private void processExportFormRequest(HttpServletRequest req,
			HttpServletResponse resp) {
		String param1 = req.getParameter("formId");
		assert param1!=null;
		
		Long formId  = Long.parseLong(param1);
		ADForm form = DB.getFormDao().getForm(formId);
		
		String name = form.getCaption();
		if(name==null){
			name=form.getName();
		}
		
		if(name==null){
			name="Untitled"+formId;
		}
		
		name=name+".xml";
		
		String xml = FormDaoHelper.exportForm(form);
		
		processAttachmentRequest(resp,xml.getBytes() , name);
		
	}

	private void processAttachmentRequest(HttpServletRequest req,
			HttpServletResponse resp) {
		String id = req.getParameter("attachmentId");
		if (id == null)
			return;

		LocalAttachment attachment = DB.getAttachmentDao().getAttachmentById(
				Long.parseLong(id));
		
		processAttachmentRequest(resp, attachment);
	}
	

	private void processAttachmentRequest(HttpServletResponse resp, LocalAttachment attachment) {

		resp.setContentType(attachment.getContentType());
		processAttachmentRequest(resp, attachment.getAttachment(), attachment.getName());
	}
	
	private void processAttachmentRequest(HttpServletResponse resp, byte[] data, String name ){
		if(name.endsWith("png") || name.endsWith("jpg") || name.endsWith("html") || name.endsWith("htm") 
				|| name.endsWith("svg") || name.endsWith("pdf")){
			//displayed automatically
			resp.setHeader("Content-disposition", "inline;filename=\""
					+ name);
		}else{
			resp.setHeader("Content-disposition", "attachment;filename=\""
					+ name);
		}
		
		resp.setContentLength(data.length);
		writeOut(resp, data);

	}

	
}
