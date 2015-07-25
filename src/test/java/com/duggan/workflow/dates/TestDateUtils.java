package com.duggan.workflow.dates;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

public class TestDateUtils {

	public static void main(String[] args) {
		
		String date =new SimpleDateFormat("dd/MM/yyyy").format(new Date());
		
		System.err.println(DateUtils.addDays(new Date(), -30));
	}
}
