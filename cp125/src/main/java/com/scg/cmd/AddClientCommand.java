package com.scg.cmd;


import com.scg.domain.ClientAccount;
import com.scg.net.server.CommandProcessor;

/**
 * The command to add a client to a list maintained by a server
 * 
 * @author craigrainey
 *
 */
public class AddClientCommand extends AbstractCommand<ClientAccount> {
	
	/** Serial Version UID	 */
	private static final long serialVersionUID = -979634413976868228L;

	/** Client account */
	private ClientAccount client;

	/**
	 * Construct an AddClientCommand object with a target
	 * 
	 * @param target client account to add to server
	 */
	public AddClientCommand(ClientAccount target) {
		this.client = target;
	}
	
	/**
	 * Execute this command
	 */
	@Override
	public void execute() {
		this.getReceiver().execute(this);
	}
	
	/**
	 * String representation of object
	 */
	@Override
	public String toString() {
		return "Add Client command";
	}
	
	/**
	 * @return client account
	 */
	public ClientAccount getClient() {
		return client;
	}
}
