package com.duggan.workflow.server.guice;

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
		serve("/upload").with(UploadServlet.class);
		serve("/getreport").with(GetReport.class);
	}
}
