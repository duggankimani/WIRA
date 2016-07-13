package com.duggan.workflow.client.ui.component;

import com.duggan.workflow.client.util.AppContext;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.wira.commons.shared.models.HTUser;

public class CommentBox extends Composite {
	
	@UiField Image img;
	@UiField Anchor aSaveComment;
	@UiField HTMLPanel commentContainer;
	@UiField TextArea commentBox;
	@UiField SpanElement spnArrow;
	@UiField FocusPanel commentPanel;
	
	private static CommentBoxUiBinder uiBinder = GWT
			.create(CommentBoxUiBinder.class);

	interface CommentBoxUiBinder extends UiBinder<Widget, CommentBox> {
	}

	public CommentBox() {
		initWidget(uiBinder.createAndBindUi(this));
		commentBox.getElement().setAttribute("placeholder","Make comments, ask for clarifications ...");
		commentPanel.getElement().removeAttribute("tabindex");
		img.addErrorHandler(new ErrorHandler() {
			
			@Override
			public void onError(ErrorEvent event) {
				img.setUrl("img/blueman.png");
			}
		});
		img.getElement().getStyle().setWidth(45.0, Unit.PX);
		img.getElement().getStyle().setHeight(45.0, Unit.PX);

		/*Comment Box Effect
		 * --OnFocus
		 * */
		FocusHandler handler = new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				commentContainer.addStyleName("comment-big");
				commentContainer.removeStyleName("comment-small");
				commentBox.addStyleName("textarea-big");
				commentBox.removeStyleName("textarea-small");
				aSaveComment.removeStyleName("hidden");
			}
		};
		
		
		BlurHandler blurHandler = new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				
				if(commentBox.getValue().trim().isEmpty()){
					commentContainer.addStyleName("comment-small");
					commentContainer.removeStyleName("comment-big");
					commentBox.addStyleName("textarea-small");
					commentBox.removeStyleName("textarea-big");
					aSaveComment.addStyleName("hidden");
				}
			}
		};
		
		aSaveComment.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				commentContainer.addStyleName("comment-small");
				commentContainer.removeStyleName("comment-big");
				commentBox.addStyleName("textarea-small");
				commentBox.removeStyleName("textarea-big");
				aSaveComment.addStyleName("hidden");
			}
		});
		commentBox.addFocusHandler(handler);
		commentBox.addBlurHandler(blurHandler);		
		
		setImage(AppContext.getContextUser());
	}
	
	public TextArea getCommentBox() {
		return commentBox;
	}
	
	public Anchor getaSaveComment() {
		return aSaveComment;
	}
	
	public SpanElement getSpnArrow() {
		return spnArrow;
	}
	
	private void setImage(HTUser user) {
		String moduleUrl = GWT.getModuleBaseURL().replace("/gwtht", "");
		if(moduleUrl.endsWith("/")){
			moduleUrl = moduleUrl.substring(0, moduleUrl.length()-1);
		}
		moduleUrl =moduleUrl+"/getreport?ACTION=GetUser&width=45&height=45&userId="+user.getUserId();
		img.setUrl(moduleUrl);
	}

}
