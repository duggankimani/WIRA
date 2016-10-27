package com.duggan.workflow.client.ui.tasklistitem;

import com.duggan.workflow.client.model.TaskType;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.events.AfterAttachmentReloadedEvent;
import com.duggan.workflow.client.ui.events.AfterDocumentLoadEvent;
import com.duggan.workflow.client.ui.events.AfterSearchEvent;
import com.duggan.workflow.client.ui.events.DocumentSelectionEvent;
import com.duggan.workflow.client.ui.events.AfterAttachmentReloadedEvent.AfterAttachmentReloadedHandler;
import com.duggan.workflow.client.ui.events.AfterDocumentLoadEvent.AfterDocumentLoadHandler;
import com.duggan.workflow.client.ui.events.AfterSearchEvent.AfterSearchHandler;
import com.duggan.workflow.client.ui.events.DocumentSelectionEvent.DocumentSelectionHandler;
import com.duggan.workflow.client.ui.tasklistitem.IsTaskPresenter.IBaseTaskItemView;
import com.duggan.workflow.client.ui.util.DocMode;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.HTSummary;
import com.duggan.workflow.shared.requests.ApprovalRequest;
import com.duggan.workflow.shared.responses.ApprovalRequestResult;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

/**
 * This class displays a Task or a Document
 * 
 * @author duggan
 *
 */
public class TaskItemPresenter extends
		IsTaskPresenter<TaskItemPresenter.ITaskItemView> 
implements AfterAttachmentReloadedHandler, AfterSearchHandler,DocumentSelectionHandler, AfterDocumentLoadHandler{

	public interface ITaskItemView extends IBaseTaskItemView {
		void bind(Doc summaryTask);

		HasClickHandlers getClaimLink();

		HasClickHandlers getStartLink();

		HasClickHandlers getSuspendLink();

		HasClickHandlers getResumeLink();

		HasClickHandlers getCompleteLink();

		HasClickHandlers getDelegateLink();

		HasClickHandlers getRevokeLink();

		HasClickHandlers getStopLink();

		HasClickHandlers getForwardLink();

		HasClickHandlers getViewLink();

		HasClickHandlers getSubmitForApprovalLink();

		HasClickHandlers getApproveLink();

		HasClickHandlers getRejectLink();

		FocusPanel getFocusContainer();

		void setSelected(boolean selected);

		void setMiniDocumentActions(boolean status);

		void setTask(boolean isTask);

		void showAttachmentIcon(boolean hasAttachment);

		void highlight(String txt);

		void highlight(String subject, String description);

	}

	@Inject
	public TaskItemPresenter(final EventBus eventBus, final ITaskItemView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
//		addRegisteredHandler(AfterDocumentLoadEvent.TYPE, this);
		addRegisteredHandler(DocumentSelectionEvent.TYPE, this);
		addRegisteredHandler(AfterAttachmentReloadedEvent.TYPE, this);
		addRegisteredHandler(AfterSearchEvent.TYPE, this);
		//
		getView().getFocusContainer().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (doc instanceof Document) {
					Document document = (Document) doc;
					fireEvent(new DocumentSelectionEvent(document.getRefId(), null,
							DocMode.READWRITE));
				} else {
					Long taskId = ((HTSummary) doc).getId();
					String docRefId = ((HTSummary) doc).getRefId();
					fireEvent(new DocumentSelectionEvent(docRefId, taskId,
							DocMode.READ));
				}
			}
		});
		getView().getFocusContainer().addMouseOverHandler(
				new MouseOverHandler() {
					@Override
					public void onMouseOver(MouseOverEvent event) {
						getView().setMiniDocumentActions(true);
					}
				});

		getView().getFocusContainer().addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				getView().setMiniDocumentActions(false);
			}
		});

		getView().getSubmitForApprovalLink().addClickHandler(
				new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						dispatcher.execute(
								new ApprovalRequest(AppContext.getUserId(),
										(Document) doc),
								new TaskServiceCallback<ApprovalRequestResult>() {
									@Override
									public void processResult(
											ApprovalRequestResult result) {
										PlaceRequest request = new PlaceRequest.Builder()
												.nameToken("home")
												.with("type",
														TaskType.INBOX.getURL())
												.build();
										// new PlaceRequest("home").
										// with("type",
										// TaskType.INBOX.getURL());

										placeManager.revealPlace(request);

									}
								});
					}
				});

	}
	
	public void setDocSummary(Doc summaryTask) {
		this.doc = summaryTask;
		if (summaryTask != null) {
			try {
				getView().bind(summaryTask);
			} catch (Exception e) {
				GWT.log(e.getMessage());
			}

		}

		if (doc instanceof HTSummary) {
			getView().setTask(true);
		} else {
			getView().setTask(false);
		}
	}

	@Override
	public void onDocumentSelection(DocumentSelectionEvent event) {
		String docRefId = event.getDocRefId();
		Long taskId = event.getTaskId();

		if ((doc instanceof Document) && taskId == null) {
			Document document = (Document) doc;
			if (!document.getRefId().equals(docRefId)) {
				getView().setSelected(false);
			} else {
				getView().setSelected(true);
			}
		} else if (doc instanceof HTSummary) {
			Long tId = (Long) doc.getId();

			if (taskId == null || !taskId.equals(tId)) {
				getView().setSelected(false);
			} else {
				getView().setSelected(true);
			}

		} else {
			getView().setSelected(false);
		}
	}
	
	@Override
	public void onAfterSearch(AfterSearchEvent event) {
		getView().highlight(event.getSubject(), event.getDescription());
	}
	
	@Override
	public void onAfterAttachmentReloaded(AfterAttachmentReloadedEvent event) {
		String docRefId = event.getDocRefId();
		if ((doc instanceof Document)) {
			Document document = (Document) doc;
			if (document.getRefId().equals(docRefId)) {
				getView().showAttachmentIcon(true);
			}

		} else if (doc instanceof HTSummary) {
			HTSummary document = (HTSummary) doc;
			if (document.getDocumentRef() == null || document.getRefId() == null) {
				// This happens if this doc was not loaded correctly.
				// this should be changed to processinstanceid - documents dont
				// matter; processes do.
				return;
			}

			assert document.getDocumentRef() != null;
			assert docRefId != null;

			if (document.getRefId().equals(docRefId)) {
				getView().showAttachmentIcon(true);
			}
		}
	}

	protected void reload(HTSummary summary) {
		setDocSummary(summary);
		if (doc instanceof Document) {
			Document document = (Document) doc;
			fireEvent(new DocumentSelectionEvent(document.getRefId(), null,
					DocMode.READWRITE));
		} else {
			Long taskId = ((HTSummary) doc).getId();
			fireEvent(new DocumentSelectionEvent(doc.getRefId(), taskId,
					DocMode.READ));
		}

	}
	
	protected void removeFromParent() {
		unbind();
		this.getView().asWidget().removeFromParent();
	}
	
	/**
	 * TODO: Review use of documentId here
	 */
	@Override
	public void onAfterDocumentLoad(AfterDocumentLoadEvent event) {
		if (doc instanceof Document) {
			return;
		}

		HTSummary summary = (HTSummary) doc;

		if (summary.getId().equals(event.getTaskId())) {
			event.setValidActions(summary.getStatus().getValidActions());
		}
	}
	
	/**
	 * Duggan - 25/Jul/2013 - Had to add this since unbind is not automatically
	 * called, leaving removed/hidden instances of TaskItemPresenter still
	 * respondng to events
	 */
	@Override
	protected void onHide() {
		super.onHide();
		this.unbind();
	}

}
