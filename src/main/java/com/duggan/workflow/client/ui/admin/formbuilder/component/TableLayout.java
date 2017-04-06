package com.duggan.workflow.client.ui.admin.formbuilder.component;

import java.util.ArrayList;
import java.util.HashMap;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.DragController;
import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.SimpleDropController;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.AppManager;
import com.duggan.workflow.client.ui.OnOptionSelected;
import com.duggan.workflow.client.ui.admin.formbuilder.component.GridDnD.Options;
import com.duggan.workflow.client.ui.component.ActionLink;
import com.duggan.workflow.client.ui.component.DropDownList;
import com.duggan.workflow.client.ui.events.ReloadGridEvent;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.form.Field;
import com.duggan.workflow.shared.model.form.Property;
import com.duggan.workflow.shared.requests.DeleteTableRowRequest;
import com.duggan.workflow.shared.responses.DeleteTableRowResponse;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class TableLayout extends Grid {

	private static int COLUMNS = 3;

	private static int ROWS = 3;

	private PickupDragController dragController;

	final HashMap<Integer, HashMap<Integer, Field>> fieldRowColumnsMap = new HashMap<Integer, HashMap<Integer, Field>>();

	public TableLayout() {
		super();
		addProperty(new Property(WIDTH, "Width", DataType.STRING));
		addProperty(new Property(ROW_COUNT, "Rows", DataType.STRING));
		addProperty(new Property(COL_COUNT, "Columns", DataType.STRING));

		flextable
				.addStyleName("table table-highlight table-border-visible table-condensed");
		// flextable.getElement().addClassName("table-border-visible");

		// use the boundary panel as this composite's widget
		/*
		 * AbsolutePanel boundaryPanel = new AbsolutePanel();
		 * boundaryPanel.setHeight("250px"); boundaryPanel.setWidth("100%");
		 * add(boundaryPanel);
		 * 
		 * boundaryPanel.add(flextable, 0, 0);
		 * flextable.addStyleName("table flextable form-table");
		 * 
		 * // initialize our drag controller dragController = new
		 * PickupDragController(boundaryPanel, false); // text area to log drag
		 * events as they are triggered final HTML eventTextArea = new HTML();
		 * eventTextArea.setSize(this.getOffsetWidth() + "px", "10em");
		 * 
		 * // instantiate shared drag handler to listen for events final
		 * DemoDragHandler demoDragHandler = new DemoDragHandler(
		 * eventTextArea); dragController.addDragHandler(demoDragHandler);
		 * dragController.addDragHandler(demoDragHandler);
		 * dragController.setBehaviorMultipleSelection(false);
		 */
		// initTable(field);
	}

	@Override
	protected void init() {
		divControls.clear();
		divControls.setWidth("50px");
		divControls.addStyleName("row-fluid");

		gridAdd.setHTML("<i class=\"icon-plus\"></i>");
		gridAdd.setTitle("Add Row");
		gridAdd.getElement().getStyle().setFontSize(14, Unit.PX);
		gridAdd.addStyleName("span6");
		gridAdd.getElement().getStyle().setDisplay(Display.INLINE_BLOCK);

		divControls.add(gridAdd);

		gridAdd.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				setProperty(ROW_COUNT, (flextable.getRowCount() + 1) + "");
				save();
			}
		});

		dragDrop = createWidgetControls(Options.MOVE);
		dragDrop.addStyleName("span6");
		divControls.add(dragDrop);

		// Add drag drop controls
		shim = dragDrop.getMoveWidget();
		divControls.add(dragDrop);
	}

	protected void initTable(Field aField) {
		// if (dragController == null) {
		// // super() may call this method before dragController is initd
		// return;
		// }

		// Unregister previous drop controllers
		// dragController.unregisterDropControllers();

		ROWS = getIntValue(ROW_COUNT, ROWS);
		COLUMNS = getIntValue(COL_COUNT, COLUMNS);

		if (getPropertyValue(WIDTH) != null) {
			flextable.setWidth(getPropertyValue(WIDTH));
		}

		clearTable();

		// create our grid
		for (int i = 0; i < ROWS; i++) {

			if (fieldRowColumnsMap == null) {
				break;
			}
			addRow();
		}

		if (designMode) {
			// Init FormBuilder Controls
			if (!field.getFields().isEmpty()) {
				String colWidth = getColWidth(field.getFields().get(0));
				if (colWidth != null) {
					flextable.getFlexCellFormatter().setWidth(rowCount, 0,
							colWidth);
				}
			}
			flextable.setWidget(0, flextable.getCellCount(0), divControls);
		}
	}

	protected void addRow() {
		int rowColumns = COLUMNS;
		int i = flextable.getRowCount();
		String cell_width = (100 / COLUMNS) + "%";

		HashMap<Integer, Field> columnMap = fieldRowColumnsMap.get(i);

		for (int j = 0; j < rowColumns; j++) {
			// create a simple panel drop target for the current cell
			FocusPanel simplePanel = new FocusPanel();
			simplePanel.getElement().getStyle().setDisplay(Display.BLOCK);
			simplePanel.setWidth("100%");
			if (designMode) {
				simplePanel.addClickHandler(new PanelSelectionHandler(i, j));
			}
			flextable.setWidget(i, j, simplePanel);

			Field colField = null;
			if (columnMap != null) {
				colField = columnMap.get(j);
			}

			int colSpan = 0;
			if (colField != null) {
				createWidget(simplePanel, colField);
				colSpan = getColSpan(colField);
				if (colSpan > 1) {
					flextable.getFlexCellFormatter().setColSpan(i, j, colSpan);
				}
			} else {
				simplePanel.setHeight("30px");
			}

			flextable.getFlexCellFormatter().setWidth(i, j, cell_width);

			// instantiate a drop controller of the panel in the current
			// cell
			// SetWidgetDropController dropController = new
			// SetWidgetDropController(
			// simplePanel);
			// dragController.registerDropController(dropController);
			if (colSpan > 1) {
				rowColumns -= colSpan - 1;
			}
		}

		if (designMode) {
			ActionLink link = newDelete();
			link.addClickHandler(new DeleteRowHandler(i, rowColumns));
			flextable.setWidget(i, rowColumns, link);
		}

	}

	private int getColSpan(Field colField) {
		String colSpan = colField.getProperty(COLSPAN);
		if (colSpan != null && colSpan.trim().matches("\\d+")) {
			return Integer.parseInt(colSpan.trim());
		}
		return 1;
	}

	private int getIntValue(String propertyName, int defaultValue) {
		Object val = getValue(propertyName);

		int value = defaultValue;
		try {
			if (val != null) {
				value = Integer.parseInt(val.toString().trim());
			}
		} catch (Exception e) {
			value = defaultValue;
		}
		return value;
	}

	private void createWidget(FocusPanel simplePanel, Field colField) {
		if (colField == null) {
			return;
		}

		FieldWidget fw = FieldWidget.getWidget(colField.getType(), colField,
				designMode);
		fw.addProperty(new Property(COLSPAN, "Colspan", DataType.STRING));
		fw.setField(colField);

		fw.gridFormat(true);
		simplePanel.clear();
		simplePanel.setWidget(fw);
	}

	@Override
	public void setField(Field field) {
		fieldRowColumnsMap.clear();
		for (Field child : field.getFields()) {
			int row = child.getRow();
			int col = child.getCol();
			if (row == -1 || col == -1) {
				continue;
			}

			HashMap<Integer, Field> columnMap = fieldRowColumnsMap.get(row);
			if (columnMap == null) {
				columnMap = new HashMap<Integer, Field>();
				fieldRowColumnsMap.put(row, columnMap);
			}
			columnMap.put(col, child);
		}
		super.setField(field);

	}

	@Override
	public void clearTable() {
		divControls.removeFromParent();
		flextable.removeAllRows();
		super.clearTable();
	}
	

	protected void deleteTableRow(int row) {
		if(field==null || field.getRefId()==null){
			return;
		}
		
		Window.alert("Initial Rows = "+field.getProperty(ROW_COUNT));
		DeleteTableRowRequest request = new DeleteTableRowRequest(field.getRefId(), row);
		AppContext.getDispatcher().execute(request, new TaskServiceCallback<DeleteTableRowResponse>() {
			@Override
			public void processResult(DeleteTableRowResponse aResponse) {
				field = aResponse.getField();
				Window.alert("New Rows = "+field.getProperty(ROW_COUNT));
				setField(field);
			}
		});
	}

	@Override
	public void onReloadGrid(ReloadGridEvent event) {
	}

	@Override
	public void setValue(Object value) {
	}

	@Override
	public FieldWidget cloneWidget() {
		return new TableLayout();
	}

	@Override
	protected DataType getType() {
		return DataType.TABLE;
	}

	protected Widget createDraggable() {
		return createDraggablePumpkinImage(dragController);
	}

	/**
	 * Create a new draggable pumpkin image.
	 * 
	 * @param dragController
	 *            the drag controller
	 * @return the new widget
	 */
	public static Widget createDraggablePumpkinImage(
			DragController dragController) {
		Image image = new Image("img/99pumpkin2-65x58.jpg");
		dragController.makeDraggable(image);
		return image;
	}

	/**
	 * Create a new draggable red box widget.
	 * 
	 * @param dragController
	 *            the drag controller
	 * @return the new widget
	 */
	public static Widget createDraggableRedBox(DragController dragController) {
		Widget redBox = new RedBoxDraggableWidget();
		dragController.makeDraggable(redBox);
		return redBox;
	}

	/**
	 * 
	 * @author duggan
	 *
	 */
	public class SetWidgetDropController extends SimpleDropController {

		private final SimplePanel dropTarget;

		public SetWidgetDropController(SimplePanel dropTarget) {
			super(dropTarget);
			this.dropTarget = dropTarget;

			// dropTarget.addAttachHandler(new AttachEvent.Handler() {
			// @Override
			// public void onAttachOrDetach(AttachEvent event) {
			// if (!event.isAttached()) {
			// Window.alert(">>> Unregister Drag Drop");
			// dragController
			// .registerDropController(SetWidgetDropController.this);
			// }
			// }
			// });
		}

		@Override
		public void onDrop(DragContext context) {
			dropTarget.setWidget(context.draggable);
			super.onDrop(context);
		}

		@Override
		public void onPreviewDrop(DragContext context) throws VetoDragException {
			if (dropTarget.getWidget() != null) {
				throw new VetoDragException();
			}
			super.onPreviewDrop(context);
		}
	}

	/*
	 * Demonstrate a draggable widget.
	 */
	public static final class RedBoxDraggableWidget extends HTML {

		private static int counter;

		private static final String CSS_DEMO_RED_BOX_DRAGGABLE_WIDGET = "demo-red-box-draggable-widget";

		private static final int DRAGGABLE_SIZE = 65;

		public RedBoxDraggableWidget() {
			setPixelSize(DRAGGABLE_SIZE, DRAGGABLE_SIZE);
			setHTML("<i>drag me!</i> draggable widget #" + ++counter);
		}

		@Override
		protected void onLoad() {
			super.onLoad();
			addStyleName(CSS_DEMO_RED_BOX_DRAGGABLE_WIDGET);
		}
	}

	/**
	 * Shared drag handler which display events as they are received by the
	 * various drag controllers.
	 */
	public class DemoDragHandler implements DragHandler {

		/**
		 * CSS blue.
		 */
		public static final String BLUE = "#4444BB";

		/**
		 * CSS green.
		 */
		public static final String GREEN = "#44BB44";

		/**
		 * CSS red.
		 */
		public static final String RED = "#BB4444";

		/**
		 * Text area where event messages are shown.
		 */
		private final HTML eventTextArea;

		DemoDragHandler(HTML dragHandlerHTML) {
			eventTextArea = dragHandlerHTML;
		}

		/**
		 * Log the drag end event.
		 * 
		 * @param event
		 *            the event to log
		 */
		@Override
		public void onDragEnd(DragEndEvent event) {
			log("onDragEnd: " + event, RED);
		}

		/**
		 * Log the drag start event.
		 * 
		 * @param event
		 *            the event to log
		 */
		@Override
		public void onDragStart(DragStartEvent event) {
			log("onDragStart: " + event, GREEN);
		}

		/**
		 * Log the preview drag end event.
		 * 
		 * @param event
		 *            the event to log
		 * @throws VetoDragException
		 *             exception which may be thrown by any drag handler
		 */
		@Override
		public void onPreviewDragEnd(DragEndEvent event)
				throws VetoDragException {
			log("<br>onPreviewDragEnd: " + event, BLUE);
		}

		/**
		 * Log the preview drag start event.
		 * 
		 * @param event
		 *            the event to log
		 * @throws VetoDragException
		 *             exception which may be thrown by any drag handler
		 */
		@Override
		public void onPreviewDragStart(DragStartEvent event)
				throws VetoDragException {
			log("onPreviewDragStart: " + event, BLUE);
		}

		public void clear() {
			eventTextArea.setHTML("");
		}

		public void log(String text, String color) {
			eventTextArea.setHTML(eventTextArea.getHTML()
					+ (eventTextArea.getHTML().length() == 0 ? "" : "<br>")
					+ "<span style='color: " + color + "'>" + text + "</span>");
		}
	}

	public class SelectFieldWidget extends Composite {

		DropDownList<DataType> dataTypes = null;

		DataType selectedType = null;

		HTMLPanel container = new HTMLPanel("");

		public SelectFieldWidget() {
			initWidget(container);

			dataTypes = new DropDownList<DataType>();

			ArrayList<DataType> types = new ArrayList<DataType>();
			types.add(DataType.LABEL);
			types.add(DataType.BOOLEAN);
			types.add(DataType.CHECKBOX);
			types.add(DataType.CHECKBOXGROUP);
			types.add(DataType.SELECTBASIC);
			types.add(DataType.DATE);
			types.add(DataType.DOUBLE);
			types.add(DataType.STRING);
			types.add(DataType.STRINGLONG);
			types.add(DataType.FILEUPLOAD);

			dataTypes.setItems(types);

			container.add(dataTypes);

			dataTypes.addValueChangeHandler(new ValueChangeHandler<DataType>() {

				@Override
				public void onValueChange(ValueChangeEvent<DataType> evt) {
					DataType type = evt.getValue();
					selectedType = type;
				}
			});
		}

		public DataType getSelectedType() {
			return selectedType;
		}
	}

	public class DeleteRowHandler implements ClickHandler {

		int col = 0;
		int row = 0;

		public DeleteRowHandler(int row, int col) {
			this.row = row;
			this.col = col;
		}

		@Override
		public void onClick(ClickEvent evt) {
			StringBuffer captions = new StringBuffer();
			HashMap<Integer, Field> rowMap = fieldRowColumnsMap.get(row);
			if(rowMap!=null){
				for(Field field: rowMap.values()){
					String caption = field.getCaption();
					captions.append(caption+",");
				}
			}
			
			String message = "Do you want to delete this row? <br/>";
			if(!captions.toString().isEmpty()){
				message = message+"This will also delete fields ["+captions.toString()+"]";
			}
			HTML html = new HTML(message);
			AppManager.showPopUp("Delete row "+row, html,
					new OnOptionSelected() {
						
						@Override
						public void onSelect(String name) {
							if(name.equals("Yes")){
								deleteTableRow(row);
							}
						}
					}, "Yes", "Cancel");
		}
	}

	public class PanelSelectionHandler implements ClickHandler {

		int col = 0;
		int row = 0;

		public PanelSelectionHandler(int row, int col) {
			this.row = row;
			this.col = col;
		}

		@Override
		public void onClick(ClickEvent evt) {
			final FocusPanel source = (FocusPanel) evt.getSource();
			if (source.getWidget() != null) {
				// Ignore
				return;
			}

			final SelectFieldWidget selectPanel = new SelectFieldWidget();
			AppManager.showPopUp("Select Widget Type", selectPanel,
					new OnOptionSelected() {

						@Override
						public void onSelect(String name) {

							if (name.equals("Ok")) {
								DataType type = selectPanel.getSelectedType();

								boolean clear = true;

								if (source.getWidget() != null) {
									Widget w = source.getWidget();
									if (w instanceof FieldWidget) {
										FieldWidget widget = (FieldWidget) w;
										DataType fieldType = widget.getType();

										if (type != fieldType) {
											clear = false;
										}
									}
								}

								if (clear) {
									source.clear();
									Field child = new Field();
									child.setType(type);
									child.setCell(row, col);
									addColumn(child);
								}
							}
						}
					}, "Ok", "Cancel");
		}
	};

}
