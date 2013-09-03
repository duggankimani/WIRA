package com.duggan.workflow.server.helper.email;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

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

public class EmailServiceHelper {

	static Properties props = new Properties();
	static Session session = null;
	static {
		try {
			
			InputStream is =null;
			//is = ClassLoader.getSystemResourceAsStream("smtp.properties");
//			is = Thread.currentThread().getContextClassLoader().
//					getResourceAsStream("com/duggan/workflow/server/helper/email/smtp.properties");
			is = EmailServiceHelper.class.getResourceAsStream("/smtp.properties");
			
			//ClassLoader.
			
			assert is!=null;			
			props.load(is);
			is.close();
			session = Session.getDefaultInstance(props,new Authenticator() {
	            @Override
	            protected PasswordAuthentication getPasswordAuthentication() {
	                return new PasswordAuthentication(props.getProperty("mail.smtp.from"),
	                		props.getProperty("mail.smtp.password"));
	            }

	        });
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		;

	}

	public static String getProperty(String name) {
		String val = props.getProperty(name);
		return val;
	}

	public static void sendEmail(String body, String subject, String recipient)
			throws MessagingException, UnsupportedEncodingException {

		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(props.getProperty("mail.smtp.from")));
		String[] emails = recipient.split(",");
		InternetAddress dests[] = new InternetAddress[emails.length];
		for (int i = 0; i < emails.length; i++) {
			dests[i] = new InternetAddress(emails[i].trim().toLowerCase());
		}
		message.setRecipients(Message.RecipientType.TO, dests);
		message.setSubject(subject, "UTF-8");
		Multipart mp = new MimeMultipart();
		MimeBodyPart mbp = new MimeBodyPart();
		mbp.setContent(body, "text/html;charset=utf-8");
		mp.addBodyPart(mbp);
		message.setContent(mp);
		message.setSentDate(new java.util.Date());

		Transport.send(message);
	}

	public static void main(String[] args) throws Exception{
		sendEmail("Hello world", "Test 1", "mdkimani@gmail.com");
	}
}
