package edu.uw.raineyck.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uw.ext.framework.dao.AccountDao;
import edu.uw.ext.framework.dao.DaoFactory;
import edu.uw.ext.framework.dao.DaoFactoryException;

/**
 * Dao Factory Implementation to get the Account DAO
 * 
 * @author craigrainey
 *
 */
public class DaoFactoryImpl implements DaoFactory {

	/** Dao Factory Constructor */
	public DaoFactoryImpl() {
	}

	/** Creates a new Account DAO */
	@Override
	public synchronized AccountDao getAccountDao() throws DaoFactoryException {
		AccountDao newDao = new AccountDaoImpl();
		return newDao;
	}

}
