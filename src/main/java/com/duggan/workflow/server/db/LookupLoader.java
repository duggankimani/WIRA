package com.duggan.workflow.server.db;

import java.util.List;

import com.duggan.workflow.shared.model.form.KeyValuePair;

/**
 * Datasources - specifically for dropdown lists that need to
 * read data from external systems 
 * 
 * @author duggan
 *
 */
public interface LookupLoader {
	
	public List<KeyValuePair> getValuesByJNDIName(String jndiName, String sql);
	
	public List<KeyValuePair> getValuesByDataSourceName(String dataSourceName, String sql);

}
