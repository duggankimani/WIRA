package com.duggan.workflow.client.ui.component;

import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;

/**
 * Page to hold the Auto-suggest invite form, ala Gmail.
 *
 * @author mraible
 */
public class SuggestField extends DialogBox {
    FlowPanel container;

    public SuggestField() {
        super(false, true);
        setText("Facebook-style Autocomplete with GWT");
        container = new FlowPanel();
        container.setStyleName("auto_suggest");

        container.add(new AutoSuggestForm());
        add(container);
        center();
        show();
    }

    private class AutoSuggestForm extends Composite {
        FlowPanel form;

        protected AutoSuggestForm() {
            form = new FlowPanel();
            form.setStyleName("form");
            initWidget(form);

            form.add(new HTML("<p>Type in the box below to see basic autocomplete in action...</p>"));

            MultipleTextBox txt = new MultipleTextBox();
            SuggestBox box = new SuggestBox(getSuggestions(), txt);
            box.addStyleName("original-token-input");
            box.setAnimationEnabled(true);

            form.add(box);

            form.add(new HTML("<p style='margin-top: 20px'>Type in the box below to see autocomplete with Facebook-style formatting.</p>"));

            // Facebook Style Autocompleter
            // CSS and DIV structure from http://loopj.com/tokeninput/demo.html:

            // 1. Create an input field
            form.add(new InputListWidget());

            form.add(new HTML("<p>For more information about this demo, see <a href=\"http://raibledesigns.com/rd/entry/creating_a_facebook_style_autocomplete\">Creating a Facebook-style Autocomplete with GWT</a>."));
        }

        public void onSubmit(DomEvent<EventHandler> event) {
            // no-op
        }
    }

    /**
     * To make this return a DTO that allows you to grab multiple values, see
     * the following tutorial:
     * <p/>
     * http://eggsylife.blogspot.com/2008/08/gwt-suggestbox-backed-by-dto-model.html
     *
     * @return names of possible contacts
     */
    public static MultiWordSuggestOracle getSuggestions() {
        MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
        oracle.add("Amy Kesic");
        oracle.add("Jason Weston");
        oracle.add("Dave Johnson");
        oracle.add("Paul Hammant");
        oracle.add("Jesse Kuhnert");
        oracle.add("Ben Alex");
        oracle.add("Tom Bender");
        oracle.add("Alexandru Popescu");
        oracle.add("Kaveh Arabfakhry");
        oracle.add("Steven Hong");
        oracle.add("Jason van Zyl");
        oracle.add("Alex Vauthey");
        oracle.add("Kiran Karnati");
        oracle.add("Kalpana Nagireddy");
        oracle.add("Ramnivas Laddad");
        oracle.add("Arj� Cahn");
        oracle.add("Amy Anne Rasberry");
        oracle.add("Vincent Stoessel");
        oracle.add("Steven Leija");
        oracle.add("Brian Burke");
        oracle.add("John Ipson");
        oracle.add("Candy Chastain Mielke");
        oracle.add("Scott Mark");
        oracle.add("Dov B. Katz");
        oracle.add("Alef Arendsen");
        oracle.add("David Jencks");
        oracle.add("Alexey Belikov");
        oracle.add("Bryan Vial");
        oracle.add("Dror Bereznitsky");
        oracle.add("David Moskowitz");
        oracle.add("Oscar Chan");
        oracle.add("Sergey Sundukovskiy");
        oracle.add("John Newton");
        oracle.add("Chris Buzzetta");
        oracle.add("Peter Svensson");
        oracle.add("Riccardo Ferretti");
        oracle.add("Christian Parker");
        oracle.add("Ann (Jaksa) Skaehill");
        oracle.add("Justin Blue");
        oracle.add("Sean Dawson");
        oracle.add("Devaraj NS");
        oracle.add("Robert Gadd");
        oracle.add("Diego Campodonico");
        oracle.add("Bryan Field-Elliot");
        oracle.add("Scott Delap");
        oracle.add("Kevin Koster");
        oracle.add("Fernand Galiana");
        oracle.add("Christopher Shuler");
        oracle.add("Geir Magnusson Jr");
        oracle.add("Tyler Hansen");
        oracle.add("Olivier Lamy");
        oracle.add("J. Thomas Richardson");
        oracle.add("Russell Beattie");
        oracle.add("Martin Ouellet");
        oracle.add("Scott Ferguson");
        oracle.add("Guillaume Laforge");
        oracle.add("Eric Weidner");
        oracle.add("Troy McKinnon");
        oracle.add("Max Hays");
        oracle.add("Phillip Rhodes");
        oracle.add("Eugene Kulechov");
        oracle.add("Bob Johnson");
        oracle.add("Richard Tucker, PMP");
        oracle.add("Mats Henricson");
        oracle.add("Floyd Marinescu");
        oracle.add("Ed Burns");
        oracle.add("Michael Root");
        oracle.add("Dana Busch");
        oracle.add("Borislav Roussev");
        oracle.add("Harris Tsim");
        oracle.add("Jason Thrasher");
        oracle.add("Soo-il Kim");
        oracle.add("Lindsey Bowman");
        oracle.add("Ganesh Hariharan");
        oracle.add("Judy Herilla");
        oracle.add("Jevgeni Kabanov");
        oracle.add("Craig Whitacre");
        oracle.add("Paul M. Garvey");
        oracle.add("Jeremy Whitlock");
        oracle.add("Fabrizio Giustina");
        oracle.add("Todd Fredrich");
        oracle.add("Matt Stine");
        oracle.add("Jo�o Vitor Lacerda Guimar�es");
        oracle.add("Yassine Hinnach");
        oracle.add("Chris Huston");
        oracle.add("Jodi Behrens-Stark");
        oracle.add("John Greenhill");
        oracle.add("Roy Porter");
        oracle.add("Paul Tuckey");
        oracle.add("Arjun Ram");
        oracle.add("Merrill Bennett");
        oracle.add("James Richards");
        oracle.add("Franz Garsombke");
        oracle.add("Kimberly Horan");
        oracle.add("Hani Suleiman");
        oracle.add("Thomas Dudziak");
        oracle.add("Andrew Penrose");
        oracle.add("Igor Polyakov");
        oracle.add("Steve Runkel");

        return oracle;
    }

}