package edu.uw.raineyck.exchange;

import edu.uw.ext.framework.exchange.NetworkExchangeProxyFactory;
import edu.uw.ext.framework.exchange.StockExchange;

/**
 * A factory interface for creating ExchangeNetworkProxy instances
 * 
 * @author craigrainey
 *
 */
public class ExchangeNetworkProxyFactory implements NetworkExchangeProxyFactory {

	/** Constructor */
	public ExchangeNetworkProxyFactory() {
	}

	/**
	 * Instantiates a new ExchangeNetwork Proxy
	 * 
	 * @param multicastIP
	 *            - the multicast IP address used to distribute events
	 * @param multicastPort
	 *            - the port used to distribute events
	 * @param commandIP
	 *            - the exchange host
	 * @param commandPort
	 *            - the listening port to be used to accept command requests
	 * 
	 * @return a newly instantiated ExchangeNetworkProxy
	 */
	@Override
	public StockExchange newProxy(String multicastIP, int multicastPort, String commandIP, int commandPort) {
		ExchangeNetworkProxy newNetworkProxy = new ExchangeNetworkProxy(multicastIP, multicastPort, commandIP,
				commandPort);
		return newNetworkProxy;
	}

}
