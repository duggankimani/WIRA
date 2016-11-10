package com.wira.commons.shared.models;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public enum ErrorCodes implements Serializable, IsSerializable{
	 NO_ERROR,SERVER_ERROR,INVALID_CASENO, INVALID_SESSION, DB_CONSTRAINT_ERROR, DB_PERSISTENCE_ERROR, INTEGRATION_SMS,

	
}
