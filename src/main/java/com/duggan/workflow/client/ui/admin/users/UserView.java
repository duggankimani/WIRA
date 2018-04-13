package com.duggan.workflow.client.ui.admin.users;

import java.util.ArrayList;
import java.util.Comparator;

import com.duggan.workflow.client.event.CheckboxSelectionEvent;
import com.duggan.workflow.client.model.UploadContext;
import com.duggan.workflow.client.model.UploadContext.UPLOADACTION;
import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.component.Checkbox;
import com.duggan.workflow.client.ui.events.LoadUsersEvent;
import com.duggan.workflow.client.ui.events.ProcessingCompletedEvent;
import com.duggan.workflow.client.ui.events.ProcessingEvent;
import com.duggan.workflow.client.ui.upload.custom.Uploader;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.requests.GetGroupsRequest;
import com.duggan.workflow.shared.requests.GetOrgsRequest;
import com.duggan.workflow.shared.requests.GetUsersRequest;
import com.duggan.workflow.shared.responses.GetGroupsResponse;
import com.duggan.workflow.shared.responses.GetOrgsResponse;
import com.duggan.workflow.shared.responses.GetUsersResponse;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.ViewImpl;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.wira.commons.shared.models.HTUser;
import com.wira.commons.shared.models.Org;
import com.wira.commons.shared.models.UserGroup;

import gwtupload.client.IUploader;
import gwtupload.client.IUploader.OnFinishUploaderHandler;
import gwtupload.client.IUploader.OnStartUploaderHandler;

public class UserView extends ViewImpl implements UserPresenter.MyView {

	protected static int CONTACTS_SIZE = 0;
	protected static int GROUPS_SIZE = 0;
	protected static int ORGS_SIZE = 0;
	private final Widget widget;
	@UiField
	Anchor aNewUser;
	@UiField
	Anchor aImportUsers;

	@UiField
	Uploader aUploader;

	@UiField
	Anchor aNewGroup;
	@UiField
	Anchor aUserstab;
	@UiField
	Anchor aGroupstab;
	@UiField
	Anchor aUnitstab;

	@UiField
	FlexTable tblUser;
	@UiField
	FlexTable tblGroup;
	@UiField
	FlexTable tblOrgs;

	@UiField
	Anchor aEditUser;
	@UiField
	Anchor aDeleteUser;
	@UiField
	Anchor aEditGroup;
	@UiField
	Anchor aDeleteGroup;

	@UiField
	Anchor aNewOrg;
	@UiField
	Anchor aEditOrg;
	@UiField
	Anchor aDeleteOrg;

	@UiField
	GroupAssignment unAssignedGroups;
	@UiField
	GroupAssignment assignedGroups;

	@UiField
	HTMLPanel divPaginate;
	
	@UiField
	HTMLPanel divGroupsPaginate;
	
	@UiField
	HTMLPanel divOrgsPaginate;

	public interface Binder extends UiBinder<Widget, UserView> {
	}

	PlaceManager placeManager;
	/**
	 * The main CellTable.
	 */
	@UiField(provided = true)
	CellTable<HTUser> usersTable;

	@UiField(provided = true)
	CellTable<UserGroup> groupsTable;
	
	@UiField(provided = true)
	CellTable<Org> orgsTable;

	/**
	 * The pager used to change the range of data.
	 */
	// @UiField(provided = true)
	SimplePager usersPager;
	SimplePager groupsPager;
	SimplePager orgsPager;

	// Create a Data Provider.
	AsyncDataProvider<HTUser> usersDataProvider;
	AsyncDataProvider<UserGroup> groupsDataProvider;
	AsyncDataProvider<Org> orgsDataProvider;

	ListHandler<HTUser> sortHandler = null;

	private EventBus eventBus;

	@Inject
	public UserView(final Binder binder, PlaceManager manager, EventBus eventBus) {
		this.eventBus = eventBus;
		// Set a key provider that provides a unique key for each contact. If key is
		// used to identify contacts when fields (such as the name and address)
		// change.
		usersTable = new CellTable<HTUser>(UserPresenter.PAGE_SIZE, HTUser.KEY_PROVIDER);
		usersTable.setWidth("100%", true);

		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		usersPager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
		usersPager.setDisplay(usersTable);

		//
		groupsTable = new CellTable<UserGroup>(UserPresenter.PAGE_SIZE, UserGroup.KEY_PROVIDER);
		groupsTable.setWidth("100%", true);
		pagerResources =GWT.create(SimplePager.Resources.class);
		groupsPager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
		groupsPager.setDisplay(groupsTable);

		
		orgsTable = new CellTable<Org>(UserPresenter.PAGE_SIZE, Org.KEY_PROVIDER);
		orgsTable.setWidth("100%", true);
		pagerResources =GWT.create(SimplePager.Resources.class);
		orgsPager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
		orgsPager.setDisplay(orgsTable);

		
		// Create a Pager to control the table.
		initialize();

		widget = binder.createAndBindUi(this);
		placeManager = manager;
		divPaginate.add(usersPager);
		divGroupsPaginate.add(groupsPager);
		divOrgsPaginate.add(orgsPager);

		aUserstab.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				placeManager.revealPlace(
						new PlaceRequest.Builder().nameToken(NameTokens.usermgt).with("page", "user").build());
			}
		});

		aGroupstab.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				placeManager.revealPlace(
						new PlaceRequest.Builder().nameToken(NameTokens.usermgt).with("page", "group").build());
			}
		});

		aUnitstab.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				placeManager.revealPlace(
						new PlaceRequest.Builder().nameToken(NameTokens.usermgt).with("page", "org").build());
			}
		});

		aImportUsers.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				final UploadContext ctx = new UploadContext();
				final ArrayList<String> types = new ArrayList<String>();
				types.add("csv");
				ctx.setAccept(types);

				ctx.setAction(UPLOADACTION.IMPORTUSERS);
				aUploader.setContext(ctx);
				triggerUpload(aUploader.getElement());
			}
		});

		aUploader.addOnStartUploaderHandler(new OnStartUploaderHandler() {
			@Override
			public void onStart(IUploader uploader) {
				AppContext.fireEvent(new ProcessingEvent());
			}
		});

		aUploader.addOnFinishUploaderHandler(new OnFinishUploaderHandler() {
			@Override
			public void onFinish(IUploader uploader) {
				AppContext.fireEvent(new ProcessingCompletedEvent());
				AppContext.fireEvent(new LoadUsersEvent());
			}
		});
	}

	protected native void triggerUpload(Element uploader) /*-{
															$wnd.jQuery(uploader).find('input').trigger('click');
															
															}-*/;

	@Override
	public void bindUsers(ArrayList<HTUser> users, Integer start, Integer totalCount) {

		if (CONTACTS_SIZE != totalCount) {
			CONTACTS_SIZE = totalCount;
			usersDataProvider.updateRowCount(CONTACTS_SIZE, true);
		}

		usersDataProvider.updateRowData(start, users);
		sortHandler.setList(users);
	}

	public void bindUsers1(ArrayList<HTUser> users) {
		clearSelections();
		tblUser.removeAllRows();
		setUserHeaders(tblUser);

		int i = 1;
		for (HTUser user : users) {
			int j = 0;
			Checkbox box = new Checkbox(user);
			box.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
				@Override
				public void onValueChange(ValueChangeEvent<Boolean> event) {
					Object model = ((Checkbox) (event.getSource())).getModel();
					AppContext.fireEvent(new CheckboxSelectionEvent(model, event.getValue()));
				}
			});

			tblUser.setWidget(i, j++, box);
			tblUser.setWidget(i, j++, new HTMLPanel(user.getSurname()));
			tblUser.setWidget(i, j++, new HTMLPanel(user.getName()));
			tblUser.setWidget(i, j++, new HTMLPanel(user.getUserId()));
			tblUser.setWidget(i, j++, new HTMLPanel(user.getEmail()));
			tblUser.setWidget(i, j++, new HTMLPanel(user.getGroupsAsString()));
			tblUser.setWidget(i, j++, new HTMLPanel(user.getInbox() + ""));
			tblUser.setWidget(i, j++, new HTMLPanel(user.getParticipated() + ""));
			tblUser.setWidget(i, j++, new HTMLPanel(user.getDrafts() + ""));
			tblUser.setWidget(i, j++, new HTMLPanel(user.getTotal() + ""));
			++i;
		}
	}

	@Override
	public void bindGroups(ArrayList<UserGroup> groups, Integer start, Integer totalCount) {
		if (GROUPS_SIZE != totalCount) {
			GROUPS_SIZE = totalCount;
			groupsDataProvider.updateRowCount(GROUPS_SIZE, true);
		}

		groupsDataProvider.updateRowData(start, groups);
	}

	private void clearSelections() {
		setUserEdit(false);
		setGroupEdit(false);
	}

	private void setUserHeaders(FlexTable table) {
		int j = 0;
		table.setWidget(0, j++, new HTMLPanel("<strong>#</strong>"));
		table.getFlexCellFormatter().setWidth(0, (j - 1), "20px");

		table.setWidget(0, j++, new HTMLPanel("<strong>Last Name</strong>"));
		table.getFlexCellFormatter().setWidth(0, (j - 1), "100px");
		table.setWidget(0, j++, new HTMLPanel("<strong>First Name</strong>"));
		table.getFlexCellFormatter().setWidth(0, (j - 1), "110px");
		table.setWidget(0, j++, new HTMLPanel("<strong>Username</strong>"));
		table.getFlexCellFormatter().setWidth(0, (j - 1), "100px");
		table.setWidget(0, j++, new HTMLPanel("<strong>Email</strong>"));
		table.getFlexCellFormatter().setWidth(0, (j - 1), "100px");
		table.setWidget(0, j++, new HTMLPanel("<strong>Groups</strong>"));
		table.getFlexCellFormatter().setWidth(0, (j - 1), "200px");
		table.setWidget(0, j++, new HTMLPanel("<strong>Inbox</strong>"));
		table.getFlexCellFormatter().setWidth(0, (j - 1), "60px");
		table.setWidget(0, j++, new HTMLPanel("<strong>Done</strong>"));
		table.getFlexCellFormatter().setWidth(0, (j - 1), "60px");
		table.setWidget(0, j++, new HTMLPanel("<strong>Drafts</strong>"));
		table.getFlexCellFormatter().setWidth(0, (j - 1), "60px");
		table.setWidget(0, j++, new HTMLPanel("<strong>Total</strong>"));
		table.getFlexCellFormatter().setWidth(0, (j - 1), "60px");

		for (int i = 0; i < table.getCellCount(0); i++) {
			table.getFlexCellFormatter().setStyleName(0, i, "th");
		}
	}

	@Override
	public void bindOrgs(ArrayList<Org> orgs, Integer start, Integer totalCount) {
		if (ORGS_SIZE != totalCount) {
			ORGS_SIZE = totalCount;
			orgsDataProvider.updateRowCount(ORGS_SIZE, true);
		}

		orgsDataProvider.updateRowData(start, orgs);

	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public HasClickHandlers getaNewUser() {
		return aNewUser;
	}

	public HasClickHandlers getImportUsers() {
		return aImportUsers;
	}

	@Override
	public HasClickHandlers getaNewGroup() {
		return aNewGroup;
	}

	@Override
	public void setType(TYPE type) {
		// if (type == TYPE.GROUP) {
		// aNewUser.addStyleName("hide");
		// aNewGroup.removeStyleName("hide");
		// liGroup.setClassName("active");
		// liUser.removeClassName("active");
		//
		// divUserContent.removeClassName("in");
		// divUserContent.removeClassName("active");
		//
		// divGroupContent.addClassName("in");
		// divGroupContent.addClassName("active");
		// } else {
		// aNewUser.removeStyleName("hide");
		// aNewGroup.addStyleName("hide");
		// liGroup.removeClassName("active");
		// liUser.addClassName("active");
		//
		// divUserContent.addClassName("in");
		// divUserContent.addClassName("active");
		//
		// divGroupContent.removeClassName("in");
		// divGroupContent.removeClassName("active");
		// }
	}

	@Override
	public void setGroupEdit(boolean value) {
		if (value) {
			aEditGroup.removeStyleName("hide");
			aDeleteGroup.removeStyleName("hide");
		} else {
			aEditGroup.addStyleName("hide");
			aDeleteGroup.addStyleName("hide");
		}

	}

	@Override
	public void setUserEdit(boolean value) {
		if (value) {
			aEditUser.removeStyleName("hide");
			aDeleteUser.removeStyleName("hide");
		} else {
			aEditUser.addStyleName("hide");
			aDeleteUser.addStyleName("hide");
		}
	}

	@Override
	public void setOrgEdit(boolean value) {
		if (value) {
			aEditOrg.removeStyleName("hide");
			aDeleteOrg.removeStyleName("hide");
		} else {
			aEditOrg.addStyleName("hide");
			aDeleteOrg.addStyleName("hide");
		}
	}

	public HasClickHandlers getEditUser() {
		return aEditUser;
	}

	public HasClickHandlers getDeleteUser() {
		return aDeleteUser;
	}

	public HasClickHandlers getEditGroup() {
		return aEditGroup;
	}

	public HasClickHandlers getDeleteGroup() {
		return aDeleteGroup;
	}

	public HasClickHandlers getNewOrg() {
		return aNewOrg;
	}

	public HasClickHandlers getEditOrg() {
		return aEditOrg;
	}

	public HasClickHandlers getDeleteOrg() {
		return aDeleteOrg;
	}

	private void initialize() {
		initializeUsers();
		initializeGroups();
		initializeOrgs();
	}

	private void initializeGroups() {
		groupsDataProvider = new AsyncDataProvider<UserGroup>() {
			@Override
			protected void onRangeChanged(HasData<UserGroup> display) {
				final int start = display.getVisibleRange().getStart();
				int end = start + display.getVisibleRange().getLength();

				end = end >= GROUPS_SIZE ? GROUPS_SIZE : end;
				if (end == 0) {
					end = UserPresenter.PAGE_SIZE;
				}

				GetGroupsRequest request = new GetGroupsRequest(null);
				request.setOffset(start);
				request.setLength(UserPresenter.PAGE_SIZE);

				// Window.alert("### Before Users Load - "+start+" - "+end+" -");
				final DispatchAsync dispatcher = AppContext.getDispatcher();
				dispatcher.execute(request, new TaskServiceCallback<GetGroupsResponse>() {
					@Override
					public void processResult(GetGroupsResponse result) {

						if (GROUPS_SIZE != result.getTotalCount()) {
							GROUPS_SIZE = result.getTotalCount();
							updateRowCount(GROUPS_SIZE, true);
						}

						ArrayList<UserGroup> groups = result.getGroups();
						updateRowData(start, groups);
						groupsTable.redraw();
					}
				});

			}
		};

		// Do not refresh the headers and footers every time the data is updated.
		groupsTable.setAutoHeaderRefreshDisabled(true);
		groupsTable.setAutoFooterRefreshDisabled(true);

		// Add a selection model so we can select cells.
		final SelectionModel<UserGroup> selectionModel = new SingleSelectionModel<UserGroup>(UserGroup.KEY_PROVIDER);
		groupsTable.setSelectionModel(selectionModel, DefaultSelectionEventManager.<UserGroup>createCheckboxManager());

		// Initialize the columns.
		initGroupColumns(selectionModel);

		// Add the CellList to the adapter in the database.
		groupsDataProvider.addDataDisplay(groupsTable);

	}
	
	private void initializeOrgs() {
		orgsDataProvider = new AsyncDataProvider<Org>() {
			@Override
			protected void onRangeChanged(HasData<Org> display) {
				final int start = display.getVisibleRange().getStart();
				int end = start + display.getVisibleRange().getLength();

				end = end >= GROUPS_SIZE ? GROUPS_SIZE : end;
				if (end == 0) {
					end = UserPresenter.PAGE_SIZE;
				}

				GetOrgsRequest request = new GetOrgsRequest();
				request.setOffset(start);
				request.setLength(UserPresenter.PAGE_SIZE);

				// Window.alert("### Before Users Load - "+start+" - "+end+" -");
				final DispatchAsync dispatcher = AppContext.getDispatcher();
				dispatcher.execute(request, new TaskServiceCallback<GetOrgsResponse>() {
					@Override
					public void processResult(GetOrgsResponse result) {

						if (GROUPS_SIZE != result.getTotalCount()) {
							GROUPS_SIZE = result.getTotalCount();
							updateRowCount(GROUPS_SIZE, true);
						}

						ArrayList<Org> groups = result.getOrgs();
						updateRowData(start, groups);
						orgsTable.redraw();
					}
				});

			}
		};

		// Do not refresh the headers and footers every time the data is updated.
		orgsTable.setAutoHeaderRefreshDisabled(true);
		orgsTable.setAutoFooterRefreshDisabled(true);

		// Add a selection model so we can select cells.
		final SelectionModel<Org> selectionModel = new SingleSelectionModel<Org>(Org.KEY_PROVIDER);
		orgsTable.setSelectionModel(selectionModel, DefaultSelectionEventManager.<Org>createCheckboxManager());

		// Initialize the columns.
		initOrgColumns(selectionModel);

		// Add the CellList to the adapter in the database.
		orgsDataProvider.addDataDisplay(orgsTable);

	}


	private void initializeUsers() {
		usersDataProvider = new AsyncDataProvider<HTUser>() {
			@Override
			protected void onRangeChanged(HasData<HTUser> display) {
				final int start = display.getVisibleRange().getStart();
				int end = start + display.getVisibleRange().getLength();

				end = end >= CONTACTS_SIZE ? CONTACTS_SIZE : end;
				if (end == 0) {
					end = UserPresenter.PAGE_SIZE;
				}

				GetUsersRequest request = new GetUsersRequest(null);
				request.setOffset(start);
				request.setLength(UserPresenter.PAGE_SIZE);

				// Window.alert("### Before Users Load - "+start+" - "+end+" -");
				final DispatchAsync dispatcher = AppContext.getDispatcher();
				dispatcher.execute(request, new TaskServiceCallback<GetUsersResponse>() {
					@Override
					public void processResult(GetUsersResponse result) {

						if (CONTACTS_SIZE != result.getTotalCount()) {
							CONTACTS_SIZE = result.getTotalCount();
							updateRowCount(CONTACTS_SIZE, true);
						}

						ArrayList<HTUser> users = result.getUsers();
						updateRowData(start, users);
						sortHandler.setList(users);
					}
				});

			}
		};

		// Do not refresh the headers and footers every time the data is updated.
		usersTable.setAutoHeaderRefreshDisabled(true);
		usersTable.setAutoFooterRefreshDisabled(true);

		// Attach a column sort handler to the ListDataProvider to sort the list.
		sortHandler = new ListHandler<HTUser>(new ArrayList<HTUser>());
		usersTable.addColumnSortHandler(sortHandler);

		// Add a selection model so we can select cells.
		final SelectionModel<HTUser> selectionModel = new SingleSelectionModel<HTUser>(HTUser.KEY_PROVIDER);
		usersTable.setSelectionModel(selectionModel, DefaultSelectionEventManager.<HTUser>createCheckboxManager());

		// Initialize the columns.
		initTableColumns(selectionModel, sortHandler);

		// Add the CellList to the adapter in the database.
		usersDataProvider.addDataDisplay(usersTable);

	}

	/**
	 * Add the columns to the table.
	 */
	private void initTableColumns(final SelectionModel<HTUser> selectionModel, ListHandler<HTUser> sortHandler) {
		// Checkbox column. This table will uses a checkbox column for selection.
		// Alternatively, you can call cellTable.setSelectionEnabled(true) to enable
		// mouse selection.
		Column<HTUser, Boolean> checkColumn = new Column<HTUser, Boolean>(new CheckboxCell(true, false)) {
			@Override
			public Boolean getValue(HTUser object) {
				// Get the value from the selection model.
				return selectionModel.isSelected(object);
			}
		};
		usersTable.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));

		usersTable.setColumnWidth(checkColumn, 40, Unit.PX);

		// First name.
		Column<HTUser, String> firstNameColumn = new Column<HTUser, String>(new TextCell()) {
			@Override
			public String getValue(HTUser object) {
				return object.getName();
			}
		};
		firstNameColumn.setSortable(true);
		sortHandler.setComparator(firstNameColumn, new Comparator<HTUser>() {
			@Override
			public int compare(HTUser o1, HTUser o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});

		usersTable.addColumn(firstNameColumn, "First Name");
		firstNameColumn.setFieldUpdater(new FieldUpdater<HTUser, String>() {
			@Override
			public void update(int index, HTUser object, String value) {
				// Called when the user changes the value.
				object.setName(value);
				// dataProvider.refresh();
			}
		});
		usersTable.setColumnWidth(firstNameColumn, 15, Unit.PCT);

		// Last name.
		Column<HTUser, String> lastNameColumn = new Column<HTUser, String>(new TextCell()) {
			@Override
			public String getValue(HTUser object) {
				return object.getSurname();
			}
		};
		lastNameColumn.setSortable(true);
		sortHandler.setComparator(lastNameColumn, new Comparator<HTUser>() {
			@Override
			public int compare(HTUser o1, HTUser o2) {
				return o1.getSurname().compareTo(o2.getSurname());
			}
		});
		usersTable.addColumn(lastNameColumn, "Last Name");
		lastNameColumn.setFieldUpdater(new FieldUpdater<HTUser, String>() {
			@Override
			public void update(int index, HTUser object, String value) {
				// Called when the user changes the value.
				object.setSurname(value);
				// dataProvider.refresh();
			}
		});
		usersTable.setColumnWidth(lastNameColumn, 15, Unit.PCT);

		// Userid
		Column<HTUser, String> userIdCol = new Column<HTUser, String>(new TextCell()) {
			@Override
			public String getValue(HTUser object) {
				return object.getUserId();
			}
		};
		userIdCol.setSortable(true);
		sortHandler.setComparator(userIdCol, new Comparator<HTUser>() {
			@Override
			public int compare(HTUser o1, HTUser o2) {
				return o1.getUserId().compareTo(o2.getUserId());
			}
		});
		usersTable.addColumn(userIdCol, "User ID");

		// Email
		Column<HTUser, String> emailColumn = new Column<HTUser, String>(new TextCell()) {
			@Override
			public String getValue(HTUser object) {
				return object.getEmail();
			}
		};
		emailColumn.setSortable(true);
		sortHandler.setComparator(emailColumn, new Comparator<HTUser>() {
			@Override
			public int compare(HTUser o1, HTUser o2) {
				return o1.getEmail().compareTo(o2.getEmail());
			}
		});
		usersTable.addColumn(emailColumn, "Email");

		lastNameColumn.setFieldUpdater(new FieldUpdater<HTUser, String>() {
			@Override
			public void update(int index, HTUser object, String value) {
				// Called when the user changes the value.
				object.setEmail(value);
				// dataProvider.refresh();
			}
		});
		usersTable.setColumnWidth(emailColumn, 15, Unit.PCT);

		// Organization
		Column<HTUser, String> orgColumn = new Column<HTUser, String>(new TextCell()) {
			@Override
			public String getValue(HTUser object) {
				return object.getOrg() == null ? "" : object.getOrg().getDisplayName();
			}
		};
		orgColumn.setSortable(true);
		sortHandler.setComparator(orgColumn, new Comparator<HTUser>() {
			@Override
			public int compare(HTUser o1, HTUser o2) {
				return o1.getOrg().getDisplayName().compareTo(o2.getOrg().getDisplayName());
			}
		});
		usersTable.addColumn(orgColumn, "Org");
		usersTable.setColumnWidth(orgColumn, 15, Unit.PCT);

		// Participated
		Column<HTUser, String> participatedCol = new Column<HTUser, String>(new TextCell()) {
			@Override
			public String getValue(HTUser object) {
				return object.getParticipated() + "";
			}
		};
		participatedCol.setSortable(true);
		sortHandler.setComparator(participatedCol, new Comparator<HTUser>() {
			@Override
			public int compare(HTUser o1, HTUser o2) {
				Integer participated = o1.getParticipated();
				Integer participated2 = o2.getParticipated();
				return participated.compareTo(participated2);
			}
		});
		usersTable.addColumn(participatedCol, "Participated");
		usersTable.setColumnWidth(participatedCol, 70, Unit.PX);

		// Inbox
		Column<HTUser, String> inboxCol = new Column<HTUser, String>(new TextCell()) {
			@Override
			public String getValue(HTUser object) {
				return object.getInbox() + "";
			}
		};
		inboxCol.setSortable(true);
		sortHandler.setComparator(inboxCol, new Comparator<HTUser>() {
			@Override
			public int compare(HTUser o1, HTUser o2) {
				Integer inbox = o1.getInbox();
				Integer inbox2 = o2.getInbox();
				return inbox.compareTo(inbox2);
			}
		});
		usersTable.addColumn(participatedCol, "Inbox");
		usersTable.setColumnWidth(participatedCol, 70, Unit.PX);

		// Inbox
		Column<HTUser, String> draftsCol = new Column<HTUser, String>(new TextCell()) {
			@Override
			public String getValue(HTUser object) {
				return object.getInbox() + "";
			}
		};
		draftsCol.setSortable(true);
		sortHandler.setComparator(draftsCol, new Comparator<HTUser>() {
			@Override
			public int compare(HTUser o1, HTUser o2) {
				Integer drafts = o1.getDrafts();
				Integer drafts2 = o2.getDrafts();
				return drafts.compareTo(drafts2);
			}
		});
		usersTable.addColumn(draftsCol, "Drafts");
		usersTable.setColumnWidth(draftsCol, 70, Unit.PX);

		// Total
		Column<HTUser, String> totalsCol = new Column<HTUser, String>(new TextCell()) {
			@Override
			public String getValue(HTUser object) {

				return (object.getDrafts() + object.getInbox() + object.getParticipated()) + "";
			}
		};
		totalsCol.setSortable(true);
		sortHandler.setComparator(totalsCol, new Comparator<HTUser>() {
			@Override
			public int compare(HTUser o1, HTUser o2) {
				Integer totals = o1.getDrafts() + o1.getInbox() + o1.getParticipated();
				Integer totals2 = o2.getDrafts() + o2.getInbox() + o2.getParticipated();

				return totals.compareTo(totals2);
			}
		});
		usersTable.getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				SingleSelectionModel<HTUser> usersSelectionModel = (SingleSelectionModel<HTUser>) usersTable
						.getSelectionModel();
				HTUser model = usersSelectionModel.getSelectedObject();
				boolean isSelected = false;
				if (model != null) {
					isSelected = usersSelectionModel.isSelected(model);
				} else {
					model = new HTUser();
				}
				eventBus.fireEvent(new CheckboxSelectionEvent(model, isSelected));
			}
		});

		usersTable.addColumn(totalsCol, "Totals");
		usersTable.setColumnWidth(totalsCol, 70, Unit.PX);
	}
	
	/**
	 * Add the columns to the table.
	 */
	private void initGroupColumns(final SelectionModel<UserGroup> selectionModel) {
		// Checkbox column. This table will uses a checkbox column for selection.
		// Alternatively, you can call cellTable.setSelectionEnabled(true) to enable
		// mouse selection.
		Column<UserGroup, Boolean> checkColumn = new Column<UserGroup, Boolean>(new CheckboxCell(true, false)) {
			@Override
			public Boolean getValue(UserGroup object) {
				// Get the value from the selection model.
				return selectionModel.isSelected(object);
			}
		};
		groupsTable.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
		groupsTable.setColumnWidth(checkColumn, 40, Unit.PX);

		// First name.
		Column<UserGroup, String> name = new Column<UserGroup, String>(new TextCell()) {
			@Override
			public String getValue(UserGroup object) {
				return object.getName();
			}
		};
		name.setSortable(true);
		groupsTable.addColumn(name, "Name");
		groupsTable.setColumnWidth(name, 40, Unit.PCT);

		// Last name.
		Column<UserGroup, String> description = new Column<UserGroup, String>(new TextCell()) {
			@Override
			public String getValue(UserGroup object) {
				return object.getDisplayName();
			}
		};
		description.setSortable(true);
		groupsTable.addColumn(description, "Description");
		
		groupsTable.getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				SingleSelectionModel<UserGroup> usersSelectionModel = (SingleSelectionModel<UserGroup>) groupsTable
						.getSelectionModel();
				
				UserGroup model = usersSelectionModel.getSelectedObject();
				boolean isSelected = false;
				if (model != null) {
					isSelected = usersSelectionModel.isSelected(model);
				} else {
					model = new UserGroup();
				}
				eventBus.fireEvent(new CheckboxSelectionEvent(model, isSelected));
			}
		});
		
	}

	private void initOrgColumns(final SelectionModel<Org> selectionModel) {
		// Checkbox column. This table will uses a checkbox column for selection.
		// Alternatively, you can call cellTable.setSelectionEnabled(true) to enable
		// mouse selection.
		Column<Org, Boolean> checkColumn = new Column<Org, Boolean>(new CheckboxCell(true, false)) {
			@Override
			public Boolean getValue(Org object) {
				// Get the value from the selection model.
				return selectionModel.isSelected(object);
			}
		};
		orgsTable.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
		orgsTable.setColumnWidth(checkColumn, 40, Unit.PX);

		Column<Org, String> name = new Column<Org, String>(new TextCell()) {
			@Override
			public String getValue(Org object) {
				return object.getName();
			}
		};
		name.setSortable(true);
		orgsTable.addColumn(name, "Name");
		orgsTable.setColumnWidth(name, 40, Unit.PCT);

		// Last name.
		Column<Org, String> description = new Column<Org, String>(new TextCell()) {
			@Override
			public String getValue(Org object) {
				return object.getDescription();
			}
		};
		description.setSortable(true);
		orgsTable.addColumn(description, "Description");
		
		orgsTable.getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				SingleSelectionModel<Org> usersSelectionModel = (SingleSelectionModel<Org>) orgsTable
						.getSelectionModel();
				
				Org model = usersSelectionModel.getSelectedObject();
				boolean isSelected = false;
				if (model != null) {
					isSelected = usersSelectionModel.isSelected(model);
				} else {
					model = new Org();
				}
				eventBus.fireEvent(new CheckboxSelectionEvent(model, isSelected));
			}
		});
		
	}

}