package com.duggan.workflow.test.process.img;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProvider;
import com.duggan.workflow.server.helper.auth.LoginHelper;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.server.helper.jbpm.ProcessMigrationHelper;

public class TestGenerateProcessImage {

	@Before
	public void setup(){
		DBTrxProvider.init();
		DB.beginTransaction();
		ProcessMigrationHelper.start(3L);
		ProcessMigrationHelper.start(1L);
//		ProcessMigrationHelper.start(16L);
//		ProcessMigrationHelper.start(4L);
//		ProcessMigrationHelper.start(1L);
	}
	
	@Test
	public void draw() throws IOException{
		//Long processInstanceId = 950L;
		//Long processInstanceId = 922L;
		//Long processInstanceId = 906L;
		Long processInstanceId = 428L;
		
		InputStream is = JBPMHelper.get().getProcessMap(processInstanceId);
		
		String prefix ="Image_PID"+processInstanceId;
		String suffix = ".png";
		
		File tmpFile = new File(prefix+suffix);//File.createTempFile(prefix, suffix);
        
        IOUtils.copy(is, new FileOutputStream(tmpFile));
        IOUtils.closeQuietly(is);
	}
	

	@org.junit.After
	public void destroy() throws IOException{
		DB.rollback();
		LoginHelper.get().close();
		DB.closeSession();
	}
}
