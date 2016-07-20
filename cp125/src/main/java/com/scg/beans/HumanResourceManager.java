package com.scg.beans;

import java.beans.PropertyVetoException;
import javax.swing.event.EventListenerList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.scg.domain.Consultant;

/**
 * Responsible for modifying the pay rate and sick leave and vacation hours of staff consultants  
 *
 * @author craigrainey
 *
 */
public class HumanResourceManager {
	
	/**	EventListenerList to hold all Listener objects */
	private EventListenerList listenerList = new EventListenerList();

	/** Logger to output property changes and vetos	 */
	private static final Logger log = LoggerFactory.getLogger(HumanResourceManager.class);

	/**	Constructor for Human Resource Manager */
	public HumanResourceManager() {
	}
	
	/**	Sets the pay rate for a staff consultant */
	public void adjustPayRate(StaffConsultant c, int newPayRate) throws PropertyVetoException {
		try {
			c.setPayRate(newPayRate);
			log.info("Approved pay adjustment for " +c.getNameConsultant());
		} catch (PropertyVetoException p) {
			log.info("REJECTED pay adjustment for " +c.getNameConsultant());
		}
	}
	
	/**	Sets the sick leave hours for a staff consultant */
	public void adjustSickLeaveHours(StaffConsultant c, int newSickLeaveHours) {
		c.setSickLeaveHours(newSickLeaveHours);
	}

	/**	Sets the vacation hours for a staff consultant */
	public void adjustVacationHours(StaffConsultant c, int newVacationHours) {
		c.setVacationHours(newVacationHours);
	}
	
	/**
	 * Fires a termination event by circulating through the Event listener list for an Eeoc object.
	 * Then calls the voluntary or forced termination method accordingly.
	 * 
	 * @param terminate is a terminate event
	 */
	private void fireTerminationEvent(TerminationEvent terminate) {
		Object[] listeners = listenerList.getListenerList();
		for (Object listen : listeners) {
			if (listen.getClass() == Eeoc.class) {
				if (terminate.getVoluntary() == true) {					
					((Eeoc)listen).voluntaryTermination(terminate);
				} else {
					((Eeoc)listen).forcedTermination(terminate);
				}
			}
		}
	}
	
	/**	Fires a voluntary termination event for a staff consultant */
	public void acceptResignation(Consultant c) {
		fireTerminationEvent(new TerminationEvent(this, c, true));
	}
	
	/**	Fires an involuntary termination event for a staff consultant */
	public void terminate(Consultant c) {
		fireTerminationEvent(new TerminationEvent(this, c, false));
	}
	
	/**	Adds a termination listener */
	public void addTerminationListener(TerminationListener l) {
		listenerList.add(TerminationListener.class, l);
	}
	
	/**	Removes a termination listener */
	public void removeTerminationListener(TerminationListener l) {
		listenerList.remove(TerminationListener.class, l);
	}
}
