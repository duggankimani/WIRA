package com.duggan.workflow.client.ui.admin.notification;

import java.util.ArrayList;

import com.duggan.workflow.client.ui.component.ActionLink;
import com.duggan.workflow.client.ui.component.AutoCompleteField;
import com.duggan.workflow.client.ui.component.DropDownList;
import com.duggan.workflow.client.ui.component.TextArea;
import com.duggan.workflow.client.ui.util.ArrayUtil;
import com.duggan.workflow.shared.model.Actions;
import com.duggan.workflow.shared.model.NotificationCategory;
import com.duggan.workflow.shared.model.StringListable;
import com.duggan.workflow.shared.model.TaskNotification;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class NotificationSetupView extends ViewImpl implements
		NotificationSetupPresenter.INotificationSetupView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, NotificationSetupView> {
	}

	@UiField TextArea txtNotification;
	@UiField TextArea txtSubject;
	@UiField CheckBox chkNotification;
	@UiField CheckBox chkDefault;
	@UiField SpanElement spnNotification;
	@UiField HTMLPanel panelContainer;
	@UiField Element notificationHeader;
	@UiField DropDownList<Actions> listActions;
	@UiField(provided=true) AutoCompleteField<StringListable> autocompleteTargets;
	@UiField ActionLink aSave;
	@UiField ActionLink aHtmlEditor;
	@UiField ActionLink aDelete;
	
	
	@Inject
	public NotificationSetupView(final Binder binder) {
		autocompleteTargets = new AutoCompleteField<StringListable>(){
			@Override
			public StringListable createItem(String s) {
				return new StringListable(s);
			}
		};
		widget = binder.createAndBindUi(this);
		listActions.setNullText("Task Creation");
		listActions.setItems(ArrayUtil.asList(Actions.COMPLETE));
		
		chkDefault.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				setUseDefaultMessage(event.getValue());
			}
		});
//		chkNotification.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
//			
//			@Override
//			public void onValueChange(ValueChangeEvent<Boolean> event) {
//				ArrayList<StringListable> values= autocompleteTargets.getSelectedItems();
//				if(values==null){
//					txtNotification.setValue("Null");
//					return;
//				}
//				txtNotification.setValue("");
//				String txt = "";
//				for(Listable listable: values){
//					txt = txt.concat("<"+listable.getName()+">"+listable.getDisplayName()+",");
//				}
//				txtNotification.setValue(txt);
//			}
//		});
		
	}

	public TaskNotification getNotificationTemplate() {
		TaskNotification notification = new TaskNotification();
		notification.setAction(listActions.getValue()==null? Actions.CREATE : listActions.getValue());
		notification.setEnableNotification(chkNotification.getValue()==null? true : chkNotification.getValue());
		notification.setNotificationTemplate(txtNotification.getValue());
		//notification.setTargets(getTargets());
		notification.setUseDefaultNotification(chkDefault.getValue()==null? false: chkDefault.getValue());
		notification.setSubject(txtSubject.getValue());
		return notification;
	}
	
	private ArrayList<String> getTargets() {
		ArrayList<StringListable> values= autocompleteTargets.getSelectedItems();
		ArrayList<String> ArrayList  = new ArrayList<String>();
		if(values!=null)
			for(StringListable val: values){
				ArrayList.add(val.getName());
			}
		
		return ArrayList;
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setNotificationCategory(NotificationCategory category) {
		if(category==null){
			panelContainer.addStyleName("hide");
			notificationHeader.setInnerText("");
			return;
		}

		panelContainer.removeStyleName("hide");
		switch (category) {
		case EMAILNOTIFICATION:
			spnNotification.setInnerText("Enable Email Notification");
			notificationHeader.setInnerText("Email Notifications");
			break;

		case ACTIVITYFEED:
			notificationHeader.setInnerText("Activity Feed");
			spnNotification.setInnerText("Enable Activity Feed");
			break;
			
		default:
			panelContainer.addStyleName("hide");
			notificationHeader.setInnerText("");
			break;
		}
	}

	public HasClickHandlers getSaveButton(){
		return aSave;
	}

	@Override
	public void setNotification(TaskNotification notification) {
		if(notification.getAction()!=null){
			listActions.setValue(notification.getAction());
		}
		chkNotification.setValue(notification.isEnableNotification());
		txtNotification.setValue(notification.getNotificationTemplate());
		txtSubject.setValue(notification.getSubject());
		ArrayList<String> targets = notification.getTargets();
		ArrayList<StringListable> ArrayList = new ArrayList<StringListable>();
		for(String value : targets){
			ArrayList.add(new StringListable(value));
		}
		
		setDefaultContextValues(notification.getAction(), ArrayList);
		autocompleteTargets.setValues(ArrayList);
		autocompleteTargets.select(ArrayList);
		
		chkDefault.setValue(notification.isUseDefaultNotification());
		setUseDefaultMessage(notification.isUseDefaultNotification());
	}
	
	private void setDefaultContextValues(Actions action,
			ArrayList<StringListable> ArrayList) {
		if(action==null){
			action = Actions.CREATE;
		}
		
		addIfNotExists(ArrayList, "@@UserId");
		if(action==Actions.CREATE){
			addIfNotExists(ArrayList,"@@GroupId");
			addIfNotExists(ArrayList, "@@ActorId");
			addIfNotExists(ArrayList, "@@OwnerId");
		}
	}

	private void addIfNotExists(ArrayList<StringListable> ArrayList, String key) {
		StringListable listable = new StringListable(key);
		if(!ArrayList.contains(listable)){
			ArrayList.add(listable);
		}
	}

	public DropDownList<Actions> getActionsDropdown(){
		return listActions;
	}
	
	@Override
	public void clear() {
		chkNotification.setValue(null);
		txtNotification.setValue(null);
		autocompleteTargets.select(null);
		autocompleteTargets.setValues(null);
		chkDefault.setValue(null);
		txtSubject.setValue(null);
		setUseDefaultMessage(false);
	}
	
	public void setUseDefaultMessage(boolean isUseDefault){
		if(isUseDefault){
			txtNotification.addStyleName("hide");
		}else{
			txtNotification.removeStyleName("hide");
		}
	}

	public HasClickHandlers getHTMLEditorLink(){
		return aHtmlEditor;
	}
	
	@Override
	public String getHtmlNotification() {
		return txtNotification.getValue();
	}

	@Override
	public void setHTMLNotification(String text) {
		txtNotification.setValue(text);
	}
	
	public HasClickHandlers getDeleteButton(){
		return aDelete;
	}
	
	
}
