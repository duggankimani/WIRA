package com.duggan.workflow.ouputdocs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.duggan.workflow.server.dao.helper.AttachmentDaoHelper;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProviderImpl;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.shared.model.Attachment;

public class TestGetDocumentsForCurrentUser {

	@Before
	public void setup(){
		DBTrxProviderImpl.init();
		DB.beginTransaction();
	}
	
	@Test
	public void getAttachmentsForUser(){
		List<Attachment> list = AttachmentDaoHelper.getAllAttachments("Administrator",true);
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
	
	@After
	public void close(){
		DB.commitTransaction();
		DB.closeSession();
	}
}
