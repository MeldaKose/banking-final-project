package com.melda.bankingproject.responses;

import com.melda.bankingproject.models.MyUser;

public class RegisterSuccessResponse {
	private boolean success;
	private String message;
	private MyUser user;
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public MyUser getUser() {
		return user;
	}
	public void setUser(MyUser user) {
		this.user = user;
	}
}
