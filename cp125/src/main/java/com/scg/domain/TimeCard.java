package com.scg.domain;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Creates a time card object for each consultant and their work week. This includes Account, Date,
 * Hours, and Skill, along with billable-time and nonbillable-time. An actual report is then able
 * to be printed
 * @author craigrainey
 *
 */
public class TimeCard implements Comparable<TimeCard>, java.io.Serializable{

	/** Serial Version ID	 */
	private static final long serialVersionUID = 946977836690478770L;

	/**	gets properties for consultant*/
	private Consultant consultant;
	
	/** Date that represents the week starting day	 */
	private  Date weekStartingDay;
	
	/** Arraylist of the all ConsultantTime objects	for this time card */
	private ArrayList<ConsultantTime> consultingTime = new ArrayList<>();
	
	/**	Formatter for the strings */
	private String STRING_FMT = "%s 	%2$8tm/%2$td/%2$tY  %3$6d   %4$6s %n";
	
	/**	Formatter for the name and date at the top of each time card */
	private String NAME_DATE_FMT = "Consultant: %s	%2$27s %3$tb/%3$td/%3$tY %n";
	
	/**	Divide or border for each time card */
	private String DIVIDE = "======================================================================= \n";
	
	/**	Dashes under each header */
	private String DASHES = "---------------------------   ----------   -----   -------------------- \n";

	/**	Headers for the time card */
	private String HEADERS = String.format("%s %2$26s %3$13s %4$7s %n%5$s", "Account", "Date", "Hours", "Skill", DASHES);
	
	/** total hours include billable and non billable	 */
	private int totalHours;
	
	/**	total nonBillable hours */
	private int totalBillableHours;
	
	/** total Non billable hours	 */
	private int totalNonBillableHours;
	
	
	/**
	 * Constructor to create a new instance of TimeCard
	 * @param consultant
	 * @param weekStartingDay
	 */
	public TimeCard(Consultant consultant, Date weekStartingDay) {
		this.consultant = consultant;
		this.weekStartingDay = weekStartingDay;
	}

	
	/**
	 * Override compareTo for TimeCard and orders by consultant, weekstarting day,
	 * total hours, total billable hours, then total nonbillable hours
	 */
	@Override
	public int compareTo(TimeCard other) {
		int comp = 0;
		
		comp = consultant.compareTo(other.consultant);
		if (comp != 0) {
			return comp;
		}
		
		comp = weekStartingDay.compareTo(other.weekStartingDay);
		if (comp != 0) {
			return comp;
		}
		
		comp = Integer.compare(this.totalHours, other.totalHours);
		if (comp != 0) {
			return comp;
		}
		
		comp = Integer.compare(this.totalBillableHours, other.totalBillableHours);
		if (comp != 0) {
			return comp;
		}
		
		comp = Integer.compare(this.getTotalNonBillableHours(), other.totalNonBillableHours);
		if (comp != 0) {
			return comp;
		}
		return comp;
	}
	
	/**
	 * @param consultantTime adds a Consultant Time object to the TimeCard
	 */
	public void addConsultantTime(ConsultantTime consultantTime) {
		consultingTime.add(consultantTime);
	}
	
	
	/**
	 * @param clientName 
	 * @return list of consulting time for each consultant
	 */
	public List<ConsultantTime> getBillableHoursForClient(String clientName) {
		ArrayList<ConsultantTime> clientConsultingTime = new ArrayList<>();
		
		for (ConsultantTime accountConsultTime : consultingTime) {
			if (accountConsultTime.account.getName().equals(clientName)) {				
				clientConsultingTime.add(accountConsultTime);
			}
		}	
		
		return clientConsultingTime;
	}
	
	
	/**
	 * @return list of consulting hours for each consultant
	 */
	public List<ConsultantTime> getConsultingHours() {
		return consultingTime;
	}
	
	
	/**
	 * @return integer for total billable hours for each consultant's week
	 */
	public int getTotalBillableHours() {
		int billableHours = 0;
		
		for (ConsultantTime consultingHours : consultingTime) {
			if (consultingHours.isBillable() == true) {
				billableHours += consultingHours.hours;
			}
		}
		totalBillableHours = billableHours;
		return billableHours;
	}
	
	
	/**
	 * @return an integer representing the total non-billable hours
	 */
	public int getTotalNonBillableHours() {
		int nonBillableHours = 0;
		
		for (ConsultantTime nonConsultingHours : consultingTime) {
			if (nonConsultingHours.isBillable() == false) {
				nonBillableHours += nonConsultingHours.hours;
			}
		}
		totalNonBillableHours = nonBillableHours;
		return nonBillableHours;
	}
	
	
	/**
	 * @return an integer representing total hours that include billable and non-billable
	 */
	public int getTotalHours() {
		totalHours = getTotalBillableHours() + getTotalNonBillableHours();
		return totalHours;
	}
	
	
	/**
	 * @return full report of the consultant time card
	 */
	public String toReportString() {
		StringBuilder build = new StringBuilder();
		
		build.append(DIVIDE);	
		build.append(toString());
		build.append("Billable Time: \n");
		build.append(HEADERS);
		
		for (ConsultantTime consultingHours : consultingTime) {
			if (consultingHours.isBillable() == true) {
				build.append(String.format(STRING_FMT, consultingHours.getAccount().getName(), 
						consultingHours.getDate(), consultingHours.getHours(), consultingHours.getSkillType()));
			}
		}
		
		build.append("\n");
		build.append("Non-Billable Time: \n");	
		build.append(HEADERS);
		
		for (ConsultantTime nonConsultingHours: consultingTime) {
			if (nonConsultingHours.isBillable() == false) {				
				build.append(String.format(STRING_FMT, nonConsultingHours.getAccount().getName(), 
						nonConsultingHours.getDate(), nonConsultingHours.getHours(),
						nonConsultingHours.getSkillType()));
			}
		}
		build.append("\n");
		
		String fmtSummary = ("Summary: %nTotal Billable: %32s %nTotal Non-Billable: %2$28s %n"
				+ "Total Hours: %3$35s %n%4$s");
		build.append(String.format(fmtSummary, getTotalBillableHours(), getTotalNonBillableHours(),
				getTotalHours(), DIVIDE));
		
		return build.toString();
	}
	
	/**
	 * @Overrides toString for the TimeCard object
	 */
	public String toString() {
		StringBuilder buildString = new StringBuilder();
		
		buildString.append(String.format(NAME_DATE_FMT, consultant.getNameConsultant(),
				"Week Starting: ", weekStartingDay)+ "\n");

		return buildString.toString();
	}
	

	/**
	 * Getters and Setters
	 */
	public Consultant getConsultant() {
		return consultant;
	}
	public Date getWeekStartingDay() {
		return weekStartingDay;
	}
	public List<ConsultantTime> getConsultingTime() {
		return consultingTime;
	}
}
