package com.duggan.workflow.mvel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mvel2.MVEL;
import org.mvel2.ParserContext;

import com.duggan.workflow.server.dao.helper.DocumentDaoHelper;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProvider;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.StringValue;

public class TestMvel {

	@Before
	public void setup(){
		DBTrxProvider.init();
		DB.beginTransaction();
	}
	
	@Test
	public void scriptMvel1() throws FileNotFoundException, IOException{
		long documentId = 94L;
		Document doc = DocumentDaoHelper.getDocument(documentId);
		doc.setValue("approved",new StringValue("Yes"));
		
		//System.err.println(doc.getDetails().keySet());
		ParserContext context = new ParserContext();
		context.addPackageImport("com.duggan.workflow.server.db");
		context.addPackageImport("com.duggan.workflow.shared.model");
		
		String script = "setValue('isApproved',new BooleanValue(values.approved.value=='Yes'))";
		Serializable compilexEx = MVEL.compileExpression(script,context);
		MVEL.executeExpression(compilexEx, doc);
		System.out.println("############ "+MVEL.evalToString("values.isApproved", doc));
		
//		System.out.println("############ "+MVEL.evalToString("details.poNumber", doc));
//		System.out.println("############ "+MVEL.evalToString("details.poParticulars", doc));
				
//		MVEL.eval("setValue('matta',values.subject); setValue('yatta',values.subject);", doc);
//		System.out.println("############ "+MVEL.evalToString("values.matta.value", doc)+ " :: "
//				+MVEL.evalToString("values.yatta.value", doc));
	}
	
	@Ignore
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
	
	@Ignore
	public void mvelLoop(){
		long documentId = 10L;
		Document doc = DocumentDaoHelper.getDocument(documentId);
		ParserContext context = new ParserContext();
		context.addPackageImport("com.duggan.workflow.server.db");
		context.addPackageImport("com.duggan.workflow.shared.model");
		
		String script= "foreach(Object l: details.requisitionLines){"
		+ "addDetail(with(new com.duggan.workflow.shared.model.DocumentLine()){"
		+ "documentId=l.documentId,"
		+ "name='poParticulars',"
		+ "values=['Item':l.values.?item,"
		+ "'quantity':l.?values.quantity,"
		+ "'uom': l.values.?uom, "
		+ "'unitPrice':l.?values.unitPrice,"
		+ "'total':l.values.total]"
		+ "})"
		+ "};"
		+ "setValue('poValue', values.reqValue);";
		
		Serializable compilexEx = MVEL.compileExpression(script,context);
		MVEL.executeExpression(compilexEx, doc);
		System.out.println("############ "+MVEL.evalToString("details.poParticulars", doc));
	}
//	
//	foreach(Object doc: details.imprestLines){
//		if(doc!=null){
//		  addDetail(with(new com.duggan.workflow.shared.model.DocumentLine()){
//		   documentId=doc.documentId,
//		   name='imprestLinesOut',
//		   values=['account': new StringValue(DBUtil.getStringValue("
//		select accountcode||'-'||accountname from pmt_accounts 
//		where accountcode='"+doc.values.?account.?value+"' ", 'workflowmgr'))]
//		}))
//		}
//		};
	
	@After
	public void close(){
		DB.rollback();
		DB.closeSession();
	}
}
