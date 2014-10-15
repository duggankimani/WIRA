package com.duggan.workflow.dates;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestDateUtils {

	public static void main(String[] args) {
		
		String date =new SimpleDateFormat("dd/MM/yyyy").format(new Date());
		
		System.err.println(date);
	}
}
