package com.duggan.workflow.server.security.realm;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public abstract class BaseServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected Logger logger = Logger.getLogger(getClass());

	
	protected void initRequest(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
			executeRequest(req, resp);
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
