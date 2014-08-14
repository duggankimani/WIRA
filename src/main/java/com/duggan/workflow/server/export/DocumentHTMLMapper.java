package com.duggan.workflow.server.export;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.model.DocumentLine;

public class DocumentHTMLMapper {

	private static Logger log = Logger.getLogger(DocumentHTMLMapper.class);
	
	public String map(Doc doc, String html){
		
		String mappedHTMLStr = "";
		
		Pattern pattern = Pattern.compile("\\$\\{([^}]+)\\}");//pick all content in ${}
		Matcher matcher = pattern.matcher(html);
		while (matcher.find()) {
			String key = matcher.group(1);
			
			String value = null;
			
			//log.debug("SendEmailWorkItemHandler Checking Document for value for key: " + key);
			value = doc.getValues().get(key)==null ? null : 
				doc.getValues().get(key).getValue()==null? null : doc.getValues().get(key).getValue().toString();
			
			if(value==null){
				//try details
				List<DocumentLine> detailLines = doc.getDetails().get(key);
				if(detailLines!=null){
					html = mapLines(key, detailLines, html);
				}				
			}
			
			if(value==null || value.isEmpty()){
				log.warn("DocumentHTMLMapper Missing Value for key: " + key);
				value = "";
			}else{
				log.debug("DocumentHTMLMapper found key: " + key+" = "+value);
			}
			
			html = html.replace("${"+key+"}",value);
		}

		return mappedHTMLStr;
	}

	/**
	 * 
	 * @param key
	 * @param detailLines
	 * @param html
	 * @return
	 */
	private String mapLines(String key, List<DocumentLine> detailLines,
			String html) {
		
		return null;
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		String detail = "<!-- Test --> <!-- Test -->"+
				"<!-- @@podetail --> <!-- @@podetail -->"+
				"<!--@@detail-->"+
				"@#col1 <td> @#col2 </td> @@col3 <td> mkimani@gmail.com"+
				"<!-- @@detail -->";
		
		List<String> vals=IOUtils.readLines(new FileInputStream("/home/duggan/Downloads/PO (1).html"));
		StringBuffer input = new StringBuffer();
		for(String in:vals){
			input.append(in);
		}
		System.out.println(input.toString());
		test2(input.toString());
		test1(input.toString());
		
	}

	private static void test2(String detail) {
		Pattern pattern = Pattern.compile("<!--\\s*?@[<>]\\w+?\\b\\s*?-->");
		Matcher matcher = pattern.matcher(detail);
		boolean found=false;
		int start=-1;
		int end=-1;
		while(matcher.find()){
			System.err.format("I found the text" +
                    " \"%s\" starting at " +
                    "index %d and ending at index %d.%n",
                    matcher.group(),
                    matcher.start(),
                    matcher.end());
			if(start==-1){
				start=matcher.end();
			}else{
				end=matcher.start();
			}
			found = true;
        }
        if(!found){
        	System.err.format("No match found.%n");
        }else{
        	System.out.println(detail.substring(start,end));
        }
	}

	private static void test1(String detail) {
		
		Pattern pattern = Pattern.compile("@[@#]\\w+?\\b");
		Matcher matcher = pattern.matcher(detail);
		boolean found=false;
		while(matcher.find()){
			System.err.format("I found the text" +
                    " \"%s\" starting at " +
                    "index %d and ending at index %d.%n",
                    matcher.group(),
                    matcher.start(),
                    matcher.end());
			found = true;
        }
        if(!found){
        	System.err.format("No match found.%n");
        }
	}
}
