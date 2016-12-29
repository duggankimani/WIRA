package com.duggan.workflow.server.servlets.upload;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.duggan.workflow.server.helper.session.SessionHelper;

public class ImportLogger {

	static final String KEY="IMPORTLOG";
	
	public static void log(String message){
		HttpSession session = SessionHelper.getHttpRequest()==null? null : SessionHelper.getHttpRequest().getSession();
		
		if(session==null){
			return;
		}
		
		List<String> importLog = new ArrayList<String>();
		Object list = session.getAttribute(ImportLogger.KEY);
		if(list!=null){
			importLog = (ArrayList)list;
		}
		
		importLog.add(message);
		session.setAttribute(ImportLogger.KEY, importLog);
	}
	
	public static void clear(){
		HttpSession session = SessionHelper.getHttpRequest()==null? null : SessionHelper.getHttpRequest().getSession();
		
		if(session==null){
			return;
		}
		session.setAttribute(ImportLogger.KEY, new ArrayList<String>());
	}

	public static ArrayList<String> get() {
		HttpSession session = SessionHelper.getHttpRequest()==null? null : SessionHelper.getHttpRequest().getSession();
		
		if(session==null){
			return new ArrayList<String>();
		}
		
		ArrayList<String> importLog = new ArrayList<String>();
		Object list = session.getAttribute(ImportLogger.KEY);
		if(list!=null){
			importLog = (ArrayList)list;
		}
		
		return importLog;
		
	}
	
}
