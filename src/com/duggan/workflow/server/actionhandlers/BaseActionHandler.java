package com.duggan.workflow.server.actionhandlers;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.helper.error.ErrorLogDaoHelper;
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

		boolean hasError= false;
		Throwable throwable=null;
		
		try {
			if(!DB.hasActiveTrx()){
				
				DB.beginTransaction();
			}
			
			execute(action, result, execContext);
			
			//DB.rollback();	
			DB.commitTransaction();
		} catch (Exception e) {	
			e.printStackTrace();
			DB.rollback();			
			hasError = true;
			throwable = e;
		}finally {
			DB.closeSession();		
			logErrors(hasError, throwable, result);
			SessionHelper.afterRequest();
		}
		
		postExecute((BaseResult) result);
		
		return result;

	}

	private void logErrors(boolean hasError, Throwable throwable, B result) {

		if(hasError){
			Long errorId=null;
			
			try{
				
				if(!DB.hasActiveTrx())
					DB.beginTransaction();
				
				errorId=ErrorLogDaoHelper.saveLog(throwable, this.getActionType().getName());
				DB.commitTransaction();
			}catch(Exception e){
				e.printStackTrace();
				try{
					DB.rollback();
				}catch(Exception ee){
					ee.printStackTrace();
				}
				
			}finally{
				DB.closeSession();				
			}
			
			//set error msg
			BaseResult baseResult = (BaseResult)result;
			baseResult.setErrorCode(1);
			baseResult.setErrorId(errorId);
			
			if(errorId!=null)
				baseResult.setErrorMessage("An error occured during processing of your request");
			else
				baseResult.setErrorMessage(throwable.getMessage());
		}

	}

	public abstract void execute(A action, BaseResult actionResult,
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
