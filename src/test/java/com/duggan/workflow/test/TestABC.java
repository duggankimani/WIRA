package com.duggan.workflow.test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.junit.Test;

public class TestABC {

	@Test
	public void comment() throws UnsupportedEncodingException{
	
		System.out.println(URLEncoder.encode(",", "UTF-8"));
		System.out.println(URLDecoder.decode("&#44;", "UTF-8"));
	}
}
