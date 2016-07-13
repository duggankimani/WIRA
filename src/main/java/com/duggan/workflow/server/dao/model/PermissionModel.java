package com.duggan.workflow.server.dao.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.wira.commons.shared.models.PermissionName;
import com.wira.commons.shared.models.PermissionPOJO;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "permission", indexes = { @Index(name = "idx_permission_name", columnList = "name") })
@NamedNativeQueries({
		@NamedNativeQuery(name = "Permission.GetPermissions", resultSetMapping = "Permission.GetPermissionsMapping", query = "select p.name,p.description,false isPermissionGranted from permission p "),

		@NamedNativeQuery(name = "Permission.GetUserPermissions", resultSetMapping = "Permission.GetPermissionsMapping", 
		query = "select p.name,p.description,true isPermissionGranted "
				+ "from permission p "
				+ "inner join role_permissions rp on (rp.permissionid=p.id) "
				+ "inner join bgroup r on (rp.roleid=r.id) "
				+ "inner join UserGroup ur on (ur.groupid=r.id) "
				+ "inner join \"buser\" u on (u.id=ur.userid) where u.userid=:username"),

		@NamedNativeQuery(name = "Permission.GetRolePermissions", resultSetMapping = "Permission.GetPermissionsMapping", query = "select p.name,p.description, true isPermissionGranted "
				+ "from permission p "
				+ "inner join role_permissions rp on (rp.permissionid=p.id) "
				+ "inner join bgroup r on (rp.roleid=r.id) "
				+ "where r.name=:name") })
@SqlResultSetMappings(
		
		@SqlResultSetMapping(name = "Permission.GetPermissionsMapping", columns = {
		@ColumnResult(name = "name", type = String.class),
		@ColumnResult(name = "description", type = String.class),
		@ColumnResult(name = "isPermissionGranted", type = Boolean.class)}

// JPA 2.0 does no support this annotation
// classes = { @ConstructorResult(targetClass = PermissionPOJO.class, columns =
// {
// @ColumnResult(name = "name", type = String.class),
// @ColumnResult(name = "description", type = String.class),
// @ColumnResult(name = "isPermissionGranted", type = Boolean.class),
// })
// }
))
public class PermissionModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	// Calls db implementation of Serial/Autoincrement
	protected Long id;

	@Column(unique = true, nullable = false)
	@Enumerated(EnumType.STRING)
	private PermissionName name;

	private String description;

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "permissions")
	private Set<Group> roles = new HashSet<Group>();

	public PermissionModel() {
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "{name:" + name + ",description:" + description + "}";
	}

	public PermissionPOJO toDto() {
		PermissionPOJO dto = new PermissionPOJO();
		dto.setDescription(description);
		dto.setName(name);

		return dto;
	}

	public PermissionModel clone(String... token) {
		PermissionModel permission = new PermissionModel();
		permission.setName(name);
		permission.setDescription(description);

		return permission;
	}

	public Set<Group> getRoles() {
		return roles;
	}

	public void setRoles(Set<Group> roles) {
		this.roles = roles;
	}

	public PermissionName getName() {
		return name;
	}

	public void setName(PermissionName name) {
		this.name = name;
	}

}
