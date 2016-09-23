package com.duggan.workflow.client.ui.activityfeed;

import java.util.ArrayList;
import java.util.Date;

import com.duggan.workflow.client.event.CheckboxSelectionEvent;
import com.duggan.workflow.client.ui.AppManager;
import com.duggan.workflow.client.ui.activityfeed.components.CarouselPopup;
import com.duggan.workflow.client.ui.component.ActionLink;
import com.duggan.workflow.client.ui.component.Checkbox;
import com.duggan.workflow.client.ui.events.CloseCarouselEvent;
import com.duggan.workflow.client.ui.events.DocumentSelectionEvent;
import com.duggan.workflow.client.ui.util.DateUtils;
import com.duggan.workflow.client.ui.util.DocMode;
import com.duggan.workflow.client.ui.util.StringUtils;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.model.DocStatus;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.HTStatus;
import com.duggan.workflow.shared.model.HTSummary;
import com.duggan.workflow.shared.model.Priority;
import com.duggan.workflow.shared.model.ProcessDef;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.wira.commons.client.util.Definitions;

public class ActivitiesView extends ViewImpl implements
		ActivitiesPresenter.MyView {

	private final Widget widget;

	private Timer timer;
	final int timerSeconds = 600;

	public interface Binder extends UiBinder<Widget, ActivitiesView> {
	}

	@UiField
	ComplexPanel panelActivity;
	@UiField
	Anchor aCreate;
	@UiField
	Anchor aFollowUp;
	@UiField
	Anchor aReceive;
	@UiField
	Anchor aReview;
	@UiField
	Anchor aClose;
	@UiField
	DivElement divTutorial;
	@UiField
	LIElement liCreate;
	@UiField
	LIElement liFollowUp;
	@UiField
	LIElement liReceive;
	@UiField
	LIElement liReview;

	@UiField
	DivElement imgReceive;
	@UiField
	DivElement imgReview;
	
	@UiField Anchor aNew;
	@UiField Element processName;
	
	@UiField AnchorElement aInbox;
	@UiField AnchorElement aDone;
	
	@UiField FocusPanel parentPanel;
	
	@UiField FlexTable recentTasks;
	
	protected boolean hasElapsed = false;

	@Inject
	public ActivitiesView(final Binder binder) {
		widget = binder.createAndBindUi(this);

	}
	

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public HasWidgets getPanelActivity() {
		return panelActivity;
	}


	@Override
	public void bind() {
		final CarouselPopup popUp1 = new CarouselPopup();
		final int[] position = new int[2];
		position[0] = 40;

		aCreate.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				// popUp1.showFocusArea();
				timer = new Timer() {
					@Override
					public void run() {
						position[1] = liCreate.getAbsoluteRight();
						popUp1.showCreate(liCreate.getClientWidth());
						AppManager.showCarouselPanel(popUp1, position, false);
						hasElapsed = true;
						popUp1.setFocus(true);
						// System.out.println("Li Create:"+ position[1]);
					}
				};
				timer.schedule(timerSeconds);
			}
		});

		aFollowUp.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				// popUp1.showFocusArea();
				timer = new Timer() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						position[1] = liFollowUp.getAbsoluteRight();
						AppManager.showCarouselPanel(popUp1, position, false);
						popUp1.showFollowUp();
						hasElapsed = true;
						popUp1.setFocus(true);
						// System.out.println("Li FollowUp:"+ position[1]);
					}
				};
				timer.schedule(timerSeconds);
			}
		});

		aReceive.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				timer = new Timer() {
					@Override
					public void run() {
						int browserWidth = Window.getClientWidth();
						int popovermaxwidth = (int) (0.4 * browserWidth);
						position[1] = imgReceive.getAbsoluteLeft()
									  - popovermaxwidth;
						popUp1.getElement().getStyle().setWidth(popovermaxwidth-15, Unit.PX);

						AppManager.showCarouselPanel(popUp1, position, true);
						popUp1.showTask();
						hasElapsed = true;
						popUp1.setFocus(false);
						// System.out.println("Li Receive:"+ position[1]);
					}
				};
				timer.schedule(timerSeconds);
			}
		});

		aReview.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {

				timer = new Timer() {
					@Override
					public void run() {
						int browserWidth = Window.getClientWidth();
						int popovermaxwidth = (int) (0.4 * browserWidth);
						position[1] = imgReview.getAbsoluteLeft()
									  - popovermaxwidth;
						popUp1.getElement().getStyle().setWidth(popovermaxwidth-20, Unit.PX);

						AppManager.showCarouselPanel(popUp1, position, true);
						popUp1.showReview();
						hasElapsed = true;
						popUp1.setFocus(false);
					}
				};
				timer.schedule(timerSeconds);
			}
		});

		aCreate.addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				if (hasElapsed) {
					timer.cancel();
					AppContext.fireEvent(new CloseCarouselEvent());
				}
			}
		});
	
		
		aFollowUp.addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				if (hasElapsed) {
					timer.cancel();
					AppContext.fireEvent(new CloseCarouselEvent());
				}
			}
		});
		aReceive.addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				if (hasElapsed) {
					timer.cancel();
					AppContext.fireEvent(new CloseCarouselEvent());
				}
			}
		});
		aReview.addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				if (hasElapsed) {
					timer.cancel();
					AppContext.fireEvent(new CloseCarouselEvent());
				}
			}
		});

		aClose.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				divTutorial.addClassName("hidden");
				AppContext.setSessionValue(Definitions.SHOWWELCOMEWIDGET, "false");
			}
		});
		
		if(!AppContext.isShowWelcomeWiget()){
			divTutorial.addClassName("hidden");
		}

		// TODO: Remove this afterwards
		// divTutorial.addClassName("hidden");
		
	}


	@Override
	public void createGroup(String label) {
		HTMLPanel divLabel = new HTMLPanel("<hr/>");
		divLabel.addStyleName("day_divider");
		
		HTMLPanel lbl = new HTMLPanel(label);
		lbl.addStyleName("day_divider_label");
		divLabel.add(lbl); 
		
		panelActivity.add(divLabel);
	}
	
	public HasClickHandlers getNew(){
		return aNew;
	}

	@Override
	public void setProcess(ProcessDef process) {
		processName.setInnerText(process.getDisplayName());
		aInbox.setHref("#/inbox/mine/"+process.getRefId());
		aDone.setHref("#/participated/"+process.getRefId());
	}


	@Override
	public void setTaskList(ArrayList<Doc> tasks) {
		
		recentTasks.removeAllRows();
		setHeaders(recentTasks);
		
		int i = recentTasks.getRowCount();

		int count = 0;
		for (Doc doc : tasks) {
			if(count++>5){
				//Print 5 only
//				break;
			}
			
			int j = 0;

			Date dateToUse = doc.getSortDate();
			InlineLabel spnTime = new InlineLabel();

			String taskActualOwner = doc.getOwner() == null ? "" : doc
					.getOwner().getFullName();

			Element spnSubject = DOM.createSpan();
			spnSubject.setInnerText("Fill in request form");

			Element spnAttach = DOM.createSpan();
			spnAttach.addClassName("icon-paper-clip clip hidden");
			if (doc.hasAttachment()) {
				spnAttach.removeClassName("hidden");
			}

			// spnPriority.setText(summaryTask.getPriority()==null? "":
			// summaryTask.getPriority().toString());
			InlineLabel spnDocIcon = new InlineLabel();
			spnDocIcon.setStyleName("doc-status");

			InlineLabel spnDescription = new InlineLabel();
			spnDescription.addStyleName("spnDescription ellipsis");

			Element spnDeadlines = DOM.createSpan();
			spnDeadlines.addClassName("spnDate");

			Element spnProcessName = DOM.createSpan();
			spnProcessName.setInnerText(doc.getProcessName());

			InlineLabel spnStatus = new InlineLabel();

			boolean isCompleted = true;
			if (doc instanceof Document) {
				isCompleted = !(((Document) doc).getStatus() == DocStatus.DRAFTED);
			}

			spnDeadlines = setDeadlines(DateUtils.addDays(doc.getCreated(), 1),
					isCompleted);

			if (doc instanceof HTSummary) {
				HTSummary summ = (HTSummary) doc;
				HTStatus status = summ.getStatus();
				spnStatus.setText(status.name());

				if (summ.getRefId() == null) {
					spnSubject.getStyle().setColor("red");
					spnSubject
							.setTitle("This request was not loaded correctly.");// documentId
																				// is
																				// missing
				}

				dateToUse = summ.getCreated();
				if (status.equals(HTStatus.COMPLETED)) {
					spnDocIcon.addStyleName("icon-ok");
					spnDocIcon.setTitle("Completed Task");
					dateToUse = summ.getCompletedOn();
				} else if (status.equals(HTStatus.SUSPENDED)) {
					spnDocIcon.addStyleName("icon-pause");
					spnDocIcon.setTitle("Task Currently Suspended");
				} else if (status.equals(HTStatus.INPROGRESS)) {
					spnDocIcon.addStyleName("icon-forward");
					spnDocIcon.setTitle("Task Currently in Progress");
				} else {
					spnDocIcon.addStyleName("icon-play");
					spnDocIcon.setTitle("Task Awaiting your action");
				}

				if (!StringUtils.isNullOrEmpty(summ.getName())) {
					spnSubject.setInnerText(summ.getName());
				} else if (!StringUtils.isNullOrEmpty(summ.getNodeName())) {
					spnSubject.setInnerText(summ.getNodeName());
				}

				// setTaskAction(summ.getStatus().getValidActions());

				if (!status.equals(HTStatus.COMPLETED)) {
					if (summ.getEndDateDue() != null) {
						spnDeadlines = setDeadlines(summ.getEndDateDue());
					} else {
						// default 1 day allowance
						spnDeadlines = setDeadlines(DateUtils.addDays(
								summ.getCreated(), 1));
					}
				}
			} else {
				Document document = (Document) doc;
				spnStatus.setText(document.getStatus().name());
				if (document.getStatus() != DocStatus.DRAFTED) {
					spnDocIcon.addStyleName("icon-ok");
					spnDocIcon.setTitle("Completed Task");
				} else {
					spnDocIcon.addStyleName("icon-file-alt color-silver-dark");
				}
			}

			// Description
			String desc = doc.getCaseNo();
			if (desc == null) {
				desc = doc.get("subject") == null ? null : doc.get("subject")
						.toString();
			}
			if (desc == null) {
				desc = doc.get("caseNo") == null ? null : doc.get("caseNo")
						.toString();
			}

			if (doc.getProcessStatus() == HTStatus.COMPLETED) {
				taskActualOwner = "Completed";
				spnDescription.getElement().setInnerHTML(
						desc + " - <span style='color:green;'>"
								+ taskActualOwner + "</span>");
			} else {
				// How far in the workflow is my request
				if (doc.getTaskActualOwner() != null) {

					// Delegations are also handled here
					if (doc instanceof HTSummary) {
						HTSummary summ = (HTSummary) doc;
						if (summ.getDelegate() != null
								&& summ.getDelegate().getDelegateTo() != null) {
							taskActualOwner = "Delegated: "
									+ summ.getTaskActualOwner().getFullName();
						} else {
							taskActualOwner = doc.getTaskActualOwner()
									.getFullName();
						}
					} else {
						taskActualOwner = doc.getTaskActualOwner()
								.getFullName();
					}

				} else {
					if (doc.getPotentialOwners() != null)
						taskActualOwner = doc.getPotentialOwners();
				}

				if (doc.getProcessInstanceId() != null
						&& (taskActualOwner == null || taskActualOwner
								.isEmpty())) {
					spnDescription
							.getElement()
							.setInnerHTML(
									desc
											+ " - <span style='color:#D74819;font-size:9pt;'>UnAssigned</span>");
				} else if (doc.getProcessInstanceId() != null) {

					// Span Description
					spnDescription
							.getElement()
							.setInnerHTML(
									desc
											+ " - <span style='color:#2C3539;font-size:9pt;'>"
											+ taskActualOwner + "</span>");
				} else {
					spnDescription
							.getElement()
							.setInnerHTML(
									desc
											+ " - <span style='color:#2C3539;font-size:9pt;'>\'Draft\'</span>");
				}
			}// End of setting descriptions

			Priority priority = Priority.get(doc.getPriority());
			InlineLabel spnPriority = new InlineLabel();
			spnPriority.addStyleName("priority");
			switch (priority) {
			case CRITICAL:
				spnPriority.addStyleName("label-important");
				break;

			case HIGH:
				spnPriority.addStyleName("label-warning"); //
				break;

			default:
				spnPriority.addStyleName("hide");
				break;
			}

			
			// Several days ago
			if(dateToUse!=null){
				if (CalendarUtil.getDaysBetween(dateToUse, new Date()) >= 1) {
					spnTime.setText(DateUtils.LONGDATEFORMAT.format(dateToUse));
				} else {
					spnTime.setText(DateUtils.LONGDATEFORMAT.format(dateToUse));
				}
			}

			ActionLink link = new ActionLink(doc);
			link.setHref("#/search/"+doc.getRefId());
			if (doc.getCaseNo() != null){
				link.setText("#" + (doc.getCaseNo().replaceAll("Case-", "")));
			}

			HTMLPanel casePanel = new HTMLPanel("");
			casePanel.add(link);
			recentTasks.setWidget(i, j++, casePanel);

//			Process Name
//			HTMLPanel subject = new HTMLPanel("");
//			subject.getElement().appendChild(spnProcessName);
//			recentTasks.setWidget(i, j++, subject);

			HTMLPanel task = new HTMLPanel("");
			task.getElement().appendChild(spnSubject);
			recentTasks.setWidget(i, j++, task);
			
			//Submitted By
//			InlineLabel submitter = new InlineLabel(doc.getOwner()==null? "": doc.getOwner().getFullName());
//			recentTasks.setWidget(i, j++, submitter);

			//Current Task
//			String currentTask = doc.getCurrentTaskName()==null? "": doc.getCurrentTaskName();
//			recentTasks.setWidget(i, j++, new HTMLPanel(currentTask));
			//Current Owner 
//			recentTasks.setWidget(i, j++, new HTMLPanel(taskActualOwner));
			// recentTasks.getFlexCellFormatter().setWidth(i, (j - 1), "150px");
//			recentTasks.setWidget(i, j++, spnTime);
//			HTMLPanel div = new HTMLPanel("");
//			div.getElement().appendChild(spnDeadlines);
//			recentTasks.setWidget(i, j++, div);

			HTMLPanel status = new HTMLPanel("");
			// status.add(spnStatus);
			switch (doc.getProcessStatus()) {
			case COMPLETED:
				status.addStyleName("text-success");
				break;
			case INPROGRESS:
				status.addStyleName("text-info");
				break;
			
			default:
				break;
			}
			
			status.add(new InlineLabel(doc.getProcessStatus().name()));
			recentTasks.setWidget(i, j++, status);
			
			//Notes
			recentTasks.setWidget(i, j++, new HTMLPanel(""));
			++i;
		}

	}


	private void setHeaders(FlexTable table) {
		int i = table.getRowCount();
		int j = 0;
//		table.setWidget(i, j++, new HTMLPanel("<strong>#</strong>"));
//		table.getFlexCellFormatter().setWidth(i, (j - 1), "10px");
		table.setWidget(i, j++, new HTMLPanel("<strong>Case No</strong>"));
		table.getFlexCellFormatter().setWidth(i, (j - 1), "50px");
//		table.setWidget(i, j++, new HTMLPanel("<strong>Process</strong>"));
		table.setWidget(i, j++, new HTMLPanel("<strong>Task</strong>"));
//		table.setWidget(i, j++, new HTMLPanel("<strong>Submitted By</strong>"));
//		table.setWidget(i, j++, new HTMLPanel("<strong>Current Task</strong>"));
//		table.setWidget(i, j++, new HTMLPanel("<strong>Current User</strong>"));
//		table.setWidget(i, j++, new HTMLPanel("<strong>Due Date</strong>"));
//		table.getFlexCellFormatter().setWidth(i, (j - 1), "80px");
//		table.setWidget(i, j++, new HTMLPanel("<strong>Last Modify</strong>"));
//		table.getFlexCellFormatter().setWidth(i, (j - 1), "80px");
		table.setWidget(i, j++, new HTMLPanel("<strong>Status</strong>"));
		table.getFlexCellFormatter().setWidth(i, (j - 1), "60px");
		table.setWidget(i, j++, new HTMLPanel("<strong>Notes</strong>"));
		table.getFlexCellFormatter().setWidth(i, (j - 1), "20px");
		for (int col = 0; col < table.getCellCount(i); col++) {
			table.getFlexCellFormatter().setStyleName(i, col, "th");
		}
		
	}
	
	private Element setDeadlines(Date endDateDue) {
		return setDeadlines(endDateDue, false);
	}

	private Element setDeadlines(Date endDateDue, boolean isCompleted) {
		Element spnDeadline = DOM.createSpan();
		spnDeadline.setInnerText(DateUtils.LONGDATEFORMAT.format(endDateDue));

		if (DateUtils.isOverdue(endDateDue)) {

			String timeDiff = DateUtils.getTimeDifference(endDateDue);

			// spnDeadline.setInnerText("Overdue");
			spnDeadline.setTitle("Overdue " + timeDiff + " Ago");
			spnDeadline.removeClassName("hidden");
			if (!isCompleted) {
				spnDeadline.getStyle().setColor("red");
			}
			// spnDeadline.addClassName("label-important");
		} else if (DateUtils.isDueInMins(30, endDateDue)) {

			String timeDiff = DateUtils.getTimeDifference(endDateDue);

			// spnDeadline.setInnerText("Due soon");
			spnDeadline.setTitle("Due in " + timeDiff);
			spnDeadline.removeClassName("hidden");
			if (!isCompleted) {
				spnDeadline.getStyle().setColor("orange");
			}
			// spnDeadline.addClassName("label-warning");
		}

		return spnDeadline;
	}


}
