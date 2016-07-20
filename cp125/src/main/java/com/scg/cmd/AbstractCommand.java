package com.scg.cmd;

import com.scg.net.server.CommandProcessor;

/**
 * The superclass of all command objects, implements the command role in the Command Design
 * pattern
 * 
 * @author craigrainey
 *
 * @param <T> the target type
 */
public abstract class AbstractCommand<T> implements Command<T> {
	
	/** Serial Version UID	 */
	private static final long serialVersionUID = 7939283714426696298L;

	/** Command */
	private Command<T> target;
	
	/** Receiver */
	private CommandProcessor rcvr;

	/**
	 * Construct an abstract command without a target; called from subclasses
	 */
	public AbstractCommand() {
	}

	/**
	 * Construct an abstract command with a target T, called from subclasses
	 * @param target T
	 */
	public AbstractCommand(Command<T> target) {
		this.target = target;
	}

	/**
	 * Gets the CommandProcessor receiver from this Command
	 * 
	 * @return the receiver for this command
	 */
	@Override
	public CommandProcessor getReceiver() {
		return rcvr;
	}

	/** Sets the CommandProcessor receiver from this Command */
	@Override
	public void setReceiver(CommandProcessor receiver) {
		this.rcvr = receiver;
	}

	/** Returns the target for this command	 */
	@Override
	public Command<T> getTarget() {
		return target;
	}
	
	/** A string representation for this command */
	@Override
	public String toString() {
		return "You want to..." +target;
	}

}
