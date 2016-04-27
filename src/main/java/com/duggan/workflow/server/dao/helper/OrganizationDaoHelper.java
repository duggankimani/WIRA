package com.duggan.workflow.server.dao.helper;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.duggan.workflow.server.dao.model.OrgModel;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.shared.model.Org;

public class OrganizationDaoHelper {
	Logger logger = Logger.getLogger(OrganizationDaoHelper.class);

	public static List<Org> getAllOrgModels(String searchText, Integer offset,
			Integer limit) {

		List<OrgModel> orgs = DB.getOrganizationDao().getAllOrgModels(searchText, offset, limit);
		List<Org> dtos = new ArrayList<>();
		for (OrgModel org : orgs) {
			dtos.add(org.toDto());
		}
		return dtos;
	}
	
	public static int getCount(String searchText) {
		
		return DB.getOrganizationDao().getOrgModelCount(searchText);
	}

	public static Org getOrgModelById(String orgId) {

		OrgModel org = DB.getOrganizationDao().getByOrgModelId(orgId);
		if (org == null) {
			return null;
		}

		return org.toDto();
	}

	public static Org createOrgModel(Org dto) {

		OrgModel org = new OrgModel();
		if(dto.getRefId()!=null){
			org = DB.getOrganizationDao().findByRefId(dto.getRefId(), OrgModel.class);
		}
		org.copyFrom(dto);
		DB.getOrganizationDao().save(org);

		return org.toDto();
	}

	public static Org updateOrgModel(String orgId, Org dto) {

		OrgModel poOrgModel = DB.getOrganizationDao().getByOrgModelId(orgId);
		poOrgModel.copyFrom(dto);
		DB.getOrganizationDao().save(poOrgModel);

		return poOrgModel.toDto();
	}

	public static void deleteOrgModel(String orgId) {
		OrgModel org = DB.getOrganizationDao().getByOrgModelId(orgId);
		DB.getOrganizationDao().delete(org);
	}

}
