package com.duggan.workflow.server.dao;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.impl.SessionImpl;

import com.duggan.workflow.server.dao.hibernate.JsonType;
import com.duggan.workflow.server.dao.model.PO;
import com.sun.jersey.api.json.JSONUnmarshaller;

public class BaseDaoImpl {

	protected Logger logger = Logger.getLogger(getClass());

	protected EntityManager em;

	public BaseDaoImpl(EntityManager em) {
		this.em = em;
	}

	public EntityManager getEntityManager() {
		return em;
	}

	public void save(PO po) {
		em.persist(po);
	}

	public void delete(PO po) {
		em.remove(po);
	}

	@SuppressWarnings("unchecked")
	public <T> T getSingleResultOrNull(Query query) {
		T value = null;
		try {
			value = (T) query.getSingleResult();
		} catch (Exception e) {
			if (!(e instanceof NoResultException)) {
				e.printStackTrace();
			}

		}

		return value;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> getResultList(Query query, Integer offSet, Integer limit) {
		List<T> values = null;

		if (limit == null || offSet == null) {
			values = query.getResultList();
		} else {
			values = query.setFirstResult(offSet).setMaxResults(limit)
					.getResultList();
		}

		return values;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> getResultList(Query query) {
		List<T> values = null;
		values = query.getResultList();
		return values;
	}

	public <T> T findByRef(Class<?> clazz, String refId,
			boolean throwExceptionIfNull) {
		T po = getSingleResultOrNull(em.createQuery(
				"from " + clazz.getName() + " u where u.refId=:refId")
				.setParameter("refId", refId));

		return po;
	}

	public <T> T findByRefId(String refId, Class<?> clazz) {
		return findByRefId(refId, clazz, true);
	}

	public <T> T findByRefId(String refId, Class<?> clazz,
			boolean throwExceptionIfNull) {
		return findByRefId(refId, clazz, new HashMap<String, Object>(),
				throwExceptionIfNull);
	}

	public <T> T findByRefId(String refId, Class<?> clazz,
			Map<String, Object> params, boolean throwExceptionIfNull) {

		StringBuffer buff = new StringBuffer("from " + clazz.getName()
				+ " c where c.refId=:refId");

		// Variables
		if (params != null) {
			for (String key : params.keySet()) {
				buff.append(" and " + key + "=:" + key);
			}
		}
		Query query = em.createQuery(buff.toString()).setParameter("refId",
				refId);
		// Params
		if (params != null) {
			for (String key : params.keySet()) {
				query.setParameter(key, params.get(key));
			}
		}

		T rtn = getSingleResultOrNull(query);

		return rtn;
	}

	public <T> T getById(Class<T> clazz, long id) {

		return em.find(clazz, id);
	}

	public <T> T getSingleResultJson(String sql,
			Map<String, String> parameters, Class<T> clazz) {

		if (parameters != null) {
			for (String key : parameters.keySet()) {
				if (key.startsWith(":")) {
					sql = sql.replace(key, parameters.get(key));
				} else {
					sql = sql.replace(":" + key, parameters.get(key));
				}
			}
		}
		
		boolean includeId = false;
		if(!(clazz.equals(String.class)) && !sql.contains(" id ")){
			sql = sql.trim();
			//generate select id,field from adjsonfield where ... 
			sql = "select id,"+sql.substring(6);
			includeId=true;
		}

		
		logger.info("GetSingleResultJson Query = " + sql);
		Session session = (Session) getEntityManager().getDelegate();
		Connection connection = ((SessionImpl) session).getJDBCContext()
				.getConnectionManager().getConnection();

		T value = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {
				int col= 1;
				
				Long id = null;
				if(includeId){
					id = rs.getLong(col++);
				}
				String jsonContent = rs.getString(col++);

				JSONUnmarshaller unmarshaller = JsonType.getJaxbContext()
						.createJSONUnmarshaller();
				value = unmarshaller
						.unmarshalFromJSON(
								new ByteArrayInputStream(jsonContent
										.getBytes("UTF-8")), clazz);
				//Set ID
				if(includeId){
					clazz.getMethod("setId", Long.class).invoke(value, id);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			close(rs);
			close(ps);
		}

		return value;
	}

	public <T> List<T> getResultListJson(String sql,
			Map<String, String> parameters, Class<T> clazz) {

		if (parameters != null) {
			for (String key : parameters.keySet()) {
				if (key.startsWith(":")) {
					sql = sql.replace(key, parameters.get(key));
				} else {
					sql = sql.replace(":" + key, parameters.get(key));
				}
			}
		}
		
		boolean includeId = false;
		if(!(clazz.equals(String.class)) && !sql.contains(" id ")){
			sql = sql.trim();
			//generate select id,field from adjsonfield where ... 
			sql = "select id,"+sql.substring(6);
			includeId=true;
		}

		logger.info("GetResultListJson Query = " + sql);
		Session session = (Session) getEntityManager().getDelegate();
		Connection connection = ((SessionImpl) session).getJDBCContext()
				.getConnectionManager().getConnection();

		ArrayList<T> values = new ArrayList<T>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				int col= 1;
				
				Long id = null;
				if(includeId){
					id = rs.getLong(col++);
				}
				
				String jsonContent = rs.getString(col++);

				if (clazz.equals(String.class)) {
					values.add((T) jsonContent);
				} else {
					// Assume Json Class
					JSONUnmarshaller unmarshaller = JsonType.getJaxbContext()
							.createJSONUnmarshaller();
					T value = unmarshaller.unmarshalFromJSON(
							new ByteArrayInputStream(jsonContent
									.getBytes("UTF-8")), clazz);
					
					//Set ID
					if(includeId){
						clazz.getMethod("setId", Long.class).invoke(value, id);
					}
					
					values.add(value);
				}

			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			close(rs);
			close(ps);
		}

		return values;

	}

	protected int executeJsonUpdate(String sql, Map<String, String> parameters) {
		if (parameters != null) {
			for (String key : parameters.keySet()) {
				if (key.startsWith(":")) {
					sql = sql.replace(key, parameters.get(key));
				} else {
					sql = sql.replace(":" + key, parameters.get(key));
				}
			}
		}

		logger.info("ExecuteJsonUpdate Query = " + sql);
		Session session = (Session) getEntityManager().getDelegate();
		Connection connection = ((SessionImpl) session).getJDBCContext()
				.getConnectionManager().getConnection();

		PreparedStatement ps = null;
		int count = 0;
		try {
			ps = connection.prepareStatement(sql);
			count = ps.executeUpdate();

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			close(ps);
		}

		return count;
	}

	private void close(PreparedStatement ps) {
		if (ps == null) {
			return;
		}

		try {
			ps.close();
		} catch (Exception e) {
			logger.warn("Failed to close PreparedStatement cause: "
					+ e.getMessage());
		}
	}

	private void close(ResultSet rs) {
		if (rs == null) {
			return;
		}

		try {
			rs.close();
		} catch (Exception e) {
			logger.warn("Failed to close PreparedStatement cause: "
					+ e.getMessage());
		}
	}

}
