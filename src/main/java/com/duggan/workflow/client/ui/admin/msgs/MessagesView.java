package com.duggan.workflow.client.ui.admin.msgs;

import static com.duggan.workflow.client.ui.util.DateUtils.*;

import java.util.List;

import com.duggan.workflow.client.ui.component.Grid;
import com.duggan.workflow.shared.model.RequestInfoDto;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class MessagesView extends ViewImpl implements
		MessagesPresenter.IMessagesView {

	private final Widget widget;
	@UiField Grid<RequestInfoDto> grid;
	
	
	public interface Binder extends UiBinder<Widget, MessagesView> {
	}

	@Inject
	public MessagesView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		
		TextColumn<RequestInfoDto> created = new TextColumn<RequestInfoDto>() {
			@Override
			public String getValue(RequestInfoDto arg0) {
				return (arg0.getTime()==null? "" : DATEFORMAT.format(arg0.getTime()));
			}
		}; 
		
		TextColumn<RequestInfoDto> commandName = new TextColumn<RequestInfoDto>() {
			@Override
			public String getValue(RequestInfoDto arg0) {
				return arg0.getCommandName();
			}
		}; 
		
		TextColumn<RequestInfoDto> executions = new TextColumn<RequestInfoDto>() {
			@Override
			public String getValue(RequestInfoDto arg0) {
				return arg0.getExecutions()+"";
			}
		}; 
		
		TextColumn<RequestInfoDto> message = new TextColumn<RequestInfoDto>() {
			@Override
			public String getValue(RequestInfoDto arg0) {
				return arg0.getMessage();
			}
		}; 
		
		TextColumn<RequestInfoDto> messageKey = new TextColumn<RequestInfoDto>() {
			@Override
			public String getValue(RequestInfoDto arg0) {
				return arg0.getMessageKey();
			}
		}; 
		
		
		
		TextColumn<RequestInfoDto> retries = new TextColumn<RequestInfoDto>() {
			@Override
			public String getValue(RequestInfoDto arg0) {
				return arg0.getRetries()+"";
			}
		}; 
		
		TextColumn<RequestInfoDto> status = new TextColumn<RequestInfoDto>() {
			@Override
			public String getValue(RequestInfoDto arg0) {
				return arg0.getStatus();
			}
		}; 
		
		grid.addColumn(commandName, "Command Name");
		grid.addColumn(created, "Last Modified");
		grid.addColumn(executions, "Executions");
		grid.addColumn(retries, "Retrues");
		grid.addColumn(message, "Message");
		grid.addColumn(messageKey,"Message Key");
		grid.addColumn(status, "Status");
	}
	
	@Override
	public void setData(List<RequestInfoDto> data, int totalCount) {
		grid.setData(data,totalCount);		
	}

	@Override
	public Grid<RequestInfoDto> getGrid() {
		return grid;
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

}
