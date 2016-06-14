package com.duggan.workflow.shared.model;

import java.util.ArrayList;

import com.duggan.workflow.client.ui.util.ArrayUtil;
import com.google.gwt.user.client.rpc.IsSerializable;

public enum HTStatus implements IsSerializable{
	
	COMPLETED,
	CREATED,
	ERROR,
	EXITED,
	FAILED,
	INPROGRESS,
	OBSOLUTE,
	READY,
	RESERVED,
	SUSPENDED;
	
	public ArrayList<Actions> getValidActions(){
		
		ArrayList<Actions> actions=new ArrayList<Actions>();
		
		switch(this){
		case COMPLETED:
			actions = ArrayUtil.asList();
			break;
		case CREATED:
			actions = ArrayUtil.asList(Actions.CLAIM);
			break;
		case ERROR:
			actions = ArrayUtil.asList();
			break;
		case EXITED:
			actions = ArrayUtil.asList();
			break;
		case FAILED:
			actions = ArrayUtil.asList();
			break;
		case INPROGRESS:
			actions = ArrayUtil.asList(Actions.SUSPEND, Actions.COMPLETE);
			break;
		case OBSOLUTE:
			actions = ArrayUtil.asList();
			break;
		case READY:
			actions = ArrayUtil.asList(Actions.CLAIM, Actions.DELEGATE, Actions.START,Actions.SUSPEND);
			break;
		case RESERVED:
			actions = ArrayUtil.asList(Actions.SUSPEND, Actions.DELEGATE, Actions.START,Actions.REVOKE);
			break;
		case SUSPENDED:
			actions = ArrayUtil.asList(Actions.RESUME);
			break;
		}
		
		return actions;
	}
}
