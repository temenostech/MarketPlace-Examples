/**
 * This example will create/update a customer in T24
 */
package com.temenos.marketplace.interaction;

import com.temenos.useragent.generic.DefaultInteractionSession;
import com.temenos.useragent.generic.InteractionSession;
import com.temenos.useragent.generic.mediatype.AtomPayloadHandler;

/**
 * @author mjangid
 *
 */
public class Customer {
	private String dataServiceUrl;
	private String resourceName;
	public Customer() {
		dataServiceUrl = "http://127.0.0.1:9089/Prospect-iris/Prospect.svc/GB0010001";
		resourceName = "verCustomer_Inputs";
	}
	
	public String createCustomer() {
		InteractionSession session = DefaultInteractionSession.newSession();
		String customerCode = null;
		// create a new user from T24
		session.registerHandler("application/atom+xml", AtomPayloadHandler.class)
				.basicAuth("add_valid_user", "add_valid_pwd")
				.header("Content-Type", "application/atom+xml")
				.header("Accept", "application/atom+xml")
				.url()
				.baseuri(dataServiceUrl)
				.path(resourceName + "()/new")
				.post();

		
		if (201 == session.result().code()) {
			customerCode = session.entities().item().get("CustomerCode");
			System.out.println("customerCode=" + customerCode);
			
			// Now enter the some mandatory information
			session.reuse().set("Mnemonic", "C" + customerCode)
			.set("verCustomer_Input_Name1MvGroup(0)/Name1", "Mr Ajay Devgan" + customerCode)
			.set("verCustomer_Input_ShortNameMvGroup(0)/ShortName", "Devgan" + customerCode)
			.set("Sector", "1001")
			.set("Gender", "MALE")
			.set("Title", "MR")
			.set("FamilyName", "Devgan" + customerCode)
			.entities()
			.item()
			.links()
			.byRel("http://temenostech.temenos.com/rels/input")
			.url()
			.post();
			if (201 == session.result().code()) {
				authoriseCustomer(customerCode);
			} else {		
				System.out.println("New Customer Input Failed");
			}
		} else {
			System.out.println("New Customer Creation Failed");
		}
		
		return customerCode;
	}

	void authoriseCustomer(String customerCode) {
		InteractionSession session = DefaultInteractionSession.newSession();
		session.registerHandler("application/atom+xml", AtomPayloadHandler.class)
				.basicAuth("add_valid_user", "add_valid_pwd")
				.header("Content-Type", "application/atom+xml")
				.header("Accept", "application/atom+xml")
				.url()
				.baseuri(dataServiceUrl)
				.path(resourceName + "('" + customerCode + "')")
				.get();
		
		String etag = session.header("ETag");
		
		if(200 == session.result().code()) {
			session.reuse()
				.header("If-Match", etag)
				.basicAuth("add_valid_user", "add_valid_pwd")
				.links()
				.byRel("http://temenostech.temenos.com/rels/authorise")
				.url()
				.put();
	
		}		
		if(200 == session.result().code()) {
			System.out.println("Customer Authorised: SUCCESS CODE=" + session.result().code());
		}
	}
	
	// Add and Update Address
	public void updateCustomer(String customerCode ) {
		InteractionSession session = DefaultInteractionSession.newSession();
		session.registerHandler("application/atom+xml", AtomPayloadHandler.class)
				.basicAuth("add_valid_user", "add_valid_pwd")
				.header("Content-Type", "application/atom+xml")
				.header("Accept", "application/atom+xml")
				.url()
				.baseuri(dataServiceUrl)
				.path(resourceName + "('" + customerCode + "')")
				.get();
		
		String etag = session.header("ETag");
		
		session.reuse().header("If-Match", etag)
		.set("verCustomer_Input_StreetMvGroup(0)/Street", "Mumbai Me")
		.set("verCustomer_Input_Phone1MvGroup(0)/Email1", "ajay@devgan.com")
		.set("verCustomer_Input_Phone1MvGroup(0)/Phone1", "0123456789")
		.entities()
		.item()
		.links()
		.byRel("http://temenostech.temenos.com/rels/input")
		.url()
		.post();
		if (201 == session.result().code()) {
			authoriseCustomer(customerCode);
		} else {		
			System.out.println("New Customer Input Failed");
		}
		
	}
}
