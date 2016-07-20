package com.scg.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.scg.domain.Consultant;
import com.scg.util.Name;

/**
 * Junit test that test the Consultant class and its methods
 * @author craigrainey
 *
 */
public class ConsultantTest {

	/**
	 * Create some new Consultants and expected string after calling toString()
	 */
	Consultant systemAnalyst = new Consultant(new Name("Rainey", "Craig", "K."));
	Consultant projManager = new Consultant(new Name("Moul", "Russ"));
	String expectedString = String.format("%s, %s %s", "Rainey", "Craig", "K.");
	String expectedProj = String.format("%s, %s %s", "Moul", "Russ", "NMN");
	
	
	/**
	 * Test that creating a new Consultant will result in expected toString output when called
	 * This test also test the class Name.java since creating a new Consultant requires
	 * creating a new Name object
	 */
	@Test
	public void test() {
		assertEquals(systemAnalyst.toString(), expectedString);
	}
	
	
	/**
	 * Similar test but with a new Consultant without a middle name
	 */
	@Test
	public void testProj() {
		System.out.println(expectedProj);
		System.out.println(projManager.toString());
		assertEquals(projManager.toString(), expectedProj);
	}

}
