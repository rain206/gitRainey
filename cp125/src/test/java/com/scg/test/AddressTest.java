package com.scg.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.scg.util.Address;
import com.scg.util.StateCode;

/**
 * Junit test to test address class
 * @author craigrainey
 *
 */
public class AddressTest {

	/** Creates test Address	 */
	Address testAddress = new Address("1313 Disneyland Dr", "Anaheim", StateCode.CA, "92802");
	
	/**
	 * Test instances of address are correct
	 */
	@Test
	public void test() {
		assertEquals(testAddress.getCity(), "Anaheim");
		assertEquals(testAddress.getPostalCode(), "92802");
		assertEquals(testAddress.getState(), StateCode.CA);
		assertEquals(testAddress.getStreetNumber(), "1313 Disneyland Dr");
	}

}
