package com.melda.bankingproject.exchanger;

public interface IAmountExchanger {
	public double exchangeAmount(double amount,String toType,String baseType);
	public double exchangeMoney(double amount,String toType,String baseType);
	public double goldPrice(double amount,String toType,String baseType);
	public double getEft(String baseType);
}
