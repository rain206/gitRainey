package com.scg.domain;

import java.util.Date;

import com.scg.util.Address;

/**
 * Header for Small Consulting Group invoices
 * @author craigrainey
 *
 */
public final class InvoiceHeader implements java.io.Serializable {

	/** Serial UID	 */
	private static final long serialVersionUID = -2219646121086103061L;

	/**	Invoicing business' name */
	private String businessName;
	
	/**	Address of invoicing business */
	private Address businessAddress;
	
	/**	Client this invoice is for */
	private ClientAccount client;
	
	/**	Date of the invoice */
	private Date invoiceDate;
	
	/**	Month of invoice */
	private Date invoiceForMonth;
	
	
	/**	Constructor to create instances of InvoiceHeader */
	public InvoiceHeader(String businessName, Address businessAddress, ClientAccount client,
			Date invoiceDate, Date invoiceForMonth) {
		this.businessName = businessName;
		this.businessAddress = businessAddress;
		this.client = client;
		this.invoiceDate = invoiceDate;
		this.invoiceForMonth = invoiceForMonth;
	}
	
	
	/**  Overrides toString to create the header with the given business' name and address */
	@Override
	public String toString() {
		StringBuilder headerBuild = new StringBuilder();
		
		headerBuild.append(businessName+ "\n");
		headerBuild.append(businessAddress.getStreetNumber()+ " \n");
		headerBuild.append(businessAddress.getCity()+ ", " +businessAddress.getState()+ " " 
				+businessAddress.getPostalCode()+ "\n");
		
		return headerBuild.toString();
	}

}
