package com.duggan.workflow.test.bpm;

public class MyClass implements ClickMeHandler {

	@Override
	public void OnClick() {
		System.out.println(getClass() + " ME A Handler");
	}

}
