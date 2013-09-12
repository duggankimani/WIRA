package com.duggan.workflow.client.ui.comments;

import java.util.Date;

import com.duggan.workflow.client.model.MODE;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.events.ActivitiesLoadEvent;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.Comment;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.requests.GetActivitiesRequest;
import com.duggan.workflow.shared.requests.MultiRequestAction;
import com.duggan.workflow.shared.requests.SaveCommentRequest;
import com.duggan.workflow.shared.responses.GetActivitiesResponse;
import com.duggan.workflow.shared.responses.MultiRequestActionResult;
import com.duggan.workflow.shared.responses.SaveCommentResponse;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.google.inject.Inject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.TextArea;

public class CommentPresenter extends PresenterWidget<CommentPresenter.ICommentView> {

	public interface ICommentView extends View {
		TextArea getCommentBox();

		void setComment(Long commentId, String comment, String createdBy,
				Date created, String updatedby, Date updated, long documentId, boolean isChild);

		HasClickHandlers getSaveCommentsLink();

		void setMode(MODE mode);

	}

	Comment comment;

	@Inject
	DispatchAsync requestHelper;

	@Inject
	public CommentPresenter(final EventBus eventBus, final ICommentView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();

		getView().getSaveCommentsLink().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String commentText = getView().getCommentBox().getText();

				if (commentText == null || commentText.trim().isEmpty()) {
					if (comment.getId() == null) {
						getView().asWidget().removeFromParent();// not previously saved & not mandatory
						return;
					} else {
						// not mandatory/ clear previously saved
						saveComment(commentText);
					}
				} else if (commentText.equals(comment.getComment())
						&& comment.getId() != null) {
					getView().setMode(MODE.VIEW);
					return;// no change
				} else {
					// new values
					saveComment(commentText);
				}
			}
		});
	}

	private void bind(Comment comment) {
		
		getView().setComment(comment.getId(), comment.getComment(), comment.getCreatedBy(),
				comment.getCreated(), comment.getUpdatedBy(), comment.getUpdated(),
				comment.getDocumentId(), comment.getParentId()!=null);
	}

	protected void saveComment(final String commentTxt) {

		if(comment==null || commentTxt.trim().isEmpty())
			return;

		Comment commentToSave = new Comment();
		commentToSave.setComment(commentTxt);
		commentToSave.setDocumentId(comment.getDocumentId());
		commentToSave.setUserId(AppContext.getUserId());
		
		if(comment.getParentId()!=null){
			commentToSave.setParentId(comment.getParentId());
		}else{
			commentToSave.setParentId(comment.getId());
		}
		
		commentToSave.setComment(commentTxt);
		commentToSave.setCreatedBy(AppContext.getUserId());

		MultiRequestAction action = new MultiRequestAction();
		action.addRequest(new SaveCommentRequest(commentToSave));
		action.addRequest(new GetActivitiesRequest(comment.getDocumentId()));
		
		requestHelper.execute(action,
				 new TaskServiceCallback<MultiRequestActionResult>(){
			@Override
			public void processResult(MultiRequestActionResult result) {
				result.get(0);
				
				GetActivitiesResponse response = (GetActivitiesResponse)result.get(1);
				
				fireEvent(new ActivitiesLoadEvent(response.getActivityMap()));
			}
		});
		
	}

	public void setComment(Comment comment) {
		this.comment = comment;
		bind(comment);
	}
}
