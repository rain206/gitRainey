package com.scg.util;

/**
 * Enum to represent US Postal State Codes
 * @author craigrainey
 */
public enum StateCode {

	AK("ALASKA"),
	AL("ALABAMA"),
	AR("ARKANSAS"),
	AZ("ARIZONA"),
	CA("CALIFORNIA"),
	CO("COLORADO"),
	CT("CONNECTICUT"),
	DC("DISTRICT OF COLUMBIA"),
	DE("DELEWARE"),
	FL("FLORIDA"),
	FM("FEDERATED STATES OF MICRONESIA"),
	GA("GEORGIA"),
	GU("GUAM"),
	HI("HAWAII"),
	IA("IOWA"),
	ID("IDAHO"),
	IL("ILLINOIS"),
	IN("INDIANA"),
	KS("KANSAS"),
	KY("KENTUCKY"),
	LA("LOUSIANA"),
	MA("MASSACHUSETTS"),
	MD("MARYLAND"),
	ME("MAINE"),
	MH("MARSHAL ISLANDS"),
	MI("MICHIGAN"),
	MO("MISSOURI"),
	MP("NORTHER MARIANA ISLANDS"),
	MS("MISSISSIPPI"),
	MT("MONTANA"),
	NC("NORTH CAROLINA"),
	ND("NORTH DAKOTA"),
	NE("NEBRASKA"),
	NH("NEW HAMPSHIRE"),
	NJ("NEW JERSEY"),
	NM("NEW MEXICO"),
	NV("NEVADA"),
	NY("NEW YORK"),
	OH("OHIO"),
	OK("OKLAHOMA"),
	OR("OREGON"),
	PA("PENNSYLVANIA"),
	PR("PUERTO RICO"),
	PW("PALAU"),
	RI("RHODE ISLAND"),
	SC("SOUTH CAROLINA"),
	SD("SOUTH DAKOTA"),
	TN("TENNESSEE"),
	TX("TEXAS"),
	UT("UTAH"),
	VA("VRIGINIA"),
	VI("VIRGIN ISLANDS"),
	VT("VERMONT"),
	WA("WASHINGTON"),
	WI("WISCONSIN"),
	WV("WEST VIRGINIA"),
	WY("WYOMING");
	
	/** String to represent the state code	 */
	private final String state;
	
	/**
	 * @param state instance of the state code
	 */
	private StateCode(String state) {
		this.state = state;
	}
	
	/**
	 * @return the state code
	 */
	public String getStateCode() {
		return state;
	}

	
}
