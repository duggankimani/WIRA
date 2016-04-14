package com.duggan.workflow.client.ui.admin.users.save;


import com.duggan.workflow.shared.model.PermissionPOJO;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class PermissionPanel extends Composite {

	private static PermissionPanelUiBinder uiBinder = GWT
			.create(PermissionPanelUiBinder.class);

	interface PermissionPanelUiBinder extends UiBinder<Widget, PermissionPanel> {
	}

	@UiField CheckBox chkPermission;
	private PermissionPOJO permission;
	
	private PermissionPanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public PermissionPanel(PermissionPOJO permission){
		this();
		this.permission = permission;
		chkPermission.setValue(permission.isPermissionGranted());
	}
	
	public PermissionPanel(PermissionPOJO permission, boolean isEditable){
		this(permission);
		chkPermission.setEnabled(isEditable);
	}

	public PermissionPOJO getPermission() {
		permission.setPermissionGranted(chkPermission.getValue());
		return permission;
	}

}
