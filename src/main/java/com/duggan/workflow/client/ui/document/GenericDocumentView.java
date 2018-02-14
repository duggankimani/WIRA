package com.duggan.workflow.client.ui.document;

import static com.duggan.workflow.client.ui.document.GenericDocumentPresenter.ACTIVITY_SLOT;
import static com.duggan.workflow.client.ui.document.GenericDocumentPresenter.ATTACHMENTS_SLOT;
import static com.duggan.workflow.client.ui.util.DateUtils.DATEFORMAT;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.duggan.workflow.client.model.ScreenMode;
import com.duggan.workflow.client.ui.component.ActionLink;
import com.duggan.workflow.client.ui.component.AttachmentItem;
import com.duggan.workflow.client.ui.component.BulletListPanel;
import com.duggan.workflow.client.ui.component.BulletPanel;
import com.duggan.workflow.client.ui.component.CommentBox;
import com.duggan.workflow.client.ui.component.TableView;
import com.duggan.workflow.client.ui.document.form.FormPanel;
import com.duggan.workflow.client.ui.events.ScreenModeChangeEvent;
import com.duggan.workflow.client.ui.upload.custom.Uploader;
import com.duggan.workflow.client.ui.util.DateUtils;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.Actions;
import com.duggan.workflow.shared.model.Attachment;
import com.duggan.workflow.shared.model.Delegate;
import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.model.DocStatus;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.HTStatus;
import com.duggan.workflow.shared.model.HTSummary;
import com.duggan.workflow.shared.model.Priority;
import com.duggan.workflow.shared.model.TaskLog;
import com.duggan.workflow.shared.model.TaskStepDTO;
import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.model.form.Form;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.wira.commons.client.security.CurrentUser;
import com.wira.commons.client.util.ArrayUtil;
import com.wira.commons.shared.models.HTUser;
import com.wira.commons.shared.models.PermissionName;

public class GenericDocumentView extends ViewImpl implements GenericDocumentPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, GenericDocumentView> {
	}

	@UiField
	HTMLPanel panelUpperContent;

	@UiField
	HTMLPanel contentArea;

	@UiField
	SpanElement spnDocType;
	@UiField
	SpanElement spnSubject;

	@UiField
	Element elAttachmentsCount;

	@UiField
	SpanElement spnDate;

	@UiField
	SpanElement spnValue;
	@UiField
	SpanElement spnPartner;
	@UiField
	SpanElement spnDescription;

	@UiField
	DivElement divAssignee;

	@UiField
	Image img;

	@UiField
	Anchor aAdminOverview;
	@UiField
	Anchor aDoc;
	@UiField
	Anchor aAudit;
	@UiField
	Anchor aSimulate;
	@UiField
	Anchor aEdit;
	@UiField
	Anchor aSave;
	@UiField
	Anchor aDelete;
	@UiField
	Anchor aClaim;
	@UiField
	Anchor aStart;
	@UiField
	Anchor aSuspend;
	@UiField
	Anchor aResume;
	@UiField
	Anchor aComplete;
	@UiField
	Anchor aDelegate;
	@UiField
	Anchor aAssign;
	@UiField
	Anchor aRevoke;
	@UiField
	Anchor aStop;
	@UiField
	Anchor aForward;
	@UiField
	Anchor aProcess;
	@UiField
	Anchor aApprove;
	@UiField
	Anchor aReject;
	@UiField
	Anchor aClose;
	@UiField
	Anchor aPrint;
	@UiField
	HTMLPanel statusContainer;
	@UiField
	HTMLPanel auditContainer;
	@UiField
	HTMLPanel divProcess;
	@UiField
	HTMLPanel divAuditLog;
	@UiField
	Element eOwner;
	@UiField
	Element eTitle;
	// @UiField Element eDelegate;
	@UiField
	HTMLPanel spnPriority;
	@UiField
	SpanElement spnAttachmentNo;
	@UiField
	SpanElement spnActivityNo;
	@UiField
	DivElement divAttachment;
	@UiField
	HTMLPanel panelActivity;
	@UiField
	Uploader uploader;
	@UiField
	HTMLPanel panelAttachments;
	@UiField
	HTMLPanel divActivity;
	// @UiField Anchor aAttach1;
	@UiField
	Anchor aAttach2;
	@UiField
	Image imgProcess;
	@UiField
	ActionLink aContinue;
	@UiField
	HTMLPanel divContinue;
	@UiField
	CommentBox commentPanel;

	@UiField
	DivElement btnGroup;
	@UiField
	DivElement divDate;
	@UiField
	DivElement divDesc;
	@UiField
	DivElement divPartner;
	@UiField
	DivElement divValue;
	@UiField
	DivElement divContent;

	@UiField
	HTMLPanel divProcessOverview;
	@UiField
	HTMLPanel overviewContainer;

	@UiField
	HTMLPanel fldForm;

	@UiField
	Element iDocStatus;

	@UiField
	BulletListPanel bulletListPanel;

	BulletPanel liPrevious;
	ActionLink aPrevious;

	BulletPanel liNext;
	ActionLink aNext;

	@UiField
	SpanElement spnTimeTaken;

	@UiField
	Element divRibbon;
	@UiField
	SpanElement spnRibbon;
	FormPanel formPanel;

	@UiField
	FlexTable tblAuditLog;
	@UiField
	SpanElement spnAuditEmpty;

	// Attachments View
	@UiField
	Anchor aViewAttachments;
	@UiField
	Element spnAttachmentsEmpty;
	@UiField
	TableView tblAttachments;
	@UiField
	HTMLPanel divAttachmentPanel;

	@UiField
	Anchor aConfigure;

	String url = null;

	ArrayList<Actions> validActions = null;
	boolean isBizProcessDisplayed = true;
	boolean isEditMode = true;
	boolean overrideDefaultComplete = false;
	boolean overrideDefaultStart = false;

	@UiField
	ActionLink aEnv;
	private Date dateDue;
	private Date dateCreated;
	DocStatus status = null;
	private boolean isUnassignedList = false;

	@UiField
	Anchor aMaximize;

	VIEWS selected = VIEWS.FORM;
	private boolean isLoadAsAdmin;
	private String processRefId;
	private String caseViewDocRefId = null; //Set by CasePresenter for Process Audit Views //Duggan 22/08/2017

	@Inject
	public GenericDocumentView(final Binder binder, CurrentUser user) {
		widget = binder.createAndBindUi(this);
		createNavigationButtons();

		aClaim.getElement().setId("aClaim_Link");
		aStart.getElement().setId("aStart_Link");
		aSuspend.getElement().setId("aSuspend_Link");
		aResume.getElement().setId("aResume_Link");
		aComplete.getElement().setId("aComplete_Link");
		aDelegate.getElement().setId("aDelegate_Link");
		aReject.getElement().setId("aReject_Link");
		aRevoke.getElement().setId("aRevoke_Link");
		aStop.getElement().setId("aStop_Link");
		aForward.getElement().setId("aForward_Link");
		aApprove.getElement().setId("aApprove_Link");
		aAssign.getElement().setId("aAssign_Link");
		aEdit.getElement().setId("aEdit_Link");
		aSave.getElement().setId("aSave_Link");
		aProcess.getElement().setId("aProcess_Link");
		aDoc.getElement().setId("aDoc_Link");
		aAudit.getElement().setId("aAudit_Link");

		// UIObject.setVisible(aEdit.getElement(), false);
		UIObject.setVisible(aConfigure.getElement(),
				user.hasPermissions(PermissionName.PROCESSES_CAN_EDIT_PROCESSES.name()));

		aEdit.getElement().setAttribute("type", "button");
		aEdit.getElement().setAttribute("data-toggle", "tooltip");
		aProcess.getElement().setAttribute("data-toggle", "button");
		aSimulate.getElement().setAttribute("type", "button");
		UIObject.setVisible(aForward.getElement(), false);
		aApprove.getElement().setAttribute("type", "button");
		aReject.getElement().setAttribute("type", "button");
		aForward.getElement().setAttribute("type", "button");
		panelActivity.getElement().setAttribute("id", "panelactivity");
		aForward.getElement().setAttribute("alt", "Forward for Approval");
		imgProcess.setVisible(false);

		tblAttachments.setHeaders(ArrayUtil.asList("File Name", "Created By", "Date Created"));

		// Register Phone events
		registerPhoneEvents(panelUpperContent.getElementById("phoneActions"));

		img.addErrorHandler(new ErrorHandler() {

			@Override
			public void onError(ErrorEvent event) {
				img.setUrl("img/blueman.png");
			}
		});

		img.addDoubleClickHandler(new DoubleClickHandler() {

			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				Window.open(img.getUrl(), "_blank", null);
			}
		});

		showDefaultFields(false);
		disableAll();// Hide all buttons

		imgProcess.removeStyleName("gwt-Anchor");

		// aShowProcess.addClickHandler(new ClickHandler() {
		// @Override
		// public void onClick(ClickEvent event) {
		// if(url!=null)
		// Window.open(url, "Process HashMap", null);
		// }
		// });

		aMaximize.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				toggleMaximize(aMaximize.getElement());
			}
		});

		imgProcess.addErrorHandler(new ErrorHandler() {

			@Override
			public void onError(ErrorEvent event) {
				statusContainer.add(new InlineLabel(
						"We Could Not Load the Process Map. (Hint, confirm that the process is running)"));
			}
		});

		aEdit.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				setEditMode(true);
				changeView(VIEWS.FORM);
			}
		});

		aAdminOverview.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				changeView(VIEWS.ADMINOVERVIEW);
			}
		});

		aProcess.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				changeView(VIEWS.PROCESSTREE);
			}
		});

		aViewAttachments.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				changeView(VIEWS.ATTACHMENTS);
			}
		});

		aSave.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (isValid()) {
					setEditMode(false);
				}
			}
		});

		aAudit.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				changeView(VIEWS.AUDITLOG);
			}
		});

		aDoc.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				changeView(VIEWS.FORM);
			}
		});

		changeView(VIEWS.FORM);

		initZoom(panelUpperContent.getElementById("divZoom"), contentArea.getElement());

		aPrint.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// print(formPanel.getElement());
			}
		});
	}

	protected native void print(Element element) /*-{
													//Get the HTML of div
													var divElements = $wnd.jQuery(element).html();
													//Get the HTML of whole page
													var oldPage = $doc.body.innerHTML;
													
													//Reset the page's HTML with div's HTML only
													$doc.body.innerHTML = 
													"<html><head><title></title></head><body>" + 
													divElements + "</body>";
													
													//Print Page
													$wnd.print();
													
													//Restore orignal HTML
													$doc.body.innerHTML = oldPage;
													}-*/;

	private native void initZoom(Element element, Element contentArea) /*-{
																		$wnd.jQuery($doc).ready(function(){
																		var dropDownText = $wnd.jQuery(element).find('span.dropDownText').get(0);
																		
																		$wnd.jQuery(element).find('a').click(function(){
																		var txt = $wnd.jQuery(this).text();
																		$wnd.jQuery(dropDownText).text(txt);
																		
																		if(txt.endsWith('%')){
																		var amount = txt.substring(0,txt.length-1);
																		var scale = amount/100;
																		
																		contentArea.style.zoom=scale;
																		}
																		});
																		});
																		}-*/;

	protected native void toggleMaximize(Element element) /*-{
															var icon = $wnd.jQuery(element).find('i').get(0);
															$wnd.jQuery(icon).toggleClass('icon-resize-small');
															$wnd.jQuery(icon).toggleClass('icon-resize-full');
															
															if($wnd.jQuery(icon).hasClass('icon-resize-full')){
															this.@com.duggan.workflow.client.ui.document.GenericDocumentView::showFullScreen(I)(0);
															}else{
															this.@com.duggan.workflow.client.ui.document.GenericDocumentView::showFullScreen(I)(1);
															}
															}-*/;

	void showFullScreen(int isFullScreen) {
		if (isFullScreen == 1) {
			// true
			AppContext.getEventBus().fireEvent(new ScreenModeChangeEvent(ScreenMode.FULLSCREEN));
		} else {
			// false
			AppContext.getEventBus().fireEvent(new ScreenModeChangeEvent(ScreenMode.SMALLSCREEN));
		}
	}

	public void createNavigationButtons() {
		// Previous
		liPrevious = new BulletPanel();
		liPrevious.addStyleName("disabled");
		aPrevious = new ActionLink();
		aPrevious.getElement().setInnerSafeHtml(
				new SafeHtmlBuilder().appendHtmlConstant("<i class=\"icon-double-angle-left\"></i>").toSafeHtml());
		liPrevious.add(aPrevious);
		// Added on setTaskSteps/ Form Steps

		liNext = new BulletPanel();
		liNext.addStyleName("disabled");
		aNext = new ActionLink();
		aNext.getElement().setInnerSafeHtml(
				new SafeHtmlBuilder().appendHtmlConstant("<i class=\"icon-double-angle-right\"></i>").toSafeHtml());
		liNext.add(aNext);
		// Added after all steps have been
	}

	public BulletPanel generateStep(String stepName, String stepTitle, String styleName) {
		BulletPanel liStep = new BulletPanel();
		if (styleName != null && !styleName.isEmpty())
			liStep.addStyleName(styleName);

		ActionLink aStep = new ActionLink(stepName);
		aStep.setTitle(stepTitle);
		liStep.add(aStep);

		return liStep;
	}

	public void setEditMode(boolean isEditMode) {
		formPanel.setReadOnly(!isEditMode);
		// --UIObject.setVisible(btnGroup, !isEditMode);
		UIObject.setVisible(aForward.getElement(), !isEditMode);
		showLi("aForward", !isEditMode);
		// UIObject.setVisible(aEdit.getElement(), !isEditMode);
		// UIObject.setVisible(aSave.getElement(), isEditMode);
		showNavigation(isEditMode);
	}

	public void showNavigation(boolean isEditMode) {
		if (!isEditMode) {
			// divActivity.addStyleName("hide");
			divContinue.addStyleName("hide");
		} else {
			// divActivity.removeStyleName("hide");
			divContinue.removeStyleName("hide");
		}
	}

	public void changeView(VIEWS itemToShow) {
		divProcess.addStyleName("hide");
		divContent.addClassName("hide");
		divAuditLog.addStyleName("hide");
		divContent.addClassName("hide");
		divAttachmentPanel.addStyleName("hide");
		// divProcessOverview.addStyleName("hide");

		// aAdminOverview.removeStyleName("disabled");
		aProcess.removeStyleName("disabled");
		aAudit.removeStyleName("disabled");
		aViewAttachments.removeStyleName("disabled");
		aDoc.removeStyleName("disabled");

		if (itemToShow == selected) {
			itemToShow = VIEWS.FORM;
		}

		switch (itemToShow) {
		case ADMINOVERVIEW:
			// aAdminOverview.addStyleName("disabled");
			// divProcessOverview.removeStyleName("hide");
			break;
		case FORM:
			aDoc.addStyleName("disabled");
			divContent.removeClassName("hide");
			break;
		case PROCESSTREE:
			divProcess.removeStyleName("hide");
			aProcess.addStyleName("disabled");
			break;
		case AUDITLOG:
			divAuditLog.removeStyleName("hide");
			aAudit.addStyleName("disabled");
			break;
		case ATTACHMENTS:
			divAttachmentPanel.removeStyleName("hide");
			aViewAttachments.addStyleName("disabled");
		}

	}

	private void disableAll() {
		show(aClaim, false);
		show(aStart, false);
		show(aSuspend, false);
		show(aResume, false);
		show(aComplete, false);
		show(aDelegate, false);
		show(aReject, false);
		show(aRevoke, false);
		show(aStop, false);
		show(aForward, false);
		show(aApprove, false);
		show(aAssign, false);

		showLi("aClaim", false);
		showLi("aStart", false);
		showLi("aSuspend", false);
		showLi("aResume", false);
		showLi("aComplete", false);
		showLi("aDelegate", false);
		showLi("aReject", false);
		showLi("aRevoke", false);
		showLi("aStop", false);
		showLi("aForward", false);
		showLi("aApprove", false);
		showLi("aAssign", false);

	}

	public native void registerPhoneEvents(Element phoneActions)/*-{
																var view = this;
																$wnd.jQuery().ready(function(){
																$wnd.jQuery(phoneActions).find('.dropdown-menu a').click(function(){
																var id= $wnd.jQuery(this).prop('id');
																view.@com.duggan.workflow.client.ui.document.GenericDocumentView::click(Ljava/lang/String;)(id);
																});
																});
																
																}-*/;

	public void click(String id) {
		Anchor target = null;
		switch (id) {
		case "aClaim":
			target = aClaim;
			break;
		case "aStart":
			target = aStart;
			break;
		case "aSuspend":
			target = aSuspend;
			break;
		case "aResume":
			target = aResume;
			break;
		case "aDelegate":
			target = aDelegate;
			break;
		case "aReject":
			target = aReject;
			break;
		case "aRevoke":
			target = aRevoke;
			break;
		case "aStop":
			target = aStop;
			break;
		case "aForward":
			target = aForward;
			break;
		case "aAssign":
			target = aAssign;
			break;
		case "aEdit":
			target = aEdit;
			break;
		case "aSave":
			target = aSave;
			break;
		case "aProcess":
			target = aProcess;
			break;
		case "aDoc":
			target = aDoc;
			break;
		case "aAudit":
			target = aAudit;
			break;
		case "aViewAttachments":
			target = aViewAttachments;
			break;
		case "aClose":
			target = aClose;
			break;

		}
		if (target == null) {
			return;
		}
		DomEvent.fireNativeEvent(
				com.google.gwt.dom.client.Document.get().createClickEvent(0, 0, 0, 0, 0, false, false, false, false),
				target);
	}

	private void showLi(String elementId, boolean isShow) {
		if (elementId == null) {
			return;
		}

		Element el = panelUpperContent.getElementById(elementId);
		if (el == null) {
			// not found
			return;
		}

		Element target = el;
		if (el != null) {
			Element parentEl = el.getParentElement();
			if (parentEl.hasTagName("li")) {
				target = parentEl;
			}
		}

		if (isShow) {
			target.removeClassName("hidden");
		} else {
			target.addClassName("hidden");
		}
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	public void setForm(Form form, Doc doc, boolean isFormReadOnly) {
		fldForm.clear();
		if (form == null || form.getFields() == null)
			return;

		// hide static fields
		showDefaultFields(false);
		
		boolean readOnly = true;
		if (formPanel != null) {
			readOnly = formPanel.isReadOnly();
		}

		formPanel = new FormPanel(form, doc);

		// Configure
		if (processRefId != null) {
			aConfigure.setHref("#/formbuilder?p=" + processRefId + "&formRefId=" + form.getRefId());
			aConfigure.setTarget("_blank");
		}

		boolean isDraft = false;
		if (doc instanceof Document) {
			isDraft = ((Document) doc).getStatus() == DocStatus.DRAFTED;
		}

		fldForm.removeStyleName("form-editable");
		fldForm.removeStyleName("form-uneditable");
		iDocStatus.removeAttribute("class");

		if (validActions != null && !validActions.isEmpty() && !isDraft) {
			if (validActions.contains(Actions.COMPLETE)) {
				formPanel.setReadOnly(false || isFormReadOnly);
				showNavigation(true);

				// Running or started
				divRibbon.addClassName("ribbon-open");
				spnRibbon.setInnerText("In Progress");
				iDocStatus.setInnerText("In Progress");
				iDocStatus.addClassName("doc-pending icon-edit");
				fldForm.addStyleName("form-editable");
			} else {

				if (validActions.contains(Actions.START) || validActions.contains(Actions.RESUME)) {
					// Not Started
					divRibbon.addClassName("ribbon-open");
					spnRibbon.setInnerText("New");
					iDocStatus.setInnerText("New");
					iDocStatus.addClassName("doc-pending icon-double-angle-right");
					fldForm.addStyleName("form-uneditable");
				} else if(doc.getProcessStatus()==HTStatus.COMPLETED){
					divRibbon.addClassName("ribbon-accepted");
					spnRibbon.setInnerText("Completed");
					iDocStatus.setInnerText("Completed");
					iDocStatus.addClassName("icon-ok-sign doc-success");
					fldForm.addStyleName("form-uneditable");
				}else {
					divRibbon.addClassName("ribbon-error");
					spnRibbon.setInnerText("Exited");
					iDocStatus.setInnerText("Exited");
					iDocStatus.addClassName("doc-exited");
					fldForm.addStyleName("form-uneditable");
				}
				formPanel.setReadOnly(true);
				showNavigation(false);

			}

		} else {
			if (status != null) {
				if (status == DocStatus.INPROGRESS) {
					divRibbon.addClassName("ribbon-accepted");
					spnRibbon.setInnerText("Completed");
					iDocStatus.setInnerText("Completed");
					iDocStatus.addClassName("icon-ok-sign doc-success");
					fldForm.addStyleName("form-uneditable");
				} else {
					divRibbon.addClassName("ribbon-draft");
					spnRibbon.setInnerText("Draft");
					iDocStatus.setInnerText("Draft");
					iDocStatus.addClassName("doc-waiting icon-edit-sign");
					fldForm.addStyleName("form-editable");
				}

			}
			formPanel.setReadOnly(readOnly || isFormReadOnly);
		}

		formPanel.setCreated(dateCreated);
		formPanel.setDeadline(dateDue);

		if (doc instanceof HTSummary) {
			HTSummary task = (HTSummary) doc;
			formPanel.setCompletedOn(task.getCompletedOn());
			setAssignee(task.getTaskActualOwner() == null ? null : task.getTaskActualOwner().getFullName());
			show(aEdit, false);
			show(aSave, false);
		} else {
			Document document = (Document) doc;
			show(aEdit, document.getStatus() == DocStatus.DRAFTED);
			show(aSave, document.getStatus() == DocStatus.DRAFTED);
			if (document.getDateSubmitted() != null) {
				formPanel.setCreated(document.getDateSubmitted());
			}
		}

		fldForm.add(formPanel);

		// Force Scroll To Top of Form
		img.getElement().scrollIntoView();
	}

	private void setAssignee(String assigneeFullName) {
		if (assigneeFullName != null && !assigneeFullName.isEmpty()) {
			divAssignee.setInnerText("Assignee: " + assigneeFullName);
		}
	}

	public void showDefaultFields(boolean show) {
		UIObject.setVisible(divDate, show);
		UIObject.setVisible(divDesc, show);
		UIObject.setVisible(divPartner, show);
		UIObject.setVisible(divValue, show);
	}

	public void setValues(HTUser createdBy, Date created, String type, String subject, Date docDate, String value,
			String partner, String description, Integer priority, DocStatus status, String docRefId,
			String taskDisplayName) {
		disableAll();
		if (createdBy != null) {
			if (createdBy.getSurname() != null)
				eOwner.setInnerText(createdBy.getSurname());
			else
				eOwner.setInnerText(createdBy.getUserId());

			setImage(createdBy);
		}

		this.dateCreated = created;
		this.status = status;

		if (!(taskDisplayName == null || taskDisplayName.equals(""))) {
			spnDocType.setInnerText(taskDisplayName);
		} else if (type != null) {
			spnDocType.setInnerText(type);
		}

		if (subject != null) {
			spnSubject.setInnerText(subject);
		}

		if (docDate != null) {
			spnDate.setInnerText(DATEFORMAT.format(docDate));
		}

		if (value != null) {
			spnValue.setInnerText(value);
			// UIObject.setVisible(divValue, true);
		}

		if (partner != null) {
			// UIObject.setVisible(divPartner, true);
			spnPartner.setInnerText(partner);
		}

		if (description != null)
			spnDescription.setInnerText(description);

		if (status == DocStatus.DRAFTED) {
			showForward(true);
		} else {
			showForward(false);
		}

		if (priority != null) {
			Priority prty = Priority.get(priority);

			switch (prty) {
			case CRITICAL:
				spnPriority.addStyleName("label-important");
				// spnPriority.setInnerText("Urgent");
				break;

			case HIGH:
				spnPriority.addStyleName("label-warning"); //
				// spnPriority.setInnerText("Important");
				break;

			default:
				spnPriority.addStyleName("hide");
				break;
			}
		}

		this.url = null;
		if (status == DocStatus.DRAFTED) {
			setProcessForDoc(docRefId);
		}
	}

	public void setValidTaskActions(ArrayList<Actions> actions) {
		this.validActions = actions;
		if (isLoadAsAdmin) {
			disableAll();
			
			if(!actions.isEmpty()) {
				//There must be valid actions
				show(aAssign, caseViewDocRefId!=null && AppContext.isCurrentUserAdmin());
				show(aStop, caseViewDocRefId!=null && AppContext.isCurrentUserAdmin());
			}
			return;
		}

		if (actions != null)
			for (Actions action : actions) {
				Anchor target = null;
				switch (action) {
				case CLAIM:
					target = aClaim;
					break;
				case COMPLETE:
					// target=aComplete;
					if (!overrideDefaultComplete) {
						// show(aApprove);
						// show(aReject);
						show(aContinue);
					}
					break;
				case DELEGATE:
					target = aDelegate;
					
					break;
				case FORWARD:
					if (!overrideDefaultStart) {
						target = aForward;
					}
					break;
				case RESUME:
					target = aResume;
					break;
				case REVOKE:
					target = aRevoke;
					break;
				case START:
					target = aStart;
					break;
				case STOP:
					target = aStop;
					break;
				case SUSPEND:
					target = aSuspend;
					break;
				}

				if (target != null) {
					show(target);
				}
			}
	}

	@Override
	public void showForward(boolean show) {
		show(aForward, show);
	}

	@Override
	public void show(boolean IsShowapprovalLink, boolean IsShowRejectLink) {

		show(aApprove, IsShowapprovalLink);
		show(aReject, IsShowRejectLink);
	}

	public HasClickHandlers getSimulationBtn() {
		return aSimulate;
	}

	public HasClickHandlers getSaveButton() {
		return aSave;
	}

	public HasClickHandlers getDeleteButton() {
		return aDelete;
	}

	@Override
	public void showEdit(boolean displayed) {
		// UIObject.setVisible(aEdit.getElement(), displayed);
		UIObject.setVisible(aDelete.getElement(), displayed);
	}

	// @Override
	// public void setStates(ArrayList<NodeDetail> states) {
	// statusContainer.clear();
	// if(states!=null){
	// NodeDetail detail = null;
	// for(NodeDetail state:states){
	// if(state.isEndNode())
	// detail = state;
	// else
	// statusContainer.add(new ProcessState(state));
	//
	// }
	//
	// //ensure end node always comes last
	// if(detail!=null){
	// statusContainer.add(new ProcessState(detail));
	// }
	// }
	// }

	public void show(Anchor target) {
		show(target, true);
	}

	public void show(Anchor target, boolean isShow) {
		isShow = isShow && (!isUnassignedList || (target.equals(aAssign)));

		if (isShow) {
			target.removeStyleName("hidden");
		}
		showLi(getRelatedId(target), isShow);
		UIObject.setVisible(target.getElement(), isShow);

	}

	private String getRelatedId(Anchor target) {

		if (target == aClaim) {
			return "aClaim";
		} else if (target == aStart) {
			return "aStart";
		} else if (target == aSuspend) {
			return "aSuspend";
		} else if (target == aResume) {
			return "aResume";
		} else if (target == aComplete) {
			return "aComplete";
		} else if (target == aDelegate) {
			return "aDelegate";
		} else if (target == aReject) {
			return "aReject";
		} else if (target == aRevoke) {
			return "aRevoke";
		} else if (target == aStop) {
			return "aStop";
		} else if (target == aForward) {
			return "aForward";
		} else if (target == aApprove) {
			return "aApprove";
		} else if (target == aAssign) {
			return "aAssign";
		} else if (target == aDoc) {
			return "aDoc";
		} else if (target == aAudit) {
			return "aAudit";
		} else if (target == aProcess) {
			return "aProcess";
		} else if (target == aEdit) {
			return "aEdit";
		} else if (target == aSave) {
			return "aSave";
		} else if (target == aViewAttachments) {
			return "aViewAttachments";
		} else if (target == aClose) {
			return "aClose";
		}else if(target == aStop) {
			return "aStop";
		}
		return null;
	}

	public HasClickHandlers getClaimLink() {
		return aClaim;
	}

	public HasClickHandlers getStartLink() {
		return aStart;
	}

	public HasClickHandlers getSuspendLink() {
		return aSuspend;
	}

	public HasClickHandlers getResumeLink() {
		return aResume;
	}

	public HasClickHandlers getCompleteLink() {
		return aComplete;
	}

	public HasClickHandlers getDelegateLink() {
		return aDelegate;
	}

	public HasClickHandlers getRevokeLink() {
		return aRevoke;
	}

	public HasClickHandlers getStopLink() {
		return aStop;
	}

	public HasClickHandlers getForwardForApproval() {
		return aForward;
	}

	public HasClickHandlers getApproveLink() {
		return aApprove;
	}

	public HasClickHandlers getRejectLink() {
		return aReject;
	}

	public Anchor getSaveCommentButton() {
		return commentPanel.getaSaveComment();
	}

	@Override
	public void setInSlot(Object slot, IsWidget content) {

		if (slot == ACTIVITY_SLOT) {
			panelActivity.clear();
			if (content != null) {
				panelActivity.add(content);
			}
		}
		if (slot == ATTACHMENTS_SLOT) {
			panelAttachments.clear();
			if (content != null) {
				panelAttachments.add(content);
			}
		} else {
			super.setInSlot(slot, content);
		}
	}

	@Override
	public void addToSlot(Object slot, IsWidget content) {

		if (slot == ACTIVITY_SLOT) {

			if (content != null) {
				panelActivity.add(content);
			}
		}
		if (slot == ATTACHMENTS_SLOT) {
			if (content != null) {
				panelAttachments.add(content);
			}
		} else {
			super.addToSlot(slot, content);
		}

	}

	@Override
	public Uploader getUploader() {
		return uploader;
	}

	@Override
	public String getComment() {

		return commentPanel.getCommentBox().getValue();
	}

	@Override
	public void setComment(String string) {
		commentPanel.getCommentBox().setText("");
	}

	public HasClickHandlers getUploadLink2() {
		return aAttach2;
	}

	public SpanElement getSpnAttachmentNo() {
		return spnAttachmentNo;
	}

	public SpanElement getSpnActivityNo() {
		return spnActivityNo;
	}

	public DivElement getDivAttachment() {
		return divAttachment;
	}

	@Override
	public boolean isValid() {
		if (formPanel == null) {
			return true;
		}
		return formPanel.isValid();
	}

	@Override
	public HashMap<String, Value> getValues() {
		if (formPanel == null) {
			return null;
		}

		return formPanel.getValues();
	}

	@Override
	public void setDelegate(Delegate delegate) {
		delegate.getCreated();
		delegate.getDelegateTo();
		delegate.getUserId();
		// eDelegate.setInnerText(delegate.getDelegateTo());
	}

	private void setImage(HTUser user) {
		String moduleUrl = GWT.getModuleBaseURL().replace("/gwtht", "");
		if (moduleUrl.endsWith("/")) {
			moduleUrl = moduleUrl.substring(0, moduleUrl.length() - 1);
		}
		moduleUrl = moduleUrl + "/getreport?ACTION=GetUser&userId=" + user.getUserId();
		img.setUrl(moduleUrl);
	}

	@Override
	public void overrideDefaultCompleteProcess() {
		overrideDefaultComplete = true;
		aApprove.addStyleName("hidden");
		aReject.addStyleName("hidden");
	}

	@Override
	public void overrideDefaultStartProcess() {
		overrideDefaultStart = true;
		aForward.addStyleName("hidden");
	}

	public HasClickHandlers getLinkNext() {
		return aNext;
	}

	public HasClickHandlers getLinkPrevious() {
		return aPrevious;
	}

	public Anchor getLinkContinue() {
		return aContinue;
	}

	@Override
	public HasClickHandlers getLinkEnv() {
		return aEnv;
	}

	@Override
	public void setDeadline(Date endDateDue) {
		this.dateDue = endDateDue;
		// formPanel.setDeadline(endDateDue);
	}

	@Override
	public void setSteps(ArrayList<TaskStepDTO> steps, int currentStep) {
		bulletListPanel.clear();
		if (steps == null || steps.isEmpty()) {
			return;
		}

		int size = steps.size();

		if (size == 1) {
			aContinue.setText("Submit");
			return; // No Next/Previous Buttons
		}

		aContinue.setText("Continue");

		liNext.removeStyleName("disabled");
		liPrevious.removeStyleName("disabled");

		bulletListPanel.add(liPrevious);
		for (TaskStepDTO dto : steps) {
			int idx = steps.indexOf(dto);
			String title = dto.getFormName() == null ? dto.getOutputDocName() : dto.getFormName();

			String styleName = null;
			if (idx == currentStep) {
				styleName = "active";
			} else {
				styleName = "disabled";
			}
			bulletListPanel.add(generateStep((idx + 1) + "", title, styleName));
		}

		bulletListPanel.add(liNext);

		if ((size - 1) == currentStep) {
			// last step
			aContinue.setText("Submit");
			liNext.setStyleName("disabled");
		} else if (currentStep == 0) {
			// first step
			liPrevious.setStyleName("disabled");
		}
	}

	public void showAssignLink(boolean show) {
		show(aAssign, show);
	}

	public HasClickHandlers getAssignLink() {
		return aAssign;
	}

	@Override
	public void setUnAssignedList(boolean isUnassignedList) {
		this.isUnassignedList = isUnassignedList;
		show(aAssign, isUnassignedList);
		show(aAttach2, !isUnassignedList);
	}

	@Override
	public void setProcessUrl(Long processInstanceId) {
		if (processInstanceId != null) {
			String root = GWT.getModuleBaseURL();
			root = root.replaceAll("/gwtht", "");
			this.url = root + "getreport?pid=" + processInstanceId + "&ACTION=PROCESSMAP&v="
					+ System.currentTimeMillis();
			imgProcess.setVisible(true);
			imgProcess.setUrl(this.url);
		}
	}

	public void setProcessForDoc(String docRefId) {
		if (docRefId != null) {
			String root = GWT.getModuleBaseURL();
			root = root.replaceAll("/gwtht", "");
			this.url = root + "getreport?docRefId=" + docRefId + "&ACTION=GETDOCUMENTPROCESS";
			imgProcess.setVisible(true);
			imgProcess.setUrl(this.url);
		}
	}

	@Override
	public HasClickHandlers getLinkViewProcessLog() {
		return aAudit;
	}

	@Override
	public void bindProcessLog(ArrayList<TaskLog> logs) {
		tblAuditLog.clear();
		
		if (logs != null && !logs.isEmpty()) {
			spnAuditEmpty.addClassName("hide");
			Date start = logs.get(0).getCreatedon();
			Date end = logs.get(logs.size() - 1).getCompletedon();
			end = end == null ? new Date() : end;
			String timeTaken = DateUtils.getTimeDifference(start, end);
			spnTimeTaken.setInnerText("(" + timeTaken + ")");

			if (!((TaskLog) logs.get(0)).isProcessLoaded()) {
				spnAuditEmpty.removeClassName("hide");
				spnAuditEmpty.setInnerText(
						"Issue404: Task Keys will be used in place of names. (Hint - Start the process to view tasks correctly)");
			}

			int row = 0;
			int col = 0;
			tblAuditLog.setWidget(row, col++, new InlineLabel("#"));
			tblAuditLog.setWidget(row, col++, new InlineLabel("Task Name"));
			tblAuditLog.setWidget(row, col++, new InlineLabel("Assignee"));
			tblAuditLog.setWidget(row, col++, new InlineLabel("Status"));
			tblAuditLog.setWidget(row, col++, new InlineLabel("Start Date"));
			tblAuditLog.setWidget(row, col++, new InlineLabel("Completion Date"));
			tblAuditLog.setWidget(row, col++, new InlineLabel("Time Taken"));
			
			if(caseViewDocRefId!=null) {
				tblAuditLog.setWidget(row, col++, new InlineLabel("Actions"));
			}
			

			++row;
			for (TaskLog log : logs) {

				InlineLabel label = new InlineLabel();
				HTStatus status = HTStatus.valueOf(log.getStatus().toUpperCase());
				String text = "";
				String styleName = "label label-success";

				switch (status) {
				case COMPLETED:
					text = "Completed";
					break;
				case CREATED:
					text = "In Progress";
					styleName = "label label-warning";
					break;
				case ERROR:
					text = "Error";
					styleName = "label label-danger";
					break;
				case EXITED:
					text = "Exited";
					styleName = "label label-danger";
					break;
				case FAILED:
					text = "Failed";
					styleName = "label label-danger";
					break;
				case INPROGRESS:
					text = "In Progress";
					styleName = "label label-warning";
					break;
				case OBSOLUTE:
					text = "Obsolute";
					styleName = "label label-danger";
					break;
				case READY:
					text = "In Progress";
					styleName = "label label-warning";
					break;
				case RESERVED:
					text = "In Progress";
					styleName = "label label-warning";
					break;
				case SUSPENDED:
					text = "Suspended";
					styleName = "label label-warning";
					break;
				}

				label.addStyleName(styleName);
				label.setText(text);

				col = 0;
				tblAuditLog.setWidget(row, col++, new InlineLabel(row+""));
				tblAuditLog.setWidget(row, col++, new InlineLabel(log.getTaskName()));
				tblAuditLog.setWidget(row, col++, new InlineLabel(
						log.getActualOwner() == null ? log.getPotOwner() : log.getActualOwner().toString()));
				tblAuditLog.setWidget(row, col++, label);
				tblAuditLog.setWidget(row, col++, new InlineLabel(
						log.getCreatedon() == null ? "" : DateUtils.DATEFORMAT.format(log.getCreatedon())));
				tblAuditLog.setWidget(row, col++, new InlineLabel(
						log.getCompletedon() == null ? "" : DateUtils.DATEFORMAT.format(log.getCompletedon())));
				tblAuditLog.setWidget(row, col++, new InlineLabel(DateUtils.getTimeDifference(log.getCreatedon(),
						log.getCompletedon() == null ? new Date() : log.getCompletedon())));
				
				if(caseViewDocRefId!=null) {
					Anchor anchor = new Anchor("View");
					String taskIdQ = (log.getTaskId()==null? "1=1&": "tid="+log.getTaskId());
					String processInstanceIdQ = log.getProcessinstanceid()==null? "": "pid="+log.getProcessinstanceid();
					String href = "#/caseview/"+caseViewDocRefId+"?"+taskIdQ+"&"+processInstanceIdQ;
					anchor.setHref(href);
					tblAuditLog.setWidget(row, col++, anchor);
				}
				
				++row;
			}

		} else {
			spnAuditEmpty.setInnerText("No Data To Display");
			spnAuditEmpty.removeClassName("hide");
			// nothing to display
		}

		auditContainer.add(tblAuditLog);
	}

	public void showAttachments(ArrayList<Attachment> attachments) {
		tblAttachments.clearRows();
		tblAttachments.addStyleName("file-attach");
		if (attachments != null) {
			for (Attachment attachment : attachments) {
				tblAttachments.addRow(ArrayUtil.asList("", "", ""), new AttachmentItem(attachment, true, false),
						new InlineLabel(
								attachment.getCreatedBy() == null ? "" : attachment.getCreatedBy().getFullName()),
						new InlineLabel(DateUtils.CREATEDFORMAT.format(attachment.getCreated())));
			}
		}
	}

	@Override
	public void setLoadAsAdmin(boolean isLoadAsAdmin) {
		this.isLoadAsAdmin = isLoadAsAdmin;

	}

	@Override
	public void enableSubmit(boolean isEnable) {
		aContinue.setEnabled(isEnable);
	}

	@Override
	public HasClickHandlers getCloseButton() {

		return aClose;
	}

	@Override
	public void forceExecJs() {
		formPanel.forceReloadJs();
	}

	@Override
	public void setAttachmentsCount(int size) {
		elAttachmentsCount.setInnerText("");
		if (size > 0) {
			elAttachmentsCount.setInnerText("" + size);
		}
	}

	@Override
	public void setProcessRefId(String processRefId) {
		this.processRefId = processRefId;
	}

	@Override
	public void setCaseViewDocRefId(String docRefId) {
		this.caseViewDocRefId = docRefId;
	}

}
