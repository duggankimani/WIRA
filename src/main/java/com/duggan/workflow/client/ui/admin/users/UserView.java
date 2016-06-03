package com.duggan.workflow.client.ui.admin.users;

import java.util.List;

import com.duggan.workflow.client.event.CheckboxSelectionEvent;
import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.ui.component.Checkbox;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.HTUser;
import com.duggan.workflow.shared.model.Org;
import com.duggan.workflow.shared.model.UserGroup;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

public class UserView extends ViewImpl implements UserPresenter.MyView {

	private final Widget widget;
	@UiField
	Anchor aNewUser;
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

	public interface Binder extends UiBinder<Widget, UserView> {
	}

	PlaceManager placeManager;

	@Inject
	public UserView(final Binder binder, PlaceManager manager) {
		widget = binder.createAndBindUi(this);
		placeManager = manager;

		aUserstab.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				placeManager.revealPlace(new PlaceRequest.Builder()
						.nameToken(NameTokens.usermgt).with("page", "user")
						.build());
			}
		});

		aGroupstab.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				placeManager.revealPlace(new PlaceRequest.Builder()
						.nameToken(NameTokens.usermgt).with("page", "group")
						.build());
			}
		});
		
		aUnitstab.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				placeManager.revealPlace(new PlaceRequest.Builder()
						.nameToken(NameTokens.usermgt).with("page", "org")
						.build());
			}
		});
	}

	@Override
	public void bindUsers(List<HTUser> users) {
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
					AppContext.fireEvent(new CheckboxSelectionEvent(model,
							event.getValue()));
				}
			});

			tblUser.setWidget(i, j++, box);
			tblUser.setWidget(i, j++, new HTMLPanel(user.getSurname()));
			tblUser.setWidget(i, j++, new HTMLPanel(user.getName()));
			tblUser.setWidget(i, j++, new HTMLPanel(user.getUserId()));
			tblUser.setWidget(i, j++, new HTMLPanel(user.getEmail()));
			tblUser.setWidget(i, j++, new HTMLPanel(user.getGroupsAsString()));
			tblUser.setWidget(i, j++, new HTMLPanel(user.getInbox() + ""));
			tblUser.setWidget(i, j++,
					new HTMLPanel(user.getParticipated() + ""));
			tblUser.setWidget(i, j++, new HTMLPanel(user.getDrafts() + ""));
			tblUser.setWidget(i, j++, new HTMLPanel(user.getTotal() + ""));
			++i;
		}
	}

	@Override
	public void bindGroups(List<UserGroup> groups) {
		tblGroup.removeAllRows();
		clearSelections();
		
		int j = 0;
		tblGroup.setWidget(0, j++, new HTMLPanel("<strong>#</strong>"));
		tblGroup.setWidget(0, j++, new HTMLPanel("<strong>Code</strong>"));
		tblGroup.setWidget(0, j++,
				new HTMLPanel("<strong>Description</strong>"));

		int i = 1;
		for (UserGroup group : groups) {
			j = 0;
			
			Checkbox box = new Checkbox(group);
			box.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
				@Override
				public void onValueChange(ValueChangeEvent<Boolean> event) {
					Object model = ((Checkbox) (event.getSource())).getModel();
					AppContext.fireEvent(new CheckboxSelectionEvent(model,
							event.getValue()));
				}
			});

			tblGroup.setWidget(i, j++, box);
			tblGroup.setWidget(i, j++, new HTMLPanel(group.getName()));
			tblGroup.setWidget(i, j++, new HTMLPanel(group.getFullName()));
			++i;
		}

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
	public void bindOrgs(List<Org> orgs) {
		tblOrgs.clear();
		tblOrgs.removeAllRows();
		clearSelections();
		
		int j = 0;
		tblOrgs.setWidget(0, j++, new HTMLPanel("<strong>#</strong>"));
		tblOrgs.getFlexCellFormatter().setWidth(0, (j - 1), "20px");
		tblOrgs.setWidget(0, j++, new HTMLPanel("<strong>Name</strong>"));

		int i = 1;
		for (Org org : orgs) {
			j = 0;
			
			Checkbox box = new Checkbox(org);
			box.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
				@Override
				public void onValueChange(ValueChangeEvent<Boolean> event) {
					Object model = ((Checkbox) (event.getSource())).getModel();
					AppContext.fireEvent(new CheckboxSelectionEvent(model,
							event.getValue()));
				}
			});

			tblOrgs.setWidget(i, j++, box);
			tblOrgs.setWidget(i, j++, new HTMLPanel(org.getName()));
			++i;
		}

	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public HasClickHandlers getaNewUser() {
		return aNewUser;
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
	
	public HasClickHandlers getEditUser(){
		return aEditUser;
	}
	
	public HasClickHandlers getDeleteUser(){
		return aDeleteUser;
	}
	
	public HasClickHandlers getEditGroup(){
		return aEditGroup;
	}
	
	public HasClickHandlers getDeleteGroup(){
		return aDeleteGroup;
	}
	
	public HasClickHandlers getNewOrg(){
		return aNewOrg;
	}
	
	public HasClickHandlers getEditOrg(){
		return aEditOrg;
	}
	
	public HasClickHandlers getDeleteOrg(){
		return aDeleteOrg;
	}
}