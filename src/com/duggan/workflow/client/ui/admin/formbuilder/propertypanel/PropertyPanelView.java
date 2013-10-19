package com.duggan.workflow.client.ui.admin.formbuilder.propertypanel;

import java.util.List;

import com.duggan.workflow.client.ui.admin.formbuilder.component.DateField;
import com.duggan.workflow.client.ui.admin.formbuilder.component.FieldWidget;
import com.duggan.workflow.client.ui.admin.formbuilder.component.InlineCheckBox;
import com.duggan.workflow.client.ui.admin.formbuilder.component.TextField;
import com.duggan.workflow.shared.model.form.Property;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.PopupViewImpl;

public class PropertyPanelView extends PopupViewImpl implements
		PropertyPanelPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, PropertyPanelView> {
	}
	
	@UiField PopupPanel popUpContainer;
	@UiField FocusPanel popoverFocus;
	@UiField HTMLPanel pBody;
	@UiField Anchor btnSave;
	@UiField Anchor btnCancel;
	@UiField HTMLPanel iArrow;
	
	
	@Inject
	public PropertyPanelView(final EventBus eventBus, final Binder binder) {
		super(eventBus);
		widget = binder.createAndBindUi(this);
		popUpContainer.getElement().getStyle().setDisplay(Display.BLOCK);
		popUpContainer.getElement().getStyle().setOverflow(Overflow.AUTO);

		btnCancel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				hide();
			}
		});
		
		popoverFocus.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				//Window.alert("called Blur");
				hide();
			}
		});
		
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void showProperties(List<Property> properties) {
		clear();
		for(Property property: properties){
			assert property!=null;
			
			add(FieldWidget.getWidget(property));
		}
	}

	private void clear() {
		pBody.clear();
	}

	private void add(Widget widget) {
		pBody.add(widget);
	}
	
	public PopupPanel getPopUpContainer() {
		return popUpContainer;
	}
	
	public HTMLPanel getiArrow() {
		return iArrow;
	}
	
	public FocusPanel getPopoverFocus() {
		return popoverFocus;
	}
	
	public HasClickHandlers getSaveButton(){
		return btnSave;
	}
}
