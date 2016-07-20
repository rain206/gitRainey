package edu.uw.raineyck.broker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uw.ext.framework.broker.DispatchFilter;
import edu.uw.ext.framework.order.StopBuyOrder;

/**
 * Dispatch filter that dispatches any orders having a price below the current
 * market price (threshold)
 * 
 * @author craigrainey
 *
 */
public class StopBuyOrderDispatchFilter implements DispatchFilter<Integer, edu.uw.ext.framework.order.StopBuyOrder> {

	/** Class logger */
	private static final Logger logger = LoggerFactory.getLogger(StopBuyOrderDispatchFilter.class);

	/** Constructor */
	public StopBuyOrderDispatchFilter() {
	}

	/**
	 * Test the provided order against the threshold. If order's price is below
	 * the current market price then returns true to indicate dispatch, false to
	 * indicate order is not to be dispatched.
	 * 
	 * @param order
	 *            - the order to be tested for dispatch
	 * @return true if the order price is below or equal the threshold
	 */
	@Override
	public boolean checkDispatch(Integer threshold, StopBuyOrder order) {
		logger.info("Checking " + order.getOrderId() + " order price: " + order.getPrice() + " target: " + threshold);
		if (order.getPrice() <= threshold) {
			return true;
		}
		return false;
	}

}
