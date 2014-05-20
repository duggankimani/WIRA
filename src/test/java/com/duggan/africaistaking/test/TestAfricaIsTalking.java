package com.duggan.africaistaking.test;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.duggan.workflow.server.sms.AfricasTalkingGateway;

public class TestAfricaIsTalking {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main1(String[] args) throws Exception {
		String username="dkimani";
		String apiKey="4f91e819ea8defb7d14111c51b46769a26856fde6a15cc85545701ca71c38026";
		String to="+254721239821";
		//String to="0729472421";
		String message = "This is my first test";
		AfricasTalkingGateway gateway = new AfricasTalkingGateway(username, apiKey);
		JSONArray array = gateway.sendMessage(to, message);
		System.out.println(array.toString());
		
		JSONObject object = array.getJSONObject(0);
		
		
		String status = object.getString("status");
		String number = object.getString("number");
		String messageId=object.getString("messageId");
		String cost=object.getString("cost");
		
		System.out.println(status);
	}
	
	public static void main(String[] args) throws JSONException {
		JSONArray array= new JSONArray("[{\"status\":\"Success\",\"number\":\"+254721239821\",\"messageId\":\"ATSid_dfcb613cf5d118ee4c37dc2daee665c3\",\"cost\":\"KES 1.00\"}]");
		
		JSONObject obj = array.getJSONObject(0);
		System.err.println(obj.getString("status")+" : "+obj.getString("number"));
		//[{"status":"Success","number":"+254721239821","messageId":"ATSid_c2d2d282b76109ffb62e135abe3f38b1","cost":"KES 1.00"}]
		//[{"status":"Invalid Phone Number","number":"0729472421","messageId":"None","cost":"0"}]
	}

}
