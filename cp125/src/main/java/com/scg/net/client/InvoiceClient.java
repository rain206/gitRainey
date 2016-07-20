package com.scg.net.client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.scg.cmd.AddClientCommand;
import com.scg.cmd.AddConsultantCommand;
import com.scg.cmd.AddTimeCardCommand;
import com.scg.cmd.CreateInvoiceCommand;
import com.scg.cmd.DisconnectCommand;
import com.scg.domain.ClientAccount;
import com.scg.domain.Consultant;
import com.scg.domain.TimeCard;
import com.scg.cmd.ShutdownCommand;

/**
 * The client of the invoice server
 * @author craigrainey
 *
 */
public class InvoiceClient extends Thread implements Runnable {
	
	/** Socket */
	private Socket socket;
	
	/**	List of time cards */
	private List<TimeCard> timeCards = new ArrayList<>();
	
	/** List of consultants */
	private List<Consultant> consultants = new ArrayList<>();
	
	/** List of client accounts */
	private List<ClientAccount> clients = new ArrayList<>();
	
	/** Output stream*/
	private OutputStream outputStream;
	
	/** Object output stream */
	private ObjectOutputStream output;
	
	/**	Max priority */
	private final int MAX_PRIORITY = 10;
	
	/**	Min priority */
	private final int MIN_PRIORITY = 1;
	
	/**	Normal priority */
	private final int NORM_PRIORITY = 5;
	
	/** Host */
	private String host;
	
	/** Port */
	private int port;
	
    /** The invoice month. */
    private static final int INVOICE_MONTH = Calendar.MARCH;

    /** The invoice year. */
    private static final int INVOICE_YEAR = 2006;
	
	/**
	 * Construct an InvoiceClient with a host and port to the server
	 * @param host
	 * @param port
	 * @param timeCardList
	 */
	public InvoiceClient(String servAddress, int port, List<TimeCard> timeCardList) throws IOException {	
		this.host = servAddress;
		this.port = port;
		this.timeCards = timeCardList;
	}
	
	
	/**
	 * Send command to the server to create invoices for the specified month
	 * @param out object output stream
	 * @param month of the invoice
	 * @param year of the invoice
	 */
	public void createInvoices(ObjectOutputStream out, int month, int year) {
		try {

			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.MONTH, month);
			calendar.set(Calendar.YEAR, year);
			Date invoiceDate = calendar.getTime();
			
			CreateInvoiceCommand invoiceCommand = new CreateInvoiceCommand(invoiceDate);
			out.writeObject(invoiceCommand);
			System.out.println("Invoice command send to server");
		} catch (IOException e) {
			System.err.println("Error sending create invoice command: " +e);
		}

	}
	
	
	/**	Runs this invoiceClient, sending clients, consultants, and timeCards to the server, then 
	 * sending commands to create invoices for a specified month */
	public void run() {
		System.out.println("Running: new Client server");
        ObjectOutputStream out = null;
		
	       try (Socket server = new Socket(host, port);) {
	            System.out.println(String.format("Connected to server at: %s/%s:%d",
	                    server.getInetAddress().getHostName(),
	                    server.getInetAddress().getHostAddress(), server.getPort()));
	            // We don't expect to get any input so shut it down.
	            server.shutdownInput();
	            out = new ObjectOutputStream(server.getOutputStream());
	            sendTimeCards(out);
	            sendDisconnect(out);
//	            server.shutdownInput();
		} catch (IOException i) {
			System.err.println("Client IO exception: " +i);
		}
		System.out.println("Client thread exiting");
	}

	
	
	/**	Send clients to the server */
	public void sendClients(ObjectOutputStream out){
		System.out.println("Sending clients to server...");
		try {
			for (ClientAccount sendClient : clients) {
				AddClientCommand clientCommand = new AddClientCommand(sendClient);
				System.out.println("Sending client " +sendClient.getName());
				output.writeObject(clientCommand);
			}
			System.out.println("Clients sent to server");
		} catch (IOException e) {
			System.err.println("Error sending clients to server" +e);
		}
	}
	
	
	/**	Send consultants to the server */
	public void sendConsultants(ObjectOutputStream out) {
		System.out.println("Sending consultants to server...");
		try {
			for (Consultant consultantSending : consultants) {
				out.writeObject(new AddConsultantCommand(consultantSending));
				System.out.println("Send consultant: " +consultantSending.getNameConsultant());
			}
			System.out.println("Consultants sent to server");
		} catch (IOException e) {
			System.err.println("Error sending Consultants to server " +e);
		}
	}
	
	
	/**	Send disconnect to the server */
	public void sendDisconnect(ObjectOutputStream out) {
		try {
			DisconnectCommand disconnect = new DisconnectCommand();
			out.writeObject(disconnect);
		} catch (IOException e) {
			System.err.println("Error sending disconnect cmd from server " +e);
		}
	}
	
	
	/**	Send shutdown to the server */
	public static void sendShutdown(String host, int port) {
		try {
			Socket servSocket = new Socket(host, port);
			ObjectOutputStream shutOut = new ObjectOutputStream(servSocket.getOutputStream());
			servSocket.shutdownInput();
			ShutdownCommand shutdown = new ShutdownCommand();
			shutOut.writeObject(shutdown);
		} catch (IOException e) {
			System.err.println("Error sending shutting down cmd command: " +e);
		}
	}
	

	/**	Send the time cards to the server */
	public void sendTimeCards(ObjectOutputStream out) {	
		try {
			for (TimeCard cards : timeCards) {
				AddTimeCardCommand sendCard = new AddTimeCardCommand(cards);
				out.writeObject(sendCard);
			}
		} catch (IOException e) {
			System.err.println("Error sending time cards to server: " +e);
		}
	}
	
	/**
	 * Getters and setters
	 */
	public Socket getSocket() {
		return socket;
	}
	public void setConsultants(List<Consultant> consultants) {
		this.consultants = consultants;
	}
	public void setClients(List<ClientAccount> clientList) {
		this.clients = clientList;
	}
	public void setTimeCardList(List<TimeCard> timeCardList) {
		this.timeCards = timeCardList;
	}
	public OutputStream getOutputStream() {
		return outputStream;
	}
	public ObjectOutputStream getObjectOutputStream() {
		return output;
	}
}
