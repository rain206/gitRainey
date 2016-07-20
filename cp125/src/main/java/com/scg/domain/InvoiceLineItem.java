package com.scg.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class to record a single line item taken from the time card lines
 * @author craigrainey
 *
 */
public final class InvoiceLineItem implements java.io.Serializable {

	/**	 Consultant on the invoice */
	private Consultant invoiceConsultant;
	
	/**	Date consultant worked */
	private Date date;
	
	/**	Skill of the consultant */
	private Skill skill;
	
	/**	 Hours the consultant worked for this given date */
	private int hours;
	
	/**
	 * Constructor for the InvoiceLineItem class to create instances
	 * @param date of the invoice
	 * @param consultant on invoice
	 * @param skill of consultant
	 * @param hours consultant worked
	 */
	public InvoiceLineItem(Date date, Consultant consultant, Skill skill, int hours) {
		
		if (hours <= 0) {
			throw new IllegalArgumentException(
					"InvoiceLineItem requires hours greater than 0");
		}
		this.date = new Date(date.getTime());
		this.invoiceConsultant = consultant;
		this.skill = skill;
		this.hours = hours;
	}
	
	/**
	 * Returns the charge of the consultant
	 */
	public int getCharge() {
		return skill.getRate() * hours;
	}
	
	/**
	 * Returns the consultant being invoiced
	 * @return
	 */
	public Consultant getConsultant() {
		return invoiceConsultant;
	}
	
	/**	Returns the date the consultant worked */
	public Date getDate() {
		return date;
	}

	/**
	 * @return the hours worked
	 */
	public int getHours() {
		return hours;
	}
	
	/**
	 * @return the consultants skill
	 */
	public Skill getSkill() {
		return skill;
	}
	
	/**
	 * toString method that will print out the date in a simple date format, consultant, skill
	 * hours, and charge
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		String dateFormat = format.format(date);
		
		builder.append(dateFormat+ " " +invoiceConsultant.getNameConsultant()+ " "
				+skill.toString()+ " " +getCharge());
		
		return builder.toString();
	}
}

