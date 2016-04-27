package com.duggan.workflow.server.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import com.duggan.workflow.server.dao.model.PermissionModel;
import com.duggan.workflow.shared.model.PermissionName;
import com.duggan.workflow.shared.model.PermissionPOJO;

public class PermissionDao extends BaseDaoImpl {

	public PermissionDao(EntityManager em) {
		super(em);
	}

	public PermissionModel getPermissionByName(PermissionName name) {
		PermissionModel permission = (PermissionModel) getEntityManager()
				.createQuery("from PermissionModel u where u.name=:name")
				.setParameter("name", name).getSingleResult();
		return permission;
	}

	public void createPermission(PermissionModel permission) {
		getEntityManager().persist(permission);
	}

	@SuppressWarnings("unchecked")
	public List<PermissionPOJO> getAllPermissions(Integer offSet, Integer limit) {
		List<Object[]> values = getEntityManager().createNamedQuery(
				"Permission.GetPermissions").getResultList();
		return getPermissions(values);
	}

	private List<PermissionPOJO> getPermissions(List<Object[]> rows) {

		List<PermissionPOJO> permissions = new ArrayList<PermissionPOJO>();
		for (Object[] row : rows) {
			PermissionPOJO pojo = new PermissionPOJO();

			String name = row[0].toString();
			String description = row[1].toString();
			boolean isPermissionGranted = (Boolean) row[2];
			pojo.setName(PermissionName.valueOf(name));
			pojo.setDescription(description);
			pojo.setPermissionGranted(isPermissionGranted);
			permissions.add(pojo);
		}
		return permissions;
	}

	@SuppressWarnings("unchecked")
	public List<PermissionPOJO> getPermissionsForUser(String username) {
		List<Object[]> values = getEntityManager()
				.createNamedQuery("Permission.GetUserPermissions")
				.setParameter("username", username).getResultList();
		return getPermissions(values);
	}

	@SuppressWarnings("unchecked")
	public List<PermissionPOJO> getPermissionsForRole(String roleName) {
		List<Object[]> values = getEntityManager()
				.createNamedQuery("Permission.GetRolePermissions")
				.setParameter("name", roleName).getResultList();
		return getPermissions(values);
	}

	public void updatePermission(PermissionModel Permission) {
		createPermission(Permission);
	}

	public int getPermissionCount() {
		Number number = (Number) getEntityManager().createQuery(
				"select count(p.id) from PermissionModel p where p.isActive=1")
				.getSingleResult();

		return number.intValue();
	}

}
