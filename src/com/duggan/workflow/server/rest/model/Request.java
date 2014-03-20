package com.duggan.workflow.server.rest.model;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement
@XmlType(name="")
@XmlAccessorType(XmlAccessType.FIELD)
public class Request {

	private String commandName;
	
	private BusinessKey businessKey;
	
	@XmlJavaTypeAdapter(value=MapAdapter.class)
	private Map<String, Object> context;
	
	private List<Detail> details;
	
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
		if(this.context==null){
			this.context = context;
		}else{
			this.context.putAll(context);
		}
	}
	
	public Object getContext(String key){
		return context.get(key);
	}

	public List<Detail> getDetails() {
		return details;
	}

	public void setDetails(List<Detail> details) {
		this.details = details;
	}
	
	public void setDescription(String description){
		checkNull();
		context.put("description", description);
	}
	
	public void setDocumentNo(String documentNo){
		checkNull();
		context.put("subject", documentNo);
	}
	
	public void setDocumentDate(Date date){
		context.put("docDate", date);
	}
	
	public String getDescription(){
		return context.get("description")==null? null: context.get("description").toString();
	}
	
	public String getDocumentNo(){
		return context.get("subject")==null? null: context.get("subject").toString();
	}
	
	public Date getDate(){
		return context.get("docDate")==null? null: (Date)context.get("docDate");
	}
	
	private void checkNull() {
		if(context==null){
			context= new HashMap<String, Object>();
		}
	}
}
