package com.duggan.workflow.server.actionhandlers;

import java.util.ArrayList;

import com.duggan.workflow.server.dao.DashboardDaoImpl;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.shared.model.DocStatus;
import com.duggan.workflow.shared.model.dashboard.Data;
import com.duggan.workflow.shared.requests.GetDashBoardDataRequest;
import com.duggan.workflow.shared.responses.GetDashBoardDataResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.response.BaseResponse;

public class GetDashBoardDataRequestHandler extends
		AbstractActionHandler<GetDashBoardDataRequest, GetDashBoardDataResponse> {

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
		response.setDocumentCounts((ArrayList<Data>) dao.getDocumentCounts());
		response.setRequestAging((ArrayList<Data>) dao.getRequestAging());
	}

	@Override
	public Class<GetDashBoardDataRequest> getActionType() {
		return GetDashBoardDataRequest.class;
	}
}
