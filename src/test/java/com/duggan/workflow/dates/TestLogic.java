package com.duggan.workflow.dates;

public class TestLogic {

	public static void main(String[] args) {
		String isHODApproved = "Yes";
		String isRMDApproved=null;
		String isBOMApproved=null;
		boolean t =  isHODApproved.equals("Yes") && isRMDApproved==null? true: isRMDApproved.equals("Yes")    && isBOMApproved==null? true: isBOMApproved.equals("Yes");
		System.out.println(t);
	}
}
