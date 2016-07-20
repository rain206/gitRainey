package com.scg.util;

/**
 * Address utility class that represents a mailing address. Uses immutable instances
 * @author craigrainey
 * 
 */
public final class Address implements Comparable<Address>, java.io.Serializable {

	/** Serial Version UID	 */
	private static final long serialVersionUID = -1419056971985391192L;

	/**	street number of address */
	final private String streetNumber;
	
	/**	City on address */
	final private String city;
	
	/**	State on address */
	final private StateCode state;
	
	/**	Postal code of the address */
	final private String postalCode;

	
	/**
	 * Constructor for Address class to create instances
	 */
	public Address(String streetNumber, String city, StateCode state, String postalCode) {
		this.streetNumber = streetNumber;
		this.city = city;
		this.state = state;
		this.postalCode = postalCode;
	}
	
	
	/**
	 * Overrides compareTo for Address objects
	 * @param other
	 * @return
	 */
	@Override
	public int compareTo(Address other) {
		int comp = streetNumber.compareTo(other.streetNumber);
		if (comp != 0) {
			return comp;
		}
		comp = city.compareTo(other.city);
		if (comp != 0) {
			return comp;
		}
		comp = state.compareTo(other.state);
		if (comp != 0) {
			return comp;
		}
		return postalCode.compareTo(other.postalCode);
	}

	/**
	 * @return the street number
	 */
	public String getStreetNumber() {
		return streetNumber;
	}
	
	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	
	/**
	 * 
	 * @return the state
	 */
	public StateCode getState() {
		return state;
	}
	
	/**
	 * 
	 * @return the postal code
	 */
	public String getPostalCode() {
		return postalCode;
	}
	
	/**
	 * Overrides toString to print street number city, state Postal Code
	 * @return
	 */
	@Override
	public String toString() {
		StringBuilder address = new StringBuilder();
		address.append(streetNumber+ "\n");
		address.append(city+", " +state+ " " +postalCode);
		return "";
	}
	
	/**
	 * Overrides hashCode for the address. This must be immutable since the class is immutable
	 */
	//make sure this is immutable
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result  + ((streetNumber == null) ? 0 : streetNumber.hashCode());
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result + ((postalCode == null) ? 0 : postalCode.hashCode()); 
		return result;
	}
	
	/**
	 * Overrides equals to ensure no two are the same
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof Address)) {
			return false;
		}
		final Address other = (Address) obj;
		
		if ((this.streetNumber == null) ? (other.streetNumber != null) : !this.streetNumber.equals(other.streetNumber)) {
			return false;
		}
		if ((this.city == null) ? (other.city != null) : !this.city.equals(other.city)) {
			return false;
		}
		if ((this.state == null) ? (other.state != null) : !this.state.equals(other.state)) {
			return false;
		}
		if (this.postalCode != other.postalCode) {
			return false;
		}
		return true;
	}
}
