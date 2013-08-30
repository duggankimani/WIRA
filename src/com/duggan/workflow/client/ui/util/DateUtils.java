package com.duggan.workflow.client.ui.util;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.datepicker.client.CalendarUtil;

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
	
	public static String getTimeDifferenceAsString(Date createdDate){

		if(createdDate==null){
			return "";
		}
	
		Date today =new Date(); 
		long now = today.getTime();
		long created = createdDate.getTime();
		long diff = now -created;
		
		StringBuffer buff = new StringBuffer();
		
		long dayInMillis = 24*3600*1000;
		long hourInMillis = 3600*1000;
		long minInMillis = 60*1000;
		
		if(diff>2*dayInMillis){
			return DateUtils.DATEFORMAT.format(createdDate);
		}
		
		if(diff>dayInMillis){
			int days = CalendarUtil.getDaysBetween(createdDate, today);
			if(days==1){
				return "yesterday";
			}
			
			return days+" days ago";
		}
				
		if(!CalendarUtil.isSameDate(createdDate, new Date())){
			return "yesterday";
		}
		
		if(diff>hourInMillis){
			long hrs = diff/hourInMillis;
			buff.append(hrs+" "+((hrs)==1? "hr":"hrs"));
			diff= diff%hourInMillis;
		}
		
		if(diff>minInMillis && buff.length()==0){
			long mins = diff/minInMillis;
			buff.append(mins+" "+((mins)==1? "min":"mins"));
			diff= diff%minInMillis;
		}
		
		if(buff.length()==0){
			long secs = diff/1000;
			buff.append(secs+" "+(secs==1? "sec":"secs"));
		}
		
		buff.append(" ago");
		return buff.toString();
	
	}
}
