package edu.uw.raineyck.RMIBroker;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uw.ext.framework.account.Account;
import edu.uw.ext.framework.broker.Broker;
import edu.uw.ext.framework.broker.BrokerException;
import edu.uw.ext.framework.exchange.StockQuote;
import edu.uw.ext.framework.order.MarketBuyOrder;
import edu.uw.ext.framework.order.MarketSellOrder;
import edu.uw.ext.framework.order.StopBuyOrder;
import edu.uw.ext.framework.order.StopSellOrder;

/**
 * Remote object that maintains a session with a broker and utilizes the
 * broker's methods. The session will end once called to.
 * 
 * @author craigrainey
 *
 */
public class RemoteBrokerSessionImpl extends UnicastRemoteObject implements RemoteBrokerSession {

	/** Serial Version UID */
	private static final long serialVersionUID = -208295831144611160L;

	/** Broker */
	private Broker broker;

	/** Session's account */
	private Account account;

	/** Logger */
	private Logger logger = LoggerFactory.getLogger(RemoteBrokerSessionImpl.class);

	/** Constructor */
	public RemoteBrokerSessionImpl(Broker sessionBroker, Account sessionAcct) throws RemoteException {
		broker = sessionBroker;
		account = sessionAcct;
	}

	/** Return the balance of session's account */
	@Override
	public int getBalance() throws RemoteException {
		return account.getBalance();
	}

	/** Delete the session's account */
	@Override
	public void deleteAccount(String username) throws RemoteException {
		try {
			broker.deleteAccount(username);
		} catch (BrokerException e) {
			logger.warn("Session cannot delete account: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/** Obtains the current price of a stock and returns a StockQuote object */
	@Override
	public StockQuote requestQuote(String ticker) throws RemoteException {
		StockQuote quote = null;
		try {
			quote = broker.requestQuote(ticker);
		} catch (BrokerException e) {
			logger.warn("Session cannot retrieve Stock Quote: " + e.getMessage());
			e.printStackTrace();
		}
		return quote;
	}

	/** Places a market buy order with the broker */
	@Override
	public void placeMarketBuyOrder(String accountId, int numShares, String ticker) throws RemoteException {
		try {
			MarketBuyOrder order = new MarketBuyOrder(accountId, numShares, ticker);
			broker.placeOrder(order);
		} catch (BrokerException e) {
			logger.warn("Session cannot place market buy order: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/** Places a market sell order with the broker */
	@Override
	public void placeMarketSellOrder(String accountId, int numShares, String ticker) throws RemoteException {
		try {
			MarketSellOrder order = new MarketSellOrder(accountId, numShares, ticker);
			broker.placeOrder(order);
		} catch (BrokerException e) {
			logger.warn("Session cannot place market sell order: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/** Places a stop buy order with the broker */
	@Override
	public void placeStopBuyOrder(String accountId, int numShares, String ticker, int stopPrice)
			throws RemoteException {
		try {
			StopBuyOrder order = new StopBuyOrder(accountId, numShares, ticker, stopPrice);
			broker.placeOrder(order);
		} catch (BrokerException e) {
			logger.warn("Session cannot place stop buy order: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/** Places a stop sell order with the broker */
	@Override
	public void placeStopSellOrder(String accountId, int numShares, String ticker, int stopPrice)
			throws RemoteException {
		try {
			StopSellOrder order = new StopSellOrder(accountId, numShares, ticker, stopPrice);
			broker.placeOrder(order);
		} catch (BrokerException e) {
			logger.warn("Session cannot place stop sell order: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/** Closes the session and releases any resources */
	@Override
	public void close() throws RemoteException {
		try {
			broker.close();
		} catch (BrokerException e) {
			logger.warn("Unable to end session with broker: " + e.getMessage());
			e.printStackTrace();
		}
	}

}
