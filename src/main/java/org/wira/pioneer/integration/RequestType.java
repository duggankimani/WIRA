package org.wira.pioneer.integration;


public enum RequestType {
	
	MPESAIPN("MPESAIPN"),
	ALLOCATIONREQUEST("TERMINAL ALLOCATION");
	
	String name;
	private RequestType(String name){
		this.name = name;
	}
	
	public static RequestType getRequestType(String parameter){
		for(RequestType type: RequestType.values()){
			if(type.name.equals(parameter)){
				return type;
			}
		}
		return null;
	}
}
