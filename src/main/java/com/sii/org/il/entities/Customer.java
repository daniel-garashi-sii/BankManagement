package com.sii.org.il.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sii.org.il.entities.enums.CustomerStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customers")
public class Customer {
	@Id
	@Column(name = "id")
	private Long id;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "email")
	private String email;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private CustomerStatus status = CustomerStatus.ACTIVE;
	
	@JsonManagedReference
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "customer")
	@Column(name = "acount")
	private List<BankAccount> accounts = new ArrayList<BankAccount>();

	@JsonManagedReference
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "customer")
	@Fetch(value = FetchMode.SUBSELECT)
	@Column(name = "loan")
	private List<Loan> loans = new ArrayList<Loan>();

	public Customer(Long id, String firstName, String lastName, String email) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.status = CustomerStatus.ACTIVE;
	}
	
	public Customer(Integer id, String firstName, String lastName, String email, List<BankAccount> accounts, List<Loan> loans) {
		this(Long.valueOf(id), firstName, lastName, email, CustomerStatus.ACTIVE, accounts, loans);
	}
	
	public Customer(Integer id, String firstName, String lastName, String email) {
		this(Long.valueOf(id), firstName, lastName, email);
	}
	
}
