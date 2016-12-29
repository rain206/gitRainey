package edu.uw.raineyck.broker;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uw.ext.framework.account.Account;
import edu.uw.ext.framework.account.AccountException;
import edu.uw.ext.framework.account.AccountManager;
import edu.uw.ext.framework.broker.Broker;
import edu.uw.ext.framework.broker.BrokerException;
import edu.uw.ext.framework.broker.OrderManager;
import edu.uw.ext.framework.broker.OrderProcessor;
import edu.uw.ext.framework.exchange.ExchangeEvent;
import edu.uw.ext.framework.exchange.ExchangeListener;
import edu.uw.ext.framework.exchange.StockExchange;
import edu.uw.ext.framework.exchange.StockQuote;
import edu.uw.ext.framework.order.MarketBuyOrder;
import edu.uw.ext.framework.order.MarketSellOrder;
import edu.uw.ext.framework.order.Order;
import edu.uw.ext.framework.order.StopBuyOrder;
import edu.uw.ext.framework.order.StopSellOrder;

/**
 * An implementation of the Broker interface, provides a full implementation
 * less the creation of the order manager and market queue
 * 
 * @author craigrainey
 *
 */
public class SimpleBroker implements Broker, ExchangeListener {

	/** Account Manager */
	private AccountManager acctManager;

	/** Name */
	private String name;

	/** Stock Exchange */
	private StockExchange exchange;

	/** Order Manager Map */
	private Map<String, SimpleOrderManager> orderManagerMap = new HashMap<String, SimpleOrderManager>();

	/** Market Order Queue */
	protected SimpleOrderQueue<Boolean, Order> marketOrders;

	/** Class Logger */
	private static final Logger logger = LoggerFactory.getLogger(SimpleBroker.class);

	/**
	 * Constructor
	 * 
	 * @param brokerName
	 *            - name of broker
	 * @param acctMgr
	 *            - the stock exchange to be used by the broker
	 * @param exchg
	 *            - the account manager to be used by the broker
	 */
	public SimpleBroker(String brokerName, AccountManager acctMgr, StockExchange exchg) {
		this.name = brokerName;
		this.acctManager = acctMgr;
		this.exchange = exchg;
		initializeOrderManagers();
		marketOrders = new SimpleOrderQueue<Boolean, Order>(exchange.isOpen(), new MarketDispatchFilter());
		StockTraderOrderProcessor marketProcessor = new StockTraderOrderProcessor(acctManager, exchange);
		marketOrders.setOrderProcessor(marketProcessor);
		exchange.addExchangeListener(this);
	}

	/**
	 * Constructor for sub classes
	 * 
	 * @param brokerName
	 * @param exchg
	 * @param acctMgr
	 */
	protected SimpleBroker(String brokerName, StockExchange exchg, AccountManager acctMgr) {
		this.name = brokerName;
		this.exchange = exchg;
		this.acctManager = acctMgr;
		marketOrders = new SimpleOrderQueue<Boolean, Order>(exchange.isOpen(), new MarketDispatchFilter());
		StockTraderOrderProcessor marketProcessor = new StockTraderOrderProcessor(acctManager, exchange);
		marketOrders.setOrderProcessor(marketProcessor);
		initializeOrderManagers();
		exchange.addExchangeListener(this);
	}

	/**
	 * @return - name of broker
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * Fetch the stock list from the exchange and initialize an order manager
	 * for each stock
	 */
	protected void initializeOrderManagers() {
		OrderProcessor orderProcessor = new StockTraderOrderProcessor(acctManager, exchange);
		String[] listOfStocks = exchange.getTickers();
		for (String tickers : listOfStocks) {
			/*
			 * Stuck
			 */
			int price = exchange.getQuote(tickers).getPrice();
			SimpleOrderManager tickerManager = new SimpleOrderManager(tickers, price);
			tickerManager.setOrderProcessor(orderProcessor);
			orderManagerMap.put(tickers, tickerManager);
		}
		logger.info("Broker intialized order managers");
	}

	/**
	 * Create an appropriate OrderManager
	 * 
	 * @param ticker
	 *            - the ticket symbol of the stock
	 * @param initialPrice
	 *            - current price of the stock
	 * @return a new OrderManager for the specified stock
	 */
	protected OrderManager createOrderManager(String ticker, int initialPrice) {
		SimpleOrderManager newOrderManager = new SimpleOrderManager(ticker, initialPrice);
		return newOrderManager;
	}

	/**
	 * Retrieves the appropriate stock manager for that stock and has them
	 * adjust the price when a price change event occurs.
	 * 
	 * @param event
	 *            - the price change event
	 */
	public void priceChanged(ExchangeEvent event) {
		OrderManager manager = orderManagerMap.get(event.getTicker());
		logger.info("Attempted to adjust stock price for: " + event.getTicker());
		if (manager != null) {
			manager.adjustPrice(event.getPrice());
		}
	}

	/**
	 * Upon the exchange opening sets the market dispatch filter threshold and
	 * processes any available orders
	 * 
	 * @param event
	 *            - the exchange (open) event
	 */
	public void exchangeOpened(ExchangeEvent event) {
		marketOrders.setThreshold(Boolean.TRUE);
	}

	/**
	 * Upon the exchange opening sets the market dispatch threshold
	 * 
	 * @param event
	 *            - the exhange (closed) event
	 */
	public void exchangeClosed(ExchangeEvent event) {
		marketOrders.setThreshold(Boolean.FALSE);
	}
	

	/**
	 * Create an interface broker
	 * 
	 * @param username
	 *            - the user or account name for the account
	 * @param password
	 *            - the password for the new account
	 * @param balance
	 *            - the initial account balance in cents
	 * @return - the new account
	 * @throws BrokerException
	 *             if unable to create account
	 */
	@Override
	public Account createAccount(String username, String password, int balance) throws BrokerException {
		try {
			Account newAccount = acctManager.createAccount(username, password, balance);
			if (newAccount != null) {
				newAccount.registerAccountManager(acctManager);
				return newAccount;
			} else {
				return null;
			}
		} catch (AccountException e) {
			logger.warn("Broker was unable to create account: " + username);
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Delete an account with the broker
	 * 
	 * @param username
	 *            - user or account name for the account to delete
	 * @throws BrokerException
	 *             if unable to delete account
	 */
	@Override
	public void deleteAccount(String username) throws BrokerException {
		try {
			acctManager.deleteAccount(username);
		} catch (AccountException a) {
			logger.warn("Broker was unable to delete account: " + username);
		}
	}

	/**
	 * Locate an account with the broker. The username and password are first
	 * verified and the account is returned
	 * 
	 * @param username
	 *            - user or account name for the account
	 * @param password
	 *            - password for the account
	 * @return the account
	 * @throws BrokerException
	 *             if username and password are invalid
	 */
	@Override
	public Account getAccount(String username, String password) throws BrokerException {
		logger.info("Attempting to retrieve account: " + username);
		try {
			boolean proceed = acctManager.validateLogin(username, password);
			if (proceed == true) {
				Account targetAcct = acctManager.getAccount(username);
				String success = String.format("Retrieved account: %s and balance: %d", username,
						targetAcct.getBalance());
				logger.info(success);
				return targetAcct;
			} else {
				logger.warn("Username and/or password provided to broker is not valid");
				throw new BrokerException();
			}
		} catch (AccountException a) {
			String warn = String.format("AccountException retrieving account %s through Broker", username);
			logger.warn(warn);
			throw new BrokerException();
		}
	}

	/**
	 * Returns the Stock quote for a specific stock
	 * 
	 * @param ticker
	 *            - symbol for the target stock
	 * @return - stock quote for that stock
	 * @throws BrokerException
	 *             - if unable to obtain quote
	 */
	@Override
	public StockQuote requestQuote(String ticker) throws BrokerException {
		StockQuote quote = exchange.getQuote(ticker);
		if (quote == null) {
			throw new BrokerException("Broker is unable to obtain stock quote");
		}
		return quote;
	}

	/**
	 * Place a buy order with the broker
	 * 
	 * @param order
	 *            - the order being placed with the broker
	 */
	@Override
	public void placeOrder(MarketBuyOrder order) throws BrokerException {
		logger.info("Placing MarketBuyOrder: " + order.getOrderId());
		marketOrders.enqueue(order);
	}

	/**
	 * Place a sell order with the broker
	 * 
	 * @param order
	 *            - the order being placed to sell with the broker
	 */
	@Override
	public void placeOrder(MarketSellOrder order) throws BrokerException {
		logger.info("Placing MarketSellOrder: " + order.getOrderId());
		marketOrders.enqueue(order);
	}

	/**
	 * Stops buy order
	 */
	@Override
	public void placeOrder(StopBuyOrder order) throws BrokerException {
		String orderReport = String.format("Placing stop buy order: %d price: %d", order.getOrderId(),
				order.getPrice());
		logger.info(orderReport);
		SimpleOrderManager targetManager = orderManagerMap.get(order.getStockTicker());
		targetManager.queueOrder(order);
	}

	/**
	 * Stop sell order
	 */
	@Override
	public void placeOrder(StopSellOrder order) throws BrokerException {
		String orderReport = String.format("Placing stop sell order: %d price: %d", order.getOrderId(),
				order.getPrice());
		logger.info(orderReport);
		SimpleOrderManager targetManager = orderManagerMap.get(order.getStockTicker());
		targetManager.queueOrder(order);
	}

	/**
	 * Released broker resources
	 * 
	 * @throws BrokerException
	 *             if the operation fails
	 */
	@Override
	public void close() throws BrokerException {
		try {
			exchange.removeExchangeListener(this);
			acctManager.close();
			orderManagerMap = null;
		} catch (AccountException a) {
			throw new BrokerException();
		}
	}

	// Check is broker variable are not null
	// private void checkInvariants() {
	// //if name, acctmanager, stock, ordermanager, market order == null
	// }

}
