package com.sii.org.il.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.sii.org.il.constants.IBankConstants;
import com.sii.org.il.entities.BankAccount;
import com.sii.org.il.entities.Customer;
import com.sii.org.il.entities.Loan;
import com.sii.org.il.entities.enums.LoanStatus;
import com.sii.org.il.exceptions.BankAccountNotExistException;
import com.sii.org.il.exceptions.BankAccountStausNotActiveException;
import com.sii.org.il.exceptions.CustomerNotExistException;
import com.sii.org.il.exceptions.CustomerStatusNotActiveException;
import com.sii.org.il.exceptions.IncorrectAssociationException;
import com.sii.org.il.exceptions.LoanAlreadyPaidException;
import com.sii.org.il.exceptions.LoanAssociatedException;
import com.sii.org.il.repositories.LoanRepository;

@Service
public class LoanService implements IBankConstants {
	@Autowired
	private LoanRepository loanRepository;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private BankAccountService bankAccountService;
	
	@Scheduled(cron = "20 * * * * *") // (cron = "20 * * * * *") (cron = "0 0 0 1 * *")
	public void bankPaymentsCollection() {
		List<Loan> loansToCollect = getLoansToCollect();
		loansToCollect.forEach((Loan loan) -> {
			BankAccount account = bankAccountService.getBankAccountByNumber(loan.getAccountNumber());

			Double monthlyPayment = loan.getMonthlyPayment();
			Double remainingAmount = Math.max(DEFUALT_BALANCE, loan.getRemainingAmount() - monthlyPayment);
			Integer remainingPaymentMonths = loan.getRemainingPaymentMonths() - 1;
			LoanStatus status = remainingAmount.equals(DEFUALT_BALANCE) ? LoanStatus.PAID_LOAN : LoanStatus.UNPAID_LOAN;

			loan.setRemainingAmount(remainingAmount);
			loan.setRemainingPaymentMonths(remainingPaymentMonths);
			loan.setStatus(status);

			loanRepository.save(loan);

			Double newBalance = account.getBalance() - monthlyPayment;
			account.setBalance(newBalance);

			bankAccountService.saveBankAccount(account);
		});
	}

	public void sendExceedingMail(Customer customer) {
		System.out.println("Hi " + customer.getFirstName() + " " + customer.getLastName());
	}

	public List<Loan> getLoans() {
		return loanRepository.findAll();
	}

	public List<Loan> getLoansByStatus(LoanStatus status) {
		return loanRepository.findByStatus(status);
	}

	private List<Loan> getLoansToCollect() {
		return loanRepository.findByStatusAndPaymentDate(LoanStatus.UNPAID_LOAN);
	}

	@Transactional
	public Loan saveLoan(Long id, Long accountNumber, Loan loan)
			throws CustomerNotExistException, CustomerStatusNotActiveException, LoanAssociatedException,
			BankAccountNotExistException, BankAccountStausNotActiveException {
		Customer customer = customerService.getCustomerById(id);

		if (customerService.isCustomerStatusNotActive(customer.getStatus()))
			throw new CustomerStatusNotActiveException();

		if (!bankAccountService.isBankAccountAssociatedToCustomer(customer.getAccounts(), accountNumber))
			throw new LoanAssociatedException();

		BankAccount account = bankAccountService.getBankAccountByNumber(accountNumber);
		if (bankAccountService.isAccountStatusNotActive(account.getStauts()))
			throw new BankAccountStausNotActiveException();

		account.setBalance(account.getBalance() + loan.getAmount());
		bankAccountService.saveBankAccount(account); // need to check if this line is necessary(compare to deposit func
														// in ForeignExchangeService)

		loan.setAccountNumber(accountNumber);
		Loan loanDB = loanRepository.save(loan);
		loanDB.setCustomer(customer);

		return loanDB;
	}

	@Transactional
	public void loanRepayment(Long id, Long loanNumber, Double partialPayment)
			throws IncorrectAssociationException, LoanAlreadyPaidException {

		Customer customer = customerService.getCustomerById(id);
		if (customerService.isCustomerStatusNotActive(customer.getStatus()))
			throw new CustomerStatusNotActiveException();

		Loan loan = loanRepository.getReferenceById(loanNumber);

		if (loan.getCustomer().getId() != customer.getId())
			throw new IncorrectAssociationException();

		if (loan.getStatus() == LoanStatus.PAID_LOAN)
			throw new LoanAlreadyPaidException();

		Double remainingAmount = Math.max(DEFUALT_BALANCE.doubleValue(), loan.getRemainingAmount() - partialPayment);
		Integer remainingPaymentMonths = remainingAmount.equals(DEFUALT_BALANCE) ? DEFUALT_REMAINING_MONTHS
				: loan.getRemainingPaymentMonths();
		Double monthlyPayment = remainingPaymentMonths.equals(DEFUALT_REMAINING_MONTHS) ? DEFUALT_BALANCE
				: remainingAmount / remainingPaymentMonths;
		LoanStatus status = remainingAmount.equals(DEFUALT_BALANCE) ? LoanStatus.PAID_LOAN : LoanStatus.UNPAID_LOAN;

		loan.setRemainingAmount(remainingAmount);
		loan.setRemainingPaymentMonths(remainingPaymentMonths);
		loan.setMonthlyPayment(monthlyPayment);
		loan.setStatus(status);
	}

}
