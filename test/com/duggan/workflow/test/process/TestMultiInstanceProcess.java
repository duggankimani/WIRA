package com.duggan.workflow.test.process;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.ProcessInstance;
import org.jbpm.task.TaskService;
import org.jbpm.task.query.TaskSummary;
import org.jbpm.test.JbpmJUnitTestCase;
import org.junit.Before;
import org.junit.Test;

public class TestMultiInstanceProcess extends JbpmJUnitTestCase {

	@Override
	@Before
	public void setUp() throws Exception {
		
		super.setUp();
	}
	
	@Test
	public void testProcess() {
		StatefulKnowledgeSession ksession = createKnowledgeSession("defaultPackage.multiinstanceprocess.bpmn2");
		TaskService serv = getTaskService(ksession);
		
		// start the process

		ProcessInstance processInstance = ksession
				.startProcess("defaultPackage.multiinstanceprocess");

		// check whether the process instance has completed successfully
		assertProcessInstanceActive(processInstance.getId(), ksession);

		Map<String, Object> params = new HashMap<>();
		params.put("rfqNumber", Arrays.asList("RFQ-001","RFQ-002","RFQ-003"));
		TaskSummary task = serv.getTasksAssignedAsPotentialOwner("jodonya", "en-UK").get(0);
		serv.completeWithResults(task.getId(), "jodonya", params);
		assertProcessVarExists(processInstance, "rfqNumber");
		
		
		
		// check whether the given nodes were executed during the process
		// execution
//		assertNodeTriggered(processInstance.getId(), "StartProcess", "Hello",
//				"EndProcess");
	}

}
