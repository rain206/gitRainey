package com.scg.cmd;

import com.scg.net.server.CommandProcessor;

/**
 * The superclass of all Command objects, implements the command role in the command
 * design pattern
 * 
 * @author craigrainey
 *
 */
public interface Command<T> extends java.io.Serializable {

	/**
	 * Must be implemented by subclasses to send a reference to themselves to the receiver
	 * by calling receiver.execute(this).
	 * 
	 */
	void execute();
	
	/**
	 * Gets the command processor receiver for this command
	 * 
	 * @return receiver
	 */
	CommandProcessor getReceiver();
	
	
	/**
	 * Sets the CommandProcessor that will execute this command
	 */
	void setReceiver(CommandProcessor receiver);
	
	
	/**
	 * Get T (Target type)
	 * 
	 * @return target
	 */
	Command<T> getTarget();
}
