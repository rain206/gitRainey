package com.scg.domain;

public interface Account extends java.io.Serializable {
	
	/** 
	 * Getter method to getName for the account
	 */
	String getName();
	
	/**
	 * determines if method is billable or not
	 */
	boolean isBillable();

}
