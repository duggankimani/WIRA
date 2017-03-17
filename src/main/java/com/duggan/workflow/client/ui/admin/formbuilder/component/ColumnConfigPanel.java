package com.duggan.workflow.client.ui.admin.formbuilder.component;

import java.util.ArrayList;

import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.VerticalPanelDropController;
import com.duggan.workflow.shared.model.form.Field;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ColumnConfigPanel extends AbsolutePanel{

	private DragHandler handler;
	private VerticalPanelDropController rowDropController;
	private PickupDragController columnDragController;
	VerticalPanel vPanel = new VerticalPanel();
	
	public ColumnConfigPanel(ArrayList<Field> fields) {
		InlineLabel label = new InlineLabel("Drag the columns to the desired  position");
		add(label);
		add(vPanel);
		
		handler = new DragHandlerImpl(this) {
			@Override
			public void onDragEnd(DragEndEvent event) {
				super.onDragEnd(event);
				Widget parent = event.getContext().draggable.getParent();
				if (!parent.equals(vPanel)) {
					// removed
					VerticalPanel panel = (VerticalPanel) event.getContext().draggable;
					GridColumn col = (GridColumn) panel.getWidget(0);
					//col.delete(); - Do nothing
				}

				//save();
			}

		};

		columnDragController = new PickupDragController(this, false) {
			@Override
			protected void restoreSelectedWidgetsLocation() {
				// TODO Auto-generated method stub
				super.restoreSelectedWidgetsLocation();
			}
		};
		

		columnDragController.addDragHandler(handler);
		columnDragController.setBehaviorMultipleSelection(false);
		columnDragController.setBehaviorDragStartSensitivity(5);

		// initialize our column drop controller
		rowDropController = new VerticalPanelDropController(vPanel);
		columnDragController.registerDropController(rowDropController);

		setFields(fields);
	}

	private void setFields(ArrayList<Field> fields) {
		vPanel.setWidth("100%");
		for(Field field: fields){
			VerticalPanel columnCompositePanel = new VerticalPanel();
			columnCompositePanel.setWidth("100%");

			// initialize inner vertical panel to hold individual widgets
			VerticalPanel verticalPanel = new VerticalPanel();
			verticalPanel.setWidth("100%");
			
			vPanel.add(columnCompositePanel);

			// Put together the column pieces
			GridColumn heading = new GridColumn(field,false);
			heading.label.addStyleName("btn btn-info");
			heading.label.setWidth("100%");
			heading.label.getElement().getStyle().setBackgroundColor("#3c6094");
			heading.label.getElement().getStyle().setFontWeight(FontWeight.BOLD);
			heading.label.getElement().getStyle().setFontSize(16, Unit.PX);
			
			columnCompositePanel.add(heading);
			columnCompositePanel.add(verticalPanel);

			columnCompositePanel.setCellHorizontalAlignment(verticalPanel,
					HasHorizontalAlignment.ALIGN_CENTER);

			// make the column draggable by its heading
			columnDragController.makeDraggable(columnCompositePanel,
					heading.getDragComponent());

		}
	}
	
	public ArrayList<Field> getFields() {

		ArrayList<Field> lst = new ArrayList<Field>();
		int count = vPanel.getWidgetCount();
		for (int i = 0; i < count; i++) {
			Widget w = ((VerticalPanel) vPanel.getWidget(i)).getWidget(0);
			
			if (w instanceof GridColumn) {
				GridColumn col = (GridColumn) w;

				Field fld = col.getField();
				fld.setPosition(i);
				lst.add(fld);
			}
		}

		return lst;
	}
	
}
