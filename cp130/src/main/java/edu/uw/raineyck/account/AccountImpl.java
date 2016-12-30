package edu.uw.raineyck.account;

import edu.uw.ext.framework.account.Account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uw.ext.framework.account.AccountException;
import edu.uw.ext.framework.account.AccountManager;
import edu.uw.ext.framework.account.Address;
import edu.uw.ext.framework.account.CreditCard;
import edu.uw.ext.framework.order.Order;

/**
 * 
 * @author craigrainey
 *
 */
public class AccountImpl implements Account {

	/** Serial Version UID */
	private static final long serialVersionUID = 2057601288447384180L;

	/** Logger for class */
	private static final Logger logger = LoggerFactory.getLogger(AccountImpl.class);

	/** Address on account */
	private Address address;

	/** Minimum allowed initial account balance */
	private static final int MIN_ACCNT_BALANCE = 100_000;

	/** Minimum account length */
	private static final int MIN_ACCOUNT_LEN = 8;

	/** Balance of account */
	private int balance;

	/** Credit card linked to account */
	private CreditCard creditCard;

	/** Email linked to account */
	private String emailAddress;

	/** Full name */
	private String fullName;

	/** Name of account */
	private String accountName;

	/** Password hash */
	private byte[] passwordHash;

	/** Phone number */
	private String phoneNumber;

	/** Account manager responsible for persisting/managing this account */
	private AccountManager accountManager;

	/** Constructor for Account */
	public AccountImpl(String acctName, byte[] passwordHash, int balance) {
		try {
			setName(acctName);
		} catch (AccountException a) {
			logger.warn("Exception creating account: " + acctName);
		}
		setPasswordHash(passwordHash);
		setBalance(balance);
	}

	/** Empty constructor to run JSON */
	public AccountImpl() {
	}

	/** Incorporates the effect of an order in the balance */
	public void reflectOrder(Order order, int executionPrice) {
		try {
			balance += order.valueOfOrder(executionPrice);
			if (accountManager != null) {
				accountManager.persist(this);
			} else {
				logger.error("Account manager has not been initialized.");
			}
		} catch (final AccountException a) {
			logger.error("Failed to persist account after adjusting for order.");
		}
	}

	/** Sets the account manager */
	public void registerAccountManager(AccountManager setAccountManager) {
		this.accountManager = setAccountManager;
	}

	/** Getters for the class attributes */
	public Address getAddress() {
		return address;
	}

	public int getBalance() {
		return balance;
	}

	public CreditCard getCreditCard() {
		return creditCard;
	}

	public String getEmail() {
		return emailAddress;
	}

	public String getFullName() {
		return fullName;
	}

	public String getName() {
		return accountName;
	}

	public byte[] getPasswordHash() {
		return passwordHash;
	}

	public String getPhone() {
		return phoneNumber;
	}

	/** Setters for the variables */
	public void setAddress(Address setAddress) {
		address = setAddress;
	}

	/** Sets balance */
	public void setBalance(int setBalance) {
		balance = setBalance;
	}

	/** Sets account credit card */
	public void setCreditCard(CreditCard setCreditCard) {
		creditCard = setCreditCard;
	}

	/** Sets account email */
	public void setEmail(String setEmail) {
		emailAddress = setEmail;
	}

	/** Sets full name on account */
	public void setFullName(String setFullName) {
		fullName = setFullName;
	}

	/**
	 * Sets account name but throws Account Exception if name is not longer than
	 * 8 characters
	 */
	public void setName(String setName) throws AccountException {
		if (setName.length() < MIN_ACCOUNT_LEN) {
			String warn = String.format("Cannot set %s because name is too short", setName);
			logger.warn(warn);
			throw new AccountException();
		}
		this.accountName = setName;
	}

	/** Sets password on account */
	public void setPasswordHash(byte[] setPassword) {
		passwordHash = setPassword;
	}

	/** Sets the phone number on the account */
	public void setPhone(String setPhoneNumber) {
		phoneNumber = setPhoneNumber;
	}
}
