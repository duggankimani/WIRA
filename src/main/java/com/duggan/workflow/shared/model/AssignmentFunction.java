package com.duggan.workflow.shared.model;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public enum AssignmentFunction implements Serializable, IsSerializable{

	DIRECT_ASSIGNMENT,
	CYCLIC_ASSIGNMENT,
	SELFSERVICE_ASSIGNMENT
}
