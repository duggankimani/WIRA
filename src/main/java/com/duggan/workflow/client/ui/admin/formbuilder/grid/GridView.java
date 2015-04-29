package com.duggan.workflow.client.ui.admin.formbuilder.grid;

import gwtupload.client.IUploader;
import gwtupload.client.IUploader.OnFinishUploaderHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.duggan.workflow.client.model.UploadContext;
import com.duggan.workflow.client.model.UploadContext.UPLOADACTION;
import com.duggan.workflow.client.ui.AppManager;
import com.duggan.workflow.client.ui.OnOptionSelected;
import com.duggan.workflow.client.ui.admin.formbuilder.component.FieldWidget;
import com.duggan.workflow.client.ui.admin.formbuilder.upload.ImportView;
import com.duggan.workflow.client.ui.events.ReloadEvent;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.DocumentLine;
import com.duggan.workflow.shared.model.StringValue;
import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.model.form.Field;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class GridView extends Composite {

	private static GridViewUiBinder uiBinder = GWT
			.create(GridViewUiBinder.class);

	interface GridViewUiBinder extends UiBinder<Widget, GridView> {
	}
	
	@UiField HTMLPanel header;
	@UiField HTMLPanel panelLines;
	@UiField Anchor aNewRecord;
	@UiField Anchor aImport;
	@UiField SpanElement spnNewRecordHandlerText;
	
	Collection<Field> columnConfigs = new ArrayList<Field>();
	private String gridName="";
	
	public GridView(Collection<Field> columns, Long parentId, String gridName) {	
		initWidget(uiBinder.createAndBindUi(this));
		this.gridName = gridName;
		
		setNewRecordHandlerText("Add Row");	
		
		if(columns!=null){
			columnConfigs = columns;		
			for(Field field:columns){
				field.setParentId(parentId);
				header.add(createHeader(field));
			}
		}
		
		init();		
		
	}
	
	private void init() {
		aNewRecord.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				DocumentLine line = new DocumentLine();
				//line count
				createLine(line,new Long(getRecords().size()+1));
			}
		});
		
		aImport.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {

				final StringBuffer uploadedItems = new StringBuffer();
				UploadContext context = new UploadContext();
				context.setAction(UPLOADACTION.IMPORTGRIDDATA);
				context.setAccept(Arrays.asList("csv"));
				
				final String message = "This will import data from a CSV file into the grid.";
				final ImportView view = new ImportView(message,context);
				view.setAvoidRepeatFiles(false);
				AppManager.showPopUp("Import Data", view, 
						new OnOptionSelected() {
							
							@Override
							public void onSelect(String name) {
								if(name.equals("Save")){
									//AppContext.fireEvent(new ReloadEvent()); 
									view.setVisible(false);
									
									setLines(uploadedItems.toString());
								}else{
									view.cancelImport();
								}
							}
						}, "Save", "Cancel");
				view.getUploader().addOnFinishUploaderHandler(new OnFinishUploaderHandler() {
					
					@Override
					public void onFinish(IUploader uploader) {
						String msg = uploader.getServerMessage().getMessage();
						view.setMessage(message+"<p>"+msg+"</p>");
						uploadedItems.append(msg);
					}
				});
			}
		});
		
	}

	private void setLines(String uploadedCSVItems) {
		if(uploadedCSVItems==null || uploadedCSVItems.trim().isEmpty()){
			return;
		}
		
		String[] items = uploadedCSVItems.split("\n");
		for(String item: items){
			String[] line = item.split(",");
			createLine(line);
		}
	}
	
	private void createLine(String[] lineValues) {
		DocumentLine docline = new DocumentLine();
		for(int i=0; i<lineValues.length && i<columnConfigs.size(); i++){
			Field field=((ArrayList<Field>)columnConfigs).get(i);
			FieldWidget widget = FieldWidget.getWidget(field.getType(), field, false);
			Value val = widget.from(field.getName(),lineValues[i]);
			docline.addValue(field.getName(), val);
		}
		createLine(docline, new Long(++count));
	}

	public void setNewRecordHandlerText(String text){
		spnNewRecordHandlerText.setInnerText(text);
	}
	
	int count= 0;
	public void setData(Collection<DocumentLine> lines){
		
		for(DocumentLine line: lines){
			//System.err.println(line);
			createLine(line, new Long(++count));
		}
	}
	
	public Collection<DocumentLine> getRecords(){
		Collection<DocumentLine> lines = new ArrayList<DocumentLine>();
		int count = panelLines.getWidgetCount();
		
		for(int i=0; i<count; i++){
			GridRow gridRow = (GridRow)panelLines.getWidget(i);
			DocumentLine line=gridRow.getRecord();
			lines.add(line);
		}
		
		return lines;
	}

	private void createLine(DocumentLine line, Long lineRefId) {
		line.setTempId(lineRefId);
		GridRow row = new GridRow(columnConfigs, line,gridName);	
		panelLines.add(row);
	}

	Widget createHeader(Field field){
		HTMLPanel panel = new HTMLPanel("");
		panel.setStyleName("th generic-header");
		panel.add(new HTML(field.getCaption()));
		return panel;
	}

	public void setReadOnly(boolean readOnly) {
		aNewRecord.setVisible(!readOnly);
		aImport.setVisible(!readOnly);
		int widgetCount = panelLines.getWidgetCount();
		for(int i=0; i<widgetCount; i++){
			GridRow row = (GridRow)panelLines.getWidget(i);
			row.setReadOnly(readOnly);
		}
	}
}
