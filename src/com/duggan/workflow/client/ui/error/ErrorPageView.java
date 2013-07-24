package com.duggan.workflow.client.ui.error;

import java.util.Date;

import com.duggan.workflow.client.ui.util.DateUtils;
import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class ErrorPageView extends ViewImpl implements
		ErrorPagePresenter.MyView {

	private final Widget widget;
	
	@UiField SpanElement spnMessage;
	@UiField SpanElement spnStack;
	@UiField SpanElement spnErrorDate;
	@UiField SpanElement spnUserAgent;
	@UiField SpanElement spnAddress;

	public interface Binder extends UiBinder<Widget, ErrorPageView> {
	}

	@Inject
	public ErrorPageView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setError(Date errorDate, String message, String stack, String userAgent, String address) {
		String[] strs = stack.split("\r\n");
		System.err.println("Size ## "+strs.length);
		
		String errDate = DateUtils.CREATEDFORMAT.format(errorDate);
		spnErrorDate.setInnerText(errDate);
		spnMessage.setInnerText(message);
		spnStack.setInnerHTML(stack.replaceAll("\r\n", "<br/>"));
		spnUserAgent.setInnerText("useragent= \""+userAgent+"\"");
		spnAddress.setInnerText("Address= "+address);
	}
}
