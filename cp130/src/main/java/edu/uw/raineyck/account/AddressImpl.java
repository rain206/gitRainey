package edu.uw.raineyck.account;

import edu.uw.ext.framework.account.Address;

/**
 * Address implementation to hold an accounts address information.
 * 
 * @author craigrainey
 *
 */
public class AddressImpl implements Address {

	/** Serial Version UID */
	private static final long serialVersionUID = -9216048784568051928L;

	/** City */
	private String city;

	/** State */
	private String state;

	/** Street address */
	private String streetAddress;

	/** Zip code */
	private String zipCode;

	/** Constructor for the Address Implementation */
	public AddressImpl() {
	}

	/**
	 * Getters for the instance variables
	 */
	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

	public String getStreetAddress() {
		return streetAddress;
	}

	public String getZipCode() {
		return zipCode;
	}

	/**
	 * Setters for the instance variables
	 */
	public void setCity(String setCity) {
		city = setCity;
	}

	public void setState(String setState) {
		state = setState;
	}

	public void setStreetAddress(String setStreetAddress) {
		streetAddress = setStreetAddress;
	}

	public void setZipCode(String setZipCode) {
		zipCode = setZipCode;
	}

}
