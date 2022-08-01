package com.sii.org.il.services;

import java.io.File;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class BankEmailServiceImpl /* implements BankEmailService */{
	@Autowired
	private JavaMailSender emailSender;

	/* @Override */
	public void sendEmail(String to, String subject, String text) {

		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("daniel_ga@sii.org.il");
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);
		emailSender.send(message);

	}

	/* @Override */
	public void sendEmailWithAttachment(String to, String subject, String text, String pathToAttachment) {

		MimeMessage message = emailSender.createMimeMessage();

		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(message, true);
			
			helper.setFrom("daniel_ga@sii.org.il");
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(text);

			FileSystemResource file = new FileSystemResource(new File(pathToAttachment));
			helper.addAttachment("Invoice", file);

			emailSender.send(message);
			System.out.println("The email was sent successfully");
		} catch (MessagingException e) {
			System.out.println("Failed to send the email");
		}
	}
}
