package com.duggan.workflow.client.ui.util;

import com.google.gwt.i18n.client.NumberFormat;

public class NumberUtils {

	private static String NUMBER_PATTERN="###,###.##";
	private static String NUMBER_PATTERN_INT="###,###";
	
	public static NumberFormat NUMBERFORMAT= NumberFormat.getFormat(NUMBER_PATTERN);
	public static NumberFormat CURRENCYFORMAT= NumberFormat.getCurrencyFormat("KES");
	public static NumberFormat CURRENCYFORMATSHORT= NumberFormat.getFormat(NUMBER_PATTERN_INT);
	public static String format(Double number, int places) {
		String format = NUMBER_PATTERN_INT;
		
		NumberFormat f = NumberFormat.getFormat(format);
		if(places<1){
			return f.format(number);
		}

		format = format+".";
		for(int i=0; i< places; i++){
			format = format+"#";
		}
		
		f = NumberFormat.getFormat(format);
		return f.format(number);
	}
}
