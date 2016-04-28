package com.duggan.workflow.server.servlets.upload;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.xml.sax.SAXException;

import com.duggan.workflow.server.dao.helper.CatalogDaoHelper;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.helper.session.SessionHelper;
import com.itextpdf.text.DocumentException;

public class DownloadReportServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			// check session
			//check session
			SessionHelper.setHttpRequest(req);
			
			DB.beginTransaction();

			executeGet(req, resp);

			DB.commitTransaction();
		} catch (Exception e) {
			DB.rollback();
			
			resp.setContentType("text/html");
			writeOut(resp, ("<p><b>"+e.getMessage()+"</b></p>"+ExceptionUtils.getStackTrace(e)).getBytes());
			e.printStackTrace();
		} finally {
			DB.closeSession();
			SessionHelper.setHttpRequest(null);
		}
	}

	private void executeGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, SAXException, ParserConfigurationException, FactoryConfigurationError, DocumentException {
		
		String catalogRefId = req.getParameter("reportRefId");
		String docType = req.getParameter("docType");
		String name = req.getParameter("NAME");
		
		String reportName = name+"."+docType.toLowerCase();
		
		byte[] bytes = CatalogDaoHelper.printCatalogue(catalogRefId, docType);
		
		processAttachmentRequest(resp, bytes, reportName);
		
	}
	
	
	private void processAttachmentRequest(HttpServletResponse resp,
			byte[] data, String name) {
		if (name.endsWith("png") || name.endsWith("jpg")
				|| name.endsWith("html") || name.endsWith("htm")
				|| name.endsWith("svg") || name.endsWith("pdf")) {
			// displayed automatically
			resp.setHeader("Content-disposition", "inline;filename=\"" + name);
		} else {
			resp.setHeader("Content-disposition", "attachment;filename=\""
					+ name);
		}
		resp.setContentLength(data.length);
		writeOut(resp, data);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doPost(req, resp);
	}
	
	private void writeOut(HttpServletResponse resp,
			byte[] data) {
		ServletOutputStream out = null;
		try{
			out = resp.getOutputStream();
			out.write(data);
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
