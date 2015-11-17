package com.duggan.workflow.test.dao;

import org.apache.onami.test.OnamiRunner;
import org.apache.onami.test.annotation.GuiceModules;
import org.jukito.JukitoRunner;
import org.jukito.UseModules;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.runner.RunWith;

import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProviderImpl;
import com.duggan.workflow.server.guice.UserTransactionProvider;
import com.google.inject.Inject;

@RunWith(OnamiRunner.class)
@GuiceModules(BaseModule.class)
public abstract class AbstractDaoTest {
	
	@Inject UserTransactionProvider trxProvider;

	@Before
	public void begin() {
		try{
			trxProvider.get().begin();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	@After
	public void commit() {
		try{
			trxProvider.get().commit();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	public void rollback() {
		try{
			trxProvider.get().rollback();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	@AfterClass
	public static void shutdown(){
		DBTrxProviderImpl.close();
	}

}
