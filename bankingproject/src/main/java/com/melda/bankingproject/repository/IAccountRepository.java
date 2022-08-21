package com.melda.bankingproject.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.melda.bankingproject.models.Account;
import com.melda.bankingproject.models.AccountWithBank;

@Mapper
public interface IAccountRepository {
	public void createAccount(Account account);
	public Account selectById(int id);
	public Account selectByAccountNumber(long number);
	public void removeAccount(int id);
	public void updateBalance(Account account);
	public List<AccountWithBank> selectAll(int user_id);
}
