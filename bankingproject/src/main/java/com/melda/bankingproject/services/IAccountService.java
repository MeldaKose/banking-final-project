package com.melda.bankingproject.services;

import java.util.List;

import com.melda.bankingproject.models.Account;
import com.melda.bankingproject.models.AccountWithBank;

public interface IAccountService {
	
	public Account createAccount(int bank_id, String type);
	public Account accountDetail(int id);
	public Account getByAccountNumber(long number);
	public boolean removeAccount(int id);
	public Account deposit(int id,double balance);
	public String transfer(int senderAccountId,int receiverAccountId,double amount);
	public List<AccountWithBank> getAllAccount();
}
