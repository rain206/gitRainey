package com.scg.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.scg.util.StateCode;

/**
 * Class to test StateCode
 * @author craigrainey
 *
 */
public class StateCodeTest {
	
	StateCode test = StateCode.CA;
	StateCode test2 = StateCode.AL;
	
	@Test
	public void test() {
		assertEquals(test, StateCode.CA);
		assertEquals(test2, StateCode.AL);
	}

}
