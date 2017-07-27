package com.duggan.workflow.client.ui.admin.formbuilder.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.duggan.workflow.client.ui.AppManager;
import com.duggan.workflow.client.ui.OnOptionSelected;
import com.duggan.workflow.client.ui.OptionControl;
import com.duggan.workflow.client.ui.admin.formbuilder.HasProperties;
import com.duggan.workflow.client.ui.admin.formbuilder.component.GridDnD.Options;
import com.duggan.workflow.client.ui.component.ActionLink;
import com.duggan.workflow.client.ui.events.AfterDeleteLineEvent;
import com.duggan.workflow.client.ui.events.DeleteLineEvent;
import com.duggan.workflow.client.ui.events.DeleteLineEvent.DeleteLineHandler;
import com.duggan.workflow.client.ui.events.EditLineEvent;
import com.duggan.workflow.client.ui.events.EditLineEvent.EditLineHandler;
import com.duggan.workflow.client.ui.events.ReloadGridEvent;
import com.duggan.workflow.client.ui.events.ReloadGridEvent.ReloadGridHandler;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.client.util.ENV;
import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.DocumentLine;
import com.duggan.workflow.shared.model.GridValue;
import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.model.form.Field;
import com.duggan.workflow.shared.model.form.Property;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;

public class Grid extends FieldWidget implements DeleteLineHandler, ReloadGridHandler, EditLineHandler {

	private static GridUiBinder uiBinder = GWT.create(GridUiBinder.class);

	interface GridUiBinder extends UiBinder<Widget, Grid> {
	}

	int rowCount = 0;

	protected HashMap<String, HTMLLine> htmlLines = new HashMap<String, HTMLLine>();
	@UiField FlexTable flextable;
	protected HTMLPanel divControls = new HTMLPanel("");
	protected Anchor gridAdd = new Anchor();
	protected GridDnD dragDrop = createWidgetControls(Options.MOVE,Options.SORT,Options.FIELDS);

	public Grid() {
		super();
		addProperty(new Property(MANDATORY, "Mandatory", DataType.CHECKBOX,
				refId));
		addProperty(new Property(READONLY, "Read Only", DataType.CHECKBOX));
		add(uiBinder.createAndBindUi(this));
		init();
	}
	
	protected void init() {
		initTable(field);
		gridAdd.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addLine(new DocumentLine());
			}
		});
		
		//Add drag drop controls
		shim = dragDrop.getMoveWidget();
		divControls.add(dragDrop);
		createDesignGrid();
	}
	
	protected GridDnD createWidgetControls(Options ... controls) {
		GridDnD dragDrop = new GridDnD(controls) {
			@Override
			protected void save(ArrayList<Field> fields) {
				field.setFields(fields);
				Grid.this.save(field);
			}
			
			public void configureColumns() {
				final ColumnConfigPanel config = new ColumnConfigPanel(field.getFields());
				
				AppManager.showPopUp("Configure Columns", config, new OptionControl(){
					@Override
					public void onSelect(String name) {
						if(name.equals("Save")){
							save(config.getFields());
							hide();
						}else{
							hide();
						}
						
					}
				}, "Save", "Cancel");
			};
			
		};
		
		return dragDrop;
	}
	
	
	
	protected void initTable(Field aField) {
		clearTable();
		
		rowCount = 0;
		int columnCount = aField.getFields()==null? 0 : aField.getFields().size();
		
		//Init Add rows
		gridAdd.addStyleName("btn btn-small");
		gridAdd.setHTML("<i class=\"icon-plus\"></i>Add Line");
		flextable.setWidget(rowCount, 0, gridAdd);
		
		if(designMode){
			//Init FormBuilder Controls
			if(!field.getFields().isEmpty()){
				String colWidth = getColWidth(field.getFields().get(0));
				if(colWidth!=null){
					flextable.getFlexCellFormatter().setWidth(rowCount, 0, colWidth);
				}
			}
			flextable.setWidget(0,columnCount+1,divControls); 
		}else{
			//To align with row delete button 
			flextable.addCell(0);
		}
		
		//Table has no columns, create default col
		if(columnCount==0){
			InlineLabel col0 = new InlineLabel("Column 0");
			flextable.setWidget(++rowCount, 0, col0);
			
			//Init Sample Field
			flextable.setWidget(++rowCount,0,new TextField());

			//Init sample delete
			Anchor rowDelete = newDelete();
			flextable.setWidget(rowCount, 1, rowDelete);
			flextable.getCellFormatter().addStyleName(rowCount, 1, "delete");
		}else{
			++rowCount;
			
			for(int col = 0; col<aField.getFields().size(); col++){
				Field gridCol = aField.getFields().get(col);
				
				flextable.setWidget(rowCount, col, createGridCol(gridCol));
				flextable.getCellFormatter().addStyleName(rowCount, col, "th");
				
				String width = getColWidth(gridCol);
				if(width!=null){
					flextable.getFlexCellFormatter().setWidth(rowCount, col, width);
				}
				
			}
			
		}
		
	}

	protected String getColWidth(Field gridCol) {
		String percWidth = gridCol.getProperty(HasProperties.COLWIDTH);
		return percWidth;
	}

	private Widget createGridCol(Field col) {
		
		return new GridColumn(col, designMode){
			@Override
			public void removeColumn() {
				Field removed = this.getField();
				field.getFields().remove(removed);
				int headerRow = 1;
				int colCount = flextable.getCellCount(headerRow);//Row 1 has table headers
				int colNum = -1;
				for(int i=0; i< colCount; i++){
					if(flextable.getWidget(headerRow, i) == this){
						colNum = i;
						break;
					}
				}
				
				if(colNum!=-1)
				for(int i=0; i< flextable.getRowCount(); i++){
					flextable.removeCell(i, colNum);
				}
				
			}
		};
	}

	protected ActionLink newDelete() {
		ActionLink rowDelete = new ActionLink();
		rowDelete.addStyleName("red");
		rowDelete.setHTML("<i class=\"icon-remove-circle\"></i>");
		return rowDelete;
	}

	@Override
	public void onDragEnd() {
		super.onDragEnd();
		
	}
	
	/**
	 * Add shim to the grid widget
	 */
	@Override
	public void addShim(int left, int top, int offSetWidth, int offSetHeight) {
		//Override
		//do nothing
	}
	
	@Override
	protected void initShim() {
		//Override - Do nothing
	}
	
	@Override
	public Widget getDragHandle() {
		return dragDrop.getMoveWidget();
	}
	
	public native JavaScriptObject getLeftPosition(Element element)/*-{
		var top = 0, left = 0;
	    do {
	        top += element.offsetTop  || 0;
	        left += element.offsetLeft || 0;
	        element = element.offsetParent;
	    } while(element);
	
	    return {
	        top: top,
	        left: left
	    };
	}-*/;
	
	@Override
	public void setField(Field field) {
		this.field = field;
		setProperties(field.getProps());
		for(Field child: field.getFields()){
			child.setForm(field.getFormId(), field.getFormRef());
			child.setParent(field.getId(), field.getRefId());
			child.setGridName(field.getName());
			child.setDocId(field.getDocId());
			child.setDocRefId(field.getDocRefId());
		}
		initTable(field);
		super.setField(field);
		
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		addRegisteredHandler(DeleteLineEvent.getType(), this);
	}
	
	/**
	 * Design Grid
	 */
	private void createDesignGrid() {
		this.getElement().getStyle().setPaddingBottom(20, Unit.PX);
		this.getElement().getStyle().setOverflow(Overflow.VISIBLE);

		dragDrop.getAtxtField().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addColumn(DataType.STRING);
			}
		});

		dragDrop.getAlblField().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addColumn(DataType.LABEL);
			}
		});

		dragDrop.getAchckBox().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addColumn(DataType.CHECKBOX);
			}
		});

		dragDrop.getAdateBox().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addColumn(DataType.DATE);
			}
		});

		dragDrop.getaRadioField().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addColumn(DataType.BOOLEAN);
			}
		});
		dragDrop.getSlctField().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addColumn(DataType.SELECTBASIC);
			}
		});

		dragDrop.getNumField().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addColumn(DataType.DOUBLE);
			}
		});
		
		dragDrop.getFileUploadField().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addColumn(DataType.FILEUPLOAD);
			}
		});

		dragDrop.getCurrField().addClickHandler(new ClickHandler() {

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

	protected void addColumn() {
		addColumn(DataType.LABEL);
	}

	protected void addColumn(DataType type) {
		Field child = new Field();
		child.setType(type);

		addColumn(child);

	}

	protected void addColumn(Field child) {
		child.setParent(null, field.getRefId());
		int pos = field.getFields().size();
		child.setPosition(pos);
		child.setCaption("Column " + (pos));
		child.setParent(field.getFormId(), field.getFormRef());
		field.addField(child); // child has not properties
		save(field);
	}

	@Override
	public void onAfterSave() {
		dragDrop.repaint(field.getFields());
	}
	
	@Override
	protected void registerHandlers() {
		super.registerHandlers();
		addRegisteredHandler(EditLineEvent.TYPE, this);
		addRegisteredHandler(DeleteLineEvent.getType(), this);
		addRegisteredHandler(ReloadGridEvent.getType(), this);
	}
	
	@Override
	public void onReloadGrid(ReloadGridEvent event) {
		Field src = event.getField();
		if(src.getParentRef().equals(field.getRefId())){
			initTable(field);
			addLine(new DocumentLine());
		}
	}

	@Override
	public void setReadOnly(boolean isReadOnly) {
		this.readOnly = isReadOnly || isComponentReadOnly();
		if(readOnly){
			if(designMode){
				flextable.getFlexCellFormatter().addStyleName(0,0, "hide");
			}else{
				flextable.getRowFormatter().addStyleName(0, "hide");
			}
		}else{
			if(designMode){
				flextable.getFlexCellFormatter().removeStyleName(0,0, "hide");
			}else{
				flextable.getRowFormatter().removeStyleName(0, "hide");
			}
		}
		
		for(HTMLLine line: htmlLines.values()){
			line.setReadonly(this.readOnly);
		}
	}
	@Override
	public Widget getInputComponent() {
		return this;
	}

	@Override
	public FieldWidget cloneWidget() {
		return new Grid();
	}

	@Override
	protected DataType getType() {
		return DataType.GRID;
	}

	@Override
	public void setComponentValid(boolean isValid) {

	}

	@Override
	public Element getViewElement() {
		return null;
	}
	
	@Override
	public Value getFieldValue() {

		GridValue value = new GridValue();
		value.setKey(field.getName());
		Collection<DocumentLine> lines = getDocumentLines(); 
		value.setValue(lines);
		
		return value;
	}
	
	public Collection<DocumentLine> getDocumentLines() {
		ArrayList<DocumentLine> lines = new ArrayList<DocumentLine>();
		for (HTMLLine line : htmlLines.values()) {
			DocumentLine doc = line.getDocument();
			if (line != null && !doc.isEmpty()) {
				lines.add(doc);
			}
		}
		return lines;
	}

	protected void addLine(DocumentLine documentLine) {
		HTMLLine line = new HTMLLine();
		line.setDocument(documentLine);
		htmlLines.put(line.getDocument().getTempId()+"", line);
	}

	@Override
	public void onDeleteLine(DeleteLineEvent event) {
		if (event.getGridName() == null
				|| !event.getGridName().equals(field.getName())) {
			return;
		}

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
	
	private void deleteLines(boolean fireDeleteFromDB) {
		HashMap<String, HTMLLine> toDelete = new HashMap<String, HTMLLine>();
		toDelete.putAll(htmlLines);
		for (HTMLLine line : toDelete.values()) {
			if(line!=null) {
				line.deleteLine(fireDeleteFromDB);
			}
			
		}
	}
		
	@Override
	public void setValue(Object value) {
		setValue(value, true);
	}
	
	public void setValue(Object value, boolean fireDeleteFromDB) {
		deleteLines(fireDeleteFromDB);
		if (value != null) {
			@SuppressWarnings("unchecked")
			Collection<DocumentLine> lines = (Collection<DocumentLine>) value;
			setLines(lines);
		} else {
			// Generate Empty row
			Collection<DocumentLine> lines = new ArrayList<DocumentLine>();
			lines.add(new DocumentLine());
			setLines(lines);
		}
	}

	private void setLines(Collection<DocumentLine> doclines) {
		for (DocumentLine line : doclines) {
			addLine(line);
		}
	}
	
	
	public void clearTable() {
		flextable.clear();
		htmlLines.clear();
	}


	class HTMLLine {

		private ArrayList<FieldWidget> inputs = new ArrayList<FieldWidget>();
		private DocumentLine documentLine;
		private int row = -1;
		final ActionLink deleteLink;

		public HTMLLine() {
			row = ++rowCount;
			int i=0;
			int col = 0;
			for(; i< field.getFields().size(); i++, col++){
				Field child = field.getFields().get(i);//.clone(true);
				Field clone = child.clone(true);
				FieldWidget fw = FieldWidget.getWidget(child.getType(), clone, false);
				inputs.add(fw);
				fw.gridFormat(true);

				flextable.setWidget(row, col, fw);
			}
			
			deleteLink = newDelete();
			deleteLink.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					deleteLine();
				}
			});

			flextable.setWidget(row, col, deleteLink);
		}

		public void setReadonly(boolean isReadOnly) {
			for (FieldWidget input : inputs) {
				input.setReadOnly(isReadOnly);
			}

			if (isReadOnly) {
				deleteLink.addStyleName("hide");
			} else {
				deleteLink.removeStyleName("hide");
			}
		}

		protected void deleteLine() {
			deleteLine(true);
		}
		
		protected void deleteLine(boolean isDeleteFromDb) {
			ArrayList<String> list = new ArrayList<String>();
			if(htmlLines!=null && !htmlLines.isEmpty()) {
				list.addAll(htmlLines.keySet());
			}
			
			String docLineTid = documentLine.getTempId()+"";
			int idx = list.indexOf(docLineTid);
			int tableRow = idx + 2;

			//This event must be fired before the the row is removed from the DOM, otherwise events wont work!
			AppContext.fireEvent(new AfterDeleteLineEvent(documentLine, isDeleteFromDb));
			
			//Remove row from table
			flextable.removeRow(tableRow);
			for (FieldWidget widget : inputs) {
				Field child = widget.getField();
				ENV.removeContext(child);
				widget.onUnload();// disable listeners
			}
			htmlLines.remove(documentLine.getTempId()+"");
			--rowCount;
		}

		public DocumentLine getDocument() {
			if (documentLine != null) {
				for (FieldWidget fw : inputs) {
					Value value = fw.getFieldValue();
					if (value != null) {
						documentLine.addValue(fw.getField().getName(),
								fw.getFieldValue());
					}
				}
				return documentLine;
			}
			return null;
		}

		public void setDocument(DocumentLine docLine) {
			this.documentLine = docLine;
			documentLine.setTempId(new Long(row));
			documentLine.setDocRefId(field.getDocRefId());
//			documentLine.setId(field.getId());
			documentLine.setName(field.getName());
			deleteLink.setModel(docLine);
			for (FieldWidget widget : inputs) {
				Field child = widget.getField();
				child.setGridName(field.getName());	
				child.setDocId(field.getDocId());
				child.setDocRefId(field.getDocRefId());
				child.setLineRefId(documentLine.getTempId());
				child.setValue(documentLine.getValue(child.getName()));

				widget.setField(child);
			}

		}

	}

}
