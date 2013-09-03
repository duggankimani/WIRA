package com.duggan.workflow.client.ui.filter;

import static com.duggan.workflow.client.ui.util.DateUtils.DATEFORMAT;

import com.duggan.workflow.shared.model.DocType;
import com.duggan.workflow.shared.model.SearchFilter;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasBlurHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class FilterView extends ViewImpl implements FilterPresenter.MyView {

	private final Widget widget;
	@UiField FocusPanel filterDialog;
	@UiField DateBox dateInput1;
	@UiField DateBox dateInput2;
	@UiField TextBox txtSubject;
	@UiField TextBox txtPhrase;
	@UiField Button btnSearch;
	@UiField Anchor aClose;
	@UiField ListBox lstDocType;
	@UiField InlineLabel spnCalendar1;
	@UiField InlineLabel spnCalendar2;
	
	
	public interface Binder extends UiBinder<Widget, FilterView> {
	}

	@Inject
	public FilterView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		dateInput1.getElement().setAttribute("Placeholder", "Start Date");	
		dateInput1.setFormat(new DateBox.DefaultFormat(DATEFORMAT));
		dateInput2.setFormat(new DateBox.DefaultFormat(DATEFORMAT));
		dateInput2.getElement().setAttribute("Placeholder", "End Date");
		DocType[] types = DocType.values();

		/*select field for document types*/
		lstDocType.addItem("Document Type");
		for(DocType type: types){
			lstDocType.addItem(type.getDisplayName());
		}
		
		spnCalendar1.getElement().setInnerHTML("<i class='icon-calendar'/>");
		spnCalendar2.getElement().setInnerHTML("<i class='icon-calendar'/>");
		spnCalendar1.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				dateInput1.showDatePicker();
				
			}
		});
		
		spnCalendar2.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				dateInput2.showDatePicker();
				
			}
		});
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public HasClickHandlers getSearchButton() {
		return btnSearch;
	}

	@Override
	public SearchFilter getSearchFilter() {
		SearchFilter filter = new SearchFilter();
		filter.setSubject(txtSubject.getValue());
		filter.setPhrase(txtPhrase.getValue());
		filter.setStartDate(dateInput1.getValue());
		filter.setEndDate(dateInput2.getValue());
		int index=lstDocType.getSelectedIndex();
		if(index==0){
			filter.setDocType(null);
		}else{
			filter.setDocType(DocType.values()[index-1]);
		}
		return filter;
	}

	@Override
	public HasClickHandlers getCloseButton() {
		return aClose;
	}

	@Override
	public HasBlurHandlers getFilterDialog() {
		return filterDialog;
	}
	
	
}
