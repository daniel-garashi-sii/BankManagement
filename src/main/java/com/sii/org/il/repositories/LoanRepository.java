package com.sii.org.il.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sii.org.il.entities.Loan;
import com.sii.org.il.entities.enums.LoanStatus;

public interface LoanRepository extends JpaRepository<Loan, Long> {
	
	@Query(name = "Loan.findByLoanStatusAndPaymentDate")
	List<Loan> findByStatusAndPaymentDate(@Param("status")LoanStatus status);
	
	List<Loan> findByStatus(@Param("status")LoanStatus status);
	
}