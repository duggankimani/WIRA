package com.duggan.workflow.client.ui.component;

import java.util.ArrayList;
import java.util.List;

import com.duggan.workflow.client.util.AppContext;
import com.google.gwt.dom.client.Style.TextAlign;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.HandlerRegistration;

public abstract class RowWidget extends Composite {

	private boolean isAutoNumber;
	private int rowNum=0;
	HTMLPanel row;
	List<HandlerRegistration> handlers = new ArrayList<HandlerRegistration>();
	private Object data=null;
	private boolean isShowRemove;
	
	public void setRow(HTMLPanel row){
		this.row = row;
	}
	
	public void setRowNumber(int number){
		this.rowNum=number;
		if(isAttached())
		if(this.isAutoNumber){
			row.getWidget(0).getElement().setInnerText(number+"");
		}
	}
	
	public boolean isAutoNumber() {
		return isAutoNumber;
	}

	public void setAutoNumber(boolean isAutoNumber) {
		this.isAutoNumber = isAutoNumber;
	}
	
	
	public void createRow(List<Widget> widgets){
		if(isAutoNumber){
			row.add(getTd(new InlineLabel(rowNum+"")));
		}
		
		for(Widget widget: widgets){
			row.add(getTd(widget));
		}
		
		
		if(isShowRemove){
			ActionLink anchor = new ActionLink();
			anchor.setTitle("Remove Line");
			anchor.getElement().setInnerHTML("<i class=\"icon-trash\"></i>");
			anchor.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					RowWidget.this.removeFromParent();
				}
			});
			row.add(getTd(anchor));
		}
	}
	
	protected Widget getTd(Widget widget) {
		HTMLPanel td = new HTMLPanel("");
		td.addStyleName("td");
		td.add(widget);				
		return td;
	}
	
	protected void createTd(Widget widget){
		createTd(widget, TextAlign.CENTER);
	}
	
	public void createTd(Widget widget, TextAlign align) {
		createTd(widget, align,null);
	}
	
	public void createTd(Widget widget, String width) {
		createTd(widget, TextAlign.CENTER,width);
	}
	
	public void createTd(Widget widget,TextAlign align,String width){
		HTMLPanel td = (HTMLPanel)getTd(widget);
		td.getElement().getStyle().setTextAlign(align);
		
		if(width!=null){
			td.setWidth(width);
		}
		row.add(td);
	}
	
	
	/**
	 * Remove widget in index i
	 * @param index Index of widget to be removed
	 * @return
	 */
	public boolean remove(Widget w){
		
		return row.remove(w);
	}
	
	/**
	 * 
	 * @param type
	 * @param handler
	 */
	public <H extends EventHandler> void addRegisteredHandler(Type<H> type, H handler){
		@SuppressWarnings("unchecked")
		HandlerRegistration hr = AppContext.getEventBus().addHandler(
				(GwtEvent.Type<EventHandler>)type, handler);
		handlers.add(hr);
	}
	
	/**
	 * 
	 */
	private void cleanUpEvents() {
		for(HandlerRegistration hr: handlers){
			hr.removeHandler();
		}
		handlers.clear();
	}

	@Override
	protected void onUnload() {
		super.onUnload();
		cleanUpEvents();
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public boolean isShowRemove() {
		return isShowRemove;
	}

	public void setShowRemove(boolean isShowRemove) {
		this.isShowRemove = isShowRemove;
	}
}
