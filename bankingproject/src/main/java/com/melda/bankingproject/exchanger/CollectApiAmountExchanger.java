package com.melda.bankingproject.exchanger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

@Component
public class CollectApiAmountExchanger implements IAmountExchanger{

	@Autowired
	private RestTemplate client;

	@Override
	public double exchangeAmount(double amount, String toType, String baseType) {
		Double result=0.0;
		if(toType.equals("GAU") || baseType.equals("GAU")) {
			if(toType.equals("USD")) {
				Double toTry=this.exchangeMoney(amount, "USD", "TRY");
				result=this.goldPrice(toTry,"TRY", "GAU");
			}
			else if(baseType.equals("USD")) {
				Double toTry=this.goldPrice(amount,"GAU", "TRY");
				result=this.exchangeMoney(toTry, "TRY", "USD");
			}else {
				result=this.goldPrice(amount, toType, baseType);
			}
		}
		else {
			result=this.exchangeMoney(amount, toType, baseType);
		}
		return result;
	}

	@Override
	public double exchangeMoney(double amount, String toType, String baseType) {
		HttpHeaders headers=new HttpHeaders();
		Double result=0.0;
		headers.add("content-type", "application/json");
		headers.add("authorization", "apikey 7sHFLxhU6cFxmwpYGQlZLs:45NkMwNSKgg5ha8N1131Qj");
		String url="https://api.collectapi.com/economy/exchange?int="+amount+"&to="+toType+"&base="+baseType;
		HttpEntity<?> requestEntity=new HttpEntity<>(headers);
		ResponseEntity<String> response=client.exchange(url, HttpMethod.GET,requestEntity,String.class);
		String res=response.getBody();
		ObjectMapper objectMapper=new ObjectMapper();
		JsonNode node;
		try {
			node = objectMapper.readTree(res);
			JsonNode resultNode=node.get("result");
			ArrayNode data=(ArrayNode) resultNode.get("data");
			JsonNode singledata=data.get(0);
			result=singledata.get("calculated").asDouble();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public double goldPrice(double amount,String toType, String baseType) {
		HttpHeaders headers=new HttpHeaders();
		Double result=0.0;
		headers.add("content-type", "application/json");
		headers.add("authorization", "apikey 7sHFLxhU6cFxmwpYGQlZLs:45NkMwNSKgg5ha8N1131Qj");
		String url="https://api.collectapi.com/economy/goldPrice";
		HttpEntity<?> requestEntity=new HttpEntity<>(headers);
		ResponseEntity<String> response=client.exchange(url, HttpMethod.GET,requestEntity,String.class);
		String res=response.getBody();
		ObjectMapper objectMapper=new ObjectMapper();
		JsonNode node;
		try {
			node = objectMapper.readTree(res);
			JsonNode resultNode=node.get("result");
			if(resultNode.isArray()) {
				ArrayNode goldNode=(ArrayNode) resultNode;
				for(int i=0;i<goldNode.size();i++) {
					JsonNode singlegold=goldNode.get(i);
					if(singlegold.get("name").toString().equals("\"Gram Altın\"")) {
						Double buy=singlegold.get("buyingstr").asDouble();
						Double sell=singlegold.get("sellingstr").asDouble();
						if(toType=="GAU") {
							result=amount/sell;
							return result;
						}else {
							result=amount*buy;
							return result;
						}
					}
				}
			}
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public double getEft(String baseType) {
		double eft=0.0;
		if(baseType.equals("TRY")) {
			eft=3.0;
		}
		if(baseType.equals("USD")) {
			eft=1.0;
		}
		return eft;
	}

}
