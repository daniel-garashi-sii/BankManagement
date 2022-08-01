package com.sii.org.il.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.sii.org.il.constants.IBankConstants;
import com.sii.org.il.entities.BankAccount;
import com.sii.org.il.entities.Customer;
import com.sii.org.il.entities.enums.BankAccountStatus;
import com.sii.org.il.exceptions.BankAccountNotExistException;
import com.sii.org.il.exceptions.CreditOverdraftException;
import com.sii.org.il.exceptions.CustomerNotExistException;
import com.sii.org.il.exceptions.CustomerStatusNotActiveException;
import com.sii.org.il.exceptions.IncorrectAssociationException;
import com.sii.org.il.repositories.BankAccountRepository;

@Service
public class BankAccountService implements IBankConstants {

	@Autowired
	private BankAccountRepository bankAccountRepository;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private BankEmailService emailService;

	@Scheduled(cron = "0 0 0 * * *")
	public void handleExceedingBankAccounts() {
		List<BankAccount> exceedingAccounts = getExceedingBankAccounts();
		exceedingAccounts.forEach((BankAccount account) -> {
			Customer customer = account.getCustomer();
			String toMail = customer.getEmail();
			String subject = "Exceeding bank account limit";
			String body = "Hi " + customer.getFirstName() + ", \n\n\nThere is an exception in your bank account";
			
			emailService.sendEmail(toMail, subject, body);
		});
		
	}

	public List<BankAccount> getExceedingBankAccounts(){
		return bankAccountRepository.findExceedingBankAccounts(); 
	}

	public List<BankAccount> getBankAccounts() {
		return bankAccountRepository.findAll();
	}

	public BankAccount getBankAccountByNumber(Long accountNumebr) throws BankAccountNotExistException {
		Optional<BankAccount> account = bankAccountRepository.findById(accountNumebr);
		if (account.isPresent())
			return account.get();

		throw new BankAccountNotExistException();
	}

	public Double getBalanceByBankAccountNumber(Long id, Long accountNumebr)
			throws CustomerNotExistException, IncorrectAssociationException {
		Customer customer = customerService.getCustomerById(id);
		Optional<BankAccount> account = bankAccountRepository.findByAccountNumberAndCustomer(accountNumebr, customer);
		if (account.isEmpty())
			throw new IncorrectAssociationException();

		return account.get().getBalance();
	}

	@Transactional
	public BankAccount openBankAccount(Long id) {
		Customer customer = customerService.getCustomerById(id);
		BankAccount account = new BankAccount(customer, BankAccountStatus.ACTIVE, DEFUALT_BALANCE, DEFUALT_CREDIT_LINE);
		return saveBankAccount(account);
	}

	@Transactional
	public BankAccount saveBankAccount(BankAccount account) {
		return bankAccountRepository.save(account);
	}

	@Transactional
	public void suspendedAccount(Long accountNumebr) throws BankAccountNotExistException {
		if (!bankAccountRepository.existsById(accountNumebr))
			throw new BankAccountNotExistException();

		BankAccount account = bankAccountRepository.getReferenceById(accountNumebr);
		account.setStauts(BankAccountStatus.SUSPENDED);
	}

	@Transactional
	public void deposit(Long accountNumebr, Double depositAmount) {
		BankAccount account = bankAccountRepository.getReferenceById(accountNumebr);

		depositAmount = depositAmount < 0 ? DEFUALT_BALANCE : depositAmount;
		account.setBalance(account.getBalance() + depositAmount);
	}

	@Transactional
	public void withdrawal(Long accountNumebr, Double withdrawalAmount) {
		BankAccount account = bankAccountRepository.getReferenceById(accountNumebr);

		Double newBalance = account.getBalance() - withdrawalAmount;
		if (!isCanWithdraw(newBalance, account.getCreditLine()))
			throw new CreditOverdraftException();

		withdrawalAmount = withdrawalAmount < 0 ? DEFUALT_BALANCE : withdrawalAmount;
		account.setBalance(newBalance);
	}

	@Transactional
	public void transferBetweenBankAccounts(Long id, Long fromAccountNumber, Double transferAmount,
			Long toAccountNumber) throws CustomerNotExistException, CustomerStatusNotActiveException,
			IncorrectAssociationException, BankAccountNotExistException, CreditOverdraftException {

		Customer customer = customerService.getCustomerById(id);

		if (customerService.isCustomerStatusNotActive(customer.getStatus()))
			throw new CustomerStatusNotActiveException();

		if (!isBankAccountAssociatedToCustomer(customer.getAccounts(), fromAccountNumber))
			throw new IncorrectAssociationException();

		if (!isBankAccountExists(fromAccountNumber) || !isBankAccountExists(toAccountNumber))
			throw new BankAccountNotExistException();

		if (fromAccountNumber == toAccountNumber)
			return;

		BankAccount fromAccount = bankAccountRepository.getReferenceById(fromAccountNumber);
		BankAccount toAccount = bankAccountRepository.getReferenceById(toAccountNumber);

		Double newBalance = fromAccount.getBalance() - transferAmount;
		if (!isCanWithdraw(newBalance, fromAccount.getCreditLine()))
			throw new CreditOverdraftException();

		transferAmount = transferAmount < 0 ? DEFUALT_BALANCE : transferAmount;
		toAccount.setBalance(toAccount.getBalance() + transferAmount);
		fromAccount.setBalance(newBalance);
	}

	public boolean isCanWithdraw(Double newBalance, Double creditLine) {
		return !(newBalance < 0 && creditLine < Math.abs(newBalance));
	}

	public boolean isBankAccountAssociatedToCustomer(List<BankAccount> customerAccounts, Long accountNumber) {
		boolean isAccountAssociated = false;

		for (BankAccount account : customerAccounts) {
			if (account.getAccountNumber() == accountNumber) {
				isAccountAssociated = true;
				break;
			}
		}

		return isAccountAssociated;
	}

	public boolean isAccountStatusNotActive(BankAccountStatus accountStatus) {
		return BankAccountStatus.ACTIVE != accountStatus;
	}

	public boolean isBankAccountExists(Long accountNumber) {
		return bankAccountRepository.existsById(accountNumber);
	}
}
