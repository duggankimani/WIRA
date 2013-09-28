package com.duggan.workflow.server.rest.model;

import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.jbpm.executor.api.CommandContext;



@XmlRootElement
@XmlType(name="")
@XmlAccessorType(XmlAccessType.FIELD)
public class Request {

	private String commandName;
	private BusinessKey businessKey;
	
	@XmlJavaTypeAdapter(value=MapAdapter.class)
	private Map<String, Object> context;
	
	public Request() {
		// TODO Auto-generated constructor stub
	}
	
	public Request(String commandName, BusinessKey bizKey, Map<String, Object> context){
		this.commandName = commandName;
		this.businessKey = bizKey;
		this.context= context;
	}
	
	public BusinessKey getBusinessKey() {
		return businessKey;
	}
	public void setBusinessKey(BusinessKey businessKey) {
		this.businessKey = businessKey;
	}
	
	public String getCommandName() {
		return commandName;
	}
	public void setCommandName(String commandName) {
		this.commandName = commandName;
	}
	
	@Override
	public String toString() {
		
		String str = "BusinessKey = [Command Name= "+commandName+"; " +
				"BusinessKey = "+businessKey+"; " +
						"Context = "+context+"]";
		
		return str;
	}

	public Map<String, Object> getContext() {
		return context;
	}

	public void setContext(Map<String, Object> context) {
		this.context = context;
	}
	
	public Object getContext(String key){
		return context.get(key);
	}
}
