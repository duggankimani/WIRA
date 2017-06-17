package com.duggan.workflow.server.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.duggan.workflow.server.dao.model.User;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.helper.auth.UserDaoHelper;
import com.wira.commons.shared.models.HTUser;

public class AccountsServlet extends BaseServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		initRequest(req, resp);
	}
	
	@Override
	protected void executeRequest(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		String action = req.getParameter("action");
		
		switch(action){
		case "GetUser":
			getUser(req, resp);
			break;
		case "UpdatePassword":
			updatePassword(req, resp);
			break;
		case "sendmail":
			sendAccountResetEmail(req, resp);
			break;
		default:
			resp.setContentType("text/html");
			resp.setStatus(405);
			writeOut(resp, ("Action '"+action+"' Not found").getBytes());
			break;
		}
		
	}

	private void sendAccountResetEmail(HttpServletRequest req,
			HttpServletResponse resp) {
		
		String email = req.getParameter("email");
		if(email==null){
			throw new RuntimeException("Email must not be null");
		}
		
		UserDaoHelper helper = new UserDaoHelper();
		HTUser user = helper.getUserByEmail(email);
		
		if(user==null){
			throw new RuntimeException("User with email '"+email+"' not found.");
		}
		
		helper.sendAccountResetEmail(user);
	}

	private void updatePassword(HttpServletRequest req, HttpServletResponse resp) {
		String activationRefId=req.getParameter("aid");
		String userRefId = req.getParameter("uid");
		String firstName = req.getParameter("firstname");
		String lastName = req.getParameter("lastname");
		String password = req.getParameter("password");
		
		if(firstName==null || lastName==null || password==null){
			throw new IllegalArgumentException("Null values not allowed for email, firstname, lastname or password");
		}
		
		if(!DB.getUserDao().isValid(activationRefId, userRefId)){
			throw new RuntimeException("Invalid activation link");
		}
		
		User user = DB.getUserDao().findByRefId(userRefId, User.class);
		assert user!=null;
		
		user.setFirstName(firstName);
		user.setLastName(lastName);
		DB.getUserDao().save(user);
		
		new UserDaoHelper().updatePassword(user.getUserId(), password);
	}

	private void getUser(HttpServletRequest req, HttpServletResponse resp) {
		String activationRefId=req.getParameter("aid");
		String userRefId = req.getParameter("uid");
		
		if(!DB.getUserDao().isValid(activationRefId, userRefId)){
			throw new RuntimeException("Invalid activation link");
		}
		
		HTUser user = DB.getUserDao().getBasicUser(userRefId);
		
		try{
			JSONObject userJson = new JSONObject();
			userJson.put("refId", user.getRefId());
			userJson.put("firstName", user.getName());
			userJson.put("lastName", user.getSurname());
			userJson.put("email", user.getEmail());
			userJson.put("lastName", user.getSurname());
			
			String json = userJson.toString();
			resp.setContentType("application/json");
			PrintWriter writer = resp.getWriter();
			writer.write(json);
			writer.close();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
}
