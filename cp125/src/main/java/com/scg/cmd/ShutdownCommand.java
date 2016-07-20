package com.scg.cmd;

import com.scg.net.server.CommandProcessor;

/**
 * Command object to cause CommandProcessor to shutdown server
 * @author craigrainey
 *
 */
public class ShutdownCommand extends AbstractCommand<Void> {
	
	/** Serial Version UID	 */
	private static final long serialVersionUID = -5613850388495904857L;

	/**	Constructor */
	public ShutdownCommand() {
		System.out.println("You want to shutdown...");
	}
	
	/**	Execute command */
	public void execute() {
		this.getReceiver().execute(this);
	}
	
	/**
	 * String representation of command that is executed
	 */
	@Override
	public String toString() {
		return "Shut down command";
	}
}
