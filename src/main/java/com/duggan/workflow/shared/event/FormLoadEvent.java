package com.duggan.workflow.shared.event;

import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.model.form.Form;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class FormLoadEvent extends GwtEvent<FormLoadEvent.FormLoadHandler> {
    private static Type<FormLoadHandler> TYPE = new Type<FormLoadHandler>();
    
    public interface FormLoadHandler extends EventHandler {
        void onFormLoad(FormLoadEvent event);
    }
    
    
    private Form form;
    private Doc doc;
   
    public FormLoadEvent(Form form, Doc doc) {
		this.form = form;
		this.doc = doc;
    }

    public static Type<FormLoadHandler> getType() {
        return TYPE;
    }

    @Override
    protected void dispatch(final FormLoadHandler handler) {
        handler.onFormLoad(this);
    }

    @Override
    public Type<FormLoadHandler> getAssociatedType() {
        return TYPE;
    }
    
    public Form getForm() {
  		return form;
  	}

  	public void setForm(Form form) {
  		this.form = form;
  	}

  	public Doc getDoc() {
  		return doc;
  	}

  	public void setDoc(Doc doc) {
  		this.doc = doc;
  	}

}