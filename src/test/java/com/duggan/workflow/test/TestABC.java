package com.duggan.workflow.test;

import java.util.Calendar;
import java.util.Locale;

import org.junit.Test;

public class TestABC {

	@Test
	public void comment(){
		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		
		String month= calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);
		int year= calendar.get(Calendar.YEAR);
		System.out.println("Month "+month+"/ "+year);
	
		
		
	}
}
