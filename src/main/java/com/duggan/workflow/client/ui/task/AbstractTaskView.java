package com.duggan.workflow.client.ui.task;

import static com.duggan.workflow.client.ui.task.AbstractTaskPresenter.DATEGROUP_SLOT;
import static com.duggan.workflow.client.ui.task.AbstractTaskPresenter.DOCUMENT_SLOT;
import static com.duggan.workflow.client.ui.task.AbstractTaskPresenter.FILTER_SLOT;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.duggan.workflow.client.event.CheckboxSelectionEvent;
import com.duggan.workflow.client.model.TaskType;
import com.duggan.workflow.client.ui.component.ActionLink;
import com.duggan.workflow.client.ui.component.BulletListPanel;
import com.duggan.workflow.client.ui.component.Checkbox;
import com.duggan.workflow.client.ui.component.DateField;
import com.duggan.workflow.client.ui.component.DateInput;
import com.duggan.workflow.client.ui.component.DropDownList;
import com.duggan.workflow.client.ui.events.DocumentSelectionEvent;
import com.duggan.workflow.client.ui.task.DraftsPresenter.IDraftsView;
import com.duggan.workflow.client.ui.task.ParticipatedPresenter.IParticipatedView;
import com.duggan.workflow.client.ui.task.SuspendedTaskPresenter.ISuspendedView;
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
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class AbstractTaskView extends ViewImpl implements
		AbstractTaskPresenter.ITaskView, IDraftsView, IParticipatedView,
		ISuspendedView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, AbstractTaskView> {
	}

	@UiField
	HTMLPanel container;
	@UiField
	TextBox txtSearch;
	// @UiField HTMLPanel divDocPopup;
	@UiField
	ScrollPanel divDocListing;
	@UiField
	Element divDocView;
	@UiField
	Element divTasks;
	@UiField
	BulletListPanel ulTaskGroups;
	@UiField
	HeadingElement hCategory;
	@UiField
	Anchor aRefresh;
	@UiField
	Anchor iFilterdropdown;
	@UiField
	HTMLPanel filterDialog;
	@UiField
	InlineLabel spnNoItems;
	@UiField
	HTMLPanel docContainer;
	@UiField
	FlexTable tblTasks;

	@UiField
	ScrollPanel divTableListing;

	// Filter Dialog Caret
	boolean isNotDisplayed = true;

	@Inject
	public AbstractTaskView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		divTasks.setId("middle-box");
		ulTaskGroups.setId("navigation-menu");
		txtSearch.getElement().setAttribute("placeholder", "Search...");
		divDocListing.getElement().setId("middle-nav");
		divDocView.setId("detailed-info");
		divTableListing.getElement().getStyle().setMarginLeft(0, Unit.PX);

		docContainer.addHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// System.err.println("### ABS");
			}
		}, ClickEvent.getType());

		iFilterdropdown.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (isNotDisplayed) {
					filterDialog.removeStyleName("hidden");
					isNotDisplayed = false;
				} else {
					filterDialog.addStyleName("hidden");
					isNotDisplayed = true;
				}
			}
		});

		txtSearch.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				// hideFilterDialog();
			}
		});

		createHeader(tblTasks);
	}

	protected void createHeader(FlexTable table) {
		int i = table.getRowCount();
		int j = 0;
		table.setWidget(i, j++, new HTMLPanel("<strong>#</strong>"));
		table.getFlexCellFormatter().setWidth(i, (j - 1), "10px");

		// table.setWidget(0, j++, new HTMLPanel("<strong>Summary</strong>"));
		// table.getFlexCellFormatter().setWidth(i, (j - 1), "100px");
		table.setWidget(i, j++, new HTMLPanel("<strong>Case No</strong>"));
		table.getFlexCellFormatter().setWidth(i, (j - 1), "40px");
		// table.setWidget(i, j++, new HTMLPanel("<strong>Case</strong>"));
		// table.getFlexCellFormatter().setWidth(i, (j - 1), "100px");
		table.setWidget(i, j++, new HTMLPanel("<strong>Process</strong>"));
		table.getFlexCellFormatter().setWidth(i, (j - 1), "200px");
		table.setWidget(i, j++, new HTMLPanel("<strong>Task</strong>"));
		table.getFlexCellFormatter().setWidth(i, (j - 1), "100px");
		table.setWidget(i, j++, new HTMLPanel("<strong>Current User</strong>"));
		table.getFlexCellFormatter().setWidth(i, (j - 1), "60px");
		table.setWidget(i, j++, new HTMLPanel("<strong>Last Modify</strong>"));
		table.getFlexCellFormatter().setWidth(i, (j - 1), "60px");
		table.setWidget(i, j++, new HTMLPanel("<strong>Due Date</strong>"));
		table.getFlexCellFormatter().setWidth(i, (j - 1), "60px");
		table.setWidget(i, j++, new HTMLPanel("<strong>Status</strong>"));
		table.getFlexCellFormatter().setWidth(i, (j - 1), "60px");

		for (int col = 0; col < table.getCellCount(i); col++) {
			table.getFlexCellFormatter().setStyleName(i, col, "th");
		}
		
		/**
		++i;
		j=0;

		table.setWidget(i, j++, new HTMLPanel("<strong>#</strong>"));
		
		table.getFlexCellFormatter().setColSpan(i, j, 7);
		HTMLPanel searchForm = new HTMLPanel("");
		searchForm.addStyleName("form-inline");
		TextBox caseSearch = new TextBox();
		caseSearch.addStyleName("input-medium search-query");
		caseSearch.getElement().setPropertyString("placeholder", "Case No");
		searchForm.add(caseSearch);
		
		DropDownList process = new DropDownList<ProcessDef>();
		process.addStyleName("input-medium");
		process.setNullText("--Process--");
		process.setItems(new ArrayList<ProcessDef>());
		searchForm.add(process);
		
		DropDownList task = new DropDownList<ProcessDef>();
		task.addStyleName("input-medium");
		task.setNullText("--Task--");
		task.setItems(new ArrayList<ProcessDef>());
		searchForm.add(task);
		
		DropDownList currentUser = new DropDownList<ProcessDef>();
		currentUser.addStyleName("input-medium");
		currentUser.setNullText("--Current User--");
		currentUser.setItems(new ArrayList<ProcessDef>());
		searchForm.add(currentUser);
		
		DropDownList status = new DropDownList<HTStatus>();
		status.addStyleName("input-medium");
		status.setNullText("--Status--");
		status.setItems(new ArrayList<HTStatus>());
		searchForm.add(status);
		
		table.setWidget(i, j, searchForm);*/
		
	}

	@Override
	public void addScrollHandler(ScrollHandler scrollHandler) {
		divDocListing.addScrollHandler(scrollHandler);
		divTableListing.addScrollHandler(scrollHandler);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setInSlot(Object slot, IsWidget content) {
		if (slot == DATEGROUP_SLOT) {
			ulTaskGroups.clear();
			if (content != null) {
				ulTaskGroups.add(content);
			}
		} else if (slot == DOCUMENT_SLOT) {
			docContainer.clear();
			if (content != null) {
				docContainer.add(content);
				displayTable(false);
			} else {
				displayTable(true);
			}
		} else if (slot == FILTER_SLOT) {
			filterDialog.clear();
			if (content != null) {
				filterDialog.add(content);
			}

		} else {
			super.setInSlot(slot, content);
		}

	}

	protected void displayTable(boolean isDisplayTable) {
		if (isDisplayTable) {
			divDocView.addClassName("hide");
			divTasks.addClassName("hide");
			divTableListing.removeStyleName("hide");
		} else {
			divDocView.removeClassName("hide");
			divTasks.removeClassName("hide");
			divTableListing.addStyleName("hide");
		}
	}

	@Override
	public void addToSlot(Object slot, IsWidget content) {
		if (slot == DATEGROUP_SLOT) {
			if (content != null) {
				ulTaskGroups.add(content);
			}
		} else {
			super.addToSlot(slot, content);
		}
	}

	public void setHeading(String heading) {
		hCategory.setInnerText(heading);
	}

	public HasClickHandlers getRefreshButton() {
		return aRefresh;
	}

	public void setHasItems(boolean hasItems) {
		UIObject.setVisible(spnNoItems.getElement(), !hasItems);
		if (!hasItems) {
			spnNoItems.setText("No items to display");
		}
	}

	public TextBox getSearchBox() {
		return txtSearch;
	}

	@Override
	public void hideFilterDialog() {
		filterDialog.addStyleName("hidden");
		isNotDisplayed = true;
	}

	@Override
	public void setSearchBox(String text) {
		txtSearch.setValue(text);
	}

	@Override
	public void setTaskType(TaskType currentTaskType) {

	}

	@Override
	public Anchor getaRefresh() {
		return null;
	}

	@Override
	public void bindTasks(ArrayList<Doc> tasks, boolean isIncremental) {

		if (!isIncremental) {
			tblTasks.removeAllRows();
			createHeader(tblTasks);
		}

		int i = tblTasks.getRowCount();

		for (Doc doc : tasks) {
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

			/**
			 * Build Table
			 */
			Checkbox box = new Checkbox(doc);
			box.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
				@Override
				public void onValueChange(ValueChangeEvent<Boolean> event) {
					Object model = ((Checkbox) (event.getSource())).getModel();
					AppContext.fireEvent(new CheckboxSelectionEvent(model,
							event.getValue()));
				}
			});

			tblTasks.setWidget(i, j++, box);
			ActionLink link = new ActionLink(doc);
			if (doc.getCaseNo() != null)
				link.setText("#" + (doc.getCaseNo().replaceAll("Case-", "")));

			HTMLPanel casePanel = new HTMLPanel("");
			casePanel.add(link);
			link.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					Object model = ((ActionLink) event.getSource()).getModel();
					if (model instanceof Document) {
						Document doc = (Document) model;
						AppContext.fireEvent(new DocumentSelectionEvent(doc
								.getRefId(), null, DocMode.READWRITE));
					} else {
						Long taskId = ((HTSummary) model).getId();
						String docRefId = ((HTSummary) model).getRefId();
						AppContext.fireEvent(new DocumentSelectionEvent(
								docRefId, taskId, DocMode.READ));
					}
				}
			});
			tblTasks.setWidget(i, j++, casePanel);

			HTMLPanel subject = new HTMLPanel("");
			subject.getElement().appendChild(spnProcessName);
			tblTasks.setWidget(i, j++, subject);

			HTMLPanel task = new HTMLPanel("");
			task.getElement().appendChild(spnSubject);
			tblTasks.setWidget(i, j++, task);

			tblTasks.setWidget(i, j++, new HTMLPanel(taskActualOwner));
			// tblTasks.getFlexCellFormatter().setWidth(i, (j - 1), "150px");
			tblTasks.setWidget(i, j++, spnTime);
			HTMLPanel div = new HTMLPanel("");
			div.getElement().appendChild(spnDeadlines);
			tblTasks.setWidget(i, j++, div);

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

			tblTasks.setWidget(i, j++, status);
			++i;
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

	@Override
	public void bindAlerts(HashMap<TaskType, Integer> alerts) {
	}
}
