package com.scg.domain;

/**
 * Enum for the skill types within the business
 * @author craigrainey
 */
public enum Skill implements java.io.Serializable {
	
	PROJECT_MANAGER("Project Manager", 250),
	SYSTEM_ARCHITECT("System Architect", 200),
	SOFTWARE_ENGINEER("Software Engineer", 150),
	SOFTWARE_TESTER("Software Tester", 100),
	UNKNOWN_SKILL("Unknown Skill", 0);
	
	/** String representing the skill set */
	private final String skillType;
	
	private final int skillRate;
	
	/**
	 * Sets the skill set of the client
	 * @param skillSet
	 */
	private Skill(String skillSet, final int rate){
		this.skillType = skillSet;
		this.skillRate = rate;
	}
	
	
	/**
	 * @return the skill type
	 */
	public String skillType(){
		return skillType;
	}
	
	/**
	 * @return a friendly string value for the Skill enum
	 */
	@Override
	public String toString() {
		return skillType;
	}
	
	public int getRate() {
		return skillRate;
	}
	
}
