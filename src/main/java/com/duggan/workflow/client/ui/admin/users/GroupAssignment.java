package com.duggan.workflow.client.ui.admin.users;

import java.util.Comparator;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionModel;
import com.wira.commons.shared.models.UserGroup;

public class GroupAssignment extends Composite {

	private static GroupAssignmentUiBinder uiBinder = GWT.create(GroupAssignmentUiBinder.class);

	interface GroupAssignmentUiBinder extends UiBinder<Widget, GroupAssignment> {
	}

	public GroupAssignment() {
		// Set a key provider that provides a unique key for each contact. If key is
		// used to identify contacts when fields (such as the name and address)
		// change.
		cellTable = new CellTable<UserGroup>(UserGroup.KEY_PROVIDER);
		cellTable.setWidth("100%", true);

		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
		pager.setDisplay(cellTable);

		// Create a Pager to control the table.
		initialize();
		initWidget(uiBinder.createAndBindUi(this));
	}

	/**
	 * The main CellTable.
	 */
	@UiField(provided = true)
	CellTable<UserGroup> cellTable;

	/**
	 * The pager used to change the range of data.
	 */
	// @UiField(provided = true)
	SimplePager pager;

	/**
	 * The provider that holds the list of contacts in the database.
	 */
	private ListDataProvider<UserGroup> dataProvider = new ListDataProvider<UserGroup>();

	private void initialize() {
		// Create a CellTable.

		// Do not refresh the headers and footers every time the data is updated.
		cellTable.setAutoHeaderRefreshDisabled(true);
		cellTable.setAutoFooterRefreshDisabled(true);

		// Attach a column sort handler to the ListDataProvider to sort the list.
		ListHandler<UserGroup> sortHandler = new ListHandler<UserGroup>(dataProvider.getList());
		cellTable.addColumnSortHandler(sortHandler);

		// Add a selection model so we can select cells.
		final SelectionModel<UserGroup> selectionModel = new MultiSelectionModel<UserGroup>(UserGroup.KEY_PROVIDER);
		cellTable.setSelectionModel(selectionModel, DefaultSelectionEventManager.<UserGroup>createCheckboxManager());

		// Initialize the columns.
		initTableColumns(selectionModel, sortHandler);

		// Add the CellList to the adapter in the database.
		dataProvider.addDataDisplay(cellTable);
	}

	/**
	 * Add the columns to the table.
	 */
	private void initTableColumns(final SelectionModel<UserGroup> selectionModel, ListHandler<UserGroup> sortHandler) {

		// First name.
		Column<UserGroup, String> groupName = new Column<UserGroup, String>(new EditTextCell()) {
			@Override
			public String getValue(UserGroup object) {
				return object.getName();
			}
		};
		groupName.setSortable(true);
		sortHandler.setComparator(groupName, new Comparator<UserGroup>() {
			@Override
			public int compare(UserGroup o1, UserGroup o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		groupName.setFieldUpdater(new FieldUpdater<UserGroup, String>() {
			@Override
			public void update(int index, UserGroup object, String value) {
				// Called when the user changes the value.
				object.setName(value);
				dataProvider.refresh();
			}
		});
		cellTable.addColumn(groupName, "Name");
		cellTable.setColumnWidth(groupName, 50, Unit.PCT);

		// Last name.
		Column<UserGroup, String> groupDescColumn = new Column<UserGroup, String>(new EditTextCell()) {
			@Override
			public String getValue(UserGroup object) {
				return object.getDisplayName();
			}
		};
		groupDescColumn.setSortable(true);
		sortHandler.setComparator(groupDescColumn, new Comparator<UserGroup>() {
			@Override
			public int compare(UserGroup o1, UserGroup o2) {
				return o1.getDisplayName().compareTo(o2.getDisplayName());
			}
		});
		groupDescColumn.setFieldUpdater(new FieldUpdater<UserGroup, String>() {
			@Override
			public void update(int index, UserGroup object, String value) {
				// Called when the user changes the value.
				object.setFullName(value);
				dataProvider.refresh();
			}
		});
		cellTable.addColumn(groupDescColumn, "Description");
		cellTable.setColumnWidth(groupDescColumn, 50, Unit.PCT);
	
	}
}
