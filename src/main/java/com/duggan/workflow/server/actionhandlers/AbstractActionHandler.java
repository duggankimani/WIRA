package com.duggan.workflow.server.actionhandlers;

import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;

import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.helper.error.ErrorLogDaoHelper;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.server.helper.session.SessionHelper;
import com.duggan.workflow.shared.exceptions.InvalidSessionException;
import com.duggan.workflow.shared.exceptions.InvalidSubjectExeption;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.rpc.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.models.ErrorCodes;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

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
public abstract class AbstractActionHandler<A extends BaseRequest<B>, B extends BaseResponse>
		implements ActionHandler<A, B> {

	Logger log = Logger.getLogger(this.getClass());

	@Inject	Provider<HttpServletRequest> request;
	@Inject	Provider<HttpServletResponse> response;
	
	@Override
	public final B execute(A action, ExecutionContext execContext)
			throws ActionException {
		@SuppressWarnings("unchecked")
		B result = (B) action.createDefaultActionResponse();
		log.debug(action.getRequestCode()+": Executing command " + action.getClass().getName());
		if(action.isEmbedded()){
			/*Embedded calls are calls executed in one command
			 * to execute another on the server side. As such, a transaction and
			 * all relevant session information have been initialized. 
			 * Initializing these again causes a deadline			 
			*/
			execute(action, result, execContext);
			return result;
		}
		
		
		
		//NEW SERVER REQUEST
		if(SessionHelper.getHttpRequest()!=null){
			//embedded call -- needed when executing multiple commands in one call
			//not usable when working with servlets
			execute(action, result, execContext);
			return result;
		}
				
		SessionHelper.setHttpRequest(request.get());
		SessionHelper.setHttpResponse(response.get());
	
		boolean hasError= false;
		Throwable throwable=null;
		
		try {
			if(!DB.hasActiveTrx()){
				
				DB.beginTransaction();
			}
			
			execute(action, result, execContext);
			
			DB.commitTransaction();
		} catch (Exception e) {	
			log.error(getClass().getCanonicalName()+": "+action.getRequestCode()+" says Cannot commit trx due to error: "+e.getMessage());
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
		
		if(hasError)
			throwable = getFirstThrowableWithMessage(throwable);
		
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
			baseResult.setErrorCode(ErrorCodes.SERVER_ERROR.ordinal());
			baseResult.setErrorId(errorId);
			
			String message = ExceptionUtils.getRootCauseMessage(throwable);
			
			if(message==null){
				baseResult.setErrorMessage("An error occurred during processing of your request");
			}else{
				log.error("Throwable : "+throwable.getMessage());
				throwable = getBaseCause(throwable);
				if(throwable instanceof ConstraintViolationException){
					baseResult.setErrorMessage("[100] A database error occurred");
					baseResult.setErrorCode(ErrorCodes.DB_CONSTRAINT_ERROR.ordinal());
				}else if(throwable instanceof PersistenceException){
					baseResult.setErrorMessage("[101] A database error occurred and has been logged");
					baseResult.setErrorCode(ErrorCodes.DB_PERSISTENCE_ERROR.ordinal());
				}else if (throwable instanceof InvalidSubjectExeption){
					baseResult.setErrorMessage("[105] Invalid Case Number");
					InvalidSubjectExeption e = (InvalidSubjectExeption)throwable;
					baseResult.setErrorCode(ErrorCodes.INVALID_CASENO.ordinal());
					baseResult.setErrorMessage(e.getMessage());
				}else if(throwable instanceof InvalidSessionException){
					baseResult.setErrorMessage("[403] No session found");
					baseResult.setErrorCode(ErrorCodes.INVALID_SESSION.ordinal());
					baseResult.setErrorMessage(throwable.getMessage());
				}else{
					throwable.printStackTrace();
					if(message!=null && !message.isEmpty()){
						baseResult.setErrorMessage("We could not complete your request due to an error: <b>"+message+"</b>");
					}else{
						baseResult.setErrorMessage("We could not complete your request due to an error. Please try again later.");	
					}
				}
				
			}
		}

	}
	
	public Throwable getBaseCause(Throwable caught){
		
		if(caught.getCause()!=null && caught.getCause().getMessage()!=null && !caught.getCause().getMessage().isEmpty()){
			return getBaseCause(caught.getCause());
		}
		
		return caught;
	}

	private Throwable getFirstThrowableWithMessage(Throwable throwable) {
		if(throwable.getMessage()==null || throwable.getMessage().isEmpty()){
			if(throwable.getCause()!=null){
				return getFirstThrowableWithMessage(throwable.getCause());
			}
		}
		return throwable;
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
