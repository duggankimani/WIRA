package com.duggan.workflow.client.ui.util;

import com.google.gwt.i18n.client.NumberFormat;

public class NumberUtils {

	private static String NUMBER_PATTERN="###,###.##";
	private static String NUMBER_PATTERN_INT="###,###";
	
	public static NumberFormat NUMBERFORMAT= NumberFormat.getFormat(NUMBER_PATTERN);
	public static NumberFormat CURRENCYFORMAT= NumberFormat.getCurrencyFormat("KES");
	public static NumberFormat CURRENCYFORMATSHORT= NumberFormat.getFormat(NUMBER_PATTERN_INT);
}
