package edu.uw.raineyck.broker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uw.ext.framework.broker.DispatchFilter;
import edu.uw.ext.framework.order.StopSellOrder;

/**
 * Dispatch filter that dispatches any orders having a price above the current
 * market price
 * 
 * @author craigrainey
 *
 */
public class StopSellOrderDispatchFilter implements DispatchFilter<Integer, edu.uw.ext.framework.order.StopSellOrder> {

	/** Class logger */
	private static final Logger logger = LoggerFactory.getLogger(StopSellOrderDispatchFilter.class);

	/** Constructor */
	public StopSellOrderDispatchFilter() {
	}

	/**
	 * test the provided order against the threshold. If order price is above
	 * market value, then returns true to indicate the order is to be
	 * dispatched, and false if it will not be.
	 * 
	 * @param order
	 *            - the order to be tested for dispatch
	 * @return - true if the order price is above or equal the threshold
	 */
	@Override
	public boolean checkDispatch(Integer threshold, StopSellOrder order) {
		logger.info("Checking " + order.getOrderId() + " order price: " + order.getPrice() + " target: " + threshold);
		if (order.getPrice() >= threshold) {
			return true;
		}
		return false;
	}
}
