package com.scg.domain;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scg.util.Address;
import com.scg.util.Name;

/**
 * Records an account for the client that the consultant is working with.
 * @author craigrainey
 */
public class ClientAccount implements Account, Comparable<ClientAccount>, java.io.Serializable {


	/** Serial Version ID	 */
	private static final long serialVersionUID = 8267001771230847818L;

	/**String representing the clients name*/
	private String name;
	
	/**Contact person for the client */
	private Name contact;
	
	/** Address for the Client Account	 */
	private Address address;


	/**
	 * Constructor for the ClientAccount to create a new instance
	 * @param name of the client
	 * @param contact of the client
	 */
	public ClientAccount(String name, Name contact, Address address) {
		this.name = name;
		this.contact = contact;
		this.address = address;
	}
	
	/** @return if client is billable (always true for now)	 */
	public boolean isBillable() {
		return true;
	}
	
	/** @param clientContact is the contact person for the client */
	public void setContactName(Name clientContact) {
		this.contact = clientContact;
	}
	
	
	/** Overrides compareTo for the ClientAccount by comparing name, contact, and address	 */
	@Override
	public int compareTo(ClientAccount other) {
		int comp = name.compareTo(other.name);
		if (comp != 0) {
			return comp;
		}
		comp = contact.compareTo(other.contact);
		if (comp != 0) {
			return comp;
		}
		return address.compareTo(other.address);
	}
	
	
	/**
	 * Proxy inner class Consultant Object to use for serialization
	 * 
	 * @author craigrainey
	 */
	private static class ClientAccountProxy implements Serializable {

		
		/** Serial Version UID */
		private static final long serialVersionUID = 1703146781088144178L;

		/**	Logger for Consultant Proxy */
		private Logger logProxy = LoggerFactory.getLogger(ClientAccountProxy.class);

		/**	Name of Client Account */
		private String name;
		
		private Name contact;

		private Address address;
		
		/**	Constructor for serialization proxy*/
		public ClientAccountProxy(ClientAccount client) {
			this.name = client.getName();
			this.contact = client.getContact();
			this.address = client.getAddress();
		}
		
		/** @return read object from serialized stream*/
		private Object readResolve() {
			return new ClientAccount(name, contact, address);
		}
		
		/**
		 * @param ois is never used
		 * @throws InvalidObjectException always thrown to message user to use proxy
		 */
		public void readObject(ObjectInputStream ois) 
				throws InvalidObjectException {	
			logProxy.info("Use Client Account Proxy Instead");
			throw new InvalidObjectException("Use Client Account Proxy instead");	
		}
		
		/**
		 * Override writeReplace method for serialization
		 * @return this object
		 */
		private Object writeReplace() {
			logProxy.info("writeReplace for Client Account");
			return this;
		}
	}
	
	
	/**	To string representation for Client Account */
	@Override
	public String toString() {
		return name;
	}
	
	/** Getters and Setter method */
	public String getName() {
		return name;
	}
	public Name getContact() {
		return contact;
	}
	public void setContact(Name contact) {
		this.contact = contact;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address addressSet) {
		this.address = addressSet;
	}
	public long getSerialVersionUID() {
		return serialVersionUID;
	}
}
