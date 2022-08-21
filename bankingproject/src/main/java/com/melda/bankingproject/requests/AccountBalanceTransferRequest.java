package com.melda.bankingproject.requests;

public class AccountBalanceTransferRequest {
	private double amount;
	private int receiverAccountId;
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public int getReceiverAccountId() {
		return receiverAccountId;
	}
	public void setReceiverAccountId(int receiverAccountId) {
		this.receiverAccountId = receiverAccountId;
	}
	
	
}
