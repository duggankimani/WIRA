package com.duggan.workflow.client.ui.activityfeed.components;

import static com.duggan.workflow.client.ui.util.DateUtils.getTimeDifferenceAsString;

import java.util.Date;

import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.Comment;
import com.duggan.workflow.shared.model.HTUser;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class CommentActivity extends Composite {

	private static CommentActivityUiBinder uiBinder = GWT
			.create(CommentActivityUiBinder.class);

	interface CommentActivityUiBinder extends UiBinder<Widget, CommentActivity> {
	}
	
	@UiField SpanElement spnAction;
	@UiField SpanElement spnSubject;
	@UiField SpanElement spnTime;
	@UiField Element spnUser;
	@UiField SpanElement commentText;
	Long commentid;

	public CommentActivity(Comment comment) {
		initWidget(uiBinder.createAndBindUi(this));
		bind(comment);
	}
	
	void bind(Comment comment) {
		
		setComment(comment.getId(), comment.getComment(), comment.getCreatedBy(),
				comment.getCreated(), comment.getUpdatedBy(), comment.getUpdated(),
				comment.getDocumentId(), comment.getParentId()!=null);
	}


	public void setComment(Long commentId, String comment, HTUser createdBy,
			Date created, String updatedby, Date updated, long documentId, boolean isChild) {
		
		spnSubject.setInnerText("Invoice ABC");
		
		this.commentid=commentId;
		if(createdBy!=null){
			spnTime.setInnerText(getTimeDifferenceAsString(created));
			
			if(AppContext.isCurrentUser(createdBy.getUserId())){
				spnUser.setInnerText("You");
			}else{
				spnUser.setInnerText(createdBy.getName());
			}
			
		}
		
		setComment(comment);
	}

	public void setComment(String comments) {
		commentText.setInnerText(comments);
	}


	public CommentActivity(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}


}
