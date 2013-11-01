package com.duggan.workflow.server.dao;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.duggan.workflow.server.dao.model.ADField;
import com.duggan.workflow.server.dao.model.ADForm;
import com.duggan.workflow.server.dao.model.ADKeyValuePair;
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
			
			Collection<ADProperty> props = form.getProperties();
			if(props!=null)
				for(ADProperty prop: props){
					if(prop.getName().equals("NAME")){
						if(prop.getValue()==null){
							ADValue value = new ADValue();
							value.setStringValue(form.getName());
							prop.setValue(value);
						}
					}
					if(prop.getName().equals("CAPTION")){
						if(prop.getValue()==null){
							ADValue value = new ADValue();
							value.setStringValue(form.getName());
							prop.setValue(value);
						}
					}
					
					if(prop.getName().equals("DESCRIPTION")){
						if(prop.getValue()==null){
							ADValue value = new ADValue();
							value.setStringValue(form.getName());
							prop.setValue(value);
						}
					}
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
	
	@SuppressWarnings("unchecked")
	public void setPosition(ADField fld, int previousPos, int newPos){
		
		if(previousPos==-1){
			previousPos=1000; // new field - reset all fields with position> newPos
		}
		List<ADField> fields = null;
		
		String hql = "FROM ADField fld where fld.form.id=:id ";
		boolean reducing=false;
		
		if(previousPos==newPos){
			fld.setPosition(newPos);
			return; //no change
		}
		
		if(previousPos>newPos){
			hql = hql.concat(" and ((fld.position>=:newPos and fld.position<:prevPos) or fld.position is null) ");
		}else{
			reducing=true;
			hql = hql.concat(" and ((fld.position<=:newPos and fld.position>:prevPos) or fld.position is null) ");
		}
		
		hql = hql.concat(" order by position");
		
//		System.err.println("PrevPos = "+previousPos);
//		System.err.println("NewPos = "+newPos);
//		System.err.println(hql);
		fields = em.createQuery(hql)
				.setParameter("id", fld.getForm().getId())
				.setParameter("newPos", newPos)
				.setParameter("prevPos", previousPos)				
				.getResultList();
		
		int size = fields.size();
		
		int count=0;
		for(ADField field: fields){
			++count;
		//	String previousStr = field.getPosition()+""; 
			if(reducing){
				field.setPosition(newPos-(size-count+1));
			}else{
				
				field.setPosition(newPos+count);
			}
			
//			System.err.println(">>Field :: Id = "+field.getId()+"; Previous = "+previousStr+
//					" Pos - "+field.getPosition());
		}		
		
		fld.setPosition(newPos);
	}

	public List<ADKeyValuePair> getKeyValuePairs(String type) {
		
		@SuppressWarnings("unchecked")
		List<ADKeyValuePair> pairs = em.createQuery("FROM ADKeyValuePair p where p.referenceType=:type")
		.setParameter("type", type).getResultList();
		
		return pairs;
	}
}
