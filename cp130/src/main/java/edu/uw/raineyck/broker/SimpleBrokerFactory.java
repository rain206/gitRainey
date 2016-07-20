package edu.uw.raineyck.broker;

import edu.uw.ext.framework.account.AccountManager;
import edu.uw.ext.framework.broker.Broker;
import edu.uw.ext.framework.broker.BrokerFactory;
import edu.uw.ext.framework.exchange.StockExchange;

/**
 * BrokerFactory implementation that returns a SimpleBroker
 * 
 * @author craigrainey
 *
 */
public class SimpleBrokerFactory implements BrokerFactory {

	/** Constructor */
	public SimpleBrokerFactory() {
	}

	/**
	 * Instantiates a new SimpleBroker
	 * 
	 * @param name
	 *            - the broker's name
	 * @param acctMngr
	 *            - the account manager to be used by the broker
	 * @param exch
	 *            - the exchange to be used by the broker
	 * @return a new created simple broker
	 */
	@Override
	public Broker newBroker(String name, AccountManager acctMngr, StockExchange exch) {
		SimpleBroker broker = new SimpleBroker(name, acctMngr, exch);
		return broker;
	}

}
