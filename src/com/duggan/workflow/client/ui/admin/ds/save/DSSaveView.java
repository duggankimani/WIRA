package com.duggan.workflow.client.ui.admin.ds.save;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.duggan.workflow.client.model.UploadContext;
import com.duggan.workflow.client.model.UploadContext.UPLOADACTION;
import com.duggan.workflow.client.ui.AppManager;
import com.duggan.workflow.client.ui.admin.component.ListField;
import com.duggan.workflow.client.ui.component.DropDownList;
import com.duggan.workflow.client.ui.component.IssuesPanel;
import com.duggan.workflow.client.ui.component.TextField;
import com.duggan.workflow.client.ui.upload.custom.Uploader;
import com.duggan.workflow.shared.model.Attachment;
import com.duggan.workflow.shared.model.DSConfiguration;
import com.duggan.workflow.shared.model.DocumentType;
import com.duggan.workflow.shared.model.ProcessDef;
import com.duggan.workflow.shared.model.RDBMSType;
import com.duggan.workflow.shared.model.form.KeyValuePair;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.PopupViewImpl;

public class DSSaveView extends PopupViewImpl implements
		DSSavePresenter.IDSSaveView {

	private final Widget widget;
	@UiField IssuesPanel issues;
	@UiField PopupPanel divPopUp;
	@UiField Anchor aSave;
	@UiField Anchor aCancel;
	@UiField Anchor aClose;
	@UiField HTMLPanel divProcessDetails;
	@UiField HTMLPanel panelUseJNDI;
	@UiField HTMLPanel panelJDBC;
	@UiField DropDownList<RDBMSType> lstRDBMS;
	@UiField CheckBox chkUseJNDI;
	@UiField TextField txtJNDIName;
	@UiField TextField txtDriver;
	@UiField TextField txtUrl;
	@UiField TextField txtUser;
	@UiField TextField txtConfig;
	@UiField PasswordTextBox txtPassword;
	
	private Long id;

	public interface Binder extends UiBinder<Widget, DSSaveView> {
	}

	@Inject
	public DSSaveView(final EventBus eventBus, final Binder binder) {
		super(eventBus);
		widget = binder.createAndBindUi(this);
		
		lstRDBMS.addValueChangeHandler(new ValueChangeHandler<RDBMSType>() {
			@Override
			public void onValueChange(ValueChangeEvent<RDBMSType> event) {
				RDBMSType type = event.getValue();
				txtDriver.setText(type.getDriverClass());
				txtUrl.setText(type.getUrl());
			}
		});
		
		aCancel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				hide();
			}
		});
		
		aClose.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				hide();
			}
		});
		
		chkUseJNDI.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				boolean selected = event.getValue();
				if(selected){
					panelUseJNDI.removeStyleName("hide");
					panelJDBC.addStyleName("hide");
				}else{
					panelUseJNDI.addStyleName("hide");
					panelJDBC.removeStyleName("hide");
				}
			}
		});
	
		lstRDBMS.setItems(Arrays.asList(RDBMSType.values()));
		
		int[] position = AppManager.calculatePosition(5, 50);
		divPopUp.setPopupPosition(position[1], position[0]);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	@Override
	public void setValues(Long dsConfigId,
			RDBMSType rdbms,String name,String jndiName,String driverName,String url, 
			String user, String password, boolean isJNDI) {
		this.id = dsConfigId;
		txtConfig.setText(name);
		lstRDBMS.setValue(rdbms);
		txtJNDIName.setText(jndiName);
		txtDriver.setText(driverName);
		txtUrl.setText(url);
		txtUser.setText(user);
		txtPassword.setText(password);
		chkUseJNDI.setValue(isJNDI);
	}

	public DSConfiguration getConfiguration(){
		DSConfiguration config = new DSConfiguration();
		config.setRDBMS(lstRDBMS.getValue());
		config.setId(id);
		config.setJNDI(chkUseJNDI.getValue()==null? false: chkUseJNDI.getValue());
		config.setName(txtConfig.getValue());
		
		if(config.isJNDI()){
			config.setJNDIName(txtJNDIName.getText());
		}else{
			config.setPassword(txtPassword.getText());
			config.setDriver(txtDriver.getText());
			config.setURL(txtUrl.getText());
			config.setUser(txtUser.getText());
			
		}
		
		return config;
	}
	
	public boolean isValid(){
		issues.clear();
		boolean isValid = true;
		
		if(lstRDBMS.getValue()==null){
			issues.addError("Please select an RDBMS");
			isValid=false;
		}
		
		if(isNullOrEmpty(txtConfig.getValue())){
			issues.addError("Please specify config name");
			isValid=false;
		}
		
		if(chkUseJNDI.getValue()!=null && chkUseJNDI.getValue()){
		
			if(isNullOrEmpty(txtJNDIName.getValue())){
				issues.addError("Please specify JNDI name");
				isValid=false;
			}
			
		}else{
			if(isNullOrEmpty(txtDriver.getValue())){
				issues.addError("Please specify database Driver");
				isValid=false;
			}
			
			if(isNullOrEmpty(txtUrl.getValue())){
				issues.addError("Please specify datasource URL");
				isValid=false;
			}
		
//			if(isNullOrEmpty(txtUser.getValue())){
//				issues.addError("Please specify database user");
//				isValid=false;
//			}
			
		}
				
		return isValid;
	}
	
	public HasClickHandlers getSave(){
		return aSave;
	}
	
	public HasClickHandlers getCancel(){
		return aClose;
	}
	
	boolean isNullOrEmpty(String value) {
		return value == null || value.trim().length() == 0;
	}

	@Override
	public void setConfigurationId(Long id) {
		this.id = id;
	}
}
