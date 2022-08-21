package com.melda.bankingproject.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import com.melda.bankingproject.responses.AccountDeleteSuccessResponse;
import com.melda.bankingproject.responses.AccountDepositSuccessResponse;
import com.melda.bankingproject.exceptions.AccessDeniedException;
import com.melda.bankingproject.exceptions.InsufficientBalanceException;
import com.melda.bankingproject.models.Account;
import com.melda.bankingproject.models.AccountWithBank;
import com.melda.bankingproject.requests.AccountBalanceDepositRequest;
import com.melda.bankingproject.requests.AccountBalanceTransferRequest;
import com.melda.bankingproject.requests.AccountCreateRequest;
import com.melda.bankingproject.responses.AccountCreateSuccessResponse;
import com.melda.bankingproject.responses.AccountInvalidTypeResponse;
import com.melda.bankingproject.responses.AccountTransferSuccessResponse;
import com.melda.bankingproject.services.IAccountService;

@RestController
public class AccountController {

	@Autowired
	private KafkaTemplate<String, String> producer;

	@Autowired
	private IAccountService accountService;

	@PostMapping(path = "/accounts")
	public ResponseEntity<?> createAccount(@RequestBody AccountCreateRequest request) {
		Account account = this.accountService.createAccount(request.getBank_id(), request.getType());
		if (account != null) {
			AccountCreateSuccessResponse resp = new AccountCreateSuccessResponse();
			resp.setSuccess(true);
			resp.setMessage("Account Created");
			resp.setAccount(account);
			return ResponseEntity.ok().body(resp);
		} else {
			AccountInvalidTypeResponse resp = new AccountInvalidTypeResponse();
			resp.setSuccess(false);
			resp.setMessage("Invalid Account Type : " + request.getType());
			return ResponseEntity.badRequest().body(resp);
		}
	}

	@GetMapping(path = "/accounts/{id}")
	public ResponseEntity<?> accountDetail(@PathVariable int id, WebRequest request) throws IOException {
		try {
			Account detail = this.accountService.accountDetail(id);
			if (request.checkNotModified(detail.getLast_update_date().getTime())) {
				return null;
			}
			return ResponseEntity.ok().lastModified(detail.getLast_update_date().getTime()).body(detail);
		} catch (AccessDeniedException exc) {
			return new ResponseEntity<>(exc.getMessage(), HttpStatus.FORBIDDEN);
		}
	}

	@DeleteMapping("/accounts/{id}")
	public ResponseEntity<?> deleteAccount(@PathVariable int id) {
		boolean isDeleted = this.accountService.removeAccount(id);
		if (isDeleted == true) {
			AccountDeleteSuccessResponse resp = new AccountDeleteSuccessResponse();
			resp.setSuccess(true);
			resp.setMessage("Deleted Successfully");
			return ResponseEntity.ok().body(resp);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PatchMapping(path = "/accounts/{id}")
	public ResponseEntity<?> deposit(@PathVariable int id, @RequestBody AccountBalanceDepositRequest request) {
		try {
			Account account = this.accountService.deposit(id, request.getAmount());
			AccountDepositSuccessResponse resp = new AccountDepositSuccessResponse();
			resp.setSuccess(true);
			resp.setMessage("Deposit Successfully");
			resp.setBalance(account.getBalance());
			String message = account.getNumber() + ", " + request.getAmount() + " " + account.getType() + " deposited";
			producer.send("logs", message);
			return ResponseEntity.ok().body(resp);
		} catch (AccessDeniedException exc) {
			return new ResponseEntity<>(exc.getMessage(), HttpStatus.FORBIDDEN);
		}
	}

	@PutMapping(path = "/accounts/{senderAccountId}")
	@CrossOrigin(origins="http://localhost:4200")
	public ResponseEntity<?> transfer(@PathVariable int senderAccountId,
			@RequestBody AccountBalanceTransferRequest request) {
		try {
			String info = this.accountService.transfer(senderAccountId, request.getReceiverAccountId(),
					request.getAmount());
			AccountTransferSuccessResponse resp = new AccountTransferSuccessResponse();
			resp.setMessage("Transfered Successfully");
			producer.send("logs", info);
			return ResponseEntity.ok().body(resp);
		} catch (InsufficientBalanceException exc) {
			return ResponseEntity.badRequest().body(exc.getMessage());
		} catch (AccessDeniedException exc) {
			return new ResponseEntity<>(exc.getMessage(), HttpStatus.FORBIDDEN);
		}
	}

	@GetMapping(path = "/accounts")
	@CrossOrigin(origins="http://localhost:4200")
	public ResponseEntity<?> getAllAccount() {
		List<AccountWithBank> account = this.accountService.getAllAccount();
		if(account != null) {
			return ResponseEntity.ok().body(account);
		}
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping(path = "/accounts/get/{number}")
	@CrossOrigin(origins="http://localhost:4200")
	public ResponseEntity<?> getByAccountnumber(@PathVariable long number) {
		Account account = this.accountService.getByAccountNumber(number);
		if(account != null) {
			return ResponseEntity.ok().body(account);
		}
		return ResponseEntity.notFound().build();
	}

}
