package com.duggan.workflow.client.ui.component;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.DoubleBox;

public class DoubleField extends DoubleBox {

	public DoubleField() {

		addKeyPressHandler(new KeyPressHandler() {

			@Override
			public void onKeyPress(KeyPressEvent event) {
				int keyCode = event.getNativeEvent().getKeyCode();
				
				switch (keyCode) {
	            case KeyCodes.KEY_LEFT:
	            case KeyCodes.KEY_RIGHT:
	            //case KeyCodes.KEY_BACKSPACE:
	            case KeyCodes.KEY_DELETE:
	            case KeyCodes.KEY_TAB:
	            case KeyCodes.KEY_UP:
	            case KeyCodes.KEY_DOWN:
	            case KeyCodes.KEY_ENTER:
	                return;
	            }
				
				if (!(Character.isDigit(event.getCharCode())) && event.getCharCode() != '.') {
					cancelKey();
				}

				if (event.getCharCode() == '.') {
					if (getValue() != null && getText().indexOf('.') != -1) {
						cancelKey();
					}
				}
				
				int index = getCursorPos();
	            String previousText = getText();
	            String newText;
	            if (getSelectionLength() > 0) {
	                newText = previousText.substring(0, getCursorPos())
	                        + event.getCharCode()
	                        + previousText.substring(getCursorPos()
	                                + getSelectionLength(), previousText.length());
	            } else {
	                newText = previousText.substring(0, index)
	                        + event.getCharCode()
	                        + previousText.substring(index, previousText.length());
	            }
	            cancelKey();

	            Double val = null;
	            if(newText!=null || !newText.trim().isEmpty()){
	            	val = new Double(newText.trim().replace(",", ""));
	            }
	            setValue(val, true);

				//Window.alert("" + getValue());
			}
		});
	}

	public void setPlaceholder(String placeHolderValue) {
		getElement().setAttribute("placeHolder", placeHolderValue);
	}

	public void setClass(String className) {
		setStyleName(className);
	}

	public void setType(String type) {
		getElement().setAttribute("type", type);
	}
}
