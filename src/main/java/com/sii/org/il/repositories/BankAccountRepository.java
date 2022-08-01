package com.sii.org.il.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sii.org.il.entities.BankAccount;
import com.sii.org.il.entities.Customer;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
	
	Optional<BankAccount> findByAccountNumberAndCustomer(Long accountNumber, Customer customer);
	
	@Query(name = "BankAccount.findExceedingBankAccounts")
	List<BankAccount> findExceedingBankAccounts();
}
