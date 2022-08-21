package com.melda.bankingproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.melda.bankingproject.models.MyUser;
import com.melda.bankingproject.requests.LoginRequest;
import com.melda.bankingproject.requests.RegisterRequest;
import com.melda.bankingproject.responses.LoginSuccessResponse;
import com.melda.bankingproject.responses.RegisterErrorResponse;
import com.melda.bankingproject.responses.RegisterSuccessResponse;
import com.melda.bankingproject.security.JWTTokenUtil;
import com.melda.bankingproject.services.UserService;

@RestController
public class AuthController {
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JWTTokenUtil jwtTokenUtil;

	@Autowired
	private UserService userService;

	@PostMapping("/auth")
	@CrossOrigin(origins="http://localhost:4200")
	public ResponseEntity<?> login(@RequestBody LoginRequest request) {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
		} catch (BadCredentialsException e) {
			return ResponseEntity.badRequest().body("Bad credentials");
		} catch (DisabledException e) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
		final UserDetails userDetails = userService.loadUserByUsername(request.getUsername());
		final String token = jwtTokenUtil.generateToken(userDetails);
		LoginSuccessResponse resp = new LoginSuccessResponse();
		resp.setStatus("success");
		resp.setMessage("Logged-In Successfully");
		resp.setToken(token);
		return ResponseEntity.ok().body(resp);
	}
	
	
	@PostMapping("/register")
	@CrossOrigin(origins="http://localhost:4200")
	public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
		MyUser user=this.userService.createUser(request.getUsername(), request.getEmail(), request.getPassword());
		if(user != null) {
			RegisterSuccessResponse resp= new RegisterSuccessResponse();
			resp.setSuccess(true);
			resp.setMessage("Created Successfully");
			resp.setUser(user);
			return ResponseEntity.ok().body(resp);
		}
		RegisterErrorResponse resp= new RegisterErrorResponse();
		resp.setSuccess(false);
		resp.setMessage("Given username or email already Used : "+ request.getUsername()+" or "+request.getEmail());
		return ResponseEntity.unprocessableEntity().body(resp);
	}
}
