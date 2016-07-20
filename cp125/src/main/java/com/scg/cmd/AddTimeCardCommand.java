package com.scg.cmd;


import com.scg.domain.TimeCard;
import com.scg.net.server.CommandProcessor;

/**
 * Command that adds a TimeCard to the server's list
 * @author craigrainey
 *
 */
public class AddTimeCardCommand extends AbstractCommand<TimeCard> {
	
	/** Serial Version UID	 */
	private static final long serialVersionUID = -346573582191046114L;

	/**	TimeCard being added to the server's list */
	private TimeCard timeCard;
	

	/**
	 * Constructor for the AddTimeCardCommand object
	 * 
	 * @param target timeCard to be added
	 */
	public AddTimeCardCommand(TimeCard target) {
		this.timeCard = target;
	}
	
	/** Execute command */
	@Override
	public void execute() {
		this.getReceiver().execute(this);
	}
	
	/**
	 * @return time card
	 */
	public TimeCard getTimeCard() {
		return timeCard;
	}
}
