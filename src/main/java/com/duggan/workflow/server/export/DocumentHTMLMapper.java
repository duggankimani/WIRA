package com.duggan.workflow.server.export;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.model.DocumentLine;
import com.duggan.workflow.shared.model.Value;

public class DocumentHTMLMapper {

	private static Logger log = Logger.getLogger(DocumentHTMLMapper.class);
	
	public String map(Doc doc, String html){
		
		html = parseReplaceGridMatches(doc,html);
		html = parseAndReplace(doc.getValues(), html);
		
		return html;
	}

	private String parseReplaceGridMatches(Doc doc, String html) {
		Pattern pattern = Pattern.compile("<!--\\s*?@[<>]\\w+?\\b\\s*?-->");
		Matcher matcher = pattern.matcher(html);
		
		//Expected <!-- @>GridName --> Content <!-- @<GridName -->
		int c=0;
		int start=0;
		while(matcher.find()){
			++c;
//			System.err.format("I found the text" +
//                    " \"%s\" starting at " +
//                    "index %d and ending at index %d.%n",
//                    matcher.group(),
//                    matcher.start(),
//                    matcher.end());
			
			if(c%2==0){
				String gridRows = html.substring(start,matcher.start());
				String gridName = getGridName(matcher.group());
				log.debug("GridName = "+gridName);
				List<DocumentLine> gridVals=doc.getDetails().get(gridName);
				
				StringBuffer buff = new StringBuffer();
				if(gridVals!=null){
					for(DocumentLine line: gridVals){
						buff.append(parseAndReplaceGridRow(line, new String(gridRows)));
					}
				}
				
				html = html.replace(gridRows, buff.toString());
				html = html.replaceAll("<!--\\s*?@[<>]"+gridName+"\\b\\s*?-->", "");
				return parseReplaceGridMatches(doc, html);
			}else{
				start=matcher.end();
			}
			
        }
        
		return html;
	}

	private String parseAndReplaceGridRow(DocumentLine line, String html) {			
		return parseAndReplace(line.getValues(), html);
	}


	private String parseAndReplace(Map<String, Value> values, String html) {
		Pattern pattern = Pattern.compile("@[@#]\\w+?\\b");
		String rtn = new String(html);
		Matcher matcher = pattern.matcher(rtn);
		
		while(matcher.find()){
			String group = matcher.group();
//			int start = matcher.start();
//			int end=matcher.end();
//			System.err.format("I found the text" +
//                    " \"%s\" starting at " +
//                    "index %d and ending at index %d.%n",
//                    group,
//                    start,
//                    end);
			String value = getValue(values.get(group.substring(2, group.length())));
			rtn= rtn.replace(matcher.group(), value);
        }
        	
        return rtn;
	}

	private String getValue(Value value) {

		if(value!=null && value.getValue()!=null){
			return value.getValue().toString();
		}
		
		return "";
	}

	public String getGridName(String group) {
		Pattern pattern = Pattern.compile("@[<>]\\w+?\\b");
		Matcher matcher = pattern.matcher(group);
		String gridName=null;
		if(matcher.find()){
			log.debug("match found >> "+matcher.group());
			gridName = group.substring(matcher.start(), matcher.end());
			return gridName.substring(2,gridName.length());
		}else{
			log.debug("No match found for "+group);
			return "";
		}
		
	}
	
}
