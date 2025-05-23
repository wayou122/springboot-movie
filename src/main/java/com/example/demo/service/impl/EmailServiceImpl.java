package com.example.demo.service.impl;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

import com.example.demo.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService{
	
	String googleAppPassword = "ymjz tofg zikn znom";
	String from = "yaogoking@gmail.com";
	
	public void sendEmail(String to, String confirmUrl) {

		String host = "smtp.gmail.com";
		Properties properties = new Properties();
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "587");

		Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(from, googleAppPassword);
			}
		});

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			message.setSubject("會員註冊確認信");
			message.setText("請點選以下連結進行確認：\n" + confirmUrl);
			Transport.send(message);
			System.out.println("發送成功: " + to);
		} catch (MessagingException e) {
			System.out.println("發送失敗: " + e.getMessage());
		}
	}

}
