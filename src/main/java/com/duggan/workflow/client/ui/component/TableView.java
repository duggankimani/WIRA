package com.duggan.workflow.client.ui.component;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.wira.commons.client.util.ArrayUtil;

public class TableView extends Composite {

	private static TableViewUiBinder uiBinder = GWT
			.create(TableViewUiBinder.class);

	interface TableViewUiBinder extends UiBinder<Widget, TableView> {
	}

	@UiField
	HTMLPanel overalContainer;
	@UiField
	HTMLPanel tblContainer;
	@UiField
	HTMLPanel panelHeader;
	@UiField
	FlowPanel panelBody;
	@UiField
	HTMLPanel panelFooter;

	private boolean isAutoNumber = true;
	private int count = 0;

	public TableView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setHeaders(ArrayList<String> names) {
		setHeaders(null,names);
	}
	public void setHeaders(ArrayList<String> tdStyles,ArrayList<String> names) {
		ArrayList<Widget> widgets = new ArrayList<Widget>();

		for (String name : names) {
			InlineLabel label = new InlineLabel(name);
			widgets.add(label);
		}

		setHeaderWidgets(tdStyles,widgets);
	}

	public void setTableHeaders(ArrayList<TableHeader> headers) {
		panelHeader.clear();
		if (isAutoNumber) {
			headers.add(0, new TableHeader("#", 1.0));
		}
		
		// InlineLabel label = new InlineLabel(header.getTitleName());
		for (TableHeader header : headers) {
			// th
			HTMLPanel th = new HTMLPanel("");
			th.addStyleName("th");

			// Label
			InlineLabel label = new InlineLabel(header.getTitleName());
			th.add(label);

			// add to row
			if (header.getWidth() != null) {
				th.getElement().getStyle()
						.setWidth(header.getWidth(), Unit.PCT);
			}

			if (header.getStyleName() != null) {
				th.addStyleName(header.getStyleName());
			}

			panelHeader.add(th);
		}
	}

	@Override
	public Widget asWidget() {
		return super.asWidget();
	}

	public void setHeaderWidgets(ArrayList<String> tdStyles,ArrayList<Widget> widgets) {
		panelHeader.clear();
		if (isAutoNumber) {
			InlineLabel label = new InlineLabel("#");
			widgets.add(0, label);
		}

		int i= isAutoNumber? -1: 0;
		for (Widget widget : widgets) {
			HTMLPanel th = new HTMLPanel("");
			th.addStyleName("th");
			if(i!=-1 && tdStyles!=null && i<tdStyles.size()){
				String style = tdStyles.get(i);
				if(style!=null && !style.trim().isEmpty())
					th.addStyleName(style);
			}
			th.add(widget);
			i++;
			panelHeader.add(th);
		}
	}

	public void addRow(Widget... widgets) {
		addRow(ArrayUtil.asList(widgets));
	}

	public void addRow(ArrayList<String> tdStyles, Widget... widgets) {
		addRow(tdStyles, ArrayUtil.asList(widgets));
	}

	public void addRow(ArrayList<Widget> widgets) {
		addRow(new ArrayList<String>(), widgets);
	}

	public void addRow(ArrayList<String> tdStyles, ArrayList<Widget> widgets) {
		HTMLPanel row = new HTMLPanel("");
		row.addStyleName("tr");

		if (isAutoNumber) {
			row.add(getTd(new InlineLabel((++count) + "")));
		}

		int i = 0;
		for (Widget widget : widgets) {
			Widget td = getTd(widget);
			if (tdStyles.size() > i) {
				if (!tdStyles.get(i).isEmpty())
					td.addStyleName(tdStyles.get(i));
			}
			row.add(td);
			++i;
		}
		panelBody.add(row);
	}

	public void addRow(RowWidget rowWidget) {
		rowWidget.setAutoNumber(isAutoNumber());
		rowWidget.setRowNumber(++count);
		panelBody.add(rowWidget);
	}

	public int getRowCount() {
		return panelBody.getWidgetCount();
	}

	public Widget getRow(int row) {
		if (panelBody.getWidgetCount() > row)
			return panelBody.getWidget(row);

		return null;
	}

	private Widget getTd(Widget widget) {
		HTMLPanel td = new HTMLPanel("");
		td.addStyleName("td");
		td.add(widget);
		return td;
	}

	public void setStriped(Boolean status) {
		if (status) {
			tblContainer.addStyleName("table-striped");
		} else {
			tblContainer.removeStyleName("table-striped");
		}
	}

	
	public void setBordered(Boolean status) {
		tblContainer.removeStyleName("table-bordered");
		if (status) {
			tblContainer.addStyleName("table-bordered");
		} else {
			tblContainer.addStyleName("table-noborder");
		}
	}

	public void setIsGrid(Boolean status) {
		if (status) {
			overalContainer.getElement().setAttribute("id", "grid");
		} else {
			overalContainer.getElement().removeAttribute("id");
		}
	}

	public void clearRows() {
		panelBody.clear();
		resetCount();
	}

	public boolean isAutoNumber() {
		return isAutoNumber;
	}

	public void setAutoNumber(boolean isAutoNumber) {
		this.isAutoNumber = isAutoNumber;
	}

	public void setFooter(ArrayList<Widget> widgets) {
		panelFooter.clear();
		HTMLPanel row = new HTMLPanel("");
		row.addStyleName("tr");

		if (isAutoNumber) {
			row.add(getTd(new InlineLabel()));
		}

		for (Widget widget : widgets) {
			row.add(getTd(widget));
		}
		panelFooter.add(row);
	}

	public void resetCount() {
		count = 0;
	}

	public void createHeader(String name, String width, String labelStyle,
			String thStyle) {
		HTMLPanel th = new HTMLPanel("");
		th.addStyleName("th");
		if (thStyle != null) {
			th.addStyleName(thStyle);
		}
		if (width != null) {
			th.setWidth(width);
		}
		InlineLabel label = new InlineLabel(name);
		label.setTitle(name);
		if (labelStyle != null) {
			label.addStyleName(labelStyle);
		}
		th.add(label);
		panelHeader.add(th);
	}

	public void createHeader(String name, String width) {
		createHeader(name, width, null, null);
	}

	public void createHeader(String name) {
		createHeader(name, null);
	}

	public void insert(RowWidget rowWidget, int beforeIndex) {
		panelBody.insert(rowWidget, beforeIndex);
	}
}
