package com.scg.test;

import static org.junit.Assert.*;


import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.scg.domain.ClientAccount;
import com.scg.domain.Invoice;
import com.scg.util.Address;
import com.scg.util.Name;
import com.scg.util.StateCode;

public class InvoiceTest {

	ClientAccount testClient = new ClientAccount("Bungie Co",
            new Name("Chief", "Master"),
            new Address("559 106th Ave NE", "Bellevue", StateCode.WA, "98004"));
	
	Invoice testInvoice = new Invoice(testClient, 1, 2016);
	
	Calendar calendar = Calendar.getInstance();
	
	@Test
	public void test() {
		assertEquals(testInvoice.getClientAccount(), testClient);
		assertEquals(testInvoice.getInvoiceMonth(), 01);
		
		assertEquals(testInvoice.getTotalHours(), 0);
		assertEquals(testInvoice.getTotalCharges(), 0);
	}
	
	@Test
	public void testDates() {
		calendar.set(Calendar.YEAR, 2016);
		calendar.set(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
		calendar.set(Calendar.MILLISECOND, calendar.getActualMinimum(Calendar.MILLISECOND));
		Date startDateTest = calendar.getTime();
		
		assertEquals(testInvoice.getStartDate(), startDateTest);
		
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
		calendar.set(Calendar.MILLISECOND, calendar.getActualMaximum(Calendar.MILLISECOND));
		Date endDateTest = calendar.getTime();
		
		assertEquals(testInvoice.getEndDate(), endDateTest);
	}

}
