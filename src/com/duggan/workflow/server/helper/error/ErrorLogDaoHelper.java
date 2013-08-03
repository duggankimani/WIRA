package com.duggan.workflow.server.helper.error;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.duggan.workflow.server.dao.ErrorDaoImpl;
import com.duggan.workflow.server.dao.model.ErrorLog;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.helper.session.SessionHelper;

/**
 * This class persists all errors produced into the database
 * 
 * @author duggan
 *
 */
public class ErrorLogDaoHelper {

	public static Long saveLog(Throwable e, String actionType){
		return saveLog(e.getMessage(),getStackTrace(e));
	}
	
	/**
	 * This is a utility method that prepares the Log object ({@link ErrorLog}) to be saved into
	 * the db 
	 * <p>
	 * @param msg This the exception message
	 * @param stackTrace This is the stacktrace message
	 * @return logId This is the log id of the log saved
	 */
	private static Long saveLog(String msg, String stackTrace){
		
		ErrorDaoImpl dao = DB.getErrorDao();
		
		String ip=null;
		String agent=null;
		
		if(SessionHelper.getHttpRequest()!=null){
			HttpServletRequest request = SessionHelper.getHttpRequest();
			ip = request.getRemoteAddr();
			agent = request.getHeader("user-agent");				
		}
		
		ErrorLog log = new ErrorLog(msg, stackTrace);
		log.setCreated(new Date());
		log.setUpdated(new Date());

		if(SessionHelper.getCurrentUser()!=null){
			log.setUpdatedBy(SessionHelper.getCurrentUser().getId());
			log.setCreatedBy(SessionHelper.getCurrentUser().getId());
		}
		log.setAgent(agent);
		log.setRemoteAddress(ip);		
		
		Long logId =  dao.saveError(log);	
		
		return logId;
	}
	
	/**
	 * This is a utility method to retrieve Errors
	 * from the database 
	 * 
	 * @param logId This is the logId of the ErrorLog to be retrieved
	 * @return ErrorLog - This is the ErrorLog retrieved
	 */
	public static ErrorLog retrieveError(Long logId){
		ErrorDaoImpl dao = DB.getErrorDao();
		
		ErrorLog log = dao.retrieveError(logId);
		
		return log;
	}
	
	/**
	 * 
	 * @param ex This is a utility method to convert StackTrace Elements to String
	 * 
	 * @return String - This the String representation of the StackTrace
	 */
	private static String getStackTrace(Throwable ex) {

		StringBuffer trace = new StringBuffer();
		
		for(StackTraceElement elem: ex.getStackTrace()){
			trace.append(elem.toString()+"\r\n");
		}
		
		return trace.toString();
	}

}
