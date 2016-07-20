package com.scg.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.scg.domain.Skill;

/**
 * Junit class that test the Skill enum to make sure everything works properly
 * @author craigrainey
 *
 */
public class SkillTest {
		
	/**
	 * Test the correct friendly string is returned when a certain enum is called
	 */
	@Test
	public void testSkillType() {
		String engineer = Skill.SOFTWARE_ENGINEER.skillType();
		String projMgr = Skill.PROJECT_MANAGER.skillType();
		String arch = Skill.SYSTEM_ARCHITECT.skillType();
		String tester = Skill.SOFTWARE_TESTER.skillType();
			
		assertEquals(engineer, "Software Engineer");
		assertEquals(projMgr, "Project Manager");
		assertEquals(arch, "System Architect");
		assertEquals(tester, "Software Tester");
		
	}
	
	/**
	 * Test the correct rate is returned when each enum is called
	 */
	@Test
	public void testSkillRate() {
		int engRate = Skill.SOFTWARE_ENGINEER.getRate();
		int projRate = Skill.PROJECT_MANAGER.getRate();
		int archRate = Skill.SYSTEM_ARCHITECT.getRate();
		int testerRate = Skill.SOFTWARE_TESTER.getRate();
		
		assertEquals(engRate, 150);
		assertEquals(projRate, 250);
		assertEquals(archRate, 200);
		assertEquals(testerRate, 100);
	}

}
