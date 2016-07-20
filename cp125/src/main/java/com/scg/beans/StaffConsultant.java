package com.scg.beans;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;


import com.scg.domain.Consultant;
import com.scg.util.Name;

/**
 * A consultant who's kept on staff (receives benefits)
 * 
 * @author craigrainey
 */
public class StaffConsultant extends Consultant {

	/** Serial version UID */
	private static final long serialVersionUID = 1301969194715425046L;

	/** Name of staff consultant */
	private Name name;
	
	/** rate of consultant */
	private int payRate;
	
	/** sick leave */
	private int sickLeaveHours;
	
	/** vacation */
	private int vacationHours;
	
	/** Pay rate property name */
	public static final String PAY_RATE_PROPERTY_NAME = "payRate";
	
	/** Sick leave hours property name */
	public static final String SICK_LEAVE_HOURS_PROPERTY_NAME = "sickLeaveHours";
	
	/** Vacation hours property name */
	public static final String VACATION_HOURS_PROPERTY_NAME = "vacationHours";
	
	/**	Property change support */
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	
	/**	Vetoable change support */
	private final VetoableChangeSupport vcs = new VetoableChangeSupport(this);
	
	
	/** Create a new instance of StaffConsultant */
	public StaffConsultant(Name name, int rate, int sickLeave, int vacation) {
		super(name);
		this.name = name;
		this.payRate = rate;
		this.sickLeaveHours = sickLeave;
		this.vacationHours = vacation;
	}
	
	
	/** Set the payRate */
	public void setPayRate(int newRate) throws PropertyVetoException {
		int oldRate = this.payRate;
		vcs.fireVetoableChange(PAY_RATE_PROPERTY_NAME, oldRate, newRate);
		this.payRate = newRate;
		pcs.firePropertyChange(PAY_RATE_PROPERTY_NAME, oldRate, newRate);
	}
	
	/**	Set the sick leave hours */
	public void setSickLeaveHours(int sickLeave) {
		int oldSick = this.sickLeaveHours;
		this.sickLeaveHours = sickLeave;
		pcs.firePropertyChange(SICK_LEAVE_HOURS_PROPERTY_NAME, oldSick, sickLeave);
	}
	
	/**	Set the Vacation hours */
	public void setVacationHours(int vacation) {
		int oldVacation = this.vacationHours;
		this.vacationHours = vacation;
		pcs.firePropertyChange(VACATION_HOURS_PROPERTY_NAME, oldVacation, vacation);
	}
	
	
	
	
	/** Add a property change listener */
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}
	
	/**	remove property change listener */
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		pcs.removePropertyChangeListener(listener);
	}
	
	/**	adds a pay rate property change listener */
	public void addPayRateListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(PAY_RATE_PROPERTY_NAME, listener);
	}
	
	/**	Remove a pay rate property change listener */
	public void removePayRateListener(PropertyChangeListener listener) {
		pcs.removePropertyChangeListener(PAY_RATE_PROPERTY_NAME, listener);
	}
	
	/**	Add a general vetoable change listener */
	public void addVetoableChangeListener(VetoableChangeListener veto) {
		vcs.addVetoableChangeListener(PAY_RATE_PROPERTY_NAME, veto);
	}

	/**	Remove a general vetoable change listener */
	public void removeVetoableChangeListener(VetoableChangeListener veto) {
		vcs.removeVetoableChangeListener(PAY_RATE_PROPERTY_NAME, veto);
	}
	
	/**	add sick leave listener */
	public void addSickLeaveListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(SICK_LEAVE_HOURS_PROPERTY_NAME, listener);
	}
	
	/**	Remove sick leave listener */
	public void removeSickLeaveListener(PropertyChangeListener listener) {
		pcs.removePropertyChangeListener(SICK_LEAVE_HOURS_PROPERTY_NAME, listener);
	}
	
	/**	Add vacation hours listener */
	public void addVacationHoursListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(VACATION_HOURS_PROPERTY_NAME, listener);
	}
	
	/**	Remove vacation hours listener */
	public void removeVacationHoursListener(PropertyChangeListener listener) {
		pcs.removePropertyChangeListener(VACATION_HOURS_PROPERTY_NAME, listener);
	}
	
	/** Override hashCode for StaffConsultant */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result  + ((name == null) ? 0 : name.hashCode());
		result += prime * result + payRate;
		result += prime * result + sickLeaveHours;
		result += prime * result + vacationHours;
		return result;
	}
	
	/** Overrides equals for StaffConsultant */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof StaffConsultant)) {
			return false;
		}
		final StaffConsultant other = (StaffConsultant) obj;	
		if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
			return false;
		}
		if (this.payRate != other.payRate) {
			return false;
		}
		if (this.vacationHours != other.vacationHours) {
			return false;
		}
		if (this.sickLeaveHours != other.sickLeaveHours) {
			return false;
		}
		return true;
	}
	
	
	/** Getter and setters */
	public synchronized int getPayRate() {
		return payRate;
	}
	public synchronized int getSickLeaveHours() {
		return sickLeaveHours;
	}
	public synchronized int getVacationHours() {
		return vacationHours;
	}
	

}
