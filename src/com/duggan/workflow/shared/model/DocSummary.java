package com.duggan.workflow.shared.model;

import java.io.Serializable;
import java.util.Date;

public abstract class DocSummary implements Serializable,Comparable<DocSummary>{

	/**
	 * 
	 */
	protected static final long serialVersionUID = 1L;


	public abstract String getSubject();

	public abstract String getDescription();

	public abstract Date getCreated();

	public abstract Date getDocumentDate();
	
	public abstract Integer getPriority();

	public abstract Object getId();
	
	/**
	 * Sorts document/task elements in descending order
	 * hence the negative sign (-)
	 */
	@Override
	public int compareTo(DocSummary o) {
		
		return - getCreated().compareTo(o.getCreated());
	}

}
