package com.scg.beans;

import java.util.EventListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Monitors the SCG's terminations
 * 
 * @author craigrainey
 */
public class Eeoc implements TerminationListener, EventListener {

	/** Count of  forced terminations */
	private int forcedTerminationCount = 0;
	
	/** Count of voluntary terminations */
	private int voluntaryTerminationCount = 0;
	
	/**	Logger to log voluntary and forceful terminations */
	private static final Logger log = LoggerFactory.getLogger(TerminationListener.class);
	
	/** Constructor for Eeoc */
	public Eeoc() {
	}
	
	/** Invoked when a consultant voluntarily resigns*/
	public void voluntaryTermination(TerminationEvent evt) {
		log.info(evt.getConsultant().getNameConsultant()+ " quit.");
		voluntaryTerminationCount++;
	}

	/** Invoked when a consultant is forcefully terminated*/
	public void forcedTermination(TerminationEvent evt) {
		log.info(evt.getConsultant().getNameConsultant() + " was fired.");
		forcedTerminationCount++;
	}
	
	/**	Getter for forced termination count */
	public int forcedTerminationCount() {
		return forcedTerminationCount;
	}
	
	/**	Getter for voluntary termination count */
	public int voluntaryTerminationCount() {
		return voluntaryTerminationCount;
	}
}
