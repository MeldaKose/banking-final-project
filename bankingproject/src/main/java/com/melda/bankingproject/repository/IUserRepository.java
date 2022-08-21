package com.melda.bankingproject.repository;

import org.apache.ibatis.annotations.Mapper;

import com.melda.bankingproject.models.MyUser;

@Mapper
public interface IUserRepository {
	public MyUser selectByUsername(String username);
	public MyUser selectByEmail(String email);
	public MyUser selectById(int id);
	public void enableDisableUser(MyUser user);
	public void createUser(MyUser user);
}
