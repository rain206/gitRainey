package edu.uw.raineyck.broker;

import edu.uw.ext.framework.broker.DispatchFilter;
import edu.uw.ext.framework.order.Order;

/**
 * Dispatch filter that dispatches orders as long as the market is open. The
 * threshold object will be a Boolean object indicating the state of the market
 * 
 * @author craigrainey
 *
 */
public class MarketDispatchFilter implements DispatchFilter<Boolean, Order> {

	/** Constructor */
	public MarketDispatchFilter() {
	}

	/**
	 * Test if the order may be dispatched, all orders are dispatchable if the
	 * market is open
	 * 
	 * @param order
	 *            - the order to be tested for dispatch
	 * @return: true if the market is open.
	 */
	@Override
	public boolean checkDispatch(Boolean threshold, Order order) {
		return threshold;
	}

}
