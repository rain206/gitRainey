package edu.uw.raineyck.dao;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uw.ext.framework.account.Account;
import edu.uw.ext.framework.account.Address;
import edu.uw.raineyck.account.AddressImpl;

/**
 * Address Ser class to send an account's address information to a binary file.
 * 
 * @author craigrainey
 *
 */
public class AddressSer {

	/** AddressSer Logger */
	private static Logger log = LoggerFactory.getLogger(AddressSer.class);

	/** String representing null was sent to binary file */
	private static final String nullString = "<null>";

	/** Encoding used to send strings to binary file */
	private static final String ENCODING = "UTF-8";

	/** Constructor */
	public AddressSer() {
	}

	/**
	 * Writes the components of the address to the appropriate binary fiel
	 * 
	 * @param fileName
	 *            name of File
	 * @param account
	 *            Account which information is being extracted from
	 * @param dos
	 *            data output stream
	 * @param AddressDir
	 *            address directory
	 */
	public void writeAddressToBinary(Account account, DataOutputStream dos) {

		if (account.getAddress() != null) {
			try {
				writeString(account.getAddress().getStreetAddress(), dos);
				writeString(account.getAddress().getCity(), dos);
				writeString(account.getAddress().getState(), dos);
				writeString(account.getAddress().getZipCode(), dos);

				dos.flush();
				dos.close();

			} catch (IOException e) {
				log.warn("IO error writing address to file");
			}
		}
	}

	/**
	 * Reads an account's address information from a binary file.
	 * 
	 * @param accountName
	 *            name of account retrieving information for
	 * @param dis
	 *            datainputstream
	 * @return address information
	 */
	public Address readAddressFromBinary(String accountName, DataInputStream dis) {
		try {
			String streetAddress = dis.readUTF();
			String city = dis.readUTF();
			String state = dis.readUTF();
			String zip = dis.readUTF();

			Address tempAddress = new AddressImpl();
			tempAddress.setStreetAddress(streetAddress);
			tempAddress.setCity(city);
			tempAddress.setState(state);
			tempAddress.setZipCode(zip);

			return tempAddress;

		} catch (IOException i) {
			// String warn = String.format("IOException retrieving address
			// information for %s.", accountName);
			// log.warn(warn);
		}
		return null;
	}

	/**
	 * Creates a byte array with the string and writes it using the data output
	 * stream.
	 * 
	 * @param sendString
	 *            address component to write to binary file
	 * @param dataOutput
	 *            data output stream
	 */
	private void writeString(String sendString, DataOutputStream dataOutput) {
		try {
			dataOutput.writeUTF(sendString == null ? nullString : sendString);
		} catch (IOException i) {
			log.warn("IO Exception sending address string.", i);
		}
	}

}
