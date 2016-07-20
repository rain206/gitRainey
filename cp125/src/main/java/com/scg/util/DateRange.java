package com.scg.util;

import java.util.Date;
import java.util.Locale;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Takes two date objects and sets a range.
 * @author craigrainey
 *
 */
public class DateRange implements java.io.Serializable {
	
	/** Serial Version UID	 */
	private static final long serialVersionUID = 6608769254189234360L;

	/** Start Date for the range	 */
	private Date startDate;
	
	/** End date for the range	 */
	private Date endDate;
	
	/** Constructor to create an instance for the Date Range with 2 date objects */
	public DateRange(Date startDate, Date endDate) {
		this.startDate = startDate;
		this.endDate = endDate;
	}

	/**
	 * Constructor to create an instance of the Date range using two String objects
	 * that have to be in correct format.
	 */
	public DateRange(String start, String end) {
		DateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
		try {
			Date date = format.parse(start);
			this.startDate = date;
			
			date = format.parse(end);
			this.endDate = date;
		} catch(ParseException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Invalid date format(s). Must be MM/dd/yyyy", e);
		}
	}
	
	
	/** Constructor that takes two integers to represent (month and year) to create a date range */
	public DateRange(int month, int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.YEAR, year);
		
		// Set start date using minimum time of given month
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
		calendar.set(Calendar.MILLISECOND, calendar.getActualMinimum(Calendar.MILLISECOND));
		this.startDate = calendar.getTime();
		
		//Set end date using maximum time of given month
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
		calendar.set(Calendar.MILLISECOND, calendar.getActualMaximum(Calendar.MILLISECOND));
		this.endDate = calendar.getTime();	
	}
	
	
	/**
	 * Compares date argument to start date and end date and returns true if within range
	 * of false if not. 
	 */
	public boolean isInRange(Date date) {
		if (date.after(startDate) && date.before(endDate) || date.equals(startDate) && date.before(endDate)
				|| date.after(startDate) && date.equals(endDate)) {
			return true;
		}
		return false;
	}
	
	/** Getters and Setters for the dates provided */
	public Date getStartDate() {
		return startDate;
	}
	public Date getEndDate() {
		return endDate;
	}

}
