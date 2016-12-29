package edu.uw.raineyck;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import test.AccountManagerTest;
import test.AccountTest;
import test.BrokerTest;
import test.DaoTest;

@RunWith(Suite.class)
// AccountTest.class, AccountManagerTest.class, DaoTest.class,
@Suite.SuiteClasses({ AccountTest.class, AccountManagerTest.class, DaoTest.class, BrokerTest.class})
public class TestSuite {
}