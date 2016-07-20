package edu.uw.raineyck.broker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uw.ext.framework.account.Account;
import edu.uw.ext.framework.account.AccountException;
import edu.uw.ext.framework.account.AccountManager;
import edu.uw.ext.framework.broker.OrderProcessor;
import edu.uw.ext.framework.exchange.StockExchange;
import edu.uw.ext.framework.order.MarketBuyOrder;
import edu.uw.ext.framework.order.Order;
import edu.uw.ext.framework.order.StopBuyOrder;
import edu.uw.ext.framework.order.StopSellOrder;

/**
 * OrderProcessor implementation that executes orders through the broker
 * 
 * @author craigrainey
 *
 */
public class StockTraderOrderProcessor implements OrderProcessor {

	/** Account manager */
	private AccountManager manager;

	/** Stock Exchange */
	private StockExchange stockExchange;

	/** Logger */
	private static final Logger logger = LoggerFactory.getLogger(StockTraderOrderProcessor.class);

	/**
	 * Constructor
	 */
	public StockTraderOrderProcessor(AccountManager acctMngr, StockExchange exchange) {
		this.manager = acctMngr;
		this.stockExchange = exchange;
	}

	/**
	 * Executes the order using the exchange
	 */
	@Override
	public void process(Order order) {
		stockExchange.executeTrade(order);
		int executionPrice = stockExchange.getQuote(order.getStockTicker()).getPrice();

		try {
			Account targetAcct = manager.getAccount(order.getAccountId());
			targetAcct.registerAccountManager(manager);
			targetAcct.reflectOrder(order, executionPrice);
		} catch (AccountException e) {
			logger.warn("AccountException retrieving account " + order.getOrderId());
			e.printStackTrace();
		}
		if (order.getClass().equals(StopBuyOrder.class)) {
			logger.info("Dispatched Stop Buy Order: " + order.getOrderId() + " symbol: " + order.getStockTicker()
					+ " shares: " + order.getNumberOfShares());
		} else if (order.getClass().equals(MarketBuyOrder.class)) {
			logger.info("Dispatched Market Buy Order: " + order.getOrderId() + " symbol: " + order.getStockTicker()
					+ " shares: " + order.getNumberOfShares());
		} else if (order.getClass().equals(StopSellOrder.class)) {
			logger.info("Dispatched Stop Sell Order: " + order.getOrderId() + " symbol: " + order.getStockTicker()
					+ " shares: " + order.getNumberOfShares());
		} else {
			logger.info("Dispatched Stop Sell Order: " + order.getOrderId() + " symbol: " + order.getStockTicker()
					+ " shares: " + order.getNumberOfShares());
		}
	}
}
