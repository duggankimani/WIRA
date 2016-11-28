package com.duggan.workflow.ouputdocs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.duggan.workflow.server.dao.helper.AttachmentDaoHelper;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.shared.model.Attachment;
import com.duggan.workflow.shared.requests.MultiRequestAction;
import com.duggan.workflow.test.dao.AbstractDaoTest;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.rpc.shared.ServiceException;
import com.gwtplatform.dispatch.shared.ActionException;

public class TestGetDocumentsForCurrentUser extends AbstractDaoTest{

	@Inject ExecutionContext execContext;
	
	@Test
	public void load() throws ActionException, ServiceException{
		execContext.execute(new MultiRequestAction());
	}
	
	@Ignore
	public void getAttachmentsForUser(){
		//List<Attachment> list = AttachmentDaoHelper.getAttachmentsByDocRefId("tdFlpNLtZEVCW6yy");
		List<Attachment> list = AttachmentDaoHelper.getAttachments(44l);
		for(Attachment a: list){
			System.err.println(a.getName());
		}
	}
	
	@Ignore
	public void getTaskIdsForUser() throws FileNotFoundException, IOException{
		
		List<Long> ids = JBPMHelper.get().getTaskIdsForUser("Administrator");
		Assert.assertNotSame(0, ids.size());
		Assert.assertSame(9, ids.size());
	}
	
}
