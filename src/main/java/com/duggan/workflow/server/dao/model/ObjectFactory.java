package com.duggan.workflow.server.dao.model;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory {

	private final static QName QName_ADFIELD_QNAME = new QName("www.wira.io/form","field");
	
	public ObjectFactory() {
	}
	
	public static ADField createADField(){
		return new ADField();
	}
	
	@XmlElementDecl(namespace="www.wira.io/form",name="field")
	public static JAXBElement<ADField> createField(ADField value){
		return new JAXBElement<ADField>(QName_ADFIELD_QNAME, ADField.class, null, value);
	}
	
}
