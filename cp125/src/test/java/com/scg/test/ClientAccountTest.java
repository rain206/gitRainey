package com.scg.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.scg.domain.ClientAccount;
import com.scg.util.Address;
import com.scg.util.Name;
import com.scg.util.StateCode;

/**
 * Junit test that test the various methods of the ClientAccount class while also
 * testing Name class simultaneously
 * @author craigrainey
 *
 */
public class ClientAccountTest {

	/**
	 * Create a couple new accounts to test
	 */
	Address testAddress = new Address("1313 DisneyLand Dr", "Anaheim", StateCode.CA, "92802");
	ClientAccount microsoft = new ClientAccount("Microsoft", new Name("Rainey", "Craig"), testAddress);
	ClientAccount google = new ClientAccount("Google", new Name("Pichai", "Sundar"), testAddress);
	String microsoftExp = "Microsoft";
	String googleExp = "Sundar";
	
	
	/**
	 * Test the getName method of ClientAccount to assure the object was built correctly
	 */
	@Test
	public void testGetName() {
		assertEquals(google.getContact().getFirstName(), googleExp);
		assertEquals(microsoft.getName(), microsoftExp);
	}
	
	/**
	 * test that a new ClientAccount method creates the correct Name object by checking
	 * the contact's first name
	 */
	@Test
	public void testGetAddress() {
		assertEquals(google.getAddress(), testAddress);
		assertEquals(microsoft.getAddress(), testAddress);
	}
	
	/**
	 * Test isBillable method when creatinga  new ClientAccount object
	 */
	@Test
	public void testIsBillable() {
		assertEquals(google.isBillable(), true);
		assertEquals(microsoft.isBillable(), true);
	}

}
