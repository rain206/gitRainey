package com.scg.domain;

/**
 * Creates a footer for the invoice
 * @author craigrainey
 *
 */
public final class InvoiceFooter implements java.io.Serializable {

	/**	Business name that is doing the invoicing */
	final private String businessName;
	
	/**	Page number of the invoice */
	private int pageNumber = 1;
	
	/**	Constructor to create an instance of InvoiceFooter */
	public InvoiceFooter(String businessName) {
		this.businessName = businessName;
	}
	
	/**	Increases page number by 1 when applicable */
	public void incrementPageNumber() {
		pageNumber++;
	}
	
	/**	 Creates the footer and boarder to represnt next page, as well as displays page number */
	public String toString() {
		StringBuilder foot = new StringBuilder();
		foot.append(String.format("%s %2$52s %3$3d %n", businessName, "Page:", pageNumber));
		foot.append("=================================================================================== \n");
		
		return foot.toString();
	}

}
