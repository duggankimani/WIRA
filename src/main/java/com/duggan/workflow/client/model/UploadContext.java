package com.duggan.workflow.client.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import com.wira.commons.client.util.ArrayUtil;

/**
 * 
 * @author duggan
 *
 */
public class UploadContext {

	private String url="/upload";
	public static final String ACTION="ACTION"; 
	private ArrayList<String> acceptsDocTypes=new ArrayList<String>();
	
	private HashMap<String,String> context = new HashMap<String, String>();
	
	public enum UPLOADACTION{
		ATTACHDOCUMENT,
		UPLOADBPMNPROCESS,
		UPLOADCHANGESET,
		IMPORTFORM, 
		UPLOADUSERIMAGE,
		UPLOADLOGO, UPLOADDOCFILE, UPLOADOUTPUTDOC, GENERATEOUTPUT,
		IMPORTGRIDDATA, IMPORTCSV, IMPORTPROCESS
	}
	
	public UploadContext(){
		this.url="upload";
	}
	
	public UploadContext(String url){
		this.url=url;
	}
	
	public String getUrl(){
		
		if(url==null || url.trim().length()<1 ){
			return null;
		}
		
		if(url.startsWith("/")){
			//ignore forward slash
			return url.substring(1, url.length());
		}
		
		return url.trim();
	}
	
	public UploadContext setContext(String key, String value){
		context.put(key, value);
		return this;
	}
	
	public UploadContext setAction(UPLOADACTION action){
		context.put(ACTION, action.name());
		return this;
	}
	
	public UPLOADACTION getAction(){
		return context.get(ACTION)==null? null : UPLOADACTION.valueOf(context.get(ACTION));
	}
	
	public String toUrl(){
		return url+"?"+getContextValuesAsURLParams();
	}
	/**
	 * This method converts all values in context into
	 * url parameters format i.e key=value&key2=value&key3=value etc
	 *  
	 * @return
	 */
	public String getContextValuesAsURLParams(){
		Set<String> keys = context.keySet();
		StringBuffer params = new StringBuffer();
		for(String key: keys){
			params.append(key+"="+context.get(key));
			params.append("&");
		}
		
		if(params.length()>0){
			params.replace(params.length()-1, params.length(), "");
		}
		return params.toString();
	}	
	
	/**
	 * 
	 * Comma separated Acceptable file extensions 
	 * 
	 * @see UploadContext#setAccept(ArrayList)
	 * 
	 * @param docTypes
	 */
	@Deprecated
	public void setAccept(String commaSeparatedDocTypes){
		acceptsDocTypes.clear();
		if(commaSeparatedDocTypes==null){			
			return;
		}
		
		String[] types= commaSeparatedDocTypes.split(",");
		acceptsDocTypes.addAll(ArrayUtil.asList(types));
	}
	
	public void setAccept(ArrayList<String> accept){
		this.acceptsDocTypes = accept;
	}
	
	/**
	 * Returns Comma separated Acceptable file extensions
	 * 
	 * @return Acceptable File Extensions
	 */
	public String[] getAcceptTypes(){
		Object[] types = acceptsDocTypes.toArray();
		String[] rtn = new String[types.length];
		for(int i=0; i<types.length; i++){
			rtn[i] = types[i].toString();
		}
		
		return rtn ;
	}
	
	public String getContext(String key){
		return context.get(key);
	}
}
