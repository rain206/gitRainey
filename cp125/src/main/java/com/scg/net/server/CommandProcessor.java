package com.scg.net.server;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scg.cmd.AddClientCommand;
import com.scg.cmd.AddConsultantCommand;
import com.scg.cmd.AddTimeCardCommand;
import com.scg.cmd.Command;
import com.scg.cmd.CreateInvoiceCommand;
import com.scg.cmd.DisconnectCommand;
import com.scg.cmd.ShutdownCommand;
import com.scg.domain.ClientAccount;
import com.scg.domain.Consultant;
import com.scg.domain.Invoice;
import com.scg.domain.TimeCard;
import com.scg.util.DateRange;
import com.scg.util.TimeCardListUtil;

/**
 * The command processor for the invoice server. Implements the receiver role in the Command
 * design pattern
 * 
 * @author craigrainey
 *
 */
public final class CommandProcessor implements Runnable {
	
	/**	Socket connection */
	private Socket connection;
	
	/**	List of Client Accounts*/
	private List<ClientAccount> clientList = new ArrayList<ClientAccount>();
	
	/**	List of Consultants */
	private List<Consultant> consultantList = new ArrayList<Consultant>();
	
	/** List of time cards */
	private List<TimeCard> timeCards = new ArrayList<TimeCard>();
	
	/**	Invoice Server */
	private final InvoiceServer invoiceServer;
	
	/** Output directory */
	private String outputDirectoryName;
	
	/**	File Output Stream */
	private FileOutputStream fileOut;
	
	/**	Print Stream */
	private PrintStream out;
	
	/**	Input stream */
	private InputStream input;
	
	/**	Name of processor */
	private String cmdName;
	
    /** The class' logger. */
    private static final Logger logger =
                         LoggerFactory.getLogger(CommandProcessor.class);
	

	/** Constructor */
	public CommandProcessor(Socket connection, String cmdName, List<ClientAccount> clientList,
			List<Consultant> consultantList, InvoiceServer server) {
		this.connection = connection;
		this.cmdName = cmdName;
		this.clientList = clientList;
		this.consultantList = consultantList;
		this.invoiceServer = server;
	}
	
	
	/**
	 * Runs this CommandProcessor
	 */
	public void run() {
		logger.info(cmdName+ " is running");
		try {
			input = connection.getInputStream();
			ObjectInputStream is = new ObjectInputStream(input);
			
			while(!connection.isClosed()) {
	            final Object obj = is.readObject();
	            if(obj == null) {
	            	break;
	            }
	            if (obj instanceof Command<?>) {
	                final Command<?> command = (Command<?>)obj;
	                logger.info("Received command: "
	                          + command.getClass().getSimpleName());
	                command.setReceiver(this);
	                command.execute();
	                if(command.getClass().equals(ShutdownCommand.class)) {
	                	break;
	                }
	            } else {
	                logger.warn(String.format("Received non command object, %s, discarding.",
	                        obj.getClass().getSimpleName()));
	            }
			}
		} catch (IOException i) {
			logger.warn("IO exception running Command Processor " +i);
		} catch (ClassNotFoundException c) {
			logger.warn("Class not found error in command processor " +c);
		}
	}
	
	/**
	 * Set the output directory name
	 * 
	 * @param outputDirectoryName name of directory
	 */
	public void setOutputDirectoryName(String outputDirectoryName) {
		logger.info("Output directory name set to: " +outputDirectoryName);
		this.outputDirectoryName = outputDirectoryName;
	}
	
	/**
	 * Execute add client command
	 * 
	 * @param command
	 */
	public void execute(AddClientCommand command) {
		final ClientAccount targetAccount = (ClientAccount) command.getTarget();
		synchronized(clientList) {
			if (!clientList.contains(targetAccount)) {
				clientList.add(targetAccount);
			}
		}
		
		clientList.add(command.getClient());
		logger.info("added client account " +targetAccount.getName());
	}
	
	/**
	 * Execute add time card command
	 * 
	 * @param command
	 */
	public void execute(AddTimeCardCommand command) {
		timeCards.add(command.getTimeCard());
		logger.info("Added time cards to receiver: " +command.getTimeCard().getWeekStartingDay());
	}

	/**	
	 * Execute add consultant command 
	 * 
	 * @param command
	 */
	public void execute(AddConsultantCommand command) {
		final Consultant addConsultant = (Consultant) command.getTarget();
		synchronized(consultantList) {
			if (!consultantList.contains(addConsultant)) {
				consultantList.add(addConsultant);
			}
		}
		logger.info("Added consultant to receiver " +addConsultant.getNameConsultant());
	}
	
	/**
	 * Execute create invoice command and print invoices to txt files in the following format:
	 * <ClientName>-<MonthName>.txt to user directory
	 * 
	 * @param command
	 */
	public void execute(CreateInvoiceCommand command) {
		logger.info("Executing create invoice command...");
		Date date = (Date) command.getDate();
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);;
		calendar.set(calendar.YEAR, 2006);
		calendar.getTime();
		
		int year = calendar.YEAR; //Won't set to 2006?
		int month = calendar.MONTH;
	    
	    final List<Invoice> invoices = new ArrayList<Invoice>();
	    
	    final List<TimeCard> timeCardList = TimeCardListUtil
	              .getTimeCardsForDateRange(timeCards, new DateRange(month, 2006));
	    
	    synchronized(clientList) {
		    for (final ClientAccount account : clientList) {
		          final Invoice invoice = new Invoice(account, month, 2006);
		          invoices.add(invoice);
		          for (final TimeCard currentTimeCard : timeCardList) {
		              invoice.extractLineItems(currentTimeCard);
		          }
		     }
		    
		    for (Invoice invoiceList : invoices) {
		    	String monthName = calendar.getDisplayName(Calendar.MONTH, month, Locale.US);
		    	String fileName = invoiceList.getClientAccount().getName()+ "-" +monthName+ 
		    			" " +cmdName;
		    	
		    	try {
		    		fileOut = new FileOutputStream(fileName);
		    		out = new PrintStream(fileOut);
		    		out.print(invoiceList.toReportString());
		    	} catch(FileNotFoundException f) {
		    		logger.warn("File output not found: " +f);
		    	}
		    }
	    }
	}
	
	/**
	 * Execute disconnect command
	 * 
	 * @param command disconnectCommand
	 */
	public void execute(DisconnectCommand command) {
		try {
			connection.close();
		} catch (IOException e) {
			logger.warn("Error disconnecting from socket: " +e);
		}
	}
	
	/**
	 * Execute shut down command. Closes any current connections, stops listening for connections
	 * and then terminates the server, without calling System.exit
	 * 
	 * @param comnand command to shut down
	 */
	public void execute(ShutdownCommand command) {
		logger.info("Sent shutdown command to server...");
		InvoiceServerShutdownHook shutdown = new InvoiceServerShutdownHook(clientList, consultantList);
		Thread shutdownThread = new Thread(shutdown);
		shutdownThread.run();
		invoiceServer.shutdown();
	}
	
	/**
	 * Returns name of Command Processor when called from Abstract Command
	 */
	@Override
	public String toString() {
		return "Command Processor";
	}
}
