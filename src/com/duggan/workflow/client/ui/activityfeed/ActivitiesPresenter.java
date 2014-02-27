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
import com.duggan.workflow.client.ui.events.ProcessingCompletedEvent;
import com.duggan.workflow.client.ui.events.ProcessingEvent;
import com.duggan.workflow.client.ui.home.HomePresenter;
import com.duggan.workflow.client.ui.login.LoginGateKeeper;
import com.duggan.workflow.shared.model.Activity;
import com.duggan.workflow.shared.model.Comment;
import com.duggan.workflow.shared.model.Notification;
import com.duggan.workflow.shared.requests.GetActivitiesRequest;
import com.duggan.workflow.shared.requests.GetAttachmentsRequest;
import com.duggan.workflow.shared.requests.GetCommentsRequest;
import com.duggan.workflow.shared.requests.MultiRequestAction;
import com.duggan.workflow.shared.responses.GetActivitiesResponse;
import com.duggan.workflow.shared.responses.GetCommentsResponse;
import com.duggan.workflow.shared.responses.MultiRequestActionResult;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

public class ActivitiesPresenter extends
		Presenter<ActivitiesPresenter.MyView, ActivitiesPresenter.MyProxy> {

	public interface MyView extends View {

		HasWidgets getPanelActivity();
		
	}

	@Inject DispatchAsync requestHelper;
	
	@ProxyCodeSplit
	@NameToken(NameTokens.activities)
	@UseGatekeeper(LoginGateKeeper.class)
	public interface MyProxy extends ProxyPlace<ActivitiesPresenter> {
	}

	@Inject
	public ActivitiesPresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy) {
		super(eventBus, view, proxy);
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, HomePresenter.ACTIVITIES_SLOT, this);
	}
	 @Override
	public void prepareFromRequest(PlaceRequest request) {
		 super.prepareFromRequest(request);
		 loadActivities();
	}
	 
	private void loadActivities() {
		MultiRequestAction requests = new MultiRequestAction();
		requests.addRequest(new GetCommentsRequest(null));
		requests.addRequest(new GetActivitiesRequest(null));
		
		fireEvent(new ProcessingEvent());
		requestHelper.execute(requests, 
				new TaskServiceCallback<MultiRequestActionResult>() {
			
			public void processResult(MultiRequestActionResult results) {
//				GetCommentsResponse commentsResult = (GetCommentsResponse)results.get(1);
//				bindCommentsResult(commentsResult);
				GetActivitiesResponse getActivities = (GetActivitiesResponse)results.get(1);
				bindActivities(getActivities);
				
				fireEvent(new ProcessingCompletedEvent());
			}
		});
		
		
	}

	protected void bindActivities(GetActivitiesResponse response) {
		getView().getPanelActivity().clear();
		
		//Map<Activity, List<Activity>> activitiesMap = response.getActivityMap();
		//setInSlot(ACTIVITY_SLOT, null);
		Map<Activity, List<Activity>> activitiesMap = response.getActivityMap();
		System.out.println(activitiesMap.size());
		Set<Activity> keyset = activitiesMap.keySet();
		List<Activity> activities= new ArrayList<Activity>();
		
		activities.addAll(keyset);
		Collections.sort(activities);
		Collections.reverse(activities);
		
		for(Activity activity: activities){
			bind(activity,false);	
			System.err.println(activity);
			List<Activity> children = activitiesMap.get(activity);	
			if(children!=null){
				for(Activity child: children){
					System.err.println(child);
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
	}
}
