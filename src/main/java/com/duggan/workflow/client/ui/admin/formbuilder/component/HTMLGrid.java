package com.duggan.workflow.client.ui.admin.formbuilder.component;

import java.util.ArrayList;
import java.util.Collection;

import com.duggan.workflow.client.ui.component.ActionLink;
import com.duggan.workflow.client.ui.events.DeleteLineEvent;
import com.duggan.workflow.client.ui.events.PropertyChangedEvent;
import com.duggan.workflow.client.ui.events.SavePropertiesEvent;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.client.util.ENV;
import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.DocumentLine;
import com.duggan.workflow.shared.model.GridValue;
import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.model.form.Field;
import com.duggan.workflow.shared.model.form.FormModel;
import com.duggan.workflow.shared.model.form.Property;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;

public class HTMLGrid extends HTMLParent {

	private Element templateRow = null;
	private ArrayList<HTMLLine> htmlLines = new ArrayList<HTMLGrid.HTMLLine>();
	long rowCount=0;

	public HTMLGrid(Element element, boolean designMode) {
		super();
		this.designMode = designMode;
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
			templateRow = Element.as(row);

			if (!designMode) {
				templateRow.addClassName("hide");
				// addLine(new DocumentLine()); - Called in setField()
			}
			// Anchor 'Add Line'
			Element addLine = DOM.getElementById(addLineId);
			String nodeType = addLine.getNodeName().toLowerCase();
			boolean isAnchor = nodeType.equals("a");
			if (!isAnchor) {
				GWT.log("Invalid 'Add Button' element type <" + nodeType
						+ "> for Grid template '" + id + "' ");
			}
			if (addLine != null && isAnchor) {
				ActionLink link = ActionLink.wrap(addLine, true);
				link.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						addLine(new DocumentLine());
					}
				});
			}
		}

	}

	private void addLine(DocumentLine documentLine) {
		Element newLine = from(templateRow);
		Element parent = templateRow.getParentElement();
		parent.appendChild(newLine);//Attach 'newLine' element

		// Element 'newLine' has to be attached before wrap methods can be called
		HTMLLine line = new HTMLLine(newLine);
		line.getElement().removeClassName("hide");
		htmlLines.add(line);

		//Set line
		line.setDocument(documentLine);
	}

	public static Element from(Element templateRow) {
		Element newRow = DOM.clone(templateRow, true);
		newRow.removeClassName("grid_row_template");
		return newRow;
	}

	@Override
	public void setField(Field field) {
		super.setField(field);
		if(designMode){
			assert templateRow!=null;
			HTMLLine line = new HTMLLine(templateRow,true);
		}
		
	}

	private ArrayList<FieldWidget> parseInputs(Element newRow) {

		NodeList<Element> elements = newRow.getElementsByTagName("input");
		ArrayList<FieldWidget> fields = new ArrayList<FieldWidget>();

		for (int i = 0; i < elements.getLength(); i++) {
			Element element = elements.getItem(i);
			FieldWidget widget = wrap(element, designMode);
			if (widget != null) {
				fields.add(widget);
			}
		}

		NodeList<Element> selects = newRow.getElementsByTagName("select");
		for (int i = 0; i < selects.getLength(); i++) {
			Element element = selects.getItem(i);
			FieldWidget widget = wrap(element, "select", designMode);
			if (widget != null) {
				fields.add(widget);
			}
		}

		return fields;
	}

	private native JavaScriptObject getGridRowTemplate(String id) /*-{
																	var div = $doc.getElementById(id);
																	var element = $wnd.jQuery(div).find('.grid_row_template').get(0);
																	return element;
																	}-*/;

	@Override
	public void setValue(Object value) {
		deleteLines();
		if (value != null) {
			// System.err.println("lines");
			@SuppressWarnings("unchecked")
			Collection<DocumentLine> lines = (Collection<DocumentLine>) value;
			setLines(lines);
		} else if(!designMode){
			//Generate Empty row
			Collection<DocumentLine> lines = new ArrayList<DocumentLine>();
			lines.add(new DocumentLine());
			setLines(lines);
		}
	}

	private void deleteLines() {
		for(HTMLLine line: htmlLines){
			//line.deleteLine();
		}
	}

	@Override
	public Value getFieldValue() {

		GridValue value = new GridValue();
		value.setKey(field.getName());
		value.setValue(getDocumentLines());

		return value;
	}

	public Collection<DocumentLine> getDocumentLines() {
		ArrayList<DocumentLine> lines = new ArrayList<DocumentLine>();
		for (HTMLLine line : htmlLines) {
			DocumentLine doc = line.getDocument();
			if (line != null && !doc.isEmpty()) {
				lines.add(doc);
			}
		}
		return lines;
	}

	private void setLines(Collection<DocumentLine> doclines) {
		for (DocumentLine line : doclines) {
			addLine(line);
		}
	}
	
	@Override
	public FieldWidget cloneWidget() {
		return new HTMLGrid(templateRow,designMode);
	}

	@Override
	protected DataType getType() {
		return DataType.GRID;
	}

	@Override
	public void setComponentValid(boolean isValid) {

	}

	@Override
	public Widget getInputComponent() {
		return null;
	}

	@Override
	public Element getViewElement() {
		return null;
	}

	class HTMLLine {

		private ArrayList<FieldWidget> inputs = new ArrayList<FieldWidget>();
		private Element line;
		private DocumentLine documentLine;

		public HTMLLine(Element line) {
			this(line, false);
		}

		public HTMLLine(Element line, boolean parseFields) {
			this.line = line;
			Element delete = null;
			NodeList<Element> nodes = line.getElementsByTagName("a");
			if (nodes.getLength() > 0) {
				for (int i = 0; i < nodes.getLength(); i++) {
					String nodeId = nodes.getItem(i).getId();
					if (nodeId != null && nodeId.endsWith("_Delete")) {
						delete = nodes.getItem(i);
						break;
					}
				}
			}

			if (delete != null && !parseFields) {
				ActionLink deleteLink = ActionLink.wrap(delete, true);
				deleteLink.setModel(line);
				deleteLink.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						deleteLine();
					}
				});
			}

			if (parseFields) {
				// Parse Inputs	
				inputs = parseInputs(line);
				for (FieldWidget widget : inputs) {
					Field child = widget.getField();
					child = initializeChild(widget);
					children.put(child.getName(), child);//overwrite fields
				}
				field.setFields(children.values());
			} else {
				for(Field child: field.getFields()){
					Element input = getElementById(line, child.getName());
					if(input!=null){
						FieldWidget widget = wrap(input,getElementType(child,input), designMode);
						child = initializeChild(widget);
//						Window.alert("Original "+child.getName()+"; "+child.getProperty(FORMULA) 
//								+"; isObserver="+widget.isObserver+"; isObserverable="+widget.isObservable);
						//Window.alert(child.getName()+" - "+child.getProperty(FORMULA));
						inputs.add(widget);
					}
					
				}
			}
		}

		protected void deleteLine() {
//			Element elementRow = (Element) ((ActionLink) event
//					.getSource()).getModel();
			Element elementRow = line;
			elementRow.removeFromParent();
			AppContext.fireEvent(new DeleteLineEvent(documentLine));
			for (FieldWidget widget : inputs) {
				Field child = widget.getField();
				ENV.removeContext(child);
				widget.onUnload();//disable listeners
			}
			htmlLines.remove(HTMLLine.this);
		}

		public DocumentLine getDocument() {
			if (documentLine != null) {
				for (FieldWidget widget : inputs) {
					Value value = widget.getFieldValue();
					if(value!=null){
						documentLine.addValue(widget.getField().getName(), widget.getFieldValue());
					}
				}
				return documentLine;
			}
			return null;
		}

		public void setDocument(DocumentLine documentLine) {
			this.documentLine = documentLine;
			documentLine.setTempId(++rowCount);
			documentLine.setDocRefId(field.getDocRefId());
			documentLine.setId(field.getId());
			documentLine.setName(field.getName());
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

		public Element getElement() {
			return line;
		}

	}

}
