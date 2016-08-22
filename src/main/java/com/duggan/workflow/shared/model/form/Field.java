package com.duggan.workflow.shared.model.form;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.Value;

/**
 * Represents A Form Field
 * 
 * @author duggan
 *
 */
@XmlSeeAlso({KeyValuePair.class,Property.class})
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Field extends FormModel implements Comparable<Field>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//Field properties - Caption, Length etc
//	@XmlTransient
//	private ArrayList<Property> properties = new ArrayList<Property>();

	//Drop down/ multiselect selection values
	private ArrayList<KeyValuePair> selectionValues = new ArrayList<KeyValuePair>();
	
	//Default field value or current field value
	@XmlTransient
	private Value value;
	
	//Field Data type
	private DataType type;
	
	//Form id
	@XmlTransient
	private Long formId=-1L;
	
	//Index position on the form
	private int position;
	
	//Id of the parent field - for a child field
	@XmlTransient
	private Long parentId;
	
	private String parentRef;
	
	//ArrayList of children fields - for a grid field
	@XmlTransient
	private ArrayList<Field> fields = new ArrayList<Field>();
	
	//Front end grid name for grid children fields
	private transient String gridName="";
	//Front end rowid/rowno for entries in a grid
	private transient Long lineRefId = null;
	//Front end document id for the main doc
	private transient String docId="";
	//Front end document refid
	private transient String docRefId="";
	//Uploaded File ID - Uploaded files on the server side are assigned unique Ids in the current user session. this is the Id of the last uploaded file
	private String uploadedFileId = null;
	
	//Fields wrapped from raw HTML 
	private transient boolean isHTMLWrappedField = false; 
	
	/**
	 * Fields this field depends on. 
	 * <p>
	 * This is used for lookups to register for change events in the parent field
	 * where an sql statement is used in child field.
	 * <p>
	 * e.g select id,name from cities where countryid=@@country  
	 * 
	 */
	private ArrayList<String> dependentFields = new ArrayList<String>();
	
	/**
	 * This field has dependants
	 */
	private boolean isDynamicParent;
	private String formRef;
	
	public Field() {
		docId="TempD";
	}
	
//	public ArrayList<Property> getProperties() {
//		return properties;
//	}
	
	public void setProperties(ArrayList<Property> properties) {
		for(Property property: properties){
			addValue(new KeyValuePair(property.getName(), getStringValue(property.getValue())));
		}
	}
	

	public void addProperty(Property prop) {
		addValue(new KeyValuePair(prop.getName(), getStringValue(prop.getValue())));
	}
	
	public Value getValue() {
		return value;
	}
	public void setValue(Value value) {
		this.value = value;
	}
	public DataType getType() {
		return type;
	}
	public void setType(DataType type) {
		this.type = type;
	}
	public Long getFormId() {
		return formId;
	}
	public void setFormId(Long formId) {
		this.formId = formId;
	}
	
	@Override
	public String toString() {
		String str = "[Id= "+getRefId()+";" +
				" FormId = "+getFormId()+";"+
				" Name = "+getName()+";"+
				" Caption = "+getCaption()+";"+
				" Type = "+type+";" +
				" Position = "+position+"]";
		
		return str;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public ArrayList<KeyValuePair> getSelectionValues() {
		return selectionValues;
	}

	public void setSelectionValues(ArrayList<KeyValuePair> selectionValues) {
		this.selectionValues = selectionValues;
	}

	public ArrayList<Field> getFields() {
		return fields;
	}

	public boolean isDetailField() {
		return type == DataType.GRID;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public void addField(Field field) {
		field.setParentRef(getRefId());
		fields.add(field);
	}

	public boolean contains(Field retrieved) {

		return fields.contains(retrieved);
	}

	public void setFields(ArrayList<Field> fieldList) {
		this.fields = fieldList;
	}
	
	public Field clone(){
		return clone(false);
	}
	
	public Field clone(boolean fullClone){
		Field field = new Field();
		this.copyTo(field, fullClone);
		
		return field;
	}

	public void sortFields(){
		if(fields!=null && fields.size()>1){
			//Grid Field order Fields
			Collections.sort(fields, new Comparator<Field>() {
				@Override
				public int compare(Field o1, Field o2) {
					int i = o1.getPosition();
					int j = o2.getPosition();
					if(i<j)
						return -1;
					
					if(i==j)
						return 0;
					
					return 1;
					
				}
				
			});
		}
	}

	public String getPropertyValue(String key) {
		return getProperty(key);
	}
	
	public Long getLastValueId(){
		if(value!=null){
			return value.getId();
		}
		
		return null;
	}

	public String getDocSpecificName(){
		return (gridName.isEmpty()? "" : gridName+"_")+name+getSuffix(docId);
	}
	
	public static String getSuffix(String documentId){
		return documentId+"D";//Delimited with a D
	}
	
	public String getQualifiedName() {
		if(lineRefId!=null){
			return getDocSpecificName()+getSeparator()+lineRefId;
		}
		return getDocSpecificName();
	}
	
//	public static String getQualifiedName(String documentId, String fieldName){
//		return fieldName+getSuffix(documentId)+getSeparator();
//	}
	
	public Long getLineRefId() {
		return lineRefId;
	}

	public void setLineRefId(Long lineRefId) {
		this.lineRefId = lineRefId;
	}
	
	/**
	 * Aggregatable field
	 * 
	 * @return
	 */
	public boolean isAggregate(){
		return this.parentId!=null;
	}

	public static String getSeparator() {
		
		return "";
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
		
		if(fields!=null){
			for(Field field: fields){
				field.setDocId(docId);
				field.setDocRefId(docRefId);
			}
		}
	}

	public String getGridName() {
		return gridName;
	}

	public void setGridName(String gridName) {
		//this.gridName = gridName;
	}

	public String getDocRefId() {
		return docRefId;
	}

	public void setDocRefId(String docRefId) {
		this.docRefId = docRefId;
	}

	@Override
	public int compareTo(Field o) {
		return getDisplayName().compareTo(o.getDisplayName());
	}

	public void copyTo(Field field, boolean copyAll) {
		field.setCaption(caption);
		field.setName(name);
		field.setPosition(position);
		field.setType(type);
		field.setForm(null,null);
		field.setRefId(null);
		
		if(copyAll){
			field.setForm(formId,formRef);
			field.setRefId(getRefId());
			field.setId(getId());
			field.setParent(parentId,getParentRef());
			field.setDocId(docId);
			field.setDocRefId(docRefId);
		}
	
		if(value!=null){
			field.setValue(value.clone(copyAll));
		}
		
		field.setSelectionValues(getSelectionValues());
		
		for(Field fld: fields){
			field.addField(fld.clone());
		}
		
		field.setProps(getProps());
	}

	public ArrayList<String> getDependentFields() {
		return dependentFields;
	}

	public void setDependentFields(ArrayList<String> dependentFields) {
		this.dependentFields = dependentFields;
	}

	public boolean isDynamicParent() {
		return isDynamicParent;
	}

	public void setDynamicParent(boolean isDynamicParent) {
		this.isDynamicParent = isDynamicParent;
	}

	public String getUploadedFileId() {
		return uploadedFileId;
	}

	public void setUploadedFileId(String uploadedFileId) {
		this.uploadedFileId = uploadedFileId;
	}

	public boolean isHTMLWrappedField() {
		return isHTMLWrappedField;
	}

	public void setHTMLWrappedField(boolean isHTMLWrappedField) {
		this.isHTMLWrappedField = isHTMLWrappedField;
	}

	public String getParentRef() {
		return parentRef;
	}

	public void setParentRef(String parentRef) {
		this.parentRef = parentRef;
	}

	public void setParent(Long id, String parentRef) {
		setParentId(id);
		setParentRef(parentRef);
	}

	public void setForm(Long formId, String formRef) {
		setFormId(formId);
		this.formRef = formRef;
		
	}

	public String getFormRef() {
		return formRef;
	}

	public void setFormRef(String formRef) {
		this.formRef = formRef;
	}
}
