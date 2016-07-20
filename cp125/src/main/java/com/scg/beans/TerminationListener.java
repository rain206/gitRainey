package com.scg.beans;

/**
 * Interface for accepting notification of consultant termination
 * 
 * @author craigrainey
 */
public interface TerminationListener extends java.util.EventListener {

	
	/**
	 * Set when a consultant voluntarily terminates their employment
	 * 
	 * @param evt is termination event
	 */
	void voluntaryTermination(TerminationEvent evt);
	
	
	/**
	 * Set when a consultant is forced to terminate employment
	 * 
	 * @param evt is termination event
	 */
	void forcedTermination(TerminationEvent evt);
}
