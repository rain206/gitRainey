package edu.uw.raineyck.account;

import edu.uw.ext.framework.account.AccountManager;
import edu.uw.ext.framework.account.AccountManagerFactory;
import edu.uw.ext.framework.dao.AccountDao;

/**
 * Factory for the creation of account managers
 * 
 * @author craigrainey
 *
 */
public class AccountManagerFactoryImpl implements AccountManagerFactory {

	/** Constructor for the Account Manager Factory */
	public AccountManagerFactoryImpl() {
	}

	/**
	 * Instantiates a new account manager instance
	 */
	public AccountManager newAccountManager(AccountDao accountDao) {
		AccountManager newManager = new AccountManagerImpl(accountDao);
		return newManager;
	}

}
