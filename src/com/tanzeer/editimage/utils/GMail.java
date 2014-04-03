package com.tanzeer.editimage.utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.harmony.awt.datatransfer.DataSource;

import android.R.string;
import android.util.Log;

public class GMail {

	//final String emailPort = "587";// gmail's smtp port
	//final String smtpAuth = "true";
	//final String starttls = "true";
	//final String emailHost = "smtp.gmail.com";
	// final String fromUser = "giftvincy@gmail.com";
	// final String fromUserEmailPassword = "jk2008gv";

	 String emailPort = "";// gmail's smtp port
	final String smtpAuth = "true";
	final String starttls = "true";
	String emailHost = "";

	String fromEmail;
	String fromPassword;
	List<String> toEmailList;
	String emailSubject;
	String emailBody;
	String path;

	Properties emailProperties;
	Session mailSession;
	MimeMessage emailMessage;

	public GMail() {

	}

	public GMail(String fromEmail, String fromPassword,
			List<String> toEmailList, String emailSubject, String emailBody,String ipath,String smtp,String port) {
		this.fromEmail=fromEmail;
		this.fromPassword = fromPassword;
		this.toEmailList = toEmailList;
		this.emailSubject = emailSubject;
		this.emailBody = emailBody;
		this.path=ipath;
		Log.v("path",this.path);
		emailProperties = System.getProperties();
		emailProperties.put("mail.smtp.port", port);
		emailProperties.put("mail.smtp.auth", smtpAuth);
		emailProperties.put("mail.smtp.starttls.enable", starttls);
		Log.i("GMail", "Mail server properties set.");
		emailHost=smtp;
	}

	public MimeMessage createEmailMessage() throws AddressException,
			MessagingException, UnsupportedEncodingException {

		mailSession = Session.getDefaultInstance(emailProperties, null);
		emailMessage = new MimeMessage(mailSession);

		emailMessage.setFrom(new InternetAddress(fromEmail, fromEmail));
		for (String toEmail : toEmailList) {
			Log.i("GMail","toEmail: "+toEmail);
			emailMessage.addRecipient(Message.RecipientType.TO,
					new InternetAddress(toEmail));
		}
		emailMessage.setSubject(emailSubject);
		BodyPart bodypartyext=new MimeBodyPart();
		bodypartyext.setText(emailBody);
		//emailMessage.s
		Multipart multipart=new MimeMultipart();
		BodyPart bodypart=new MimeBodyPart();
		String path=this.path;
		FileDataSource datasource=new FileDataSource(path);
		bodypart.setDataHandler(new DataHandler(datasource));
		bodypart.setFileName(path);
		multipart.addBodyPart(bodypart);
		multipart.addBodyPart(bodypartyext);
		//emailMessage.setContent(emailBody, "text/html");// for a html email
		// emailMessage.setText(emailBody);// for a text email
		Log.i("GMail", "Email Message created.");
		emailMessage.setContent(multipart);
		//emailMessage.setContent(mp)
		return emailMessage;
	}
	public void sendEmail() throws AddressException, MessagingException {

		Transport transport = mailSession.getTransport("smtp");
		Log.v("host",emailHost+fromEmail+fromPassword);
		transport.connect(emailHost, fromEmail, fromPassword);
		Log.i("GMail","allrecipients: "+emailMessage.getAllRecipients());
		transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
		transport.close();
		Log.i("GMail", "Email sent successfully.");
	}

}
