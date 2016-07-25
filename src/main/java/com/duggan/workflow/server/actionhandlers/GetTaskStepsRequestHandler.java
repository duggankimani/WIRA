package com.duggan.workflow.server.actionhandlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.duggan.workflow.server.dao.helper.ProcessDaoHelper;
import com.duggan.workflow.server.export.DocumentHTMLMapper;
import com.duggan.workflow.server.mvel.MVELExecutor;
import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.HTask;
import com.duggan.workflow.shared.model.TaskStepDTO;
import com.duggan.workflow.shared.requests.GetTaskStepsRequest;
import com.duggan.workflow.shared.responses.GetTaskStepsResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.response.BaseResponse;

public class GetTaskStepsRequestHandler extends
		AbstractActionHandler<GetTaskStepsRequest, GetTaskStepsResponse> {
	
	@Inject
	public GetTaskStepsRequestHandler() {
	}
	
	@Override
	public void execute(GetTaskStepsRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		
		List<TaskStepDTO> steps = new ArrayList<>();
		if(action.getDoc()!=null){
			//Execution
			if(action.getDoc() instanceof HTask){
				steps = ProcessDaoHelper.getTaskStepsByTaskId(((HTask)action.getDoc()).getId());
			}else{
				steps = ProcessDaoHelper.getTaskStepsByDocumentId(((Document)action.getDoc()).getDocumentId());
			}
			
			((GetTaskStepsResponse)actionResult).setConditionalFields(getConditionalFields(steps));
			steps = filter(steps, action.getDoc());
		}else{
			steps = ProcessDaoHelper.getSteps(action.getProcessId(), action.getNodeId());
		}
		
		((GetTaskStepsResponse)actionResult).setSteps((ArrayList<TaskStepDTO>) steps);
		
	}
	
	private ArrayList<String> getConditionalFields(List<TaskStepDTO> steps) {
		
		ArrayList<String> fields = new ArrayList<String>();
		for(TaskStepDTO dto: steps){
			if(dto.getCondition()!=null){
				getKeys(dto.getCondition(), fields);
			}
		}
		
		log.debug("TaskSteps Conditional Loading fields : "+fields);
		
		return fields;
	}
	
	public String getKeys(String html, List<String> keys) {
		Pattern pattern = Pattern.compile("@[@#]\\w+?\\b");
		String rtn = new String(html);
		Matcher matcher = pattern.matcher(rtn);

		while (matcher.find()) {
			String group = matcher.group();
			// int start = matcher.start();
			// int end=matcher.end();
			// System.err.format("I found the text" +
			// " \"%s\" starting at " +
			// "index %d and ending at index %d.%n",
			// group,
			// start,
			// end);
			String key = group.substring(2, group.length());
			keys.add(key);
			rtn = rtn.replace(matcher.group(), "");
		}

		return rtn;
	}


	private List<TaskStepDTO> filter(List<TaskStepDTO> steps, Doc doc) {
		List<TaskStepDTO> dtos = new ArrayList<TaskStepDTO>();
		for(TaskStepDTO dto: steps){
			String stepName = dto.getFormName()==null? dto.getOutputDocName(): dto.getFormName();
			if(dto.getCondition()!=null){
				HashMap<String,Object> values = doc.toObjectMap();
				String condition = new DocumentHTMLMapper().map(values, dto.getCondition(), true);
				Boolean value = new MVELExecutor().executeBoolean(condition, doc);
				log.debug("Filtering TaskSteps - "+stepName+ " ; Displayed = "+value);
				
				if(value!=null && !value){
					//Check for false result only; otherwise assume true
					continue;
				}
			}
			
			dtos.add(dto);
		}
		return dtos;
	}

	public java.lang.Class<GetTaskStepsRequest> getActionType() {
		return GetTaskStepsRequest.class;
	};

}
