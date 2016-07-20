package com.scg.cmd;

import com.scg.domain.Consultant;
import com.scg.net.server.CommandProcessor;

/**
 * The command to add a Consultant to a list maintained by the server
 * 
 * @author craigrainey
 *
 */
public class AddConsultantCommand extends AbstractCommand<Consultant> {
	
	/**	Serial Version UID */
	private static final long serialVersionUID = 4423404454812808383L;
	
	/**	Consultant that is being added */
	private Consultant consultant;

	/**	Constructor for the command */
	public AddConsultantCommand(Consultant target) {
		this.consultant = target;
	}
	
	/** Execute this command */
	public void execute() {
		this.getReceiver().execute(this);
	}
	
	@Override
	public String toString() {
		return "Add Consutant command";
	}

}
