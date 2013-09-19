package com.duggan.workflow.test.bpm;

import java.util.ArrayList;
import java.util.List;

public class Clicker {

	static ClickMeHandler handler1 = new ClickMeHandler() {
		
		@Override
		public void OnClick() {
			System.out.println("Hello there");
		}
	};
	
	static ClickMeHandler handler2 = new ClickMeHandler() {
		
		@Override
		public void OnClick() {
			System.out.println("Hello there, am number 2");
		}
	};
	
	
	public static void main(String[] args) {
		addHandler(handler1);
		
		addHandler(handler2);
		
		addHandler(new MyClass());
		clicked();
	}

	private static void addHandler(ClickMeHandler handler) {
		handlers.add(handler);
	}
	
	private static void clicked() {
		for(ClickMeHandler handler : handlers){
			handler.OnClick();
		}
	}

	static List<ClickMeHandler> handlers = new ArrayList<>();	
}
