package com.duggan.workflow.server.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Registers new Daemon Threads
 * 
 * @author duggan
 *
 */
public class InitializerServlet extends HttpServlet {

	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String x= req.getContentType();
		assert x.equals("application/json");
		
		resp.setContentType("application/json");
		resp.getWriter().print("");
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void init() throws ServletException {		
		super.init();
		//ExecutorModule.getInstance().getExecutorServiceEntryPoint().init();
	}
	
	@Override
	public void destroy() {
		//ExecutorModule.getInstance().getExecutorServiceEntryPoint().destroy();
		super.destroy();
	}
}
