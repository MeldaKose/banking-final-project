package com.melda.bankingproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.melda.bankingproject.models.MyUser;
import com.melda.bankingproject.requests.EnableDisableUserRequest;
import com.melda.bankingproject.responses.EnableDisableUserResponse;
import com.melda.bankingproject.services.UserService;

@RestController
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@PatchMapping(path="users/{id}")
	public ResponseEntity<?> enableDisableUser(@PathVariable int id,@RequestBody EnableDisableUserRequest request){
		MyUser user=this.userService.enableDisableUser(id,request.isEnabled());
		if(user!=null) {
			EnableDisableUserResponse resp=new EnableDisableUserResponse();
			resp.setStatus("success");
			if(request.isEnabled()==true) {
				resp.setMessage("User enabled");
			}else {
				resp.setMessage("User disabled");
			}
			return ResponseEntity.ok().body(resp);
		}
		return ResponseEntity.notFound().build();
	}

}
