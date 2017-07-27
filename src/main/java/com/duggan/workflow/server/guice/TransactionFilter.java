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

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

@Singleton
public class TransactionFilter implements Filter {

	private Provider<EntityManager> emProvider;

	Logger logger = Logger.getLogger(TransactionFilter.class);
	
	@Inject
	public TransactionFilter(Provider<EntityManager> emProvider) {
		this.emProvider = emProvider;
	}
	
	@Override
	public void init(FilterConfig arg0) throws ServletException {
		
	}

	public void doFilter(final ServletRequest servletRequest,
			final ServletResponse servletResponse, final FilterChain filterChain)
			throws IOException, ServletException {

		EntityManager em = emProvider.get();
		final EntityTransaction txn = em.getTransaction();
	    txn.begin();

		try {
			
			filterChain.doFilter(servletRequest, servletResponse);
			txn.commit();
		}catch(Exception e){
			logger.error("TransactionFilter caught exception: Attempting transaction rollback : "+e.getMessage());
			try{
				txn.rollback();
			}catch(Exception x){}
			
			((HttpServletResponse)servletResponse).setStatus(500);
			throw e;
			
		}
	}

	@Override
	public void destroy() {
		
	}

}
