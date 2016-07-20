package edu.uw.raineyck.dao;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uw.ext.framework.account.Account;
import edu.uw.raineyck.account.AccountImpl;

/**
 * Account Ser class to send account information to a binary file
 * 
 * @author craigrainey
 *
 */
public class AccountSer {

	/** AccountSer logger */
	private static final Logger log = LoggerFactory.getLogger(AccountSer.class);

	/** String representing null is sent to file */
	private static final String nullString = "<null>";

	/** Encoding used to write to binary file */
	private static final String ENCODING = "UTF-8";

	/** Constructor */
	public AccountSer() {
	}

	/**
	 * Writes credit card information to binary file
	 * 
	 * @param card
	 *            Credit card which information is being sent
	 * @param dos
	 *            data output stream
	 */
	public void writeAccountToBinary(Account account, DataOutputStream dos) {

		if (account != null) {
			try {
				writeString(account.getName(), dos);
				writeString(account.getFullName(), dos);
				writeString(account.getEmail(), dos);
				writeString(account.getPhone(), dos);
				dos.writeInt(account.getBalance());
				dos.write(account.getPasswordHash());

				dos.flush();
				dos.close();

			} catch (IOException e) {
				log.warn("IO Exception writing account information to binary file.", e);
			}
		}
	}

	/**
	 * Reads the account information from the binary file
	 * 
	 * @param dis
	 *            datainputstream
	 * @param accountName
	 *            name of account getting information for
	 * @return account instantiation of target acct.
	 */
	public Account readAccountFromBinary(DataInputStream dis, String accountName) {
		try {
			String nameOfAccount = dis.readUTF();
			String fullNameOnAccount = dis.readUTF();
			String emailOnAccount = dis.readUTF();
			String phoneNumOnAcct = dis.readUTF();
			int balanceOnAcct = dis.readInt();

			byte[] passwordHash = new byte[dis.available()];
			dis.read(passwordHash);

			Account tempAccount = new AccountImpl(nameOfAccount, passwordHash, balanceOnAcct);
			tempAccount.setFullName(fullNameOnAccount);
			tempAccount.setEmail(emailOnAccount);
			tempAccount.setPhone(phoneNumOnAcct);

			return tempAccount;

		} catch (IOException i) {
			String warn = String.format("Error retrieving account information for: %s", accountName);
			i.printStackTrace();
			log.warn(warn);
		}
		return null;
	}

	/**
	 * Writes the strings to the binary file using data output stream
	 * 
	 * @param accountInfo
	 *            specific account information to send
	 * @param dos
	 *            data output stream
	 */
	private void writeString(String accountInfo, DataOutputStream dos) {
		try {
			dos.writeUTF(accountInfo == null ? nullString : accountInfo);
		} catch (IOException e) {
			e.printStackTrace();
			String warn = String.format("IO Exception writing %s to binary file", accountInfo);
			log.warn(warn, e);
		}
	}

}
