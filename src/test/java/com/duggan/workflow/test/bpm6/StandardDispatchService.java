package com.duggan.workflow.test.bpm6;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.Dispatch;
import com.gwtplatform.dispatch.rpc.shared.Action;
import com.gwtplatform.dispatch.rpc.shared.DispatchService;
import com.gwtplatform.dispatch.rpc.shared.Result;
import com.gwtplatform.dispatch.rpc.shared.ServiceException;
import com.gwtplatform.dispatch.shared.ActionException;

public class StandardDispatchService implements DispatchService
{
    private Dispatch dispatch;
 
    @Inject
    public StandardDispatchService(Dispatch dispatch)
    {
        this.dispatch = dispatch;
    }


	@Override
	public Result execute(String cookieSentByRPC, Action<?> action)
			throws ActionException, ServiceException {
		Result result = dispatch.execute(action);
        return result;
	}

	@Override
	public void undo(String cookieSentByRPC, Action<Result> action,
			Result result) throws ActionException, ServiceException {
		
	}
}
