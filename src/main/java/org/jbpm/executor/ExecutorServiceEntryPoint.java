/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jbpm.executor;

import java.util.List;

import org.jbpm.executor.api.CommandCode;
import org.jbpm.executor.api.CommandContext;
import org.jbpm.executor.entities.RequestInfo;

import com.duggan.workflow.server.dao.model.ErrorLog;

/**
 *
 * @author salaboy
 */
public interface ExecutorServiceEntryPoint {

    public List<RequestInfo> getQueuedRequests();

    public List<RequestInfo> getExecutedRequests();

    public List<RequestInfo> getInErrorRequests();

    public List<RequestInfo> getCancelledRequests();

    public List<ErrorLog> getAllErrors();

    public List<RequestInfo> getAllRequests(int offset, int limit);

    public int clearAllRequests();

    public int clearAllErrors();

    public Long scheduleRequest(CommandCode commandName, CommandContext ctx);

    public void cancelRequest(Long requestId);

    public void init();

    public void destroy();

    public int getInterval();

    public void setInterval(int waitTime);

    public int getRetries();

    public void setRetries(int defaultNroOfRetries);

    public int getThreadPoolSize();

    public void setThreadPoolSize(int nroOfThreads);

	public int getAllRequestCount();

	public RequestInfo getRequestById(String refId);
}
