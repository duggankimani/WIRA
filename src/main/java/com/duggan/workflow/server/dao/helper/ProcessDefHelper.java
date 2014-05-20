package com.duggan.workflow.server.dao.helper;

import static com.duggan.workflow.server.dao.helper.DocumentDaoHelper.getType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.duggan.workflow.server.dao.ProcessDaoImpl;
import com.duggan.workflow.server.dao.model.ADDocType;
import com.duggan.workflow.server.dao.model.LocalAttachment;
import com.duggan.workflow.server.dao.model.ProcessDefModel;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.shared.model.DocumentType;
import com.duggan.workflow.shared.model.ProcessDef;
import com.duggan.workflow.shared.model.Status;

public class ProcessDefHelper {

	public static ProcessDef save(ProcessDef processDef) {
		ProcessDaoImpl dao = DB.getProcessDao();
		ProcessDefModel model = get(processDef);

		dao.save(model);

		return get(model);
	}

	public static void delete(Long processDefId) {
		ProcessDaoImpl dao = DB.getProcessDao();
		dao.remove(dao.getProcessDef(processDefId));
	}

	public static void delete(ProcessDef processDef) {
		ProcessDaoImpl dao = DB.getProcessDao();
		ProcessDefModel model = get(processDef);

		dao.remove(model);
	}

	public static ProcessDef getProcessDef(Long processDefId) {
		ProcessDaoImpl dao = DB.getProcessDao();

		ProcessDefModel model = dao.getProcessDef(processDefId);

		ProcessDef def = get(model);

		return def;
	}

	public static List<ProcessDef> getAllProcesses() {

		ProcessDaoImpl dao = DB.getProcessDao();

		List<ProcessDefModel> process = dao.getAllProcesses();

		List<ProcessDef> processDefs = new ArrayList<>();

		if (processDefs != null) {
			for (ProcessDefModel model : process) {
				ProcessDef processDef = get(model);
				processDefs.add(processDef);
			}
		}

		return processDefs;
	}

	public static ProcessDef get(ProcessDefModel model) {

		ProcessDef def = new ProcessDef();
		def.setDocTypes(getDocTypes(model.getDocumentTypes()));
		def.setName(model.getName());
		def.setProcessId(model.getProcessId());
		def.setId(model.getId());

		boolean running = JBPMHelper.get().isProcessingRunning(
				model.getProcessId());
		def.setStatus(running ? Status.RUNNING
				: Status.INACTIVE);

		def.setDescription(model.getDescription());

		def.setLastModified(model.getUpdated() == null ? model.getCreated()
				: model.getUpdated());

		List<LocalAttachment> attachments = DB.getAttachmentDao()
				.getAttachmentsForProcessDef(model);

//		LocalAttachment attachment = (attachments == null || attachments.size() == 0) ? null
//				: attachments.get(0);

		if (attachments != null) {
			for(LocalAttachment attach: attachments){
				def.addFile(AttachmentDaoHelper.get(attach));
			}			
//			def.setFileName(attachment.getName());
//			def.setFileId(attachment.getId());
		}
		
		
		List<LocalAttachment> image = DB.getAttachmentDao()
				.getAttachmentsForProcessDef(model,true);

		if(image!=null && !image.isEmpty()){
			def.setImageId(image.get(0).getId());
			
			def.setImageName(image.get(0).getName());
		}
			

		List<DocumentType> docTypes = new ArrayList<>();

		Collection<ADDocType> docModels = model.getDocumentTypes();
		
		for (ADDocType doc : docModels) {
			docTypes.add(getType(doc));
		}

		def.setDocTypes(docTypes);

		return def;
	}

	private static List<DocumentType> getDocTypes(
			Collection<ADDocType> processDocuments) {
		if (processDocuments == null) {
			return new ArrayList<>();
		}

		List<DocumentType> docs = new ArrayList<>();

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

		List<DocumentType> types = processDef.getDocTypes();

		if (types != null) {
			for (DocumentType type : types) {
				ADDocType adtype = getType(type);
				model.addDocType(adtype);
			}

		}

		return model;

	}

}
