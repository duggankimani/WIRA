package com.duggan.workflow.mvel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mvel2.MVEL;
import org.mvel2.ParserContext;
import org.mvel2.integration.VariableResolverFactory;
import org.mvel2.integration.impl.MapVariableResolverFactory;

import com.duggan.workflow.server.dao.helper.DocumentDaoHelper;
import com.duggan.workflow.server.dao.helper.ProcessDaoHelper;
import com.duggan.workflow.server.dao.model.ADTaskStepTrigger;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProviderImpl;
import com.duggan.workflow.server.helper.jbpm.ProcessMigrationHelper;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.StringValue;
import com.duggan.workflow.shared.model.TaskStepDTO;
import com.duggan.workflow.shared.model.TriggerType;

public class TestMvel {

	@Before
	public void setup() {
		DBTrxProviderImpl.init();
		DB.beginTransaction();
		// ProcessMigrationHelper.start(4L);
	}

	@Test
	public void testVars(){
		String script = ""
				+ ""
				+ "setValue('totalClaimValue', "
				+ "values.selectExpense.value.equals('Meals')? "
				+ "values.?totalMealsClaim: "
				+ "values.selectExpense.value.equals('Per diem')? "
				+ "values.?totalPerDiemClaim: "
				+ "values.?totalTaxiClaim); "
				+ ""
				+ "department=(values.departmentOrbranch.?value==null? 'DPTNOTFOUND': "
				+ "values.departmentOrbranch.?value); "
				+ ""
				+ "System.out.println(\"tata>>\"+department);"
				+ ""
				+ "totalClaim = values.?totalClaimValue.value; "
				+ ""
				+ "gl_code=values.selectExpense.?value==null? 'GLNOTFOUND': "
				+ "values.selectExpense.?value=='Meals'? '400180065': "
				+ "values.selectExpense.?value=='Per diem'? '409000001': "
				+ "values.selectExpense.?value=='Taxi'? '409000003': 'EXPENSENOTFOUND'; "
				+ ""
				+ "sql=\"select proc_updatebudget('\"+caseNo+\"',"
				+ "'\"+department+\"',"
				+ "'\"+gl_code+\"',"
				+ "\"+totalClaim+\");\" ;"
				+ ""
				+ "System.out.println(sql); "
				+ ""
				+ "DBUtil.getStringValue(sql,'wiradb');";
		
		 
		
		long documentId = 23L;
		Document doc = DocumentDaoHelper.getDocument(documentId);
		System.err.println(">>>>>> Total taxi claim = "+doc.getValues().get("totalTaxiClaim"));
		// System.err.println(doc.getDetails().keySet());
		ParserContext context = new ParserContext();
		context.addPackageImport("com.duggan.workflow.server.db");
		context.addPackageImport("com.duggan.workflow.shared.model");
		context.addPackageImport("com.duggan.workflow.server.helper.session");
		context.addPackageImport("java.util");

		VariableResolverFactory myVarFactory = new MapVariableResolverFactory();
		// MVEL.eval(script, myVarFactory);

		Serializable compilexEx = MVEL.compileExpression(script, context);
		MVEL.executeExpression(compilexEx, doc, myVarFactory);

		System.out.println("############ "
				+ MVEL.evalToString("details.?approvalLines", doc));

		
	}
	
	@Ignore
	public void testVariables() {
		long documentId = 24L;
		Document doc = DocumentDaoHelper.getDocument(documentId);
		// System.err.println(doc.getDetails().keySet());
		ParserContext context = new ParserContext();
		context.addPackageImport("com.duggan.workflow.server.db");
		context.addPackageImport("com.duggan.workflow.shared.model");
		context.addPackageImport("com.duggan.workflow.server.helper.session");
		context.addPackageImport("java.util");

		String script = "setValue('approvedByLog', "
				+ "new StringValue(SessionHelper.getCurrentUser().getFullName()));"
				+ "setValue('dateApprovedLog', new DateValue(new Date()));"
				+ ""
				+ "addDetail(new com.duggan.workflow.shared.model.DocumentLine("
				+ "'approvalLines',"
				+ "null,"
				+ "getId(),"
				+ "values.?approvedByLog,"
				+ "values.?dateApprovedLog,"
				+ "values.?actionApprovedLog" + "));";
		//
		VariableResolverFactory myVarFactory = new MapVariableResolverFactory();
		// MVEL.eval(script, myVarFactory);

		Serializable compilexEx = MVEL.compileExpression(script, context);
		MVEL.executeExpression(compilexEx, doc, myVarFactory);

		System.out.println("############ "
				+ MVEL.evalToString("details.?approvalLines", doc));
		// System.out.println("############ "+MVEL.evalToString("details.poNumber",
		// doc));
		// System.out.println("############ "+MVEL.evalToString("details.poParticulars",
		// doc));

		// MVEL.eval("setValue('matta',values.subject); setValue('yatta',values.subject);",
		// doc);
		// System.out.println("############ "+MVEL.evalToString("values.matta.value",
		// doc)+ " :: "
		// +MVEL.evalToString("values.yatta.value", doc));
	}

	@Ignore
	public void scriptMvel1() throws FileNotFoundException, IOException {
		Long taskId = 1090L;
		List<TaskStepDTO> steps = ProcessDaoHelper.getTaskStepsByTaskId(taskId);

		for (TaskStepDTO dto : steps) {
			System.out.println(dto.getId() + " >> " + dto.getFormName());
		}
		TaskStepDTO step = steps.get(steps.size() - 1);
		Collection<ADTaskStepTrigger> adTriggers = DB.getProcessDao()
				.getTaskStepTriggers(step.getId(), TriggerType.AFTERSTEP);
		for (ADTaskStepTrigger t : adTriggers) {
			System.err.println(">> " + t.getTrigger().getName());
		}

		long documentId = 94L;
		Document doc = DocumentDaoHelper.getDocument(documentId);
		doc.setValue("approved", new StringValue("Yes"));

		// System.err.println(doc.getDetails().keySet());
		ParserContext context = new ParserContext();
		context.addPackageImport("com.duggan.workflow.server.db");
		context.addPackageImport("com.duggan.workflow.shared.model");

		String script = "setValue('isApproved',new BooleanValue(values.approved.value=='Yes'))";
		Serializable compilexEx = MVEL.compileExpression(script, context);
		MVEL.executeExpression(compilexEx, doc);
		System.out.println("############ "
				+ MVEL.evalToString("values.isApproved", doc));

		// System.out.println("############ "+MVEL.evalToString("details.poNumber",
		// doc));
		// System.out.println("############ "+MVEL.evalToString("details.poParticulars",
		// doc));

		// MVEL.eval("setValue('matta',values.subject); setValue('yatta',values.subject);",
		// doc);
		// System.out.println("############ "+MVEL.evalToString("values.matta.value",
		// doc)+ " :: "
		// +MVEL.evalToString("values.yatta.value", doc));
	}

	@Ignore
	public void scriptMvel() throws FileNotFoundException, IOException {
		long documentId = 5L;
		Document doc = DocumentDaoHelper.getDocument(documentId);
		// System.err.println(doc.getDetails().keySet());
		ParserContext context = new ParserContext();
		context.addPackageImport("com.duggan.workflow.server.db");
		context.addPackageImport("com.duggan.workflow.shared.model");

		// {total=total:120000.0, name=name:Item#1, item=item:Item#1,
		// unitPrice=unitPrice:100.0, quantity=quantity:1200.0}
		String script = "setValue('poNumber',"
				+ "new StringValue(null,'poNumber', "
				+ "DBUtil.getStringValue("
				+ "\"select concat(id,subject) from localdocument where subject='\"+ subject +\"'\" , 'LocalDB')));";

		// + "foreach(Object l: details.requisitionParticulars){"
		// +
		// "addDetail(with(new com.duggan.workflow.shared.model.DocumentLine()){"
		// + "documentId=l.documentId,"
		// + "name='poParticulars',"
		// +
		// "values=['Item':l.values.?item,'Qty':l.?values.quantity,'UOM': l.values.?uom, "
		// + "'Price':l.?values.unitPrice,'total':l.values.total]"
		// + "})"
		// + "};";

		Serializable compilexEx = MVEL.compileExpression(script, context);
		MVEL.executeExpression(compilexEx, doc);
		System.out.println("############ "
				+ MVEL.evalToString("values.poNumber", doc));
		// System.out.println("############ "+MVEL.evalToString("details.poNumber",
		// doc));
		// System.out.println("############ "+MVEL.evalToString("details.poParticulars",
		// doc));

		// MVEL.eval("setValue('matta',values.subject); setValue('yatta',values.subject);",
		// doc);
		// System.out.println("############ "+MVEL.evalToString("values.matta.value",
		// doc)+ " :: "
		// +MVEL.evalToString("values.yatta.value", doc));
	}

	@Ignore
	public void mvelLoop() {
		long documentId = 10L;
		Document doc = DocumentDaoHelper.getDocument(documentId);
		ParserContext context = new ParserContext();
		context.addPackageImport("com.duggan.workflow.server.db");
		context.addPackageImport("com.duggan.workflow.shared.model");

		String script = "foreach(Object l: details.requisitionLines){"
				+ "addDetail(with(new com.duggan.workflow.shared.model.DocumentLine()){"
				+ "documentId=l.documentId," + "name='poParticulars',"
				+ "values=['Item':l.values.?item,"
				+ "'quantity':l.?values.quantity," + "'uom': l.values.?uom, "
				+ "'unitPrice':l.?values.unitPrice,"
				+ "'total':l.values.total]" + "})" + "};"
				+ "setValue('poValue', values.reqValue);";

		Serializable compilexEx = MVEL.compileExpression(script, context);
		MVEL.executeExpression(compilexEx, doc);
		System.out.println("############ "
				+ MVEL.evalToString("details.poParticulars", doc));
	}

	//
	// foreach(Object doc: details.imprestLines){
	// if(doc!=null){
	// addDetail(with(new com.duggan.workflow.shared.model.DocumentLine()){
	// documentId=doc.documentId,
	// name='imprestLinesOut',
	// values=['account': new StringValue(DBUtil.getStringValue("
	// select accountcode||'-'||accountname from pmt_accounts
	// where accountcode='"+doc.values.?account.?value+"' ", 'workflowmgr'))]
	// }))
	// }
	// };

	@After
	public void close() {
		DB.rollback();
		DB.closeSession();
	}
}
