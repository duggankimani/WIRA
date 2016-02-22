package com.duggan.workflow.server.helper.jbpm;

import java.util.List;
import java.util.Map;

import org.kie.api.definition.process.Process;
import org.kie.api.event.process.ProcessEventListener;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.task.TaskLifeCycleEventListener;
import org.kie.api.task.TaskService;

import com.duggan.workflow.shared.model.Actions;
import com.duggan.workflow.shared.model.Document;

public interface WiraSessionManager {

	TaskService getTaskClient();

	void disposeSessions();

	ProcessInstance startProcess(String processId,
			Map<String, Object> initialParams, Document doc);

	org.kie.api.definition.process.Process getProcess(String processId);

	void execute(long taskId, String userId, Actions action,
			Map<String, Object> values);

	void loadKnowledge(byte[] bytes, String processName);

	boolean isRunning(String processId);

	void unloadKnowledgeBase(String processId);

	void upgradeProcessInstance(long processInstanceId, String processId);

	void loadKnowledge(List<byte[]> files, List<ResourceType> types,
			String rootProcess);

	org.jbpm.process.instance.ProcessInstance getProcessInstance(
			long processInstanceId);

	org.kie.api.definition.process.Process getProcess(long processInstanceId);

	void addProcessEventListener(Class<? extends ProcessEventListener> listener);

	void addTaskListener(Class<? extends TaskLifeCycleEventListener> listener);

}
