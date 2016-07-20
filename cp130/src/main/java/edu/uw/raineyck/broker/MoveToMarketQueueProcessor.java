package edu.uw.raineyck.broker;

import edu.uw.ext.framework.broker.OrderProcessor;
import edu.uw.ext.framework.broker.OrderQueue;
import edu.uw.ext.framework.order.Order;

/**
 * Moves order to a broker market order queue
 * 
 * @author craigrainey
 *
 */
public class MoveToMarketQueueProcessor implements OrderProcessor {

	/** Market Queue */
	private OrderQueue<?, Order> marketQueue;

	/** Constructor */
	public MoveToMarketQueueProcessor(edu.uw.ext.framework.broker.OrderQueue<?, Order> newMarketQueue) {
		this.marketQueue = newMarketQueue;
	}

	/**
	 * Enques the order into the market order queue
	 */
	@Override
	public void process(Order order) {
		marketQueue.enqueue(order);
	}

}
