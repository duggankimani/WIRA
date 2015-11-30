package com.duggan.workflow.test.bpm6.kna;

import java.util.Arrays;
import java.util.List;

import org.jbpm.persistence.processinstance.ProcessInstanceInfo;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.kie.api.task.model.Task;

import com.duggan.workflow.server.helper.auth.LoginHelper;
import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.model.DocStatus;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.DocumentType;
import com.duggan.workflow.shared.model.HTStatus;
import com.duggan.workflow.shared.model.HTUser;
import com.duggan.workflow.shared.model.SearchFilter;
import com.duggan.workflow.shared.model.StringValue;
import com.duggan.workflow.shared.model.UserGroup;
import com.duggan.workflow.shared.requests.ApprovalRequest;
import com.duggan.workflow.shared.requests.CreateDocumentRequest;
import com.duggan.workflow.shared.requests.GetTaskList;
import com.duggan.workflow.shared.responses.ApprovalRequestResult;
import com.duggan.workflow.shared.responses.CreateDocumentResult;
import com.duggan.workflow.shared.responses.GetTaskListResult;
import com.duggan.workflow.test.bpm6.AbstractBPM6Test;
import com.gwtplatform.dispatch.rpc.shared.ServiceException;
import com.gwtplatform.dispatch.shared.ActionException;

public class TestKNAProcess extends AbstractBPM6Test {
	
	private String RPCCOOKIE="AUTHCOOKIEID";

	public void initialize() {
		
		// Create user groups
		UserGroup admin = new UserGroup();
		admin.setName("Admin");
		admin.setFullName("Admin");
		LoginHelper.get().createGroup(admin);

		UserGroup chiefEditors = new UserGroup();
		chiefEditors.setName("ChiefEditor");
		chiefEditors.setFullName("Chief Editor");
		LoginHelper.get().createGroup(chiefEditors);

		UserGroup editors = new UserGroup();
		editors.setName("Editor");
		editors.setFullName("Editor");
		LoginHelper.get().createGroup(editors);
		
		UserGroup general = new UserGroup();
		general.setName("User");
		general.setFullName("General User");
		LoginHelper.get().createGroup(general);


		// Create users
		// Administrator
		HTUser user = new HTUser();
		user.setEmail("kimani@wira.io");
		user.setName("Administrator");
		user.setSurname("");
		user.setUserId("Administrator");
		user.setGroups(Arrays.asList(admin));
		LoginHelper.get().createUser(user);
		
		//Content Creators
		HTUser contentCreator = new HTUser();
		contentCreator.setEmail("mdkimani@gmail.com");
		contentCreator.setName("Matt");
		contentCreator.setSurname("Mutembei");
		contentCreator.setUserId("matt.mutembei");
		contentCreator.setGroups(Arrays.asList(general));
		LoginHelper.get().createUser(contentCreator);

		contentCreator = new HTUser();
		contentCreator.setEmail("mdkimani@gmail.com");
		contentCreator.setName("Justin");
		contentCreator.setSurname("Okocha");
		contentCreator.setUserId("justin.okocha");
		contentCreator.setGroups(Arrays.asList(general));
		LoginHelper.get().createUser(contentCreator);

		contentCreator = new HTUser();
		contentCreator.setEmail("mdkimani@gmail.com");
		contentCreator.setName("Faith");
		contentCreator.setSurname("Wambui");
		contentCreator.setUserId("faith.wambui");
		contentCreator.setGroups(Arrays.asList(general));
		LoginHelper.get().createUser(contentCreator);

		// Editors
		HTUser editor = new HTUser();
		editor.setEmail("mdkimani@gmail.com");
		editor.setName("Njeri");
		editor.setSurname("Maina");
		editor.setUserId("njeri.maina");
		editor.setGroups(Arrays.asList(editors));
		LoginHelper.get().createUser(editor);

		editor = new HTUser();
		editor.setEmail("mdkimani@gmail.com");
		editor.setName("Ngotho");
		editor.setSurname("Calvin");
		editor.setUserId("ngotho.calvin");
		editor.setGroups(Arrays.asList(editors));
		LoginHelper.get().createUser(editor);

		// Chief Editor
		HTUser chiefeditor = new HTUser();
		chiefeditor.setEmail("mdkimani@gmail.com");
		chiefeditor.setName("Kaburo");
		chiefeditor.setSurname("Kobia");
		chiefeditor.setUserId("kaburo.kobia");
		chiefeditor.setGroups(Arrays.asList(chiefEditors));
		LoginHelper.get().createUser(chiefeditor);
	}
	
	@Ignore
	public void createDraftRequest() throws ActionException, ServiceException{
		Document doc = new Document();
		doc.setDescription("Ruto Prayer Meetings");
		DocumentType type = new DocumentType();
		type.setId(3L);
		type.setName("KE.GO.KNA.KNAEDITORIALWORKFLOW");
		type.setDisplayName("KNA workflow");
		doc.setType(type);
		
		CreateDocumentRequest request = new CreateDocumentRequest(doc);
		CreateDocumentResult result = (CreateDocumentResult) dispatchService.execute(RPCCOOKIE, request);
		doc = result.getDocument();
		Assert.assertNotNull(doc.getRefId());
		Assert.assertNotNull(doc.getId());
		Assert.assertNotNull(doc.getCaseNo());
		Assert.assertEquals(doc.getStatus(), DocStatus.DRAFTED);
		Assert.assertNull(doc.getProcessInstanceId());
	}

	@Ignore
	public void startProcess() throws ActionException, ServiceException {
		//initialize();
		
		DocumentType type = new DocumentType();
		type.setId(3L);
		type.setName("KE.GO.KNA.KNAEDITORIALWORKFLOW");
		type.setDisplayName("KNA workflow");

		Document doc = new Document();
		doc.setDescription("Ruto Prayer Meetings");
		doc.setType(type);
		doc.setValue("assignees",new StringValue("faith.wambui"));
		
		ApprovalRequest request = new ApprovalRequest("njeri.maina", doc);
		ApprovalRequestResult result = (ApprovalRequestResult) dispatchService.execute(RPCCOOKIE, request);
		
		doc = result.getDocument();
		Assert.assertNotNull(doc.getRefId()); 
		Assert.assertNotNull(doc.getProcessInstanceId());
		Assert.assertEquals(doc.getProcessStatus(), HTStatus.INPROGRESS);
		Assert.assertEquals(doc.getStatus(), DocStatus.INPROGRESS);
	}
	
	@Test
	public void startMultiAssigneeProcess() throws ActionException, ServiceException {
		DocumentType type = new DocumentType();
		type.setId(3L);
		type.setName("KE.GO.KNA.KNAEDITORIALWORKFLOW");
		type.setDisplayName("KNA workflow");
		
		ProcessInstanceInfo l;

		Document doc = new Document();
		doc.setDescription("Ruto Prayer Meetings");
		doc.setType(type);
		doc.setValue("assignees",new StringValue("faith.wambui,justin.okocha"));
		
		ApprovalRequest request = new ApprovalRequest("njeri.maina", doc);
		ApprovalRequestResult result = (ApprovalRequestResult) dispatchService.execute(RPCCOOKIE, request);
		
		doc = result.getDocument();
		Assert.assertNotNull(doc.getRefId()); 
		Assert.assertNotNull(doc.getProcessInstanceId());
		Assert.assertEquals(doc.getProcessStatus(), HTStatus.INPROGRESS);
		Assert.assertEquals(doc.getStatus(), DocStatus.INPROGRESS);
		
	}
	
	@Ignore
	public void retrieveTasksFroUser() throws ActionException, ServiceException{
		Task t;
		GetTaskList list = new GetTaskList("faith.wambui", new SearchFilter());
		GetTaskListResult result = (GetTaskListResult)dispatchService.execute(RPCCOOKIE, list);
		List<Doc> tasks = result.getTasks();
		Assert.assertNotEquals(0, tasks.size());
	}
}
