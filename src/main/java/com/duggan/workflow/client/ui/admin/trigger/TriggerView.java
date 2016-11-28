package com.duggan.workflow.client.ui.admin.trigger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;

import com.duggan.workflow.client.ui.component.ActionLink;
import com.duggan.workflow.client.ui.component.TableHeader;
import com.duggan.workflow.client.ui.component.TableView;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.events.EditTriggerEvent;
import com.duggan.workflow.shared.model.Trigger;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class TriggerView extends ViewImpl implements
		TriggerPresenter.ITriggerView {

	private final Widget widget;
	@UiField Anchor aNewTrigger;
	@UiField Anchor aCloneTrigger;
	@UiField TableView tblView;

	public interface Binder extends UiBinder<Widget, TriggerView> {
	}

	@Inject
	public TriggerView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		setTable();
	}

	private void setTable() {
		tblView.setAutoNumber(true);
		ArrayList<TableHeader> th = new ArrayList<TableHeader>();
		th.add(new TableHeader("Name", 50.0,"title"));
		th.add(new TableHeader("Action(s)", 50.0));
		
		tblView.setTableHeaders(th);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	public HasClickHandlers getAddTriggerLink() {
		return aNewTrigger;
	}

	@Override
	public void setTriggers(ArrayList<Trigger> triggers) {
		tblView.clearRows();
		Collections.sort(triggers, new Comparator<Trigger>(){
			@Override
			public int compare(Trigger o1, Trigger o2) {
			
				return o1.getName().compareTo(o2.getName());
			}
		});
		
		for(Trigger trigger: triggers){
			createRow(trigger);
		}
	}

	private void createRow(final Trigger trigger) {
		
		final Link edit = new Link("Edit",trigger.clone());
		edit.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Trigger d = edit.getTrigger();
				d.setActive(true);
				AppContext.fireEvent(new EditTriggerEvent(d));
			}
		});
		
		final Link delete = new Link("Delete", trigger.clone());
		delete.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Trigger d= delete.getTrigger();
				d.setActive(false);
				AppContext.fireEvent(new EditTriggerEvent(d));
			}
		});
		
		HTMLPanel panel = new HTMLPanel("");
		panel.add(edit);
		panel.add(delete);
		
		tblView.addRow(new InlineLabel(trigger.getName()), panel);
	}
	
	class Link extends ActionLink{
		Trigger trigger;
		Link(String text,Trigger trigger){
			this.trigger = trigger;
			setText(text);
		}
		
		Trigger getTrigger(){
			return trigger;
		}
	}

	@Override
	public HasClickHandlers getCloneTriggerLink() {
		return aCloneTrigger;
	}
	
}
