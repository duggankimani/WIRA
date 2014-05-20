package com.duggan.workflow.shared.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum HTStatus {
	
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
	
	public List<Actions> getValidActions(){
		
		List<Actions> actions=new ArrayList<Actions>();
		
		switch(this){
		case COMPLETED:
			actions = Arrays.asList();
			break;
		case CREATED:
			actions = Arrays.asList(Actions.CLAIM);
			break;
		case ERROR:
			actions = Arrays.asList();
			break;
		case EXITED:
			actions = Arrays.asList();
			break;
		case FAILED:
			actions = Arrays.asList();
			break;
		case INPROGRESS:
			actions = Arrays.asList(Actions.SUSPEND, Actions.COMPLETE);
			break;
		case OBSOLUTE:
			actions = Arrays.asList();
			break;
		case READY:
			actions = Arrays.asList(Actions.CLAIM, Actions.DELEGATE, Actions.START,Actions.SUSPEND);
			break;
		case RESERVED:
			actions = Arrays.asList(Actions.SUSPEND, Actions.DELEGATE, Actions.START,Actions.REVOKE);
			break;
		case SUSPENDED:
			actions = Arrays.asList(Actions.RESUME);
			break;
		}
		
		return actions;
	}
}
