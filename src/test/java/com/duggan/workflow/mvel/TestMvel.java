package com.duggan.workflow.mvel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mvel2.MVEL;
import org.mvel2.ParserContext;

import com.duggan.workflow.server.dao.helper.DocumentDaoHelper;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProvider;
import com.duggan.workflow.shared.model.Document;

public class TestMvel {

	@Before
	public void setup(){
		DBTrxProvider.init();
		DB.beginTransaction();
	}
	
	@Test
	public void scriptMvel() throws FileNotFoundException, IOException{
		long documentId = 5L;
		Document doc = DocumentDaoHelper.getDocument(documentId);
		//System.err.println(doc.getDetails().keySet());
		ParserContext context = new ParserContext();
		context.addPackageImport("com.duggan.workflow.server.db");
		context.addPackageImport("com.duggan.workflow.shared.model");
		
		//{total=total:120000.0, name=name:Item#1, item=item:Item#1, unitPrice=unitPrice:100.0, quantity=quantity:1200.0}
		String script = "setValue('poNumber',"
				+ "new StringValue(null,'poNumber', "
				+ "DBUtil.getStringValue("
				+ "\"select concat(id,subject) from localdocument where subject='\"+ subject +\"'\" , 'LocalDB')));";
	
//				+ "foreach(Object l: details.requisitionParticulars){"
//				+ "addDetail(with(new com.duggan.workflow.shared.model.DocumentLine()){"
//				+ "documentId=l.documentId,"
//				+ "name='poParticulars',"
//				+ "values=['Item':l.values.?item,'Qty':l.?values.quantity,'UOM': l.values.?uom, "
//				+ "'Price':l.?values.unitPrice,'total':l.values.total]"
//				+ "})"
//				+ "};";
		
		Serializable compilexEx = MVEL.compileExpression(script,context);
		MVEL.executeExpression(compilexEx, doc);
		System.out.println("############ "+MVEL.evalToString("values.poNumber", doc));
//		System.out.println("############ "+MVEL.evalToString("details.poNumber", doc));
//		System.out.println("############ "+MVEL.evalToString("details.poParticulars", doc));
				
//		MVEL.eval("setValue('matta',values.subject); setValue('yatta',values.subject);", doc);
//		System.out.println("############ "+MVEL.evalToString("values.matta.value", doc)+ " :: "
//				+MVEL.evalToString("values.yatta.value", doc));
	}
	
	@After
	public void close(){
		DB.rollback();
		DB.closeSession();
	}
}
