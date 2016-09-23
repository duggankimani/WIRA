package com.duggan.workflow.client.ui.activityfeed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import com.duggan.workflow.client.model.TaskType;
import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.activityfeed.components.CommentActivity;
import com.duggan.workflow.client.ui.activityfeed.components.TaskActivity;
import com.duggan.workflow.client.ui.events.CreateDocumentEvent;
import com.duggan.workflow.client.ui.events.ProcessingCompletedEvent;
import com.duggan.workflow.client.ui.events.ProcessingEvent;
import com.duggan.workflow.client.ui.home.HomePresenter;
import com.duggan.workflow.client.ui.home.HomeTabData;
import com.duggan.workflow.client.ui.security.LoginGateKeeper;
import com.duggan.workflow.client.ui.util.DateUtils;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.Activity;
import com.duggan.workflow.shared.model.Comment;
import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.model.Notification;
import com.duggan.workflow.shared.model.ProcessDef;
import com.duggan.workflow.shared.requests.GetActivitiesRequest;
import com.duggan.workflow.shared.requests.GetProcessRequest;
import com.duggan.workflow.shared.requests.GetTaskList;
import com.duggan.workflow.shared.requests.MultiRequestAction;
import com.duggan.workflow.shared.responses.GetActivitiesResponse;
import com.duggan.workflow.shared.responses.GetCommentsResponse;
import com.duggan.workflow.shared.responses.GetProcessResponse;
import com.duggan.workflow.shared.responses.GetTaskListResult;
import com.duggan.workflow.shared.responses.MultiRequestActionResult;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.TabData;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.TabInfo;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.TabContentProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

public class ActivitiesPresenter
		extends
		Presenter<ActivitiesPresenter.MyView, ActivitiesPresenter.IActivitiesProxy> {

	public interface MyView extends View {
		HasWidgets getPanelActivity();

		void bind();

		void createGroup(String label);

		HasClickHandlers getNew();

		void setProcess(ProcessDef process);

		void setTaskList(ArrayList<Doc> tasks);
	}

	@ProxyCodeSplit
	@NameToken({ NameTokens.activitiesPerProcess })
	@UseGatekeeper(LoginGateKeeper.class)
	public interface IActivitiesProxy extends
			TabContentProxyPlace<ActivitiesPresenter> {
	}

	@TabInfo(container = HomePresenter.class)
	public static TabData getTabLabel(LoginGateKeeper adminGatekeeper) {
		HomeTabData data = new HomeTabData("activities", "Activities",
				"icon-dashboard", 10, adminGatekeeper, false);
		return data;
	}

	@Inject
	DispatchAsync requestHelper;
	private String processRefId;

	@Inject
	public ActivitiesPresenter(final EventBus eventBus, final MyView view,
			final IActivitiesProxy proxy) {
		super(eventBus, view, proxy, HomePresenter.SLOT_SetTabContent);
	}

	@Override
	protected void onBind() {
		super.onBind();
		getView().bind();

		getView().getNew().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (processRefId != null) {
					fireEvent(new CreateDocumentEvent(processRefId));
				}
			}
		});
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		processRefId = request.getParameter("processRefId", null);
		loadActivities(processRefId);

	}

	public void loadActivities(String processRefId) {
		MultiRequestAction requests = new MultiRequestAction();

		requests.addRequest(new GetProcessRequest(processRefId));
		GetTaskList recentTasks = new GetTaskList(AppContext.getUserId(),
				TaskType.INBOX);
		recentTasks.setLength(5);
		requests.addRequest(recentTasks);
		requests.addRequest(new GetActivitiesRequest(null));

		fireEvent(new ProcessingEvent());
		requestHelper.execute(requests,
				new TaskServiceCallback<MultiRequestActionResult>() {

					public void processResult(MultiRequestActionResult results) {
						int i = 0;

						GetProcessResponse getProcess = (GetProcessResponse) results
								.get(i++);
						ProcessDef process = getProcess.getProcessDef();
						bindProcess(process);

						GetTaskListResult result = (GetTaskListResult) results
								.get(i++);
						getView().setTaskList(result.getTasks());

						GetActivitiesResponse getActivities = (GetActivitiesResponse) results
								.get(i++);
						bindActivities(getActivities);

						fireEvent(new ProcessingCompletedEvent());
					}
				});

	}

	protected void bindProcess(ProcessDef process) {
		getView().setProcess(process);
	}

	int i = 0;

	protected void bindActivities(GetActivitiesResponse response) {
		getView().getPanelActivity().clear();

		HashMap<Activity, ArrayList<Activity>> activitiesMap = response
				.getActivityMap();
		Set<Activity> keyset = activitiesMap.keySet();
		ArrayList<Activity> activities = new ArrayList<Activity>();

		activities.addAll(keyset);
		Collections.sort(activities);
		Collections.reverse(activities);

		Date dateGroup = null;
		String previousGroup = "";
		Date today = new Date();
		boolean activitiesOlderThanAMonth = false;
		for (Activity activity : activities) {
			Date created = activity.getCreated();

			if (!activitiesOlderThanAMonth)
				if (!DateUtils.isSameDate(dateGroup, created)) {
					if (dateGroup != null
							&& CalendarUtil.getDaysBetween(created, today) > 31) {
						activitiesOlderThanAMonth = true;
					}
					dateGroup = created;
					String groupName = DateUtils
							.getDateGroupDescription(dateGroup);

					if (!previousGroup.equals(groupName)) {
						getView().createGroup(groupName);
					}

					previousGroup = groupName;
				}

			bind(activity, false);
			ArrayList<Activity> children = activitiesMap.get(activity);
			if (children != null) {
				for (Activity child : children) {
					bind(child, true);
				}

			}
		}
	}

	private void bind(Activity activity, boolean b) {

		if (activity instanceof Notification) {
			Notification note = (Notification) activity;
			if (note.getSubject() != null) {
				TaskActivity activityView = new TaskActivity(
						(Notification) activity);
				getView().getPanelActivity().add(activityView);
			}
		} else {
			Comment comment = (Comment) activity;
			if (comment.getSubject() != null) {
				CommentActivity activityView = new CommentActivity(comment);
				getView().getPanelActivity().add(activityView);
			}
		}
	}

	protected void bindCommentsResult(GetCommentsResponse commentsResult) {

	}

}
