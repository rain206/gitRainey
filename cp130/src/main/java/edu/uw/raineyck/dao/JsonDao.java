package edu.uw.raineyck.dao;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import edu.uw.ext.framework.account.Account;
import edu.uw.ext.framework.account.AccountException;
import edu.uw.ext.framework.account.Address;
import edu.uw.ext.framework.account.CreditCard;
import edu.uw.ext.framework.dao.AccountDao;
import edu.uw.raineyck.account.AccountImpl;
import edu.uw.raineyck.account.AddressImpl;
import edu.uw.raineyck.account.CreditCardImpl;

public class JsonDao implements AccountDao {

	/** Logger */
	private static final Logger logger = LoggerFactory.getLogger(JsonDao.class);

	/** Accounts directory */
	private static final File FINAL_ACCOUNTS_DIR = new File("target/accounts");

	/** JSON serializer */
	private static final ObjectMapper mapper = new ObjectMapper();

	/** Constructor */
	public JsonDao() {
		SimpleModule module = new SimpleModule();
		module.addAbstractTypeMapping(Account.class, AccountImpl.class);
		mapper.registerModule(module);

		module.addAbstractTypeMapping(Address.class, AddressImpl.class);
		mapper.registerModule(module);

		module.addAbstractTypeMapping(CreditCard.class, CreditCardImpl.class);
		mapper.registerModule(module);
	}

	/**
	 * Retrieves and returns an account based on the account name. Will return
	 * null if no file exist.
	 * 
	 * @param accountName
	 *            = username to retrieve from files
	 * 
	 * @return target account
	 */
	@Override
	public Account getAccount(String accountName) {
		File accountFile = new File(FINAL_ACCOUNTS_DIR + File.separator + accountName + ".json");

		if (accountFile.canRead() == true) {
			try {
				// Retrieves Account from json file
				Account account = mapper.readValue(accountFile, Account.class);
				String update = String.format("Succesfully retrieved %s from json files", accountName);
				logger.info(update);

				return account;
			} catch (IOException e) {
				e.printStackTrace();
				String warn = String.format("IOException retrieving account information for %s", accountName);
				logger.warn(warn);
			}
		}
		return null;
	}

	/**
	 * Writes an account to a JSON file
	 * 
	 * @param account
	 *            to write to file
	 */
	@Override
	public void setAccount(Account account) throws AccountException {
		if (!FINAL_ACCOUNTS_DIR.mkdirs()) {
			FINAL_ACCOUNTS_DIR.mkdirs();
		}
		try {
			File accountFile = new File(FINAL_ACCOUNTS_DIR + File.separator + account.getName() + ".json");

			// Write account object to JSON file
			mapper.writeValue(accountFile, account);

			String update = String.format("Wrote %s to JSON file.", account.getName());
			logger.info(update);

		} catch (IOException e) {
			String warn = String.format("IOException writing account %s to JSON file", account.getName());
			logger.warn(warn);
		}
	}

	/**
	 * Deletes a target account file
	 * 
	 * @param accountName
	 *            is the name of the account to delete
	 */
	@Override
	public void deleteAccount(String accountName) throws AccountException {
		File[] fileList = FINAL_ACCOUNTS_DIR.listFiles();
		for (File dirFiles : fileList) {
			if (dirFiles.getName().equals(accountName + ".json")) {
				dirFiles.delete();
				logger.info("Deleted: " + dirFiles.getName());
				break;
			}
		}
	}

	/** Removes all accounts from the directory */
	@Override
	public void reset() throws AccountException {
		File[] fileList = FINAL_ACCOUNTS_DIR.listFiles();
		if (fileList != null) {
			for (File dirFile : fileList) {
				dirFile.delete();
			}
		}
		logger.info("Succesfully removed all accounts from directory");
	}

	/** Closes any remaining files that are open */
	@Override
	public void close() throws AccountException {
	}

}
