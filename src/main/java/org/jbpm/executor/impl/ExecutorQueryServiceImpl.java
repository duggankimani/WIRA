/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jbpm.executor.impl;

import java.util.List;

import javax.persistence.EntityManager;

import org.jbpm.executor.api.ExecutorQueryService;
import org.jbpm.executor.entities.RequestInfo;

import com.duggan.workflow.server.dao.BaseDaoImpl;
import com.duggan.workflow.server.dao.model.ErrorLog;
import com.duggan.workflow.server.db.DB;

/**
 *
 * @author salaboy
 */
public class ExecutorQueryServiceImpl extends BaseDaoImpl implements ExecutorQueryService{
    
    public ExecutorQueryServiceImpl() {
    	super(DB.getEntityManager());
    }
    
    public List<RequestInfo> getQueuedRequests() {
        List resultList = em.createNamedQuery("QueuedRequests").getResultList();
        return resultList;
    }

    public List<RequestInfo> getExecutedRequests() {
        List resultList = em.createNamedQuery("ExecutedRequests").getResultList();
        return resultList;
    }

    public List<RequestInfo> getInErrorRequests() {
        List resultList = em.createNamedQuery("InErrorRequests").getResultList();
        return resultList;
    }

    public List<RequestInfo> getCancelledRequests() {
        List resultList = em.createNamedQuery("CancelledRequests").getResultList();
        return resultList;
    }

    public List<ErrorLog> getAllErrors() {
        List resultList = em.createNamedQuery("GetAllErrors").getResultList();
        return resultList;
    }
    
    public List<RequestInfo> getAllRequests(int offset, int limit) {
        List<RequestInfo> resultList = getResultList(em.createNamedQuery("GetAllRequests"),
        		offset, limit);
        return resultList;
    }

	@Override
	public int getAllRequestCount() {
		Number count = getSingleResultOrNull(em.createQuery("select count(*) FROM RequestInfo"));
		return count.intValue();
	}

	@Override
	public RequestInfo getRequestById(String refId) {
		return findByRefId(refId, RequestInfo.class);
	}

}
