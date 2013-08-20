package com.duggan.workflow.test.bpm;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProvider;
import com.duggan.workflow.server.helper.auth.LoginHelper;
import com.duggan.workflow.server.helper.dao.CommentDaoHelper;
import com.duggan.workflow.shared.model.Comment;

public class TestSaveComment {

	@Before
	public void setup() {
		DBTrxProvider.init();
		LoginHelper.get();
		DB.beginTransaction();
	}

	@Test
	public void save() {
		Comment comment = new Comment();
		comment.setComment("Comment xyz .......... ");
		comment.setCreated(new Date());
		comment.setCreatedby("mariano");
		comment.setDocumentId(2L);
		comment.setId(null);
		comment.setUserId("mariano");
		CommentDaoHelper.saveComment(comment);
	}

	@After
	public void tearDown() {
		DB.commitTransaction();
		try {
			LoginHelper.get().close();
		} catch (Exception e) {
		}
		DBTrxProvider.close();
	}
}
