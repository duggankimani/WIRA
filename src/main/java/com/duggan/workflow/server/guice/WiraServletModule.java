package com.duggan.workflow.server.guice;

import java.util.HashMap;
import java.util.Map;

import com.duggan.workflow.server.servlets.upload.GetReport;
import com.duggan.workflow.server.servlets.upload.UploadServlet;
import com.google.inject.persist.PersistFilter;
import com.google.inject.servlet.ServletModule;

public class WiraServletModule extends ServletModule{

	@Override
	protected void configureServlets() {
		super.configureServlets();
		
        //if you had a Persistence Service like JPA Unit of Work you would need to add this PersistFilter also.
        filter("/*").through(PersistFilter.class);
		

        Map<String, String> params = new HashMap<String, String>();
        params.put("loadonstartup", "1");
        params.put("maxSize", "5000485760");
        params.put("maxFileSize", "5000485760");
		serve("/upload").with(UploadServlet.class,params);
		serve("/getreport").with(GetReport.class,params);
	}
}
