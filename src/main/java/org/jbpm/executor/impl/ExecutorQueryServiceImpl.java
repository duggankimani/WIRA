/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jbpm.executor.impl;

import java.util.List;

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
        List resultList = getEntityManager().createNamedQuery("QueuedRequests").getResultList();
        return resultList;
    }

    public List<RequestInfo> getExecutedRequests() {
        List resultList = getEntityManager().createNamedQuery("ExecutedRequests").getResultList();
        return resultList;
    }

    public List<RequestInfo> getInErrorRequests() {
        List resultList = getEntityManager().createNamedQuery("InErrorRequests").getResultList();
        return resultList;
    }

    public List<RequestInfo> getCancelledRequests() {
        List resultList = getEntityManager().createNamedQuery("CancelledRequests").getResultList();
        return resultList;
    }

    public List<ErrorLog> getAllErrors() {
        List resultList = getEntityManager().createNamedQuery("GetAllErrors").getResultList();
        return resultList;
    }
    
    public List<RequestInfo> getAllRequests(int offset, int limit) {
        List<RequestInfo> resultList = getResultList(getEntityManager().createNamedQuery("GetAllRequests"),
        		offset, limit);
        return resultList;
    }

	@Override
	public int getAllRequestCount() {
		Number count = getSingleResultOrNull(getEntityManager().createQuery("select count(*) FROM RequestInfo"));
		return count.intValue();
	}

	@Override
	public RequestInfo getRequestById(String refId) {
		return findByRefId(refId, RequestInfo.class);
	}

}
