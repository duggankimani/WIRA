package com.duggan.workflow.client.ui.component;

import java.util.ArrayList;
import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;
import com.wira.commons.shared.models.HasKey;

public class Grid<T extends HasKey> extends Composite {

	private static GridUiBinder uiBinder = GWT.create(GridUiBinder.class);

	interface GridUiBinder extends UiBinder<Widget, Grid> {
	}

	static int PAGESIZE = 10;

	@UiField
	ScrollPanel container;
	SimplePager pager;

	private DataGrid<T> dataGrid;
	private AsyncDataProvider<T> dataProvider;
	private ListHandler<T> sortDataHandler;
	private final ProvidesKey<T> KEY_PROVIDER = new ProvidesKey<T>() {
		@Override
		public Object getKey(T item) {
			return item.getKey();
		}
	};

	private final SelectionModel<T> selectionModel = new SingleSelectionModel<T>(
			KEY_PROVIDER);
	
	T selected;

	public Grid() {
		initWidget(uiBinder.createAndBindUi(this));
		int height = Window.getClientHeight();
		if ((height - 260) > 0)
			container.setHeight((height - 260) + "px");
		initGrid();
	}

	public void initGrid() {
		dataGrid = createDataGrid();
		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
	    pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
	    pager.setDisplay(dataGrid);
	    dataGrid.setEmptyTableWidget(new Label("No records to display"));
		
		VerticalPanel vp = new VerticalPanel();
		vp.setWidth("100%");
		vp.add(pager);
		vp.add(dataGrid);
		container.add(vp);
	}

	private DataGrid<T> createDataGrid() {
		this.sortDataHandler = new ListHandler<T>(new ArrayList<T>());

		final DataGrid<T> dataGrid = new DataGrid<T>(PAGESIZE, KEY_PROVIDER);
		dataGrid.setSize("100%", "75vh");
		dataGrid.setStyleName("responsive-table");

		SimplePager.Resources pagerResources = GWT
				.create(SimplePager.Resources.class);
		pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0,
				true);
		pager.setRangeLimited(true);

		pager.setDisplay(dataGrid);
		dataGrid.addColumnSortHandler(sortDataHandler);
//		dataGrid.setSelectionModel(selectionModel, DefaultSelectionEventManager.<T>createCheckboxManager());

		dataGrid.setSelectionModel(selectionModel);
				
		dataGrid.getSelectionModel().addSelectionChangeHandler(
				new SelectionChangeEvent.Handler() {

					@Override
					public void onSelectionChange(SelectionChangeEvent event) {
						selected = (T) ((SingleSelectionModel)dataGrid.getSelectionModel()).getSelectedObject();
					}});

		return dataGrid;

	}

	public void addColumn(Column col, String name) {
		dataGrid.addColumn(col, name);
	}

	public void addColumn(Column col, String name, String width) {
		addColumn(col, name);
		dataGrid.setColumnWidth(col, width);
	}

	public void setDataProvider(AsyncDataProvider<T> dataProvider) {
		this.dataProvider = dataProvider;
		dataProvider.addDataDisplay(dataGrid);
	}

	public void setData(ArrayList<T> data, int totalCount) {
		Range range = dataGrid.getVisibleRange();
		dataProvider.updateRowCount(totalCount, true);
		dataProvider.updateRowData(range.getStart(), data);
	}

	public SimplePager getPager() {
		return pager;
	}

	public Range getVisibleRange() {
		return dataGrid.getVisibleRange();
	}

	public void refresh() {
		dataGrid.redraw();
	}

	public SelectionModel<T> getSelectionModel() {
		return selectionModel;
	}

	public void setColumnWidth(Column<T, Boolean> column, String width) {
		dataGrid.setColumnWidth(column, width);
	}

	public void addColumn(Column<T, Boolean> checkColumn,
			SafeHtml fromSafeConstant) {
		dataGrid.addColumn(checkColumn, fromSafeConstant);
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		// NodeList<Element> elements =
		// dataGrid.getElement().getElementsByTagName("input");

	}

	public void setColumnWidth(Column col, int width,
			Unit unit) {
		dataGrid.setColumnWidth(col, width, unit);
	}

	public T getSelectedValue() {
		return selected;
	}

}
