package org.wira.pioneer.integration;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.duggan.workflow.server.ServerConstants;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.server.helper.session.SessionHelper;
import com.duggan.workflow.server.rest.model.BusinessKey;
import com.duggan.workflow.server.rest.model.Detail;
import com.duggan.workflow.server.rest.model.Request;
import com.duggan.workflow.server.rest.model.Response;
import com.duggan.workflow.server.rest.service.impl.IncomingRequestImpl;
import com.duggan.workflow.shared.model.HTUser;

/**
 * This servlet receives php requests from Pioneer IPN app and starts off a new
 * BPM process for handling wrong/non-existent ID numbers {see IPNProcess.bpmn2}
 * 
 * url - http://localhost:8080/wira/ipnserv?paramName=paramValue etc
 * 
 * @author Tom
 * 
 */
public class PioneerMpesaServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse resp)
			throws ServletException, IOException {
		// super.doGet(req, resp);

		HttpSession session = null;
		Request wiraRequest = new Request();

		try {
			session = request.getSession(true);
			SessionHelper.setHttpRequest(request);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Response response = new Response();

		try {
			DB.beginTransaction();
			wiraRequest
					.setCommandName(IncomingRequestImpl.NEWAPPROVALREQUESTCOMMAND);

			String parameter = request.getParameter("docType");
			RequestType requestType = RequestType.getRequestType(parameter);

			// Create Context and set it
			wiraRequest.setContext(createContext(request, requestType,
					wiraRequest));

			HTUser user = new HTUser();
			user.setUserId(wiraRequest.getContext("ownerId").toString());
			if (session != null)
				session.setAttribute(ServerConstants.USER, user);

			// Submit
			IncomingRequestImpl handler = new IncomingRequestImpl();
			handler.executeClientRequest(wiraRequest, response);

			// Response
			PrintWriter out = resp.getWriter();
			BusinessKey key = response.getBusinessKey();
			assert key != null;

			resp.setContentType("text/json");
			out.println("Response:" + key.getDocumentId() + " : "
					+ key.getProcessInstanceId() + ":" + key.getSessionId()
					+ "</b>");
			out.close();

			DB.commitTransaction();
		} catch (Exception e) {
			DB.rollback();
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			if (session != null) {
				session.invalidate();
			}

			DB.closeSession();
			SessionHelper.setHttpRequest(null);
			JBPMHelper.clearRequestData();
		}
	}

	private Map<String, Object> createContext(HttpServletRequest req,
			RequestType reqType, Request wiraRequest) {
		Map<String, Object> context = new HashMap<String, Object>();
		context.put("docType", req.getParameter("docType"));
		context.put("requestType", req.getParameter("docType"));

		switch (reqType) {
		case ALLOCATIONREQUEST:
			wiraRequest.setDescription(req.getParameter("allocateeName") + "-"
					+ req.getParameter("terminalName"));
			context.put("allocateeName", req.getParameter("allocateeName"));
			context.put("terminalName", req.getParameter("terminalName"));
			context.put("terminalId", req.getParameter("terminalId"));
			context.put("imeiCode", req.getParameter("imeiCode"));
			context.put("allocatedTo", req.getParameter("allocatedTo"));
			context.put("ownerId", req.getParameter("allocateeName"));
			break;

		case MPESAIPN:
			wiraRequest.setDescription(req.getParameter("senderName") + "-"
					+ req.getParameter("senderPhone"));
			context.put("senderName", req.getParameter("senderName"));
			context.put("senderPhone", req.getParameter("senderPhone"));
			context.put("enteredId", req.getParameter("enteredId"));
			context.put("clCode", req.getParameter("clCode"));
			context.put("customerNames", req.getParameter("customerNames"));
			context.put("mpesaCode", req.getParameter("mpesaCode"));
			context.put("mpesaDate", req.getParameter("mpesaDate"));
			context.put("mpesaTime", req.getParameter("mpesaTime"));
			context.put("mpesaAmount", req.getParameter("mpesaAmount"));
			context.put("ownerId", "Administrator");
			break;

		case DEALLOCATIONREQUEST:
			wiraRequest.setDescription(req.getParameter("allocateeNames") + "-"
					+ req.getParameter("terminalName"));

			Detail detail = new Detail();
			detail.setName("summaryTable");
			Map<String, Object> summaryDetails = new HashMap<String, Object>();

			if (req.getParameter("allocateeNames") != null) {
				summaryDetails.put(
						"terminalUser",
						req.getParameter("userName") + " "
								+ req.getParameter("terminalName"));
			}

			if (req.getParameter("transactionCount") != null) {
				summaryDetails.put("transactionCount",
						req.getParameter("transactionCount"));
			}
			
			System.err.println("transactionSum<<<"+req.getParameter("transactionSum"));
			if (req.getParameter("transactionSum") != null) {
				try {
					summaryDetails.put("transactionSum",
							NumberFormat.getNumberInstance().parse(req.getParameter("transactionSum")));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}

			if (req.getParameter("customerCount") != null) {
				summaryDetails.put("customerCount",
						req.getParameter("customerCount"));
			}

			if (req.getParameter("allocationId") != null) {
				summaryDetails.put("allocationId",
						req.getParameter("allocationId"));
			}

			detail.setDetails(summaryDetails);

			wiraRequest.setDetails(Arrays.asList(detail));

			context.put("ownerId", req.getParameter("userName"));
			context.put("actorId", req.getParameter("userName"));
			break;

		default:
			break;
		}
		return context;
	}

	public static void main(String[] args) throws ParseException {
	
				System.err.println(NumberFormat.getNumberInstance().parse("1,232,456.11"));
	}
}
