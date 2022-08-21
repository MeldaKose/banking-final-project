package com.melda.bankingproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.melda.bankingproject.models.Bank;
import com.melda.bankingproject.requests.BankCreateRequest;
import com.melda.bankingproject.responses.BankCreateErrorResponse;
import com.melda.bankingproject.responses.BankCreateSuccessResponse;
import com.melda.bankingproject.services.IBankService;

@RestController
public class BankController {
	
	@Autowired
	private IBankService bankService;
	
	@PostMapping(path="/banks")
	public ResponseEntity<?> createBank(@RequestBody BankCreateRequest request){
		Bank bank=this.bankService.createBank(request.getName());
		if(bank!=null) {
			BankCreateSuccessResponse resp= new BankCreateSuccessResponse();
			resp.setSuccess(true);
			resp.setMessage("Created Successfully");
			resp.setBank(bank);
			return ResponseEntity.ok().body(resp);
		}else {
			BankCreateErrorResponse resp= new BankCreateErrorResponse();
			resp.setSuccess(false);
			resp.setMessage("Given name Already Used : "+request.getName());
			return ResponseEntity.unprocessableEntity().body(resp);
		}
	}
	
	
}
