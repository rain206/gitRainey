package com.scg.beans;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.EventListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Logs changes in benefits
 * 
 * @author craigrainey
 */
public class BenefitManager implements PropertyChangeListener, EventListener {
	
	private static final Logger log = LoggerFactory.getLogger(BenefitManager.class);

	/** Constructor for Benefit Manager */
	public BenefitManager() {
	}

	/**
	 * Logs the change to evt - a property change event for sickLeaveHours, vacationHours,
	 * or payRate property;
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String property = evt.getPropertyName();
		if ("vacationHours".equals(property)) {
			log.info("sickLeaveHours changed from " +evt.getOldValue()+ " to " +evt.getNewValue());
		} else if ("sickLeaveHours".equals(property)) {
			log.info("vacationHours changed from " +evt.getOldValue()+ " to " +evt.getNewValue());
		}
	}

}
