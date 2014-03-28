package com.duggan.workflow.client.ui.admin.settings;

import java.util.ArrayList;
import java.util.List;

import gwtupload.client.IUploader;

import com.duggan.workflow.client.model.UploadContext;
import com.duggan.workflow.client.model.UploadContext.UPLOADACTION;
import com.duggan.workflow.client.ui.component.IntegerField;
import com.duggan.workflow.client.ui.component.IssuesPanel;
import com.duggan.workflow.client.ui.component.PasswordField;
import com.duggan.workflow.client.ui.component.TextField;
import com.duggan.workflow.client.ui.upload.custom.Uploader;
import com.duggan.workflow.shared.model.BooleanValue;
import com.duggan.workflow.shared.model.LongValue;
import com.duggan.workflow.shared.model.StringValue;
import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.model.settings.SETTINGNAME;
import com.duggan.workflow.shared.model.settings.Setting;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class SettingsView extends ViewImpl implements SettingsPresenter.ISettingsView {

	private final Widget widget;
	

	public interface Binder extends UiBinder<Widget, SettingsView> {
	}

	@UiField IssuesPanel issues;
	
	@UiField Uploader uploader;
	@UiField FocusPanel panelPicture;
	@UiField FocusPanel uploadPanel;
	@UiField Image imgLogo;
	@UiField TextField txtCompanyName;
	@UiField TextField txtHost;
	@UiField IntegerField txtPort;
	@UiField TextField txtProcotol;
	@UiField CheckBox chkStartTls;
	@UiField CheckBox chkAuth;
	@UiField TextField txtAccount;
	@UiField PasswordField txtPassword;
	
	@UiField Element spnCompanyName;
	@UiField Element spnHost;
	@UiField Element spnPort;
	@UiField Element spnProtocol;
	@UiField Element spnAccount;
	@UiField Element spnPassword;
	@UiField Element spnTLS;
	@UiField Element spnAuth;
	
	@UiField Anchor aSave;
	@UiField Anchor aEdit;
	
	@Inject
	public SettingsView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		
		setEdit(false);		
		panelPicture.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				uploader.removeStyleName("hide");
			}
		});
		
		imgLogo.addErrorHandler(new ErrorHandler() {
			
			@Override
			public void onError(ErrorEvent event) {
				imgLogo.setUrl("images/logo.png");
			}
		});
		
		uploadPanel.addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				uploader.addStyleName("hide");
			}
		});
		
		uploader.addOnFinishUploaderHandler(new IUploader.OnFinishUploaderHandler() {
			
			@Override
			public void onFinish(IUploader uploaderRef) {
				setLogoImage();
				
				imgLogo.setUrl(imgLogo.getUrl()+"&version="+Random.nextInt());
			}
		});
		
		aEdit.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				setEdit(true);
			}
		});
		aSave.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				setEdit(false);
			}
		});
		
		UploadContext context = new UploadContext();
		context.setAction(UPLOADACTION.UPLOADLOGO);
		context.setContext("settingName",SETTINGNAME.ORGLOGO.name());
		context.setAccept("png,jpeg,jpg,gif");
		uploader.setContext(context);
		setLogoImage();
		
	}

	protected void setEdit(boolean edit) {
		UIObject.setVisible(spnCompanyName,!edit);
		UIObject.setVisible(spnHost,!edit);
		UIObject.setVisible(spnPort,!edit);
		UIObject.setVisible(spnProtocol,!edit);
		UIObject.setVisible(spnAccount,!edit);
		UIObject.setVisible(spnPassword,!edit);
		UIObject.setVisible(spnTLS,!edit);
		UIObject.setVisible(spnAuth,!edit);
		
		txtCompanyName.setVisible(edit);
		txtHost.setVisible(edit);
		txtPort.setVisible(edit);
		txtProcotol.setVisible(edit);
		chkStartTls.setVisible(edit);
		chkAuth.setVisible(edit);
		txtAccount.setVisible(edit);
		txtPassword.setVisible(edit);
		
		aSave.setVisible(edit);
		aEdit.setVisible(!edit);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setValues(List<Setting> settings) {
		if(settings!=null)
		for(Setting setting: settings){
			setValue(setting.getName(), setting.getValue());				
		}
	}

	private void setValue(SETTINGNAME name, Value value) {
		Object settingValue = value==null? null : value.getValue()==null? null: value.getValue();
		
		switch (name) {
		case SMTP_ACCOUNT:
			txtAccount.setValue(settingValue==null?null: settingValue.toString());	
			spnAccount.setInnerText(settingValue==null?null: settingValue.toString());;
			break;
		case SMTP_AUTH:
			chkAuth.setValue(settingValue==null?null: (Boolean)settingValue);
			spnAuth.setInnerText(settingValue==null?null: settingValue.toString());;
			break;
		case SMTP_HOST:
			txtHost.setValue(settingValue==null?null: settingValue.toString());
			spnHost.setInnerText(settingValue==null?null: settingValue.toString());
			break;
		case ORGNAME:
			txtCompanyName.setValue(settingValue==null?null: settingValue.toString());
			spnCompanyName.setInnerText(settingValue==null?null: settingValue.toString());
			break;
		case SMTP_PASSWORD:
			txtPassword.setValue(settingValue==null?null: settingValue.toString());
			spnPassword.setInnerText(settingValue==null?null: settingValue.toString());
			break;
		case SMTP_PORT:
			txtPort.setValue(settingValue==null? null: ((Number)settingValue).intValue());
			spnPort.setInnerText(settingValue==null?null: settingValue.toString());
			break;
		case SMTP_PROTOCOL:
			txtProcotol.setValue(settingValue==null?null: settingValue.toString());
			spnProtocol.setInnerText(settingValue==null?null: settingValue.toString());
			break;
		case SMTP_STARTTLS:
			chkStartTls.setValue(settingValue==null?null: (Boolean)settingValue);
			spnTLS.setInnerText(settingValue==null?null: settingValue.toString());
			break;
		
		}
	}
	
	@Override
	public List<Setting> getSettings(){
		List<Setting> lst = new ArrayList<Setting>();
		
		Setting setting  = new Setting(SETTINGNAME.SMTP_ACCOUNT, new StringValue(null, SETTINGNAME.SMTP_ACCOUNT.name(), txtAccount.getValue()));
		lst.add(setting);
		
		setting  = new Setting(SETTINGNAME.ORGNAME, new StringValue(null, SETTINGNAME.ORGNAME.name(), txtCompanyName.getValue()));
		lst.add(setting);
		
		setting  = new Setting(SETTINGNAME.SMTP_HOST, new StringValue(null, SETTINGNAME.SMTP_HOST.name(), txtHost.getValue()));
		lst.add(setting);
		
		setting  = new Setting(SETTINGNAME.SMTP_PASSWORD, new StringValue(null, SETTINGNAME.SMTP_PASSWORD.name(), txtPassword.getValue()));
		lst.add(setting);
		
		setting  = new Setting(SETTINGNAME.SMTP_PORT, new LongValue(null, SETTINGNAME.SMTP_PORT.name(), txtPort.getValue()==null? null: (txtPort.getValue()).longValue()));
		lst.add(setting);
		
		setting  = new Setting(SETTINGNAME.SMTP_PROTOCOL, new StringValue(null, SETTINGNAME.SMTP_PROTOCOL.name(), txtProcotol.getValue()));
		lst.add(setting);
		
		setting  = new Setting(SETTINGNAME.SMTP_AUTH, new BooleanValue(null, SETTINGNAME.SMTP_AUTH.name(), chkAuth.getValue()));
		lst.add(setting);
		
		setting  = new Setting(SETTINGNAME.SMTP_STARTTLS, new BooleanValue(null, SETTINGNAME.SMTP_STARTTLS.name(), chkStartTls.getValue()));
		lst.add(setting);
		return lst;
	}
	
	@Override
	public boolean isValid(){
		issues.clear();
		boolean isValid = true;
		
		return isValid; 
	}
	
	boolean isNullOrEmpty(String value) {
		return value == null || value.trim().length() == 0;
	}

	private void setLogoImage() {
		String moduleUrl = GWT.getModuleBaseURL().replace("/gwtht", "");
		if (moduleUrl.endsWith("/")) {
			moduleUrl = moduleUrl.substring(0, moduleUrl.length() - 1);
		}
		String url = moduleUrl
				+ "/getreport?ACTION=GetLogo&width=179&settingName="+SETTINGNAME.ORGLOGO.name();
		imgLogo.setUrl(url);
	}
	
	@Override
	public HasClickHandlers getSaveLink(){
		return aSave;
	}
}
