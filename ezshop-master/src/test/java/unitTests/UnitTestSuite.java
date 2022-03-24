package unitTests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({TestCardCounter.class,TestCreditCard.class,TestCustomersCounter.class,
	TestEmployeesCounter.class,TestMyCustomer.class,TestMyTicketEntry.class,
	TestMyUser.class,TestOperationsCounter.class,TestPaymentCounter.class,
	TestProductCounter.class, TestTicketCounter.class, TestPosition.class, TestMyOrder.class})
public class UnitTestSuite {

}
