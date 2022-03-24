package it.polito.ezshop.acceptanceTests;



import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import APItesting.ApiSuite;
import integrationTests.IntegrationTestSuite;
import unitTests.UnitTestSuite;

@RunWith(Suite.class)
@Suite.SuiteClasses({UnitTestSuite.class, IntegrationTestSuite.class, ApiSuite.class})
public class TestEZShop {  

 
   
    
}
