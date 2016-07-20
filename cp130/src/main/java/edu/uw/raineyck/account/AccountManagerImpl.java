package edu.uw.raineyck.account;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;

import edu.uw.ext.framework.account.Account;
import edu.uw.ext.framework.account.AccountException;
import edu.uw.ext.framework.account.AccountManager;
import edu.uw.ext.framework.account.AccountManagerFactory;
import edu.uw.ext.framework.dao.AccountDao;

/**
 * Manager of accounts - creates, deletes, authentication, and persistence
 * 
 * @author craigrainey
 *
 */
public class AccountManagerImpl implements AccountManager, AccountManagerFactory {

	/** Account Manager logger */
	private static final org.slf4j.Logger log = LoggerFactory.getLogger(AccountManagerImpl.class);

	private List<String> listOfAccounts = new ArrayList<String>();

	/** Account DAO */
	private AccountDao dao;

	/** Algorithm for password hashing */
	private static final String ALGORITHM = "SHA-1";

	public AccountManagerImpl(AccountDao accountDao) {
		this.dao = accountDao;
	}

	/**
	 * Instantiates a new account manager instance
	 */
	public AccountManager newAccountManager(AccountDao accountDao) {
		return new AccountManagerImpl(accountDao);
	}

	/**
	 * Releases any resources used by the AccountManager implementation
	 */
	public void close() throws AccountException {
		dao.close();
		dao = null;
	}

	/**
	 * Creates an account if one with the same name does not already exist
	 */
	public Account createAccount(String accountName, String password, int balance) throws AccountException {

		if (getAccount(accountName) == null) {
			try {
				byte[] passwordHash = hashPassword(password);

				Account newAccount = new AccountImpl(accountName, passwordHash, balance);

				listOfAccounts.add(accountName);
				log.info("Created account for: " + accountName);
				persist(newAccount);
				return newAccount;
			} catch (AccountException a) {
				String warn = String.format("Account exception creating %s", accountName);
				log.warn(warn);
			} catch (UnsupportedEncodingException u) {
				u.printStackTrace();
			}
		} else if (getAccount(accountName) != null) {
			log.info("Exception creating account: account name already exist");
		}
		return null;
	}

	/**
	 * Remove tan account with the DAO.
	 */
	public void deleteAccount(String accountName) throws AccountException {
		listOfAccounts.remove(accountName);
		dao.deleteAccount(accountName);
	}

	/**
	 * Lookup an account based on account name from the DAO
	 */
	public Account getAccount(String accountName) throws AccountException {
		return dao.getAccount(accountName);
	}

	/**
	 * Used to persist an account
	 */
	public void persist(Account account) throws AccountException {
		dao.setAccount(account);
	}

	/**
	 * Checks whether a login is valid
	 */
	public boolean validateLogin(String accountName, String password) throws AccountException {
		try {
			Account tempAccount = getAccount(accountName);
			if (tempAccount != null) {

				byte[] checkPass = hashPassword(password);

				String acctPassword = validatePassword(tempAccount.getPasswordHash());

				String inputedPassword = validatePassword(checkPass);

				if (acctPassword.equals(inputedPassword)) {
					log.info("Validation success: login information is valid.");
					return true;
				}
			}
		} catch (UnsupportedEncodingException u) {
			u.printStackTrace();
		}
		log.info("Validation failed: login username or password is invalid.");
		return false;
	}

	/**
	 * Hashes the password
	 * 
	 * @param password
	 *            String
	 * @return byte array
	 * @throws AccountException
	 * @throws UnsupportedEncodingException
	 */
	private byte[] hashPassword(String password) throws AccountException, UnsupportedEncodingException {
		try {
			MessageDigest md = MessageDigest.getInstance(ALGORITHM);
			md.reset();
			md.update(password.getBytes());
			byte[] passwordHash = md.digest();

			return passwordHash;
		} catch (NoSuchAlgorithmException e) {
			throw new AccountException();
		}
	}

	/**
	 * Converts password hash to a hexidecimal string that is readable
	 * 
	 * @param hash
	 *            password hash
	 * @return hexidecimal representation of password hash
	 */
	private String validatePassword(byte[] hash) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < hash.length; i++) {
			sb.append(Integer.toString((hash[i] & 0xff) + 0x100, 16).substring(1));
		}
		return sb.toString();
	}

}
