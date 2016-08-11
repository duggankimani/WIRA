package com.duggan.workflow.shared.model.form;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.Value;

@XmlRootElement(name="property")
@XmlAccessorType(XmlAccessType.FIELD)
public class Property extends FormModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DataType type;
	private String fieldRefId;//field property
	private String formRefId; //form property
	@XmlTransient
	private Value value;
	private String caption;
	private transient boolean showInPropertyPanel = true;
	private ArrayList<KeyValuePair> kvp= new ArrayList<KeyValuePair>();
	
	public Property(){
	}

	public Property(String name, String caption, DataType type) {
		this.name = name;
		this.caption = caption;
		this.type = type;
	}
	
	public Property(String name, String caption, DataType type,KeyValuePair... pairs) {
		this.name = name;
		this.caption = caption;
		this.type = type;
		
		for(KeyValuePair p: pairs){
			this.kvp.add(p);
		}
	}
	
	public void addSelectionItem(KeyValuePair pair){
		this.kvp.add(pair);
	}
	
	public Property(String name, String caption, DataType type, String fieldRefId){
		this(name, caption, type);
		this.fieldRefId = fieldRefId;
	}
	
	

	public DataType getType() {
		return type;
	}

	public void setType(DataType type) {
		this.type = type;
	}

	public String getFieldRefId() {
		return fieldRefId;
	}

	public void setFieldRefId(String fieldRefId) {
		this.fieldRefId = fieldRefId;
	}

	public String getFormRefId() {
		return formRefId;
	}

	public void setFormRefId(String formRefId) {
		this.formRefId = formRefId;
	}

	public Value getValue() {
		return value;
	}

	public void setValue(Value value) {
		this.value = value;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}
	
	@Override
	public boolean equals(Object obj) {
		Property other = (Property)obj;
		
		return this.name.equals(other.name);
	}
	
	public void setSelectionValues(ArrayList<KeyValuePair> pairs){
		if(pairs!=null){
			kvp.clear();
			kvp.addAll(pairs);
		}
	}
	
	public ArrayList<KeyValuePair> getSelectionValues(){
		return kvp;
	}
	
	@Override
	public String toString() {
		return "{id="+getRefId()+
				";Name="+getName()+
				";Value="+(getValue()==null? null: getValue().getValue())+
				"}"
				;
	}
	
	public void sort(){
		Collections.sort(kvp, new Comparator<KeyValuePair>() {
			public int compare(KeyValuePair o1, KeyValuePair o2) {
				String a = o1.getValue();
				String b = o2.getValue();
				
				return a.compareTo(b);
			};
		});
	}
	
	public Property clone(boolean fullClone){
		Property prop = new Property(name, caption, type);
		prop.setSelectionValues(getSelectionValues());
		prop.setType(type);
		if(fullClone){
			prop.setRefId(getRefId());
		}
		
		if(value!=null)
			prop.setValue(value.clone(fullClone));
		
		return prop;
	}

	public boolean isShowInPropertyPanel() {
		return showInPropertyPanel;
	}

	public void setShowInPropertyPanel(boolean showInPropertyPanel) {
		this.showInPropertyPanel = showInPropertyPanel;
	}
	
}
