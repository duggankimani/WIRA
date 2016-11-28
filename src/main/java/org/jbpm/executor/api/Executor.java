package org.jbpm.executor.api;

import com.duggan.workflow.shared.model.RequestInfoDto;

/**
 *
 * @author salaboy
 */
public interface Executor extends Service {

	public Long scheduleRequest(CommandCode commandName, CommandContext ctx);
	
	public Long updateRequest(RequestInfoDto dto, boolean resend);

	public void cancelRequest(Long requestId);

	public int getInterval();

	public void setInterval(int waitTime);

	public int getRetries();

	public void setRetries(int defaultNroOfRetries);

	public int getThreadPoolSize();

	public void setThreadPoolSize(int nroOfThreads);
}
