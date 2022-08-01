package com.sii.org.il.entities;

import java.sql.Date;

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
import com.sii.org.il.entities.enums.LoanStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "loans")
public class Loan {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "loanNumber")
	private Long loanNumber;

	@Column(name = "amount")
	private Double amount;

	@Column(name = "interest")
	private Float interest;

	@Column(name = "remaining_amount")
	private Double remainingAmount;

	@Column(name = "monthly_payment")
	private Double monthlyPayment;

	@Column(name = "remaining_payment_months")
	private Integer remainingPaymentMonths;

	@Column(name = "payment_date")
	private Date paymentDate;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private LoanStatus status = LoanStatus.UNPAID_LOAN;

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;
	
	@Column(name = "account_number")
	private Long accountNumber;

	public Loan(Double amount, Float interest, Double remainingAmount, Double monthlyPayment,
			Integer remainingPaymentMonths, Date paymentDate, LoanStatus status, Customer customer, Long accountNumber) {
		super();
		this.amount = amount;
		this.interest = interest;
		this.remainingAmount = remainingAmount;
		this.monthlyPayment = monthlyPayment;
		this.remainingPaymentMonths = remainingPaymentMonths;
		this.paymentDate = paymentDate;
		this.status = status;
		this.customer = customer;
		this.accountNumber = accountNumber;
	}
	
	public Loan(Double amount, Float interest, Double remainingAmount, Double monthlyPayment,
			Integer remainingPaymentMonths, Date paymentDate, LoanStatus status) {
		super();
		this.amount = amount;
		this.interest = interest;
		this.remainingAmount = remainingAmount;
		this.monthlyPayment = monthlyPayment;
		this.remainingPaymentMonths = remainingPaymentMonths;
		this.paymentDate = paymentDate;
		this.status = status;
	}
	
	public Loan(Double amount, Float interest, Double remainingAmount, Double monthlyPayment,
			Integer remainingPaymentMonths, Date paymentDate, LoanStatus status, Long accountNumber) {
		super();
		this.amount = amount;
		this.interest = interest;
		this.remainingAmount = remainingAmount;
		this.monthlyPayment = monthlyPayment;
		this.remainingPaymentMonths = remainingPaymentMonths;
		this.paymentDate = paymentDate;
		this.status = status;
		this.accountNumber = accountNumber;
	}
	
	public Loan(Double amount, Float interest, Double remainingAmount, Double monthlyPayment,
			Integer remainingPaymentMonths, Date paymentDate) {
		super();
		this.amount = amount;
		this.interest = interest;
		this.remainingAmount = remainingAmount;
		this.monthlyPayment = monthlyPayment;
		this.remainingPaymentMonths = remainingPaymentMonths;
		this.paymentDate = paymentDate;
		this.status = LoanStatus.UNPAID_LOAN;
	}

	@Override
	public String toString() {
		return "Loan [loanNumber=" + loanNumber + ", amount=" + amount + ", interest=" + interest + ", remainingAmount="
				+ remainingAmount + ", monthlyPayment=" + monthlyPayment + ", remainingPaymentMonths=" + remainingPaymentMonths
				+ ", paymentDate=" + paymentDate + ", status=" + status + ", accountNumber=" + accountNumber + "]";
	}
}
