package com.duggan.africaistaking.test;

import org.junit.Test;

import com.duggan.workflow.server.sms.SMSIntegration;

public class TestSMSIntegration {

	@Test
	public void sendMessage(){
		new SMSIntegration().send("0721239821", "Testing sms!!");
	}
}
