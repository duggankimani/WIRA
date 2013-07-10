package com.duggan.workflow.client.ui.util;

import com.google.gwt.i18n.client.DateTimeFormat;

/**
 * 
 * @author duggan
 *
 */
public class DateUtils {
	static String datepattern="dd/MM/yyyy";
	static String createdpattern="dd/MM/yyyy HH:mm";
			
	public static final DateTimeFormat CREATEDFORMAT = DateTimeFormat.getFormat(createdpattern);
	public static final DateTimeFormat DATEFORMAT = DateTimeFormat.getFormat(datepattern);
}
