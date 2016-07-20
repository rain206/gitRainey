package edu.uw.raineyck.account;

import edu.uw.ext.framework.account.CreditCard;

/**
 * Credit Card implentation to hold an account's credit card information.
 * 
 * @author craigrainey
 *
 */
public class CreditCardImpl implements CreditCard {

	/** Serial Version UID */
	private static final long serialVersionUID = 580093643926088291L;

	/** Account number */
	private String accountNumber;

	/** Expiration date */
	private String expirationDate;

	/** Holder */
	private String holder;

	/** Issuer of card */
	private String issuer;

	/** Type of card */
	private String type;

	/** Constructor for Credit Card Implementation */
	public CreditCardImpl() {
	}

	/**
	 * Getters for the instance variables
	 */
	public String getAccountNumber() {
		return accountNumber;
	}

	public String getExpirationDate() {
		return expirationDate;
	}

	public String getHolder() {
		return holder;
	}

	public String getIssuer() {
		return issuer;
	}

	public String getType() {
		return type;
	}

	/**
	 * Setters for the instance variables
	 */
	public void setAccountNumber(String setAccountNumber) {
		accountNumber = setAccountNumber;
	}

	public void setExpirationDate(String setExpDate) {
		expirationDate = setExpDate;
	}

	public void setHolder(String setHolder) {
		holder = setHolder;
	}

	public void setIssuer(String setIssuer) {
		issuer = setIssuer;
	}

	public void setType(String setType) {
		type = setType;
	}

}
