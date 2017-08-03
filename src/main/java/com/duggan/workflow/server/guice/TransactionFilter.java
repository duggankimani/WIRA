package com.duggan.workflow.server.guice;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.log4j.Logger;

import com.duggan.workflow.server.db.DB;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

@Singleton
public class TransactionFilter implements Filter {

	Logger logger = Logger.getLogger(TransactionFilter.class);
	
	@Override
	public void init(FilterConfig arg0) throws ServletException {
		
	}

	public void doFilter(final ServletRequest servletRequest,
			final ServletResponse servletResponse, final FilterChain filterChain)
			throws IOException, ServletException {

		try {
			DB.beginTransaction();
			logger.trace("Begun Trx for "+getClass().getCanonicalName()+" - Active="+DB.hasActiveTrx());
			filterChain.doFilter(servletRequest, servletResponse);
			DB.commitTransaction();
			logger.trace("Committed Trx for "+getClass().getCanonicalName()+" - Active="+DB.hasActiveTrx());
		}catch(Exception e){
			logger.error("TransactionFilter caught exception: Attempting transaction rollback : "+e.getMessage());
			try{
				DB.rollback();
			}catch(Exception x){}
			
			((HttpServletResponse)servletResponse).setStatus(500);
			
			if(e instanceof RuntimeException) {
				throw (RuntimeException)e;
			}else {
				throw new RuntimeException(e);
			}
			
			
		}
	}

	@Override
	public void destroy() {
		
	}

}
