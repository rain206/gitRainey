package com.scg.util;


/**
 * Name class that records the first, last, and middle name of a Consultant
 */
import com.scg.domain.ConsultantTime;

/**
 * Utility class for the full name of a person
 * @author craigrainey
 *
 */
public final class Name implements Comparable<Name>, java.io.Serializable {


	/** Serial Version UID	 */
	private static final long serialVersionUID = 1882503499145876695L;

	/**	 String representing first name of person*/
	private String firstName = "";

	/**	 Last name of person*/
	private String lastName = "";
	
	/**	Middle name of person */
	private String middleName = "NMN";
	
	
	/**
	 * Creates new instance of Name
	 */
	public Name() {
	}
	
	
	/**
	 * Constructor for name if person only has first and last
	 * @param firstName
	 * @param lastName
	 */
	public Name(final String lastName, final String firstName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	
	/**
	 * Constructor for name if person has first, middle, and last
	 * @param firstName of person
	 * @param lastName of person
	 * @param middleName of person
	 */
	public Name(final String lastName, final String firstName, final String middleName) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.middleName = middleName;
	}
	
	
	/**
	 * @Overrides hashcode for the consultant class
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result  + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((middleName == null) ? 0 : middleName.hashCode());
		return result;
	}
	
	
	/**
	 * @Overrides equals for the Consultant's name
	 */
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof ConsultantTime)) {
			return false;
		}
		final Name other = (Name) obj;
		
		if ((this.firstName == null) ? (other.firstName != null) : !this.firstName.equals(other.firstName)) {
			return false;
		}
		if ((this.lastName == null) ? (other.lastName != null) : !this.lastName.equals(other.lastName)) {
			return false;
		}
		if ((this.middleName == null) ? (other.middleName != null) : !this.middleName.equals(other.middleName)) {
			return false;
		}
		return true;
	}

	
	/**
	 * @Overrides toString to return a string representing the person's full name
	 */
	public String toString() {
		return String.format("%s, %2$s %3$s", lastName, firstName, middleName);
	}
	
	
	/**
	 * Overrides compareTo for Name by placing in order of first name
	 * @param other
	 * @return
	 */
	@Override
	public int compareTo(Name other) {
		int compName = this.firstName.compareTo(other.firstName);
		if (compName != 0) {
			return compName;
		}
		
//		int diff;
//		diff = (lastName == null) ? ((other.lastName == null) ? 0 : 1) : lastName.compareTo(other.lastName);
		
		compName = this.lastName.compareTo(other.lastName);
		if(compName != 0) {
			return compName;
		}
		
		return this.middleName.compareTo(other.middleName);
	}
	

	/**
	 * Getters and Setters
	 */
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
}
