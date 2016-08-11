package com.duggan.pg.jsontyp.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.duggan.workflow.server.dao.helper.DocumentDaoHelper;
import com.duggan.workflow.server.dao.hibernate.DocValues;
import com.duggan.workflow.server.dao.hibernate.JsonType;
import com.duggan.workflow.server.dao.model.DocumentModelJson;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProviderImpl;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.DocumentLine;
import com.sun.jersey.api.json.JSONMarshaller;
import com.sun.jersey.api.json.JSONUnmarshaller;

public class TestJsonDocType {

	@Before
	public void setup() {
		DBTrxProviderImpl.init();
		DB.beginTransaction();
	}
	
	@Ignore
	public void migrateData(){
		List<BigInteger> documentIds = DB.getEntityManager()
				.createNativeQuery("select id from localdocument limit 20")
				.getResultList();
		
		for(BigInteger id: documentIds){
			Document doc = DocumentDaoHelper.getDocument(id.longValue());
			DocumentDaoHelper.createJson(doc);
		}
	}
	
	@Ignore
	public void delete(){
		String lineRefId = "mgTLcofNdMyfftuB";
		DocumentDaoHelper.deleteJsonDocLine(lineRefId);
		
		String docRefId = "5ftPhgc32pzHCCOO";
		DocumentDaoHelper.deleteJsonDoc(docRefId);
	}
	
	@Test
	public void saveJsonDocument(){
		long id = 136l;//27l
		Document doc = DocumentDaoHelper.getDocument(id);
		System.err.println("Values -> "+doc.getValues().size());
		System.err.println("Details -> "+doc.getDetails().size());
		Document jsonDoc = DocumentDaoHelper.createJson(doc);
		Assert.assertNotNull(jsonDoc.getId());
	}
	

	@Ignore
	public void unmarshalJsonDocValues() throws JAXBException, UnsupportedEncodingException{
		String json = "{\"values\": [{\"key\": \"inputEmail\", \"type\": \"stringValue\", \"value\": \"Administrator\"}]}";
		JSONUnmarshaller unmarshaller = JsonType.getJaxbContext().createJSONUnmarshaller();
		DocValues values = unmarshaller.unmarshalFromJSON(new ByteArrayInputStream(json.getBytes("UTF-8")), DocValues.class);
		
		Assert.assertNotNull(values);
	}
	
	
	@Ignore
	public void retrieveDocJson(){
		DocumentModelJson jsonDoc=DB.getDocumentDao().findByRefId("5ftPhgc32pzHCCOO", DocumentModelJson.class);
		
		Assert.assertNotNull(jsonDoc);
	}
	
	@Ignore
	public void generateJson() throws JAXBException{
		long id = 136l;//27l
		Document doc = DocumentDaoHelper.getDocument(id);
		System.err.println("Values -> "+doc.getValues().size());
		JSONMarshaller marshaller = JsonType.getJaxbContext().createJSONMarshaller();
		
//		marshaller.marshallToJSON(doc, System.out);
		
		DocValues values = new DocValues(); 
		//marshaller.marshallToJSON(new DocValues(doc.getValues()), System.out);
		
		for(String key:doc.getDetails().keySet()){
			List<DocumentLine> lines = doc.getDetails().get(key);
			for(DocumentLine line: lines){
				line.setName(key);
				StringWriter out = new StringWriter();
				marshaller.marshallToJSON(line, out);
				System.err.println(out);
			}
		}
	}

	@After
	public void commit() throws IOException {
		DB.commitTransaction();
		DB.closeSession();
	}
}
