package com.duggan.workflow.shared.model.form;

import java.io.Serializable;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import com.duggan.workflow.shared.model.Value;
import com.wira.commons.shared.models.Listable;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class FormModel implements Serializable, Listable {

	public static final String FORMMODEL = "FORMMODEL";
	public static final String FIELDMODEL = "FIELDMODEL";
	public static final String PROPERTYMODEL = "PROPERTYMODEL";
	
	@XmlTransient
	private Long id;
	private String refId;
	protected String name;
	protected String caption;
	private ArrayList<KeyValuePair> props = new ArrayList<KeyValuePair>();

	public FormModel() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (name != null) {
			this.name = name.trim();
		} else {
			this.name = name;
		}
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	protected String getStringValue(Value value) {
		return value == null ? null : value.getValue() == null ? null : value
				.getValue().toString();
	}

	@Override
	public String getDisplayName() {
		return caption;
	}

	public ArrayList<KeyValuePair> getValues() {
		return props;
	}

	public void setValues(ArrayList<KeyValuePair> values) {
		this.props = values;
	}

	public void addValue(KeyValuePair pair) {
		if (props.contains(pair)) {
			// Replace existing pair
			props.remove(pair);
		}

		if (pair.getValue() == null || pair.getValue().trim().isEmpty()) {
			// Ignore empty
			return;
		}
		props.add(pair);
	}

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public ArrayList<KeyValuePair> getProps() {
		return props;
	}

	public void setProps(ArrayList<KeyValuePair> props) {
		this.props = props;
	}

	public String getProperty(String propertyName) {
		for (KeyValuePair pair : props) {
			if (pair.getKey().equals(propertyName)) {
				return pair.getValue();
			}
		}

		return null;
	}

	@Override
	public boolean equals(Object obj) {
		FormModel other = (FormModel) obj;

		if (refId != null && other.refId != null) {
			//Both are not null
			if (!refId.equals(other.refId)) {
				return false;
			}
		}else if (!(refId == null && other.refId == null)) {
			//Both must be null
			return false;
		}
		
		if (other.props.size() != props.size()) {
			return false;
		}
		
		for(KeyValuePair otherPair: other.props){
			if(props.indexOf(otherPair)==-1){
//				logger.info(getClass()+" Missing KeyValuePair "+otherPair);
				return false;
			}
			
			int idx = props.indexOf(otherPair);
			KeyValuePair pair = props.get(idx);
			if(!pair.getValue().equals(otherPair.getValue())){
//				logger.info(getClass()+" Equality failed: "+pair+" != "+otherPair);
				return false;
			}
		}

//		logger.info(getClass()+" Equal Fields "+getName());
		return true;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
