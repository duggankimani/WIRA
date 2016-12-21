package com.duggan.workflow.server.dao.helper;

import static com.duggan.workflow.server.dao.helper.DocumentDaoHelper.getType;
import static com.duggan.workflow.server.dao.helper.DocumentDaoHelper.getTypeFromProcess;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.io.IOUtils;
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
import com.duggan.workflow.server.dao.model.ADForm;
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
import com.duggan.workflow.server.helper.dao.JaxbFormExportProviderImpl;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
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
import com.sun.jersey.api.json.JSONJAXBContext;
import com.sun.jersey.api.json.JSONMarshaller;
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
	
	public static ProcessDef getProcessDef(String processRefId, boolean isDetailed) {
		ProcessDaoImpl dao = DB.getProcessDao();

		ProcessDefModel model = dao.findByRefId(processRefId,
				ProcessDefModel.class);

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

		List<ProcessDefModel> process = dao.getAllProcesses(searchTerm,beginIdx,length);

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

		model.setName(processDef.getName());
		model.setDescription(processDef.getDescription());
		model.setProcessId(processDef.getProcessId());

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
		dto.setProcessDefId(model.getProcessDef().getId());
		dto.setProcessRefId(model.getProcessDef().getRefId());
		dto.setSequenceNo(model.getSequenceNo());
		dto.setStepName(model.getStepName());

		return dto;
	}

	private static TaskStepModel getStep(TaskStepDTO step) {
		ProcessDaoImpl dao = DB.getProcessDao();

		TaskStepModel model = new TaskStepModel();
		if (step.getId() != null) {
			model = dao.getById(TaskStepModel.class, step.getId());
		}
		model.setCondition(step.getCondition());
		model.setFormRef(step.getFormRefId());
		model.setDoc(step.getOutputDocId() == null ? null : dao.getById(
				ADOutputDoc.class, step.getOutputDocId()));
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

	public static List<TaskStepDTO> getTaskStepsByDocumentJson(String docRefId) {
		Document docModel = DB.getDocumentDao().getDocJson(docRefId, false);

		String processId = docModel.getProcessId();
		if (processId == null && docModel.getType() != null) {
			processId = docModel.getType() == null ? docModel.getProcessId()
					: docModel.getType().getProcessId();
		}
		List<TaskStepModel> models = DB.getProcessDao().getTaskSteps(processId,
				null);

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
		List<TaskStepModel> models = DB.getProcessDao().getTaskSteps(processId,
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
		List<TaskStepModel> models = DB.getProcessDao().getTaskSteps(processId,
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

	private static ADTaskStepTrigger getStepTrigger(TaskStepTrigger trigger) {
		ProcessDaoImpl dao = DB.getProcessDao();
		ADTaskStepTrigger adTaskStep = new ADTaskStepTrigger();

		if (trigger.getId() != null) {
			adTaskStep = dao.getById(ADTaskStepTrigger.class, trigger.getId());
		}

		adTaskStep.setTaskStep(dao.getById(TaskStepModel.class,
				trigger.getTaskStepId()));

		adTaskStep.setType(trigger.getType());
		adTaskStep.setTrigger(dao.getById(ADTrigger.class, trigger.getTrigger()
				.getId()));

		adTaskStep.setCondition(trigger.getCondition());
		return adTaskStep;
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
		if (notification.getId() != null) {
			model = dao.getTaskNotificationById(notification.getId());
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

	public static void exportProcess(String processRefId) {
		ProcessDaoImpl dao = DB.getProcessDao();
		ProcessDefModel model = dao.getProcessDef(dao
				.getProcessDefId(processRefId));
		List<LocalAttachment> attachments = DB.getAttachmentDao()
				.getAttachmentsForProcessDef(model);
		List<LocalAttachment> images = DB.getAttachmentDao()
				.getAttachmentsForProcessDef(model, true);
		attachments.addAll(images);

		// Steps
		Collection<TaskStepModel> taskSteps = model.getTaskSteps();

		String folder = "/home/duggan/Projects/WIRA/jaxbexport/";
		String processXmlName = model.getName() + ".xml";
		String attachmentsFolder = folder + "attachments/";
		String stepsFolder = folder + "steps/";
		String formsFolder = folder + "forms/";
		String outputDocsFolder = folder + "outputdocs/";
		String triggersFolder = folder + "triggers/";

		try {
			// Attachments
			for (LocalAttachment a : attachments) {
				model.addAttachmentName(a.getName());
			}

			// Process
			IOUtils.write(exportObject(model), new FileOutputStream(new File(
					folder + processXmlName)));

			// Task Steps
			for (TaskStepModel step : taskSteps) {
				// ADForm form = step.getForm();
				Form form = FormDaoHelper.getFormJson(step.getRefId(), false);
				ADOutputDoc doc = step.getDoc();

				Collection<ADTaskStepTrigger> stepTriggers = step
						.getTaskStepTriggers();
				log.debug("### " + step.getNodeId() + " - "
						+ stepTriggers.size());
				// dao.getTaskStepTriggers(step.getId(),
				// TriggerType.BEFORESTEP);
				// stepTriggers.addAll(dao.getTaskStepTriggers(step.getId(),
				// TriggerType.AFTERSTEP));

				for (ADTaskStepTrigger stepTrigger : stepTriggers) {
					stepTrigger.setTriggerName(stepTrigger.getTrigger()
							.getName());
					log.warn(">>>" + stepTrigger.getTriggerName());
				}

				String name = "ProcessInitiation";
				if (form != null) {
					name = form.getName();
				}

				if (doc != null) {
					name = doc.getName();
				}

				String stepName = step.getSequenceNo() + "_" + name + ".xml";
				if (step.getNodeId() != null) {
					stepName = step.getNodeId() + "_" + stepName;
				} else {
					stepName = "00" + "_" + stepName;
				}
				// step.setFormRefId(step.getForm()==null? null:
				// step.getForm().getRefId());
				step.setOutputRefId(step.getDoc() == null ? null : step
						.getDoc().getRefId());
				IOUtils.write(exportObject(step), new FileOutputStream(
						new File(stepsFolder + stepName)));
			}

			// Forms
			// List<ADForm> forms = DB.getFormDao().getAllForms(model.getId());
			List<Form> forms = FormDaoHelper.getFormsJson(model.getRefId(),
					true);
			for (Form form : forms) {
				String name = form.getName() + ".xml";
				IOUtils.write(exportObject(form), new FileOutputStream(
						new File(formsFolder + name)));
			}

			List<ADOutputDoc> docs = DB.getOutputDocDao().getOutputDocuments(
					model.getRefId(), null);
			for (ADOutputDoc doc : docs) {
				String name = doc.getName() + ".xml";
				IOUtils.write(exportObject(doc), new FileOutputStream(new File(
						outputDocsFolder + name)));
				if (doc.getAttachment() != null) {
					attachments.add(doc.getAttachment());
				}
			}

			List<ADTrigger> triggers = DB.getProcessDao().getTriggers(
					model.getRefId(), null);
			for (ADTrigger trigger : triggers) {
				String name = trigger.getName() + ".xml";
				IOUtils.write(exportObject(trigger), new FileOutputStream(
						new File(triggersFolder + name)));
			}

			// Attachments
			for (LocalAttachment a : attachments) {
				IOUtils.write(a.getAttachment(), new FileOutputStream(new File(
						attachmentsFolder + a.getName())));
			}

		} catch (IOException | JAXBException e) {
			throw new RuntimeException(e);
		}
	}

	private static byte[] exportObject(Object model) throws JAXBException,
			IOException {
		JAXBContext context = new JaxbFormExportProviderImpl().getContext(model
				.getClass());
		String out = null;
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		StringWriter writer = new StringWriter();
		marshaller.marshal(model, writer);

		out = writer.toString();
		writer.close();
		return out.getBytes();
	}

	public static List<Schema> getProcessSchema(String processRefId) {
		return DB.getFormDao().getProcessSchema(processRefId);
	}
	
	public static String exportProcessJson(String processRefId) throws JSONException{
		ProcessDaoImpl dao = DB.getProcessDao();
		ProcessDefModel model = dao.findByRefId(processRefId,
				ProcessDefModel.class);
		ProcessDef processDef = get(model, true);

		
		List<Attachment> files = processDef.getFiles();//Does not include process svg image
		List<LocalAttachment> imageFiles = DB.getAttachmentDao().getAttachmentsForProcessDef(model,true);
		for(LocalAttachment a: imageFiles){
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
		process.put(ProcessDef.CATEGORY, processDef.getCategory()==null? null: processDef.getCategory().getName());
		
		JSONArray processAttachments = new JSONArray();
		for(Attachment a: processDef.getFiles()){
			JSONObject att = new JSONObject();
			att.put(Attachment.ID, a.getRefId());
			att.put(Attachment.NAME, a.getName());
			att.put(Attachment.TYPE, a.getType());
			att.put(Attachment.SIZE, a.getSize());
			att.put(Attachment.CONTENTTYPE, a.getContentType());
			att.put(Attachment.PATH, a.getPath());
			processAttachments.put(att);
			//Process SVG Image not included 
		}
		//Process BPMN2 & SVG
		process.put("attachments", processAttachments);
		
		//Access Rights
		ArrayList<Listable> usersAndGroups = processDef.getUsersAndGroups();
		JSONArray access = new JSONArray();
		for(Listable userOrGroup: usersAndGroups){
			if(userOrGroup instanceof HTUser){
				access.put(((HTUser)userOrGroup).getUserId());
			}else{
				access.put(userOrGroup.getName());
			}
			
		}
		process.put("access", access);//Access info
		
		
		//Task Steps
		ArrayList<TaskStepDTO> taskSteps = getSteps(processRefId);
		JSONArray steps = new JSONArray();
		for(TaskStepDTO dto: taskSteps){
			JSONObject obj = new JSONObject();
			obj.put(TaskStepDTO.ID, dto.getRefId());
			obj.put(TaskStepDTO.NODEID, dto.getNodeId());
			obj.put(TaskStepDTO.STEPNAME, dto.getStepName());
			obj.put(TaskStepDTO.SEQUENCENO, dto.getSequenceNo());
			obj.put(TaskStepDTO.MODE, dto.getMode()==null? MODE.EDIT.name(): dto.getMode().name());
			obj.put(TaskStepDTO.CONDITION, dto.getCondition());
			obj.put(TaskStepDTO.FORMNAME, dto.getFormName());
			obj.put(TaskStepDTO.FORMREF, dto.getFormRefId());
			obj.put(TaskStepDTO.OUTPUTNAME, dto.getOutputDocName());
			obj.put(TaskStepDTO.OUTPUTREF, dto.getOutputDocId());
			steps.put(obj);
			
			//Step Triggers
			List<TaskStepTrigger> triggers = getTaskStepTriggers(dto.getId(), null);
			JSONArray trigg = new JSONArray();
			for(TaskStepTrigger trigger: triggers){
				JSONObject item = new JSONObject();
				item.put(TaskStepTrigger.ID, trigger.getRefId());
				item.put(TaskStepTrigger.TRIGGER, trigger.getTrigger().getName());
				item.put(TaskStepTrigger.CONDITION, trigger.getCondition());
				item.put(TaskStepTrigger.TYPE, trigger.getType().name());
				trigg.put(item);
			}
			//Step Triggers
			obj.put("triggers", trigg);
		}
		
		//Steps
		process.put("steps", steps);
		
		List<TaskNotification> notificationTemplates =  getTaskNotificationTemplates(processDef.getId());
		JSONArray notificationArr = new JSONArray();
		for(TaskNotification template: notificationTemplates){
			JSONObject obj = new JSONObject();
			obj.put(TaskNotification.CATEGORY, template.getCategory().name());
			obj.put(TaskNotification.ENABLENOTIFICATION, template.isEnableNotification());
			obj.put(TaskNotification.ACTION, template.getAction().name());
			obj.put(TaskNotification.DEFAULTANOTIFICATION, template.isUseDefaultNotification());
			obj.put(TaskNotification.TEMPLATE, template.getNotificationTemplate());
			obj.put(TaskNotification.NODEID, template.getNodeId());
			obj.put(TaskNotification.STEPNAME, template.getStepName());
			obj.put(TaskNotification.SUBJECT, template.getSubject());
			obj.put(TaskNotification.RECIPIENTS, template.getTargets());
			notificationArr.put(obj);			
		}
		//Notifications
		process.put("notifications", notificationArr);
		

		return process.toString();
	}

	private static List<TaskNotification> getTaskNotificationTemplates(
			Long processDefId) {
		assert processDefId != null;
		List<ADTaskNotification> adTaskNotes = DB.getProcessDao().getTaskNotifications(processDefId);
		List<TaskNotification> taskNotes = new ArrayList<TaskNotification>();
		for(ADTaskNotification template: adTaskNotes){
			taskNotes.add(getTaskNotification(template));
		}
		
		return taskNotes;
	}

	private static ArrayList<TaskStepDTO> getSteps(String processRefId) {
		List<TaskStepModel> taskSteps = DB.getProcessDao().getTaskStepsByProcessRef(processRefId);
		ArrayList<TaskStepDTO> steps = new ArrayList<TaskStepDTO>();
		for(TaskStepModel model: taskSteps){
			TaskStepDTO taskStep = getStep(model);
			steps.add(taskStep);
		}
		
		return steps;
	}
	
	public static void exportProcessAsZip(String processRefId, OutputStream outputStream) throws IOException{
		String processMeta = null;
		List<OutputDocument> docs = OutputDocumentDaoHelper.getDocuments(processRefId);
		List<Trigger> triggers = ProcessDaoHelper.getTriggers(processRefId, null);
		List<Form> forms = FormDaoHelper.getForms(processRefId, true);
		
		try{
			processMeta = exportProcessJson(processRefId);
		}catch(JSONException e){
			throw new RuntimeException(e);
		}
		
		ZipOutputStream zos  = new ZipOutputStream(outputStream);
		addZipEntry(zos, "process.json",processMeta.getBytes());
		//Triggers
		for(Trigger t: triggers){
			String triggerMeta = null;
			try {
				triggerMeta = exportProcessTriggersJson(t);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			addZipEntry(zos, "triggers/"+t.getName()+".json",triggerMeta.getBytes());
		}
		
		//Output Documents
		for(OutputDocument doc: docs){
			String template = OutputDocumentDaoHelper.getHTMLTemplateByRefId(doc.getRefId());
			doc.setTemplate(template);
			String outputMeta = null;
			try {
				outputMeta = exportProcessOutputJson(doc);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			addZipEntry(zos, "outputs/"+doc.getName()+".json",outputMeta.getBytes());
		}
		
		//Forms
		for(Form form : forms){
			String formMeta = exportFormJson(form);
			addZipEntry(zos, "forms/"+form.getName()+".json",formMeta.getBytes());
		}
	
		//Process Attachments
		//Attach files
		try{
			JSONObject process = new JSONObject(processMeta);
			JSONArray arr = process.getJSONArray("attachments");
			for(int i=0 ; i<arr.length(); i++){
				JSONObject attachment = arr.getJSONObject(i);
				String attachmentRefId = attachment.getString(Attachment.ID);
				String name = attachment.getString(Attachment.NAME);
				LocalAttachment a = DB.getAttachmentDao().findByRefId(attachmentRefId, LocalAttachment.class);
				addZipEntry(zos, name,a.getAttachment());
			}
		}catch(JSONException e){
			throw new RuntimeException(e);
		}
		
		zos.close();
	}
	
	static String exportFormJson(Form form) {
		String out = null;
		try{
			JSONMarshaller marshaller  = JsonForm.getJaxbContext().createJSONMarshaller();
			final StringWriter w = new StringWriter();
			marshaller.marshallToJSON(form,w);
			w.flush();
			JSONObject formJson = new JSONObject(w.toString());
			JSONArray arr = new JSONArray();
			if(form.getFields()!=null)
			for(Field field: form.getFields()){
				String fieldJson = exportFieldJson(field);
				JSONObject obj = new JSONObject(fieldJson);
				arr.put(obj);
			}
			formJson.put("fields", arr);
			out = formJson.toString();
		}catch(JAXBException | JSONException ex){
			throw new RuntimeException(ex);
		}
		return out;
	}

	private static String exportFieldJson(Field field) {
		String out = null;
		try{
			JSONMarshaller marshaller  = JsonForm.getJaxbContext().createJSONMarshaller();
			final StringWriter w = new StringWriter();
			marshaller.marshallToJSON(field,w);
			w.flush();
			out = w.toString();
		}catch(JAXBException ex){
			throw new RuntimeException(ex);
		}
		return out;
	}

	private static void addZipEntry(ZipOutputStream zos,String entryName, byte[] entryContent)
			throws IOException {
		ZipEntry entry = new ZipEntry(entryName);
		zos.putNextEntry(entry);
		zos.write(entryContent);
		zos.closeEntry();
	}

	private static String exportProcessOutputJson(OutputDocument doc) throws JSONException {
		
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

	private static String exportProcessTriggersJson(Trigger trigger) throws JSONException {
		
		JSONObject t = new JSONObject();
		t.put(Trigger.ID, trigger.getRefId());
		t.put(Trigger.NAME, trigger.getName());
		t.put(Trigger.IMPORTS, trigger.getImports());
		t.put(Trigger.SCRIPT, trigger.getScript());
		
		return t.toString();
	}

	public static void exportProcessAsFile(String processRefId, String fileName) throws IOException{
		FileOutputStream fos = new FileOutputStream(new File(fileName));
		exportProcessAsZip(processRefId, fos);
		fos.close();
	}

}
