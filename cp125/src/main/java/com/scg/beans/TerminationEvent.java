package com.scg.beans;

import java.util.EventObject;

import com.scg.domain.Consultant;

/**
 * Event used to notify users of consultant termination. Extends EventObject
 * 
 * @author craigrainey
 */
public class TerminationEvent extends EventObject {
	
	/** Serial Version UID	 */
	private static final long serialVersionUID = 2790303176732906818L;

	/** Consultant being terminated */
	private Consultant consultant;
	
	/** Boolean describing if termination is voluntary or not */
	private boolean voluntary;

	/** Constructor for Termination Event */
	public TerminationEvent(Object source, Consultant consultant, boolean voluntary) {
		super(source);
		this.consultant = consultant;
		this.voluntary = voluntary;
	}
	
	/** Getter for consultant */
	public Consultant getConsultant() {
		return consultant;
	}

	/** Getter for voluntary boolean*/
	public boolean getVoluntary() {
		return voluntary;
	}
}
