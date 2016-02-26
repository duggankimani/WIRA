package com.duggan.workflow.client.ui.addDoc.doctypeitem;

import com.duggan.workflow.shared.events.CreateDocumentEvent;
import com.duggan.workflow.shared.model.DocumentType;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class DocTypeItemPresenter extends
		PresenterWidget<DocTypeItemPresenter.MyView> {
	
	public interface MyView extends View {
		public Anchor getaDocAnchor();
		public void setValues(String displayName, String className);
	}
	@Inject
	public DocTypeItemPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}
	
	DocumentType type;

	@Override
	protected void onBind() {
		super.onBind();
		
		getView().getaDocAnchor().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				fireEvent(new CreateDocumentEvent(type));			
				}
		});
	}
	
	@Override
	protected void onReveal() {
		super.onReveal();
	}
	
	public void setDocumentTypes(DocumentType type) {
		this.type=type;
		getView().setValues(type.getDisplayName(), type.getClassName());
	}
}
