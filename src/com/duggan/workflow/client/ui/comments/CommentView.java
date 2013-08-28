package com.duggan.workflow.client.ui.comments;

import static com.duggan.workflow.client.ui.util.DateUtils.*;

import java.util.Date;

import com.duggan.workflow.client.model.MODE;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class CommentView extends ViewImpl implements CommentPresenter.ICommentView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, CommentView> {
	}
	
	@UiField HTMLPanel root;
	
	@UiField SpanElement spnCreatedBy;
	@UiField SpanElement spnCreated;
	@UiField SpanElement spnMessage;
	
	@UiField Anchor lnkSaveComment;
	@UiField Anchor aReply;
	@UiField TextArea txtCommentBox;
	@UiField DivElement divSave;
	
	@UiField SpanElement spnTime;

	static String HIDDEN="hidden";
	static String DISPLAYED="block";

	static String CANCELIMAGE = "images/mm/close.gif";
	static String EDITIMAGE="images/mm/pen_icon12.png";
	
	Long commentid=null;
	MODE mode;
	

	@Inject
	public CommentView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		 txtCommentBox.getElement().setAttribute("placeholder","write your comment...");
		 txtCommentBox.setHeight("15px");
		 
		aReply.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				
				if(mode==MODE.VIEW){
					setMode(MODE.EDIT);		
				}else{
					if(commentid==null){
						asWidget().removeFromParent();
						return;
					}else{
						setMode(MODE.VIEW);		
						String txt = txtCommentBox.getText();
						txtCommentBox.setText(spnMessage.getInnerText());//cancel hit
						if((txt==null || txt.trim().isEmpty()) && commentid==null){
							asWidget().removeFromParent();
							return;
						}
					}
				}
			}
		});

	}
	
	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setComment(Long commentId, String comment, String createdBy,
			Date created, String updatedby, Date updated, long documentId) {
		
		this.commentid=commentId;
		if(createdBy!=null){
			spnCreated.setInnerText(getTimeDifferenceAsString(created));
			spnCreatedBy.setInnerText(createdBy);
		}
						
		setComment(comment);
		
	}

	public void setComment(String comments) {
		if(commentid!=null){
			spnMessage.setInnerText(comments);	
			txtCommentBox.setText(comments);
		
			//setMode(MODE.VIEW);
			setMode(MODE.VIEW);
		}else{
			setMode(MODE.EDIT);
		}
	}
	
	/**
	 * 1 - View
	 * 2 - Edit
	 * @param mode
	 */
	public void setMode(MODE mode){
		this.mode=mode;
		
		if(mode==MODE.EDIT){			
			((Widget)aReply).setTitle("Edit");
			divSave.removeClassName(HIDDEN);
			txtCommentBox.removeStyleName(HIDDEN);
			
			spnMessage.addClassName(HIDDEN);
			aReply.addStyleName(HIDDEN);
			spnTime.setClassName(HIDDEN);
		}else{

			((Widget)aReply).setTitle("Cancel");
			divSave.addClassName(HIDDEN);
			txtCommentBox.addStyleName(HIDDEN);

			spnMessage.removeClassName(HIDDEN);
			aReply.removeStyleName(HIDDEN);
			spnTime.removeClassName(HIDDEN);
		}
	}
	
	public TextArea getCommentBox(){
		return txtCommentBox;
	}
	
	public HasClickHandlers getSaveCommentsLink(){
		return lnkSaveComment;
	}

}
