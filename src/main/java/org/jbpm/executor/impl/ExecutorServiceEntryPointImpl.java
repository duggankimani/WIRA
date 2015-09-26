/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jbpm.executor.impl;

import java.util.List;

import org.jbpm.executor.ExecutorServiceEntryPoint;
import org.jbpm.executor.api.CommandCodes;
import org.jbpm.executor.api.CommandContext;
import org.jbpm.executor.api.Executor;
import org.jbpm.executor.api.ExecutorQueryService;
import org.jbpm.executor.api.ExecutorRequestAdminService;
import org.jbpm.executor.entities.ErrorInfo;
import org.jbpm.executor.entities.RequestInfo;

/**
 *
 * @author salaboy
 */
public class ExecutorServiceEntryPointImpl implements ExecutorServiceEntryPoint {
    
    private Executor executor;
    
    private ExecutorRequestAdminService adminService;

    public ExecutorServiceEntryPointImpl() {
    	
    }

    public Executor getExecutor() {
        return executor;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }
    
    public ExecutorRequestAdminService getAdminService() {
        return adminService;
    }

    public void setAdminService(ExecutorRequestAdminService adminService) {
        this.adminService = adminService;
    }
    
    

    public List<RequestInfo> getQueuedRequests() {
        return getQueryService().getQueuedRequests();
    }

    public List<RequestInfo> getExecutedRequests() {
        return getQueryService().getExecutedRequests();
    }

    public List<RequestInfo> getInErrorRequests() {
        return getQueryService().getInErrorRequests();
    }

    public List<RequestInfo> getCancelledRequests() {
        return getQueryService().getCancelledRequests();
    }

    public List<ErrorInfo> getAllErrors() {
        return getQueryService().getAllErrors();
    }

    public List<RequestInfo> getAllRequests(int offset, int limit) {
        return getQueryService().getAllRequests(offset, limit);
    }
    
    @Override
	public int getAllRequestCount() {
		return getQueryService().getAllRequestCount();
	}

    public int clearAllRequests() {
        return adminService.clearAllRequests();
    }

    public int clearAllErrors() {
        return adminService.clearAllErrors();
    }

    public synchronized Long scheduleRequest(CommandCodes commandName, CommandContext ctx) {
    	assert executor!=null;
    	assert commandName!=null;
    	assert ctx!=null;
    	
        return executor.scheduleRequest(commandName, ctx);
    }

    public void cancelRequest(Long requestId) {
        executor.cancelRequest(requestId);
    }

    public void init() {
    	executor = ExecutorFactory.getExecutor();
    	adminService = ExecutorFactory.getExecutorRequestAdminService();
    	       
    }
    
    public ExecutorQueryService getQueryService(){
    	return ExecutorFactory.getExecutorQueryService();
    }

    public void destroy() {
        executor.destroy();
    }

    public int getInterval() {
        return executor.getInterval();
    }

    public void setInterval(int waitTime) {
        executor.setInterval(waitTime);
    }

    public int getRetries() {
        return executor.getRetries();
    }

    public void setRetries(int defaultNroOfRetries) {
        executor.setRetries(defaultNroOfRetries);
    }

    public int getThreadPoolSize() {
        return executor.getThreadPoolSize();
    }

    public void setThreadPoolSize(int nroOfThreads) {
        executor.setThreadPoolSize(nroOfThreads);
    }

    
}
