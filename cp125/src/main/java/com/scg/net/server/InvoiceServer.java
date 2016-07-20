package com.scg.net.server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.scg.domain.ClientAccount;
import com.scg.domain.Consultant;

/**
 * The server for creation of account invoices based on time cards sent from the client
 * 
 * @author craigrainey
 *
 */
public class InvoiceServer {
	
	/**	Int to represent port */
	private int port;
	
	private ServerSocket servSocket;
	
	/**	List of clients */
	private List<ClientAccount> clientList = new ArrayList<>();
	
	/**	List of consultants */
	private List<Consultant> consultantList = new ArrayList<>();
	
	/** Command Processor or Receiver */
	private CommandProcessor rcvr;
	
	/** Socket */
	private Socket socket;
	
    /** The class' logger. */
    private static final Logger logger =
                         LoggerFactory.getLogger(InvoiceServer.class);
	
	/**
	 * Construct an InvoiceServer with a port
	 * 
	 * @param port
	 * @param clientList
	 * @param consultantList
	 */
	public InvoiceServer(int port, List<ClientAccount> clientList, 
			List<Consultant> consultantList) {
		this.port = port;
		this.clientList = clientList;
		this.consultantList = consultantList;
	}
	
	/**
	 * Runs this server, establishing connections, receiving commands, and sending them to
	 * CommandProcessor
	 * 
	 */
	public void run() {
		int processorNumber = 0;
		try {
			System.out.println("Waiting for client on port: " +port);
			
			servSocket = new ServerSocket(port);
			System.out.println("Server ready on socket " +port);
			
			while (!servSocket.isClosed()) {
				processorNumber++;
				socket = servSocket.accept();
				logger.info("Server just connected to: " +socket.getRemoteSocketAddress());
				
				String cmdName = "CommandProcessor_" +processorNumber;
				final CommandProcessor cmdProc = new CommandProcessor(socket, cmdName, clientList, consultantList, this);
				Thread cmdThread = new Thread(cmdProc, "CommandProcessor_");
				cmdThread.start();
			}
			
		} catch (IOException e) {
			logger.warn("Error running server: " +e);
		}
	}
	
	/**
	 * Shut down this server
	 */
	void shutdown() {
		try {
            if (!servSocket.isClosed()) {
                servSocket.close();
                logger.info("Succesfully shutdown server!");
            }
		} catch (IOException e) {
			System.err.println("Error shutting down: " +e);
		}	
	}
	
	/**
	 * Getter and setters
	 */
	public Socket getSocket() {
		return socket;
	}
	public void setServerSocket(ServerSocket serverSocket) {
		this.servSocket = serverSocket;
	}
	public CommandProcessor getReceiver() {
		return rcvr;
	}
	public void setReceiver(CommandProcessor rcvr) {
		this.rcvr = rcvr;
	}
}
