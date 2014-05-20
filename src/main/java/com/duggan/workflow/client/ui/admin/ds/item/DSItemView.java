package com.duggan.workflow.client.ui.admin.ds.item;

import java.util.Date;

import com.duggan.workflow.client.ui.util.DateUtils;
import com.duggan.workflow.shared.model.RDBMSType;
import com.duggan.workflow.shared.model.Status;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class DSItemView extends ViewImpl implements
		DSItemPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, DSItemView> {
	}
	
	@UiField SpanElement spnRDBMSName;
	@UiField SpanElement spnConfigName;
	@UiField SpanElement spnJNDIName;
	@UiField SpanElement spnDriver;
	@UiField SpanElement spnURL;
	@UiField SpanElement spnUser;
	@UiField SpanElement spnStatus;
	@UiField SpanElement spnLastModified;
	
	@UiField Anchor aEdit;
	@UiField Anchor aDelete;
	@UiField Anchor aTest;
	
	String url=null;
	Long imageId=null;
	String imageName=null;
	
	@Inject
	public DSItemView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	public HasClickHandlers getTestButton(){
		return aTest;
	}
	
	public HasClickHandlers getEditButton(){
		return aEdit;
	}
	
	public HasClickHandlers getDeleteButton(){
		return aDelete;
	}

	public void setValues(Long dsConfigId,
			RDBMSType rdbms,String name,String jndiName,String driverName,String url, 
			String user, Date lastModified, Status status){
	
		spnConfigName.setInnerText(name);
		spnRDBMSName.setInnerText(rdbms.getDisplayName());
		spnURL.setInnerText(url);
		spnJNDIName.setInnerText(jndiName);
		spnDriver.setInnerText(driverName);
		spnUser.setInnerText(user);
				
		if(lastModified!=null)
			spnLastModified.setInnerText(DateUtils.CREATEDFORMAT.format(lastModified));
		
		spnStatus.setInnerText(status.name());
		
		switch (status) {
		case INACTIVE:
			spnStatus.setClassName("label label-warning arrowed-in");
			break;
			
		case RUNNING:
			spnStatus.setClassName("label label-success arrowed-in");
			break;
		}
		
	}
}
