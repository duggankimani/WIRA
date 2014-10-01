package com.duggan.workflow.client.ui.document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.duggan.workflow.client.ui.admin.formbuilder.HasProperties;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.model.OutputDocument;
import com.duggan.workflow.shared.model.StringValue;
import com.duggan.workflow.shared.model.form.Field;
import com.duggan.workflow.shared.model.form.Form;
import com.duggan.workflow.shared.model.form.Property;

public class GenericDocUtils {

	public static Form generateForm(OutputDocument outDoc, Doc doc) {
	
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
		prop.setValue(new StringValue(outDoc.getDisplayName()));
		properties.add(prop);
		
		prop = new Property(HasProperties.HREF, "Href", DataType.STRING);
		String href = AppContext.getBaseUrl()+"/getreport?name="+outDoc.getDisplayName()
				+"&template="+outDoc.getCode()+"&doc="+doc.getId()+"&ACTION=generateoutput";
		prop.setValue(new StringValue(href));
		properties.add(prop);
		
		field.setProperties(properties);
		form.addField(field);
		
		return form;
	}

	
}
