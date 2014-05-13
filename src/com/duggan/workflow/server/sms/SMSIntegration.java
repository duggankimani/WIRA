package com.duggan.workflow.server.sms;

import java.util.Map;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

import com.duggan.workflow.server.dao.model.SMS;
import com.duggan.workflow.server.db.DB;

public class SMSIntegration {

	Random random = new Random();
	/**
	 * 
	 * @param id
	 * @return
	 */
	public String execute(Map<String, String> values){
		String to = values.get("to");
		String message = values.get("message");
		
		if(to==null || to.isEmpty()){
			return "Failed: The recipient telephone number must be provided. Kindly confirm the relavant fields are provided";
		}
		
		if(message==null || message.isEmpty()){
			return "Failed: SMS Message cannot be empty";
		}
		
		String pin = random.nextInt(10000)+"";
		message = message.replaceAll("\\{AUTHPIN\\}", pin);
		
		String subject = values.get("subject");
		SMS sms = new SMS(subject, to, pin);
		DB.getEntityManager().persist(sms);

		return send(to, message);
	}
	
	public String send(String to, String message){
		String username="dkimani";
		String apiKey="4f91e819ea8defb7d14111c51b46769a26856fde6a15cc85545701ca71c38026";
		
		//String to="+254721239821";
		AfricasTalkingGateway gateway = new AfricasTalkingGateway(username, apiKey);
		try{
			JSONArray resp = gateway.sendMessage(to, message);

			JSONObject object = resp.getJSONObject(0);
			
			String status = object.getString("status");
			String number = object.getString("number");
			String messageId=object.getString("messageId");
			String cost=object.getString("cost");
			
			if(!status.equals("Success")){
				throw new RuntimeException("SMS Failed: "+status);
			}
			System.err.println(resp);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		
		return null;
				
	}
}
