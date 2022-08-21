package com.melda.bankingproject.requests;

public class AccountCreateRequest {
	private int bank_id;
	private String type;
	public int getBank_id() {
		return bank_id;
	}
	public void setBank_id(int bank_id) {
		this.bank_id = bank_id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
