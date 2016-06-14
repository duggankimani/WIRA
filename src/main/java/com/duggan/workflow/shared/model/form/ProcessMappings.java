package com.duggan.workflow.shared.model.form;

import java.util.HashMap;
import java.util.HashMap;

public class ProcessMappings {

	HashMap<String, String> inputMappings = new HashMap<String, String>();
	HashMap<String, String> outMappings = new HashMap<String,String>();
	
	public ProcessMappings() {
		
	}
	
	public void setInputMappings(HashMap<String, String> inMappings){
		this.inputMappings = inMappings;
	}
	
	public void setOutMappings(HashMap<String, String> outMappings){
		this.outMappings = outMappings;
	}
	
	public String getOutputName(String oldName){
		String newName = outMappings.get(oldName);
		if(newName==null){
			newName = oldName;
		}
		return newName;
	}
	
	public boolean containsOutputMappingFrom(String outputKey){
		return outMappings.containsKey(outputKey);
	}
}
