package com.scg.test;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.scg.domain.ClientAccount;
import com.scg.domain.Consultant;
import com.scg.domain.ConsultantTime;
import com.scg.domain.NonBillableAccount;
import com.scg.domain.Skill;
import com.scg.domain.TimeCard;
import com.scg.util.Address;
import com.scg.util.Name;
import com.scg.util.StateCode;

/**
 * Junit Test class to test TimeCard Class by testing its notable methods
 * @author craigrainey
 */
public class TimeCardTest {
	
	/**Creates a temporary client account representing Google	 */
	Address testAddress = new Address("1313 DisneyLand Dr", "Anaheim", StateCode.CA, "92802");
	ClientAccount googleAccount = new ClientAccount("Google", new Name("Pichai", "Sundar"), testAddress);
	
	/** Creates a new Consultant for testing purposes	 */
	Consultant consultant = new Consultant(new Name("Moul", "Russ", "NMN"));

	
	/**
	 * Creates a calendar and date
	 */
	Calendar calendar = Calendar.getInstance();
	Date currentDate = calendar.getTime();
	
	/**
	 * Test the methods of the TimeCard class, specifically the return values on TotalBillableHours,
	 * TotalNonBillableHours, and TotalHours
	 */
	@Test
	public void test() {
		TimeCard testCard = new TimeCard(consultant, currentDate);
		
        testCard.addConsultantTime(new ConsultantTime(currentDate, googleAccount,
                Skill.SOFTWARE_ENGINEER, 8));
        calendar.add(Calendar.DATE, 1);
        currentDate = calendar.getTime();
        testCard.addConsultantTime(new ConsultantTime(currentDate, googleAccount,
                Skill.SOFTWARE_ENGINEER, 8));   
        calendar.add(Calendar.DATE, 1);
        testCard.addConsultantTime(new ConsultantTime(currentDate, NonBillableAccount.BUSINESS_DEVELOPMENT,
        		Skill.SOFTWARE_ENGINEER, 8));

        assertEquals(testCard.getTotalBillableHours(), 16);
        assertEquals(testCard.getTotalNonBillableHours(), 8);
        assertEquals(testCard.getTotalHours(), 24);
        
	}

}
