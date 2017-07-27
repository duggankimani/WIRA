package com.duggan.workflow.server.helper.email;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.duggan.workflow.server.dao.helper.SettingsDaoHelper;
import com.duggan.workflow.server.dao.model.LocalAttachment;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProviderImpl;
import com.duggan.workflow.shared.model.settings.SETTINGNAME;
import com.wira.commons.shared.models.HTUser;

public class EmailServiceHelper {

	static Session session = null;
	static Properties props;
	int count = 0;

	static Logger log = Logger.getLogger(EmailServiceHelper.class);

	public static Properties getProperties() {
		initProperties();
		return props;
	}

	public static void initProperties() {
		initProperties(false);
	}

	public static void initProperties(boolean forceRefresh) {

		if (forceRefresh) {
			session = null;
		}

		if (session != null) {
			log.warn(">> Init Reusing javamail session " + session);
			return;
		} else {
			log.warn(">> Init Creating New javamail session " + session);
		}

		try {
			props = new Properties();
			Object auth = SettingsDaoHelper
					.getSettingValue(SETTINGNAME.SMTP_AUTH);
			Object host = SettingsDaoHelper
					.getSettingValue(SETTINGNAME.SMTP_HOST);
			Object password = SettingsDaoHelper
					.getSettingValue(SETTINGNAME.SMTP_PASSWORD);
			Object account = SettingsDaoHelper
					.getSettingValue(SETTINGNAME.SMTP_ACCOUNT);
			Object port = SettingsDaoHelper
					.getSettingValue(SETTINGNAME.SMTP_PORT);
			Object protocol = SettingsDaoHelper
					.getSettingValue(SETTINGNAME.SMTP_PROTOCOL);
			Object starttls = SettingsDaoHelper
					.getSettingValue(SETTINGNAME.SMTP_STARTTLS);
			Object orgName = SettingsDaoHelper
					.getSettingValue(SETTINGNAME.ORGNAME); 

			props.setProperty(SETTINGNAME.SMTP_AUTH.getKey(),
					auth == null ? null : auth.toString());
			props.setProperty(SETTINGNAME.SMTP_AUTH.getKey(),
					auth == null ? null : auth.toString());
			props.setProperty(SETTINGNAME.SMTP_HOST.getKey(),
					host == null ? null : host.toString());
			props.setProperty(SETTINGNAME.SMTP_PASSWORD.getKey(),
					password == null ? null : password.toString());
			props.setProperty(SETTINGNAME.SMTP_ACCOUNT.getKey(),
					account == null ? null : account.toString());
			props.setProperty(SETTINGNAME.SMTP_PORT.getKey(),
					port == null ? null : port.toString());
			props.setProperty(SETTINGNAME.SMTP_PROTOCOL.getKey(),
					protocol == null ? null : protocol.toString());
			props.setProperty(SETTINGNAME.SMTP_STARTTLS.getKey(),
					starttls == null ? null : starttls.toString());
			props.setProperty(SETTINGNAME.ORGNAME.getKey(),
					orgName == null ? null : orgName.toString());

			for (Object prop : props.keySet()) {
				if (prop.equals(SETTINGNAME.SMTP_PASSWORD.getKey())) {
					log.debug(prop + " : xxxxxxxxx");
				} else {
					log.debug(prop + " : " + props.getProperty(prop.toString()));
				}

			}
			session = Session.getInstance(props, new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(props
							.getProperty("mail.smtp.from"), props
							.getProperty("mail.smtp.password"));
				}

			});

		} catch (Exception e) {
			log.warn("EmailServiceHelper.initProperties failed to initialize: "
					+ e.getMessage());
			// e.printStackTrace();
		}
		;

	}

	public static String getProperty(String name) {
		String val = getProperties().getProperty(name);
		return val;
	}

	public static void sendEmail(String body, String subject,
			List<HTUser> recipients, HTUser initiatorId)
			throws MessagingException, UnsupportedEncodingException {
		initProperties();
		assert session != null;
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(getProperties().getProperty(
				"mail.smtp.from"), props.getProperty(SETTINGNAME.ORGNAME
				.getKey()) == null ? "WIRA BPMS" : props
				.getProperty(SETTINGNAME.ORGNAME.getKey())));

		InternetAddress dests[] = new InternetAddress[recipients.size()];
		for (int i = 0; i < recipients.size(); i++) {
			HTUser recipient = recipients.get(i);
			assert recipient.getEmail() != null;
			log.debug("Recipient : " + recipient.getEmail() + " : "
					+ recipient.getFullName());
			dests[i] = new InternetAddress(recipient.getEmail(),
					recipient.getFullName());
		}

		message.setRecipients(Message.RecipientType.TO, dests);
		message.setSubject(subject, "UTF-8");

		try {
			Multipart multipart = new MimeMultipart();
			// HTML
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(body, "text/html;charset=utf-8");
			multipart.addBodyPart(messageBodyPart);

			String rootFolder = "com/duggan/workflow/server/helper/email";

			if (body.contains("cid:imageLogo")) {
				MimeBodyPart part = getBodyType(DB.getAttachmentDao()
						.getSettingImage(SETTINGNAME.ORGLOGO), "<imageLogo>",
						rootFolder + "/logo.png");
				if (part != null) {
					multipart.addBodyPart(part);
				}
			}

			if (body.contains("cid:imageUser")) {
				if (initiatorId != null) {
					MimeBodyPart part = getBodyType(DB.getAttachmentDao()
							.getUserImage(initiatorId.getUserId()),
							"<imageUser>", rootFolder + "/blueman(small).png");
					if (part != null) {
						multipart.addBodyPart(part);
					}
				} else {
					MimeBodyPart part = getBodyType(DB.getAttachmentDao()
							.getUserImage("RD2D-doesnt-exist"), "<imageUser>",
							rootFolder + "/blueman(small).png");
					if (part != null) {
						multipart.addBodyPart(part);
					}
				}
			}

			message.setContent(multipart);
			message.setSentDate(new java.util.Date());

			assert message != null;
			log.debug("Sending ........");
			Transport.send(message);
			log.debug("Email Successfully send........");
		} catch (Exception e) {
			log.fatal("Could not send email: " + subject + ": Cause "
					+ e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Deprecated Use {@link #sendEmail(String, String, List, HTUser)}
	 * 
	 * @param body
	 * @param subject
	 * @param recipient
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 */
	@Deprecated
	public static void sendEmail(String body, String subject, String recipient)
			throws MessagingException, UnsupportedEncodingException {
		sendEmail(body, subject, recipient, null);
	}

	/**
	 * Deprecated Use {@link #sendEmail(String, String, List, HTUser)}
	 * 
	 * @param body
	 * @param subject
	 * @param recipient
	 * @param initiatorId
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 */
	@Deprecated
	public static void sendEmail(String body, String subject, String recipient,
			String initiatorId) throws MessagingException,
			UnsupportedEncodingException {

		initProperties();
		assert session != null;
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress("WIRA BPM", getProperties()
				.getProperty("mail.smtp.from")));

		String[] emails = recipient.split(",");
		InternetAddress dests[] = new InternetAddress[emails.length];
		for (int i = 0; i < emails.length; i++) {
			dests[i] = new InternetAddress(emails[i].trim().toLowerCase());
		}
		message.setRecipients(Message.RecipientType.TO, dests);
		message.setSubject(subject, "UTF-8");

		try {
			Multipart multipart = new MimeMultipart();
			// HTML
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(body, "text/html;charset=utf-8");
			multipart.addBodyPart(messageBodyPart);

			String rootFolder = "com/duggan/workflow/server/helper/email";

			if (body.contains("cid:imageLogo")) {
				MimeBodyPart part = getBodyType(DB.getAttachmentDao()
						.getSettingImage(SETTINGNAME.ORGLOGO), "<imageLogo>",
						rootFolder + "/logo.png");

				if (part != null)
					multipart.addBodyPart(part);
			}

			if (body.contains("cid:imageUser")) {
				if (initiatorId != null) {
					MimeBodyPart part = getBodyType(DB.getAttachmentDao()
							.getUserImage(initiatorId), "<imageUser>",
							rootFolder + "/blueman(small).png");

					if (part != null)
						multipart.addBodyPart(part);

				} else {
					MimeBodyPart part = getBodyType(DB.getAttachmentDao()
							.getUserImage(initiatorId), "<imageUser>",
							rootFolder + "/blueman(small).png");

					if (part != null)
						multipart.addBodyPart(part);
				}
			}

			message.setContent(multipart);
			message.setSentDate(new java.util.Date());

			assert message != null;
			log.warn("Sending ........");
			Transport.send(message);
			log.warn("Email Successfully send........");
		} catch (Exception e) {
			log.fatal("Could not send email: " + subject + ": Cause "
					+ e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static MimeBodyPart getBodyType(LocalAttachment attachment,
			String imageId, String fallbackFileName) throws IOException,
			MessagingException {
		return getBodyType(
				attachment == null ? null : attachment.getAttachment(),
				imageId, fallbackFileName);
	}

	public static MimeBodyPart getBodyType(byte[] attachment, String imageId,
			String fallbackImageName) throws IOException, MessagingException {
		// Image
		MimeBodyPart imageBodyPart = new MimeBodyPart();
		DataSource fds = null;
		if (attachment != null) {
			fds = new ByteArrayDataSource(attachment, "image/png");
		} else {
			InputStream imageStream = EmailServiceHelper.class.getClass()
					.getResourceAsStream("/" + fallbackImageName);
			assert imageStream != null;

			try {
				fds = new ByteArrayDataSource(IOUtils.toByteArray(imageStream),
						"image/png");
			} catch (Exception e) {
			}

			assert fds != null;
		}

		if (fds == null) {
			return null;
		}
		imageBodyPart.setDataHandler(new DataHandler(fds));
		imageBodyPart.setHeader("Content-ID", imageId);

		return imageBodyPart;

	}

	public static void main(String[] args) throws Exception {
		DBTrxProviderImpl.init();
		DB.beginTransaction();
		sendEmail("Hello World", "Test",
				Arrays.asList(new HTUser("Test User", "mdkimani@gmail.com")),
				new HTUser("Test User", "mdkimani@gmail.com"));
		DB.commitTransaction();
		DB.closeSession();
	}

}
