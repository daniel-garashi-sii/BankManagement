package com.sii.org.il.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService implements BankEmailService{

	@Autowired
	private JavaMailSender mailSender;
	
	@Value("${spring.mail.username}")
	private String fromMail;

	@Override
	public void sendEmail(String toMail, String subject, String body) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(fromMail);
		message.setTo(toMail);
		message.setText(body);
		message.setSubject(subject);

		mailSender.send(message);

		System.out.println("Mail sent successfully...");
	}

	@Override
	public void sendEmailWithAttachment(String toMail, String subject, String body, String pathToAttachment) {
		sendEmail(toMail, subject, body);
	}

	
}
