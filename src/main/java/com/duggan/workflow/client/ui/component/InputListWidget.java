package com.duggan.workflow.client.ui.component;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.TextBox;

public class InputListWidget extends Composite {

	List<String> itemsSelected = new ArrayList<String>();
	FlowPanel panel = new FlowPanel();
	
    public InputListWidget() {    
        initWidget(panel);
    }

    @Override
    protected void onLoad() {
    	super.onLoad();
    	 // 2. Show the following element structure and set the last <div> to display: block
        /*
        <ul class="token-input-list-facebook">
            <li class="token-input-input-token-facebook">
                <input type="text" style="outline-color: -moz-use-text-color; outline-style: none; outline-width: medium;"/>
            </li>
        </ul>
        <div class="token-input-dropdown-facebook" style="display: none;"/>
         */
        final BulletListPanel list = new BulletListPanel();
        list.setStyleName("token-input-list-facebook");
        final BulletPanel item = new BulletPanel();
        item.setStyleName("token-input-input-token-facebook");
        final TextBox itemBox = new TextBox();
        itemBox.getElement().setAttribute("style", "outline-color: -moz-use-text-color; outline-style: none; outline-width: medium;");
        
        final SuggestBox box = new SuggestBox(SuggestField.getSuggestions(), itemBox);        
        box.getElement().setId("suggestion_box");
        item.add(box);
        list.add(item);

        // this needs to be on the itemBox rather than box, or backspace will get executed twice
        itemBox.addKeyDownHandler(new KeyDownHandler() {
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    // only allow manual entries with @ signs (assumed email addresses)
                    if (itemBox.getValue().contains("@"))
                        deselectItem(itemBox, list);
                }
                // handle backspace
                if (event.getNativeKeyCode() == KeyCodes.KEY_BACKSPACE) {
                    if ("".equals(itemBox.getValue().trim())) {
                        BulletPanel li = (BulletPanel) list.getWidget(list.getWidgetCount() - 2);
                        Paragraph p = (Paragraph) li.getWidget(0);
                        if (itemsSelected.contains(p.getText())) {
                            itemsSelected.remove(p.getText());
                            GWT.log("Removing selected item '" + p.getText() + "'", null);
                            GWT.log("Remaining: " + itemsSelected, null);
                        }
                        list.remove(li);
                        itemBox.setFocus(true);
                    }
                }
            }
        });

        box.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
            public void onSelection(SelectionEvent selectionEvent) {
                deselectItem(itemBox, list);
            }
        });

        panel.add(list);

        panel.getElement().setAttribute("onclick", "document.getElementById('suggestion_box').focus()");
        box.setFocus(true);
        /* Div structure after a few elements have been added:
            <ul class="token-input-list-facebook">
                <li class="token-input-token-facebook">
                    <p>What's New Scooby-Doo?</p>
                    <span class="token-input-delete-token-facebook">x</span>
                </li>
                <li class="token-input-token-facebook">
                    <p>Fear Factor</p>
                    <span class="token-input-delete-token-facebook">x</span>
                 </li>
                 <li class="token-input-input-token-facebook">
                     <input type="text" style="outline-color: -moz-use-text-color; outline-style: none; outline-width: medium;"/>
                 </li>
            </ul>
         */

    }
    private void deselectItem(final TextBox itemBox, final BulletListPanel list) {
        if (itemBox.getValue() != null && !"".equals(itemBox.getValue().trim())) {
            /** Change to the following structure:
             * <li class="token-input-token-facebook">
             * <p>What's New Scooby-Doo?</p>
             * <span class="token-input-delete-token-facebook">x</span>
             * </li>
             */

            final BulletPanel displayItem = new BulletPanel();
            displayItem.setStyleName("token-input-token-facebook");
            Paragraph p = new Paragraph(itemBox.getValue());

            displayItem.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent clickEvent) {
                    displayItem.addStyleName("token-input-selected-token-facebook");
                }
            });

            /** TODO: Figure out how to select item and allow deleting with backspace key
            displayItem.addKeyDownHandler(new KeyDownHandler() {
                public void onKeyDown(KeyDownEvent event) {
                    if (event.getNativeKeyCode() == KeyCodes.KEY_BACKSPACE) {
                        removeBulletPanel(displayItem, list);
                    }
                }
            });
            displayItem.addBlurHandler(new BlurHandler() {
                public void onBlur(BlurEvent blurEvent) {
                    displayItem.removeStyleName("token-input-selected-token-facebook");
                }
            });
            */

            Span span = new Span("x");
            span.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent clickEvent) {
                    removeBulletPanel(displayItem, list);
                }
            });

            displayItem.add(p);
            displayItem.add(span);
            // hold the original value of the item selected

            GWT.log("Adding selected item '" + itemBox.getValue() + "'", null);
            itemsSelected.add(itemBox.getValue());
            GWT.log("Total: " + itemsSelected, null);

            list.insert(displayItem, list.getWidgetCount() - 1);
            itemBox.setValue("");
            itemBox.setFocus(true);
        }
    }

    private void removeBulletPanel(BulletPanel displayItem, BulletListPanel list) {
        GWT.log("Removing: " + displayItem.getWidget(0).getElement().getInnerHTML(), null);
        itemsSelected.remove(displayItem.getWidget(0).getElement().getInnerHTML());
        list.remove(displayItem);
    }
}
