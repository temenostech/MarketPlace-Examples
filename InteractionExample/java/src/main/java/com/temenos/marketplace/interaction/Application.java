package com.temenos.marketplace.interaction;

public class Application {

	public static void main(String[] args) {
		Customer customer = new Customer();
		String customerCode = customer.createCustomer();
		
		customer.updateCustomer(customerCode);
	}

}
