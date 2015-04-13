package com.duggan.workflow.shared.model;

import java.util.ArrayList;
import java.util.Collection;

public class GridValue implements Value {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Collection<DocumentLine> value = new ArrayList<DocumentLine>();
	private String key;
	private Long id;

	public GridValue() {

	}

	@Override
	public DataType getDataType() {
		return DataType.GRID;
	}

	public Collection<DocumentLine> getValue() {
		return value;
	}

	public void setCollectionValue(Collection<DocumentLine> value) {
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setValue(Object value) {
		if (value != null)
			setCollectionValue((Collection<DocumentLine>) value);
	}

	public GridValue clone(boolean fullClone) {
		GridValue gvalue = new GridValue();
		// gvalue.setId(id);
		gvalue.setKey(key);
		if (fullClone) {
			gvalue.setId(id);
		}

		Collection<DocumentLine> linez = new ArrayList<DocumentLine>();

		for (DocumentLine line : value) {
			linez.add(line.clone(fullClone));
		}
		gvalue.setCollectionValue(linez);

		return gvalue;
	}

	@Override
	public String toString() {
		String s = "{name:" + key + ", lineId:" + getId() + ", [";

		for (DocumentLine v : value) {
			if (v != null) {
				s = s.concat(v.toString());
			}
		}

		return s.concat("]}\n");
	}
}
