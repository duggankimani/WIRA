package com.duggan.workflow.client.ui.admin.formbuilder.propertypanel;

import static com.duggan.workflow.client.ui.admin.formbuilder.HasProperties.SELECTIONTYPE;
import static com.duggan.workflow.client.ui.admin.formbuilder.HasProperties.SQLDS;
import static com.duggan.workflow.client.ui.admin.formbuilder.HasProperties.SQLSELECT;

import java.util.ArrayList;

import com.duggan.workflow.client.ui.admin.formbuilder.component.FieldWidget;
import com.duggan.workflow.client.ui.admin.formbuilder.component.InputSelection;
import com.duggan.workflow.shared.model.form.Field;
import com.duggan.workflow.shared.model.form.FormModel;
import com.duggan.workflow.shared.model.form.Property;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class PropertyPanelView extends ViewImpl implements
		PropertyPanelPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, PropertyPanelView> {
	}
	
	@UiField FocusPanel popoverFocus;
	@UiField HTMLPanel pBody;
	@UiField HTMLPanel iArrow;
	
	@Inject
	public PropertyPanelView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void showProperties(ArrayList<Property> properties,FormModel model) {
		clear();
		boolean isDSAvailable=false;
		boolean isSQLAvailable=false;
		for(Property property: properties){
			assert property!=null;
			if(!property.isShowInPropertyPanel()){
				continue;
			}
			FieldWidget fw = FieldWidget.getWidget(property);
			add(fw);
			
			/**
			 * This is an input component for providing
			 * possible values/answers for choice boxes
			 * and comboboxes
			 * <p>
			 * A single key is assigned to all possible
			 * choices
			 * 
			 */
			if(property.getName().equals(SQLDS)){
				if(property.getValue()!=null && property.getValue().getValue()!=null){
					isDSAvailable=true;
				}
			}
			
			if(property.getName().equals(SQLSELECT)){
				if(property.getValue()!=null && property.getValue().getValue()!=null){
					isSQLAvailable=true;
				}
			}
			
			if(property.getName().equals(SELECTIONTYPE) && !(isDSAvailable&&isSQLAvailable)){
				
				property.setFieldRefId(model.getRefId());
				UIObject.setVisible(fw.createComponent(false).getElement(), false);
				
				assert property.getFieldRefId()!=null;
				addSelection((Field)model,property);
			}
		}
	}

	private void addSelection(Field field, Property property) {
		InputSelection selection = new InputSelection(property);
		selection.setValues(field.getSelectionValues());
		add(selection);
	}
	
	@Override
	public void showBody(boolean status, Widget w){
		if(status){
			add(w);
		}
	}
	
	private void clear() {
		pBody.clear();
	}

	private void add(Widget widget) {
		pBody.add(widget);
	}
	
	public HTMLPanel getiArrow() {
		return iArrow;
	}
	
	public FocusPanel getPopoverFocus() {
		return popoverFocus;
	}
	
}
