package edu.uw.raineyck.broker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uw.ext.framework.broker.OrderManager;
import edu.uw.ext.framework.broker.OrderProcessor;
import edu.uw.ext.framework.order.StopBuyOrder;
import edu.uw.ext.framework.order.StopSellOrder;

/**
 * Maintains queues to different types of orders and request the execution of
 * orders when price conditions allow their execution
 * 
 * @author craigrainey
 *
 */
public class SimpleOrderManager implements OrderManager {

	/** Stop buy order queue */
	protected SimpleOrderQueue<Integer, StopBuyOrder> stopBuyOrderQueue;

	/** Stop Sell Order Queue */
	protected SimpleOrderQueue<Integer, StopSellOrder> stopSellOrderQueue;

	/** Stop Buy Order Dispatch Filter */
	private final StopBuyOrderDispatchFilter BUY_ORDER_FILTER = new StopBuyOrderDispatchFilter();

	/** Stop Buy Order Comparator */
	private final StopBuyOrderComparator BUY_ORDER_COMPARATOR = new StopBuyOrderComparator();

	/** Stop SellOrder Dispatch Filter */
	private final StopSellOrderDispatchFilter SELL_ORDER_FILTER = new StopSellOrderDispatchFilter();

	/** Stop Sell Order Comparator */
	private final StopSellOrderComparator SELL_ORDER_COMPARATOR = new StopSellOrderComparator();

	/** Stock ticker symbol */
	private String stockSymbol;

	/** Stock price */
	private int stockPrice;

	/** Class logger */
	private static final Logger logger = LoggerFactory.getLogger(SimpleOrderManager.class);

	/**
	 * Constructor to be used by sub-classes to finish intialization
	 * 
	 * @param stockTickerSymbol
	 *            - ticker symol of the stock this instance manages orders for
	 */
	protected SimpleOrderManager(String stockTickerSymbol) {
		this.stockSymbol = stockTickerSymbol;
		setupQueues(0); // Sends 0 since price is not set.
	}

	/**
	 * Constructor
	 * 
	 * @param stockTickerSymbol
	 *            - ticker symbol of the stock this instance manages orders for
	 * @param price
	 *            - current price of stock symbol to be managed
	 */
	public SimpleOrderManager(String stockTickerSymbol, int price) {
		this.stockSymbol = stockTickerSymbol;
		this.stockPrice = price;
		setupQueues(price);
	}

	/**
	 * Setup the Queues with the correct comparators
	 * 
	 * @param priceThreshold
	 */
	private void setupQueues(int priceThreshold) {
		stopBuyOrderQueue = new SimpleOrderQueue<Integer, StopBuyOrder>(priceThreshold, BUY_ORDER_FILTER,
				BUY_ORDER_COMPARATOR);

		stopSellOrderQueue = new SimpleOrderQueue<Integer, StopSellOrder>(priceThreshold, SELL_ORDER_FILTER,
				SELL_ORDER_COMPARATOR);
	}

	/**
	 * Getters for stock symbol
	 */
	@Override
	public String getSymbol() {
		return stockSymbol;
	}

	/**
	 * Respond to a stock price adjustment by setting threshold on dispatch
	 * filters
	 * 
	 * @param price
	 *            - the new price
	 */
	@Override
	public void adjustPrice(int price) {
		logger.info(String.format("Adjust stock price to %d from %d", price, stockPrice));
		stockPrice = price;

		stopBuyOrderQueue.setThreshold(price);
		stopSellOrderQueue.setThreshold(price);
	}

	/**
	 * Queue a stop buy order
	 * 
	 * @param order
	 *            - the order to be queued
	 */
	@Override
	public void queueOrder(StopBuyOrder order) {
		stopBuyOrderQueue.enqueue(order);
	}

	/**
	 * Queue a stop sell order
	 * 
	 * @param order
	 *            - the order to be queued
	 */
	@Override
	public void queueOrder(StopSellOrder order) {
		stopSellOrderQueue.enqueue(order);
	}

	/**
	 * Registers the processor to be used during processing. This will be passed
	 * on to the order queues as the dispatch callback
	 * 
	 * @param processor
	 *            - the callback to be registered
	 */
	@Override
	public void setOrderProcessor(OrderProcessor processor) {
		stopBuyOrderQueue.setOrderProcessor(processor);
		stopSellOrderQueue.setOrderProcessor(processor);
	}

}
