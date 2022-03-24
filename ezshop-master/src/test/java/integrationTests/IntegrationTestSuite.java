package integrationTests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;



@RunWith(Suite.class)
@Suite.SuiteClasses({TestAccountBook.class,TestMyProductType.class, TestCatalogue.class, TestPayment.class, TestMyBalanceOperation.class, TestEmployees.class, TestMySaleTransaction.class, TestMyReturnTransaction.class, TestCustomerList.class, TestDatabase.class})
public class IntegrationTestSuite {

}
