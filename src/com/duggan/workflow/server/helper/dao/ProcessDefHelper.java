package com.duggan.workflow.server.helper.dao;

import java.util.ArrayList;
import java.util.List;

import com.duggan.workflow.server.dao.ProcessDaoImpl;
import com.duggan.workflow.server.dao.model.ProcessDefModel;
import com.duggan.workflow.server.dao.model.ProcessDocModel;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.shared.model.DocType;
import com.duggan.workflow.shared.model.ProcessDef;

public class ProcessDefHelper {
	
	public static ProcessDef save(ProcessDef processDef){
		ProcessDaoImpl dao = DB.getProcessDao();
		ProcessDefModel model = get(processDef);
		
		dao.save(model);
		
		return get(model);
	}
	
	public static void delete(Long processDefId){
		ProcessDaoImpl dao = DB.getProcessDao();
		dao.remove(dao.getProcessDef(processDefId));
	}
	
	public static void delete(ProcessDef processDef){
		ProcessDaoImpl dao = DB.getProcessDao();
		ProcessDefModel model = get(processDef);
		
		dao.remove(model);
	}
	
	public static ProcessDef getProcessDef(Long processDefId){
		ProcessDaoImpl dao = DB.getProcessDao();
		
		ProcessDefModel model = dao.getProcessDef(processDefId);
		
		ProcessDef def = get(model);
				
		return def;
	}
	
	public static List<ProcessDef> getAllProcesses(){
		ProcessDaoImpl dao = DB.getProcessDao();
		List<ProcessDefModel> process = dao.getAllProcesses();
		
		List<ProcessDef> processDefs = new ArrayList<>();
		
		if(processDefs!=null){
			for(ProcessDefModel model: process){
				ProcessDef processDef = get(model);
				processDefs.add(processDef);
			}
		}
		
		return processDefs;
	}
	
	public static ProcessDef get(ProcessDefModel model){

		ProcessDef def = new ProcessDef();
		def.setDocTypes(getDocTypes(model.getProcessDocuments()));
		def.setName(model.getName());
		def.setProcessId(model.getProcessId());
		def.setId(model.getId());
		
		
		List<ProcessDocModel> docModels = model.getProcessDocuments();
		List<DocType> docTypes = new ArrayList<>();
		if(docModels!=null){
			for(ProcessDocModel doc: docModels){
				docTypes.add(doc.getDocType());
			}
		}
		
		def.setDocTypes(docTypes);
		
		return def;
	}
	
	
	private static List<DocType> getDocTypes(
			List<ProcessDocModel> processDocuments) {
		if(processDocuments==null){
			return new ArrayList<>();
		}
		
		List<DocType> docs = new ArrayList<>();
		
		for(ProcessDocModel model: processDocuments){
			docs.add(model.getDocType());
		}
		
		return docs;
	}

	public static ProcessDefModel get(ProcessDef processDef){
		ProcessDaoImpl dao = DB.getProcessDao();
		
		ProcessDefModel model = new ProcessDefModel();
		Long id = processDef.getId();
		
		if(id!=null){
			model = dao.getProcessDef(id);
		}
		
		model.setName(processDef.getName());
		model.setProcessId(processDef.getProcessId());
		
		List<DocType> types = processDef.getDocTypes();
		
		List<ProcessDocModel> docList = new ArrayList<>();
				
		if(types!=null){
			for(DocType type: types){
				ProcessDocModel docModel = dao.getProcessDoc(type);
				
				if(docModel==null){
					docModel = new ProcessDocModel();
					
				}	
				docModel.setDocType(type);
				docModel.setProcessDef(model);
			
				docList.add(docModel);
			}
			
			model.setProcessDocuments(docList);
		}
		
		return model;
		
	}
	
}
