package com.duggan.workflow.client.ui.addDoc;

import static com.duggan.workflow.client.ui.addDoc.DocumentPopupPresenter.DOCITEM_SLOT;
import static com.duggan.workflow.client.ui.resources.ICONS.INSTANCE;

import java.util.List;

import com.duggan.workflow.client.ui.events.CreateDocumentEvent;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.DocumentType;
import com.duggan.workflow.shared.model.IsProcessDisplay;
import com.duggan.workflow.shared.model.ProcessCategory;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;
import com.sencha.gxt.widget.core.client.tree.Tree;

public class DocumentPopupView extends ViewImpl implements
		DocumentPopupPresenter.MyView {

	private final Widget widget;
	
	@UiField Tree<IsProcessDisplay, String> tree;
	
	@UiField HTMLPanel panelDocTypes;
	
	public interface Binder extends UiBinder<Widget, DocumentPopupView> {
	}

	@Inject
	public DocumentPopupView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		
		//tree.getStore().add
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	@Override
	public void setInSlot(Object slot, IsWidget content) {
		if(slot == DOCITEM_SLOT){
			panelDocTypes.clear();
			if(content!=null){
				panelDocTypes.add(content);
			}
		}else{
			super.setInSlot(slot, content);
		}
		
	}
	
	@Override
	public void addToSlot(Object slot, IsWidget content) {
		if(slot == DOCITEM_SLOT){			
			if(content!=null){
				panelDocTypes.add(content);
			}
		}else{
			super.addToSlot(slot, content);
		}
		
	}
	
	@Override
	public void addSeparator(String category) {
		if(category==null){
			category= new String("");
		}
		
		HTML html = new HTML("<div>"+
		"<span style=\"padding-left:8px; font-size:12px;font-family:Arial Unicode MS,Arial,sans-serif;\">"+category+"</span>"+
		"<li style=\"margin:2px 0px;\" role=\"separator\" class=\"divider\"></li>"+
		"</div>");
		panelDocTypes.add(html);
	}

	@Override
	public void addContent(String content) {
		HTML html = new HTML("<div style=\"margin-left:8px;\">"+
				"<span class=\"label label-info\" "
				+ "style=\"padding-left:8px; font-size:12px;font-family:Arial Unicode MS,Arial,sans-serif;\">"+
				content+"</span>"+
				"</div>");
				panelDocTypes.add(html);
	}

	@Override
	public void setData(List<ProcessCategory> categories) {
		if(categories==null){
			return;
		}
		//tree.getStore().addSubTree(0,categories);
	}
	
	@UiFactory
	public Tree<IsProcessDisplay, String> createTree(){
		
		final TreeStore<IsProcessDisplay> sourceStore = new TreeStore<IsProcessDisplay>(ModelProperties.key);

	    final Tree<IsProcessDisplay, String> sourceTree = new Tree<IsProcessDisplay, String>(sourceStore, ModelProperties.name){
	    	@Override
	    	public boolean isLeaf(IsProcessDisplay model) {
	    	
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
	    
//	    sourceTree.setIconProvider(new IconProvider<IsProcessDisplay>() {
//			
//			@Override
//			public ImageResource getIcon(IsProcessDisplay model) {
//				
//				if(model instanceof ProcessCategory){
//					return null;
//				}
//				
//	    		String name = model.getName();
//	    		if(name.endsWith(".pdf")){
//	    			return ImageResources.IMAGES.pdf();
//	    		}else if(name.endsWith(".docx") || name.endsWith(".doc")){
//	    			return ImageResources.IMAGES.doc();
//	    		}else if(name.endsWith(".odt")){
//	    			return ImageResources.IMAGES.odt();
//	    		}else if(name.endsWith(".ods")){
//	    			return ImageResources.IMAGES.ods();
//	    		}else if(name.endsWith(".csv")){
//	    			return ImageResources.IMAGES.csv();
//	    		}
//	    		else if(name.endsWith(".xls") || name.endsWith(".xlsx")){
//	    			return ImageResources.IMAGES.xls();
//	    		}
//	    		else if(name.endsWith(".png") || name.endsWith(".jpg") 
//	    				|| name.endsWith(".jpeg") ||name.endsWith(".gif")){
//	    			return ImageResources.IMAGES.img();
//	    		}else if(name.endsWith(".txt") ||name.endsWith(".text")){
//	    			return ImageResources.IMAGES.txt();
//	    		}
//	    		else{
//	    			return ImageResources.IMAGES.file();
//	    		}
//				
//			}
//		});
	    
	    tree.getSelectionModel().addSelectionChangedHandler(new SelectionChangedHandler<IsProcessDisplay>() {
	    	@Override
	    	public void onSelectionChanged(
	    			SelectionChangedEvent<IsProcessDisplay> event) {
	    		IsProcessDisplay model = tree.getSelectionModel().getSelectedItem();
				if(model.isDirectory()){
					return;
				}
				
				AppContext.fireEvent(new CreateDocumentEvent((DocumentType)model));
	    	}
		});
	    
	
	    return sourceTree;
	}
	

	public interface ModelProperties extends PropertyAccess<IsProcessDisplay>{
		
		public final ModelKeyProvider<IsProcessDisplay> key = new ModelKeyProvider<IsProcessDisplay>() {
			@Override
			public String getKey(IsProcessDisplay item) {
				return item.getName()+"";
			}
		};
		
		public final ValueProvider<IsProcessDisplay, String> name = new ValueProvider<IsProcessDisplay, String>() {

			@Override
			public String getValue(IsProcessDisplay object) {
				return object.getName();
			}

			@Override
			public void setValue(IsProcessDisplay object, String value) {
				//object.setName(value);
			}

			@Override
			public String getPath() {
				return "name";
			}
		};
	}


}
