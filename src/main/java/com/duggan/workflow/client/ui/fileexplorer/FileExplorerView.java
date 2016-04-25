package com.duggan.workflow.client.ui.fileexplorer;

import static com.duggan.workflow.client.ui.resources.ICONS.INSTANCE;

import java.util.List;

import javax.inject.Inject;

import com.duggan.workflow.client.event.CheckboxSelectionEvent;
import com.duggan.workflow.client.model.UploadContext;
import com.duggan.workflow.client.ui.component.ActionLink;
import com.duggan.workflow.client.ui.component.Checkbox;
import com.duggan.workflow.client.ui.events.FileSelectedEvent;
import com.duggan.workflow.client.ui.images.ImageResources;
import com.duggan.workflow.client.ui.upload.attachment.ShowAttachmentEvent;
import com.duggan.workflow.client.ui.util.DateUtils;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.Attachment;
import com.duggan.workflow.shared.model.TreeType;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.IconProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.tree.Tree;
import com.sencha.gxt.widget.core.client.tree.TreeSelectionModel;

class FileExplorerView extends ViewImpl implements FileExplorerPresenter.MyView {
	interface Binder extends UiBinder<Widget, FileExplorerView> {
	}

	@UiField
	FlexTable fileTable;
	
	@UiField SpanElement spnNoAttachments;

	@UiField
	Tree<Attachment, String> fileTree;
	@UiField
	Tree<Attachment, String> processTree;
	@UiField
	Tree<Attachment, String> userTree;

	@UiField
	Anchor aFileTree;
	@UiField
	Anchor aProcessesTree;
	@UiField
	Anchor aUsersTree;

	@Inject
	FileExplorerView(Binder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
		userTree.setWidth("100%");
		processTree.setWidth("100%");

		setHeaders(fileTable);

		aFileTree.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				event.preventDefault();
				showTab(aFileTree.getElement());
			}
		});

		aUsersTree.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				event.preventDefault();
				showTab(aUsersTree.getElement());
			}
		});

		aProcessesTree.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				event.preventDefault();
				showTab(aProcessesTree.getElement());
			}
		});
	}

	public static native void showTab(Element anchor)/*-{
														$wnd.$(anchor).tab('show');
														}-*/;

	@Override
	public void bindAttachments(List<Attachment> attachments) {
		fileTable.removeAllRows();
		if(attachments.isEmpty()){
			spnNoAttachments.addClassName("hide");
		}else{
			spnNoAttachments.removeClassName("hide");
		}
		setHeaders(fileTable);

		int i = 1;
		for (Attachment file : attachments) {
			int j = 0;
			Checkbox box = new Checkbox(file);
			box.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
				@Override
				public void onValueChange(ValueChangeEvent<Boolean> event) {
					Object model = ((Checkbox) (event.getSource())).getModel();
					AppContext.fireEvent(new CheckboxSelectionEvent(model,
							event.getValue()));
				}
			});

			fileTable.setWidget(i, j++, box);

			Image fileImg = new Image();
			fileImg.setResource(getImage(file.getName()));
			fileTable.setWidget(i, j++, fileImg);

			ActionLink link = new ActionLink(file);
			link.setText(file.getName());
			link.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					Attachment file = (Attachment) ((ActionLink) event
							.getSource()).getModel();
					String href = getHref(file);
					AppContext.fireEvent(new ShowAttachmentEvent(href, file
							.getName()));
				}
			});

			fileTable.setWidget(i, j++, link);
			fileTable.setWidget(i, j++, new InlineLabel(
					DateUtils.MONTHDAYFORMAT.format(file.getCreated())));
			fileTable.setWidget(i, j++, new InlineLabel(file.getCreatedBy()
					.getFullName()));
			fileTable
					.setWidget(i, j++, new InlineLabel(file.getType().name()));
			fileTable.setWidget(i, j++, new InlineLabel(file.getProcessName()+" ["+file.getDocStatus()+"]"));
			fileTable.setWidget(i, j++, new InlineLabel(file.getCaseNo()));

			++i;
		}
	}

	private ImageResource getImage(String name) {
		if (name.endsWith(".pdf")) {
			return ImageResources.IMAGES.pdf();
		} else if (name.endsWith(".docx") || name.endsWith(".doc")) {
			return ImageResources.IMAGES.doc();
		} else if (name.endsWith(".odt")) {
			return ImageResources.IMAGES.odt();
		} else if (name.endsWith(".ods")) {
			return ImageResources.IMAGES.ods();
		} else if (name.endsWith(".csv")) {
			return ImageResources.IMAGES.csv();
		} else if (name.endsWith(".xls") || name.endsWith(".xlsx")) {
			return ImageResources.IMAGES.xls();
		} else if (name.endsWith(".png") || name.endsWith(".jpg")
				|| name.endsWith(".jpeg") || name.endsWith(".gif")) {
			return ImageResources.IMAGES.img();
		} else if (name.endsWith(".txt") || name.endsWith(".text")) {
			return ImageResources.IMAGES.txt();
		} else {
			return ImageResources.IMAGES.file();
		}
	}

	private void setHeaders(FlexTable table) {
		int j = 0;
		table.setWidget(0, j++, new HTMLPanel("<strong>#</strong>"));
		table.getFlexCellFormatter().setWidth(0, (j - 1), "20px");
		table.setWidget(0, j++, new HTMLPanel("<strong></strong>"));

		table.setWidget(0, j++, new HTMLPanel("<strong>File Name</strong>"));

		table.setWidget(0, j++, new HTMLPanel("<strong>Modified</strong>"));
		// table.getFlexCellFormatter().setWidth(0, (j - 1), "100px");
		table.setWidget(0, j++, new HTMLPanel("<strong>Owner</strong>"));
		// table.getFlexCellFormatter().setWidth(0, (j - 1), "110px");
		table.setWidget(0, j++, new HTMLPanel("<strong>Type</strong>"));
		// table.getFlexCellFormatter().setWidth(0, (j - 1), "100px");	
		table.setWidget(0, j++, new HTMLPanel("<strong>Process</strong>"));
		// table.getFlexCellFormatter().setWidth(0, (j - 1), "100px");
		table.setWidget(0, j++, new HTMLPanel("<strong>Case</strong>"));
		// table.getFlexCellFormatter().setWidth(0, (j - 1), "200px");
	}

	private String getHref(Attachment attachment) {

		UploadContext context = new UploadContext("getreport");
		context.setContext("attachmentId", attachment.getId() + "");
		context.setContext("ACTION", "GETATTACHMENT");
		String url = context.toUrl();

		String href = (AppContext.getBaseUrl() + "/" + url);

		return href;
	}

	@UiFactory
	public Tree<Attachment, String> createTree() {

		// ModelProperties props = GWT.create(ModelProperties.class);
		final TreeStore<Attachment> sourceStore = new TreeStore<Attachment>(
				ModelProperties.key);

		final Tree<Attachment, String> sourceTree = new Tree<Attachment, String>(
				sourceStore, ModelProperties.name) {
			@Override
			public boolean isLeaf(Attachment model) {
				return model.getChildren() == null
						|| model.getChildren().isEmpty();
			}
		};
		
		sourceTree.getStyle().setLeafIcon(INSTANCE.folder());
		sourceTree.setBorders(false);

		sourceTree.getStyle().setNodeCloseIcon(INSTANCE.folder());
		sourceTree.getStyle().setNodeOpenIcon(INSTANCE.folderOpen());
		sourceTree.getElement().getStyle().setBackgroundColor("white");

		sourceTree.setIconProvider(new IconProvider<Attachment>() {

			@Override
			public ImageResource getIcon(Attachment model) {

				if (model.isDirectory()) {
					return null;
				}

				String name = model.getName();
				if (name.endsWith(".pdf")) {
					return ImageResources.IMAGES.pdf();
				} else if (name.endsWith(".docx") || name.endsWith(".doc")) {
					return ImageResources.IMAGES.doc();
				} else if (name.endsWith(".odt")) {
					return ImageResources.IMAGES.odt();
				} else if (name.endsWith(".ods")) {
					return ImageResources.IMAGES.ods();
				} else if (name.endsWith(".csv")) {
					return ImageResources.IMAGES.csv();
				} else if (name.endsWith(".xls") || name.endsWith(".xlsx")) {
					return ImageResources.IMAGES.xls();
				} else if (name.endsWith(".png") || name.endsWith(".jpg")
						|| name.endsWith(".jpeg") || name.endsWith(".gif")) {
					return ImageResources.IMAGES.img();
				} else if (name.endsWith(".txt") || name.endsWith(".text")) {
					return ImageResources.IMAGES.txt();
				} else {
					return ImageResources.IMAGES.file();
				}

			}
		});

		sourceTree.getSelectionModel().addSelectionHandler(new SelectionHandler<Attachment>() {
			@Override
			public void onSelection(SelectionEvent<Attachment> event) {
				TreeSelectionModel<Attachment> selectionModel =(TreeSelectionModel<Attachment>) event.getSource(); 
				Attachment a = event.getSelectedItem();
//				if(a.hasChildren()){
//					return;
//				}
				if(selectionModel.getTree()==fileTree){
					AppContext.fireEvent(new FileSelectedEvent(TreeType.FILES, a.getRefId()));
				}else if(selectionModel.getTree()==processTree){
					AppContext.fireEvent(new FileSelectedEvent(TreeType.PROCESSES,a.getProcessRefId()));
				}else if(selectionModel.getTree()==userTree){
					AppContext.fireEvent(new FileSelectedEvent(TreeType.USERS, 
							a.getCreatedBy().getUserId()));
				}
				
			}
		});


		return sourceTree;
	}

	public interface ModelProperties extends PropertyAccess<Attachment> {

		public final ModelKeyProvider<Attachment> key = new ModelKeyProvider<Attachment>() {
			@Override
			public String getKey(Attachment item) {
				return item.getRefId();
			}
		};

		public final ValueProvider<Attachment, String> name = new ValueProvider<Attachment, String>() {

			@Override
			public String getValue(Attachment object) {
				return object.getName()+"( "+object.getChildCount()+")";
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

	public void setFolders(TreeType type, List<Attachment> folders) {

		Tree<Attachment, String> tree = null;
		switch (type) {
		case FILES:
			tree = fileTree;
			break;
		case PROCESSES:
			tree = processTree;
			break;
		case USERS:
			tree = userTree;
			break;
		}

		tree.getStore().clear();
		// System.err.println("#############>>>>>>>>>>>>>>> onreset1 - setfolders");
		assert folders != null;

		Attachment attachment = new Attachment();
		attachment.setName("Documents");
		attachment.setId(-1L);
		attachment.setDirectory(true);
		attachment.setChildren(folders);
		tree.getStore().addSubTree(0, folders);

	}

	@Override
	public Tree<Attachment, String> getTree() {
		return fileTree;
	}

	@Override
	public HasClickHandlers getFilesLink() {
		return aFileTree;
	}

	@Override
	public HasClickHandlers getProcessesLink() {
		return aProcessesTree;
	}

	@Override
	public HasClickHandlers getUserLink() {
		return aUsersTree;
	}
	
	@Override
	public void onLoad() {
		int clientWidth = Window.getClientWidth();
//		int clientHeight= Window.getClientHeight();
		double sideBarNav = Window.getClientWidth()*0.12;
		double fileTreeNavWidthAndMargins = 227+16;
		double tableDivMargins = 30;
		double attachmentTableWidth = clientWidth - (sideBarNav+fileTreeNavWidthAndMargins+tableDivMargins);
		
		fileTable.setWidth(attachmentTableWidth+"px");
	}

}