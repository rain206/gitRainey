package app;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uw.ext.framework.exchange.StockQuote;
import edu.uw.raineyck.RMIBroker.RemoteBrokerGateway;
import edu.uw.raineyck.RMIBroker.RemoteBrokerSession;

/**
 * Class to act as client that will interact with broker and test operations.
 * 
 * @author craigrainey
 *
 */
public class RmiBrokerClient {

	/** Account name */
	private static final String ACCT_NAME = "raineyck";

	/** Account password */
	private static final String ACCT_PASSWORD = "javacert";

	/** Symbol F */
	private static final String SYMBOL_F = "F";

	/** Symbol GM */
	private static final String SYMBOL_GM = "GM";

	/** Symbol HWP */
	private static final String SYMBOL_HWP = "HWP";

	/** Starting Balance */
	private static final int START_BALANCE = 100_000;

	/** Logger */
	private static Logger logger = LoggerFactory.getLogger(RmiBrokerClient.class);

	/**
	 * Main method to inact a client interacting with RMI server
	 * 
	 * @param args
	 * @throws NotBoundException
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		String serverName = RmiBrokerServer.SERVER_NAME;
		int serverPort = RmiBrokerServer.PORT;
		String serverUrl = String.format("//localhost:%d/%s", serverPort, serverName);

		RemoteBrokerGateway brokerGateway = (RemoteBrokerGateway) Naming.lookup(serverUrl);
		RemoteBrokerSession brokerSession = null;
		try {
			brokerSession = brokerGateway.createAccount(ACCT_NAME, ACCT_PASSWORD, START_BALANCE);

			if (brokerSession == null) {
				brokerSession = brokerGateway.login(ACCT_NAME, ACCT_PASSWORD);
				logger.info("Cound not create account but was able to login to existing account");
			}

			int balance = brokerSession.getBalance();
			logger.info("Account balance: " + balance);

			StockQuote stockObj = brokerSession.requestQuote("BA");
			logger.info("Current price for BA: " + stockObj.getPrice());

			brokerSession.placeMarketBuyOrder(ACCT_NAME, 15, SYMBOL_F);
			logger.info("Placed market buy order for account: " + ACCT_NAME + " shares: 15   " + "symbol: " + SYMBOL_F);

			brokerSession.placeMarketSellOrder(ACCT_NAME, 2, SYMBOL_F);
			logger.info("Placed market sell order for account: " + ACCT_NAME + " shares: 2   " + "symbol: " + SYMBOL_F);

			brokerSession.placeStopBuyOrder(ACCT_NAME, 2, SYMBOL_GM, 4250);
			logger.info("Placed stop buy order for account: " + ACCT_NAME + " shares: 2    " + "symbol: " + SYMBOL_GM
					+ " Buy Price: 4250");

			brokerSession.placeStopSellOrder(ACCT_NAME, 5, SYMBOL_HWP, 1615);
			logger.info("Placed stop sell order for account: " + ACCT_NAME + " shares: 5   " + "symbol: " + SYMBOL_HWP
					+ " Sell Price: 1615");

			brokerSession.deleteAccount(ACCT_NAME);
			logger.info("Successfully deleted account: ckrainey");

			brokerSession.close();
			logger.info("Closed session with broker.");

		} catch (RemoteException e) {
			e.printStackTrace();
			;
		}
	}
}
