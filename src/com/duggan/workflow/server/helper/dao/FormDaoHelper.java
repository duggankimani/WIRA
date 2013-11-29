package com.duggan.workflow.server.helper.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.duggan.workflow.server.dao.FormDaoImpl;
import com.duggan.workflow.server.dao.model.ADField;
import com.duggan.workflow.server.dao.model.ADForm;
import com.duggan.workflow.server.dao.model.ADKeyValuePair;
import com.duggan.workflow.server.dao.model.ADProperty;
import com.duggan.workflow.server.dao.model.ADValue;
import com.duggan.workflow.server.dao.model.HasProperties;
import com.duggan.workflow.server.dao.model.PO;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.shared.model.BooleanValue;
import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.DateValue;
import com.duggan.workflow.shared.model.DoubleValue;
import com.duggan.workflow.shared.model.LongValue;
import com.duggan.workflow.shared.model.StringValue;
import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.model.form.Field;
import com.duggan.workflow.shared.model.form.Form;
import com.duggan.workflow.shared.model.form.FormModel;
import com.duggan.workflow.shared.model.form.KeyValuePair;
import com.duggan.workflow.shared.model.form.Property;

public class FormDaoHelper {

	/**
	 * 
	 * @return Forms in the detabase (Only general form details are included - no fields & Form properties are included)
	 */
	public static List<Form> getForms(){
		
		FormDaoImpl dao = DB.getFormDao();		
		List<ADForm> adforms = dao.getAllForms();
		
		
		List<Form> forms = new ArrayList<>();
		for(ADForm adform: adforms){
			forms.add(getForm(adform, false));
		}
		
		return forms;
	}
	
	/**
	 * Returns a Form based on ID - with all form details included
	 * @param id
	 * @return
	 */
	public static Form getForm(Long id, boolean loadDetails){
		FormDaoImpl dao = DB.getFormDao();
		
		ADForm adform = dao.getForm(id);
		Form form = getForm(adform, loadDetails);
		
		return form;
	}
	
	private static Form getForm(ADForm adform, boolean loadFields) {
		if(adform==null){
			return null;
		}
		
		Form form = new Form();
		form.setCaption(adform.getCaption());
		
		if(loadFields)
			form.setFields(getFields(adform.getFields()));
		
		form.setId(adform.getId());
		form.setName(adform.getName());
		form.setProperties(getProperties(adform.getProperties()));
				
		return form;
	}

	/**
	 * Retrieves a field and field value
	 * 
	 * @param id
	 * @return
	 */
	public static Field getField(Long id){
		FormDaoImpl dao = DB.getFormDao();
		ADField adfield = dao.getField(id);
		
		Field field = getField(adfield);
		
		return field;
	}
	
	private static Field getField(ADField adfield) {
		Field field = new Field();
		field.setCaption(adfield.getCaption());
		field.setFormId(adfield.getForm()==null? null : adfield.getForm().getId());
		field.setId(adfield.getId());
		field.setName(adfield.getName());
		field.setProperties(getProperties(adfield.getProperties()));
		field.setType(adfield.getType());
		field.setValue(getValue(adfield.getValue(), adfield.getType()));
		field.setPosition(adfield.getPosition()==null? 0: adfield.getPosition().intValue());
		
		if(field.getType().isDropdown()){
			String type = null;
			
			for(Property prop: field.getProperties()){
				if(prop.getName().equals("SELECTIONTYPE")){
					Object value = prop.getValue()==null? null : prop.getValue().getValue();
					type = value==null? null : value.toString();
				}
			}
			
			if(type!=null){
				field.setSelectionValues(getDropdownValues(type));
			}
		}
		
		return field;
	}

	public static Collection<? extends FormModel> getFields(Long parentId,
			boolean loadDetailsToo) {
		FormDaoImpl dao = DB.getFormDao();
		List<ADField> adfields = dao.getFields(parentId);
		
		return getFields(adfields);
	}
	
	private static List<Field> getFields(Collection<ADField> adfields) {
		List<Field> fields = new ArrayList<>();
		
		if(adfields!=null){
			for(ADField adfield: adfields){
				fields.add(getField(adfield));
			}
		}
				
		return fields;
	}


	/**
	 * Retrieves a Property and its value
	 * @param id
	 * @return
	 */
	public static Property getProperty(Long id){
		FormDaoImpl dao = DB.getFormDao();
		ADProperty adproperty = dao.getProperty(id);
		Property property = getProperty(adproperty);
		
		return property;
	}
	
	private static Property getProperty(ADProperty adproperty) {
		
		if(adproperty==null)
			return null;
		
		Property property = new Property();
		property.setCaption(adproperty.getCaption());
		
		ADField field = adproperty.getField();
		property.setFieldId(field==null? null: field.getId());
		
		ADForm form = adproperty.getForm();		
		property.setFormId(form==null? null: form.getId());
		
		assert form==null ^ field==null; //XOR
		
		property.setId(adproperty.getId());
		property.setName(adproperty.getName());
		property.setType(adproperty.getType());
		
		property.setValue(getValue(adproperty.getValue(), adproperty.getType()));
		
		return property;
	}
	
	private static List<Property> getProperties(Collection<ADProperty> properties) {
		List<Property> props = new ArrayList<>();
		if(properties==null)
			return props;
		
		for(ADProperty property: properties){
			props.add(getProperty(property));
		}
		
		return props;
	}

	public static Value getValue(ADValue advalue, DataType type) {
		if(advalue==null)
			return null;
		
		if(type==null)
			throw new IllegalArgumentException("FormDaoHelper.GetValue: Datatype must be provided");
		
		Value value = null;
		
		Long id = advalue.getId();
		String key = advalue.getFieldName();
		
		switch (type) {
		case BOOLEAN:
			value = new BooleanValue(id, key, advalue.getBooleanValue());
			break;
			
		case DATE:
			value = new DateValue(id, key, advalue.getDateValue());
			break;
			
		case DOUBLE:
			value =new DoubleValue(id, key, advalue.getDoubleValue());
			break;
			
		case INTEGER:
			value = new LongValue(id, key, advalue.getLongValue());
			break;
			
		case STRING:
			value = new StringValue(id, key, advalue.getStringValue());
			break;

		case CHECKBOX:
			value = new BooleanValue(id, key, advalue.getBooleanValue());
			break;

			
		case MULTIBUTTON:
			value = new StringValue(id, key, advalue.getStringValue());
			break;

			
		case SELECTBASIC:
			value = new StringValue(id, key, advalue.getStringValue());
			break;

			
		case SELECTMULTIPLE:
			value = new StringValue(id, key, advalue.getStringValue());
			break;

			
		case STRINGLONG:
			value = new StringValue(id, key, advalue.getStringValue());
			break;
		}
		
		
		return value;
	}

	public static void deleteValue(Long valueId){
		DB.getFormDao().deleteValue(valueId);
	}
	
	
	public static Form createForm(Form form, Boolean reloadAll){
		
		FormDaoImpl dao = DB.getFormDao();
		
		ADForm adform = new ADForm();
		adform.setCaption(form.getCaption());		
		getADFields(form.getFields(), adform);
		adform.setId(form.getId());
		adform.setName(form.getName());		
		getADProperties(form.getProperties(), adform);
		//adform.setProperties(properties);
		dao.save(adform);
		
		assert adform.getId()!=null;
		
		return getForm(adform, reloadAll); 
	}
	
	
	private static void getADFields(List<Field> fields, ADForm adform) {
		
		if(fields==null){
			return;
		}
		
		for(Field field:fields){
			adform.addField(getField(field));
		}
	}

	private static ADField getField(Field field) {

		FormDaoImpl dao = DB.getFormDao();
		
		ADField adfield = new ADField();
		int previous=-1;
		
		if(field.getId()!=null){
			adfield = dao.getField(field.getId());
			previous = adfield.getPosition()==null? -1 : adfield.getPosition();
		}
		
		adfield.setCaption(field.getCaption());
		adfield.setName(field.getName());
	
		if(field.getFormId()!=null)
			adfield.setForm(dao.getForm(field.getFormId()));
		
		adfield.setId(field.getId());
		
		//copy
		getADProperties(field.getProperties(), adfield);
		
		adfield.setType(field.getType());
		adfield.setValue(getValue(adfield.getValue(),field.getValue()));
		
		dao.setPosition(adfield, previous, field.getPosition());
		
		return adfield;
	}

	public static Field createField(Field field){
		FormDaoImpl dao = DB.getFormDao();
		
		ADField adfield = getField(field);
		
		dao.save(adfield);
		
		return getField(adfield);
	}

	private static void getADProperties(List<Property> propertiesFrom,
			HasProperties formComponent) {

		if(propertiesFrom!=null){
			for(Property prop: propertiesFrom){
				formComponent.addProperty(get(prop));
			}
		}
	}

	public static Property createProperty(Property property){
		FormDaoImpl dao = DB.getFormDao();
		
		ADProperty adprop = get(property);
		
		dao.save(adprop);
		
		return getProperty(adprop);
	}
	
	private static ADProperty get(Property property) {
		FormDaoImpl dao = DB.getFormDao();
		
		ADProperty adprop = new ADProperty();
		adprop.setCaption(property.getCaption());
		
		if(property.getId()!=null){
			adprop = dao.getProperty(property.getId());
		}
		
		if(property.getFieldId()!=null)
		adprop.setField(dao.getField(property.getFieldId()));
		
		if(property.getFormId()!=null)
			adprop.setForm(dao.getForm(property.getFormId()));
		
		adprop.setId(property.getId());
		adprop.setName(property.getName());
		adprop.setType(property.getType());
		
		adprop.setValue(getValue(adprop.getValue(), property.getValue()));
		
		return adprop;
	}
	
	public static Value createValueForField(Long fieldId, Value value){
		FormDaoImpl dao = DB.getFormDao();
		ADField field = dao.getField(fieldId);
		
		ADValue advalue = getValue(field.getValue(),value);
		field.setValue(advalue);
				
		dao.save(advalue);
		return getValue(dao.getValue(advalue.getId()), value.getDataType());
	}
	
	public static Value createValueForProperty(Long propertyId, Value value){
		FormDaoImpl dao = DB.getFormDao();
		ADProperty prop = dao.getProperty(propertyId);
		
		ADValue advalue = getValue(prop.getValue(), value);
		advalue.setProperty(prop);
		
		dao.save(advalue);
		return getValue(dao.getValue(advalue.getId()), value.getDataType());
	}

	public static ADValue getValue(ADValue previousValue, Value value) {
		if(value==null)
			return null;
		
		ADValue advalue = new ADValue();
		if(previousValue!=null){
			advalue = previousValue;
		}else{
			advalue.setId(value.getId());
		}
		
		advalue.setFieldName(value.getKey());
		if(value.getValue()==null)
			return advalue;
		
		switch (value.getDataType()) {
		case BOOLEAN:
			advalue.setBooleanValue((Boolean)value.getValue());
			break;
			
		case DATE:
			advalue.setDateValue((Date)value.getValue());
			break;
			
		case DOUBLE:
			advalue.setDoubleValue((Double)value.getValue());
			break;
			
		case INTEGER:
			if(value.getValue() instanceof Integer){
				advalue.setLongValue(new Long((Integer)value.getValue()));
			}else{
				advalue.setLongValue((Long)value.getValue());
			}
			
			break;
			
		case STRING:
			advalue.setStringValue((String)value.getValue());			
			break;
			
		case STRINGLONG:
			advalue.setStringValue((String)value.getValue());			
			break;
			
		case CHECKBOX:
			advalue.setBooleanValue((Boolean)value.getValue());			
			break;
			
		case MULTIBUTTON:
			advalue.setStringValue((String)value.getValue());			
			break;
			
		case SELECTBASIC:
			advalue.setStringValue((String)value.getValue());			
			break;
			
		case SELECTMULTIPLE:
			advalue.setStringValue((String)value.getValue());			
			break;
		}
		
		return advalue;
	}

	public static void delete(FormModel model) {
		
		FormDaoImpl dao = DB.getFormDao();
		PO po=null;
		if(model instanceof Form){
			po = dao.getForm(model.getId());
		}
		
		if(model instanceof Field){
			po = dao.getField(model.getId());
			
		}
		
		if(model instanceof Property){
			po = dao.getProperty(model.getId());
		}
		
		if(po!=null)
			dao.delete(po);
	}
	
	public static List<KeyValuePair> getDropdownValues(String type){
		FormDaoImpl dao = DB.getFormDao();
		List<ADKeyValuePair> adkeyvaluepairs = dao.getKeyValuePairs(type);
		
		List<KeyValuePair> keyvaluepairs = new ArrayList<>();
		for(ADKeyValuePair adpair: adkeyvaluepairs){
			KeyValuePair kvp = new KeyValuePair();
			kvp.setKey(adpair.getName());
			kvp.setValue(adpair.getDisplayValue());
			
			keyvaluepairs.add(kvp);
		}
		
		return keyvaluepairs;
	}

	public static FormModel getFormByName(String formName) {
		if(formName==null){
			return null;
		}
		Long formId = DB.getFormDao().getFormByName(formName);
		
		if(formId==null){
			return null;
		}		
		return getForm(formId, true);
	}

	public void save(String selectionKey,List<KeyValuePair> keyValuePairs){
		FormDaoImpl dao = DB.getFormDao();
		List<ADKeyValuePair> adkeyvaluepairs = dao.getKeyValuePairs(selectionKey);//previous set
		
		if(adkeyvaluepairs!=null)
		for(ADKeyValuePair kvp : adkeyvaluepairs){
			dao.delete(kvp);
		}
		
		if(keyValuePairs!=null)
		for(KeyValuePair keyValuePair: keyValuePairs){
			ADKeyValuePair pair = new ADKeyValuePair();
			pair.setReferenceType(selectionKey);
			pair.setDisplayValue(keyValuePair.getDisplayName());
			pair.setName(keyValuePair.getName());
			dao.save(pair);
		}
	}
}
