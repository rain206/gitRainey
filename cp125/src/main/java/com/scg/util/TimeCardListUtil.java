package com.scg.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.scg.domain.Consultant;
import com.scg.domain.TimeCard;


/**
 * Time Card List Utility class that gets all the time cards for each consultant,
 * retrieves relevant time cards that are within a given date range, and sorts
 * the list by start date or consultant name.
 * @author craigrainey
 *
 */
public final class TimeCardListUtil implements java.io.Serializable {

	/** Serial Version UID	 */
	private static final long serialVersionUID = 6681999168804818952L;


	/**
	 * Goes through all the time card objects and returns a list of time cards for the specified
	 * consultant
	 * @param timeCards
	 * @param consultant
	 * @return
	 */
	public static List<TimeCard> getTimeCardsForConsultants(List<TimeCard> timeCards, Consultant consultant) {
		List consultantList = new ArrayList<TimeCard>();
		
		for (TimeCard consultantTime : timeCards) {
			if (consultantTime.getConsultant().equals(consultant)) {
				consultantList.add(consultantTime);
			}
		}
		return consultantList;
	}
	
	
	/**
	 * Scans through time cards and add to list if the time card has a Consultant Time object that
	 * is within the date range. Once such an object is found the time card will be added.
	 * @param timeCards
	 * @param dateRange
	 * @return
	 */
	public static List<TimeCard> getTimeCardsForDateRange(List<TimeCard> timeCards, DateRange dateRange) {
		List dateList = new ArrayList<TimeCard>();
		Calendar calendar = Calendar.getInstance();
		
		for (TimeCard card : timeCards) {
			calendar.setTime(card.getWeekStartingDay());
			Date startDate = calendar.getTime();
			
			calendar.set(Calendar.DAY_OF_WEEK, 6);
			Date endDate = calendar.getTime();
			
			if (dateRange.isInRange(startDate) || dateRange.isInRange(endDate)) {
				dateList.add(card);
			}
		}
		return dateList;
	}
	
	
	/**
	 * Compares each date in a List of TimeCard objects and the sorts the list in ascending order
	 * @param timeCards
	 */
	public static void sortByStartDate(List<TimeCard> timeCards) {
		Collections.sort(timeCards, new Comparator<TimeCard>() {
			public int compare(TimeCard t1, TimeCard t2) {
				return t1.getWeekStartingDay().compareTo(t2.getWeekStartingDay());
			}
		});
	}
	
	
	/**
	 * Sorts given list by consultant's first name. If first is the same then middle, and then last
	 * if both of those are the same.
	 * @param timeCards
	 */
	public static void sortByConsultantName(List<TimeCard> timeCards) {
		Collections.sort(timeCards, new Comparator<TimeCard>() {
			public int compare(TimeCard t1, TimeCard t2) {
				
				TimeCardConsultantComparator comparator = new TimeCardConsultantComparator();
				return comparator.compare(t1, t2);
			}
		});
	}

}
