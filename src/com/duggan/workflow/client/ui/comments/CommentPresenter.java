package com.duggan.workflow.client.ui.comments;

import java.util.Date;

import com.duggan.workflow.client.model.MODE;
import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.shared.model.Comment;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.requests.SaveCommentRequest;
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
				Date created, String updatedby, Date updated, long documentId);

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
		getView().setComment(
				comment.getId(),
				comment.getComment(), "Created By",
				comment.getCreated(), "Updated By",
				comment.getUpdated(), comment.getDocumentId());

	}

	protected void saveComment(final String commentTxt) {

		comment.setComment(commentTxt);
		
		final SaveCommentRequest request = new SaveCommentRequest(comment);

		requestHelper.execute(request,
				new TaskServiceCallback<SaveCommentResponse>() {
					public void processResult(SaveCommentResponse response) {

						comment = response.getComment();

						if (comment == null) {
							getView().asWidget().removeFromParent();
							return;
						}
						bind(comment);
					};
				});
	}

	public void setComment(Comment comment) {
		this.comment = comment;
		bind(comment);
	}
}
