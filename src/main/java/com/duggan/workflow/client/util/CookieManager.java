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
	
	public static void setSessionValue(String name, String value){
		Cookies.setCookie(name, value);
	}
	
	public static String getSessionValue(String name){
		return getSessionValue(name, null);
	}
	
	public static String getSessionValue(String name, String valueIfNull){
		String value = Cookies.getCookie(name);
		if(value==null){
			return valueIfNull;
		}
		
		return value;
	}

	public static boolean isCurrentUserAdmin() {
		String value = CookieManager.getSessionValue(Definitions.ISADMINSESSION);
		return value!=null && value.equals("Y");
	}
}
