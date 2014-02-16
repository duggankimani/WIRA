package com.duggan.workflow.server.actionhandlers;

import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Status;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.exception.ConstraintViolationException;

import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.helper.error.ErrorLogDaoHelper;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.server.helper.session.SessionHelper;
import com.duggan.workflow.shared.exceptions.InvalidSubjectExeption;
import com.duggan.workflow.shared.requests.BaseRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
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
public abstract class BaseActionHandler<A extends BaseRequest<B>, B extends BaseResponse>
		implements ActionHandler<A, B> {

	private static Logger log = Logger.getLogger(BaseActionHandler.class);

	@Inject	Provider<HttpServletRequest> request;
	
	@Override
	public final B execute(A action, ExecutionContext execContext)
			throws ActionException {
		@SuppressWarnings("unchecked")
		B result = (B) action.createDefaultActionResponse();		
		
		log.debug(action.getRequestCode()+": Executing command " + action.getClass().getName());

		if(SessionHelper.getHttpRequest()!=null){
			//embedded call -- needed when executing multiple commands in one call
			//not usable when working with servlets
			execute(action, result, execContext);
			return result;
		}
				
		SessionHelper.setHttpRequest(request.get());
	
		boolean hasError= false;
		Throwable throwable=null;
		
		try {
			if(!DB.hasActiveTrx()){
				
				DB.beginTransaction();
			}
			
			execute(action, result, execContext);
			
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
		
		postExecute((BaseResponse) result);
		
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
			BaseResponse baseResult = (BaseResponse)result;
			baseResult.setErrorCode(1);
			baseResult.setErrorId(errorId);
			
			if(throwable.getMessage()==null){
				baseResult.setErrorMessage("An error occurred during processing of your request");
			}else{
				log.error("Throwable : "+throwable.getMessage());
				if(throwable instanceof ConstraintViolationException){
					baseResult.setErrorMessage("[400] A database error occurred");
				}else if(throwable instanceof PersistenceException){
					baseResult.setErrorMessage("[500] A database error occurred and has been logged");
				}else if (throwable instanceof InvalidSubjectExeption){
					InvalidSubjectExeption e = (InvalidSubjectExeption)throwable;
					baseResult.setErrorMessage(e.getMessage());
				}else{
					throwable.printStackTrace();
					//baseResult.setErrorMessage("[600] "+throwable.getMessage());
				}
				
			}
		}

	}

	public abstract void execute(A action, BaseResponse actionResult,
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
	private void postExecute(BaseResponse result) {
		//dispose knowledge sessions
		JBPMHelper.clearRequestData();
	}
}
