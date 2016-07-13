package com.wira.commons.shared.models;


public enum REPORTVIEWIMPL implements Listable{

	GOOGLE_DOCS, NEW_TAB, IFRAME;

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name();
	}

	@Override
	public String getDisplayName() {
		return name();
	}

	public static REPORTVIEWIMPL getDefaultImplementation() {
		return IFRAME;
	}
}
