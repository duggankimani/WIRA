/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jbpm.executor.api;

import java.util.List;

import org.jbpm.executor.entities.RequestInfo;

import com.duggan.workflow.server.dao.model.ErrorLog;

/**
 *
 * @author salaboy
 */
public interface ExecutorQueryService {
    List<RequestInfo> getQueuedRequests();
    List<RequestInfo> getExecutedRequests();
    List<RequestInfo> getInErrorRequests();
    List<RequestInfo> getCancelledRequests();
    List<ErrorLog> getAllErrors(); 
    List<RequestInfo> getAllRequests(int offset, int limit);
	int getAllRequestCount();
	RequestInfo getRequestById(String refId); 
}
