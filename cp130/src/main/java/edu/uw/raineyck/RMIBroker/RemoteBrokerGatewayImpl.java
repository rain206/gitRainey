package edu.uw.raineyck.RMIBroker;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uw.ext.framework.account.Account;
import edu.uw.ext.framework.broker.Broker;
import edu.uw.ext.framework.broker.BrokerException;

public class RemoteBrokerGatewayImpl extends UnicastRemoteObject implements RemoteBrokerGateway {

	/** Serial Version UID */
	private static final long serialVersionUID = 3860765554177953838L;

	/** Broker */
	private Broker broker;

	/** Logger */
	private Logger logger = LoggerFactory.getLogger(RemoteBrokerGatewayImpl.class);

	/** Constructor */
	public RemoteBrokerGatewayImpl(Broker sessionBroker) throws RemoteException {
		if (sessionBroker != null) {
			broker = sessionBroker;
		} else {
			logger.warn("Cannot move on with null broker");
		}
	}

	/** Creates an account, it should return a reference to a remote session */
	@Override
	public RemoteBrokerSession createAccount(String username, String password, int balance) throws RemoteException {
		Account newAccount = null;
		try {
			newAccount = broker.createAccount(username, password, balance);
		} catch (BrokerException e) {
			logger.warn("Error creating account and session from Gateway");
			e.printStackTrace();
		}

		if (newAccount == null) {
			return null;
		}
		return new RemoteBrokerSessionImpl(broker, newAccount);
	}

	/**
	 * Login to an existing account, it should return a reference to a remote
	 * session
	 */
	@Override
	public RemoteBrokerSession login(String acctName, String acctPassword) throws RemoteException {
		Account targetAcct = null;
		try {
			targetAcct = broker.getAccount(acctName, acctPassword);
		} catch (BrokerException e) {
			logger.warn("Broker Gateway is unable to get account. Username or password not correct. " + e.getMessage());
			e.printStackTrace();
		}

		if (targetAcct == null) {
			return null;
		}
		return new RemoteBrokerSessionImpl(broker, targetAcct);
	}
}
