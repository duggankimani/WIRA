package com.duggan.workflow.client.ui.home.doctree;

import java.util.List;

import com.duggan.workflow.shared.model.Attachment;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class DocTreeView extends ViewImpl implements DocTreePresenter.IDocTreeView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, DocTreeView> {
	}

	@UiField Tree tree;
	TreeItem root;
	
	@Inject
	public DocTreeView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		root = new TreeItem();
		root.setHTML("Root");
		tree.addItem(root);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void display(List<Attachment> attachments) {
		root.removeItems();
		
		for(Attachment attachment: attachments){
			SafeHtmlBuilder builder = new SafeHtmlBuilder();
			TreeItem item = new TreeItem(builder.appendEscaped(attachment.getName()).toSafeHtml());
			item.setTitle(attachment.getName());
			root.addItem(item);
		}
	}

}
