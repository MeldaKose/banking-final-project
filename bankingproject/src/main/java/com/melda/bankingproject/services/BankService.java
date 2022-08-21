package com.melda.bankingproject.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.melda.bankingproject.models.Bank;
import com.melda.bankingproject.repository.IBankRepository;

@Service
public class BankService implements IBankService {

	@Autowired
	private IBankRepository bankRepository;
	
	@Override
	public Bank createBank(String name) {
		if (this.bankRepository.selectByName(name) == null) {
			this.bankRepository.createBank(name);
			Bank bank = this.bankRepository.selectByName(name);
			return bank;
		}
		return null;
	}

}
