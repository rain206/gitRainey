package com.scg.domain;

import java.util.Date;

/**
 * Consultant Time object that tracks data for the Date, Account, Skill type, and hours the consultants
 * worked with each day
 * @author craigrainey
 *
 */
public final class ConsultantTime implements java.io.Serializable {

	/** Serial Version UID */
	private static final long serialVersionUID = 3251639756123731061L;

	/** Date representation	 */
	private Date date;
	
	/**	Account type */
	protected Account account;
	
	/**	Skill type of the consultant */
	private final Skill skillType;
	
	/**	Hours the consultant has worked  */
	protected int hours;
	

	/**
	 * Constructor for the Consultant Time
	 * @param date this instance occurred
	 * @param account to charge hours to
	 * @param skillType of the consultant
	 * @param number of hours, must be positive
	 */
	public ConsultantTime(final Date date, final Account account, final Skill skillType, final int hours) {
		setHours(hours);
		this.date = new Date(date.getTime());
		this.account = account;
		this.skillType = skillType;
		this.hours = hours;
	}
	
	
	/**
	 * @return if account is billable or not
	 */
	public boolean isBillable() {
		if (account.equals(NonBillableAccount.VACATION) || account.equals(NonBillableAccount.BUSINESS_DEVELOPMENT)
				|| account.equals(NonBillableAccount.SICK_LEAVE)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * @Overrides hashcode for the ConsultantTime
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result  + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((skillType == null) ? 0 : skillType.hashCode());
		result = prime * result + hours;
		return result;
	}
	
	/**
	 * @Overrides equals for the Consultant Time
	 */
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof ConsultantTime)) {
			return false;
		}
		final ConsultantTime other = (ConsultantTime) obj;
		
		if ((this.date == null) ? (other.date != null) : !this.date.equals(other.date)) {
			return false;
		}
		if (this.account != other.account) {
			return false;
		}
		return true;
	}

	
	/**
	 * @Overrides toString to create a string representation of the consultant time
	 */
	public String toString() {
		return String.format("Consultant Time: %d", hours);
	}
	
	
	/**
	 * Getters and Settersß
	 */
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	public Skill getSkillType() {
		return skillType;
	}
	public int getHours() {
		return hours;
	}
	/**
	 * Setter for hours that will throw an IllegalArgumentException if hours parameter
	 * is less than 0 since this is impossible.
	 */
	public void setHours(int hours) {
		if (hours > 0) {
			this.hours = hours;
		} else {
			throw new IllegalArgumentException("Hours must be greater than 0");
		}
	}
}
