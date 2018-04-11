package com.duggan.workflow.server.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.duggan.workflow.server.dao.model.OrgModel;

public class OrganizationDao extends BaseDaoImpl {
	
	public OrganizationDao(EntityManager em) {
		super(em);
	}

	public OrgModel getByOrgModelId(String refId) {
		return getByOrgModelId(refId, true);
	}

	public OrgModel getByOrgModelId(String refId, boolean isThrowExceptionIfNull) {
		OrgModel role = getSingleResultOrNull(
				getEntityManager().createQuery("from OrgModel u where u.refId=:refId")
				.setParameter("refId", refId));
		return role;
	}

	public void createOrgModel(OrgModel role) {
		save(role);
	}

	public List<OrgModel> getAllOrgModels(String searchText, Integer offSet, Integer limit) {
		StringBuffer sql = new StringBuffer("from OrgModel where isActive=1 ");

		if (searchText != null && !searchText.isEmpty()) {
			sql.append(" and (lower(name) like :searchText)");
		}
		sql.append(" order by name");
		Query query = getEntityManager().createQuery(sql.toString());

		// QueryImpl<X> l;
		if (searchText != null && !searchText.isEmpty()) {
			query.setParameter("searchText", "%" + searchText.toLowerCase() + "%");
		}

		return getResultList(query, offSet, limit);
	}

	public void updateOrgModel(OrgModel role) {
		createOrgModel(role);
	}

	public int getOrgModelCount(String searchText) {
		StringBuffer sql = new StringBuffer("select count(*) from orgmodel where isactive=1");
		if (searchText != null && !searchText.isEmpty()) {
			sql.append(" and (lower(name) like :searchText)");
		}

		Query query = getEntityManager().createNativeQuery(sql.toString());
		if (searchText != null && !searchText.isEmpty()) {
			query.setParameter("searchText", "%" + searchText.toLowerCase() + "%");
		}

		Number number = getSingleResultOrNull(query);
		return number.intValue();
	}

}
