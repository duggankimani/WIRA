package com.duggan.workflow.shared.model;

import com.google.gwt.user.client.rpc.IsSerializable;


public enum DocStatus implements IsSerializable{

	DRAFTED,
	INPROGRESS,
	APPROVED,
	COMPLETED,
	REJECTED, FAILED;
}
