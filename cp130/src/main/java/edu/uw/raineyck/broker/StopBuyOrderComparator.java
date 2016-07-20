package edu.uw.raineyck.broker;

import java.util.Comparator;
import edu.uw.ext.framework.order.StopBuyOrder;

/**
 * Comparator which places the orders in ascending order - the highest priced
 * order is last
 * 
 * @author craigrainey
 *
 */
public class StopBuyOrderComparator implements Comparator<edu.uw.ext.framework.order.StopBuyOrder> {

	/** Constructor */
	public StopBuyOrderComparator() {
	}

	/**
	 * Performs the comparison
	 * 
	 * @param order1
	 * @param order2
	 * @return order1 and order2 in correct order
	 */
	@Override
	public int compare(StopBuyOrder order1, StopBuyOrder order2) {
		final int BEFORE = -1;
		final int EQUAL = 0;
		final int AFTER = 1;

		if (order1 == order2) {
			if (order1.getNumberOfShares() == order2.getNumberOfShares()) {
				if (order1.getOrderId() > order2.getOrderId())
					return AFTER;
				if (order1.getOrderId() < order2.getOrderId())
					return BEFORE;
			}
			if (order1.getNumberOfShares() > order2.getNumberOfShares())
				return AFTER;
			if (order1.getNumberOfShares() < order2.getNumberOfShares())
				return BEFORE;
		}

		if (order1.getPrice() > order2.getPrice())
			return AFTER;
		if (order1.getPrice() < order2.getPrice())
			return BEFORE;

		return EQUAL;
	}

}
