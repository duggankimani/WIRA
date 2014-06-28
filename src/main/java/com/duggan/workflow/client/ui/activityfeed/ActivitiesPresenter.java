package com.duggan.workflow.client.ui.activityfeed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.activityfeed.components.CommentActivity;
import com.duggan.workflow.client.ui.activityfeed.components.TaskActivity;
import com.duggan.workflow.client.ui.admin.TabDataExt;
import com.duggan.workflow.client.ui.events.ProcessingCompletedEvent;
import com.duggan.workflow.client.ui.events.ProcessingEvent;
import com.duggan.workflow.client.ui.home.HomePresenter;
import com.duggan.workflow.client.ui.login.LoginGateKeeper;
import com.duggan.workflow.client.ui.task.TaskPresenter;
import com.duggan.workflow.shared.model.Activity;
import com.duggan.workflow.shared.model.Comment;
import com.duggan.workflow.shared.model.Notification;
import com.duggan.workflow.shared.requests.GetActivitiesRequest;
import com.duggan.workflow.shared.requests.MultiRequestAction;
import com.duggan.workflow.shared.responses.GetActivitiesResponse;
import com.duggan.workflow.shared.responses.GetCommentsResponse;
import com.duggan.workflow.shared.responses.MultiRequestActionResult;
import com.google.gwt.user.client.ui.HasWidgets;
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

public class ActivitiesPresenter extends
		Presenter<ActivitiesPresenter.MyView, ActivitiesPresenter.IActivitiesProxy> {

	public interface MyView extends View {
		HasWidgets getPanelActivity();
		void bind();
	}
	
	@ProxyCodeSplit
	@NameToken(NameTokens.home)
	@UseGatekeeper(LoginGateKeeper.class)
	public interface IActivitiesProxy extends TabContentProxyPlace<ActivitiesPresenter> {
	}
	
	@TabInfo(container = HomePresenter.class)
    static TabData getTabLabel(LoginGateKeeper adminGatekeeper) {
        return new TabDataExt("Activities","icon-dashboard",2, adminGatekeeper);
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

	protected void bindActivities(GetActivitiesResponse response) {
		getView().getPanelActivity().clear();
		
		Map<Activity, List<Activity>> activitiesMap = response.getActivityMap();
		Set<Activity> keyset = activitiesMap.keySet();
		List<Activity> activities= new ArrayList<Activity>();
		
		activities.addAll(keyset);
		Collections.sort(activities);
		Collections.reverse(activities);
		
		for(Activity activity: activities){
			bind(activity,false);	
			List<Activity> children = activitiesMap.get(activity);	
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
