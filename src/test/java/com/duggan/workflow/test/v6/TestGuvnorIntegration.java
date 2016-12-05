package com.duggan.workflow.test.v6;

import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.drools.compiler.kproject.ReleaseIdImpl;
import org.drools.core.io.impl.UrlResource;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;

public class TestGuvnorIntegration {
	
	Logger log = Logger.getLogger(TestGuvnorIntegration.class);

	@Test
	public void load() throws IOException {

        // works even without -SNAPSHOT versions
        String url = "http://localhost:9090/kie-wb/maven2/ke/co/workpoint/Workplans/1.0/WorkplanRequisition-1.0.bpmn2";
        //"http://localhost:8080/kie-drools-wb/maven2/groupId/artifactId/1.0/artifactId-1.0.jar";


        // make sure you use "LATEST" here!
        //ReleaseIdImpl releaseId = new ReleaseIdImpl("ke.co.workpoint.workplan", "WorkplanRequisition", "LATEST");

        KieServices ks = KieServices.Factory.get();
        KieRepository kr = ks.getRepository();
        UrlResource urlResource = (UrlResource) ks.getResources()
                .newUrlResource(url);
        urlResource.setUsername("workbench");
        urlResource.setPassword("workbench1!");
        urlResource.setBasicAuthentication("enabled");
        InputStream is = urlResource.getInputStream();
        KieModule kModule = kr.addKieModule(ks.getResources()
                .newInputStreamResource(is));

        ReleaseId rid = kModule.getReleaseId();
        
        log.debug("Release ID - "+rid.getArtifactId()+":"+rid.getGroupId()+":"+rid.getVersion());
        KieContainer kContainer = ks.newKieContainer(kModule.getReleaseId());

        kContainer.newStatelessKieSession();
        
	}
}
