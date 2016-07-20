package com.scg.cmd;

import java.util.Date;

import com.scg.net.server.CommandProcessor;

/**
 * CreateInvoiceCommand object to add an invoice to the server's list
 * 
 * @author craigrainey
 *
 */
public class CreateInvoiceCommand extends AbstractCommand<Date> {

	/** Serial Version UID	 */
	private static final long serialVersionUID = -7639572856285341744L;

	/**	Invoice to add */
	private Date date;
	
	/**
	 * Constructor for the Invoice Command
	 * 
	 * @param target invoice to add
	 */
	public CreateInvoiceCommand(Date target) {
		this.date = target;
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
		return "Create invoice command";
	}
	
	/**
	 * 
	 * @return Date
	 */
	public Date getDate() {
		return date;
	}
}
