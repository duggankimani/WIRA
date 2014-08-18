package com.duggan.workflow.client.ui.home.doctree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.duggan.workflow.client.model.UploadContext;
import com.duggan.workflow.client.ui.component.ActionLink;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.Attachment;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.TreeNode;
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
	
	@Inject
	public DocTreeView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void display(List<Attachment> attachments) {
		tree.removeItems();
		Map<String, List<String>> docTypeDocNameList = new HashMap<String, List<String>>();
		Map<String, List<Attachment>> docNameAttachmentList = new HashMap<String, List<Attachment>>();

		for(Attachment attachment: attachments){
			String docType = attachment.getDocumentType();
			List<String> docNameList = docTypeDocNameList.get(docType);
			if(docNameList==null){
				docNameList= new ArrayList<String>();
				docTypeDocNameList.put(docType, docNameList);
			}
			String subject= attachment.getSubject();
			if(docNameAttachmentList.get(subject)==null){
				docNameList.add(subject);
			}
			
			//Attachments
			List<Attachment> attachmentsForDoc = docNameAttachmentList.get(subject);
			if(attachmentsForDoc==null){
				attachmentsForDoc = new ArrayList<Attachment>();
				docNameAttachmentList.put(subject, attachmentsForDoc);
			}
			attachmentsForDoc.add(attachment);
			
		}
		
		for(String docType: docTypeDocNameList.keySet()){
			
			TreeItem rootNode = createNode(null, docType);
			
			List<String> subjects = docTypeDocNameList.get(docType);
			for(String subject: subjects){
				TreeItem docNode = createNode(rootNode, subject);
				
				List<Attachment> docAttachments = docNameAttachmentList.get(subject);
				for(Attachment a: docAttachments){
					createNode(docNode, a);
				}
				
			}
		}
	}

	private TreeItem createNode(TreeItem parent, Attachment attachment) {
		
		UploadContext context = new UploadContext("getreport");
		context.setContext("attachmentId", attachment.getId()+"");
		context.setContext("ACTION", "GETATTACHMENT");
		String url = context.toUrl();
		
		ActionLink link = new ActionLink(attachment.getName());
		link.setTitle(attachment.getName());
		link.setTarget("_blank");
		link.setHref(AppContext.getBaseUrl()+"/"+url);
		
		TreeItem item = new TreeItem(link);
		if(parent!=null){
			parent.addItem(item);
		}else{
			tree.addItem(item);
		}
		
		return item;
	}

	private TreeItem createNode(TreeItem parent, String display) {
		SafeHtmlBuilder rootBuilder = new SafeHtmlBuilder();
		TreeItem item = new TreeItem(rootBuilder.appendEscaped(display).toSafeHtml());
		if(parent!=null){
			parent.addItem(item);
		}else{
			tree.addItem(item);
		}
		
		return item;
	}

}
