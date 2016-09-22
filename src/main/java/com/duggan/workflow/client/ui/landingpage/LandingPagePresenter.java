package com.duggan.workflow.client.ui.landingpage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.home.HomePresenter;
import com.duggan.workflow.client.ui.home.HomeTabData;
import com.duggan.workflow.client.ui.security.LoginGateKeeper;
import com.duggan.workflow.client.ui.util.StringUtils;
import com.duggan.workflow.shared.model.DocumentType;
import com.duggan.workflow.shared.model.ProcessCategory;
import com.duggan.workflow.shared.requests.GetDocumentTypesRequest;
import com.duggan.workflow.shared.responses.GetDocumentTypesResponse;
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
public class LandingPagePresenter extends Presenter<LandingPagePresenter.ILandingPageView, LandingPagePresenter.ILandingPageProxy>  {
    interface ILandingPageView extends View  {

		void setProcesses(ArrayList<ProcessCategory> categories);

		void setNoProcess(boolean noProcessesAssignedToUser);
    }
    
    @ProxyCodeSplit
    @NameToken(NameTokens.home)
	@UseGatekeeper(LoginGateKeeper.class)
	public interface ILandingPageProxy extends TabContentProxyPlace<LandingPagePresenter> {
	}
	
	@TabInfo(container = HomePresenter.class)
    static TabData getTabLabel(LoginGateKeeper gatekeeper) {
		HomeTabData data = new HomeTabData("landing","Landing","icon-dashboard",11,gatekeeper, false);
        return data;
    }
	
	@Inject DispatchAsync requestHelper;

    @Inject
    LandingPagePresenter(
            EventBus eventBus,
            ILandingPageView view, 	
            ILandingPageProxy proxy) {
        super(eventBus, view, proxy, HomePresenter.SLOT_SetTabContent);
        
    }
    
    @Override
    public void prepareFromRequest(PlaceRequest request) {
        super.prepareFromRequest(request);
        load();
    }
    
    protected void onBind() {
        super.onBind();
    }
    
    public void load(){
		GetDocumentTypesRequest req=new GetDocumentTypesRequest();
		requestHelper.execute(req,new TaskServiceCallback<GetDocumentTypesResponse>() {
			@Override
			public void processResult(GetDocumentTypesResponse result) {
				ArrayList<DocumentType> types=result.getDocumentTypes();
				Collections.sort(types, new Comparator<DocumentType>() {
					@Override
					public int compare(DocumentType o1, DocumentType o2) {
						
						String o1Category = o1.getCategory();
						String o2Category = o2.getCategory();
						
						if(o1Category==null){
							return -1;
						}
						
						if(o2Category==null){
							return 1;
						}
						
						return o1Category.compareTo(o2Category);
					}
				});
				
				setDocumentItems(types);
			}
		});
	}
    
    public void setDocumentItems(ArrayList<DocumentType> types){
		ArrayList<ProcessCategory> categories  = new ArrayList<ProcessCategory>();
		ProcessCategory all = new ProcessCategory();
		all.setName("All");
		all.setRefId("All");
		categories.add(all);
		
		String category="";
		ProcessCategory processCat = new ProcessCategory();
		for(final DocumentType type : types) {
			all.addChild(type);
			
			if(type.getCategory()!=null && !category.equals(type.getCategory())){
				category = type.getCategory();
				processCat = new ProcessCategory();
				processCat.setName(category);
				processCat.setRefId(StringUtils.camelCase(category));
				categories.add(processCat);
			}else if(type.getCategory()==null && !category.equals("NULL")){
				category="General";
				processCat = new ProcessCategory();
				processCat.setName(category);
				categories.add(processCat);
			}
			
			processCat.addChild(type);
		}
		
		if(types.size()==0){
			getView().setNoProcess(true);
		}else{
			getView().setProcesses(categories);
		}
	}
	
    
}