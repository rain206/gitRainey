package com.scg.util;

import java.util.Comparator;
import com.scg.domain.TimeCard;

/**
 * Compares two TimeCard objects
 * @author craigrainey
 *
 */
public final class TimeCardConsultantComparator implements Comparator<TimeCard>{

	/** Constructor	 */
	public TimeCardConsultantComparator() {
	}
	
	/** Compares the time card objects by ascending consultant, timecard week, total hours,
	 * 	totalBillableHours, and totalNonBillableHours. Returns equal if all are same.
	 */
	@Override
	public int compare(TimeCard firstTimeCard, TimeCard secondTimeCard) {
		final int BEFORE = -1;
		final int EQUAL = 0;
		final int AFTER = 1;
		
		if (firstTimeCard == secondTimeCard) return EQUAL;
		
		int diff = firstTimeCard.getConsultant().compareTo(secondTimeCard.getConsultant());
		if (diff != 0) return diff;
		
		diff = firstTimeCard.getWeekStartingDay().compareTo(secondTimeCard.getWeekStartingDay());
		if (diff != 0) return diff;
		
		if (firstTimeCard.getTotalHours() < secondTimeCard.getTotalHours()) return BEFORE;
		if (firstTimeCard.getTotalHours() > secondTimeCard.getTotalHours()) return AFTER;
		
		if (firstTimeCard.getTotalBillableHours() < secondTimeCard.getTotalBillableHours()) return BEFORE;
		if (firstTimeCard.getTotalBillableHours() > firstTimeCard.getTotalBillableHours()) return AFTER;
		
		if (firstTimeCard.getTotalNonBillableHours() < secondTimeCard.getTotalNonBillableHours()) return BEFORE;
		if (firstTimeCard.getTotalNonBillableHours() > secondTimeCard.getTotalNonBillableHours()) return AFTER;
		
		return EQUAL;
	}

}
