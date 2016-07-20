package edu.uw.raineyck.dao;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uw.ext.framework.account.Account;
import edu.uw.ext.framework.account.AccountException;
import edu.uw.ext.framework.account.Address;
import edu.uw.ext.framework.account.CreditCard;
import edu.uw.ext.framework.dao.AccountDao;

/**
 * Defines methods needed to store and load accounts from a persistent storage
 * mechanism
 * 
 * @author craigrainey
 *
 */
public class AccountDaoImpl implements AccountDao {

	/** Accounts directory */
	private static final File FINAL_ACCOUNTS_DIR = new File("target/accounts");

	/** Credit card directory */
	private static final File CREDIT_CARDS_DIR = new File("creditcard.bin");

	/** Address directory */
	private static final File ADDRESS_DIR = new File("address.bin");

	/** File output stream */
	private FileOutputStream fileOut;

	/** Data output stream */
	private DataOutputStream dos;

	/** File input stream */
	private FileInputStream fileIn;

	/** Data input stream */
	private DataInputStream inputStream;

	/** Acount Ser to read and write account information to file */
	private static final AccountSer accountSer = new AccountSer();

	/** Address Ser to read and write address information to file */
	private static final AddressSer addressSer = new AddressSer();

	/** Credit Card Ser to read and write credit card information to file */
	private static final CreditCardSer creditSer = new CreditCardSer();

	/** Logger */
	private static final Logger log = LoggerFactory.getLogger(AccountDaoImpl.class);

	/** Constructor */
	public AccountDaoImpl() {
	}

	/**
	 * Closes the DAO
	 */
	public void close() throws AccountException {
		// this.close();
	}

	/**
	 * Removes a specific account
	 */
	public void deleteAccount(String accountName) throws AccountException {
		File[] fileList = FINAL_ACCOUNTS_DIR.listFiles();
		for (File dirFiles : fileList) {
			if (dirFiles.getName().equals(accountName)) {
				File[] acctFiles = dirFiles.listFiles();
				for (File files : acctFiles) {
					files.delete();
				}
			}
			dirFiles.delete();
		}
	}

	/**
	 * Lookup an account based on account name from file directory.
	 */
	public Account getAccount(String accountName) {
		File accountFile = new File(FINAL_ACCOUNTS_DIR + "/" + accountName);

		if (accountFile.canRead() == true) {
			try {
				fileIn = new FileInputStream(accountFile + "/account.bin");
				inputStream = new DataInputStream(fileIn);

				Account tempAccount = accountSer.readAccountFromBinary(inputStream, accountName);

				fileIn = new FileInputStream(accountFile + "/address.bin");
				inputStream = new DataInputStream(fileIn);

				Address accountAddress = addressSer.readAddressFromBinary(accountName, inputStream);
				tempAccount.setAddress(accountAddress);

				fileIn = new FileInputStream(accountFile + "/creditcard.bin");
				inputStream = new DataInputStream(fileIn);

				CreditCard acctCard = creditSer.readCreditCardFromBinary(accountName, inputStream);
				tempAccount.setCreditCard(acctCard);

				String retrieve = String.format("Successfully retrieved account %s from file", tempAccount.getName());
				log.info(retrieve);

				fileIn.close();
				inputStream.close();
				return tempAccount;

			} catch (IOException e) {
				// String warn = String.format("IO Exception trying to retrieve
				// account information for %s", accountName+ "Account.bin");
				// log.warn(warn);
			}
		}
		return null;
	}

	/**
	 * Remove all accounts from the directory
	 */
	public void reset() throws AccountException {
		File[] fileList = FINAL_ACCOUNTS_DIR.listFiles();
		for (File dirFile : fileList) {
			File[] dirFileList = dirFile.listFiles();
			for (File acctFile : dirFileList) {
				acctFile.delete();
			}
		}
		log.info("Succesfully reset");
	}

	/**
	 * Adds or updates an account
	 */
	public void setAccount(Account account) throws AccountException {
		try {

			if (!FINAL_ACCOUNTS_DIR.mkdirs()) {
				FINAL_ACCOUNTS_DIR.mkdirs();
			}

			File accountDirectory = new File(FINAL_ACCOUNTS_DIR + "/" + account.getName());

			if (!accountDirectory.mkdirs()) {
				accountDirectory.mkdirs();
			}

			fileOut = new FileOutputStream(new File(accountDirectory + "/account.bin"));
			dos = new DataOutputStream(fileOut);
			accountSer.writeAccountToBinary(account, dos);

			if (!ADDRESS_DIR.mkdirs()) {
				ADDRESS_DIR.mkdirs();
			}
			dos = new DataOutputStream(new FileOutputStream(accountDirectory + "/address.bin"));
			addressSer.writeAddressToBinary(account, dos);

			if (!CREDIT_CARDS_DIR.mkdirs()) {
				CREDIT_CARDS_DIR.mkdirs();
			}
			File creditFile = new File(accountDirectory + "/creditcard.bin");
			dos = new DataOutputStream(new FileOutputStream(creditFile));
			creditSer.writeCreditCardToBinary(account.getCreditCard(), dos);

			String sent = String.format("Successfully sent %s account information to file", account.getName());
			log.info(sent);

			dos.flush();
			fileOut.close();
			dos.close();

		} catch (IOException i) {
			log.warn("IO Exception while setting account into directory");
			throw new AccountException();
		}
	}
}
