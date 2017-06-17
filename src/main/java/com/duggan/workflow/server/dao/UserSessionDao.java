package com.duggan.workflow.server.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.duggan.workflow.server.dao.model.UserSession;
import com.wira.commons.shared.models.HTUser;

public class UserSessionDao extends BaseDaoImpl {
	
	private static final int TWO_WEEKS_AGO_IN_DAYS = -14;

	private final Logger logger = Logger.getLogger(getClass());
	
	public UserSessionDao(EntityManager em) {
		super(em);
	}
	
	public String createSessionCookie(String currentCookie, HTUser userDto) {
		String cookie = UUID.randomUUID().toString();
		UserSession userSession = new UserSession(userDto.getRefId(), cookie);
		save(userSession);
		logger.info("UserSessionDao.createLoggedInCookie(user) user=" + userDto + " userSessionCookie="
				+ userSession.getCookie());
		return userSession.getCookie();
	}

	public void removeLoggedInCookie(HTUser userDto) {
		if (userDto == null) {
			return;
		}
		getEntityManager().createNativeQuery("delete from UserSession where userRefId=:userId")
				.setParameter("userId", userDto.getRefId()).executeUpdate();

		logger.info("UserSessionDao.removeLoggedInCookie(user): Cookie is removed from database.");
	}

	private Date getTwoWeeksAgo() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, TWO_WEEKS_AGO_IN_DAYS);

		return calendar.getTime();
	}

	private List<UserSession> findUserSession(String userRefId) {
		Query query = getEntityManager().createQuery("FROM UserSession where userRefId=:refId order by id desc");

		return getResultList(query);
	}

	public void updateLogedInCookie(String loggedInCookie) {
		int query = getEntityManager()
				.createNativeQuery("UPDATE usersession u SET u.created=:date where u.cookie=:cookie")
				.setParameter("cookie", loggedInCookie).setParameter("date", new Date()).executeUpdate();
	}
}
