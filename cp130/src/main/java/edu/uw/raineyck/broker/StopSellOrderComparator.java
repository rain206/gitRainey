package edu.uw.raineyck.broker;

import java.util.Comparator;
import edu.uw.ext.framework.order.StopSellOrder;

/**
 * Comparator which places the orders in descending order - the highest priced
 * order is the first item in the list.
 * 
 * @author craigrainey
 *
 */
public class StopSellOrderComparator implements Comparator<edu.uw.ext.framework.order.StopSellOrder> {

	/** Constructor */
	public StopSellOrderComparator() {
	}

	/**
	 * Performs the comparison
	 * 
	 * @param order1
	 *            - first of the two orders to be compared
	 * @param order2
	 *            - second of the two orders to be compared
	 * 
	 * @return -1, 0, 1, depending on if the first argument is less than, equal,
	 *         or greater than the second. Where the lesser order has the higher
	 *         price, if prices are equal the lesser order will have the highest
	 *         order quantity, finally if a price and quantity are equal, the
	 *         lesser order will have the lower order id
	 */
	@Override
	public int compare(StopSellOrder order1, StopSellOrder order2) {
		final int BEFORE = -1;
		final int EQUAL = 0;
		final int AFTER = 1;

		if (order1.getPrice() == order2.getPrice()) {
			if (order1.getNumberOfShares() == order2.getNumberOfShares()) {
				if (order1.getOrderId() > order2.getOrderId())
					return BEFORE;
				if (order1.getOrderId() < order2.getOrderId())
					return AFTER;
			}
			if (order1.getNumberOfShares() > order2.getNumberOfShares())
				return BEFORE;
			if (order1.getNumberOfShares() < order2.getNumberOfShares())
				return AFTER;
		}

		if (order1.getPrice() > order2.getPrice())
			return BEFORE;
		if (order1.getPrice() < order2.getPrice())
			return AFTER;

		return EQUAL;
	}

}
