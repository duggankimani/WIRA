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
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(indexes={@Index(name="idx_ref_id",columnList="refId")})
public class DetailModel extends PO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable=false)
	private String name;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="documentid", referencedColumnName="id")
	private DocumentModel document;
	
	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="detail")
	private Collection<ADValue> values = new HashSet<>();

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
		this.name = name;
	}

	public DocumentModel getDocument() {
		return document;
	}

	public void setDocument(DocumentModel document) {
		this.document = document;
	}

	public Collection<ADValue> getValues() {
		return values;
	}
	
	public void addValue(ADValue value){
		values.add(value);
		value.setDetail(this);
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if(!(obj instanceof DetailModel)){
			return false;
		}
		
		DetailModel other = (DetailModel)obj;
		
		if(id==null ^ other.id==null){
			return false;
		}
		
		if(id!=null){
			return id.equals(other.id);
		}
		
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		int hashcode=7;
		
		if(id!=null){
			hashcode += 7* id.hashCode(); 
		}
		
		return hashcode;
	}
	
}
