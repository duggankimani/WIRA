package com.duggan.workflow.client.ui.component;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.wira.commons.shared.models.Listable;

public class AutoCompleteField<T extends Listable> extends Composite {

	public static AutoCompleteFieldUiBinder uiBinder = 
			GWT.create(AutoCompleteFieldUiBinder.class);	
	@SuppressWarnings("rawtypes")
	interface AutoCompleteFieldUiBinder extends UiBinder<Widget, AutoCompleteField>{}
	
	@UiField HTMLPanel container;
	@UiField BulletListPanel ulPanel;
	@UiField BulletPanel liPanel;
	final TextBox itemBox = new TextBox();
	
	ArrayList<String> itemsSelected = new ArrayList<String>();
	HashMap<String,T> valuesMap = new HashMap<String, T>(); 
	DataOracle<T> oracle = new DataOracle<T>();
	SuggestBox box=null;
	
	static int x=0;//count to help generate unique ids
	String id="suggestion_box"+(++x);
	
	public AutoCompleteField() {	
		initWidget(uiBinder.createAndBindUi(this));
		itemBox.getElement().setAttribute("style", "outline-color: -moz-use-text-color; outline-style: none; outline-width: medium;");
		container.getElement().setAttribute("onclick", "document.getElementById('"+id+"').focus()");
		box = new SuggestBox(oracle, itemBox);
		box.getElement().setId(id);
		box.setAnimationEnabled(true);		
		liPanel.add(box);
		box.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
            public void onSelection(SelectionEvent selectionEvent) {
                deselectItem(itemBox, ulPanel);
            }
        });
		
		box.setFocus(true);
	}

	public void setValues(ArrayList<T> values){
		valuesMap.clear();
		if(values==null){
			return;
		}
		
		for(T t: values){
			valuesMap.put(t.getDisplayName(), t);
		}
	
		oracle.setValues(values);	
		
	}
	
	@Override
    protected void onLoad() {
    	super.onLoad();
    	 // 2. Show the following element structure and set the last <div> to display: block
        /*
        <ul class="token-input-ArrayList-facebook">
            <li class="token-input-input-token-facebook">
                <input type="text" style="outline-color: -moz-use-text-color; outline-style: none; outline-width: medium;"/>
            </li>
        </ul>
        <div class="token-input-dropdown-facebook" style="display: none;"/>
         */

        // this needs to be on the itemBox rather than box, or backspace will get executed twice
        itemBox.addKeyDownHandler(new KeyDownHandler() {
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    // only allow manual entries with @ signs (assumed email addresses)
                    if (itemBox.getValue().contains("@"))
                        deselectItem(itemBox, ulPanel);
                }
                // handle backspace
                if (event.getNativeKeyCode() == KeyCodes.KEY_BACKSPACE) {
                    if ("".equals(itemBox.getValue().trim())) {
                        BulletPanel li = (BulletPanel) ulPanel.getWidget(ulPanel.getWidgetCount() - 2);
                        Paragraph p = (Paragraph) li.getWidget(0);
                        if (itemsSelected.contains(p.getText())) {
                            itemsSelected.remove(p.getText());
                            GWT.log("Removing selected item '" + p.getText() + "'", null);
                            GWT.log("Remaining: " + itemsSelected, null);
                        }
                        ulPanel.remove(li);
                        itemBox.setFocus(true);
                    }
                }
            }
        });

        /* Div structure after a few elements have been added:
            <ul class="token-input-ArrayList-facebook">
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
	
    private void deselectItem(final TextBox itemBox, final BulletListPanel ArrayList) {
        if (itemBox.getValue() != null && !"".equals(itemBox.getValue().trim())) {
        	//System.err.println(":::::::: loaded");
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
                        removeBulletPanel(displayItem, ArrayList);
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
                    removeBulletPanel(displayItem, ArrayList);
                }
            });

            displayItem.add(p);
            displayItem.add(span);
            // hold the original value of the item selected

            GWT.log("Adding selected item '" + itemBox.getValue() + "'", null);
            itemsSelected.add(itemBox.getValue());
            GWT.log("Total: " + itemsSelected, null);

            ArrayList.insert(displayItem, ArrayList.getWidgetCount() - 1);
            itemBox.setValue("");
            itemBox.setFocus(true);
        }
    }

    private void removeBulletPanel(BulletPanel displayItem, BulletListPanel ArrayList) {
        GWT.log("Removing: " + displayItem.getWidget(0).getElement().getInnerHTML(), null);
        itemsSelected.remove(displayItem.getWidget(0).getElement().getInnerHTML());
        ArrayList.remove(displayItem);
    }
    
    public void addItems(ArrayList<T> items){		
    	//itemsSelected.clear();
		//others
		setValues(items);
	}
	
	public void select(ArrayList<T> items){
		clearSelection();
		//others
		if(items!=null)
		for(T item: items){			
			itemBox.setValue(item.getDisplayName());			
			deselectItem(itemBox, ulPanel);
		}
	}
	
	public ArrayList<T> getSelectedItems(){
		ArrayList<T> selectedVals = new ArrayList<T>();
		for(String s: itemsSelected){
			T val = valuesMap.get(s);
			if(val==null){
				val = createItem(s);
			}
			if(val!=null){
				selectedVals.add(val);
			}
		}
		return selectedVals;
	}
	
	public T createItem(String s) {
		return null;
	}

	public void clearSelection(){
		itemsSelected.clear();
		int count = ulPanel.getWidgetCount();
		for(int i=count-1; i>=0; i--){
			Widget w = ulPanel.getWidget(i);
			if(w instanceof BulletPanel && !(liPanel.equals(w))){
				removeBulletPanel((BulletPanel)w,ulPanel);
			}
		}
	}

}
