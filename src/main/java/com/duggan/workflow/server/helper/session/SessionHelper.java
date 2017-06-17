package com.duggan.workflow.server.helper.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.wira.commons.shared.models.HTUser;

/**
 * A utility class for retrieval and use of session variables & the HTTPSession
 * 
 * @author duggan
 *
 */
@Singleton
public class SessionHelper {

	@Inject
	static Provider<HttpServletRequest> request;
	
	@Inject
	static Provider<HttpServletResponse> response;


	private static Logger log = LoggerFactory.getLogger(SessionHelper.class);

	/**
	 * 
	 * @return User This is the currently logged in user
	 */
	public static HTUser getCurrentUser() {
		Subject subject = SecurityUtils.getSubject();
		
		if(!(subject.isAuthenticated() || subject.isRemembered())){
			return null;
		}
		
		PrincipalCollection principals = subject.getPrincipals();
		HTUser dto = (HTUser) principals.iterator().next();
		
		return dto;
	}

	public static String getRemoteIP() {
		return getHttpRequest() == null ? "Not Defined" : getHttpRequest()
				.getRemoteAddr();
	}

	public static String getApplicationPath() {
		HttpServletRequest request = getHttpRequest();
		if (request != null) {
			if(request.getRequestURL()==null){
				return null;
			}
			String requestURL = request.getRequestURL().toString();
			String servletPath = request.getServletPath();
			String pathInfo = request.getPathInfo();

			log.debug("# RequestURL = " + requestURL);
			log.debug("# ServletPath = " + servletPath);
			log.debug("# Path Info = " + pathInfo);
			if (pathInfo != null) {
				requestURL = requestURL.replace(pathInfo, "");
			}
			log.debug("# Remove Path Info = " + requestURL);
			requestURL = requestURL.replace(servletPath, "");
			log.debug("# Replace ServletPath = " + requestURL);

			return requestURL;
		}

		return null;
	}

	public static String generateDocUrl(String docRefId){
		HttpServletRequest request = getHttpRequest();
		if(request!=null){
			String requestURL = request.getRequestURL().toString();
			String servletPath = request.getServletPath();
			String pathInfo = request.getPathInfo();
			
			log.debug("# RequestURL = "+requestURL);
			log.debug("# ServletPath = "+servletPath);
			log.debug("# Path Info = "+pathInfo);
			if(pathInfo!=null){
				requestURL = requestURL.replace(pathInfo, "");
			}
			log.debug("# Remove Path Info = "+requestURL);				
			requestURL = requestURL.replace(servletPath, "/#/search/"+docRefId);
			log.debug("# Replace ServletPath = "+requestURL);
			
			return requestURL;
		}
		return "#";
	}

	public static HttpServletRequest getHttpRequest() {
		return request.get();
	}
	
	public static String getContextPath() {
		HttpSession session = request.get().getSession(false);
		String contextPath = (session).getServletContext().getContextPath();

		if (!contextPath.isEmpty() && !contextPath.equals("/")) {
			contextPath = (contextPath.startsWith("/") ? "" : "/")
					+ contextPath + "/";
		} else {
			contextPath = "/";
		}
		return contextPath;
	}

	public static HttpServletResponse getHttpResponse() {
		return response.get();
	}
}
