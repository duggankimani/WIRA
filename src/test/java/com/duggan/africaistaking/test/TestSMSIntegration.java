package com.duggan.africaistaking.test;

import org.junit.Test;

import com.duggan.workflow.server.sms.SMSIntegration;

public class TestSMSIntegration {

	@Test
	public void sendMessage(){
		SMSIntegration.sendSMS("0704660993", "Testing sms!!");
	}
}
