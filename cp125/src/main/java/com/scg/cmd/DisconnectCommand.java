package com.scg.cmd;

import com.scg.net.server.CommandProcessor;

/**
 * DisconnectCommand to disconnect from server
 * 
 * @author craigrainey
 *
 */
public class DisconnectCommand extends AbstractCommand<Void> {

	/** Serial Version UID	 */
	private static final long serialVersionUID = 8220129266954241772L;
	
	/**	Construct a disconnect command */
	public DisconnectCommand() {
		System.out.println("Disconnecting...");
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
		return "Disconnect command";
	}
}
