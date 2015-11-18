package com.duggan.workflow.server.guice;

import java.util.HashMap;
import java.util.Map;

import com.duggan.workflow.server.servlets.upload.GetReport;
import com.duggan.workflow.server.servlets.upload.UploadServlet;
import com.google.inject.servlet.ServletModule;
import com.gwtplatform.dispatch.rpc.server.guice.DispatchServiceImpl;
import com.gwtplatform.dispatch.rpc.shared.ActionImpl;

public class DispatchServletModule extends ServletModule {

	@Override
	public void configureServlets() {
		serve("/" + ActionImpl.DEFAULT_SERVICE_NAME + "*").with(
				DispatchServiceImpl.class);
		

        Map<String, String> params = new HashMap<String, String>();
        params.put("loadonstartup", "1");
        params.put("maxSize", "5000485760");
        params.put("maxFileSize", "5000485760");
		serve("/upload").with(UploadServlet.class,params);
		serve("/getreport").with(GetReport.class,params);
	}
}
