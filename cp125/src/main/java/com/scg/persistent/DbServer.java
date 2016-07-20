package com.scg.persistent;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scg.domain.ClientAccount;
import com.scg.domain.Consultant;
import com.scg.domain.ConsultantTime;
import com.scg.domain.Invoice;
import com.scg.domain.InvoiceLineItem;
import com.scg.domain.NonBillableAccount;
import com.scg.domain.Skill;
import com.scg.domain.TimeCard;
import com.scg.util.Address;
import com.scg.util.DateRange;
import com.scg.util.Name;
import com.scg.util.StateCode;

/**
 * Responsible for providing a programmatic interface to store and access objects in a database
 * 
 * @author craigrainey
 *
 */
public class DbServer {
	
	
	/** Invoice line items */
	private List<InvoiceLineItem> invoiceLines;
	
	/**	String representing the database URL */
	private String dbUrl;
	
	/**	String representing the username */
	private String username;
	
	/**	String representing the password */
	private String password;
	
	/** Connection for server */
	private Connection conn;
	
	/** Prepared SQL statement */
	private PreparedStatement psmt;

	/** Integer for Client ID	*/
	private int clientId;
	
	/** Integer for TimeCard ID	 */
	private int timeCardId;
	
	/** Date Range Object	 */
	private DateRange dateRange;
	
	/** Simple Date Format	 */
	private DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
	
	/** Logger for class	 */
	private static final Logger log = LoggerFactory.getLogger(DbServer.class);
	
	/**	SQL query to add a client to the server */
	private final String ADD_CLIENT_SQL = "INSERT INTO CLIENTS (name, street, city, state, postal_code,"
											+ " contact_last_name, contact_first_name, contact_middle_name)"
											+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	
	/**	SQL query to add a consultant to the server */
	private final String ADD_CONSULTANT_SQL = "INSERT INTO CONSULTANTS (last_name, first_name, middle_name)"
										+ " VALUES (?, ?, ?)";
	
	/**	SQL query to select a consultant from the server */
	private final String SELECT_CONSULT_ID_SQL =  "SELECT ID"
													+"	FROM CONSULTANTS"
													+"	WHERE LAST_NAME = ?"
													+"	AND FIRST_NAME = ?"
													+"	AND MIDDLE_NAME = ?";

	/**	SQL query to insert a non-billable hours object to server */
	private final String NON_BILLABLE_HOURS_SQL = "INSERT INTO NON_BILLABLE_HOURS (account_name, timecard_id, date, hours)"
													+ " VALUES (?, ?, ?, ?)";
	
	/**	SQL query to intert a billable-hours object to the server */
	private final String BILLABLE_HOURS_SQL = "INSERT INTO BILLABLE_HOURS (client_id, timecard_id, date, skill, hours)"
												+ " VALUES (?, ?, ?, ?, ?)";	

	/**	SQL query to inter a consultant into time cards table*/
	private final String INSERT_TIMECARD_CONSULTANT_SQL = "INSERT INTO TIMECARDS (consultant_id, start_date)"
												+ " VALUES (?, ?)";
	
	/**	SQL query to select an ID from time card with certain start date */
	private final String SELECT_TIMECARD_ID = "SELECT ID"
												+ " FROM TIMECARDS"
												+ " WHERE START_DATE = ?";
	
	/**	SQL query to select all clients in the CLIENTS table */
	private final String SELECT_ALL_CLIENTS = "SELECT name, street, city, state, postal_code, contact_last_name, contact_first_name, contact_middle_name"
												+"	FROM CLIENTS";
	
	/**	SQL query to select all consultants in the CONSULTANTS table */
	private final String SELECT_ALL_CONSULTANTS = "SELECT LAST_NAME, FIRST_NAME, MIDDLE_NAME"
													+" FROM CONSULTANTS";
	

	
	/**	Constructor for DbServer */
	public DbServer(String dbUrl, String username, String password) {
		this.dbUrl = dbUrl;
		this.username = username;
		this.password = password;
		
		try {
			conn = DriverManager.getConnection(dbUrl, username, password);
		} catch (SQLException s) {
			s.printStackTrace();
		}
	}
	
	
	/**
	 * Add a client to the database
	 * 
	 * @param client is a client account object
	 */
	public void addClient(ClientAccount client) throws SQLException, ClassNotFoundException{
		try {
			
			psmt = conn.prepareStatement(ADD_CLIENT_SQL);
			psmt.setString(1, client.getName());
			psmt.setString(2, client.getAddress().getStreetNumber());
			psmt.setString(3, client.getAddress().getCity());
			String state = client.getAddress().getState().toString();
			psmt.setString(4, state);
			psmt.setString(5, client.getAddress().getPostalCode());
			psmt.setString(6, client.getContact().getFirstName());
			psmt.setString(7, client.getContact().getLastName());
			psmt.setString(8, client.getContact().getMiddleName());		
			psmt.executeUpdate();
			
		} catch (SQLException sql) {
			sql.printStackTrace();
		}
	}
	
	
	/**
	 * Add a consultant to the database
	 * 
	 * @param consultant object
	 */
	public void addConsultant(Consultant consultant) throws SQLException{
		try {
			
			psmt = conn.prepareStatement(ADD_CONSULTANT_SQL);
			psmt.setString(1, consultant.getNameConsultant().getLastName());
			psmt.setString(2, consultant.getNameConsultant().getFirstName());
			psmt.setString(3, consultant.getNameConsultant().getMiddleName());
			psmt.executeUpdate();
			
		} catch (SQLException sql) {
			sql.printStackTrace();
		}
	}
	
	/**
	 * Add a Time Card to the database
	 * 
	 * @param timeCard is a timeCard object
	 */
	public void addTimeCard(TimeCard timeCard) throws SQLException{	

		String startDate = format.format(timeCard.getWeekStartingDay());
		try {
			Name name = timeCard.getConsultant().getNameConsultant();
			psmt = conn.prepareStatement(SELECT_CONSULT_ID_SQL);
			psmt.setString(1, name.getLastName());
			psmt.setString(2, name.getFirstName());
			psmt.setString(3, name.getMiddleName());
			
			ResultSet rs = psmt.executeQuery();
			while(rs.next()) {
				int consultantId = rs.getInt("ID");	
				psmt = conn.prepareStatement(INSERT_TIMECARD_CONSULTANT_SQL);
				psmt.setInt(1, consultantId);
				psmt.setDate(2, java.sql.Date.valueOf(startDate));
				psmt.execute();
			}
			
			for (ConsultantTime time : timeCard.getConsultingTime()) {

				String consultDate = format.format(time.getDate());

				psmt = conn.prepareStatement(SELECT_TIMECARD_ID);
				psmt.setDate(1, java.sql.Date.valueOf(startDate));
				rs = psmt.executeQuery();
				while(rs.next()) {
					timeCardId = rs.getInt("ID");
				}

				psmt = conn.prepareStatement("SELECT ID"
						+" FROM CLIENTS"
						+" WHERE NAME = '" +time.getAccount().getName()+ "' ");
				rs = psmt.executeQuery();
				while(rs.next()) {
					clientId = rs.getInt("ID");
				}

				psmt = conn.prepareStatement("SELECT NAME"
						+" FROM SKILLS"
						+" WHERE RATE = " +time.getSkillType().getRate());
				rs = psmt.executeQuery();
				while(rs.next()) {
					if (time.isBillable() == true) {
						psmt = conn.prepareStatement(BILLABLE_HOURS_SQL);
						psmt.setInt(1, clientId);
						psmt.setInt(2, timeCardId);
						psmt.setDate(3, java.sql.Date.valueOf(consultDate));
						psmt.setString(4, rs.getString("NAME"));
						psmt.setInt(5, time.getHours());
						psmt.executeUpdate();
					} else {
						psmt = conn.prepareStatement(NON_BILLABLE_HOURS_SQL);
						psmt.setString(1, time.getAccount().getName().toString());
						psmt.setInt(2, timeCardId);
						psmt.setDate(3, java.sql.Date.valueOf(consultDate));
						psmt.setInt(4, time.getHours());
						psmt.executeUpdate();
					}
				}
			}
		} catch (SQLException s) {
			s.printStackTrace();
		}
	}

	
	
	/**
	 * Get all of the clients in the database
	 * 
	 * @return
	 */
	public List<ClientAccount> getClients() throws SQLException{
		/**	List of Client Account objects */
		List<ClientAccount> clientList = new ArrayList<>();
		
		Statement stmt = conn.createStatement();
		String query = SELECT_ALL_CLIENTS;
		
		ResultSet rs = stmt.executeQuery(query);
		while(rs.next()) {
			String name = rs.getString("name");
			String street = rs.getString("street");
			String city = rs.getString("city");
			String state = rs.getString("state");
			String postalCode = rs.getString("postal_code");
			String contactLast = rs.getString("contact_last_name");
			String contactFirst = rs.getString("contact_first_name");
			String contactMiddle = rs.getString("contact_middle_name");
			
			Address tempAddress = new Address(street, city, StateCode.valueOf(state), postalCode);
			Name clientName = new Name(contactLast, contactFirst, contactMiddle);
			
			ClientAccount tempClient = new ClientAccount(name, clientName, tempAddress);
			clientList.add(tempClient);
		}
		return clientList;
	}
	
	/**
	 * Get all of the consultants in the database
	 * 
	 * @return
	 */
	public List<Consultant> getConsultants() throws SQLException{
		List<Consultant> consultants = new ArrayList<>();
		
		Statement stmt = conn.createStatement();
		String query = SELECT_ALL_CONSULTANTS;
		
		ResultSet rs = stmt.executeQuery(query);
		while(rs.next()) {
			String lastName = rs.getString("LAST_NAME");
			String firstName = rs.getString("FIRST_NAME");
			String middleName = rs.getString("MIDDLE_NAME");
			
			Consultant tempCons = new Consultant(new Name(lastName, firstName, middleName));
			consultants.add(tempCons);
		}
		return consultants;
	}
	
	/**
	 * Gets clients monthly invoice 
	 *
	 * @param client the invoice is for
	 * @param month the invoice is from 
	 * @param year of the invoice
	 * @return the invoice object
	 */
	public Invoice getInvoice(ClientAccount client, int month, int year) throws SQLException{
		Invoice clientInvoice = new Invoice(client, month, year);
		dateRange = new DateRange(month, year);
		String startDate = format.format(dateRange.getStartDate());
		String endDate = format.format(dateRange.getEndDate());
		
		try {
			Statement stmt = conn.createStatement();
			String query = "SELECT b.date, c.last_name, c.first_name, c.middle_name, b.skill, s.rate, b.hours "
							+ " FROM billable_hours b, consultants c, skills s, timecards t "
							+ " WHERE b.client_id = (SELECT DISTINCT id "
							+ "		FROM clients "
							+ "		WHERE name = '" +client.getName()+ "') "
									+ "AND b.skill = s.name "
									+ "AND b.timecard_id = t.id "
									+ "AND c.id = t.consultant_id "
									+ "AND b.date >= '" +java.sql.Date.valueOf(startDate)+ "' "
									+ "AND b.date <= '" +java.sql.Date.valueOf(endDate)+ "' ";
			
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				String lastName = rs.getString("last_name");
				String firstName = rs.getString("first_name");
				String middle = rs.getString("middle_name");
				Date date = rs.getDate("date");
				String skill = rs.getString("skill");
				Skill skillType = Skill.valueOf(skill);
				int hours = rs.getInt("hours");

				
				Name consultantName = new Name(lastName, firstName, middle);
				InvoiceLineItem lineItem = new InvoiceLineItem(date, new Consultant(consultantName), 
						skillType, hours);
				clientInvoice.addLineItems(lineItem);
			}	
		} catch (SQLException s) {
			s.printStackTrace();
		}
		System.out.println(clientInvoice.toReportString());
		return clientInvoice;
	}
}
