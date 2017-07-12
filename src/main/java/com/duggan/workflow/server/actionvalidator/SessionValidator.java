package com.duggan.workflow.server.actionvalidator;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.duggan.workflow.server.ServerConstants;
import com.duggan.workflow.shared.exceptions.InvalidSessionException;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.gwtplatform.dispatch.rpc.server.actionvalidator.ActionValidator;
import com.gwtplatform.dispatch.rpc.shared.Action;
import com.gwtplatform.dispatch.rpc.shared.Result;
import com.gwtplatform.dispatch.shared.ActionException;

/**
 * Validate current Session
 * @author duggan
 *
 */
public class SessionValidator implements ActionValidator {

	@Inject Provider<HttpServletRequest> request;
	
	@Override
	public boolean isValid(Action<? extends Result> action)
			throws ActionException {
		
		if(!action.isSecured()){
			return true;
		}
		
		HttpSession session = request.get().getSession(false);
		
		if(session==null){
			throw new InvalidSessionException("No valid session found[1]");
		}
		
		String sessionId = session.getId();
		
		Cookie[] cookies = request.get().getCookies();
//		for(Cookie c: cookies){
//			if(c.getName().equals(ServerConstants.AUTHENTICATIONCOOKIE)){
//				if(sessionId.equals(c.getValue())){
//					return true;
//				}
//			}
//		}
		
		return true;
		//session.invalidate();
		
		//throw new InvalidSessionException("No valid session found[3]");
	}

}
