package com.scg.domain;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import com.scg.util.Address;
import com.scg.util.DateRange;
import com.scg.util.StateCode;


/**
 * Creates client invoices for a given time period from time cards that are produced. Invoicing
 * business' name and addressed are retrieved from a properties file, which is declared
 * as PROP_FILE_NAME.
 * @author craigrainey
 */
public final class Invoice implements java.io.Serializable {

	/** Serial Version ID	 */
	private static final long serialVersionUID = -3673962383826298057L;

	/**	File name of properties file with business' name and address */
	private final String PROP_FILE_NAME = "invoice.properties";

	/**	City of invoicing business */
	private String BUSINESS_CITY_PROP;
	
	/**	Name of invoicing business */
	private String BUSINESS_NAME_PROP;
	
	/**	State of invoicing business */
	private String BUSINESS_STATE_PROP;
	
	/**	State code (abbreviation) of invoicing business' location */
	private StateCode stateCode;
	
	/**	Street address of invoicing business*/
	private String BUSINESS_STREET_PROP;
	
	/**	Zipcode of invoicing business */
	private String BUSINESS_ZIP_PROP;
	
	/**	String to use if address information is not applicable */
	private final String NA = "";
	
	/**	Client account of Time card */
	private ClientAccount clientAccount;
	
	/**	Month of invoice in 0-month format */
	private int invoiceMonth;
	
	/**	Year of invoice represented as int */
	private int invoiceYear;
	
	/**	Date of invoice */
	private Date invoiceDate;
	
	/** Date Range of each Invoice */
	private final DateRange dateRange;
	
	private Date startDate;
	
	private Date endDate;
	
	/**	Int representing total billable hours */
	private int totalBillableHours;
	
	/**	 Int representing total charges*/
	private int totalCharges;
	
	/**	ArrayList containing invoice line items extracted from time cards */
	private ArrayList<InvoiceLineItem> invoiceLines = new ArrayList<>();
	
	/**	Formatting string */
	final private String FMT = "%s %2$17s %3$25s %4$21s %5$9s %n";
	
	/**	 2nd Formatting string*/
	final private String FMT2 = "%1$tm/%1$td/%1$-5tY %2$-30s %3$9s	%4$6d %5$11s %n";
	
	/**	String formatter to represent metrics being accounted for */
	final private String METRICS = String.format(FMT, "Date", "Consultant", "Skill", "Hours", "Charge \n");
	
	/**	 Dashes for invoice*/
	final private String DASHES = "----------  -----------------------------  ------------------    -----  ---------- \n";

	
	/**
	 * Constructor for the Invoice class to create various instances while also extracting
	 * invoicing business' address from a properties file
	 * @param client
	 * @param invoiceMonth
	 * @param invoiceYear
	 */
	//Check static block
	public Invoice(ClientAccount client, int invoiceMonth, int invoiceYear) {
		this.clientAccount = client;
		this.invoiceMonth = invoiceMonth;
		this.invoiceYear = invoiceYear;
		
		//Use this calendar to get this startDate and endDate of this invoice
		Calendar calendar = Calendar.getInstance();
		this.invoiceDate = calendar.getTime();
		calendar.set(Calendar.YEAR, invoiceYear);
		calendar.set(Calendar.MONTH, invoiceMonth);
		dateRange = new DateRange(invoiceMonth, invoiceYear);
		
		startDate = dateRange.getStartDate();
		endDate = dateRange.getEndDate();
		
		//Extract properties from properties file and set business' address
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		Properties properties = new Properties();
		try(InputStream resourceStream = loader.getResourceAsStream(PROP_FILE_NAME)) {
			File file = new File(PROP_FILE_NAME);
			FileInputStream fileInput = new FileInputStream(file);
			properties.load(fileInput);
			this.BUSINESS_CITY_PROP = properties.getProperty("business.city");
			this.BUSINESS_NAME_PROP = properties.getProperty("business.name");
			this.BUSINESS_STATE_PROP = properties.getProperty("business.state");
			this.BUSINESS_STREET_PROP = properties.getProperty("business.street");
			this.BUSINESS_ZIP_PROP = properties.getProperty("business.zip");
			this.stateCode = StateCode.valueOf(BUSINESS_STATE_PROP);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Retrieve line items from the TimeCards for appropriate business to then put into the 
	 * invoiceList arraylist
	 * @param timeCard
	 */
	public void extractLineItems(TimeCard timeCard) {
		for (ConsultantTime accountConsultTime : timeCard.getConsultingTime()) {
			
			if (accountConsultTime.account.getName().equals(clientAccount.getName())
					&& dateRange.isInRange(accountConsultTime.getDate())) {	
				
				InvoiceLineItem lineItem = new InvoiceLineItem(accountConsultTime.getDate(), 
						timeCard.getConsultant(), accountConsultTime.getSkillType(), accountConsultTime.getHours());
				
				invoiceLines.add(lineItem);
				
			}
		}	
	}
	
	
	/**
	 * @return the client account for this invoice
	 */
	public ClientAccount getClientAccount() {
		return clientAccount;
	}
	
	/**
	 * @return the month this invoice is for
	 */
	public int getInvoiceMonth() {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(dateRange.getStartDate());
		return cal.get(Calendar.MONTH);
	}
	
	
	/**
	 * Retrieves the first Consultant Time object within the invoice Client ArrayList, since this
	 * should have been the first one entered. Then returns the date (which should be start date).
	 * @return start date
	 */
	public Date getStartDate() {
		return startDate;
	}
	
	/**
	 * Gets the last entry's Date in the invoice array list since this should be the date
	 * that represents end date
	 * @return date of last entry
	 */
	public Date getEndDate() {
		return endDate;
	}
	
	/**
	 * Setter for the invoice line items arraylist to use for DbServer
	 * 
	 * @param invoiceLine is an Invoice Line Item
	 */
	public void addLineItems(InvoiceLineItem invoiceLine) {
		invoiceLines.add(invoiceLine);
	}
	
	public ArrayList<InvoiceLineItem> getLineItems() {
		return invoiceLines;
	}
	
	/**
	 * Goes through the array list invoiceClient and records the total hours worked. Then
	 * multiplies the hours by the charge coinciding with the consultant's skill
	 * @return
	 */
	public int getTotalHours() {
		int hours = 0;
		for (InvoiceLineItem consultingCharges : invoiceLines) {
			if (dateRange.isInRange(consultingCharges.getDate())) {
				hours += consultingCharges.getHours();
			}
		}
		return hours;
	}

	
	/**
	 * Multiplies the total hours that was accumulated by the method getTotalHours
	 * and then multiplies them by the rate coinciding with the consultant's skill type
	 * @return
	 */
	public int getTotalCharges() {
		int totalCharges = 0;
		for (InvoiceLineItem consultingTime : invoiceLines) {
			totalCharges += consultingTime.getSkill().getRate() * consultingTime.getHours();
		}
		return totalCharges;
	}
	
	/**
	 * Method to return string containing information on the business this invoice is for. Mostly
	 * address information and contact name
	 * @return
	 */
	public String invoiceInfo() {
		StringBuilder invoiceBuild = new StringBuilder();
		Address bizAddress = new Address(BUSINESS_STREET_PROP, BUSINESS_CITY_PROP,
				stateCode, BUSINESS_ZIP_PROP);
		InvoiceHeader header = new InvoiceHeader(BUSINESS_NAME_PROP, bizAddress, clientAccount, invoiceDate, dateRange.getStartDate());
		Calendar month = Calendar.getInstance();
		
		invoiceBuild.append(header+ "\n");
		invoiceBuild.append(toString());
		month.set(Calendar.MONTH, invoiceMonth);		
		invoiceBuild.append("Invoice for Month of: " +month.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())+ 
				" " +invoiceYear+ "\n");
		invoiceBuild.append("Invoice Date: February 03, 2016 \n");
		invoiceBuild.append("\n");

		invoiceBuild.append(METRICS);
		invoiceBuild.append(DASHES);
		return invoiceBuild.toString();
	}
	
	
	/**
	 * Overrides toString to create a string representation of this invoice's
	 * client name and billing start date
	 */
	@Override
	public String toString() {
		StringBuilder buildString = new StringBuilder();
		
		buildString.append("Invoice for: \n");
		buildString.append(clientAccount.getName()+ "\n");
		buildString.append(clientAccount.getAddress().getStreetNumber()+ "\n");
		buildString.append(clientAccount.getAddress().getCity()+", ");
		buildString.append(clientAccount.getAddress().getState()+ " ");
		buildString.append(clientAccount.getAddress().getPostalCode()+ "\n");
		buildString.append(clientAccount.getContact().getLastName()+ ", ");
		buildString.append(clientAccount.getContact().getFirstName()+ " ");
		buildString.append(clientAccount.getContact().getMiddleName()+ "\n");
		buildString.append("\n");
		
		return buildString.toString();
	}
	
	/**
	 * Formatted string that will return the printable invoice. This includes a header and footer
	 * on each page
	 * @return String
	 */
	public String toReportString() {
		StringBuilder build = new StringBuilder();
		
		InvoiceFooter footer = new InvoiceFooter(BUSINESS_NAME_PROP);
		String pattern = "###,##0.00";
		DecimalFormat decimalFormat = new DecimalFormat(pattern);

		build.append(invoiceInfo());

		int lines = 1;
		for (InvoiceLineItem consultantLine : invoiceLines) {
			
			double charge = 0;
			charge += consultantLine.getSkill().getRate() * consultantLine.getHours();
			totalCharges += charge;
			totalBillableHours += consultantLine.getHours();
			build.append(String.format(FMT2, consultantLine.getDate(), consultantLine.getConsultant(),
					consultantLine.getSkill(), consultantLine.getHours(), 
					decimalFormat.format(charge)));
			lines++;
			
			if (lines > 5) {
				lines = 1;
				build.append("\n" +footer+ "\n");
				footer.incrementPageNumber();
				build.append(invoiceInfo());
			}
		}
		build.append("\n");
		
		build.append(String.format("%s: %2$63d %3$11s %n", "Total", totalBillableHours, decimalFormat.format(totalCharges)));
		build.append("\n" +footer);
		return build.toString();
	}
}
