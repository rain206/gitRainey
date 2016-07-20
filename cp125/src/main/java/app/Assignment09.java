package app;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scg.cmd.AddClientCommand;
import com.scg.cmd.AddConsultantCommand;
import com.scg.cmd.AddTimeCardCommand;
import com.scg.cmd.DisconnectCommand;
import com.scg.cmd.ShutdownCommand;
import com.scg.domain.ClientAccount;
import com.scg.domain.Consultant;
import com.scg.domain.Invoice;
import com.scg.domain.TimeCard;
import com.scg.net.client.InvoiceClient;
import com.scg.persistent.DbServer;
import com.scg.util.Address;
import com.scg.util.DateRange;
import com.scg.util.ListFactory;
import com.scg.util.Name;
import com.scg.util.StateCode;
import com.scg.util.TimeCardListUtil;

/**
 * Assignment 08 application.
 */
public final class Assignment09 {
    /** The invoice month. */
    private static final int INVOICE_MONTH = Calendar.MARCH;

    /** The test year. */
    private static final int INVOICE_YEAR = 2006;

    /** This class' logger. */
    private static final Logger log = LoggerFactory.getLogger("Assignment07");
    
    /**  ArrayLists for clients */
    private static ArrayList<ClientAccount> clients = new ArrayList<ClientAccount>();
    
    /** Lists of time cards     */
    private static ArrayList<TimeCard> timeCards = new ArrayList<TimeCard>();
    
    /**   Consultants  */
    private static ArrayList<Consultant> consultants = new ArrayList<Consultant>();
    
    /**  Port   */
	private final static int PORT = 10888;
	
	/** Local host */
	private static final String LOCALHOST = "127.0.0.1";
	
	

    /**Private constructor to prevent instantiation */
    private Assignment09() {
    }

    
    /**
     * Main method to drive cp125. Creates an instance of assignment 07 and of the derby server
     * being accessed. Then retrieves the client account list from the server, gets the invoice
     * and prints them for each Client Account.
     * 
     * @param args
     */
    public static void main(String[] args) {
    	Assignment09 assign = new Assignment09();
    	ListFactory.populateLists(clients, consultants, timeCards);
    	consultants.clear();
    	
    	try {
    		
    		/*
    		 * Start 3 clients that send time cards to the server
    		 */
    		InvoiceClient client = new InvoiceClient(LOCALHOST, PORT, timeCards);
    		InvoiceClient client2 = new InvoiceClient(LOCALHOST, PORT, timeCards);
    		InvoiceClient client3 = new InvoiceClient(LOCALHOST, PORT, timeCards);
    		InvoiceClient client4 = new InvoiceClient(LOCALHOST, PORT, timeCards);
    		
    		client.start();
    		client2.start();
    		client3.start();
    		client4.start();
    		
    		client.join();
    		client2.join();
    		client3.join();
    		client4.join();
    		
//    		send quit command
            InvoiceClient.sendShutdown(LOCALHOST, PORT);
    		
    	} catch (IOException e) {
    		e.printStackTrace();
    	} catch (InterruptedException i) {
    		i.printStackTrace();
    	}
    }
    
    
    
//    /**
//     * Create invoices for the clients from the timecards.
//     *
//     * @param accounts the accounts to create the invoices for
//     * @param timeCards the time cards to create the invoices from
//     *
//     * @return the created invoices
//     */
//    private static List<Invoice> createInvoices(final List<ClientAccount> accounts,
//                                            final List<TimeCard> timeCards) {
//        final List<Invoice> invoices = new ArrayList<Invoice>();
//
//        final List<TimeCard> timeCardList = TimeCardListUtil
//                .getTimeCardsForDateRange(timeCards, new DateRange(INVOICE_MONTH, INVOICE_YEAR));
//        for (final ClientAccount account : accounts) {
//            final Invoice invoice = new Invoice(account, INVOICE_MONTH, INVOICE_YEAR);
//            invoices.add(invoice);
//            for (final TimeCard currentTimeCard : timeCardList) {
//                invoice.extractLineItems(currentTimeCard);
//            }
//        }
//
//        return invoices;
//    }

//    /**
//     * Print the invoice to a PrintStream.
//     *
//     * @param invoices the invoices to print
//     * @param out The output stream; can be System.out or a text file.
//     */
//    private static void printInvoices(final List<Invoice> invoices, final PrintStream out) {
//        for (final Invoice invoice : invoices) {
//            out.println(invoice.toReportString());
//        }
//    }
    
//    /**
//     * Deserialize the client and time card list compiled in InitList to then put into
//     * respective arrays
//     */
//    public void deSerializeList() {
//    	
//    	/*  deSerialize the client list */
//    	FileInputStream fileIn;
//    	
//    	try {
//    		fileIn = new FileInputStream("ClientList.ser");
//    		ObjectInputStream in = new ObjectInputStream(fileIn);
//    		clients = (ArrayList) in.readObject();
//    		in.close();
//    	} catch (IOException e) {
//    		e.printStackTrace();
//    	} catch (ClassNotFoundException c) {
//    		c.printStackTrace();
//    	}
//    	
//    	/*  deSerialize the timeCard list*/
//    	try {
//    		fileIn = new FileInputStream("TimeCardList.ser");
//    		ObjectInputStream in = new ObjectInputStream(fileIn);
//    		timeCards = (ArrayList) in.readObject();
//    		in.close();
//    	} catch (IOException e) {
//    		e.printStackTrace();
//    	} catch (ClassNotFoundException c) {
//    		c.printStackTrace();
//    	}
//    }

}
