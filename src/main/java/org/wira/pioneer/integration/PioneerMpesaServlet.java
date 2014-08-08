package org.wira.pioneer.integration;

import java.io.IOException;
import java.io.PrintWriter;
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
	private Map<String, Object> context;
	private Request wiraRequest;

	/**
	 *
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse resp)
			throws ServletException, IOException {
		// super.doGet(req, resp);

		HttpSession session = null;
		try {
			session = request.getSession(true);
			SessionHelper.setHttpRequest(request);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Response response = new Response();

		try {
			DB.beginTransaction();

			wiraRequest = new Request();
			wiraRequest.setCommandName(IncomingRequestImpl.NEWAPPROVALREQUESTCOMMAND);
			
			System.err.println("Received the request");
			
			String parameter = request.getParameter("docType");
			RequestType requestType = RequestType.getRequestType(parameter);
			
			//Create Context and set it
			createContext(request, requestType);
			wiraRequest.setContext(context);
		
			System.err.println(context);
			
			HTUser user = new HTUser();
			user.setUserId(wiraRequest.getContext("ownerId").toString());
			if (session != null)
				session.setAttribute(ServerConstants.USER, user);

			// Submit
			IncomingRequestImpl handler = new IncomingRequestImpl();
			handler.executeClientRequest(wiraRequest, response);
			
			
			//Response
			PrintWriter out = resp.getWriter();
			BusinessKey key = response.getBusinessKey();
			assert key != null;

			resp.setContentType("text/html");
			out.println("<b style=\"color:red\">" + key.getDocumentId() + " : "
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

	private void createContext(HttpServletRequest req, RequestType reqType) {
		context = new HashMap<String, Object>();
		switch (reqType) {
		case ALLOCATIONREQUEST:
			wiraRequest.setDescription(req.getParameter("allocateeName") + "-"
					+ req.getParameter("terminalName"));
			context.put("allocateeName", req.getParameter("allocateeName"));
			context.put("terminalName", req.getParameter("terminalName"));
			context.put("imeiCode", req.getParameter("imeiCode"));
			context.put("ownerId", req.getParameter("allocateeName"));
			context.put("docType", "TERMINAL ALLOCATION");
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
			context.put("docType", "MPESAIPN");
			context.put("ownerId", "Administrator");
			break;
			
		default:
			break;
		}
		
		
	}

}
