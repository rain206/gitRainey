package edu.uw.raineyck.exchange;

import edu.uw.ext.framework.exchange.ExchangeAdapter;
import edu.uw.ext.framework.exchange.NetworkExchangeAdapterFactory;
import edu.uw.ext.framework.exchange.StockExchange;

/**
 * A NetworkExchangeAdapterFactory implementation for creating
 * ExchangeNetworkAdapter instances
 * 
 * @author craigrainey
 *
 */
public class SimpleExchangeNetworkAdapterFactory implements NetworkExchangeAdapterFactory {

	/** Constructor */
	public SimpleExchangeNetworkAdapterFactory() {
	}

	/**
	 * Instantiates and returns a new ExchangeNetworkAdapter
	 * 
	 * @param exchange
	 *            - exchange to be used (real exchange)
	 * @param multicastIP
	 *            - the multicast IP address used to distribute events
	 * @param multicastPort
	 *            - port used to distribute events
	 * @param commandPort
	 *            - the listening port to be used to accept command requests
	 * 
	 * @return a new ExchangeNetworkAdapter, or null if instantiation fails
	 */
	@Override
	public ExchangeAdapter newAdapter(StockExchange exchange, String multicastIP, int multicastPort, int commandPort) {
		SimpleExchangeNetworkAdapter newExchangeAdapter = new SimpleExchangeNetworkAdapter(exchange, multicastIP,
				multicastPort, commandPort);
		// exchange.addExchangeListener(newExchangeAdapter);
		return newExchangeAdapter;
	}
}
