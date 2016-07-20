package com.scg.domain;

/**
 * Enum class for the Non-billable accounts
 * @author craigrainey
 */
public enum NonBillableAccount implements Account, java.io.Serializable {
	
	/** Creates enumerated values: Sick leave, Vacation, and Business Development.
	 * Spaces after Sick Leave and Vacation were added to fix formatting issues 
	 * only occurring with those two enums.
	 * */
	SICK_LEAVE("SICK_LEAVE"),
	VACATION("VACATION"),
	BUSINESS_DEVELOPMENT("BUSINESS_DEVELOPMENT");
	
	/** String representation for a nonBillable account	 */
	private final String getName;
	
	
	/**
	 * sets the reason for not being billable
	 * @param nonBillable
	 */
	private NonBillableAccount(final String nonBillable) {
		this.getName = nonBillable;
	}
	
	
	/**
	 * @return name of account
	 */
	public String getName() {
		return getName;
	}
	
	
	/**
	 * boolean stating these accounts are not billage
	 */
	public boolean isBillable() {
		return false;
	}
	
	
	/**
	 *Overrides toString for this enum class
	 */
	@Override
	public String toString() {
		return getName;
	}

}
