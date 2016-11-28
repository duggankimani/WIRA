package com.duggan.workflow.server.helper.jbpm;

import java.util.List;

import org.apache.log4j.Logger;
import org.jbpm.services.task.lifecycle.listeners.TaskLifeCycleEventListener;
import org.jbpm.workflow.core.node.HumanTaskNode;
import org.kie.api.definition.process.Node;
import org.kie.api.task.TaskEvent;
import org.kie.api.task.model.Task;

import com.duggan.workflow.server.dao.helper.ProcessDaoHelper;
import com.duggan.workflow.server.dao.model.ProcessDefModel;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.shared.model.Actions;
import com.duggan.workflow.shared.model.AssignmentDto;
import com.duggan.workflow.shared.model.AssignmentFunction;

public class AssignmentTaskEventListener implements TaskLifeCycleEventListener {

	static Logger logger = Logger.getLogger(AssignmentTaskEventListener.class);

//	@Override
//	public void taskCreated(TaskUserEvent arg0) {
//		assignTask(arg0.getTaskId());
//	}

	private void assignTask(Task task) {
		try {
//			Task task = JBPMHelper.get().getTaskClient().getTask(taskId);
			String processId = task.getTaskData().getProcessId();
			if (task.getTaskData().getActualOwner() != null) {
				return;
			}

			Node taskNode = (HumanTaskNode) JBPMHelper.get().getNode(task);
			ProcessDefModel model = DB.getProcessDao().getProcessDef(
					task.getTaskData().getProcessId());

			AssignmentDto assignment = ProcessDaoHelper.getAssignment(
					model.getRefId(), taskNode.getId());
			if (assignment == null) {
				return;
			}

			if (assignment.getFunction() != AssignmentFunction.CYCLIC_ASSIGNMENT) {
				return;
			}

			/*
			 * This runs in a different entity manager than the entity manager that created 
			 * the current task. The two entity managers should however be executing in the 
			 * same global transaction
			 */
			String taskName = JBPMHelper.get().getTaskName(task.getId());
			List<String> potentialAssignees = JBPMHelper.get().getPotentialOwnersAsList(task);
			
			String assignee = DB.getProcessDao().getNextAssignee(task.getId(),taskName
					,processId,potentialAssignees);

			if (assignee == null) {
				
				throw new RuntimeException(
						"Cyclic assignment failed. Not assignee could be found for task "
								+ "{taskId:" + task.getId() + ",name:" + taskName
								+ ",processid:"
								+ task.getTaskData().getProcessId() + "}"
								+ " : Group [" + potentialAssignees + "]");
			}

			JBPMHelper.get().execute(task.getId(), assignee, Actions.CLAIM, null);
		} catch (Exception e) {
			logger.fatal("Assignment failed: Cause = " + e.getMessage());
			e.printStackTrace();
			throw new RuntimeException("Assignment Failed", e);
		}

	}

	@Override
	public void afterTaskActivatedEvent(TaskEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterTaskAddedEvent(TaskEvent arg0) {
		assignTask(arg0.getTask());
	}

	@Override
	public void afterTaskClaimedEvent(TaskEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterTaskCompletedEvent(TaskEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterTaskDelegatedEvent(TaskEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterTaskExitedEvent(TaskEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterTaskFailedEvent(TaskEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterTaskForwardedEvent(TaskEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterTaskNominatedEvent(TaskEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterTaskReleasedEvent(TaskEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterTaskResumedEvent(TaskEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterTaskSkippedEvent(TaskEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterTaskStartedEvent(TaskEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterTaskStoppedEvent(TaskEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterTaskSuspendedEvent(TaskEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTaskActivatedEvent(TaskEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTaskAddedEvent(TaskEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTaskClaimedEvent(TaskEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTaskCompletedEvent(TaskEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTaskDelegatedEvent(TaskEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTaskExitedEvent(TaskEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTaskFailedEvent(TaskEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTaskForwardedEvent(TaskEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTaskNominatedEvent(TaskEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTaskReleasedEvent(TaskEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTaskResumedEvent(TaskEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTaskSkippedEvent(TaskEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTaskStartedEvent(TaskEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTaskStoppedEvent(TaskEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTaskSuspendedEvent(TaskEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterTaskUpdatedEvent(TaskEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTaskUpdatedEvent(TaskEvent arg0) {
		// TODO Auto-generated method stub
		
	}


}
