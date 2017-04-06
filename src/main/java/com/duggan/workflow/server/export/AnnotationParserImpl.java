package com.duggan.workflow.server.export;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.model.Value;

public class AnnotationParserImpl {

	public static ArrayList<String> extractAnnotations(String operand) {
		ArrayList<String> annotations = new ArrayList<String>();
		Pattern pattern = Pattern.compile("@[@#]\\w+?\\b");
		Matcher matcher = pattern.matcher(operand);

		while (matcher.find()) {
			String group = matcher.group();
			boolean isNumber = group.startsWith("@#");
			String key = group.substring(2, group.length());
			// rtn= rtn.replace(matcher.group(), value);
			annotations.add(key);
		}

		return annotations;
	}

	public static String parseForSQL(Doc context, String sqlSelect) {
		Map<String, Value> values = context.getValues();
		return parseAndReplace(values, sqlSelect, true);
	}
	
	public static String parse(Doc context, String operand) {
		Map<String, Value> values = context.getValues();
		return parseAndReplace(values, operand,false);
	}

	private static String parseAndReplace(Map<String, Value> values,
			String operand, boolean sqlFormat) {
		Pattern pattern = Pattern.compile("@[@#]\\w+?\\b");
		String rtn = new String(operand);
		Matcher matcher = pattern.matcher(rtn);

		while (matcher.find()) {
			String group = matcher.group();
			// int start = matcher.start();
			// int end=matcher.end();
			// System.err.format("I found the text" +
			// " \"%s\" starting at " +
			// "index %d and ending at index %d.%n",
			// group,
			// start,
			// end);
			boolean isNumber = group.startsWith("@#");
			String key = group.substring(2, group.length());
			Object val = get(values, key);
			String value = val == null ? "" : getValue((Value) val, isNumber);
			
			if(!isNumber && sqlFormat){
				value = "'"+value+"'";
			}
			
			rtn = rtn.replace(matcher.group(), value);
		}

		System.out.println("RTN>> "+rtn);
		return rtn;
	}

	private static Object get(Map<String, ?> values, String key) {
		Object value = values.get(key);
		if (value == null) {
			key = key.substring(0, 1).toUpperCase()
					+ key.substring(1, key.length());
			value = values.get(key);
		}

		return value;
	}

	private static String getValue(Value value) {
		return getValue(value, false);
	}

	private static String getValue(Value value, boolean isNumber) {
		if (value != null && value.getValue() != null) {
			if (value.getValue() instanceof Date) {
				return new SimpleDateFormat("dd/MM/yyyy").format((Date) value
						.getValue());
			}

			if (isNumber && (value.getValue() instanceof Number)) {
				NumberFormat format = NumberFormat.getNumberInstance();
				String out = format.format(value.getValue());
				return out;
			}

			return value.getValue().toString();
		}

		return "";
	}

}
