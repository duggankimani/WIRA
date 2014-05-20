package com.duggan.workflow.server.helper.email;

import java.io.UnsupportedEncodingException;
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

import org.apache.log4j.Logger;

import com.duggan.workflow.server.dao.helper.SettingsDaoHelper;
import com.duggan.workflow.shared.model.settings.SETTINGNAME;

public class EmailServiceHelper {

	static Session session = null;
	static Properties props;
	int count=0;
	
	static Logger log = Logger.getLogger(EmailServiceHelper.class);
	
	public static Properties getProperties(){
		if(session==null){
			initProperties();
		}
		return props;
	}

	public static void initProperties() {
		
		try {
			props = new Properties();
			Object auth = SettingsDaoHelper.getSettingValue(SETTINGNAME.SMTP_AUTH);
			Object host = SettingsDaoHelper.getSettingValue(SETTINGNAME.SMTP_HOST);
			Object password = SettingsDaoHelper.getSettingValue(SETTINGNAME.SMTP_PASSWORD);
			Object account = SettingsDaoHelper.getSettingValue(SETTINGNAME.SMTP_ACCOUNT);
			Object port = SettingsDaoHelper.getSettingValue(SETTINGNAME.SMTP_PORT);
			Object protocol = SettingsDaoHelper.getSettingValue(SETTINGNAME.SMTP_PROTOCOL);
			Object starttls= SettingsDaoHelper.getSettingValue(SETTINGNAME.SMTP_STARTTLS);
			
			props.setProperty(SETTINGNAME.SMTP_AUTH.getKey(), auth==null?null: auth.toString());
			props.setProperty(SETTINGNAME.SMTP_HOST.getKey(), host==null?null: host.toString());
			props.setProperty(SETTINGNAME.SMTP_PASSWORD.getKey(), password==null?null: password.toString());
			props.setProperty(SETTINGNAME.SMTP_ACCOUNT.getKey(), account==null?null: account.toString());
			props.setProperty(SETTINGNAME.SMTP_PORT.getKey(), port==null?null: port.toString());
			props.setProperty(SETTINGNAME.SMTP_PROTOCOL.getKey(), protocol==null?null: protocol.toString());
			props.setProperty(SETTINGNAME.SMTP_STARTTLS.getKey(), starttls==null?null: starttls.toString());
			
			for(Object prop: props.keySet()){
				log.debug(prop+" : "+props.getProperty(prop.toString()));
			}
			session = Session.getDefaultInstance(props,new Authenticator() {
	            @Override
	            protected PasswordAuthentication getPasswordAuthentication() {
	                return new PasswordAuthentication(props.getProperty("mail.smtp.from"),
	                		props.getProperty("mail.smtp.password"));
	            }

	        });
			
		} catch (Exception e) {
			log.warn("EmailServiceHelper.initProperties failed to initialize: "+e.getMessage());
			//e.printStackTrace();
		}
		;

	}

	public static String getProperty(String name) {
		String val = getProperties().getProperty(name);
		return val;
	}

	public static void sendEmail(String body, String subject, String recipient)
			throws MessagingException, UnsupportedEncodingException {

		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(getProperties().getProperty("mail.smtp.from")));
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
