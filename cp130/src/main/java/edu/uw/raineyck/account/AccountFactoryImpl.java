package edu.uw.raineyck.account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uw.ext.framework.account.Account;
import edu.uw.ext.framework.account.AccountFactory;

/**
 * Account Factory Implementation which creates a new account and moderates if a
 * new account is created or not.
 * 
 * @author craigrainey
 *
 */
public class AccountFactoryImpl implements AccountFactory {

	/** Minimum allowed initial account balance */
	private static final int MIN_ACCNT_BALANCE = 100000;

	/** Minimum account length */
	private static final int MIN_ACCOUNT_LEN = 8;

	/** Class logger */
	private static final Logger log = LoggerFactory.getLogger(AccountFactoryImpl.class);

	/** Warning string */
	private String warn;

	/** Constructor */
	public AccountFactoryImpl() {
	}

	/**
	 * Creates and returns a new account when called. Will not create an account
	 * if the name is not long enough or initial balance is not large enough.
	 */
	@Override
	public Account newAccount(String accountName, byte[] hashedPassword, int initialBalance) {
		if (accountName.length() < MIN_ACCOUNT_LEN || initialBalance < MIN_ACCNT_BALANCE) {
			if (accountName.length() < MIN_ACCOUNT_LEN) {
				warn = String.format("Account name %s is too short. Accounts names must exceed 8 characters",
						accountName);
				log.warn(warn);
			} else {
				warn = String.format(
						"Account balance of %d for %s is too low. Initial balances must exceed 100000 (in cents)",
						initialBalance, accountName);
				log.warn(warn);
			}
			return null;
		}
		Account newAcct = new AccountImpl(accountName, hashedPassword, initialBalance);
		return newAcct;
	}

}
