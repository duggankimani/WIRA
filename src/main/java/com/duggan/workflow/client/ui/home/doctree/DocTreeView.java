package com.duggan.workflow.client.ui.home.doctree;

import static com.duggan.workflow.client.ui.resources.ICONS.INSTANCE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.duggan.workflow.client.model.UploadContext;
import com.duggan.workflow.client.ui.images.ImageResources;
import com.duggan.workflow.client.ui.resources.ICONS;
import com.duggan.workflow.client.ui.upload.attachment.ShowAttachmentEvent;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.Attachment;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.IconProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.event.ShowContextMenuEvent;
import com.sencha.gxt.widget.core.client.event.ShowContextMenuEvent.ShowContextMenuHandler;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import com.sencha.gxt.widget.core.client.tree.Tree;

public class DocTreeView extends ViewImpl implements DocTreePresenter.IDocTreeView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, DocTreeView> {
	}

	@UiField Tree<Attachment, String> tree;
	
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
		if(attachments==null){
			return;
		}
		
		Map<String, List<String>> docTypeDocNameList = new HashMap<String, List<String>>();
		Map<String, ArrayList<Attachment>> docNameAttachmentList = new HashMap<String, ArrayList<Attachment>>();
		
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
			ArrayList<Attachment> attachmentsForDoc = docNameAttachmentList.get(subject);
			if(attachmentsForDoc==null){
				attachmentsForDoc = new ArrayList<Attachment>();
				docNameAttachmentList.put(subject, attachmentsForDoc);
			}
			attachmentsForDoc.add(attachment);
			
		}
		
		long i=0;
		ArrayList<Attachment> list = new ArrayList<Attachment>();
		for(String docType: docTypeDocNameList.keySet()){
			
			Attachment rootNode = createAttachment(docType);
			rootNode.setId(i+=1);
			list.add(rootNode);
			
			List<String> subjects = docTypeDocNameList.get(docType);
			
			for(String subject: subjects){
				Attachment child = createAttachment(subject);
				child.setId(i+=1);
					
				rootNode.addChild(child);
				ArrayList<Attachment> docAttachments = docNameAttachmentList.get(subject);
				child.setChildren(docAttachments);
	
			}
			
		}
		
		setFolders(list);
	}

	private Attachment createAttachment(String subject) {
		Attachment a = new Attachment();
		a.setDirectory(true);
		a.setName(subject);
		return a;
	}

	private String getHref(Attachment attachment) {
		
		UploadContext context = new UploadContext("getreport");
		context.setContext("attachmentId", attachment.getId()+"");
		context.setContext("ACTION", "GETATTACHMENT");
		String url = context.toUrl();
	
		String href = (AppContext.getBaseUrl()+"/"+url);
		
		return href;
	}

	@UiFactory
	public Tree<Attachment, String> createTree(){
		
		//ModelProperties props = GWT.create(ModelProperties.class);
		final TreeStore<Attachment> sourceStore = new TreeStore<Attachment>(ModelProperties.key);

	    final Tree<Attachment, String> sourceTree = new Tree<Attachment, String>(sourceStore, ModelProperties.name){
	    	@Override
	    	public boolean isLeaf(Attachment model) {
	    	
	    		return !model.isDirectory();
	    	}
	    };
	    //sourceTree.setHeight(300);
	    sourceTree.setWidth(160);
	    
	    sourceTree.getStyle().setLeafIcon(INSTANCE.text());
	    sourceTree.setBorders(false);
	    
	    sourceTree.getStyle().setNodeCloseIcon(INSTANCE.folder());
	    sourceTree.getStyle().setNodeOpenIcon(INSTANCE.folderOpen());
	    sourceTree.getElement().getStyle().setBackgroundColor("white");
	    
	    sourceTree.setIconProvider(new IconProvider<Attachment>() {
			
			@Override
			public ImageResource getIcon(Attachment model) {
				
				if(model.isDirectory()){
					return null;
				}
				
	    		String name = model.getName();
	    		if(name.endsWith(".pdf")){
	    			return ImageResources.IMAGES.pdf();
	    		}else if(name.endsWith(".docx") || name.endsWith(".doc")){
	    			return ImageResources.IMAGES.doc();
	    		}else if(name.endsWith(".odt")){
	    			return ImageResources.IMAGES.odt();
	    		}else if(name.endsWith(".ods")){
	    			return ImageResources.IMAGES.ods();
	    		}else if(name.endsWith(".csv")){
	    			return ImageResources.IMAGES.csv();
	    		}
	    		else if(name.endsWith(".xls") || name.endsWith(".xlsx")){
	    			return ImageResources.IMAGES.xls();
	    		}
	    		else if(name.endsWith(".png") || name.endsWith(".jpg") 
	    				|| name.endsWith(".jpeg") ||name.endsWith(".gif")){
	    			return ImageResources.IMAGES.img();
	    		}else if(name.endsWith(".txt") ||name.endsWith(".text")){
	    			return ImageResources.IMAGES.txt();
	    		}
	    		else{
	    			return ImageResources.IMAGES.file();
	    		}
				
			}
		});
	    final Menu contextMenu = new Menu();  
	    contextMenu.setWidth(140);  
	      
	    MenuItem download = new MenuItem();  
	    download.setText("View");  
	    download.setIcon(ICONS.INSTANCE.download());  
	    contextMenu.add(download);  
	    sourceTree.setContextMenu(contextMenu);
	    
	    contextMenu.addSelectionHandler(new SelectionHandler<Item>() {
			
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				Attachment a = tree.getSelectionModel().getSelectedItem();
				String href = getHref(a);
				
				AppContext.fireEvent(
						new ShowAttachmentEvent(href, a.getName()));
				//Window.open(href, "_blank", null);
			}
		});
	    
	    sourceTree.addShowContextMenuHandler(new ShowContextMenuHandler() {
			
			@Override
			public void onShowContextMenu(ShowContextMenuEvent event) {
				Attachment a = tree.getSelectionModel().getSelectedItem();
				if(a.isDirectory()){
					contextMenu.hide();
				}
			}
		});
	 
	    return sourceTree;
	}
	

	public interface ModelProperties extends PropertyAccess<Attachment>{
		
		public final ModelKeyProvider<Attachment> key = new ModelKeyProvider<Attachment>() {
			@Override
			public String getKey(Attachment item) {
				//System.err.println("id=="+item.getId()+" - "+item.getName());
				return item.getId()+"";
			}
		};
		
		public final ValueProvider<Attachment, String> name = new ValueProvider<Attachment, String>() {

			@Override
			public String getValue(Attachment object) {
				return object.getName();
			}

			@Override
			public void setValue(Attachment object, String value) {
				object.setName(value);
			}

			@Override
			public String getPath() {
				return "name";
			}
		};
	}

	public void setFolders(ArrayList<Attachment> folders) {
		tree.getStore().clear();
		//System.err.println("#############>>>>>>>>>>>>>>> onreset1 - setfolders");
		assert folders!=null;
		
		Attachment attachment = new Attachment();
		attachment.setName("Documents");
		attachment.setId(-1L);
		attachment.setDirectory(true);
		attachment.setChildren(folders);
		
//		if(tree.getStore().getChild(0)!=null){
//			Attachment parent = tree.getStore().getChild(0);
//			parent.setChildren(folders);
//			tree.getStore().add(parent,folders);
//		}else{
			tree.getStore().addSubTree(0,Arrays.asList(attachment));
		//}
		
		
	}

	@Override
	public Tree<Attachment, String> getTree() {
		return tree;
	}
}
