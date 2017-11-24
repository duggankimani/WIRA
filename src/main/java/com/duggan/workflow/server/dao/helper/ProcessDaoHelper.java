package com.duggan.workflow.server.dao.helper;

import static com.duggan.workflow.server.dao.helper.DocumentDaoHelper.getType;
import static com.duggan.workflow.server.dao.helper.DocumentDaoHelper.getTypeFromProcess;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.drools.definition.process.Node;
import org.jbpm.task.Task;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.duggan.workflow.client.model.TaskType;
import com.duggan.workflow.server.dao.ProcessDaoImpl;
import com.duggan.workflow.server.dao.hibernate.JsonForm;
import com.duggan.workflow.server.dao.model.ADDocType;
import com.duggan.workflow.server.dao.model.ADOutputDoc;
import com.duggan.workflow.server.dao.model.ADProcessCategory;
import com.duggan.workflow.server.dao.model.ADTaskNotification;
import com.duggan.workflow.server.dao.model.ADTaskStepTrigger;
import com.duggan.workflow.server.dao.model.ADTrigger;
import com.duggan.workflow.server.dao.model.AssignmentPO;
import com.duggan.workflow.server.dao.model.DocumentModel;
import com.duggan.workflow.server.dao.model.Group;
import com.duggan.workflow.server.dao.model.LocalAttachment;
import com.duggan.workflow.server.dao.model.ProcessDefModel;
import com.duggan.workflow.server.dao.model.TaskStepModel;
import com.duggan.workflow.server.dao.model.User;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.helper.auth.UserDaoHelper;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.server.servlets.upload.ImportLogger;
import com.duggan.workflow.shared.model.Actions;
import com.duggan.workflow.shared.model.AssignmentDto;
import com.duggan.workflow.shared.model.Attachment;
import com.duggan.workflow.shared.model.AttachmentType;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.DocumentType;
import com.duggan.workflow.shared.model.MODE;
import com.duggan.workflow.shared.model.NotificationCategory;
import com.duggan.workflow.shared.model.OutputDocument;
import com.duggan.workflow.shared.model.ProcessCategory;
import com.duggan.workflow.shared.model.ProcessDef;
import com.duggan.workflow.shared.model.Schema;
import com.duggan.workflow.shared.model.Status;
import com.duggan.workflow.shared.model.TaskLog;
import com.duggan.workflow.shared.model.TaskNotification;
import com.duggan.workflow.shared.model.TaskStepDTO;
import com.duggan.workflow.shared.model.TaskStepTrigger;
import com.duggan.workflow.shared.model.Trigger;
import com.duggan.workflow.shared.model.TriggerType;
import com.duggan.workflow.shared.model.form.Field;
import com.duggan.workflow.shared.model.form.Form;
import com.sun.jersey.api.json.JSONMarshaller;
import com.sun.jersey.api.json.JSONUnmarshaller;
import com.wira.commons.shared.models.HTUser;
import com.wira.commons.shared.models.Listable;
import com.wira.commons.shared.models.UserGroup;

public class ProcessDaoHelper {

	static Logger log = Logger.getLogger(ProcessDaoHelper.class);

	public static ProcessDef save(ProcessDef processDef) {
		ProcessDaoImpl dao = DB.getProcessDao();
		ProcessDefModel model = get(processDef);

		dao.save(model);
		return get(model, true);
	}

	public static List<ProcessCategory> getProcessCategories() {
		ProcessDaoImpl dao = DB.getProcessDao();
		List<ADProcessCategory> list = dao.getAllProcessCategories();
		List<ProcessCategory> categories = new ArrayList<>();

		for (ADProcessCategory cat : list) {
			categories.add(get(cat));
		}

		return categories;
	}

	public static ProcessCategory save(ProcessCategory category) {
		ProcessDaoImpl dao = DB.getProcessDao();
		ADProcessCategory adCat = get(category);

		dao.save(adCat);

		return get(adCat);
	}

	private static ADProcessCategory get(ProcessCategory category) {
		ProcessDaoImpl dao = DB.getProcessDao();
		ADProcessCategory adCat = new ADProcessCategory();
		if (category.getId() != null) {
			adCat = dao.getById(ADProcessCategory.class, category.getId());
		}
		adCat.setIndex(category.getIndex());
		adCat.setName(category.getName());

		return adCat;
	}
	
	public static void delete(Long processDefId) {
		ProcessDaoImpl dao = DB.getProcessDao();

		// Check if there are any docs linked to the process
		// if not, delete
		ProcessDefModel model = dao.getProcessDef(processDefId);
		model.setArchived(true);
		model.setIsActive(0);

		dao.save(model);
	}

	public static void delete(ProcessDef processDef) {
		ProcessDaoImpl dao = DB.getProcessDao();
		ProcessDefModel model = get(processDef);

		dao.remove(model);
	}

	public static ProcessDef getProcessDef(Long processDefId) {
		ProcessDaoImpl dao = DB.getProcessDao();

		ProcessDefModel model = dao.getProcessDef(processDefId);

		ProcessDef def = get(model, true);

		return def;
	}

	public static ProcessDef getProcessDef(String processRefId) {
		return getProcessDef(processRefId, true);
	}

	public static ProcessDef getProcessDef(String processRefId,
			boolean isDetailed) {
		ProcessDaoImpl dao = DB.getProcessDao();

		ProcessDefModel model = dao.findByRefId(processRefId,
				ProcessDefModel.class);
		if (model == null) {
			return null;
		}

		ProcessDef def = get(model, isDetailed);

		return def;
	}

	public static List<ProcessDef> getAllProcesses(String searchTerm,
			boolean isLoadDetails) {
		return getAllProcesses(searchTerm, isLoadDetails, 0, 0);
	}

	public static List<ProcessDef> getAllProcesses(String searchTerm,
			boolean isLoadDetails, int beginIdx, int length) {

		ProcessDaoImpl dao = DB.getProcessDao();

		List<ProcessDefModel> process = dao.getAllProcesses(searchTerm,
				beginIdx, length);

		List<ProcessDef> processDefs = new ArrayList<>();

		if (processDefs != null) {
			for (ProcessDefModel model : process) {
				ProcessDef processDef = get(model, isLoadDetails);
				processDefs.add(processDef);
			}
		}

		return processDefs;
	}

	public static ProcessDef get(ProcessDefModel model, boolean isDetailed) {

		ProcessDef def = new ProcessDef();
		def.setName(model.getName());
		def.setProcessId(model.getProcessId());
		def.setId(model.getId());
		def.setRefId(model.getRefId());

		if (isDetailed) {
			// Process Access
			UserDaoHelper helper = new UserDaoHelper();
			Collection<User> users = model.getUsers();
			for (User user : users) {
				def.addUserOrGroup(helper.get(user, false));
			}
			Collection<Group> groups = model.getGroups();
			for (Group g : groups) {
				def.addUserOrGroup(helper.get(g));
			}

			// Process Counts
			HashMap<TaskType, Integer> counts = new HashMap<>();
			JBPMHelper.get().getProcessCounts(model.getProcessId(), counts);
			def.setInbox(counts.get(TaskType.INBOX));
			def.setParticipated(counts.get(TaskType.PARTICIPATED));

			boolean running = JBPMHelper.get().isProcessingRunning(
					model.getProcessId());
			def.setStatus(running ? Status.RUNNING : Status.INACTIVE);

			ArrayList<DocumentType> docTypes = new ArrayList<>();
			Collection<ADDocType> docModels = model.getDocumentTypes();

			for (ADDocType doc : docModels) {
				docTypes.add(getType(doc));
				def.setBackgroundColor(doc.getBackgroundColor());
				def.setIconStyle(doc.getIconStyle());
				def.setCategory(get(doc.getCategory()));
			}
			def.setDocTypes(docTypes);
		}

		def.setDescription(model.getDescription());
		def.setTargetDays(model.getTargetDays());
		def.setLastModified(model.getUpdated() == null ? model.getCreated()
				: model.getUpdated());

		// ?? Review below attachment loading mechanism - Duggan 26/09/2016
		List<LocalAttachment> attachments = DB.getAttachmentDao()
				.getAttachmentsForProcessDef(model);

		if (attachments != null) {
			for (LocalAttachment attach : attachments) {
				def.addFile(AttachmentDaoHelper.get(attach));
			}
		}

		List<LocalAttachment> image = DB.getAttachmentDao()
				.getAttachmentsForProcessDef(model, true);

		if (image != null && !image.isEmpty()) {
			def.setImageId(image.get(0).getId());

			def.setImageName(image.get(0).getName());
		}

		return def;
	}

	public static ProcessCategory get(ADProcessCategory category) {
		if (category == null) {
			return null;
		}

		ProcessCategory cat = new ProcessCategory();
		cat.setId(category.getId());
		cat.setIndex(category.getIndex());
		cat.setName(category.getName());
		cat.setRefId(category.getRefId());

		return cat;
	}

	private static ArrayList<DocumentType> getDocTypes(
			Collection<ADDocType> processDocuments) {
		if (processDocuments == null) {
			return new ArrayList<>();
		}

		ArrayList<DocumentType> docs = new ArrayList<>();

		for (ADDocType model : processDocuments) {
			docs.add(getType(model));
		}

		return docs;
	}

	public static ProcessDefModel get(ProcessDef processDef) {
		ProcessDaoImpl dao = DB.getProcessDao();

		ProcessDefModel model = new ProcessDefModel();
		Long id = processDef.getId();

		if (id != null) {
			model = dao.getProcessDef(id);
			model.clear();
		}

		model.setRefId(processDef.getRefId());
		model.setName(processDef.getName());
		model.setDescription(processDef.getDescription());
		model.setProcessId(processDef.getProcessId());
		model.setTargetDays(processDef.getTargetDays());

		List<Listable> usersAndGroups = processDef.getUsersAndGroups();
		for (Listable item : usersAndGroups) {
			if (item instanceof HTUser) {
				User u = DB.getUserGroupDao().getUser(
						((HTUser) item).getUserId());
				model.addUser(u);
			} else {
				Group g = DB.getUserGroupDao().getGroup(
						((UserGroup) item).getName());
				model.addGroup(g);
			}
		}

		List<DocumentType> types = processDef.getDocTypes();

		if (types != null && !types.isEmpty()) {
			for (DocumentType type : types) {

				type.setName(processDef.getProcessId());
				type.setDisplayName(processDef.getDisplayName());
				type.setProcessId(processDef.getProcessId());
				type.setBackgroundColor(processDef.getBackgroundColor());
				type.setIconStyle(processDef.getIconStyle());
				ADDocType adtype = getType(type);
				DB.getProcessDao().save(adtype);

				ProcessCategory category = processDef.getCategory();
				if (category != null) {
					ADProcessCategory cat = dao.getById(
							ADProcessCategory.class, category.getId());
					adtype.setCategory(cat);
				}

				model.addDocType(adtype);
			}
		} else {
			// Auto Generate Types
			ADDocType adtype = getTypeFromProcess(processDef);
			adtype.setBackgroundColor(processDef.getBackgroundColor());
			adtype.setIconStyle(processDef.getIconStyle());
			DB.getProcessDao().save(adtype);

			model.addDocType(adtype);
		}

		return model;
	}

	public static List<TaskStepDTO> createTaskSteps(List<TaskStepDTO> steps) {
		ProcessDaoImpl dao = DB.getProcessDao();
		List<TaskStepDTO> list = new ArrayList<>();

		Long processDefId = steps.get(0).getProcessDefId();
		Long nodeId = steps.get(0).getNodeId();
		int size = dao.getStepCount(processDefId, nodeId);

		for (TaskStepDTO step : steps) {

			int previousPos = -1;
			int newPos = -1;
			if (step.getId() != null) {
				previousPos = dao.getById(TaskStepModel.class, step.getId())
						.getSequenceNo();
				newPos = step.getSequenceNo();
			} else {
				newPos = previousPos = ++size;
				step.setSequenceNo(newPos);
			}

			if (previousPos != newPos) {
				// Changed sequence
				TaskStepModel previousModel = dao.getTaskStepBySequenceNo(
						step.getProcessDefId(), step.getNodeId(), newPos);
				if (previousModel != null) {
					// Swap the positions of affected models
					previousModel.setSequenceNo(previousPos);
					dao.createStep(previousModel);
				}
			}

			TaskStepModel model = getStep(step);
			if (!step.isActive()) {
				dao.cascadeDelete(model);
				recalculatePositionsForSubsequentSteps(step);
				continue;
			}

			// Creating/ Updating model
			dao.createStep(model);

			list.add(getStep(model));
		}

		return list;
	}

	/**
	 * Called After step delete</p> Recalculates positions of subsequent steps
	 * after delete
	 * <p>
	 * 
	 * @param sequenceNo
	 */
	private static void recalculatePositionsForSubsequentSteps(
			TaskStepDTO deletedModel) {
		ProcessDaoImpl dao = DB.getProcessDao();
		List<TaskStepModel> models = dao.getTaskStepsAfterSeqNo(
				deletedModel.getProcessDefId(), deletedModel.getNodeId(),
				deletedModel.getSequenceNo());
		for (TaskStepModel model : models) {
			int seqNo = model.getSequenceNo();
			model.setSequenceNo(seqNo - 1);
			dao.save(model);
		}
	}

	private static TaskStepDTO getStep(TaskStepModel model) {

		TaskStepDTO dto = new TaskStepDTO();
		dto.setCondition(model.getCondition());
		dto.setFormName(model.getForm() == null ? null : model.getForm()
				.getCaption());
		dto.setFormRefId(model.getFormRef());
		if (dto.getFormRefId() != null) {
			dto.setFormName(DB.getFormDao().getFormNameJson(dto.getFormRefId()));
		}
		dto.setId(model.getId());
		dto.setRefId(model.getRefId());
		dto.setMode(model.getMode());
		dto.setNodeId(model.getNodeId());
		dto.setOutputDocName(model.getDoc() == null ? null : model.getDoc()
				.getName());
		dto.setOutputDocId(model.getDoc() == null ? null : model.getDoc()
				.getId());
		dto.setOutputRefId(model.getDoc() == null ? null : model.getDoc()
				.getRefId());
		dto.setProcessDefId(model.getProcessDef().getId());
		dto.setProcessRefId(model.getProcessDef().getRefId());
		dto.setSequenceNo(model.getSequenceNo());
		dto.setStepName(model.getStepName());

		return dto;
	}

	private static TaskStepModel getStep(TaskStepDTO step) {
		ProcessDaoImpl dao = DB.getProcessDao();

		TaskStepModel model = new TaskStepModel();
		if (step.getRefId() != null) {
			model = dao.findByRefId(step.getRefId(), TaskStepModel.class);
		} else if (step.getId() != null) {
			model = dao.getById(TaskStepModel.class, step.getId());
		}

		if (model == null) {
			model = new TaskStepModel();
		}

		model.setRefId(step.getRefId());
		model.setCondition(step.getCondition());
		model.setFormRef(step.getFormRefId());
		if (step.getOutputRefId() != null) {
			ADOutputDoc doc = dao.findByRefId(step.getOutputRefId(),
					ADOutputDoc.class);
			model.setDoc(doc);
		} else {
			model.setDoc(step.getOutputDocId() == null ? null : dao.getById(
					ADOutputDoc.class, step.getOutputDocId()));
		}

		model.setMode(step.getMode());
		model.setNodeId(step.getNodeId());
		model.setProcessDef(dao.getById(ProcessDefModel.class,
				step.getProcessDefId()));
		model.setSequenceNo(step.getSequenceNo());
		model.setStepName(step.getStepName());

		return model;
	}

	public static List<TaskStepDTO> getSteps(String processId, Long nodeId) {
		ProcessDaoImpl dao = DB.getProcessDao();
		List<TaskStepModel> list = dao.getTaskSteps(processId, nodeId);
		List<TaskStepDTO> dtos = new ArrayList<>();

		for (TaskStepModel model : list) {
			dtos.add(getStep(model));
		}

		return dtos;
	}

	public static List<TaskStepDTO> getTaskStepsByTaskId(Long taskId) {
		Task task = JBPMHelper.get().getSysTask(taskId);
		Node node = null;

		try {
			node = JBPMHelper.get().getNode(task);
		} catch (Exception e) {
			log.warn(e.getMessage());
		}

		if (node == null) {
			return new ArrayList<>();
		}

		List<TaskStepModel> models = DB.getProcessDao().getTaskSteps(
				task.getTaskData().getProcessId(), node.getId());

		List<TaskStepDTO> steps = new ArrayList<>();
		for (TaskStepModel m : models) {
			steps.add(getStep(m));
		}
		return steps;
	}
	
	public static List<TaskStepDTO> getTaskStepsByProcessInstanceId(Long processInstanceId){
		String processId = DB.getProcessDao().getProcessId(processInstanceId);
		log.trace("Get PID = "+processInstanceId+", ProcessId="+processId);
		List<TaskLog> instanceTasks = getProcessLog(processInstanceId, "en-UK");
		
		List<Long> nodeIds = new ArrayList<>();
		for(TaskLog instance: instanceTasks) {
			Long taskId = instance.getTaskId();
			Task task = JBPMHelper.get().getSysTask(taskId);
			Node node = JBPMHelper.get().getNode(task);
			
			if(nodeIds.contains(node.getId())) {
				continue;//Avoid processing same node more than once
			}
			nodeIds.add(node.getId());
		}
		
		log.trace("GET TaskSteps for "+processId+", nodeIds = "+nodeIds);
		List<TaskStepModel> steps = DB.getProcessDao().getTaskStepsForNodes(processId, nodeIds);
		List<TaskStepModel> uniqueSteps = new ArrayList<>();
		
		List<String> forms = new ArrayList<>();
		for(TaskStepModel model: steps) {
			String formRef = model.getFormRef();
			if(formRef==null) {
				continue;
			}
			
			if(forms.contains(formRef)) {
				continue;
			}
			
			forms.add(formRef);
			log.trace("TaskStep ID - "+model.getId()+" :: "+model.getRefId());
			uniqueSteps.add(model);
		}
		
		List<TaskStepDTO> stepDtos = new ArrayList<>();
		for (TaskStepModel m : uniqueSteps) {
			stepDtos.add(getStep(m));
		}
		return stepDtos;
	}

	public static List<TaskStepDTO> getTaskStepsByDocumentJson(String docRefId) {
		Document docModel = DB.getDocumentDao().getDocJson(docRefId, false);

		String processId = docModel.getProcessId();
		if (processId == null && docModel.getType() != null) {
			processId = docModel.getType() == null ? docModel.getProcessId()
					: docModel.getType().getProcessId();
		}
		Long nodeId = null;
		List<TaskStepModel> models = DB.getProcessDao().getTaskSteps(processId,
				nodeId);

		List<TaskStepDTO> steps = new ArrayList<>();
		for (TaskStepModel m : models) {
			steps.add(getStep(m));
		}
		return steps;
	}

	/**
	 * Please use {@link #getTaskStepsByDocumentJson(String)}
	 * 
	 * 
	 * @param documentId
	 * @return
	 */
	@Deprecated
	public static List<TaskStepDTO> getTaskStepsByDocumentId(Long documentId) {

		DocumentModel docModel = DB.getDocumentDao().getById(documentId);

		String processId = docModel.getProcessId();
		if (processId == null && docModel.getType() != null) {
			processId = docModel.getType().getProcessDef().getProcessId();
		}
		List<TaskStepModel> models = DB.getProcessDao().getTaskStepsForNodes(processId,
				null);

		List<TaskStepDTO> steps = new ArrayList<>();
		for (TaskStepModel m : models) {
			steps.add(getStep(m));
		}
		return steps;
	}

	public static List<TaskStepDTO> getTaskStepsByDocRefId(String docRefId) {

		// DocumentModel docModel = DB.getDocumentDao().findByRefId(docRefId,
		// DocumentModel.class);

		Document docModel = DB.getDocumentDao().getDocJson(docRefId, false);

		String processId = docModel.getProcessId();
		if (processId == null && docModel.getType() != null) {
			processId = docModel.getType().getProcessId();
		}
		List<TaskStepModel> models = DB.getProcessDao().getTaskStepsForNodes(processId,
				null);

		List<TaskStepDTO> steps = new ArrayList<>();
		for (TaskStepModel m : models) {
			steps.add(getStep(m));
		}
		return steps;
	}

	public static List<Trigger> getTriggers() {
		ProcessDaoImpl dao = DB.getProcessDao();

		List<Trigger> triggers = new ArrayList<>();
		List<ADTrigger> adTriggers = dao.getTriggers();

		for (ADTrigger adtrigger : adTriggers) {
			triggers.add(getTrigger(adtrigger));
		}

		return triggers;
	}

	public static List<Trigger> getTriggers(String processRefId,
			String searchTerm) {
		ProcessDaoImpl dao = DB.getProcessDao();

		List<Trigger> triggers = new ArrayList<>();
		List<ADTrigger> adTriggers = dao.getTriggers(processRefId, searchTerm);

		for (ADTrigger adtrigger : adTriggers) {
			triggers.add(getTrigger(adtrigger));
		}

		return triggers;
	}

	// public static List<Trigger> getTriggers(Long taskStepId, TriggerType
	// type) {
	//
	// ProcessDaoImpl dao = DB.getProcessDao();
	//
	// List<Trigger> triggers = new ArrayList<>();
	// List<ADTrigger> adTriggers = dao.getTriggers(taskStepId, type);
	//
	// for(ADTrigger adtrigger: adTriggers){
	// triggers.add(getTrigger(adtrigger));
	// }
	//
	// return triggers;
	// }

	public static Trigger saveTrigger(Trigger trigger) {
		ProcessDaoImpl dao = DB.getProcessDao();
		ADTrigger po = getTrigger(trigger);

		if (trigger.isActive()) {
			dao.save(po);
		} else {
			dao.cascadeDeleteTrigger(po);
			return trigger;
		}

		Trigger saved = getTrigger(po);

		return saved;
	}

	private static Trigger getTrigger(ADTrigger po) {

		Trigger trigger = new Trigger();
		trigger.setId(po.getId());
		trigger.setRefId(po.getRefId());
		trigger.setName(po.getName());
		trigger.setScript(po.getScript());
		trigger.setImports(po.getImports());
		trigger.setProcessRefId(po.getProcessRefId());
		return trigger;
	}

	private static ADTrigger getTrigger(Trigger trigger) {

		ProcessDaoImpl dao = DB.getProcessDao();
		ADTrigger po = new ADTrigger();

		if (trigger.getId() != null) {
			po = dao.getById(ADTrigger.class, trigger.getId());
		}

		po.setProcessRefId(trigger.getProcessRefId());
		po.setName(trigger.getName());
		po.setImports(trigger.getImports());
		po.setScript(trigger.getScript());
		return po;
	}

	public static List<TaskStepTrigger> getTaskStepTriggers(Long taskStepId,
			TriggerType type) {
		ProcessDaoImpl dao = DB.getProcessDao();

		Collection<ADTaskStepTrigger> adTriggers = dao.getTaskStepTriggers(
				taskStepId, type);

		List<TaskStepTrigger> triggers = new ArrayList<>();
		for (ADTaskStepTrigger adTrigger : adTriggers) {
			TaskStepTrigger trigger = getStepTrigger(adTrigger);
			triggers.add(trigger);
		}

		return triggers;
	}

	public static int getTriggerCount(Long taskStepId, TriggerType type) {

		ProcessDaoImpl dao = DB.getProcessDao();

		return dao.getTaskCount(taskStepId, type);
	}

	public static TaskStepTrigger saveTaskStepTrigger(TaskStepTrigger trigger) {
		ProcessDaoImpl dao = DB.getProcessDao();

		ADTaskStepTrigger adTaskStep = getStepTrigger(trigger);

		if (trigger.isActive()) {
			dao.save(adTaskStep);
		} else {

			dao.delete(adTaskStep);
			return trigger;
		}

		return getStepTrigger(adTaskStep);
	}

	private static TaskStepTrigger getStepTrigger(ADTaskStepTrigger adTaskStep) {

		TaskStepTrigger stepTrigger = new TaskStepTrigger();
		stepTrigger.setTrigger(getTrigger(adTaskStep.getTrigger()));
		stepTrigger.setTaskStepId(adTaskStep.getTaskStep().getId());
		stepTrigger.setType(adTaskStep.getType());
		stepTrigger.setId(adTaskStep.getId());
		stepTrigger.setRefId(adTaskStep.getRefId());
		stepTrigger.setCondition(adTaskStep.getCondition());

		return stepTrigger;
	}

	private static ADTaskStepTrigger getStepTrigger(TaskStepTrigger stepTrigger) {
		ProcessDaoImpl dao = DB.getProcessDao();
		ADTaskStepTrigger adTaskStepTrigger = new ADTaskStepTrigger();

		if (stepTrigger.getId() != null) {
			adTaskStepTrigger = dao.getById(ADTaskStepTrigger.class,
					stepTrigger.getId());
		} else if (stepTrigger.getRefId() != null) {
			adTaskStepTrigger = dao.findByRefId(stepTrigger.getRefId(),
					ADTaskStepTrigger.class);
		}
		if (adTaskStepTrigger == null) {
			adTaskStepTrigger = new ADTaskStepTrigger();
		}
		adTaskStepTrigger.setType(stepTrigger.getType());
		adTaskStepTrigger.setCondition(stepTrigger.getCondition());

		// Step
		TaskStepModel stepModel = dao.getById(TaskStepModel.class,
				stepTrigger.getTaskStepId());
		adTaskStepTrigger.setTaskStep(stepModel);

		// trigger
		ADTrigger trigger = dao.getById(ADTrigger.class, stepTrigger
				.getTrigger().getId());
		adTaskStepTrigger.setTrigger(trigger);

		return adTaskStepTrigger;
	}

	public static TaskNotification saveTaskNotification(
			TaskNotification notification) {
		ProcessDaoImpl dao = DB.getProcessDao();
		ADTaskNotification model = getTaskNotificationModel(notification);
		dao.save(model);
		return getTaskNotification(model);
	}

	private static TaskNotification getTaskNotification(ADTaskNotification model) {

		TaskNotification notification = new TaskNotification();
		if (model == null) {
			return notification;
		}
		notification.setId(model.getId());
		notification.setAction(model.getAction());
		notification.setCategory(model.getCategory());
		notification.setEnableNotification(model.isEnableNotification());
		notification.setNotificationTemplate(model.getNotificationTemplate());
		notification.setTargets(model.getReceipients());
		notification
				.setUseDefaultNotification(model.isUseDefaultNotification());
		notification.setNodeId(model.getNodeId());
		notification.setStepName(model.getStepName());
		notification.setProcessDefId(model.getProcessDefId());
		notification.setSubject(model.getSubject());

		return notification;
	}

	private static ADTaskNotification getTaskNotificationModel(
			TaskNotification notification) {

		ProcessDaoImpl dao = DB.getProcessDao();
		ADTaskNotification model = new ADTaskNotification();
		if (notification.getRefId() != null) {
			model = dao.findByRefId(notification.getRefId(),
					ADTaskNotification.class);
		} else if (notification.getId() != null) {
			model = dao.getTaskNotificationById(notification.getId());
		}

		if (model == null) {
			model = new ADTaskNotification();
		}

		model.setAction(notification.getAction());
		model.setCategory(notification.getCategory());
		model.setEnableNotification(notification.isEnableNotification());
		model.setNotificationTemplate(notification.getNotificationTemplate());
		model.setReceipients(notification.getTargets());
		model.setUseDefaultNotification(notification.isUseDefaultNotification());
		model.setNodeId(notification.getNodeId());
		model.setStepName(notification.getStepName());
		model.setProcessDefId(notification.getProcessDefId());
		model.setSubject(notification.getSubject());

		return model;
	}

	public static TaskNotification getTaskNotificationTemplate(Long nodeId,
			String stepName, Long processDefId, NotificationCategory category,
			Actions action) {
		ProcessDaoImpl dao = DB.getProcessDao();
		ADTaskNotification model = dao.getTaskNotification(nodeId, stepName,
				processDefId, category, action);
		return getTaskNotification(model);
	}

	public static List<TaskLog> getProcessLog(Long processInstanceId,
			String language) {
		return DB.getProcessDao().getProcessLog(processInstanceId, language);
	}

	public static void deleteTaskNotification(Long notificationId) {

		ADTaskNotification notification = DB.getProcessDao().getById(
				ADTaskNotification.class, notificationId);
		DB.getProcessDao().delete(notification);
	}

	public static AssignmentDto create(AssignmentDto dto) {
		ProcessDaoImpl dao = DB.getProcessDao();
		AssignmentPO po = new AssignmentPO();
		if (po.getRefId() != null) {
			po = dao.findByRefId(po.getRefId(), AssignmentPO.class);
		}

		po.setNodeId(dto.getNodeId());
		po.setStepName(dto.getTaskName());
		ProcessDefModel model = dao.findByRefId(dto.getProcessRefId(),
				ProcessDefModel.class);
		po.setProcessDef(model);
		po.setAssignmentFunction(dto.getFunction());
		dao.save(po);

		return getAssignment(po);
	}

	private static AssignmentDto getAssignment(AssignmentPO po) {
		AssignmentDto dto = new AssignmentDto();
		dto.setFunction(po.getAssignmentFunction());
		dto.setNodeId(po.getNodeId());
		dto.setProcessRefId(po.getProcessDef().getRefId());
		dto.setRefId(dto.getRefId());
		dto.setTaskName(po.getStepName());

		return dto;
	}

	public static AssignmentDto getAssignment(String processRefId, Long nodeId) {
		ProcessDaoImpl dao = DB.getProcessDao();
		AssignmentPO po = dao.getAssignment(processRefId, nodeId);

		if (po != null)
			return getAssignment(po);

		return null;
	}

	// public static void exportProcess(String processRefId) {
	// ProcessDaoImpl dao = DB.getProcessDao();
	// ProcessDefModel model = dao.getProcessDef(dao
	// .getProcessDefId(processRefId));
	// List<LocalAttachment> attachments = DB.getAttachmentDao()
	// .getAttachmentsForProcessDef(model);
	// List<LocalAttachment> images = DB.getAttachmentDao()
	// .getAttachmentsForProcessDef(model, true);
	// attachments.addAll(images);
	//
	// // Steps
	// Collection<TaskStepModel> taskSteps = model.getTaskSteps();
	//
	// String folder = "/home/duggan/Projects/WIRA/jaxbexport/";
	// String processXmlName = model.getName() + ".xml";
	// String attachmentsFolder = folder + "attachments/";
	// String stepsFolder = folder + "steps/";
	// String formsFolder = folder + "forms/";
	// String outputDocsFolder = folder + "outputdocs/";
	// String triggersFolder = folder + "triggers/";
	//
	// try {
	// // Attachments
	// for (LocalAttachment a : attachments) {
	// model.addAttachmentName(a.getName());
	// }
	//
	// // Process
	// IOUtils.write(exportObject(model), new FileOutputStream(new File(
	// folder + processXmlName)));
	//
	// // Task Steps
	// for (TaskStepModel step : taskSteps) {
	// // ADForm form = step.getForm();
	// Form form = FormDaoHelper.getFormJson(step.getRefId(), false);
	// ADOutputDoc doc = step.getDoc();
	//
	// Collection<ADTaskStepTrigger> stepTriggers = step
	// .getTaskStepTriggers();
	// log.debug("### " + step.getNodeId() + " - "
	// + stepTriggers.size());
	// // dao.getTaskStepTriggers(step.getId(),
	// // TriggerType.BEFORESTEP);
	// // stepTriggers.addAll(dao.getTaskStepTriggers(step.getId(),
	// // TriggerType.AFTERSTEP));
	//
	// for (ADTaskStepTrigger stepTrigger : stepTriggers) {
	// stepTrigger.setTriggerName(stepTrigger.getTrigger()
	// .getName());
	// log.warn(">>>" + stepTrigger.getTriggerName());
	// }
	//
	// String name = "ProcessInitiation";
	// if (form != null) {
	// name = form.getName();
	// }
	//
	// if (doc != null) {
	// name = doc.getName();
	// }
	//
	// String stepName = step.getSequenceNo() + "_" + name + ".xml";
	// if (step.getNodeId() != null) {
	// stepName = step.getNodeId() + "_" + stepName;
	// } else {
	// stepName = "00" + "_" + stepName;
	// }
	// // step.setFormRefId(step.getForm()==null? null:
	// // step.getForm().getRefId());
	// step.setOutputRefId(step.getDoc() == null ? null : step
	// .getDoc().getRefId());
	// IOUtils.write(exportObject(step), new FileOutputStream(
	// new File(stepsFolder + stepName)));
	// }
	//
	// // Forms
	// // List<ADForm> forms = DB.getFormDao().getAllForms(model.getId());
	// List<Form> forms = FormDaoHelper.getFormsJson(model.getRefId(),
	// true);
	// for (Form form : forms) {
	// String name = form.getName() + ".xml";
	// IOUtils.write(exportObject(form), new FileOutputStream(
	// new File(formsFolder + name)));
	// }
	//
	// List<ADOutputDoc> docs = DB.getOutputDocDao().getOutputDocuments(
	// model.getRefId(), null);
	// for (ADOutputDoc doc : docs) {
	// String name = doc.getName() + ".xml";
	// IOUtils.write(exportObject(doc), new FileOutputStream(new File(
	// outputDocsFolder + name)));
	// if (doc.getAttachment() != null) {
	// attachments.add(doc.getAttachment());
	// }
	// }
	//
	// List<ADTrigger> triggers = DB.getProcessDao().getTriggers(
	// model.getRefId(), null);
	// for (ADTrigger trigger : triggers) {
	// String name = trigger.getName() + ".xml";
	// IOUtils.write(exportObject(trigger), new FileOutputStream(
	// new File(triggersFolder + name)));
	// }
	//
	// // Attachments
	// for (LocalAttachment a : attachments) {
	// IOUtils.write(a.getAttachment(), new FileOutputStream(new File(
	// attachmentsFolder + a.getName())));
	// }
	//
	// } catch (IOException | JAXBException e) {
	// throw new RuntimeException(e);
	// }
	// }
	//
	// private static byte[] exportObject(Object model) throws JAXBException,
	// IOException {
	// JAXBContext context = new JaxbFormExportProviderImpl().getContext(model
	// .getClass());
	// String out = null;
	// Marshaller marshaller = context.createMarshaller();
	// marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	//
	// StringWriter writer = new StringWriter();
	// marshaller.marshal(model, writer);
	//
	// out = writer.toString();
	// writer.close();
	// return out.getBytes();
	// }

	public static List<Schema> getProcessSchema(String processRefId) {
		return DB.getFormDao().getProcessSchema(processRefId);
	}

	public static String exportProcessJson(String processRefId)
			throws JSONException {
		ProcessDaoImpl dao = DB.getProcessDao();
		ProcessDefModel model = dao.findByRefId(processRefId,
				ProcessDefModel.class);
		ProcessDef processDef = get(model, true);

		List<Attachment> files = processDef.getFiles();// Does not include

		// process svg image
		List<LocalAttachment> imageFiles = DB.getAttachmentDao()
				.getAttachmentsForProcessDef(model, true);
		for (LocalAttachment a : imageFiles) {
			files.add(AttachmentDaoHelper.get(a));
		}

		JSONObject process = new JSONObject();
		process.put(ProcessDef.ID, processDef.getRefId());
		process.put(ProcessDef.NAME, processDef.getName());
		process.put(ProcessDef.PROCESSID, processDef.getProcessId());
		process.put(ProcessDef.BACKGROUNDCOLOR, processDef.getBackgroundColor());
		process.put(ProcessDef.ICONSTYLE, processDef.getIconStyle());
		process.put(ProcessDef.FILENAME, processDef.getFileName());
		process.put(ProcessDef.DESCRIPTION, processDef.getDescription());
		process.put(ProcessDef.IMAGENAME, processDef.getImageName());
		process.put(ProcessDef.CATEGORY,
				processDef.getCategory() == null ? null : processDef
						.getCategory().getName());

		JSONArray processAttachments = new JSONArray();
		for (Attachment a : processDef.getFiles()) {
			JSONObject att = new JSONObject();
			att.put(Attachment.ID, a.getRefId());
			att.put(Attachment.NAME, a.getName());
			att.put(Attachment.TYPE, a.getType());
			att.put(Attachment.SIZE, a.getSize());
			att.put(Attachment.CONTENTTYPE, a.getContentType());
			att.put(Attachment.PATH, a.getPath());
			processAttachments.put(att);
			// Process SVG Image not included
		}
		// Process BPMN2 & SVG Metadata
		process.put("attachments", processAttachments);

		// Access Rights
		ArrayList<Listable> usersAndGroups = processDef.getUsersAndGroups();
		JSONArray access = new JSONArray();
		for (Listable userOrGroup : usersAndGroups) {
			if (userOrGroup instanceof HTUser) {
				access.put(((HTUser) userOrGroup).getUserId());
			} else {
				access.put(userOrGroup.getName());
			}

		}
		process.put("access", access);// Access info

		// Task Steps
		ArrayList<TaskStepDTO> taskSteps = getSteps(processRefId);
		JSONArray steps = new JSONArray();
		for (TaskStepDTO dto : taskSteps) {
			JSONObject obj = new JSONObject();
			obj.put(TaskStepDTO.ID, dto.getRefId());
			obj.put(TaskStepDTO.NODEID, dto.getNodeId());
			obj.put(TaskStepDTO.STEPNAME, dto.getStepName());
			obj.put(TaskStepDTO.SEQUENCENO, dto.getSequenceNo());
			obj.put(TaskStepDTO.MODE, dto.getMode() == null ? MODE.EDIT.name()
					: dto.getMode().name());
			obj.put(TaskStepDTO.CONDITION, dto.getCondition());
			obj.put(TaskStepDTO.FORMNAME, dto.getFormName());
			obj.put(TaskStepDTO.FORMREF, dto.getFormRefId());
			obj.put(TaskStepDTO.OUTPUTNAME, dto.getOutputDocName());
			obj.put(TaskStepDTO.OUTPUTREF, dto.getOutputRefId());
			steps.put(obj);

			// Step Triggers
			List<TaskStepTrigger> triggers = getTaskStepTriggers(dto.getId(),
					null);
			JSONArray trigg = new JSONArray();
			for (TaskStepTrigger trigger : triggers) {
				JSONObject item = new JSONObject();
				item.put(TaskStepTrigger.ID, trigger.getRefId());
				item.put(TaskStepTrigger.TRIGGER, trigger.getTrigger()
						.getName());
				item.put(TaskStepTrigger.CONDITION, trigger.getCondition());
				item.put(TaskStepTrigger.TYPE, trigger.getType().name());
				trigg.put(item);
			}
			// Step Triggers
			obj.put("triggers", trigg);
		}

		// Steps
		process.put("steps", steps);

		List<TaskNotification> notificationTemplates = getTaskNotificationTemplates(processDef
				.getId());
		JSONArray notificationArr = new JSONArray();
		for (TaskNotification template : notificationTemplates) {
			JSONObject obj = new JSONObject();
			obj.put(TaskNotification.ID, template.getRefId());
			obj.put(TaskNotification.CATEGORY, template.getCategory().name());
			obj.put(TaskNotification.ENABLENOTIFICATION,
					template.isEnableNotification());
			obj.put(TaskNotification.ACTION, template.getAction().name());
			obj.put(TaskNotification.DEFAULTANOTIFICATION,
					template.isUseDefaultNotification());
			obj.put(TaskNotification.TEMPLATE,
					template.getNotificationTemplate());
			obj.put(TaskNotification.NODEID, template.getNodeId());
			obj.put(TaskNotification.STEPNAME, template.getStepName());
			obj.put(TaskNotification.SUBJECT, template.getSubject());
			obj.put(TaskNotification.RECIPIENTS, template.getTargets());
			notificationArr.put(obj);
		}
		// Notifications
		process.put("notifications", notificationArr);

		return process.toString();
	}

	private static List<TaskNotification> getTaskNotificationTemplates(
			Long processDefId) {
		assert processDefId != null;
		List<ADTaskNotification> adTaskNotes = DB.getProcessDao()
				.getTaskNotifications(processDefId);
		List<TaskNotification> taskNotes = new ArrayList<TaskNotification>();
		for (ADTaskNotification template : adTaskNotes) {
			taskNotes.add(getTaskNotification(template));
		}

		return taskNotes;
	}

	private static ArrayList<TaskStepDTO> getSteps(String processRefId) {
		List<TaskStepModel> taskSteps = DB.getProcessDao()
				.getTaskStepsByProcessRef(processRefId);
		ArrayList<TaskStepDTO> steps = new ArrayList<TaskStepDTO>();
		for (TaskStepModel model : taskSteps) {
			TaskStepDTO taskStep = getStep(model);
			steps.add(taskStep);
		}

		return steps;
	}

	public static void exportProcessAsZip(String processRefId,
			OutputStream outputStream) throws IOException {

		List<OutputDocument> docs = OutputDocumentDaoHelper
				.getDocuments(processRefId);
		List<Trigger> triggers = ProcessDaoHelper.getTriggers(processRefId,
				null);
		List<Form> forms = FormDaoHelper.getFormsJson(processRefId, true);
		String processMeta = null;
		try {
			processMeta = exportProcessJson(processRefId);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}

		ZipOutputStream zos = new ZipOutputStream(outputStream);
		addZipEntry(zos, "process.json", processMeta.getBytes());
		// Triggers
		for (Trigger t : triggers) {
			String triggerMeta = null;
			try {
				triggerMeta = exportProcessTriggersJson(t);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			addZipEntry(zos, "triggers/" + t.getName() + ".json",
					triggerMeta.getBytes());
		}

		// Output Documents
		for (OutputDocument doc : docs) {
			String template = OutputDocumentDaoHelper
					.getHTMLTemplateByRefId(doc.getRefId());
			doc.setTemplate(template);
			String outputMeta = null;
			try {
				outputMeta = exportProcessOutputJson(doc);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			addZipEntry(zos, "outputs/" + doc.getName() + ".json",
					outputMeta.getBytes());
		}

		// Forms
		assert !forms.isEmpty();
		for (Form form : forms) {
			String formMeta = exportFormJson(form);
			addZipEntry(zos, "forms/" + form.getName() + ".json",
					formMeta.getBytes());
		}

		// Process Attachments
		// Attach files
		try {
			JSONObject process = new JSONObject(processMeta);
			JSONArray arr = process.getJSONArray("attachments");
			for (int i = 0; i < arr.length(); i++) {
				JSONObject attachment = arr.getJSONObject(i);
				String attachmentRefId = attachment.getString(Attachment.ID);
				String name = attachment.getString(Attachment.NAME);
				LocalAttachment a = DB.getAttachmentDao().findByRefId(
						attachmentRefId, LocalAttachment.class);
				name = format(name);
				addZipEntry(zos, name, a.getAttachment());
			}
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}

		zos.close();
	}

	private static String format(String input) {
		// input = input.replaceAll("\\s", "_");
		return input;
	}

	static String exportFormJson(Form form) {
		String out = null;
		try {
			JSONMarshaller marshaller = JsonForm.getJaxbContext()
					.createJSONMarshaller();
			final StringWriter w = new StringWriter();
			marshaller.marshallToJSON(form, w);
			w.flush();
			JSONObject formJson = new JSONObject(w.toString());
			JSONArray arr = new JSONArray();
			if (form.getFields() != null)
				for (Field field : form.getFields()) {
					String fieldJson = exportFieldJson(field);
					JSONObject obj = new JSONObject(fieldJson);
					appendChildren(obj, field);
					arr.put(obj);
				}
			formJson.put("fields", arr);
			out = formJson.toString();
		} catch (JAXBException | JSONException ex) {
			throw new RuntimeException(ex);
		}
		return out;
	}

	/**
	 * Recursively append children fields into a parent
	 * 
	 * @param parentJson
	 * @param parent
	 * @throws JSONException
	 */
	private static void appendChildren(JSONObject parentJson, Field parent)
			throws JSONException {
		if (parent.getFields() == null || parent.getFields().isEmpty()) {
			return;
		}

		JSONArray children = new JSONArray();
		for (Field field : parent.getFields()) {
			String fieldJson = exportFieldJson(field);
			JSONObject obj = new JSONObject(fieldJson);
			children.put(obj);
			appendChildren(obj, field);
		}
		parentJson.put("fields", children);
	}

	private static String exportFieldJson(Field field) {
		String out = null;
		try {
			JSONMarshaller marshaller = JsonForm.getJaxbContext()
					.createJSONMarshaller();
			final StringWriter w = new StringWriter();
			marshaller.marshallToJSON(field, w);
			w.flush();
			out = w.toString();
		} catch (JAXBException ex) {
			throw new RuntimeException(ex);
		}
		return out;
	}

	private static void addZipEntry(ZipOutputStream zos, String entryName,
			byte[] entryContent) throws IOException {
		ZipEntry entry = new ZipEntry(entryName);
		zos.putNextEntry(entry);
		zos.write(entryContent);
		zos.closeEntry();
	}

	private static String exportProcessOutputJson(OutputDocument doc)
			throws JSONException {

		JSONObject obj = new JSONObject();
		obj.put(OutputDocument.ID, doc.getRefId());
		obj.put(OutputDocument.NAME, doc.getName());
		obj.put(OutputDocument.CODE, doc.getCode());
		obj.put(OutputDocument.DESCRIPTION, doc.getDescription());
		obj.put(OutputDocument.ATTACHMENTNAME, doc.getAttachmentName());
		obj.put(OutputDocument.PATH, doc.getPath());
		obj.put(OutputDocument.TEMPLATE, doc.getTemplate());
		return obj.toString();
	}

	private static String exportProcessTriggersJson(Trigger trigger)
			throws JSONException {

		JSONObject t = new JSONObject();
		t.put(Trigger.ID, trigger.getRefId());
		t.put(Trigger.NAME, trigger.getName());
		t.put(Trigger.IMPORTS, trigger.getImports());
		t.put(Trigger.SCRIPT, trigger.getScript());

		return t.toString();
	}

	public static void exportProcessAsFile(String processRefId, String fileName)
			throws IOException {
		FileOutputStream fos = new FileOutputStream(new File(fileName));
		exportProcessAsZip(processRefId, fos);
		fos.close();
	}

	private static final String OUTPUT_FOLDER = "wiraimportzip";

	public static void importProcessAsStream(String name, long size,
			InputStream is) throws IOException {
		log.info("Importing process from inputstream {name:" + name + ", size:"
				+ (size / 1024) + "kb}");
		Set<PosixFilePermission> perms = new HashSet<>();
		// add permission as rw-r--r-- 644
		perms.add(PosixFilePermission.OWNER_WRITE);
		perms.add(PosixFilePermission.OWNER_READ);
		perms.add(PosixFilePermission.OWNER_EXECUTE);
		perms.add(PosixFilePermission.GROUP_READ);
		perms.add(PosixFilePermission.GROUP_WRITE);
		perms.add(PosixFilePermission.GROUP_EXECUTE);
		perms.add(PosixFilePermission.OTHERS_READ);
		perms.add(PosixFilePermission.OTHERS_EXECUTE);
		FileAttribute<Set<PosixFilePermission>> fileAttributes = PosixFilePermissions
				.asFileAttribute(perms);

		Path zipFilePath = Files.createTempFile(name, null, fileAttributes);
		OpenOption[] options = new OpenOption[] { StandardOpenOption.WRITE,
				StandardOpenOption.CREATE };
		OutputStream fos = Files.newOutputStream(zipFilePath, options);
		final byte[] buffer = new byte[1024];
		int len;
		while ((len = is.read(buffer)) > 0) {
			fos.write(buffer, 0, len);
		}
		fos.close();
		is.close();

		importProcessAsZip(zipFilePath.toString());

		zipFilePath.toFile().delete();
	}

	public static void importProcessAsZip(String zipFileName) {

		log.info("Importing zip file " + zipFileName);
		// create output directory is not exists

		Set<PosixFilePermission> perms = new HashSet<>();
		// add permission as rw-r--r-- 644
		perms.add(PosixFilePermission.OWNER_WRITE);
		perms.add(PosixFilePermission.OWNER_READ);
		perms.add(PosixFilePermission.OWNER_EXECUTE);
		perms.add(PosixFilePermission.GROUP_READ);
		perms.add(PosixFilePermission.GROUP_WRITE);
		perms.add(PosixFilePermission.GROUP_EXECUTE);
		perms.add(PosixFilePermission.OTHERS_READ);
		perms.add(PosixFilePermission.OTHERS_EXECUTE);
		FileAttribute<Set<PosixFilePermission>> fileAttributes = PosixFilePermissions
				.asFileAttribute(perms);

		ZipFile zipFile = null;
		Path root = null;
		try {
			zipFile = new ZipFile(zipFileName);

			// root = Files.createDirectories(Paths.get(OUTPUT_FOLDER),
			// fileAttributes);
			root = Files.createTempDirectory(OUTPUT_FOLDER, fileAttributes);
			log.debug("Creating temporary working directory " + root.toString());

			// File folder = path.toFile();
			// folder.deleteOnExit();

			for (Enumeration<? extends ZipEntry> e = zipFile.entries(); e
					.hasMoreElements();) {
				ZipEntry zipEntry = e.nextElement();

				String fileName = zipEntry.getName();
				// File newFile = File.createTempFile(OUTPUT_FOLDER +
				// File.separator
				// + fileName,"", folder);
				// newFile.deleteOnExit();

				// Might be process.json
				// or
				// triggers/triggers.json
				Path newFilePath = root.resolve(fileName);

				// File newFile = newFilePath.toFile();
				log.debug("file unzip : " + newFilePath.toString());
				// log.debug("file unzip : " + newFile.getAbsoluteFile());

				// create all non exists folders
				// else you will hit FileNotFoundException for compressed folder
				if (newFilePath.getParent() != null) {
					Files.createDirectories(newFilePath.getParent(),
							fileAttributes);
				}

				// new File(newFile.getParent()).mkdirs();
				OpenOption[] options = new OpenOption[] {
						StandardOpenOption.WRITE, StandardOpenOption.CREATE };

				// try with resources statement will automatically close the
				// stream
				OutputStream fos = Files.newOutputStream(newFilePath, options);
				InputStream is = zipFile.getInputStream(zipEntry);
				final byte[] buffer = new byte[1024];
				int len;
				while ((len = is.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}
				fos.close();
				is.close();
			}

			importProcess(root);
		} catch (FileSystemException fse) {
			log.fatal("importProcessAsZip threw FileSystemException msg: "
					+ fse.getMessage() + ": reason " + fse.getReason()
					+ ": otherFile " + fse.getOtherFile());
			throw new RuntimeException(fse);
		} catch (IOException ioe) {
			log.fatal("importProcessAsZip threw IOException: "
					+ ioe.getMessage());
			throw new RuntimeException(ioe);
		} finally {
			try {
				zipFile.close();
			} catch (IOException e) {
				log.fatal("importProcessAsZip unable to close ZipInputStream: "
						+ e.getMessage());
				e.printStackTrace();
			}
		}

		try {
			log.debug("Cleaning up working directory " + root.toString());
			FileUtils.deleteDirectory(root.toFile());
		} catch (IOException e) {
			log.warn("Unable to clean working directory " + root.toString()
					+ ", cause: " + e.getMessage());
		}

	}

	/**
	 * Import Process
	 * 
	 * @param rootPath
	 */
	private static void importProcess(Path rootPath) {
		Path processPath = rootPath.resolve("process.json");
		try {

			byte[] processBytes = Files.readAllBytes(processPath);
			String processJsonStr = new String(processBytes);
			JSONObject processJson = new JSONObject(processJsonStr);

			log.info("Importing process ID: "
					+ processJson.getString(ProcessDef.ID) + ", Name:"
					+ processJson.getString(ProcessDef.NAME));
			ImportLogger.log("Importing process ID: "
					+ processJson.getString(ProcessDef.ID) + ", Name:"
					+ processJson.getString(ProcessDef.NAME));

			ProcessDef processDef = getProcessDef(processJson
					.getString(ProcessDef.ID));
			if (processDef == null) {
				processDef = new ProcessDef();
			}
			processDef.setRefId(processJson.getString(ProcessDef.ID));
			processDef.setName(processJson.getString(ProcessDef.NAME));
			processDef
					.setProcessId(processJson.optString(ProcessDef.PROCESSID));
			processDef.setBackgroundColor(processJson
					.optString(ProcessDef.BACKGROUNDCOLOR));
			processDef
					.setIconStyle(processJson.optString(ProcessDef.ICONSTYLE));
			processDef.setFileName(processJson.optString(ProcessDef.FILENAME));
			processDef.setDescription(processJson
					.optString(ProcessDef.DESCRIPTION));
			processDef
					.setImageName(processJson.optString(ProcessDef.IMAGENAME));

			String category = processJson.optString(ProcessDef.CATEGORY);
			ProcessCategory cat = getProcessCategoryByName(category);
			if (cat == null) {
				cat = new ProcessCategory();
				cat.setName(category);
				cat.setIndex(category);
				cat = save(cat);
			}

			processDef.setCategory(cat);
			processDef = save(processDef);

			ProcessDefModel processDefModel = DB.getProcessDao().findByRefId(
					processDef.getRefId(), ProcessDefModel.class);
			assert processDefModel != null;

			// Process BPMN2 & SVG
			JSONArray processAttachments = processJson
					.getJSONArray("attachments");
			for (int i = 0; i < processAttachments.length(); i++) {

				// Attachments
				JSONObject att = processAttachments.getJSONObject(i);
				LocalAttachment attachment = new LocalAttachment();
				if (att.getString(Attachment.ID) != null) {
					attachment = DB.getAttachmentDao()
							.findByRefId(att.getString(Attachment.ID),
									LocalAttachment.class);
				}
				if (attachment == null) {
					attachment = new LocalAttachment();
				}

				attachment.setRefId(att.getString(Attachment.ID));
				attachment.setName(att.getString(Attachment.NAME));
				if (att.optString(Attachment.TYPE) != null) {
					attachment.setType(AttachmentType.valueOf(att
							.getString(Attachment.TYPE)));
				}
				attachment.setSize(att.optLong(Attachment.SIZE));
				attachment
						.setContentType(att.getString(Attachment.CONTENTTYPE));
				attachment.setPath(att.optString(Attachment.PATH));
				if (attachment.getName().endsWith("bpmn2")
						|| attachment.getName().endsWith("BPMN2")) {
					attachment.setProcessDef(processDefModel);
				} else {
					attachment.setProcessDefImage(processDefModel);
				}

				String attachmentName = attachment.getName();
				attachmentName = format(attachmentName);
				log.info("importing attachment " + attachmentName);
				ImportLogger.log("importing attachment " + attachmentName);
				attachment.setAttachment(Files.readAllBytes(rootPath
						.resolve(attachmentName)));

				DB.getAttachmentDao().save(attachment);
				// Process SVG Image not included
			}

			importTriggers(rootPath, processDefModel);
			importOutputs(rootPath, processDefModel);
			importForms(rootPath, processDefModel);
			importTaskSteps(processJson, processDefModel);
			importTaskNotifications(processJson, processDefModel);

		} catch (IOException e) {
			log.fatal("Unable to read bytes from " + processPath.toString());
			throw new RuntimeException(e);
		} catch (JSONException jsonEx) {
			log.fatal("Unable to read json from " + processPath.toString());
			throw new RuntimeException(jsonEx);
		}

	}

	private static void importTaskNotifications(JSONObject processJson,
			ProcessDefModel processDefModel) throws JSONException {
		JSONArray notificationArr = processJson.getJSONArray("notifications");
		for (int i = 0; i < notificationArr.length(); i++) {
			JSONObject obj = notificationArr.getJSONObject(i);
			TaskNotification notification = new TaskNotification();
			notification.setCategory(NotificationCategory.valueOf(obj
					.getString(TaskNotification.CATEGORY)));
			notification.setEnableNotification(obj
					.optBoolean(TaskNotification.ENABLENOTIFICATION));
			notification.setAction(Actions.valueOf(obj
					.optString(TaskNotification.ACTION)));
			notification.setUseDefaultNotification(obj
					.optBoolean(TaskNotification.DEFAULTANOTIFICATION));
			notification.setNotificationTemplate(obj
					.optString(TaskNotification.TEMPLATE));
			notification.setNodeId(obj.optLong(TaskNotification.NODEID));
			notification.setStepName(obj.optString(TaskNotification.STEPNAME));
			notification.setSubject(obj.optString(TaskNotification.SUBJECT));
			notification.setProcessDefId(processDefModel.getId());
			notification.setRefId(obj.optString(TaskNotification.ID));
			// notification.setTargets(obj.optString(TaskNotification.RECIPIENTS));
			saveTaskNotification(notification);
		}
	}

	private static void importTaskSteps(JSONObject processJson,
			ProcessDefModel processDefModel) throws JSONException {

		ProcessDaoImpl dao = DB.getProcessDao();

		// Task Steps
		JSONArray steps = processJson.getJSONArray("steps");
		for (int i = 0; i < steps.length(); i++) {
			JSONObject obj = steps.getJSONObject(i);
			log.info("Importing task step "
					+ obj.optString(TaskStepDTO.FORMNAME)
					+ obj.optString(TaskStepDTO.OUTPUTNAME));
			ImportLogger.log("Importing task step "
					+ obj.optString(TaskStepDTO.FORMNAME)
					+ obj.optString(TaskStepDTO.OUTPUTNAME));
			TaskStepModel model = DB.getProcessDao().findByRefId(
					obj.getString(TaskStepDTO.ID), TaskStepModel.class);
			if (model == null) {
				model = new TaskStepModel();
			}

			model.setRefId(obj.getString(TaskStepDTO.ID));
			model.setNodeId(obj.optLong(TaskStepDTO.NODEID));
			if (model.getNodeId() == 0) {
				model.setNodeId(null);
			}
			model.setStepName(obj.optString(TaskStepDTO.STEPNAME));
			model.setSequenceNo(obj.getInt(TaskStepDTO.SEQUENCENO));
			model.setMode(MODE.valueOf(obj.getString(TaskStepDTO.MODE)));
			model.setCondition(obj.optString(TaskStepDTO.CONDITION));
			model.setFormRef(obj.optString(TaskStepDTO.FORMREF));
			model.setOutputRefId(obj.optString(TaskStepDTO.OUTPUTREF));
			if (model.getOutputRefId() != null) {
				ADOutputDoc doc = dao.findByRefId(model.getOutputRefId(),
						ADOutputDoc.class);
				model.setDoc(doc);
			}

			model.setProcessDef(processDefModel);
			dao.createStep(model);

			// Step Triggers
			JSONArray triggersJson = obj.getJSONArray("triggers");
			for (int j = 0; j < triggersJson.length(); j++) {
				JSONObject item = triggersJson.getJSONObject(j);
				log.info("Importing step trigger "
						+ item.getString(TaskStepTrigger.TRIGGER));
				ImportLogger.log("Importing step trigger "
						+ item.getString(TaskStepTrigger.TRIGGER));
				ADTaskStepTrigger trigger = dao.findByRefId(
						item.getString(TaskStepTrigger.ID),
						ADTaskStepTrigger.class);
				if (trigger == null) {
					trigger = new ADTaskStepTrigger();
				}

				trigger.setRefId(item.getString(TaskStepTrigger.ID));
				trigger.setTaskStep(model);
				trigger.setType(TriggerType.valueOf(item
						.getString(TaskStepTrigger.TYPE)));
				ADTrigger adTrigger = DB.getProcessDao().getTrigger(
						item.getString(TaskStepTrigger.TRIGGER));
				trigger.setTrigger(adTrigger);
				trigger.setCondition(item.optString(TaskStepTrigger.CONDITION));
				dao.save(trigger);
			}
		}

	}

	private static void importForms(Path rootPath,
			ProcessDefModel processDefModel) throws IOException {
		// Forms
		Path formsPath = rootPath.resolve("forms");
		LinkOption[] options = new LinkOption[] { LinkOption.NOFOLLOW_LINKS };
		if (Files.notExists(formsPath, options)) {
			return;
		}

		try (DirectoryStream<Path> forms = Files.newDirectoryStream(formsPath)) {
			for (Path path : forms) {
				log.info("Importing form " + path.toString());
				ImportLogger.log("Importing form " + path.getFileName());
				byte[] bytes = Files.readAllBytes(path);
				Form form = null;
				try {
					JSONUnmarshaller unmarshaller = JsonForm.getJaxbContext()
							.createJSONUnmarshaller();

					form = unmarshaller.unmarshalFromJSON(new StringReader(
							new String(bytes)), Form.class);
					form.setProcessRefId(processDefModel.getRefId());

					// Form
					JSONObject formJson = new JSONObject(new String(bytes));

					// fields
					JSONArray fields = formJson.getJSONArray("fields");
					for (int i = 0; i < fields.length(); i++) {
						String fieldJson = fields.getJSONObject(i).toString();
						Field field = unmarshaller.unmarshalFromJSON(
								new StringReader(fieldJson), Field.class);
						importChildren(fields.getJSONObject(i), field);
						form.addField(field);
					}
					FormDaoHelper.createJson(form);
				} catch (JAXBException jaxbEx) {
					log.fatal("JAXBException: Unable to read form '"
							+ form.getName() + "' - " + path.toString());
					throw new RuntimeException(jaxbEx);
				} catch (JSONException e) {
					log.fatal("JSONException: Unable to read form '"
							+ form.getName() + "' - " + path.toString());
					throw new RuntimeException(e);
				}
			}
		} catch (DirectoryIteratorException ex) {
			throw ex.getCause();
		}
	}

	private static void importChildren(JSONObject parentJson, Field parent)
			throws JAXBException, JSONException {
		JSONArray children = parentJson.optJSONArray("fields");
		if (children == null || children.length() == 0) {
			return;
		}

		JSONUnmarshaller unmarshaller = JsonForm.getJaxbContext()
				.createJSONUnmarshaller();

		for (int i = 0; i < children.length(); i++) {
			JSONObject fieldJson = children.getJSONObject(i);
			Field field = unmarshaller.unmarshalFromJSON(new StringReader(
					fieldJson.toString()), Field.class);
			parent.addField(field);
			importChildren(fieldJson, field);
		}
	}

	private static void importOutputs(Path rootPath,
			ProcessDefModel processDefModel) throws JSONException, IOException {

		// Triggers
		Path outputsPath = rootPath.resolve("outputs");
		LinkOption[] options = new LinkOption[] { LinkOption.NOFOLLOW_LINKS };
		if (Files.notExists(outputsPath, options)) {
			return;
		}

		try (DirectoryStream<Path> outputs = Files
				.newDirectoryStream(outputsPath)) {
			for (Path path : outputs) {
				ImportLogger.log("Importing outputdoc " + path.getFileName());
				log.info("Importing outputdoc " + path.toString());
				byte[] bytes = Files.readAllBytes(path);
				JSONObject outputJson = new JSONObject(new String(bytes));
				OutputDocument doc = OutputDocumentDaoHelper.getDocument(
						outputJson.getString(OutputDocument.ID), true);
				if (doc == null) {
					doc = new OutputDocument();
				}
				doc.setRefId(outputJson.getString(OutputDocument.ID));
				doc.setName(outputJson.getString(OutputDocument.NAME));
				doc.setCode(outputJson.getString(OutputDocument.CODE));
				doc.setDescription(outputJson
						.getString(OutputDocument.DESCRIPTION));
				doc.setPath(outputJson.getString(OutputDocument.PATH));
				doc.setTemplate(outputJson.getString(OutputDocument.TEMPLATE));
				doc.setProcessRefId(processDefModel.getRefId());

				OutputDocumentDaoHelper.saveOutputDoc(doc);
			}
		} catch (DirectoryIteratorException ex) {
			throw ex.getCause();
		}
	}

	private static void importTriggers(Path rootPath,
			ProcessDefModel processDefModel) throws IOException, JSONException {
		ProcessDaoImpl dao = DB.getProcessDao();
		// Triggers
		Path triggersPath = rootPath.resolve("triggers");
		LinkOption[] options = new LinkOption[] { LinkOption.NOFOLLOW_LINKS };
		if (Files.notExists(triggersPath, options)) {
			return;
		}

		try (DirectoryStream<Path> triggers = Files
				.newDirectoryStream(triggersPath)) {
			for (Path path : triggers) {
				ImportLogger.log("Importing trigger " + path.getFileName());
				log.info("Importing trigger " + path.toString());
				byte[] bytes = Files.readAllBytes(path);
				JSONObject triggerJson = new JSONObject(new String(bytes));

				ADTrigger trigger = dao.findByRefId(
						triggerJson.getString(Trigger.ID), ADTrigger.class);
				if (trigger == null) {
					trigger = new ADTrigger();
				}
				trigger.setRefId(triggerJson.getString(Trigger.ID));
				trigger.setName(triggerJson.getString(Trigger.NAME));
				trigger.setImports(triggerJson.optString(Trigger.IMPORTS));
				trigger.setScript(triggerJson.optString(Trigger.SCRIPT));
				trigger.setProcessRefId(processDefModel.getRefId());
				dao.save(trigger);
			}
		} catch (DirectoryIteratorException ex) {
			throw ex.getCause();
		}
	}

	public static ProcessCategory getProcessCategoryByName(String categoryName) {
		ADProcessCategory category = DB.getProcessDao().getProcessCategoryByName(categoryName);
		return get(category);
	}

}
