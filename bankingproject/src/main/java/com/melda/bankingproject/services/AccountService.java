package com.melda.bankingproject.services;

import java.sql.Timestamp;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.melda.bankingproject.models.MyUser;
import com.melda.bankingproject.exceptions.AccessDeniedException;
import com.melda.bankingproject.exceptions.InsufficientBalanceException;
import com.melda.bankingproject.exchanger.IAmountExchanger;
import com.melda.bankingproject.models.Account;
import com.melda.bankingproject.models.AccountWithBank;
import com.melda.bankingproject.repository.IAccountRepository;
import com.melda.bankingproject.repository.IUserRepository;

@Service
public class AccountService implements IAccountService{
	
	@Autowired
	private IAccountRepository accountRepository;
	
	@Autowired
	private IUserRepository userRepository;
	
	@Autowired
	private IAmountExchanger amountExchanger;

	@Override
	public Account createAccount(int bank_id, String type) {
		if(type.equals("USD") || type.equals("TRY") || type.equals("GAU")) {
			Timestamp date=new Timestamp(System.currentTimeMillis());
			User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			MyUser user=this.userRepository.selectByUsername(authUser.getUsername());
			Account account=new Account();
			account.setUser_id(user.getId());
			account.setBank_id(bank_id);
			account.setNumber(new Random().nextLong(999999999L, 10000000000L));
			account.setType(type);
			account.setBalance(0);
			account.setCreation_date(date);
			account.setLast_update_date(date);
			account.setIs_deleted(false);
			this.accountRepository.createAccount(account);
			return this.accountRepository.selectByAccountNumber(account.getNumber());
		}
		return null;
	}

	@Override
	public Account accountDetail(int id) {
		User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		MyUser user=this.userRepository.selectByUsername(authUser.getUsername());
		Account account=this.accountRepository.selectById(id);
		if(account != null && account.isIs_deleted()==false && user.getId()==account.getUser_id()) {
			return account;
		}
		throw new AccessDeniedException("Access Denied");
	}

	@Override
	public boolean removeAccount(int id) {
		Account account = this.accountRepository.selectById(id);
		if (account != null && account.isIs_deleted() == false) {
			this.accountRepository.removeAccount(id);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Account deposit(int id,double balance) {
		Account account =this.accountDetail(id);
		if(account!=null) {
			account.setBalance(account.getBalance() + balance);
			account.setLast_update_date(new Timestamp(System.currentTimeMillis()));
			this.accountRepository.updateBalance(account);
			return account;
		}
		throw new AccessDeniedException("Access Denied");
	}
	
	@Transactional
	@Override
	public String transfer(int senderAccountId,int receiverAccountId,double amount) {
		Account recieverAccount =this.accountRepository.selectById(receiverAccountId);
		Account senderAccount =this.accountDetail(senderAccountId);
		if(recieverAccount!= null && recieverAccount.isIs_deleted() == false && senderAccount != null) {
			double amountResult=amount;
			double result=amount;
			if(recieverAccount.getBank_id() != senderAccount.getBank_id()) {
				amountResult=amountResult+amountExchanger.getEft(senderAccount.getType());
			}
			if(0<=senderAccount.getBalance()-amountResult) {
				if (!recieverAccount.getType().equals(senderAccount.getType())) {
					result = amountExchanger.exchangeAmount(amount, recieverAccount.getType(),
							senderAccount.getType());
				}
				Timestamp date=new Timestamp(System.currentTimeMillis());
				recieverAccount.setBalance(recieverAccount.getBalance() + result);
				senderAccount.setBalance(senderAccount.getBalance() - amountResult);
				senderAccount.setLast_update_date(date);
				recieverAccount.setLast_update_date(date);
				this.accountRepository.updateBalance(senderAccount);
				this.accountRepository.updateBalance(recieverAccount);
				String info =amount + senderAccount.getType()+ ", "+senderAccount.getNumber()+" to "+ recieverAccount.getNumber()+ " : transferred";
				return info;
			}
			throw new InsufficientBalanceException("Insufficient Balance");
		}
		throw new AccessDeniedException("Access Denied");
	}

	@Override
	public List<AccountWithBank> getAllAccount() {
		User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		MyUser user=this.userRepository.selectByUsername(authUser.getUsername());
		List<AccountWithBank> account=this.accountRepository.selectAll(user.getId());
		if(account != null) {
			return account;
		}
		return null;
	}

	@Override
	public Account getByAccountNumber(long number) {
		Account account =this.accountRepository.selectByAccountNumber(number);
		if(account != null) {
			return account;
		}
		return null;
	}
	
	
	
	
}
