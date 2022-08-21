package com.melda.bankingproject.services;

import com.melda.bankingproject.models.MyUser;

public interface IUserService {
	public MyUser enableDisableUser(int id,boolean enabled);
	public MyUser createUser(String username, String email, String password);
}
