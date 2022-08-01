package com.sii.org.il.services;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.sii.org.il.entities.BankAccount;
import com.sii.org.il.entities.ForeignExchange;
import com.sii.org.il.exceptions.BankAccountNotExistException;
import com.sii.org.il.exceptions.CreditOverdraftException;

@Service
public class ForeignExchangeService {
	private final static String URL = "https://api.exchangerate-api.com/v4/latest";

	private final static String API_LAYER_URL = "https://api.apilayer.com/exchangerates_data/latest?";
	private final static String API_LAYER_KEY = "7r52Xe4MPing0y9ByAD8UU6VChXcrolV";
	private final static String API_LAYER_BASE = "base=";
	private final static String FOREIGN_EXCHANGE_BASE = "ILS";

	@Autowired
	private BankAccountService bankAccountService;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ForeignExchange foreignExchange;

	@Bean
	@Scheduled(cron = "0 0 0 * * *")
	public void getForeignExchange() throws IOException {
		final HttpHeaders headers = new HttpHeaders();
		headers.set("apikey", API_LAYER_KEY);
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

		HttpEntity<ForeignExchange> request = new HttpEntity<ForeignExchange>(headers);
		ResponseEntity<ForeignExchange> response = restTemplate.exchange(
				API_LAYER_URL + API_LAYER_BASE + FOREIGN_EXCHANGE_BASE, HttpMethod.GET, request, ForeignExchange.class);

		if (response.getStatusCode() == HttpStatus.OK) {
			foreignExchange = response.getBody();
			System.out.println("Status " + response.getStatusCodeValue() + ": foreign currencies updated successfully.");
		} else {
			System.out.println("Status " + response.getStatusCodeValue() + ": Failed to update foreign currencies.");
		}

	}

	public ForeignExchange getForeignCurrencies() {
		return foreignExchange;
	}

	@Transactional
	public void deposit(Long accountNumber, String convertFrom, Double amountToDeposit)
			throws BankAccountNotExistException {
		BankAccount account = bankAccountService.getBankAccountByNumber(accountNumber);

		Double amountAsBase = convert(convertFrom, amountToDeposit, FOREIGN_EXCHANGE_BASE);
		account.setBalance(account.getBalance() + amountAsBase);
		// bankAccountService.saveBankAccount(account);
	}

	@Transactional
	public void withdrawal(Long accountNumber, String convertTo, Double amountToWithdrawal) {
		BankAccount account = bankAccountService.getBankAccountByNumber(accountNumber);
		Double convertAmountToBase = convert(convertTo, amountToWithdrawal, FOREIGN_EXCHANGE_BASE);
		Double newBalance = account.getBalance() - convertAmountToBase;

		if (!bankAccountService.isCanWithdraw(newBalance, account.getCreditLine()))
			throw new CreditOverdraftException();

		account.setBalance(newBalance);
		
		//add use of account service instead
	}

	public Double convert(String convertFrom, Double amount, String convertTo) {
		Map<String, Double> rates = foreignExchange.getRates();
		Double toBaseFE = amount / rates.get(convertFrom);
		Double convertToFE = toBaseFE * rates.get(convertTo);

		return convertToFE;
	}

}
