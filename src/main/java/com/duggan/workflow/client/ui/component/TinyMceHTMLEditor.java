package com.duggan.workflow.client.ui.component;

import com.duggan.workflow.client.util.AppContext;
import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

public class TinyMceHTMLEditor extends Composite implements HasText {

	private static TinyMceHTMLEditorUiBinder uiBinder = GWT
			.create(TinyMceHTMLEditorUiBinder.class);

	interface TinyMceHTMLEditorUiBinder extends
			UiBinder<Widget, TinyMceHTMLEditor> {
	}

	@UiField
	HTMLPanel container;

	TextArea txtEditor = new TextArea();
	private String id;

	public TinyMceHTMLEditor() {
		initWidget(uiBinder.createAndBindUi(this));
		id = HTMLPanel.createUniqueId();
		txtEditor.getElement().setId(id);
		txtEditor.setVisibleLines(30);
		container.add(txtEditor);
	}

	public TinyMceHTMLEditor(String html) {
		this();
		txtEditor.setText(html);
	}

	public void setHeight(int height) {
		setHeight(height + "px");
	}


	public boolean loadLibrary() {
		String scriptUrl = AppContext.getBaseUrl()
				+ "/js/tinymce/tinymce.min.js";
		JavaScriptObject scriptInstance = ScriptInjector.fromUrl(scriptUrl)
				.setWindow(ScriptInjector.TOP_WINDOW)
				.setCallback(new Callback<Void, Exception>() {

					@Override
					public void onFailure(Exception reason) {
						Window.alert("TinyMCE failed to load.");
					}

					@Override
					public void onSuccess(Void result) {
						init(id);
						setWidth("100%");
						setTextAreaToTinyMCE(id);
						GWT.log("Success init Loading JS!!! Now setting txtArea - "+id+" = "+txtEditor.getElement().getId());
						
//						GWT.log("Success setId JS!!!");
						focusMCE(id);
						
					}
				}).inject();
		return (scriptInstance != null);
	}

	protected native void init(String id)/*-{
	$wnd.tinyMCE.init({
        theme : "modern",
        skin : "lightgray",
        mode : "textareas",
        plugins : "code,pagebreak,layer,table,save,image,hr,insertdatetime,preview,media,searchreplace,print,contextmenu,paste,directionality,fullscreen,noneditable,visualchars,nonbreaking,template",

        // Theme options
        theme_advanced_buttons1 : "save,newdocument,|,bold,italic,underline,strikethrough,|,justifyleft,justifycenter,justifyright,justifyfull,|,styleselect,formatselect,fontselect,fontsizeselect",
        theme_advanced_buttons2 : "cut,copy,paste,pastetext,pasteword,|,search,replace,|,bullist,numlist,|,outdent,indent,blockquote,|,undo,redo,|,link,unlink,anchor,image,cleanup,help,code,|,insertdate,inserttime,preview,|,forecolor,backcolor",
        theme_advanced_buttons3 : "tablecontrols,|,hr,removeformat,visualaid,|,sub,sup,|,charmap,emotions,iespell,media,advhr,|,print,|,ltr,rtl,|,fullscreen",
        theme_advanced_buttons4 : "insertlayer,moveforward,movebackward,absolute,|,styleprops,|,cite,abbr,acronym,del,ins,attribs,|,visualchars,nonbreaking,template,pagebreak,|,code",
        theme_advanced_toolbar_location : "top",
        theme_advanced_toolbar_align : "left",
        theme_advanced_statusbar_location : "bottom",
        theme_advanced_resizing : true,
        entity_encoding : "raw",
        add_unload_trigger : false,
        remove_linebreaks : false,
        button_tile_map : true,
        auto_focus : id
    });
    
    // prevent Bootstrap from hijacking TinyMCE modal focus    
	$wnd.jQuery($doc).on('focusin', function(e) {
        if ($wnd.jQuery(e.target).closest(".mce-window").length) {
            e.stopImmediatePropagation();
        }
    });
}-*/;

	
	/**
	 * getID() -
	 *
	 * @return the MCE element's ID
	 */
	public String getID() {
		return id;
	}

	protected static native String getEditorContents(String elementId) /*-{
																		return $wnd.tinyMCE.get(elementId).getContent();
																		}-*/;

	protected static native void setEditorContents(String elementId, String html) /*-{
																					$wnd.tinyMCE.execInstanceCommand(
																					elementId, 'mceSetContent', false, html, false);
																					}-*/;

	public void setText(String text) {
		setEditorContents(id, text);
	}

	public String getText() {
		return getEditorContents(id);
	}

	public void setEnabled(boolean enabled) {
		txtEditor.setEnabled(enabled);
	}

	/**
	 * @see com.google.gwt.user.client.ui.Widget#onLoad()
	 */
	protected void onLoad() {
		super.onLoad();
		loadLibrary();
//		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
//			
//			@Override
//			public void execute() {
//				setWidth("100%");
//				setTextAreaToTinyMCE(id);
//				focusMCE(id);
//			}
//		});
	}
	
	@Override
	protected void onUnload() {
		super.onUnload();
		unload();
	}

	/**
	 * focusMCE() -
	 *
	 * Use this to set the focus to the MCE area
	 * 
	 * @param id
	 *            - the element's ID
	 */
	//$wnd.tinyMCE.execCommand('mceFocus', true, id);
	protected native void focusMCE(String id) /*-{
												$wnd.tinyMCE.EditorManager.get(id).focus();
												}-*/;

	/**
	 * resetMCE() -
	 *
	 * Use this if reusing the same MCE element, but losing focus
	 */
	public native void resetMCE() /*-{
									$wnd.tinyMCE.execCommand('mceResetDesignMode', true);
									}-*/;

	/**
	 * unload() -
	 *
	 * Unload this MCE editor instance from active memory. I use this in the
	 * onHide function of the containing widget. This helps to avoid problems,
	 * especially when using tabs.
	 */
	public void unload() {
		unloadMCE(id);
	}

	/**
	 * unloadMCE() -
	 *
	 * @param id
	 *            - The element's ID JSNI method to implement unloading the MCE
	 *            editor instance from memory
	 */
	protected native void unloadMCE(String id) /*-{
												$wnd.tinyMCE.execCommand('mceFocus', false, id);
												$wnd.tinyMCE.execCommand('mceRemoveEditor', false, id);
												}-*/;

	/**
	 * updateContent() -
	 *
	 * Update the internal referenced content. Use this if you programatically
	 * change the original text area's content (eg. do a clear)
	 * 
	 * @param id
	 *            - the ID of the text area that contains the content you wish
	 *            to copy
	 */
	protected native void updateContent(String id) /*-{
													$wnd.tinyMCE.activeEditor = $wnd.tinyMCE.get(id);
													$wnd.tinyMCE.activeEditor.setContent($wnd.document.getElementById(id).value);
													}-*/;

	/**
	 * getTextArea() -
	 *
	 */
	protected native void getTextData(String id) /*-{
													$wnd.tinyMCE.activeEditor = $wnd.tinyMCE.get(id);
													$wnd.tinyMCE.activeEditor.save();
													$wnd.tinyMCE.triggerSave();
													}-*/;

	/**
	 * encodeURIComponent() -
	 *
	 * Wrapper for the native URL encoding methods
	 * 
	 * @param text
	 *            - the text to encode
	 * @return the encoded text
	 */
	protected native String encodeURIComponent(String text) /*-{
															return encodeURIComponent(text);
															}-*/;

	/**
	 * setTextAreaToTinyMCE() -
	 *
	 * Change a text area to a tiny MCE editing field
	 * 
	 * @param id
	 *            - the text area's ID
	 */
	protected native void setTextAreaToTinyMCE(String id) /*-{
															$wnd.tinyMCE.execCommand('mceAddEditor', true, id);
															}-*/;

	/**
	 * removeMCE() -
	 *
	 * Remove a tiny MCE editing field from a text area
	 * 
	 * @param id
	 *            - the text area's ID
	 */
	public native void removeMCE(String id) /*-{
											$wnd.tinyMCE.execCommand('mceRemoveEditor', true, id);
											}-*/;

}
