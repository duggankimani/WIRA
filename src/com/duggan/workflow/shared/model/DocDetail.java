package com.duggan.workflow.shared.model;

import java.io.Serializable;
import java.util.List;

/**
 *  
 * @author duggan
 *
 */
public class DocDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long documentId;
	private Long docDetailId;
	private String name;
	private List<Value> values;
	
	public DocDetail(){}

	public Long getDocumentId() {
		return documentId;
	}

	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
	}

	public Long getDocDetailId() {
		return docDetailId;
	}

	public void setDocDetailId(Long docDetailId) {
		this.docDetailId = docDetailId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Value> getValues() {
		return values;
	}

	public void setValues(List<Value> values) {
		this.values = values;
	}

}
