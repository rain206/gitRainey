package edu.uw.raineyck.RMIBroker;

import java.rmi.Remote;
import java.rmi.RemoteException;

import edu.uw.ext.framework.exchange.StockQuote;

/**
 * Provides an interface for an object that maintains a session with the broker.
 * The session should be aware of the account that is associated with the
 * session.
 * 
 * @author craigrainey
 */
public interface RemoteBrokerSession extends Remote {

	/**
	 * Gets the balance on an account
	 * 
	 * @return the balance
	 * @throws RemoteException
	 */
	int getBalance() throws RemoteException;

	/**
	 * Deletes an account
	 * 
	 * @param username
	 *            - username of the account
	 * @throws RemoteException
	 */
	void deleteAccount(String username) throws RemoteException;

	/**
	 * Retrieves a stock quote for a certain stock
	 * 
	 * @param ticker
	 *            - symbol for stock
	 * @return - stock quote
	 * @throws RemoteException
	 */
	StockQuote requestQuote(String ticker) throws RemoteException;

	/**
	 * Place a market buy order with broker
	 * 
	 * @param accountId
	 *            - account ID for person placing order
	 * @param numShares
	 *            - number of shares
	 * @param ticker
	 *            - stock symbol
	 * @throws RemoteException
	 */
	void placeMarketBuyOrder(String accountId, int numShares, String ticker) throws RemoteException;

	/**
	 * Place a market sell order
	 * 
	 * @param accountId
	 *            - account ID for person placing order
	 * @param numShares
	 *            - number of shares
	 * @param ticker
	 *            - stock symbol
	 * @throws RemoteException
	 */
	void placeMarketSellOrder(String accountId, int numShares, String ticker) throws RemoteException;

	/**
	 * Place a stop buy order
	 * 
	 * @param accountId
	 *            - ID for person placing order
	 * @param numShares
	 *            - number of shares
	 * @param ticker
	 *            - stock symbol
	 * @param stopPrice
	 *            - price to execute order at
	 * @throws RemoteException
	 */
	void placeStopBuyOrder(String accountId, int numShares, String ticker, int stopPrice) throws RemoteException;

	/**
	 * Place stop buy order
	 * 
	 * @param accountId
	 *            - ID for person placing order
	 * @param numShares
	 *            - number of shares
	 * @param ticker
	 *            - stock symbol
	 * @param stopPrice
	 *            - price to execute order
	 * @throws RemoteException
	 */
	void placeStopSellOrder(String accountId, int numShares, String ticker, int stopPrice) throws RemoteException;

	/**
	 * Closes the session with broker
	 * 
	 * @throws RemoteException
	 */
	void close() throws RemoteException;
}
