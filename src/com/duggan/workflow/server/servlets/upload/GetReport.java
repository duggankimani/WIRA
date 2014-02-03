package com.duggan.workflow.server.servlets.upload;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.duggan.workflow.server.dao.helper.ProcessDefHelper;
import com.duggan.workflow.server.dao.model.ADDocType;
import com.duggan.workflow.server.dao.model.DocumentModel;
import com.duggan.workflow.server.dao.model.LocalAttachment;
import com.duggan.workflow.server.dao.model.ProcessDefModel;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.shared.model.Attachment;
import com.duggan.workflow.shared.model.ProcessDef;

public class GetReport extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		try {
			// check session

			DB.beginTransaction();

			executeGet(req, resp);

			DB.commitTransaction();
		} catch (Exception e) {
			DB.rollback();
			e.printStackTrace();
		} finally {
			DB.closeSession();
		}

	}

	protected void executeGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String action = req.getParameter("ACTION");

		if (action == null) {
			action = "none";
		}

		if(action.equals("GETDOCUMENTPROCESS")){
			String docId = req.getParameter("did");
			if(docId==null){
				throw new IllegalStateException("DocumentId must not be null for action "+action);
			}
			Long documentId = new Long(docId);
			
			DocumentModel model = DB.getDocumentDao().getById(documentId);
			assert model!=null;
			
			ADDocType type = model.getType();
			
			if(type==null){
				return ;
			}
			
			ProcessDefModel processDefnition = type.getProcessDef();
			
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
		if(attachment.getName().endsWith("png")){
			//displayed automatically
			resp.setHeader("Content-disposition", "inline;filename=\""
					+ attachment.getName());
		}else{
			resp.setHeader("Content-disposition", "attachment;filename=\""
					+ attachment.getName());
		}
			
		
		resp.setContentLength(new Long(attachment.getSize()).intValue());
		
		writeOut(resp, attachment);

	}

	private void writeOut(HttpServletResponse resp,
			LocalAttachment attachment) {
		ServletOutputStream out = null;
		try{
			out = resp.getOutputStream();
			out.write(attachment.getAttachment());
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		
		try{
			out.close();			
		}catch(Exception e){
			throw new RuntimeException(e);
		}		
	}

}
