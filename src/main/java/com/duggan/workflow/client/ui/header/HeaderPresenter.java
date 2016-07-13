package com.duggan.workflow.client.ui.header;

import java.util.Date;
import java.util.HashMap;

import com.duggan.workflow.client.model.TaskType;
import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.ui.events.AdminPageLoadEvent;
import com.duggan.workflow.client.ui.events.AdminPageLoadEvent.AdminPageLoadHandler;
import com.duggan.workflow.client.ui.events.AfterSaveEvent;
import com.duggan.workflow.client.ui.events.AfterSaveEvent.AfterSaveHandler;
import com.duggan.workflow.client.ui.events.AlertLoadEvent;
import com.duggan.workflow.client.ui.events.ContextLoadedEvent;
import com.duggan.workflow.client.ui.events.ContextLoadedEvent.ContextLoadedHandler;
import com.duggan.workflow.client.ui.events.LoadAlertsEvent;
import com.duggan.workflow.client.ui.events.LoadAlertsEvent.LoadAlertsHandler;
import com.duggan.workflow.client.ui.events.NotificationsLoadEvent;
import com.duggan.workflow.client.ui.events.SearchEvent;
import com.duggan.workflow.client.ui.notifications.NotificationsPresenter;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.SearchFilter;
import com.duggan.workflow.shared.requests.GetAlertCount;
import com.duggan.workflow.shared.requests.GetNotificationsAction;
import com.duggan.workflow.shared.requests.LogoutAction;
import com.duggan.workflow.shared.requests.MultiRequestAction;
import com.duggan.workflow.shared.responses.GetAlertCountResult;
import com.duggan.workflow.shared.responses.GetNotificationsActionResult;
import com.duggan.workflow.shared.responses.LogoutActionResult;
import com.duggan.workflow.shared.responses.MultiRequestActionResult;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasBlurHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.TextBox;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.Tab;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.wira.commons.client.security.CurrentUser;
import com.wira.commons.shared.models.HTUser;
import com.wira.commons.shared.models.Version;
import com.duggan.workflow.client.service.TaskServiceCallback;

public class HeaderPresenter extends
		PresenterWidget<HeaderPresenter.IHeaderView> implements
		AfterSaveHandler, AdminPageLoadHandler, ContextLoadedHandler,
		LoadAlertsHandler {

	public interface IHeaderView extends View {
		HasClickHandlers getLogout();

		void setValues(String userNames, String userGroups, String orgName);

		Anchor getNotificationsButton();

		void setPopupVisible();

		void setCount(Integer count);

		HasBlurHandlers getpopupContainer();

		void setLoading(boolean b);

		void setAdminPageLookAndFeel(boolean isAdminPage);

		void changeFocus();

		void showAdminLink(boolean admin);

		void setVersionInfo(Date created, String date, String version);

		void setImage(HTUser currentUser);

		TextBox getSearchField();

		void showTab(Tab tab);

		void refreshLinks();
	}

	@Inject
	DispatchAsync dispatcher;

	@Inject
	PlaceManager placeManager;

	@Inject
	NotificationsPresenter notifications;

	boolean onFocus = true;

	@ContentSlot
	public static final Type<RevealContentHandler<?>> NOTIFICATIONS_SLOT = new Type<RevealContentHandler<?>>();

	private CurrentUser currentUser;

	private Version version;

	String searchTerm = "";

	Timer timer = new Timer() {

		@Override
		public void run() {
			search();
		}
	};

	@Inject
	public HeaderPresenter(final EventBus eventBus, final IHeaderView view,
			final CurrentUser currentUser, final Version version) {
		super(eventBus, view);
		this.currentUser = currentUser;
		this.version = version;
		alertTimer.scheduleRepeating(alertReloadInterval);
	}

	static int alertReloadInterval = 60 * 1000 * 5; // 5 mins
	static long lastLoad = 0;
	private Timer alertTimer = new Timer() {

		@Override
		public void run() {
			loadAlertCount();
		}
	};

	@Override
	protected void onBind() {
		super.onBind();
		this.addRegisteredHandler(AfterSaveEvent.TYPE, this);
		this.addRegisteredHandler(AdminPageLoadEvent.TYPE, this);
		this.addRegisteredHandler(ContextLoadedEvent.TYPE, this);
		this.addRegisteredHandler(LoadAlertsEvent.TYPE, this);

		getView().getLogout().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				logout();
			}
		});

		getView().getNotificationsButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// Clicks must be atleast 5 min apart
				long currentTime = System.currentTimeMillis();
				if ((currentTime - lastLoad) > alertReloadInterval) {
					lastLoad = currentTime;
				} else {
					return;
				}

				loadAlerts();
			}
		});

		getView().getSearchField().addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				String txt = getView().getSearchField().getValue().trim();

				if (!txt.equals(searchTerm)
						|| event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					searchTerm = txt;
					timer.cancel();
					timer.schedule(400);
				}

			}
		});

	}

	protected void search() {
		timer.cancel();

		if (searchTerm != null && searchTerm.trim().isEmpty()) {
			searchTerm = null;
		}

		SearchFilter filter = new SearchFilter();
		filter.setPhrase(searchTerm);
		filter.setSubject(searchTerm);

		fireEvent(new SearchEvent(filter));
	}

	protected void loadAlerts() {

		dispatcher.execute(new GetNotificationsAction(AppContext.getUserId()),
				new TaskServiceCallback<GetNotificationsActionResult>() {

					@Override
					public void processResult(
							GetNotificationsActionResult notificationsResult) {

						assert notificationsResult != null;
						fireEvent(new NotificationsLoadEvent(
								notificationsResult.getNotifications()));
						getView().setLoading(false);
					}
				});
	}

	/**
	 * Called too many times - reloading context/ alert counts from here slows
	 * the application down.
	 * 
	 * TODO: Find Out why
	 */
	@Override
	protected void onReset() {
		super.onReset();
		setInSlot(NOTIFICATIONS_SLOT, notifications);
		// loadAlertCount();
		// AppContext.reloadContext();
	}

	@Override
	protected void onReveal() {
		super.onReveal();
		HTUser user = currentUser.getUser();
		if (user != null) {
			getView().setImage(user);
			getView().showAdminLink(user.isAdmin());
			getView().setValues(user.getSurname(), user.getGroupsAsString(),
					AppContext.getOrganizationName());
			getView().setVersionInfo(version.getCreated(), version.getDate(),
					version.getVersion());
		}

		loadAlertCount();
	}

	protected void loadAlertCount() {
		alertTimer.cancel();
		getView().setLoading(true);
		MultiRequestAction action = new MultiRequestAction();
		action.addRequest(new GetAlertCount());

		dispatcher.execute(action,
				new TaskServiceCallback<MultiRequestActionResult>() {
					@Override
					public void processResult(MultiRequestActionResult results) {

						GetAlertCountResult result = (GetAlertCountResult) results
								.get(0);
						HashMap<TaskType, Integer> alerts = result.getCounts();
						getView().setCount(alerts.get(TaskType.NOTIFICATIONS));
						fireEvent(new AlertLoadEvent(alerts));
						alertTimer.schedule(alertReloadInterval);
					}
				});

	}

	protected void logout() {
		dispatcher.execute(new LogoutAction(),
				new TaskServiceCallback<LogoutActionResult>() {
					@Override
					public void processResult(LogoutActionResult result) {
						AppContext.destroy();
						Window.Location.replace(NameTokens.loginPage);
//						placeManager.revealPlace(new PlaceRequest.Builder()
//								.nameToken(NameTokens.splash).build());
					}
				});
	}

	@Override
	public void onAfterSave(AfterSaveEvent event) {
		loadAlertCount();
	}

	@Override
	public void onAdminPageLoad(AdminPageLoadEvent event) {
		getView().setAdminPageLookAndFeel(event.isAdminPage());
	}

	@Override
	public void onContextLoaded(ContextLoadedEvent event) {
		getView().refreshLinks();
		HTUser user = currentUser.getUser();
		getView().setImage(user);
		getView().showAdminLink(user.isAdmin());
		getView().setValues(user.getSurname(), user.getGroupsAsString(),
				AppContext.getOrganizationName());
		loadAlertCount();
	}

	@Override
	public void onLoadAlerts(LoadAlertsEvent event) {
		loadAlertCount();
	}

}
