package com.wira.commons.shared.models;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

/**
 * 
 * @author duggan
 *
 */
public class UserGroup implements Serializable,IsSerializable, Listable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
     * The key provider that provides the unique ID of a contact.
     */
    public static final ProvidesKey<UserGroup> KEY_PROVIDER = new ProvidesKey<UserGroup>() {
      @Override
      public Object getKey(UserGroup item) {
        return item == null ? null : item.getName();
      }
    };
    
	private Long id;
	
	private String name;
	
	private String fullName;
	
	private ArrayList<PermissionPOJO> permissions = new ArrayList<PermissionPOJO>();
	
	public UserGroup(){
	}

	public UserGroup(String name){
		this.name=name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof UserGroup)){
			return false;
		}
		
		UserGroup other = (UserGroup)obj;
		
		return other.name.equals(name);
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
	
	@Override
	public String toString() {
		
		return name;
	}

	@Override
	public String getDisplayName() {
		
		return fullName;
	}

	public ArrayList<PermissionPOJO> getPermissions() {
		return permissions;
	}

	public void setPermissions(ArrayList<PermissionPOJO> permissions) {
		this.permissions = permissions;
	}
}
