package com.duggan.workflow.client.ui.document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.duggan.workflow.client.ui.admin.formbuilder.HasProperties;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.client.util.ENV;
import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.HTSummary;
import com.duggan.workflow.shared.model.OutputDocument;
import com.duggan.workflow.shared.model.StringValue;
import com.duggan.workflow.shared.model.form.Field;
import com.duggan.workflow.shared.model.form.Form;
import com.duggan.workflow.shared.model.form.Property;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;

public class GenericDocUtils {

	public static Form generateForm(OutputDocument outDoc, Doc doc) {
	
		//Path to be assigned to the generated output.
		String path = generatePath(outDoc.getPath(),doc);
		String name = (path==null? outDoc.getDisplayName(): 
			path.contains("/")? path.substring(path.lastIndexOf("/")+1,path.length())
					:path); 
		
		Form form = new Form();
		form.setId(-10L);
		form.setCaption("Generate Document");
		Property prop = new Property(HasProperties.CAPTION, null, DataType.STRING);
		prop.setValue(new StringValue(form.getCaption()));
		Property help = new Property(HasProperties.HELP, null, DataType.STRING);
		help.setValue(new StringValue("Click Link To Generate Document"));
		form.setProperties(Arrays.asList(prop,help));
				
		Field field = new Field();
		field.setType(DataType.LINK);
		
		List<Property> properties = new ArrayList<Property>();
		prop = new Property(HasProperties.CAPTION, "Caption", DataType.STRING);
		prop.setValue(new StringValue(name));
		properties.add(prop);
		
		prop = new Property(HasProperties.HELP, "Help", DataType.STRING);
		prop.setValue(new StringValue(path));
		properties.add(prop);
		
		prop = new Property(HasProperties.HREF, "Href", DataType.STRING);
		String href = AppContext.getBaseUrl()+"/getreport?name="+name
				+"&path="+path
				+"&template="+outDoc.getCode()+"&doc="+(doc instanceof Document? 
						doc.getId() : ((HTSummary)doc).getDocumentRef())+"&ACTION=generateoutput";
		prop.setValue(new StringValue(href));
		properties.add(prop);
		
		field.setProperties(properties);
		form.addField(field);
		
		return form;
	}

	private static String generatePath(String codedPath, Doc doc) {
		
		if(codedPath==null || codedPath.isEmpty()){
			return null;
		}
		
		String[] elements = codedPath.split("/");
		StringBuffer buffer = new StringBuffer();
		for(String el: elements){
			String decoded= decode(el, doc);
			decoded = decoded.replace("/", "-");
			buffer.append(decoded+"/");
		}
		
		String out = buffer.toString();
		return out.substring(0, out.length()-1);
	}

	/**
	 * We are looking for String containing @@ or @# followed by any set of characters
	 *  
	 * <p>
	 * @param pathEl
	 * @return
	 */
	private static String decode(String pathEl, Doc doc) {
		
		if(!pathEl.contains("@@") && !pathEl.contains("@#")){
			return pathEl;
		}
				
		RegExp regExp = RegExp.compile("@[@#]\\w+?\\b");
		
		MatchResult matchResult = regExp.exec(new String(pathEl));
		if(matchResult!=null){
			for(int i=0; i<matchResult.getGroupCount(); i++){
				String groupStr = matchResult.getGroup(i);
				String fieldName = groupStr.substring(2, groupStr.length());
				Object fieldValue = getFieldValue(fieldName,doc);		
				System.err.println("### "+pathEl + ", "+fieldName+ "="+fieldValue);
				pathEl = pathEl.replaceAll(groupStr, fieldValue==null? "": fieldValue.toString());
			}
		}
	
		return pathEl;
	}

	private static Object getFieldValue(String fieldName, Doc doc) {
		
		Field field = new Field();
		field.setName(fieldName);
		if(doc.getId()!=null)
			field.setDocId(doc.getId()+"");
		
		return ENV.getValue(field.getQualifiedName());
	}

	
}
