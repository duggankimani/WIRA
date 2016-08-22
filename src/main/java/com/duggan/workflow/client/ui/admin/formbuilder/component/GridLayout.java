package com.duggan.workflow.client.ui.admin.formbuilder.component;

import java.util.ArrayList;
import java.util.Collection;

import com.duggan.workflow.client.ui.AppManager;
import com.duggan.workflow.client.ui.OnOptionSelected;
import com.duggan.workflow.client.ui.admin.formbuilder.grid.GridView;
import com.duggan.workflow.client.ui.events.EditLineEvent;
import com.duggan.workflow.client.ui.events.EditLineEvent.EditLineHandler;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.BooleanValue;
import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.DocumentLine;
import com.duggan.workflow.shared.model.GridValue;
import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.model.form.Field;
import com.duggan.workflow.shared.model.form.Property;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.duggan.workflow.client.ui.component.*;

public class GridLayout extends FieldWidget implements EditLineHandler {

	private static GridLayoutUiBinder uiBinder = GWT
			.create(GridLayoutUiBinder.class);

	interface GridLayoutUiBinder extends UiBinder<Widget, GridLayout> {
	}

	@UiField
	Label lblEl;
	@UiField
	HTMLPanel divControls;
	@UiField
	Anchor btnAdd;

	GridDnD grid = null;

	private Widget widget;

	public GridLayout() {
		super();
		addProperty(new Property(MANDATORY, "Mandatory", DataType.CHECKBOX,
				refId));
		addProperty(new Property(READONLY, "Read Only", DataType.CHECKBOX));

		widget = uiBinder.createAndBindUi(this);
		add(widget);
		DOM.setStyleAttribute(getElement(), "overflow", "auto");

		btnAdd.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addColumn();
			}
		});

		AppContext.getEventBus().addHandler(EditLineEvent.TYPE, this);
	}

	public GridLayout(Element element, boolean designMode) {
		super();
		addProperty(new Property(MANDATORY, "Mandatory", DataType.CHECKBOX,
				refId));
		addProperty(new Property(READONLY, "Read Only", DataType.CHECKBOX));

		// Set
		setProperty(NAME, element.getId());
		field.setName(element.getId());
		setProperty(HELP, element.getTitle());
		// field Properties update
		field.setProperties(getProperties());

		final String id = element.getId();
		String addLineId = element.getId() + "_Add";

		JavaScriptObject row = getGridRowTemplate(id);
		if (row != null) {
			final Element newRow = Element.as(row);
			Element addLine = DOM.getElementById(addLineId);
			String nodeType = addLine.getNodeName().toLowerCase();
			boolean isAnchor = nodeType.equals("a");
			if(!isAnchor){
				GWT.log("Invalid 'Add Button' element type <"+nodeType+"> for Grid template '"+id+"' ");
			}
			if (addLine != null && isAnchor) {
				ActionLink link = ActionLink.wrap(addLine, true);
				link.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						Element parent = newRow.getParentElement();
						if (parent != null) {
							Element child = DOM.clone(newRow, true);
							child.removeClassName("grid_row_template");
							parent.appendChild(child);

							Element delete = null;
							NodeList<Element> nodes = child.getElementsByTagName("a");
							if(nodes.getLength()>0){
								for(int i=0; i<nodes.getLength(); i++){
									String nodeId = nodes.getItem(i).getId();
									if(nodeId!=null && nodeId.endsWith("_Delete")){
										delete = nodes.getItem(i);
										break;
									}
								}
							}
							
							if (delete != null) {
								ActionLink deleteLink = ActionLink.wrap(delete,
										true);
								deleteLink.setModel(child);
								deleteLink.addClickHandler(new ClickHandler() {

									@Override
									public void onClick(ClickEvent event) {
										Element elementRow = (Element) ((ActionLink) event
												.getSource()).getModel();
										elementRow.removeFromParent();
									}
								});
							}
						}
					}
				});
			}
		}

	}

	private native JavaScriptObject getGridRowTemplate(String id) /*-{
																	var div = $doc.getElementById(id);
																	var element = $wnd.jQuery(div).find('.grid_row_template').get(0);
																	return element;
																	}-*/;

	@Override
	protected void afterInit() {
		if (designMode) {
			showDesignGrid();
		}
	}

	/**
	 * Design Grid
	 */
	private void showDesignGrid() {
		divControls.clear();
		field.sortFields();
		grid = new GridDnD(field.getFields()) {
			@Override
			protected void save(ArrayList<Field> fields) {
				field.setFields(fields);
				GridLayout.this.save(field);
			}
		};

		divControls.add(grid);
		this.getElement().getStyle().setPaddingBottom(20, Unit.PX);
		this.getElement().getStyle().setOverflow(Overflow.VISIBLE);

		grid.getAtxtField().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addColumn(DataType.STRING);
			}
		});

		grid.getAlblField().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addColumn(DataType.LABEL);
			}
		});

		grid.getAchckBox().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addColumn(DataType.CHECKBOX);
			}
		});

		grid.getAdateBox().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addColumn(DataType.DATE);
			}
		});

		grid.getaRadioField().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addColumn(DataType.BOOLEAN);
			}
		});
		grid.getSlctField().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addColumn(DataType.SELECTBASIC);
			}
		});

		grid.getNumField().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addColumn(DataType.DOUBLE);
			}
		});

		grid.getCurrField().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Field field = new Field();
				field.setType(DataType.DOUBLE);
				ArrayList<Property> values = new ArrayList<Property>();
				values.add(new Property(CURRENCY, "Currency",
						DataType.SELECTBASIC));
				field.setProperties(values);
				addColumn(field);
			}
		});

	}

	private void setLines(Collection<DocumentLine> doclines) {
		if (designMode) {
			// design mode
			return;
		}

		divControls.clear();

		assert field != null;
		assert field.getFields() != null;

		field.sortFields();
		GridView view = new GridView(field);
		view.setData(doclines);
		divControls.add(view);
		view.setReadOnly(isReadOnly());
	}

	@Override
	public void addShim(int left, int top, int offSetWidth, int offSetHeight) {
		offSetHeight = 125;
		// if(showShim){
		top = 30;
		getShim().setPixelSize(offSetWidth, offSetHeight - top);
		// }
		// System.err.println("####### top="+top);
		super.addShim(left, top, offSetWidth, offSetHeight);
	}

	@Override
	public void activateShimHandler() {
		getShim().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				AppManager.showPropertyPanel(field, getProperties());
			}
		});
	}

	@Override
	public FieldWidget cloneWidget() {
		GridLayout layout = new GridLayout();
		// layout.setField(field);
		return layout;
	}

	@Override
	protected DataType getType() {
		return DataType.GRID;
	}

	protected void addColumn() {
		addColumn(DataType.LABEL);
	}

	protected void addColumn(DataType type) {
		Field child = new Field();
		child.setType(type);

		addColumn(child);

	}

	private void addColumn(Field child) {
		child.setParent(null, field.getRefId());
		int pos = field.getFields().size();
		child.setPosition(pos);
		child.setCaption("Column " + (pos));

		field.addField(child); // child has not properties
		save(field);
	}

	@Override
	public void onAfterSave() {
		assert field != null;

		if (grid == null) {
			showDesignGrid();
		}
		assert grid != null;

		grid.repaint(field.getFields());
	}

	/**
	 * Runtime - Edit line
	 */
	@Override
	public void onEditLine(EditLineEvent event) {
		DocumentLine line = event.getLine();
		HTMLPanel panel = new HTMLPanel("");
		for (String key : line.getValues().keySet()) {

			Value val = line.getValue(key);

			panel.add(new HTML(key + " : "
					+ (val == null ? "" : val.getValue())));
		}

		AppManager.showPopUp("Values", panel, new OnOptionSelected() {

			@Override
			public void onSelect(String name) {

			}
		}, "OK");
	}

	public Collection<DocumentLine> getDocumentLines() {
		Widget child = divControls.getWidget(0);
		if (!(child instanceof GridView)) {
			return new ArrayList<DocumentLine>();
		}
		GridView gridView = (GridView) child;
		Collection<DocumentLine> documentLines = gridView.getRecords();

		return documentLines;
	}

	@Override
	public void setValue(Object value) {
		if (value != null) {
			// System.err.println("lines");
			@SuppressWarnings("unchecked")
			Collection<DocumentLine> lines = (Collection<DocumentLine>) value;
			setLines(lines);
		} else {
			Collection<DocumentLine> lines = new ArrayList<DocumentLine>();
			lines.add(new DocumentLine());
			setLines(lines);
		}
	}

	@Override
	public Value getFieldValue() {

		GridValue value = new GridValue();
		value.setKey(field.getName());
		value.setValue(getDocumentLines());

		return value;
	}

	@Override
	protected void onUnload() {
		super.onUnload();
	}

	@Override
	public void setReadOnly(boolean isReadOnly) {
		if (!designMode) {
			// not design mode
			this.readOnly = isReadOnly || isComponentReadOnly();

			Widget child = divControls.getWidget(0);

			if (child instanceof GridView) {
				GridView view = (GridView) child;
				view.setReadOnly(this.readOnly);
			}
		}
	}

	/**
	 * Synchronization of the actual column Field values with the ArrayList of
	 * fields in this Grid's field model.
	 */
	@Override
	public void save() {
		if (grid != null) {
			// design mode
			field.setFields(grid.getFields());
		}
		super.save();
	}

	@Override
	protected void setFormula(String formula) {
		super.setFormula(formula);
	}

	@Override
	public Widget getInputComponent() {
		return this;
	}

	@Override
	public Element getViewElement() {
		return null;
	}

	@Override
	public void setComponentValid(boolean isValid) {

	}
}
