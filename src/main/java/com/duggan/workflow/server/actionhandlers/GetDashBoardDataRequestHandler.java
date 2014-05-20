package com.duggan.workflow.server.actionhandlers;

import com.duggan.workflow.server.dao.DashboardDaoImpl;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.shared.model.DocStatus;
import com.duggan.workflow.shared.requests.GetDashBoardDataRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetDashBoardDataResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetDashBoardDataRequestHandler extends
		BaseActionHandler<GetDashBoardDataRequest, GetDashBoardDataResponse> {

	@Inject
	public GetDashBoardDataRequestHandler() {
	}

	@Override
	public void execute(GetDashBoardDataRequest action,
			BaseResponse actionResult, ExecutionContext execContext)
			throws ActionException {
		
		DashboardDaoImpl dao = DB.getDashboardDao();
		GetDashBoardDataResponse response = (GetDashBoardDataResponse)actionResult;
		response.setActiveCount(dao.getRequestCount(DocStatus.INPROGRESS));
		response.setRequestCount(dao.getRequestCount(false,DocStatus.DRAFTED));
		response.setFailureCount(dao.getRequestCount(DocStatus.FAILED));
		response.setDocumentCounts(dao.getDocumentCounts());
		response.setRequestAging(dao.getRequestAging());
	}

	@Override
	public Class<GetDashBoardDataRequest> getActionType() {
		return GetDashBoardDataRequest.class;
	}
}
