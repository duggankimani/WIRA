package com.duggan.workflow.client.ui.dashboardimmigration;

import com.duggan.workflow.client.ui.component.ActionLink;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class ImmigrationView extends ViewImpl implements
		ImmigrationPresenter.IImmigrationView {

	private final Widget widget;

	final int timerSeconds = 600;

	public interface Binder extends UiBinder<Widget, ImmigrationView> {
	}
	
	@UiField ActionLink aDashboard;
	@UiField ActionLink aTraffic;
	@UiField ActionLink aTrafficByLocation;
	@UiField ActionLink aTrafficByClass;
	@UiField ActionLink aPerformance;
	@UiField ActionLink aPerformanceByDepartment;
	@UiField Image aImage;
	

	@Inject
	public ImmigrationView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		aImage.setWidth("80%");
		aDashboard.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				aImage.setUrl("/img/immigration/dashboard.png");
			}
		});
		aTraffic.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				aImage.setUrl("/img/immigration/trafficbylocation.png");
			}
		});
		aTrafficByLocation.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				aImage.setUrl("/img/immigration/trafficbylocation.png");
			}
		});
		aTrafficByClass.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				aImage.setUrl("/img/immigration/trafficbyclass.png");
			}
		});
		aPerformance.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				aImage.setUrl("/img/immigration/performancebydept.png");
			}
		});
		aPerformanceByDepartment.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				aImage.setUrl("/img/immigration/performancebydept.png");
			}
		});
	}
	

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void bind() {
		aImage.setUrl("/img/immigration/dashboard.png");
	}
}
