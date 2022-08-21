package com.melda.bankingproject.repository;

import org.apache.ibatis.annotations.Mapper;

import com.melda.bankingproject.models.Bank;

@Mapper
public interface IBankRepository {
	public void createBank(String name);
	public Bank selectByName(String name);
}
