package com.duggan.workflow.test;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;

public class TestUrl {

	/**
	 * @param args
	 * @throws URISyntaxException 
	 * @throws MalformedURLException 
	 */
	public static void main(String[] args) {
		//space before
		String url= " http://localhost:8030/Paybill_IPN/index.php/paybill/updateDetails?mpesaCode=EV32VZ788&idNo=3421441&clientCode=PB/02555";
		try{
			URI uri = URI.create(url);
			
			System.err.println(URLEncoder.encode(uri.getQuery(),"UTF-8"));	
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}

}
