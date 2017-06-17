package com.duggan.workflow.server.security.realm.google;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.servlet.ServletException;

import com.duggan.workflow.server.servlets.AbstractFileServlet;

public class OauthServlet extends AbstractFileServlet{

	@Override
	public void init() throws ServletException {
		super.init();
		
		try{
			Files.createDirectories(Paths.get(BASE_PATH, "System/oauth"), getPermissions());
		}catch(IOException e){
			throw new RuntimeException(e);
		}
		
	}
	
	public static String getBASEPATH(){
		return BASE_PATH+"/System/oauth";
	}
}
