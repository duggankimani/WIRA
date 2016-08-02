package com.duggan.workflow.client.ui.component;

import static com.duggan.workflow.client.ui.util.DateUtils.DATEFORMAT_;
import static com.duggan.workflow.client.ui.util.StringUtils.isNullOrEmpty;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsDate;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.CalendarUtil;

public class DateInput extends Composite implements HasValue<Date> {

	private static DateInputUiBinder uiBinder = GWT
			.create(DateInputUiBinder.class);

	interface DateInputUiBinder extends UiBinder<Widget, DateInput> {
	}

	@UiField
	TextField txtDate;

	String id = DOM.createUniqueId();

	ArrayList<HandlerRegistration> handlers = new ArrayList<HandlerRegistration>();

	public DateInput() {
		initWidget(uiBinder.createAndBindUi(this));
		getElement().setId(id);
		txtDate.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				// Window.alert("Value changed!! -- "+event.getValue());
			}
		});
	}

	public DateInput(Element element) {
		
		Element parent = element.getParentElement();

		if (!(parent.hasClassName("input-group") && parent
				.hasClassName("date"))) {
			int idx = DOM.getChildIndex(parent, element);
			element.removeFromParent();

			Element controlGroup = DOM.createDiv();
			controlGroup.addClassName("form-group span3");

			id = element.getId()+ "_Field";
			Element div = DOM.createDiv();
			div.addClassName("input-group date datepicker");
			div.setId(id);
			element.addClassName("form-control");
			div.appendChild(element);

			Element span = DOM.createSpan();
			span.addClassName("input-group-addon");
			span.getStyle().setPaddingLeft(26, Unit.PX);
			span.setInnerHTML("<span class=\"icon-calendar\"></span>");
			div.appendChild(span);

			controlGroup.appendChild(div);
			DOM.insertChild(parent, controlGroup, idx);
			// initWidget(txtDate);
		}else{
			id = parent.getId();
		}
		
		txtDate = TextField.wrap(element,true);
		initCollapsable(this, id);
		txtDate.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				// Window.alert("Value changed!! -- "+event.getValue());
			}
		});
	}

	public Date getValueDate() {
		String dateStr = txtDate.getValue();
		// yyyy-MM-dd
		if (isNullOrEmpty(dateStr)) {
			return null;
		}

		return DATEFORMAT_.parse(dateStr);
	}

	public HasValueChangeHandlers<String> getDateInput() {
		return txtDate;
	}

	public void setPlaceholder(String placeHolderValue) {
		txtDate.getElement().setAttribute("placeHolder", placeHolderValue);
	}

	public void setClass(String className) {
		setStyleName(className);
	}

	public void setType(String type) {
		txtDate.getElement().setAttribute("type", type);
	}

	public void setDataToggle(String dataToggle) {
		txtDate.getElement().setAttribute("data-toggle", dataToggle);
	}

	public void setAriaHaspopup(String ariaHasPopup) {
		txtDate.getElement().setAttribute("aria-haspopup", ariaHasPopup);
	}

	public void setAriaExpanded(String ariaExpanded) {
		txtDate.getElement().setAttribute("aria-expanded", ariaExpanded);
	}

	public void setDisabled(Boolean isDisabled) {
		if (isDisabled) {
			txtDate.getElement().setAttribute("disabled", "disabled");
		}
	}

	public void setRequired(Boolean isRequired) {
		if (isRequired) {
			txtDate.getElement().setAttribute("required", "required");
		}
	}

	public void setMaxLength(String maxLength) {
		txtDate.getElement().setAttribute("max-length", maxLength);
	}

	public void setSize(String size) {
		txtDate.getElement().setAttribute("size", size);
	}

	public void clear() {
		txtDate.setValue("");
	}

	@Override
	protected void onAttach() {
		super.onAttach();
		Date today = new Date();
		CalendarUtil.addDaysToDate(today, 1);

		initCollapsable(this, id);
	}

	public static native void initCollapsable(DateInput parent, String id)/*-{
																			$wnd.jQuery($doc).ready(function(){
																			var dp = $wnd.jQuery("#"+id).datetimepicker({
																			icons: {
																			time: "icon-time",
																			date: "icon-calendar",
																			up: "icon-angle-up",
																			down: "icon-angle-down",
																			previous: 'icon-angle-left',
																			next: 'icon-angle-right',
																			},
																			format: 'DD-MM-YYYY',
																			useCurrent:false,
																			});
																			
																			
																			dp.on('dp.change', function(e) {
																				//$wnd.alert('Changed-- '+e.oldDate+' - '+e.date);
																				var value = new $wnd.Date(e.date);
																				parent.@com.duggan.workflow.client.ui.component.DateInput::setDate(Lcom/google/gwt/core/client/JsDate;)(value);
																			});
																			
																			});
																			}-*/;

	public Date getDate() {
		return getValueDate();
	}

	public Widget getInputComponent() {
		return txtDate;
	}

	public void setStyle(String styleName) {
		txtDate.addStyleName(styleName);
	}

	public void setValue(Date date) {
		if (date == null) {
			txtDate.setValue("");
		} else {
			txtDate.setValue(DATEFORMAT_.format(date));
		}
	}

	private void setDate(JsDate date) {
		if (date != null) {
			setValue(new Date(new Double(date.getTime()).longValue()), true);
		} else {
			setValue(null, true);
		}
	}

	@Override
	public void setValue(Date value, boolean fireEvents) {
		setValue(value);
		if (fireEvents) {
			ValueChangeEvent.fire(this, value);
		}
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<Date> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	@Override
	public Date getValue() {
		return getValueDate();
	}

	public void addClickHandler(ClickHandler clickHandler) {
		txtDate.addClickHandler(clickHandler);
	}

}