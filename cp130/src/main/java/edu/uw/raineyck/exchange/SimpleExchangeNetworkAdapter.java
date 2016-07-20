package edu.uw.raineyck.exchange;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uw.ext.framework.exchange.ExchangeAdapter;
import edu.uw.ext.framework.exchange.ExchangeEvent;
import edu.uw.ext.framework.exchange.ExchangeListener;
import edu.uw.ext.framework.exchange.StockExchange;

/**
 * Provides a network interface to an exchange
 * 
 * @author craigrainey
 */
public class SimpleExchangeNetworkAdapter implements ExchangeAdapter, ExchangeListener {

	/** Stock Exchange */
	private StockExchange stockExchange;

	/** Multicast Port */
	private int multicastPort;

	/** Datagram Socket */
	private DatagramSocket dataSocket;

	/** Address to connect with */
	private InetAddress address;

	/** Custom protocol for closed event */
	private final String CLOSED_EVNT = ProtocolConstants.CLOSED_EVNT;

	/** Custom protocol for open event */
	private final String OPEN_EVNT = ProtocolConstants.OPEN_EVNT;

	/** Custom protocol for price change event */
	private final String PRICE_CHANGE_EVNT = ProtocolConstants.PRICE_CHANGE_EVNT;

	/** Executor to launch new threads */
	private ExecutorService executor = Executors.newCachedThreadPool();

	/** Command Listener */
	private CommandListener cmdListener;

	/** Class Logger */
	private final Logger log = LoggerFactory.getLogger(SimpleExchangeNetworkAdapter.class);

	/** Constructor */
	public SimpleExchangeNetworkAdapter() {
	}

	/**
	 * Constructor
	 * 
	 * @param exchange
	 *            - the exchange used to service the network request
	 * @param multicastIP
	 *            =-the ip address used to propagate price changes
	 * @param multicastPort
	 *            - the ip port used to propagate price changes
	 * @param commandPort
	 *            - the ports for listening for commands
	 * 
	 * @throws UnknownHostException
	 *             - if unable to resolve multicast IP address
	 * @throws SocketException
	 *             - if an error occurs on a socket operation
	 */
	public SimpleExchangeNetworkAdapter(StockExchange exchange, String multicastIP, int multicastPort,
			int commandPort) {
		this.stockExchange = exchange;
		this.multicastPort = multicastPort;

		/*
		 * Creates a new command listener upon instantiation
		 */
		this.cmdListener = new CommandListener(commandPort, stockExchange);
		executor.execute(cmdListener); // Executors.newSingleThreadExecutor().execute(cmdListener);
		log.info("Command listener up and running");
		exchange.addExchangeListener(this);

		try {
			address = InetAddress.getByName(multicastIP);
			dataSocket = new DatagramSocket();
		} catch (UnknownHostException u) {
			System.out.println("Error connecting Inet Address from Network Adapter");
			u.printStackTrace();
		} catch (SocketException e) {
			log.error("Socket error from Network Adapter");
			e.printStackTrace();
		}
	}

	/**
	 * The exchange has opened and prices are adjusting - add listener to
	 * receive price change events from the exchange and multicast them to
	 * brokers
	 * 
	 * @param event
	 *            - the event
	 */
	@Override
	public void exchangeOpened(ExchangeEvent event) {
		log.info("open Evnt");
		try {
			byte[] message = OPEN_EVNT.getBytes();
			DatagramPacket openEvntPacket = new DatagramPacket(message, message.length, address, multicastPort);
			dataSocket.send(openEvntPacket);
		} catch (IOException ex) {
			log.error("Error sending open event text from Network Adapter");
			ex.printStackTrace();
		}
	}

	/**
	 * The exchange has closed - notify clients and remove price change
	 * listener.
	 * 
	 * @param event
	 *            - the event
	 */
	@Override
	public void exchangeClosed(ExchangeEvent event) {
		log.info("Closed evnt");
		try {
			byte[] closedMsg = CLOSED_EVNT.getBytes();
			DatagramPacket closedEvntPacket = new DatagramPacket(closedMsg, closedMsg.length, address, multicastPort);
			dataSocket.send(closedEvntPacket);
		} catch (IOException ex) {
			log.error("Error sending closed event text from Network Adapter");
			ex.printStackTrace();
		}
	}

	/**
	 * Process price change events
	 * 
	 * @param event
	 *            - the price change event
	 */
	@Override
	public void priceChanged(ExchangeEvent event) {
		try {
			// Can also use string builder -- will look cleaner
			String priceChng = PRICE_CHANGE_EVNT + ProtocolConstants.ELEMENT_DELIMITER + event.getTicker()
					+ ProtocolConstants.ELEMENT_DELIMITER + event.getPrice();
			byte[] priceChngMsg = priceChng.getBytes();
			DatagramPacket priceChngPacket = new DatagramPacket(priceChngMsg, priceChngMsg.length, address,
					multicastPort);
			dataSocket.send(priceChngPacket);
		} catch (IOException ex) {
			log.error("Error sending price change event text from Network Adapter");
			ex.printStackTrace();
		}
	}

	/**
	 * Close the adapter
	 */
	@Override
	public void close() {
		stockExchange.removeExchangeListener(this);
		cmdListener.terminate();
		dataSocket.close();
	}

}
