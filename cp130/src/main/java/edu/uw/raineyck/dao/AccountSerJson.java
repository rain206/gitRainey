package edu.uw.raineyck.dao;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.uw.ext.framework.account.Account;

public class AccountSerJson {

	/** Logger */
	private static final Logger logger = LoggerFactory.getLogger(AccountSerJson.class);

	/** Null string to send if instance is null */
	private static final String nullString = "<null>";

	/** Constructor */
	public AccountSerJson() {
	}

	public void writeAccountToFile(Account account, File file) {
		ObjectMapper mapper = new ObjectMapper();

		try {
			mapper.writeValue(file, account);
			String update = String.format("Wrote %s to JSON file.", account.getName());
			logger.info(update);

		} catch (IOException e) {
			String warn = String.format("IOException writing value to file %s", file.getName());
			logger.warn(warn);
		}
	}

	public void readAccountFile(Account account, File file) {

	}

}
