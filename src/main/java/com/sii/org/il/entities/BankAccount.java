package com.sii.org.il.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sii.org.il.entities.enums.BankAccountStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bank_accounts")
public class BankAccount {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "account_number")
	private Long accountNumber;

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;

	@Enumerated(EnumType.STRING)
	@Column(name = "stauts")
	private BankAccountStatus stauts;

	@Column(name = "balance")
	private Double balance;

	@Column(name = "credit_line")
	private Double creditLine;

	public BankAccount(Customer customer, BankAccountStatus stauts, Double balance, Double creditLine) {
		super();
		this.customer = customer;
		this.stauts = stauts;
		this.balance = balance;
		this.creditLine = creditLine;
	}

	public BankAccount(Customer customer, BankAccountStatus stauts, Integer balance, Integer creditLine) {
		this(customer, stauts, Double.valueOf(balance), Double.valueOf(creditLine));
	}

	@Override
	public String toString() {
		return "BankAccount [accountNumber=" + accountNumber + ", stauts=" + stauts + ", balance=" + balance
				+ ", creditLine=" + creditLine + "]";
	}

}
