package edu.uw.raineyck.RMIBroker;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteBrokerGateway extends Remote {

	RemoteBrokerSession createAccount(String accountName, String password, int balance) throws RemoteException;

	RemoteBrokerSession login(String targetUsername, String targetPassword) throws RemoteException;
}
