package com.sii.org.il.services;

import com.sii.org.il.entities.Customer;

public interface BankEmailService {
	public void sendEmail(String toMail, String subject, String body);

	public void sendEmailWithAttachment(String toMail, String subject, String body, String pathToAttachment);
	
	default public void buildBankTemplateMessage(Customer customer) {
		
	}
}
