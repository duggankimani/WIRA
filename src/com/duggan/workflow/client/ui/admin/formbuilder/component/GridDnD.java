package com.duggan.workflow.client.ui.admin.formbuilder.component;

import java.util.List;

import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.HorizontalPanelDropController;
import com.duggan.workflow.client.ui.component.ActionLink;
import com.duggan.workflow.shared.model.form.Field;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class GridDnD extends AbsolutePanel {

	private static GridDnDUiBinder uiBinder = GWT.create(GridDnDUiBinder.class);

	interface GridDnDUiBinder extends UiBinder<Widget, GridDnD> {
	}

	@UiField HorizontalPanel hPanel;
	private DragHandler handler;
	@UiField ActionLink atxtField;
	@UiField ActionLink aRadioField;
	@UiField ActionLink alblField;
	@UiField ActionLink adateBox;
	@UiField ActionLink achckBox;
	@UiField ActionLink slctField;
	
	private PickupDragController columnDragController;
	private HorizontalPanelDropController columnDropController;
	
	public GridDnD(final List<Field> columns) {
		this.getElement().getStyle().setOverflow(Overflow.VISIBLE);
		this.add(uiBinder.createAndBindUi(this));
		
		handler = new DragHandlerImpl(this){
			@Override
			public void onDragEnd(DragEndEvent event) {
				super.onDragEnd(event);
				Widget parent = event.getContext().draggable.getParent();
				if(!parent.equals(hPanel)){
					//removed
					VerticalPanel panel = (VerticalPanel)event.getContext().draggable;
					GridColumn col = (GridColumn)panel.getWidget(0);
					col.delete();
				}
				
				columns.clear();
				int count = hPanel.getWidgetCount();
				for(int i=0; i<count; i++){
					Widget w = ((VerticalPanel)hPanel.getWidget(i)).getWidget(0);
					
					if(w instanceof GridColumn){
						GridColumn col = (GridColumn)w;
						
						Field fld = col.getField();
						fld.setPosition(i);
						columns.add(fld);
					}
				}
				
			}
			
		};
		
		columnDragController = new PickupDragController(
				this, false){
			@Override
			protected void restoreSelectedWidgetsLocation() {
				// TODO Auto-generated method stub
				//super.restoreSelectedWidgetsLocation();
			}

		};
		
		columnDragController.setBehaviorMultipleSelection(false);
		columnDragController.addDragHandler(handler);
		columnDragController.setBehaviorDragStartSensitivity(5);

		hPanel.addStyleName(CSS_DEMO_INSERT_PANEL_EXAMPLE_CONTAINER);
		hPanel.setSpacing(10);
		this.add(hPanel);

		// initialize our column drop controller
		columnDropController = new HorizontalPanelDropController(
				hPanel);
		columnDragController.registerDropController(columnDropController);

		createColumns(columns);
	}

	private void createColumns(List<Field> columns) {
		for (Field col : columns) {
			
			// initialize a vertical panel to hold the heading and a second vertical
		      // panel
		      VerticalPanel columnCompositePanel = new VerticalPanel();
		    
		      //columnCompositePanel.addStyleName(CSS_DEMO_INSERT_PANEL_EXAMPLE_COLUMN_COMPOSITE);
		      
		   //initialize inner vertical panel to hold individual widgets
		      VerticalPanel verticalPanel = new VerticalPanel();
		      
		      //verticalPanel.addStyleName(CSS_DEMO_INSERT_PANEL_EXAMPLE_CONTAINER);
		      
		      verticalPanel.setSpacing(SPACING);
		     
		      hPanel.add(columnCompositePanel);
				      
		      //Put together the column pieces
		      GridColumn heading = new GridColumn(col);
		      
		      columnCompositePanel.add(heading);
		      columnCompositePanel.add(verticalPanel);

		      // make the column draggable by its heading
		      columnDragController.makeDraggable(columnCompositePanel, heading);

		      
		      for (int row = 1; row <= ROWS; row++) {
		          // initialize a widget
		          HTML widget = new HTML("Row " + ++count);
		          widget.addStyleName("td");
		          verticalPanel.add(widget);
		        }
		}
	}
	
	private void clearCols(){
		hPanel.clear();
	}
	
	public void repaint(List<Field> fields) {
		clearCols();
		createColumns(fields);
	}
	

	int count = 0;
	

	private static final String CSS_DEMO_INSERT_PANEL_EXAMPLE_COLUMN_COMPOSITE = "demo-InsertPanelExample-column-composite";

	private static final String CSS_DEMO_INSERT_PANEL_EXAMPLE_CONTAINER = "tbody tr";

	private static final int ROWS = 3;

	private static final int SPACING = 0;

	
	public ActionLink getAtxtField() {
		return atxtField;
	}

	public ActionLink getaRadioField() {
		return aRadioField;
	}

	public ActionLink getAlblField() {
		return alblField;
	}

	public ActionLink getAdateBox() {
		return adateBox;
	}

	public ActionLink getAchckBox() {
		return achckBox;
	}

	public ActionLink getSlctField() {
		return slctField;
	}

}
