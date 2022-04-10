package com.email.Email_NSE;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) throws MessagingException {
		System.out.println("Email sending....");
		Properties prop = null;
		try {
			prop = readPropertiesFile("C:\\Users\\am912\\Email_NSE\\src\\main\\resources\\application.properties");
		} catch (IOException e) {
			e.printStackTrace();
		}
		String message=prop.getProperty("message");
		String subject =prop.getProperty("subject");
		String to =prop.getProperty("to");
		String from =prop.getProperty("from");
		  // sendEmail(message,subject,to,from);
		sendAttachment(message,subject,to,from,prop);
		 
	}

	// method for sending mail with attachment
	private static void sendAttachment(String message, String subject, String to, String from,Properties pro) {
		// Variable for host
		String host = pro.getProperty("host");
		final String username = pro.getProperty("username");
		final String password = pro.getProperty("password");

		Properties prop = new Properties(System.getProperties());
		System.out.println("Properties:" + prop);

		// setting host

		prop.put("mail.smtp.host", host);
		prop.put("mail.smtp.port", 465);
		prop.put("mail.smtp.ssl.enable", true);
		prop.put("mail.smtp.auth", true);

		// Step 1: create a session
		Session session = Session.getInstance(prop, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {

				return new PasswordAuthentication(username, password);
			}
		});

		session.setDebug(true);
		// step 2: compose mail(text, file,png)

		MimeMessage msg = new MimeMessage(session);
		try {
			
			String[] fromvalues = pro.get("from").toString().split(",");
			System.out.println(fromvalues);
			for (String fr : fromvalues) {
				msg.setFrom(fr);
			}
			
			String[] values = pro.get("to").toString().split(",");
			System.out.println(values);
			for (String re : values) {
				msg.addRecipient(Message.RecipientType.TO, new InternetAddress(re));
			}
			msg.setSubject(subject);

			// set File Path
			String Path = pro.getProperty("attachment");

			MimeMultipart mimeMultipart = new MimeMultipart();

			MimeBodyPart textMime = new MimeBodyPart();

			MimeBodyPart fileMime = new MimeBodyPart();
			try {
				textMime.setText(message);

				File file = new File(Path);
				fileMime.attachFile(file);

				mimeMultipart.addBodyPart(textMime);
				mimeMultipart.addBodyPart(fileMime);
				msg.setContent(mimeMultipart);
				Transport.send(msg);

			} catch (Exception e) {
				e.printStackTrace();
			}

			System.out.println("Message sent successfully");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Properties readPropertiesFile(String fileName) throws IOException {
		FileInputStream fis = null;
		Properties prop = null;
		try {
			fis = new FileInputStream(fileName);
			prop = new Properties();
			prop.load(fis);
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			fis.close();
		}
		return prop;
	}

}
