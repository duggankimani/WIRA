package com.duggan.workflow.server.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.helper.session.SessionHelper;

public abstract class BaseServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected Logger log = Logger.getLogger(getClass());

	
	protected void initRequest(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			// check session
			// check session
			SessionHelper.setHttpRequest(req);

			DB.beginTransaction();

			executeRequest(req, resp);

			DB.commitTransaction();
		} catch (Exception e) {
			DB.rollback();
			resp.setContentType("text/html");
			resp.setStatus(500);
			writeOut(resp,
					("<p><b>" + e.getMessage() + "</b></p>" + ExceptionUtils
							.getStackTrace(e)).getBytes());
			e.printStackTrace();
		} finally {
			DB.closeSession();
			SessionHelper.setHttpRequest(null);
		}

	}

	protected void writeOut(HttpServletResponse resp, byte[] data) {
		ServletOutputStream out = null;
		try {
			out = resp.getOutputStream();
			out.write(data);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		try {
			out.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected abstract void executeRequest(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException;
		
}
