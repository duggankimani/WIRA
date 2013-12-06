package com.duggan.workflow.shared.model.form;

import java.util.HashMap;
import java.util.Map;

public class ProcessMappings {

	Map<String, String> inputMappings = new HashMap<String, String>();
	Map<String, String> outMappings = new HashMap<String,String>();
	
	public ProcessMappings() {
		
	}
	
	public void setInputMappings(Map<String, String> inMappings){
		this.inputMappings = inMappings;
	}
	
	public void setOutMappings(Map<String, String> outMappings){
		this.outMappings = outMappings;
	}
	
}
