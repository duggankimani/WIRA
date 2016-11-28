package com.duggan.workflow.server.guice;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.transaction.UserTransaction;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class UserTransactionProvider implements Provider<UserTransaction> {
	
	@Inject
	public UserTransactionProvider() {
	}
	
	@Override
	public UserTransaction get() {

		UserTransaction userTrx = null;

		try {

			Context ctx = new InitialContext();
			Object value = ctx.lookup("java:comp/UserTransaction");
			if (value == null) {
				return null;
			}
			userTrx = (UserTransaction) value;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return userTrx;

	}
}
