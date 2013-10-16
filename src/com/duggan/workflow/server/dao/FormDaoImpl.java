package com.duggan.workflow.server.dao;

import java.util.List;

import javax.persistence.EntityManager;

import com.duggan.workflow.server.dao.model.ADField;
import com.duggan.workflow.server.dao.model.ADForm;
import com.duggan.workflow.server.dao.model.ADProperty;
import com.duggan.workflow.server.dao.model.ADValue;
import com.duggan.workflow.server.dao.model.PO;

/**
 * 
 * @author duggan
 *
 */
public class FormDaoImpl extends BaseDaoImpl {

	public FormDaoImpl(EntityManager em) {
		super(em);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<ADForm> getAllForms(){
		
		List lst = em.createQuery("from ADForm f").getResultList();
		return lst;
	}
	
	
	public ADForm getForm(Long id){
		
		return em.find(ADForm.class, id);
	}
	
	public ADField getField(Long fieldId){
		return em.find(ADField.class, fieldId);
	}
	
	public ADValue getValue(Long valueId){
		return em.find(ADValue.class, valueId);
	}
	
	public ADProperty getProperty(Long propertyId){
		return em.find(ADProperty.class, propertyId);
	}
	
	@Override
	public void save(PO po) {
		
		if(po.getId()!=null){
			prepare(po);
			em.merge(po);
		}
		else
			super.save(po);
		

		if(po instanceof ADForm){
			ADForm form = (ADForm)po;
			if(form.getName()==null || form.getName().isEmpty() || form.getName().equals("Untitled")){
				form.setName("Untitled"+form.getId());
			}
			
			if(form.getCaption()==null || form.getCaption().equals("Untitled")){
				form.setCaption("Untitled"+form.getId());
			}
			
			em.merge(form);
		}
	}
	
	public void deleteForm(Long formId){
		delete(getForm(formId));
	}
	
	public void deleteField(Long fieldId){
		delete(getField(fieldId));
	}
	
	public void deleteValue(Long valueid){
		delete(getValue(valueid));
	}

	public void deleteProperty(Long propertyId){
		delete(getProperty(propertyId));
	}

	public List<ADField> getFields(Long parentId) {
		
		@SuppressWarnings("unchecked")
		List<ADField> fields = em.createQuery("FROM ADField fld where fld.form.id=:id")
				.setParameter("id", parentId).getResultList();
		
		return fields;
	}
}
