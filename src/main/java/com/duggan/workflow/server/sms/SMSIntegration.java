package com.duggan.workflow.server.sms;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.duggan.workflow.server.dao.model.SMS;
import com.duggan.workflow.server.db.DB;

public class SMSIntegration {

	static Random random = new Random();
	static Logger logger = Logger.getLogger(SMSIntegration.class);
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static String execute(Map<String, String> values){
		String to = values.get("to");
		String message = values.get("message");
		
		if(to==null || to.isEmpty()){
			return "Failed: The recipient telephone number must be provided."
					+ " Kindly confirm the relavant fields are provided";
		}
		
		if(message==null || message.isEmpty()){
			return "Failed: SMS Message cannot be empty";
		}
		
		String pin = random.nextInt(10000)+"";
		message = message.replaceAll("\\{AUTHPIN\\}", pin);
		
		String subject = values.get("subject");
		SMS sms = new SMS(subject, to, pin);
		DB.getEntityManager().persist(sms);

		return sendSMS(to, message);
	}
	
	static Properties settings = new Properties();
	static {
		try {
			settings.load(AfricasTalkingGateway.class.getClassLoader().getResourceAsStream("general.properties"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static String sendSMS(String phoneNumber, String message) {
		return sendSMS(phoneNumber, message, null);
	}

	public static String sendSMS(String phoneNumber, String message, String from) {
		JSONObject jsonData = new JSONObject();
		String failureReason = null;
		try {
			
			String username = settings.getProperty("africastalking.username");

			String keyword = settings.getProperty("africastalking.keyword");
			
			String apiKey = settings.getProperty("africastalking.apikey");
			
			AfricasTalkingGateway gateway = new AfricasTalkingGateway(username, apiKey);
			JSONArray arr = null;
			logger.trace("Sending message with keyword>>>" + keyword + ">>> username >>>" + username + ">>>password>>>>"
					+ apiKey);
			arr = gateway.sendMessage(phoneNumber, message, keyword);

			logger.trace(arr.toString());
			for (int n = 0; n < arr.length(); n++) {
				jsonData = arr.getJSONObject(n);
			}

		} catch (Exception e) {
			try {
				jsonData.put("failureReason", "Sms failed because: " + e.getMessage());
				failureReason = jsonData.getString("failureReason");
			}catch (Exception e1) {
			}
			
			throw new RuntimeException(e);
		}
		return failureReason;
	}
}
