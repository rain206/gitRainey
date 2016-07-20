package edu.uw.raineyck.dao;

import edu.uw.ext.framework.dao.AccountDao;
import edu.uw.ext.framework.dao.DaoFactory;
import edu.uw.ext.framework.dao.DaoFactoryException;

public class JsonDaoFactory implements DaoFactory {

	/** Constructor */
	public JsonDaoFactory() {
	}

	/** Creates and returns a new DAO */
	@Override
	public AccountDao getAccountDao() throws DaoFactoryException {
		AccountDao dao = new JsonDao();
		return dao;
	}

}
