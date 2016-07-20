package edu.uw.raineyck.dao;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uw.ext.framework.account.CreditCard;
import edu.uw.raineyck.account.CreditCardImpl;

/**
 * Credit card Ser class to send an account's credit card information to a
 * binary file.
 * 
 * @author craigrainey
 *
 */
public class CreditCardSer {

	/** Logger */
	private static final Logger log = LoggerFactory.getLogger(CreditCardSer.class);

	/** String representing null was sent to binary file */
	private static final String nullString = "<null>";

	/** Encoding used to send strings to binary file */
	private static final String ENCODING = "UTF-8";

	/** Constructor */
	public CreditCardSer() {
	}

	/**
	 * Writes credit card information to binary file
	 * 
	 * @param card
	 *            Credit card which information is being sent
	 * @param dos
	 *            data output stream
	 */
	public void writeCreditCardToBinary(CreditCard card, DataOutputStream dos) {

		if (card != null) {
			try {
				writeString(card.getAccountNumber(), dos);
				writeString(card.getExpirationDate(), dos);
				writeString(card.getHolder(), dos);
				writeString(card.getIssuer(), dos);
				writeString(card.getType(), dos);

				dos.flush();
				dos.close();

			} catch (IOException e) {
				log.warn("IO Exception writing credit card information to binary file.", e);
			}
		}
	}

	/**
	 * Reads credit card information for target account from binary file.
	 * 
	 * @param accountName
	 *            name of account to get information for
	 * @param dis
	 *            datainputstream
	 * @return credit card instantiation of account's credit card info.
	 */
	public CreditCard readCreditCardFromBinary(String accountName, DataInputStream dis) {
		try {
			String accountNum = dis.readUTF();
			String expDate = dis.readUTF();
			String holder = dis.readUTF();
			String issuer = dis.readUTF();
			String type = dis.readUTF();

			CreditCard acctCard = new CreditCardImpl();
			acctCard.setAccountNumber(accountNum);
			acctCard.setExpirationDate(expDate);
			acctCard.setHolder(holder);
			acctCard.setIssuer(issuer);
			acctCard.setType(type);

			return acctCard;

		} catch (IOException i) {
			// String warn = String.format("IOException retreiving credit card
			// information for %s", accountName);
			// log.warn(warn);
		}

		return null;
	}

	/**
	 * Writes the strings to the binary file using data output stream
	 * 
	 * @param cardInfo
	 *            specific card information to send
	 * @param dos
	 *            data output stream
	 */
	private void writeString(String cardInfo, DataOutputStream dos) {
		try {
			dos.writeUTF(cardInfo == null ? nullString : cardInfo);
		} catch (IOException e) {
			String warn = String.format("IO Exception writing %s to binary file", cardInfo);
			log.warn(warn, e);
		}
	}
}
