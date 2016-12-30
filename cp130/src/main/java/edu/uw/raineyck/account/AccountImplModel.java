package edu.uw.raineyck.account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uw.ext.framework.account.Account;
import edu.uw.ext.framework.account.AccountException;
import edu.uw.ext.framework.account.AccountManager;
import edu.uw.ext.framework.account.Address;
import edu.uw.ext.framework.account.CreditCard;
import edu.uw.ext.framework.order.Order;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Account Implementation class that sets and gets all account information
 * including the address, credit card, and other important information. This
 * class also moderates new accounts being open by requiring a minimum of 8
 * character account name and beginning balance of $1,000 (in cents).
 * This class is designed to be used for the view.
 * 
 * @author craigrainey
 *
 */
public class AccountImplModel implements Account {
	
	/**	Serial Version UID */
	private static final long serialVersionUID = 1L;

	/**	Logger for class */
	private static final Logger logger = LoggerFactory.getLogger(AccountImplModel.class);
	
	/**	Address on account */
	private ObjectProperty<Address> address;
	
	/**	Minimum allowed initial account balance */
	private static final int MIN_ACCT_BALANCE = 100_000;

	/**	Minimum account length */
	private static final int MIN_ACCOUNT_LEN = 8;
	
	/**	Balance of account */
	private final IntegerProperty balance;
	
	/**	Credit card linked to account */
	private final ObjectProperty<CreditCard> creditCard;
	
	/**	Email linked to account */
	private final StringProperty emailAddress;
	
	/**	Full name on account */
	private final StringProperty fullName;
	
	/**	Name of account */
	private final StringProperty accountName;
	
	/**	Password hash */
	private byte[] passwordHash;
	
	/**	Phone number on account */
	private StringProperty phoneNumber;
	
	/**	Account manager responsible for persisting/managing this account */
	private final ObjectProperty<AccountManager> accountManager;
	
	
	
	/**
	 * Constructor for the AccountImplModel
	 * @param acctName - name of account
	 * @param password - password for account
	 * @param balance - balance of account
	 */
	public AccountImplModel(String acctName, byte[] password, int balance) {
		this.accountName = new SimpleStringProperty(acctName);
		this.passwordHash = password;
		this.balance = new SimpleIntegerProperty(balance);
		
		//Initial dummy data for testing
		this.emailAddress = new SimpleStringProperty("rainey.ck@gmail.com");
		this.fullName = new SimpleStringProperty("Craig Rainey");
		this.phoneNumber = new SimpleStringProperty("2536532676");
		this.address = new SimpleObjectProperty<Address>();
		this.creditCard = new SimpleObjectProperty<CreditCard>();
		this.accountManager = new SimpleObjectProperty<AccountManager>();
	}
	
	
	/**
	 * Adjust the balance based on a processed buy or sell order/
	 * 
	 * @param order - the order that was executed
	 * @param executionPrice - price of the order.
	 */
	public void reflectOrder(Order order, int executionPrice) {
		try {
			balance.add(order.valueOfOrder(executionPrice));
			if (accountManager != null) {
				accountManager.get().persist(this);
			} else {
				logger.error("Account manager has not been initialized.");
			}
		} catch (final AccountException a) {
			logger.error("Failed to persist account after adjusting for order.");
		}
	}
	
	
	/**
	 * Getters and setters for the properties
	 * @return
	 */
	public String getFullName() {
		return fullName.get();
	}
	public StringProperty fullNameProperty() {
		return fullName;
	}
	public void setFullName(String newFullName) {
		this.fullName.set(newFullName);
	}
	
	public Address getAddress() {
		return address.get();
	}
	public ObjectProperty<Address> addressProperty() {
		return address;
	}
	public void setAddress(Address newAddress) {
		this.address.set(newAddress);
	}
	
	public int getBalance() {
		return balance.get();
	}
	public IntegerProperty balanceProperty() {
		return balance;
	}
	public void setBalanace(int balance) {
		this.balance.set(balance);
	}
	
	public CreditCard getCreditCard() {
		return creditCard.get();
	}
	public ObjectProperty<CreditCard> creditCardProperty() {
		return creditCard;
	}
	public void setCreditCard(CreditCard newCard) {
		creditCard.set(newCard);
	}
	
	public String getEmail() {
		return emailAddress.get();
	}
	public StringProperty emailProperty() {
		return emailAddress;
	}
	public void setEmail(String email) {
		emailAddress.set(email);
	}
	
	public String getAcctName() {
		return accountName.get();
	}
	public StringProperty accountNameProperty() {
		return accountName;
	}
	public void setName(String newAcctName) throws AccountException {
		if (newAcctName.length() < MIN_ACCOUNT_LEN) {
			String warn = String.format("Cannot set %s because name is too short", newAcctName);
			logger.warn(warn);
			throw new AccountException();
		}
		accountName.set(newAcctName);
	}
	
	public byte[] getPasswordHash() {
		return passwordHash;
	}
	public void setPasswordHash(byte[] newPassword) {
		passwordHash = newPassword;
	}
	
	public String getPhone() {
		return phoneNumber.get();
	}
	public StringProperty phoneNumberProperty() {
		return phoneNumber;
	}
	public void setPhone(String newNumber) {
		phoneNumber.set(newNumber);
	}

	@Override
	public String getName() {
		return accountName.get();
	}

	@Override
	public void registerAccountManager(AccountManager setAccountManager) {
		this.accountManager.set((AccountManagerImpl) setAccountManager);
	}

	@Override
	public void setBalance(int balance) {
		this.balance.set(balance);
	}
}
