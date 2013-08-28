package com.duggan.workflow.server.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.jbpm.executor.ExecutorModule;

/**
 * Registers new Daemon Threads
 * 
 * @author duggan
 *
 */
public class InitializerServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void init() throws ServletException {		
		super.init();
		ExecutorModule.getInstance().getExecutorServiceEntryPoint().init();
	}
	
	@Override
	public void destroy() {
		ExecutorModule.getInstance().getExecutorServiceEntryPoint().destroy();
		super.destroy();
	}
}
