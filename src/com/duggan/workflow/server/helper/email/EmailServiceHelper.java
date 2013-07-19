package com.duggan.workflow.server.helper.email;

public class EmailServiceHelper {

	public static void sendEmail(String from, String to, String subject, String message){
		System.err.println("Sending mail: from "+from+", to "+to+" subject "+subject);
	}
}
