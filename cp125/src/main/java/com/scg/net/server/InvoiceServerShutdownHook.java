package com.scg.net.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scg.domain.ClientAccount;
import com.scg.domain.Consultant;

/**
 * ShutdownHook for the invoice server
 * 
 * @author craigrainey
 *
 */
public class InvoiceServerShutdownHook extends Thread implements Runnable {

	/** List of clients */
	private final List<ClientAccount> clients;
	
	/** List of consultants */
	private final List<Consultant> consultants;
	
	/** Wait time for interupptions in thread sleep */
	private final int WAIT_TIME = 1000;
	
	/** Seconds to wait for interruption before shutdown */
	private final int SHUTDOWN_DLAY_SECONDS = 10;
	
	/** Logger */
	private Logger logger = LoggerFactory.getLogger(InvoiceServerShutdownHook.class);

	/**
	 * Construct an InvoiceServerShutdownHook
	 * 
	 * @param clientList list of clients
	 * @param consultantList list of consultants
	 */
	public InvoiceServerShutdownHook(List<ClientAccount> clientList, List<Consultant> consultantList) {
		this.clients = clientList;
		this.consultants = consultantList;
	}
	
	/**
	 * Called by the Runtime when a shutdown signal is received
	 */
	public void run() {
		logger.info("Running server shutdown hook...");
		try {
			for (int i = SHUTDOWN_DLAY_SECONDS; i > 0 ; i--) {
				logger.info("Countdown to ending run: " +i);
				Thread.sleep(WAIT_TIME);
			}
		} catch(InterruptedException e) {
			logger.warn("Interrupted thread " +e);
		}
		
		try {
			File file = new File("Clients.txt");
			FileOutputStream fileOut = new FileOutputStream(file);
			PrintStream out = new PrintStream(fileOut);
			for (ClientAccount printClient : clients) {
				out.println(printClient);
			}
			
			file = new File("ConsultantList.txt");
			fileOut = new FileOutputStream(file);
			out = new PrintStream(fileOut);
			for (Consultant printConsult : consultants) {
				out.println(printConsult);
			}
			out.close();
			
			logger.info("Printed clients and consultants");
			
		} catch (FileNotFoundException f) {
			logger.warn("File not found " +f);
		}
		logger.info("Thread run is over.");
	}

}
