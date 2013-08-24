package com.duggan.workflow.server.servlets.upload;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.duggan.workflow.server.dao.model.LocalAttachment;
import com.duggan.workflow.server.db.DB;

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

		resp.setContentType(attachment.getContentType());
		resp.setHeader("Content-disposition", "attachment;filename=\""
				+ attachment.getName());
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
