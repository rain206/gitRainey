package edu.uw.raineyck.exchange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.event.EventListenerList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uw.ext.framework.exchange.ExchangeListener;
import edu.uw.ext.framework.exchange.StockExchange;
import edu.uw.ext.framework.exchange.StockQuote;
import edu.uw.ext.framework.order.BuyOrder;
import edu.uw.ext.framework.order.Order;

/**
 * Client for interacting with a network accessible exchange. This
 * SocketExchange methods encode the method request as a string, per
 * ProtocolConstants, and send the command to the ExchangeNetworkAdapter,
 * receive the response decode it and return the result.
 * 
 * @author craigrainey
 *
 */
public class ExchangeNetworkProxy implements StockExchange {

	/** Command IP Address */
	private String commandIPAddress;

	/** Command Port */
	private int commandPort;

	/** Cmd Socket */
	private Socket cmdServer;

	/** Print Writer */
	private PrintWriter printWtr;

	/** Buffered Reader */
	private BufferedReader reader;

	/** Event Listener List */
	private EventListenerList listenerList = new EventListenerList();

	/** Executor to launch new threads */
	private ExecutorService executor = Executors.newCachedThreadPool();

	/** Logger */
	private Logger log = LoggerFactory.getLogger(ExchangeNetworkProxy.class);

	/** Simple Constructor */
	public ExchangeNetworkProxy() {
	}

	/**
	 * Constructor
	 * 
	 * @param eventIPAddress
	 *            - the multicast IP address to connect with
	 * @param eventPort
	 *            - the multicast port to connect with
	 * @param cmdIPAddress
	 *            - the address the exchange accepts request on
	 * @param cmdPort
	 *            - the address the exchange accepts request on
	 */
	public ExchangeNetworkProxy(String eventIPAddress, int eventPort, String cmdIPAddress, int cmdPort) {
		this.commandIPAddress = cmdIPAddress;
		this.commandPort = cmdPort;

		/*
		 * Creates a Net Event Processor and begins running it on a separate
		 * thread
		 */
		NetEventProcessor eventProcessor = new NetEventProcessor(eventIPAddress, eventPort);
		executor.execute(eventProcessor);

		log.info("Network Proxy attempting to connect with Command Listener");
		try {
			cmdServer = new Socket(commandIPAddress, commandPort);
			log.info("Network proxy connected with command listener");

			printWtr = new PrintWriter(cmdServer.getOutputStream(), true);
			reader = new BufferedReader(new InputStreamReader(cmdServer.getInputStream()));
		} catch (IOException ex) {
			log.warn("Error connecting from Network Proxy");
			ex.printStackTrace();
		}
	}

	/**
	 * The state of the exchange
	 */
	@Override
	public boolean isOpen() {
		printWtr.println(ProtocolConstants.GET_STATE_CMD);

		String response;
		try {
			response = reader.readLine();
			if (response.equals("true")) {
				return true;
			}
		} catch (IOException e) {
			log.warn("Error getting state of exchange from network proxy");
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Gets the ticker symbols for all the stocks traded on the exchange
	 */
	@Override
	public String[] getTickers() {
		printWtr.println(ProtocolConstants.GET_TICKERS_CMD);

		String[] tickerList = null;
		try {
			tickerList = reader.readLine().trim().split(ProtocolConstants.ELEMENT_DELIMITER);
			log.info("Successfully retrieved list of tickers");
		} catch (IOException ex) {
			log.warn("Error receiving//sending tickers from network proxy");
		}

		return tickerList;
	}

	/**
	 * Gets the current price of certain stock by sending command to command
	 * handler and receiving a response.
	 */
	@Override
	public StockQuote getQuote(String ticker) {
		StringBuilder quoteCmd = new StringBuilder();
		quoteCmd.append(ProtocolConstants.GET_QUOTE_CMD);
		quoteCmd.append(ProtocolConstants.ELEMENT_DELIMITER);
		quoteCmd.append(ticker);

		printWtr.println(quoteCmd.toString());

		StockQuote quote = null;
		try {
			int price = Integer.parseInt(reader.readLine().trim());

			if (price >= 0) {
				quote = new StockQuote(ticker, price);
				log.info(String.format("Stock Quote for %s is: %d", ticker, price));
			}
		} catch (IOException ex) {
			log.warn("Error reading quote from command handler");
		}
		return quote;
	}

	/**
	 * Adds a market listener
	 */
	@Override
	public void addExchangeListener(ExchangeListener addListener) {
		listenerList.add(ExchangeListener.class, addListener);
	}

	/**
	 * Removes a market listener
	 */
	@Override
	public void removeExchangeListener(ExchangeListener removeListener) {
		listenerList.remove(ExchangeListener.class, removeListener);
	}

	/**
	 * Creates a command to execute a trade and sends it to the exchange
	 */
	@Override
	public int executeTrade(Order order) {
		log.info("Proxy attempting to execute trade");

		StringBuilder orderCmd = new StringBuilder();
		orderCmd.append(ProtocolConstants.EXECUTE_TRADE_CMD);
		orderCmd.append(ProtocolConstants.ELEMENT_DELIMITER);

		if (order.getClass().equals(BuyOrder.class)) {
			orderCmd.append(ProtocolConstants.BUY_ORDER);
		} else {
			orderCmd.append(ProtocolConstants.SELL_ORDER);
		}

		orderCmd.append(ProtocolConstants.ELEMENT_DELIMITER);
		orderCmd.append(order.getAccountId());
		orderCmd.append(ProtocolConstants.ELEMENT_DELIMITER);
		orderCmd.append(order.getStockTicker());
		orderCmd.append(ProtocolConstants.ELEMENT_DELIMITER);
		orderCmd.append(order.getNumberOfShares());

		printWtr.println(orderCmd.toString());

		int executionPrice = 0;
		try {
			executionPrice = Integer.parseInt(reader.readLine());
		} catch (IOException ex) {
			log.warn("Error executing trade from exchange proxy");
		}
		return executionPrice;
	}
}
