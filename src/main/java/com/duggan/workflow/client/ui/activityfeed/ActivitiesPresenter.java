package com.duggan.workflow.client.ui.activityfeed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.ui.activityfeed.components.CommentActivity;
import com.duggan.workflow.client.ui.activityfeed.components.TaskActivity;
import com.duggan.workflow.client.ui.admin.TabDataExt;
import com.duggan.workflow.client.ui.events.ProcessingCompletedEvent;
import com.duggan.workflow.client.ui.events.ProcessingEvent;
import com.duggan.workflow.client.ui.home.HomePresenter;
import com.duggan.workflow.client.ui.security.LoginGateKeeper;
import com.duggan.workflow.client.ui.util.DateUtils;
import com.duggan.workflow.shared.model.Activity;
import com.duggan.workflow.shared.model.Comment;
import com.duggan.workflow.shared.model.Notification;
import com.duggan.workflow.shared.requests.GetActivitiesRequest;
import com.duggan.workflow.shared.requests.MultiRequestAction;
import com.duggan.workflow.shared.responses.GetActivitiesResponse;
import com.duggan.workflow.shared.responses.GetCommentsResponse;
import com.duggan.workflow.shared.responses.MultiRequestActionResult;
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
import com.duggan.workflow.client.service.TaskServiceCallback;

public class ActivitiesPresenter extends
		Presenter<ActivitiesPresenter.MyView, ActivitiesPresenter.IActivitiesProxy> {

	public interface MyView extends View {
		HasWidgets getPanelActivity();
		void bind();
		void createGroup(String label);
	}
	
	@ProxyCodeSplit
	@NameToken(NameTokens.home)
	@UseGatekeeper(LoginGateKeeper.class)
	public interface IActivitiesProxy extends TabContentProxyPlace<ActivitiesPresenter> {
	}
	
	@TabInfo(container = HomePresenter.class)
    static TabData getTabLabel(LoginGateKeeper adminGatekeeper) {
		TabDataExt data = new TabDataExt("Activities","icon-dashboard",10, adminGatekeeper,false);
        return data;
    }

	@Inject DispatchAsync requestHelper;

	@Inject
	public ActivitiesPresenter(final EventBus eventBus, final MyView view,
			final IActivitiesProxy proxy){
		super(eventBus, view, proxy, HomePresenter.SLOT_SetTabContent);
	}
	
	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		loadActivities();
	}
	 
	public void loadActivities() {
		MultiRequestAction requests = new MultiRequestAction();
		requests.addRequest(new GetActivitiesRequest(null));
		
		fireEvent(new ProcessingEvent());
		requestHelper.execute(requests, 
				new TaskServiceCallback<MultiRequestActionResult>() {
			
			public void processResult(MultiRequestActionResult results) {
				GetActivitiesResponse getActivities = (GetActivitiesResponse)results.get(0);
				bindActivities(getActivities);
				
				fireEvent(new ProcessingCompletedEvent());
			}
		});
		
		
	}

	int i=0;
	protected void bindActivities(GetActivitiesResponse response) {
		getView().getPanelActivity().clear();
		
		HashMap<Activity, ArrayList<Activity>> activitiesMap = response.getActivityMap();
		Set<Activity> keyset = activitiesMap.keySet();
		ArrayList<Activity> activities= new ArrayList<Activity>();
		
		activities.addAll(keyset);
		Collections.sort(activities);
		Collections.reverse(activities);
		
		Date dateGroup = null;
		String previousGroup="";
		Date today = new Date();
		boolean activitiesOlderThanAMonth=false;
		for(Activity activity: activities){
			Date created = activity.getCreated();
			
			if(!activitiesOlderThanAMonth)
			if(!DateUtils.isSameDate(dateGroup,created)){
				if(dateGroup!=null && CalendarUtil.getDaysBetween(created, today)>31){
					activitiesOlderThanAMonth=true;
				}
				dateGroup = created;
				String groupName = DateUtils.getDateGroupDescription(dateGroup);
				
				if(!previousGroup.equals(groupName)){
					getView().createGroup(groupName);
				}
				
				previousGroup=groupName;
			}
			
			bind(activity,false);
			ArrayList<Activity> children = activitiesMap.get(activity);	
			if(children!=null){
				for(Activity child: children){										
					bind(child, true);
				}
				
			}
		}
	}

	private void bind(Activity activity, boolean b) {
	
		if(activity instanceof Notification){
			TaskActivity activityView = new TaskActivity((Notification)activity);
			getView().getPanelActivity().add(activityView);
		}else{
			CommentActivity activityView = new CommentActivity((Comment)activity);
			getView().getPanelActivity().add(activityView);
		}
	}

	protected void bindCommentsResult(GetCommentsResponse commentsResult) {
		
	}

	@Override
	protected void onBind() {
		super.onBind();
		getView().bind();
	}
}
