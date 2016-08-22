package com.duggan.pg.jsontyp.test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.duggan.workflow.client.ui.admin.formbuilder.HasProperties;
import com.duggan.workflow.server.dao.helper.FormDaoHelper;
import com.duggan.workflow.server.dao.helper.ProcessDaoHelper;
import com.duggan.workflow.server.dao.model.ADDocType;
import com.duggan.workflow.server.dao.model.ADField;
import com.duggan.workflow.server.dao.model.ADForm;
import com.duggan.workflow.server.dao.model.ADKeyValuePair;
import com.duggan.workflow.server.dao.model.ADOutputDoc;
import com.duggan.workflow.server.dao.model.ADProcessCategory;
import com.duggan.workflow.server.dao.model.ADProperty;
import com.duggan.workflow.server.dao.model.ADTaskStepTrigger;
import com.duggan.workflow.server.dao.model.ADTrigger;
import com.duggan.workflow.server.dao.model.ADValue;
import com.duggan.workflow.server.dao.model.CatalogColumnModel;
import com.duggan.workflow.server.dao.model.CatalogModel;
import com.duggan.workflow.server.dao.model.ProcessDefModel;
import com.duggan.workflow.server.dao.model.TaskStepModel;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProviderImpl;
import com.duggan.workflow.shared.model.ProcessDef;
import com.duggan.workflow.shared.model.form.Field;
import com.duggan.workflow.shared.model.form.Form;
import com.duggan.workflow.shared.model.form.KeyValuePair;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONJAXBContext;

public class TestJsonType {

	@Before
	public void setup() {
		DBTrxProviderImpl.init();
		DB.beginTransaction();
	}
	
	@Ignore
	public void updateBasicJsonField(){
		String fieldRefId=  "VEXVP06omY0ASCDi";
		Field field = FormDaoHelper.getFieldJson(fieldRefId);
		String caption = field.getProperty(HasProperties.CAPTION);
		caption=caption+" XX";
		field.addValue(new KeyValuePair(HasProperties.CAPTION, caption));
		FormDaoHelper.createJson(field);
		
		//email: YVhlqZsUbwZ3gSq7
		fieldRefId="YVhlqZsUbwZ3gSq7";
		field = FormDaoHelper.getFieldJson(fieldRefId);
		caption = field.getProperty(HasProperties.CAPTION);
		caption=caption+" XX";
		field.addValue(new KeyValuePair(HasProperties.CAPTION, caption));
		FormDaoHelper.createJson(field);
		
		//introducedBy: 6jXixv6v4SRXsW45
		fieldRefId="6jXixv6v4SRXsW45";
		field.addValue(new KeyValuePair(HasProperties.CAPTION, caption));
		caption = field.getProperty(HasProperties.CAPTION);
		caption=caption+" XX";
		field = FormDaoHelper.getFieldJson(fieldRefId);
		FormDaoHelper.createJson(field);
		
		//postalAdd
		fieldRefId="Z7TwHEsoyRgv3his";
		caption = field.getProperty(HasProperties.CAPTION);
		caption=caption+" XX";
		field.addValue(new KeyValuePair(HasProperties.CAPTION, caption));
		field = FormDaoHelper.getFieldJson(fieldRefId);
		
		FormDaoHelper.createJson(field);
	}
	
	@Ignore
	public void updateJsonField(){
		String fieldRefId="3iiLV0B4ietZizVY";
		Field field = FormDaoHelper.getFieldJson(fieldRefId);
		
		System.err.println("Fields >> "+field.getFields().size());
		String suffix = " 1234Test";
		String html = field.getProperty(HasProperties.HTMLCONTENT);
		//html = html.replace(suffix, "");
		html = html+suffix;
		field.addValue(new KeyValuePair(HasProperties.HTMLCONTENT, html));
		FormDaoHelper.createJson(field);
		
//		Field saved = FormDaoHelper.getFieldJson(fieldRefId);
//		String htmlContent = saved.getProperty(HasProperties.HTMLCONTENT);
//		Assert.assertTrue(htmlContent.endsWith(suffix));
	}
	
	@Test
	public void getJsonForm(){
		String formRefId = "BzQwh3HgnmQUsqTW";
		Form dbJsonForm = FormDaoHelper.getFormJson(formRefId,true);
		
		for(Field field: dbJsonForm.getFields()){
			System.err.println(field.getId()+" : "+field.getName());
			if(field.getType().hasChildren()){
				for(Field f: field.getFields()){
					System.err.println(f.getId()+" : "+f.getName());
				}
			}
		}
		Assert.assertNotNull(dbJsonForm);
		Assert.assertNotNull(dbJsonForm.getRefId());
		Assert.assertFalse(dbJsonForm.getFields().isEmpty());
	}
	
	@Ignore
	public void migrateForms(){
		List<ProcessDef> processes = ProcessDaoHelper.getAllProcesses(null, false);
		for(ProcessDef p: processes){
			List<Form> adforms = FormDaoHelper.getForms(p.getRefId(),true);
			for(Form form: adforms){
				form.setProcessRefId(p.getRefId());
				Form jsonForm = FormDaoHelper.createJson(form);
				Assert.assertNotNull(jsonForm.getRefId());
			}
		}
		
		//update taskstepmodels
		DB.getEntityManager().createNativeQuery("update taskstepmodel t set formref=(select refid from adform where id=t.formid) where t.formid is not null and formref is null").executeUpdate();
	}
	
	@Ignore
	public void saveJsonForm(){
		long id = 40l;//27l
		Form form = FormDaoHelper.getForm(id,true);//Current storage
		
		Form jsonForm = FormDaoHelper.createJson(form);
		Assert.assertNotNull(jsonForm.getRefId());
		
//		Field field = FormDaoHelper.getField(106l);
//		FormDaoHelper.createJson(field);
	}

	@Ignore
	public void convertForm() throws JAXBException, FileNotFoundException {
		ADForm form = DB.getFormDao().getForm(27l);

		JSONJAXBContext context = new JSONJAXBContext( JSONConfiguration.natural().build(),
						ProcessDefModel.class, TaskStepModel.class,
						ADTaskStepTrigger.class, ADTrigger.class,
						ADDocType.class, ADProcessCategory.class, ADForm.class,
						ADOutputDoc.class, ADField.class, ADProperty.class,
						ADValue.class, ADKeyValuePair.class,
						KeyValuePair.class, CatalogModel.class,
						CatalogColumnModel.class);
		
		StringWriter writer = new StringWriter();
		context.createJSONMarshaller().marshallToJSON(form, new FileOutputStream(""+form.getRefId()+"_form.json"));
		System.err.println(writer.toString());
	}

	@After
	public void commit() throws IOException {
		DB.commitTransaction();
		DB.closeSession();
	}
}
