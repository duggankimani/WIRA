package com.duggan.workflow.test.bpm6.kna;

import java.util.List;

import org.jbpm.services.task.impl.model.TaskImpl;
import org.junit.Test;

import com.duggan.workflow.server.helper.auth.LoginHelper;
import com.duggan.workflow.test.bpm6.AbstractBPM6Test;
import com.wira.commons.shared.models.HTUser;

public class TestKNAUserManagement extends AbstractBPM6Test{

	@Test
	public void loadUsers(){
		TaskImpl l;
		List<HTUser> users = LoginHelper.get().getAllUsers();
		System.err.println(users.size());
	}
}
 