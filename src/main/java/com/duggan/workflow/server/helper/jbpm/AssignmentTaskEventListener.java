package com.duggan.workflow.server.helper.jbpm;

import java.util.List;

import org.apache.log4j.Logger;
import org.drools.definition.process.Node;
import org.drools.persistence.jpa.JPAKnowledgeService;
import org.jbpm.task.Task;
import org.jbpm.task.event.TaskEventListener;
import org.jbpm.task.event.entity.TaskUserEvent;
import org.jbpm.workflow.core.node.HumanTaskNode;

import com.duggan.workflow.server.dao.helper.ProcessDefHelper;
import com.duggan.workflow.server.dao.model.ProcessDefModel;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.shared.model.Actions;
import com.duggan.workflow.shared.model.AssignmentDto;
import com.duggan.workflow.shared.model.AssignmentFunction;

public class AssignmentTaskEventListener implements TaskEventListener {

	static Logger logger = Logger.getLogger(AssignmentTaskEventListener.class);

	@Override
	public void taskClaimed(TaskUserEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void taskCompleted(TaskUserEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void taskCreated(TaskUserEvent arg0) {
		assignTask(arg0.getTaskId());
	}

	private void assignTask(long taskId) {
		try {
			Task task = JBPMHelper.get().getTaskClient().getTask(taskId);
			String processId = task.getTaskData().getProcessId();
			if (task.getTaskData().getActualOwner() != null) {
				return;
			}

			Node taskNode = (HumanTaskNode) JBPMHelper.get().getNode(task);
			ProcessDefModel model = DB.getProcessDao().getProcessDef(
					task.getTaskData().getProcessId());

			AssignmentDto assignment = ProcessDefHelper.getAssignment(
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
			String taskName = JBPMHelper.get().getTaskName(taskId);
			List<String> potentialAssignees = JBPMHelper.get().getPotentialOwnersAsList(task);
			
			String assignee = DB.getProcessDao().getNextAssignee(task.getId(),taskName
					,processId,potentialAssignees);

			if (assignee == null) {
				
				throw new RuntimeException(
						"Cyclic assignment failed. Not assignee could be found for task "
								+ "{taskId:" + taskId + ",name:" + taskName
								+ ",processid:"
								+ task.getTaskData().getProcessId() + "}"
								+ " : Group [" + potentialAssignees + "]");
			}

			JBPMHelper.get().execute(taskId, assignee, Actions.CLAIM, null);
		} catch (Exception e) {
			logger.fatal("Assignment failed: Cause = " + e.getMessage());
			e.printStackTrace();
			throw new RuntimeException("Assignment Failed", e);
		}

	}

	@Override
	public void taskFailed(TaskUserEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void taskForwarded(TaskUserEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void taskReleased(TaskUserEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void taskSkipped(TaskUserEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void taskStarted(TaskUserEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void taskStopped(TaskUserEvent arg0) {
		// TODO Auto-generated method stub

	}

}
