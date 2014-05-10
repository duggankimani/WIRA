package com.duggan.workflow.server.sms;
import java.io.*;
import java.net.*;
import java.util.*;
import org.json.*;

public class AfricasTalkingGateway
{
    private String _username;
    private String _apiKey;
    
    private static final String SMSURLString   = "https://api.africastalking.com/version1/messaging";
    private static final String VoiceURLString = "https://voice.africastalking.com/call";
    
    public AfricasTalkingGateway(String username_, String apiKey_)
    {
	_username = username_;
	_apiKey   = apiKey_;
    }
    
    public JSONArray sendMessage(String to_, String message_) throws Exception
    {
	HashMap<String, String> data = new HashMap<String, String>();
	data.put("username", _username);
	data.put("to", to_);
	data.put("message", message_);
	
	JSONObject response       = sendPOSTRequest(data, SMSURLString);
	JSONObject smsMessageData = response.getJSONObject("SMSMessageData");
	JSONArray  recipients     = smsMessageData.getJSONArray("Recipients");
	if ( recipients.length() == 0 ) {
	    throw new Exception(smsMessageData.getString("Message"));
	}
	return recipients;
    }
    
    public JSONArray sendMessage(String to_, String message_, String from_, int bulkSMSMode_) throws Exception
    {
	/*
	 * The optional from_ parameter should be populated with the value of a shortcode or alphanumeric that is 
	 * registered with us 
	 * The optional  will be used by the Mobile Service Provider to determine who gets billed for a 
	 * message sent using a Mobile-Terminated ShortCode. The default value is 1 (which means that 
	 * you, the sender, gets charged). This parameter will be ignored for messages sent using 
	 * alphanumerics or Mobile-Originated shortcodes.
	 */
	
	HashMap<String, String> data = new HashMap<String, String>();
	data.put("username", _username);
	data.put("to", to_);
	data.put("message", message_);
	
	if ( from_.length() > 0 ) data.put("from", from_);
	
	data.put("bulkSMSMode", Integer.toString(bulkSMSMode_));
	
	JSONObject response       = sendPOSTRequest(data, SMSURLString);
	JSONObject smsMessageData = response.getJSONObject("SMSMessageData");
	JSONArray  recipients     = smsMessageData.getJSONArray("Recipients");
	if ( recipients.length() == 0 ) {
	    throw new Exception(smsMessageData.getString("Message"));
	}
	return recipients;
    }
    
    public void call(String from_, String to_) throws Exception
    {
	HashMap<String, String> data = new HashMap<String, String>();
	data.put("username", _username);
	data.put("from", from_);
	data.put("to", to_);
	
	JSONObject response = sendPOSTRequest(data, VoiceURLString);
	if ( !response.getString("Status").equals("Success")) {
	    throw new Exception(response.getString("ErrorMessage"));
	}
    }
    
    public JSONArray fetchMessages(int lastReceivedId_) throws Exception
    {
	try {
	    
	    StringBuilder sb = new StringBuilder();
	    sb.append(lastReceivedId_);
	    
	    String urlString  = SMSURLString + "?";
	    urlString        += URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(_username, "UTF-8");
	    urlString        += "&" + URLEncoder.encode("lastReceivedId", "UTF-8") + "=" + URLEncoder.encode(sb.toString(), "UTF-8");
	    
	    URL url = new URL(urlString);
	    
	    URLConnection conn = url.openConnection();
	    conn.setRequestProperty("Accept", "application/json");
	    conn.setRequestProperty("apikey", _apiKey);
	    
	    HttpURLConnection httpConn = (HttpURLConnection)conn;
	    int responseCode           = httpConn.getResponseCode();
	    
	    BufferedReader reader;
	    if ( responseCode == 200) {
		reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
		
		String inputLine          = reader.readLine();
		JSONObject response       = new JSONObject(inputLine);
		JSONObject smsMessageData = response.getJSONObject("SMSMessageData");
		
		reader.close();
		
		return smsMessageData.getJSONArray("Messages");
	    
	    } else {
		reader = new BufferedReader(new InputStreamReader(httpConn.getErrorStream()));
		
		String inputLine          = reader.readLine();
		JSONObject response       = new JSONObject(inputLine);
		JSONObject smsMessageData = response.getJSONObject("SMSMessageData");
		
		reader.close();
		
		throw new Exception(smsMessageData.getString("Message"));
		
	    }
	    
	} catch (Exception e){
	    throw e;
	}
    }
    
    private JSONObject sendPOSTRequest(HashMap<String, String> dataMap_, String urlString_) throws Exception {
	try {
	    String data = new String();
	    
	    Iterator it = dataMap_.entrySet().iterator();
	    while (it.hasNext()) {
		Map.Entry pairs = (Map.Entry)it.next();
		data += URLEncoder.encode(pairs.getKey().toString(), "UTF-8");
		data += "=" + URLEncoder.encode(pairs.getValue().toString(), "UTF-8");
		if ( it.hasNext() ) data += "&";
	    }
	    
	    URL url = new URL(urlString_);
	    URLConnection conn = url.openConnection();
	    conn.setRequestProperty("Accept", "application/json");
	    conn.setRequestProperty("apikey", _apiKey);
	    
	    conn.setDoOutput(true);
	    OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
	    writer.write(data);
	    writer.flush();
	    
	    HttpURLConnection httpConn = (HttpURLConnection)conn;
	    int responseCode           = httpConn.getResponseCode();
	    
	    BufferedReader reader;
	    if ( responseCode == 201) {
		reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
	    } else {
		reader = new BufferedReader(new InputStreamReader(httpConn.getErrorStream()));
	    }
	    
	    String inputLine  = reader.readLine();
	    reader.close();
	    
	    return new JSONObject(inputLine);
	    
	} catch (Exception e){
	    
	    throw e;
	    
	}
    }
}
