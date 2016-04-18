package com.duggan.workflow.client.ui.admin;

import com.duggan.workflow.client.event.ProcessChildLoadedEvent;
import com.duggan.workflow.client.event.ProcessChildLoadedEvent.ProcessChildLoadedHandler;
import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.ui.admin.process.ProcessPresenter;
import com.duggan.workflow.client.ui.admin.processes.ProcessListingPresenter;
import com.duggan.workflow.client.ui.component.BulletListPanel;
import com.duggan.workflow.client.ui.events.ProcessSelectedEvent;
import com.duggan.workflow.client.ui.events.ProcessSelectedEvent.ProcessSelectedHandler;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.ProcessDef;
import com.duggan.workflow.shared.model.Status;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.Tab;
import com.gwtplatform.mvp.client.TabData;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

public class TabPanel extends AbstractTabPanel implements
		ProcessSelectedHandler, ProcessChildLoadedHandler {

	private static TabPanelUiBinder uiBinder = GWT
			.create(TabPanelUiBinder.class);

	interface TabPanelUiBinder extends UiBinder<Widget, TabPanel> {
	}

	@UiField
	BulletListPanel linksPanel;
	@UiField
	HTMLPanel tabContent;
	@Inject
	PlaceManager placeManager;
	@UiField
	SpanElement spanTitle;
	@UiField
	SpanElement iconTitle;

	@UiField
	Element divDefaultHeader;
	@UiField
	Element divProcessHeader;
	private boolean isProcessListing;

	@UiField
	AnchorElement aBCProcessName;
	@UiField
	AnchorElement aBCDropdown;
	@UiField
	AnchorElement aBCPreview;
	@UiField
	AnchorElement aBCFormBuilder;
	@UiField
	AnchorElement aBCTriggers;
	@UiField
	AnchorElement aBCOutputs;
	@UiField
	AnchorElement aBCProcessConfig;

	@UiField
	AnchorElement aPillPreview;
	@UiField
	AnchorElement aPillFormBuilder;
	@UiField
	AnchorElement aPillTriggers;
	@UiField
	AnchorElement aPillOutputDocs;
	@UiField
	AnchorElement aPillProcessConf;

	@UiField
	Element ulNavPills;

	@Inject
	public TabPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		aBCProcessName.setId("drop5");
	}

	public void setPanelContent(IsWidget panelContent) {
		tabContent.clear();
		if (panelContent != null) {
			tabContent.add(panelContent);
		}
	}

	@Override
	protected Tab createNewTab(TabData tabData) {
		IconTabItem item = new IconTabItem(tabData);
		return item;
	}

	@Override
	public BulletListPanel getLinksPanel() {
		return linksPanel;
	}

	@Override
	public void setActiveTab(Tab tab) {
		super.setActiveTab(tab);

		iconTitle.setClassName(((IconTabItem) tab).getTabData().getIconStyle());
		spanTitle.setInnerText(((AbstractTabItem) tab).getTabData().getLabel());

		// Window.alert("Active Tab set "+tab.getText());
		boolean isProcessesTab = tab.getText().equals("Processes");
//		showDefaultHeader(!isProcessesTab
//				|| (isProcessesTab && isProcessListing));
		showDefaultHeader(true);
	}

	/**
	 * Process links
	 */
	@Override
	protected void onLoad() {
		super.onLoad();
		AppContext.getEventBus().addHandler(ProcessChildLoadedEvent.getType(),
				this);
		AppContext.getEventBus().addHandler(ProcessSelectedEvent.getType(),
				this);
	}

	@Override
	public void onProcessSelected(ProcessSelectedEvent event) {
		ProcessDef process = event.getProcessDef();
		if (process.getStatus() == Status.RUNNING) {
			aBCProcessName.getStyle().setColor("green");
		} else {
			aBCProcessName.getStyle().setColor("grey");
		}

		aBCProcessName.setInnerText(process.getName());
		PlaceManager manager = AppContext.getPlaceManager();

		aBCPreview.setHref("#"
				+ manager.buildHistoryToken(new PlaceRequest.Builder()
						.nameToken(NameTokens.processes)
						.with("a", ProcessPresenter.ACTION_PREVIEW)
						.with("p", process.getRefId()).build()));

		aBCProcessName.setHref(aBCPreview.getHref());

		aBCFormBuilder.setHref("#"
				+ manager.buildHistoryToken(new PlaceRequest.Builder()
						.nameToken(NameTokens.formbuilder)
						.with("p", process.getRefId()).build()));

		aBCTriggers.setHref("#"
				+ manager.buildHistoryToken(new PlaceRequest.Builder()
						.nameToken(NameTokens.triggers)
						.with("p", process.getRefId()).build()));

		aBCOutputs.setHref("#"
				+ manager.buildHistoryToken(new PlaceRequest.Builder()
						.nameToken(NameTokens.outputdocs)
						.with("p", process.getRefId()).build()));

		aBCProcessConfig.setHref("#"
				+ manager.buildHistoryToken(new PlaceRequest.Builder()
						.nameToken(NameTokens.processes)
						.with("a", ProcessPresenter.ACTION_CONFIG)
						.with("p", process.getRefId()).build()));

		aPillPreview.setHref("#"
				+ manager.buildHistoryToken(new PlaceRequest.Builder()
						.nameToken(NameTokens.processes)
						.with("a", ProcessPresenter.ACTION_PREVIEW)
						.with("p", process.getRefId()).build()));

		aPillFormBuilder.setHref("#"
				+ manager.buildHistoryToken(new PlaceRequest.Builder()
						.nameToken(NameTokens.formbuilder)
						.with("p", process.getRefId()).build()));

		aPillTriggers.setHref("#"
				+ manager.buildHistoryToken(new PlaceRequest.Builder()
						.nameToken(NameTokens.triggers)
						.with("p", process.getRefId()).build()));

		aPillOutputDocs.setHref("#"
				+ manager.buildHistoryToken(new PlaceRequest.Builder()
						.nameToken(NameTokens.outputdocs)
						.with("p", process.getRefId()).build()));

		aPillProcessConf.setHref("#"
				+ manager.buildHistoryToken(new PlaceRequest.Builder()
						.nameToken(NameTokens.processes)
						.with("a", ProcessPresenter.ACTION_CONFIG)
						.with("p", process.getRefId()).build()));
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void onProcessChildLoaded(ProcessChildLoadedEvent event) {
		PresenterWidget child = event.getChild();

		if (child instanceof ProcessListingPresenter) {
			isProcessListing = true;
		} else {
			ProxyPlace proxy = (ProxyPlace) ((Presenter) child).getProxy();
			String name = "";
			disableAll();
			ulNavPills.addClassName("hide");
			switch (proxy.getNameToken()) {
			case NameTokens.formbuilder:
				name = "Form Builder";
				setActive(aPillFormBuilder, true);
				break;
			case NameTokens.triggers:
				name = "Triggers";
				setActive(aPillTriggers, true);
				break;
			case NameTokens.processes:
				String config = event.getConfig();
				if (config == null) {
					config = ProcessPresenter.ACTION_PREVIEW;
				}

				switch (config) {
				case ProcessPresenter.ACTION_CONFIG:
					name = "Process Config";
					setActive(aPillProcessConf, true);
					break;
				default:
					name = "Preview";
					setActive(aPillPreview, true);
					ulNavPills.removeClassName("hide");
					break;
				}

				break;
			case NameTokens.outputdocs:
				name = "Output documents";
				setActive(aPillOutputDocs, true);
				break;
			}

			aBCDropdown.setInnerHTML(name + "<b class=\"caret\"></b>");
			isProcessListing = false;
		}

		showDefaultHeader(false || isProcessListing);
	}

	private void disableAll() {
		setActive(aPillPreview, false);
		setActive(aPillFormBuilder, false);
		setActive(aPillTriggers, false);
		setActive(aPillOutputDocs, false);
		setActive(aPillProcessConf, false);
	}

	private void setActive(AnchorElement target, boolean isActive) {
		if (target != null) {
			Element parent = target.getParentElement();
			if (parent.getNodeName().equals("LI")) {
				if (isActive) {
					parent.addClassName("active");
				} else {
					parent.removeClassName("active");
				}
			}
		}
	}

	private void showDefaultHeader(boolean show) {
		if (show) {
			divDefaultHeader.removeClassName("hide");
			divProcessHeader.addClassName("hide");
		} else {
			divDefaultHeader.addClassName("hide");
			divProcessHeader.removeClassName("hide");
		}
	}

}
