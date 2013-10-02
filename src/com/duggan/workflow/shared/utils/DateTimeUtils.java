package com.duggan.workflow.shared.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.Duration;
import org.joda.time.Interval;

public class DateTimeUtils {

	public static String datepattern="dd/MM/yyyy";
	public static String createdpattern="dd/MM/yyyy HH:mm";

	public static final SimpleDateFormat CREATEDFORMAT = new SimpleDateFormat(createdpattern);
	public static final SimpleDateFormat DATEFORMAT = new SimpleDateFormat(datepattern);
	
	public static String getTimeDifferenceAsString(Date createdDate){

		if(createdDate==null){
			return "";
		}
	
		Date today =new Date(); 
		long now = today.getTime();
		long created = createdDate.getTime();
		
		Interval interval = new Interval(created,now);
		
		Duration duration = interval.toDuration();
		duration.getStandardSeconds();
		duration.getStandardDays();
		duration.getStandardMinutes();
		duration.getStandardHours();
		
		StringBuffer buff = new StringBuffer();
		
		if(duration.getStandardDays()>1L){
			return DATEFORMAT.format(createdDate);
		}
		
		if(duration.getStandardDays()==1L){			
			return "Yesterday";
		}
				
		if(duration.getStandardHours()>0){
			long hrs = duration.getStandardHours();
			buff.append(hrs+" "+((hrs)==1? "hr":"hrs"));
		}
		
		if(duration.getStandardMinutes()>0 && buff.length()==0){
			long mins = duration.getStandardMinutes();
			buff.append(mins+" "+((mins)==1? "min":"mins"));
		}
		
		if(buff.length()==0){
			long secs = duration.getStandardSeconds();
			buff.append(secs+" "+(secs==1? "sec":"secs"));
		}
		
		buff.append(" ago");
		return buff.toString();
	
	}
}
