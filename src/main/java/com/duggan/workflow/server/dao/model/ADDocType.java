package com.duggan.workflow.server.dao.model;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

@XmlSeeAlso(ADProcessCategory.class)
@XmlRootElement(name="doctype")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class ADDocType extends PO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@XmlTransient
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@XmlAttribute
	@Column(unique=true)
	private String name;
	
	@XmlAttribute
	private String display;
	
	@XmlAttribute
	private String className;
	
	@XmlTransient
	@OneToMany(fetch=FetchType.LAZY, mappedBy="type")
	private Collection<DocumentModel> documents = new HashSet<>();
	
	@XmlTransient
	@ManyToOne(fetch=FetchType.LAZY, cascade={CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name="processDefId", referencedColumnName="id")
	private ProcessDefModel processDef;
	
	//last assigned number
	//add 1 to this to get the next num
	@XmlTransient
	private Integer lastNum;
	
	@XmlAttribute
	private String subjectFormat;
	
	@ManyToOne
	@JoinColumn(name="categoryId")
	private ADProcessCategory category;
	
	public ADDocType() {
	}
	
	public ADDocType(String refId, String name, String display){
		this.refId = refId;
		if(name!=null)
			name = name.toUpperCase();
		
		this.name = name;
		this.display= display;
	}
	
	public ADDocType(String refId, String name, String display, String className){
		this.refId = refId;
		if(name!=null)
			name = name.toUpperCase();
		
		this.name = name;
		this.display= display;
		this.className = className;
	}
	
	public ADDocType(String refId, String name, String display, String className, ADProcessCategory category){
		this(refId,name,display,className);
		this.category = category;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		if(name!=null)
			name = name.toUpperCase();
		
		this.name = name;
	}
	public String getDisplay() {
		return display;
	}
	public void setDisplay(String display) {
		this.display = display;
	}
	
	public String getClassName() {
		return className;
	}
	/**
	 * Sets the classNames for a particular Document Type. The icon to be displayed;
	 * @param className
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	public ProcessDefModel getProcessDef() {
		return processDef;
	}

	public void setProcessDef(ProcessDefModel processDef) {
		this.processDef = processDef;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof ADDocType))
			return false;
		
		ADDocType other = (ADDocType)obj;
		
		if(id==null ^ other.id==null)
			return false;
		
		if(id!=null){
			return id.equals(other.id);
		}
		
		if(name==null ^ other.name==null){
			return false;
		}
		
		if(name!=null)
			if(!name.equals(other.name)){
				return false;
			}
		
		
		return true;
	}
	
	
	@Override
	public int hashCode() {
		int hashcode =7;
		
		if(id!=null){
			hashcode += hashcode+id.hashCode();
		}
		
		if(name!=null){
			hashcode += 17+name.hashCode();
		}
		
		if(hashcode!=7){
			return hashcode;
		}
		
		return super.hashCode();
	}

	public Integer getLastNum() {
		return lastNum;
	}

	public void setLastNum(Integer lastNum) {
		this.lastNum = lastNum;
	}

	public String getSubjectFormat() {
		return subjectFormat;
	}

	public void setSubjectFormat(String subjectFormat) {
		this.subjectFormat = subjectFormat;
	}

	public ADProcessCategory getCategory() {
		return category;
	}

	public void setCategory(ADProcessCategory category) {
		this.category = category;
	}
}
