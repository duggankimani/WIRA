package com.duggan.workflow.server.actionhandlers;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.helper.session.SessionHelper;
import com.duggan.workflow.shared.requests.BaseRequest;
import com.duggan.workflow.shared.responses.BaseResult;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

/**
 * This is a super class for all requests handlers.
 * It is meant to help with management of transactions, and error handling
 * 
 * 
 * @author duggan
 * 
 * @param <A>
 * @param <B>
 */
public abstract class BaseActionHandler<A extends BaseRequest<B>, B extends BaseResult>
		implements ActionHandler<A, B> {

	private static Logger log = LoggerFactory
			.getLogger(BaseActionHandler.class);

	@Inject	Provider<HttpServletRequest> request;
	
	@Override
	public final B execute(A action, ExecutionContext execContext)
			throws ActionException {
		SessionHelper.setHttpRequest(request.get());
				
		log.info("Executing command " + action.getClass().getName());
		
		B result = (B) action.createDefaultActionResponse();

		try {
			if(!DB.hasActiveTrx()){
				
				DB.beginTransaction();
			}
			
			result = execute(action, result, execContext);
			DB.commitTransaction();
		} catch (RuntimeException e) {
			e.printStackTrace();
			DB.rollback();
			throw e;
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e.getMessage(),e); 
		}finally {
			DB.closeSession();
			SessionHelper.afterRequest();
		}

		postExecute((BaseResult) result);

		return result;

	}

	public abstract B execute(A action, BaseResult actionResult,
			ExecutionContext execContext) throws ActionException;

	@Override
	public void undo(A action, B actionResult, ExecutionContext arg2)
			throws ActionException {

	}

	/**
	 * Check Messages/ Data Status/ Errors etc
	 * 
	 * 
	 * @param result
	 */
	private void postExecute(BaseResult result) {

	}
}
