package com.duggan.workflow.email;

import java.io.IOException;
import java.util.List;

import org.jbpm.executor.ExecutorModule;
import org.jbpm.executor.entities.RequestInfo;
import org.jbpm.executor.impl.ExecutorFactory;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.duggan.workflow.server.dao.model.ErrorLog;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProviderImpl;
import com.duggan.workflow.server.helper.auth.UserDaoHelper;
import com.duggan.workflow.shared.model.RequestInfoDto;

public class TestEmailSchedular {

	@Before
	public void setup(){
		DBTrxProviderImpl.init();
		DB.beginTransaction();
	}
	
	@Test
	public void sendMail(){
		RequestInfo info = ExecutorModule.getInstance().getExecutorServiceEntryPoint()
				.getRequestById("ef8074d3-8d5f-43a1-8793-6f54b9dcfd14");
		
		RequestInfoDto dto = info.toDto();
		System.out.println("################# Executor recipients: "+dto.getUsers());
		ExecutorFactory.getExecutor().updateRequest(dto, true);
	}
	
	@Ignore
	public void getAllEmailRequests(){
		List<ErrorLog> errors = ExecutorModule.getInstance().getExecutorServiceEntryPoint().getAllErrors();
		System.err.println("\n\n>>>> InError");
		for(ErrorLog e: errors){
			System.err.println(e.getId()+" : "+e.getMsg());
		}
		
//		List<RequestInfo> reqList = ExecutorModule.getInstance().getExecutorServiceEntryPoint().getAllRequests();
//		for(RequestInfo i: reqList){
//			System.err.println(i.getKey()+" : "+i.getStatus());
//		}
		System.err.println("\n\n>>>> InErrorRequests");
		List<RequestInfo> reqErrList = ExecutorModule.getInstance().getExecutorServiceEntryPoint().getInErrorRequests();
		for(RequestInfo i: reqErrList){
			i.getRequestData();
			System.err.println(i.getKey()+" : "+i.getStatus());
		}
	}
	
	@org.junit.After
	public void destroy() throws IOException{
		DB.commitTransaction();
		DB.closeSession();
	}
}
