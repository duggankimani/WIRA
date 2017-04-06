package com.duggan.workflow.server.dao.hibernate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;

import org.hibernate.HibernateException;
import org.json.JSONArray;
import org.json.JSONObject;

public class JsonDocValue extends JsonType{

	@Override
	public Class returnedClass() {
		return DocValues.class;
	}
	
	@Override
	public Object nullSafeGet(ResultSet rs, String[] names, Object owner)
			throws HibernateException, SQLException {
		String colName = names[0];
		final String cellContent = rs.getString(colName);
		if (cellContent == null) {
			return null;
		}
		try {
			
			logger.debug(" - Content col= "+colName+"; json= "+cellContent);
			DocValues values = new DocValues();
			JSONObject obj = new JSONObject(cellContent);
			JSONArray objectNames = obj.names();
			for(int i=0;i<obj.length(); i++){
				String name = objectNames.getString(i); 
				values.add(name, obj.get(name));				
			}
			return values;
		} catch (final Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("Failed to convert String \" "+cellContent+" \" to "
					+ returnedClass() +" "+ ex.getMessage(), ex);
		}
	}

	@Override
	public void nullSafeSet(PreparedStatement ps, Object value, final int idx)
			throws HibernateException, SQLException {
		if (value == null) {
			ps.setNull(idx, Types.OTHER);
			return;
		}
		try {
			Map<String,Object> data = ((DocValues)value).getRawValues();
			JSONObject object = new JSONObject();
			for(String key: data.keySet()){
				object.put(key, data.get(key));
			}

			ps.setObject(idx,object.toString(), Types.OTHER);
		} catch (final Exception ex) {
			throw new RuntimeException("Failed to convert Object to String: "
					+ ex.getMessage(), ex);
		}
	}

	
	
}
