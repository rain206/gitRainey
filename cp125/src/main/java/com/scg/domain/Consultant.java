package com.scg.domain;


import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scg.util.Name;

/**
 * Consultant class to represent the name of the consultant
 * @author craigrainey
 *
 */
public class Consultant implements Comparable <Consultant>, java.io.Serializable {

	/** Serial Version ID	 */
	private static final long serialVersionUID = 7276414432630599412L;
	
	/**Name of the consultant */
	private Name nameConsultant;

	/** Constructor for the Consultant class */
	public Consultant(Name consultantName) {
		this.nameConsultant = consultantName;
	}
	
	/** Overwrites toString method to print the name of the consultant	 */
	public final String toString() {
		return nameConsultant.toString();
	}
	
	/** Overrides compareTo for Consultant class */
	@Override
	public final int compareTo(Consultant other) {
		return this.nameConsultant.compareTo(other.nameConsultant);
	}
	
	
	/**
	 * Proxy inner class Consultant Object to use for serialization
	 * 
	 * @author craigrainey
	 */
	private static class ConsultantProxy implements Serializable {
		

		/** Serial Version UID	 */
		private static final long serialVersionUID = 8534189375285464752L;
		
		/**	Logger for Consultant Proxy */
		private Logger logProxy = LoggerFactory.getLogger(ConsultantProxy.class);

		/**	Name of Consultant */
		private Name name;

		
		/**	Constructor for serialization proxy*/
		public ConsultantProxy(Consultant consultant) {
			this.name = consultant.getNameConsultant();
		}
		
		/** @return read object from serialized stream*/
		private Object readResolve() {
			return new Consultant(name);
		}
		
		/**
		 * @param ois is never used
		 * @throws InvalidObjectException always thrown to message user to use proxy
		 */
		public void readObject(ObjectInputStream ois) 
				throws InvalidObjectException {	
			logProxy.info("Use Consultant Proxy Instead");
			throw new InvalidObjectException("Use Consultant Proxy instead");	
		}
		
		/**
		 * Override writeReplace method for serialization
		 * @return this object
		 */
		private Object writeReplace() {
			logProxy.info("writeReplace for Consultant");
			return this;
		}
	}
	

	
	/** Getter and setters */
	public final Name getNameConsultant() {
		return nameConsultant;
	}
	public void setNameConsultant(Name nameConsultant) {
		this.nameConsultant = nameConsultant;
	}
	public long getSerialVerionID() {
		return serialVersionUID;
	}
}
