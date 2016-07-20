package app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import edu.uw.ext.framework.account.AccountManager;
import edu.uw.ext.framework.account.AccountManagerFactory;
import edu.uw.ext.framework.broker.Broker;
import edu.uw.ext.framework.broker.BrokerFactory;
import edu.uw.ext.framework.dao.AccountDao;
import edu.uw.ext.framework.dao.DaoFactory;
import edu.uw.ext.framework.exchange.StockExchangeSpi;
import edu.uw.raineyck.RMIBroker.RemoteBrokerGateway;
import edu.uw.raineyck.RMIBroker.RemoteBrokerGatewayImpl;

/**
 * Rmi Broker Server that will register with a port and bind the remote broker
 * gateway.
 * 
 * @author craigrainey
 *
 */
public class RmiBrokerServer {

	/** Logger */
	private static Logger logger = LoggerFactory.getLogger(RmiBrokerServer.class);

	/** Server Name */
	public static final String SERVER_NAME = "CRAIGS_SERVER";

	/** Port */
	public static final int PORT = 11099;

	/** Class Path XML Application */
	private static ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("context.xml");

	/** Constructor */
	public RmiBrokerServer() {
	}

	/**
	 * Main method to run application
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		StockExchangeSpi exchange = ExchangeFactory.newTestStockExchange();
		exchange.open();

		DaoFactory factoryDao = appContext.getBean("DaoFactory", DaoFactory.class);
		AccountDao accountDao = factoryDao.getAccountDao();

		AccountManagerFactory managerFactory = appContext.getBean("AccountManagerFactory", AccountManagerFactory.class);
		AccountManager acctManager = managerFactory.newAccountManager(accountDao);

		BrokerFactory brokerFactory = appContext.getBean("BrokerFactory", BrokerFactory.class);
		Broker broker = brokerFactory.newBroker("Session Broker", acctManager, exchange);

		try {
			RemoteBrokerGateway rbg = new RemoteBrokerGatewayImpl(broker);

			Registry registry = LocateRegistry.createRegistry(PORT);
			registry.bind(SERVER_NAME, rbg);

			System.out.println("Registry up and running");

			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
				System.out.println("#");
				System.out.println("#");
				System.out.println("#");
				System.out.println("# Press enter to exit");
				System.out.println("#");
				System.out.println("#");
				System.out.println("#");
				reader.readLine();
			} catch (IOException i) {
				i.printStackTrace();
			}

			UnicastRemoteObject.unexportObject(rbg, true);
			logger.info("Successfully unexeported object");
			System.exit(0);

		} catch (RemoteException e) {
			logger.warn("Remote exception thrown from RMI server: " + e.getMessage());
			e.printStackTrace();
		} catch (AlreadyBoundException a) {
			logger.warn("Already Bound Exception thrown from RMI server: " + a.getMessage());
			a.printStackTrace();
		}
	}
}
