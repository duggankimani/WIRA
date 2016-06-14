package com.duggan.workflow.client.ui.save;

import static com.duggan.workflow.client.ui.save.CreateDocPresenter.UPLOAD_SLOT;
import static com.duggan.workflow.client.ui.util.DateUtils.DATEFORMAT;

import java.util.Date;
import java.util.ArrayList;

import com.duggan.workflow.client.model.UploadContext;
import com.duggan.workflow.client.model.UploadContext.UPLOADACTION;
import com.duggan.workflow.client.ui.AppManager;
import com.duggan.workflow.client.ui.component.DropDownList;
import com.duggan.workflow.client.ui.component.IssuesPanel;
import com.duggan.workflow.client.ui.upload.custom.Uploader;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.DocumentType;
import com.duggan.workflow.shared.model.Priority;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.web.bindery.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.PopupViewImpl;

public class CreateDocView extends PopupViewImpl implements
		CreateDocPresenter.ICreateDocView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, CreateDocView> {
	}
	@UiField
	DialogBox popupView;
	
	@UiField
	HasClickHandlers btnSave;
	@UiField
	HasClickHandlers btnApproval;
	@UiField
	HasClickHandlers btnCancel;

	@UiField
	DropDownList<DocumentType> lstDocumentType;
	
	@UiField
	TextBox txtSubject;
	@UiField
	TextArea txtDescription;

	@UiField
	CheckBox chkNormal;
	@UiField
	CheckBox chkHigh;
	@UiField
	CheckBox chkCritical;

	@UiField
	DateBox dtDocDate;
	@UiField
	TextBox txtPartner;
	@UiField
	TextBox txtValue;

	@UiField
	IssuesPanel issues;

	@UiField
	HTMLPanel uploadPanel;

	@UiField Uploader uploader;
	
	@Inject
	public CreateDocView(final EventBus eventBus, final Binder binder) {
		super(eventBus);
		widget = binder.createAndBindUi(this);
		dtDocDate.setFormat(new DateBox.DefaultFormat(DATEFORMAT));
		ValueChangeHandler<Boolean> changeHandler = new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {

				boolean v = event.getValue();

				// if(v){
				chkNormal.setValue(event.getSource().equals(chkNormal) ? v
						: false);
				chkHigh.setValue(event.getSource().equals(chkHigh) ? v : false);
				chkCritical.setValue(event.getSource().equals(chkCritical) ? v
						: false);
				// }
			}
		};

		chkCritical.addValueChangeHandler(changeHandler);
		chkHigh.addValueChangeHandler(changeHandler);
		chkNormal.addValueChangeHandler(changeHandler);
		
		int[] position=AppManager.calculatePosition(5, 50);
		popupView.setPopupPosition(position[1],position[0]);
		
		UIObject.setVisible(uploadPanel.getElement(), false);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void addToSlot(Object slot, IsWidget content) {

		if (slot == UPLOAD_SLOT) {
			uploadPanel.clear();

			if (content != null) {
				uploadPanel.add(content);
			}
		} else {
			super.addToSlot(slot, content);
		}

	}

	@Override
	public HasClickHandlers getSave() {

		return btnSave;
	}

	@Override
	public HasClickHandlers getCancel() {

		return btnCancel;
	}

	@Override
	public HasClickHandlers getForward() {

		return btnApproval;
	}

	@Override
	public Document getDocument() {
		Document doc = new Document();
		doc.setDescription(txtDescription.getValue());
		doc.setDocumentDate(new Date());
		doc.setId(null);
		doc.setCaseNo(txtSubject.getText());
		doc.setDocumentDate(dtDocDate.getValue());
		doc.setPartner(txtPartner.getValue());
		doc.setValue(txtValue.getValue());
		doc.setPriority(getPriority().ordinal());
		doc.setType(lstDocumentType.getValue());

		return doc;
	}

	@Override
	public boolean isValid() {
		// txtDescription.getValue();

		boolean isValid = true;

		issues.clear();

		if (lstDocumentType.getValue() == null) {
			issues.addError("Please select a document type");
			isValid = false;
		}

		if (isNullOrEmpty(txtSubject.getValue())) {
			issues.addError("Subject field is mandatory");
			isValid = false;
		}

		if (isNullOrEmpty(txtDescription.getValue())) {
			issues.addError("Description field is mandatory");
			isValid = false;
		}

		return isValid;
	}

	boolean isNullOrEmpty(String value) {
		return value == null || value.trim().length() == 0;
	}

	@Override
	public void setValues(DocumentType docType, String subject, Date docDate,
			String partner, String value, String description, Priority priority, String docRefId) {

		if (docType != null)
			setDocumentType(docType);

		if (subject != null)
			txtSubject.setValue(subject);

		if (docDate != null)
			dtDocDate.setValue(docDate);

		if (partner != null)
			txtPartner.setValue(partner);

		if (value != null)
			txtValue.setValue(value);

		if (description != null)
			txtDescription.setValue(description);

		if (priority == null) {
			priority = Priority.NORMAL;
		}

		setPriority(priority);
		
		if(docRefId!=null){
			//will be enabled in edit mode
			//UIObject.setVisible(uploadPanel.getElement(), true);
			//set upload context here
			UploadContext context = new UploadContext();
			context.setContext("docRefId", docRefId+"");
			context.setAction(UPLOADACTION.ATTACHDOCUMENT);
			uploader.setContext(context);
		}

	}

	private void setPriority(Priority priority) {
		switch (priority) {
		case CRITICAL:
			chkCritical.setValue(true);
			break;
		case HIGH:
			chkHigh.setValue(true);
			break;
		case NORMAL:
			chkNormal.setValue(true);
			break;
		}
	}

	public Priority getPriority() {
		Priority priority = Priority.NORMAL;

		if (chkCritical.getValue()) {
			priority = Priority.CRITICAL;
		}

		if (chkHigh.getValue()) {
			priority = Priority.HIGH;
		}

		return priority;
	}

	private void setDocumentType(DocumentType docType) {
		lstDocumentType.setValue(docType);
	}

	@Override
	public void setDocTypes(ArrayList<DocumentType> types) {
		lstDocumentType.setItems(types);
	}

}
