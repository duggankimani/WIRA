package com.duggan.workflow.client.util;

import com.google.gwt.user.client.Cookies;

public class CookieManager {

	public static void setCookies(String authCookie, long authTime){
		Cookies.setCookie(Definitions.AUTHENTICATIONCOOKIE, authCookie);
		Cookies.setCookie(Definitions.AUTHENTICATIONTIMECOOKIE, authTime+"");
	}
	
	public static String getAuthCookie(){
		return Cookies.getCookie(Definitions.AUTHENTICATIONCOOKIE);
	}
	
	public static String getAuthenticatedAt(){
		return Cookies.getCookie(Definitions.AUTHENTICATIONTIMECOOKIE);
	}

	public static void clear() {
		Cookies.removeCookie(Definitions.AUTHENTICATIONCOOKIE);
		Cookies.removeCookie(Definitions.AUTHENTICATIONTIMECOOKIE);
	}
}
