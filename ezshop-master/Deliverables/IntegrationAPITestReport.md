# Integration and API Test Documentation

Authors:
	Antonio Vespa s284101
	Pietro Borgaro s292501
	Giulio Corallo s282380

Date: 26/04/2021

Version: 1

# Contents

- [Dependency graph](#dependency graph)

- [Integration approach](#integration)

- [Tests](#tests)

- [Scenarios](#scenarios)

- [Coverage of scenarios and FR](#scenario-coverage)
- [Coverage of non-functional requirements](#nfr-coverage)



# Dependency graph 

     <report the here the dependency graph of the classes in EzShop, using plantuml>
### 
![DependencyGraph](/ezshop-master//Deliverables/immagini/DependencyGraph.png)
     
# Integration approach

    <Write here the integration sequence you adopted, in general terms (top down, bottom up, mixed) and as sequence
    (ex: step1: class A, step 2: class A+B, step 3: class A+B+C, etc)> 
    <Some steps may  correspond to unit testing (ex step1 in ex above), presented in other document UnitTestReport.md>
    <One step will  correspond to API testing>
    
     Strategy used: bottom up.

     Step 1 (UnitTesting): class MyUser, class MyCustomer, class MyTicketEntry, class CreditCard, class Position, class TicketCounter,
     class ProductCounter, class PaymentCounter, class OperationsCounter, class CardCounter, class CustomerCounter, class EmployeesCounter,
     class MyOrder.

     Step 2: class MyCustomer+CustomerCounter+CustomerList, class MyUser+EmployeesCounter+Employees, class CreditCard+PaymentCounter+Payment, class Position+MyProductType.

     Step 3: class CreditCard+PaymentCounter+Payment+MyBalanceOperation, class Position+MyProductType+Catalogue.

     Step 4: class Position+MyProductType+Catalogue+MyTicketEntry+MySaleTransaction.

     Step 5: class Position+MyProductType+Catalogue+MyTicketEntry+MySaleTransaction+MyReturnTransaction.

     Step 6: class Position+MyProductType+Catalogue+MyTicketEntry+MySaleTransaction+MyReturnTransaction+MyOrder+CreditCard+PaymentCounter+Payment+MyBalanceOperation+AccountBook.

     Step 7: class all classes+EZShop.

     Step 8: class all classes+EZShop+Database.

#  Tests

   <define below a table for each integration step. For each integration step report the group of classes under test, and the names of
     JUnit test cases applied to them> JUnit test classes should be here src/test/java/it/polito/ezshop

## Step 1

See UnitTestReport.md

| Classes  | JUnit test cases |
|--|--|
|||


## Step 2
| Classes  | JUnit test cases |
|--|--|
| Position+MyProductType | TestMyProductType |
|| testSetQuantity() |
|| testSetLocation() |
|| testSetNote() |
|| testSetProductDescription() |
|| testSetBarCode() |
|| testSetPricePerUnit() |
|| testSetId() |
|| testUpdateQuantity() |
|| testUpdatePosition() |
| MyCustomer+CustomerCounter+CustomerList | TestCustomerList |
|| testAddCustomer() |
|| testGetCustomer() |
|| testGetCustomerName() |
|| testGetAllCustomer() |
|| testDeleteCustomer() |
|| testSearchCustomer() |
|| testModifyCustomer() |
|| modifyCustomerName() |
|| testSearchCardOwner() |
|| testUpdatePoints() |
| MyUser+EmployeesCounter+Employees | TestEmployees |
|| testAddUser() |
|| testDeleteUser() |
|| testGetUser() |
|| testGetAllUser() |
|| testSearchUser() |
| CreditCard+PaymentCounter+Payment | TestPayment |
|| testComputeChange() |
|| testSetPrice() |
|| testSetReturnPrice() |
|| testSetCashOrCard() |
|| testSetCommited() |
|| testSetPaymentID() |
|| testSetCard() |

## Step 3
| Classes  | JUnit test cases |
|--|--|
| CreditCard+PaymentCounter+Payment+MyBalanceOperation | TestMyBalanceOperation |
|| testSetBalanceId() |
|| testSetDate() |
|| testSetMoney() |
|| testSetType() |
|| testSetTime() |
|| testSetStatus() |
|| testSetPayment() |
|| testStartPaymentCard() |
|| testStartPaymentCash() |
| Position+MyProductType+Catalogue | TestCatalogue |
|| testAddProductType() |
|| testDeleteProductType() |
|| testCheckPosition() |
|| testCheckBarCode() |



## Step 4
| Classes  | JUnit test cases |
|--|--|
| Position+MyProductType+Catalogue+MyTicketEntry+MySaleTransaction | TestMySaleTransaction |
|| testSetTicketNumber() |
|| testSetEntries() |
|| testSetDiscountRate() |
|| testSetPrice() |
|| testSetStatus() |
|| testAddProductToSale() |
|| testRemoveProductFromSale() |
|| testComputeTotalPrice() |
|| testAddDiscountToProduct() |
|| testComputePointsEarned() |
|| testRemoveAllProductFromSale() |


## Step 5
| Classes  | JUnit test cases |
|--|--|
| Position+MyProductType+Catalogue+MyTicketEntry+MySaleTransaction+MyReturnTransaction | TestMyreturnTransaction |
|| testReturnProduct() |
|| testCommit() |
|| testComputeTotalPrice() |
|| testGetReturnID() |
|| testSetReturnID() |
|| testGetSaleID() |
|| testSetSaleID() |
|| testGetSaleTransaction() |
|| testGetStatus() |
|| testSetSaleTransaction() |
|| testSetReturnedPerBarcode() |


## Step 6
| Classes  | JUnit test cases |
|--|--|
| Position+MyProductType+Catalogue+MyTicketEntry+MySaleTransaction+MyReturnTransaction+MyOrder<br>+CreditCard+PaymentCounter+Payment+MyBalanceOperation+AccountBook | TestAccountBook |
|| testIssueOrder() |
|| testRecoverOrder() |
|| testPayOrderFor() |
|| testPayOrder() |
|| testRecordOrderArrival() |
|| testGetAllOrders() |
|| testIssueSaleTransaction() |
|| testAddOrRemoveProductToSale() |
|| testRecoverBalanceOperation() |
|| testAddNewBalanceOperationSale() |
|| testRemoveSaleTransaction() |
|| testRecoverSale() |
|| testRecoverTicket() |
|| testIssueReturnTransaction() |
|| testRemoveReturnTransaction() |
|| testAddNewBalanceOperationReturn() |
|| testRecordBalanceUpdate() |
|| testRemoveBalanceOperation() |
|| testGetBalanceOperationByDate() |


## Step 7
| Classes  | JUnit test cases |
|--|--|
| all classes + EZShop | ApiTesting |
|| testCreateUser() |
|| testDeleteUser() |
|| testGetAllUsers() |
|| testGetUser() |
|| testUpdateUserRights() |
|| testLogin() |
|| testLogout() |
|| testCreateProductType() |
|| testUpdateProduct() |
|| testDeleteProductType() |
|| testGetAllProductType() |
|| testGetProductTypeByBarcode() |
|| testGetProductTypesByDescription() |
|| testUpdateQuantity() |
|| testUpdatePosition() |
|| testIssueOrder() |
|| testPayOrderFor() |
|| testPayOrder() |
|| testRecordOrderArrival() |
|| testGetAllOrders() |
|| testDefineCustomer() |
|| testModifyCustomer() |
|| testDeleteCustomer() |
|| testGetCustomer() |
|| testGetAllCustomers() |
|| testCreateCard() |
|| testAttachCardToCustomer() |
|| testModifyPointsOnCard() |
|| testStartSaleTransaction() |
|| testAddProductToSale() |
|| testDeleteProductFromSale() |
|| testApplyDiscountRateToProduct() |
|| testApplyDiscountRateToSale() |
|| testComputePointsForSale() |
|| testEndSaleTransaction() |
|| testDeleteSaleTransaction() |
|| testGetSaleTransaction() |
|| testStartReturnTransaction() |
|| testReturnProduct() |
|| testEndReturnTransaction() |
|| testDeleteReturnTransaction() |
|| testReceiveCashPayment() |
|| testReceiveCreditCardPayment() |
|| testReturnCashPayment() |
|| testReturnCreditCardPayment() |
|| testRecordBalanceUpdate() |
|| testGetCreditsAndDebits() |
|| testComputeBalance() |


## Step 8
| Classes  | JUnit test cases |
|--|--|
| Database | TestDatabase |
|| testCreateTableEntries() |
|| testAddEntry() |
|| testCreateTableSales() |
|| testAddSale() |
|| testCreateTableOperations() |
|| testAddOperation() |
|| testCreateTableOrders() |
|| testAddOrder() |
|| testCreateTableProducts() |
|| testAddProduct() |
|| testCreateTableEmployees() |
|| testAddEmployee() |
|| testCreateTableCustomers() |
|| testAddCustomer() |


# Scenarios

<If needed, define here additional scenarios for the application. Scenarios should be named
 referring the UC in the OfficialRequirements that they detail>

## Scenario UC2.4

| Scenario | Retrieve list of all users |
| ------------- |:-------------:| 
|  Precondition     | Admin A exists and is logged in |
|  Post condition     | List of users is shown to A |
| Step#        | Description  |
|  1     |  A asks for list of all users |  
|  2     |  System shows data of all users to A |

## Scenario UC2.5

| Scenario | Retrieve user details |
| ------------- |:-------------:| 
|  Precondition     | Admin A exists and is logged in |
|  Post condition     | User details are shown to A |
| Step#        | Description  |
|  1     |  A asks for a specific user |  
|  2     |  System shows data of that user to A |

## Scenario UC1.4

| Scenario | Delete product type |
| ------------- |:-------------:| 
|  Precondition     | Employee C exists and is logged in |
|                   | Product type X exists |
|  Post condition     | Product type X doesn't exist |
| Step#        | Description  |
|  1     | C searches X via bar code |  
|  2     | C selects X's record |
|  3     | C deletes X |
|  4     | C confirms the delete |
|  5     | X is deleted |

## Scenario UC1.5

| Scenario | List all product types |
| ------------- |:-------------:| 
|  Precondition     | Employee C exists and is logged in |
|  Post condition     | List of product types is shown to C |
| Step#        | Description  |
|  1     | C asks for list of all product types  |  
|  2     | System shows data of all product types in catalogue |


## Scenario UC1.6

| Scenario | Modify product type quantity |
| ------------- |:-------------:| 
|  Precondition     | Employee C exists and is logged in |
|                   | Product type X exists |
|  Post condition     | X.quantity = Q |
| Step#        | Description  |
|  1     | C searches X via bar code |  
|  2     | C selects X's record |
|  3     | C inserts a new quantity>=0 |
|  4     | C confirms the update |
|  5     | X is updated |

## Scenario UC3.4

| Scenario |  Record order of product type X arrival |
| ------------- |:-------------:| 
|  Precondition     | ShopManager S exists and is logged in |
| | Product type X exists |
| | X.location is valid |
| | Order O exists  |
|  Post condition     | O is in PAYED state  |
| | Balance not changed |
| | X.units not changed |
| Step#        | Description  |
|  1    | S creates order O |
|  1    |  S fills  quantity of product to be ordered and the price per unit |  
|  3    | S register payment done for O |
|  4    |  O is recorded in the system in PAYED state |

## Scenario UC3.5
| Scenario |  List all Orders |
| ------------- |:-------------:| 
|  Precondition     | ShopManager S or Administrator A exists and is logged in |
|  Post condition     | list of all Orders is shown to S or A |
| Step#        | Description  |
|1| S or A asks for list of all orders  |  
|  2    | System shows data of all orders |

## Scenario UC4.5
| Scenario | Delete Customer |
| ------------- |:-------------:| 
|  Precondition     | Employee E exists and is logged in |
|                   | Account U for Customer Cu existing |
|  Post condition     | Account U for Customer Cu not exist |
| Step#        | Description  |
|  1     | E selects customer record U |  
|  2     | E delete customer record U |
|  3    | E confirms the delete |
|  4     | U is deleted |

## Scenario UC4.6
| Scenario | Search a Customer |
| ------------- |:-------------:| 
|  Precondition     | Employee E exists and is logged in |
|  Post condition     | Customer details are shown to E |
| Step#        | Description  |
|  1     |  E asks for a specific Customer |  
|  2     |  System shows data of that Customer to E |

## Scenario UC4.7
| Scenario | Get all Customers |
| ------------- |:-------------:| 
|  Precondition     | Employee E exists and is logged in |
|  Post condition     | list of all Customers are shown to E |
| Step#        | Description  |
|  1     |  E asks for list of all Customers |  
|  2     |  System shows list of all Customers to E |

## Scenario UC6.7

| Scenario |  name |
| ------------- |:-------------:| 
|  Precondition     | Cashier C exists and is logged in |
|       | Product type X exists and has been already added to the sale |
|  Post condition     |  Balance -= N*X.unitPrice |
|       |  X.quantity += N |
| Step#        | Description  |
|  1    |  C starts a new sale transaction |  
|  2    |  C reads bar code of X |
|  3    |  C adds N units of X to the sale |
|  4    |  X available quantity is decreased by N |
|  5    |  C remove N units of X to the sale |
|  6    |  X available quantity is increased by N |
|  7    |  C closes the sale transaction |
|  8    |  A sale review is shown to C and the Customer |
|  9    |  Sytem ask payment type |
|  10    |  Manage cash payment (UC7) |
|  11    |  Payment successful |
|  12   |  C confirms the sale and prints the sale Ticket |
|  13   |  Balance is updated |

## Scenario UC6.8

| Scenario |  name |
| ------------- |:-------------:| 
|  Precondition     | Cashier C exists and is logged in |
|       | sale exists |
|  Post condition     |  Sale details are showed to C |
| Step#        | Description  |
|  1    |  C reads the ticket number of the sale transaction |  
|  2    |  C writes it on the system |
|  3    |  EZShop system searches the sale via ticket number |
|  4    |  Sale details are showed to C |

## Scenario UC9.2

| Scenario |  name |
| ------------- |:-------------:| 
|  Precondition     | Manager C exists and is logged in |
|  Post condition     |  Balance displayed |
| Step#        | Description  |
|  1    |  C asks to EZShop to compute thee balance |  
|  2    |  EZShop system computes it |
|  3    |  Balance displayed to C |

# Coverage of Scenarios and FR

<Report in the following table the coverage of  scenarios (from official requirements and from above) vs FR. 
Report also for each of the scenarios the (one or more) API JUnit tests that cover it. >

| Scenario ID | Functional Requirements covered | JUnit  Test(s) | 
| ----------- | ------------------------------- | -------------- | 
| Scenario 1.1 | FR3.1 | testCreateProductType(),testUpdatePosition() |
| Scenario 1.2 | FR3.4, FR4.2 | testUpdatePosition(), testGetProductTypeByBarcode() |
| Scenario 1.3 | FR3.4, FR3.1 | testGetProductTypeByBarcode(), testUpdateProduct() |
| Scenario 1.4 | FR3.2 | testDeleteProductType() |
| Scenario 1.5 | FR3.3 | testGetAllProductTypes() |  
| Scenario 1.6 | FR4.1 | testUpdateQuantity() |
| Scenario 2.1 | FR1.1 | testCreateUser() |
| Scenario 2.2 | FR1.2 | testDeleteUser() |
| Scenario 2.3 | FR1.5 | testUpdateUserRights() |
| Scenario 2.4 | FR1.3 | testGetAllUsers() |
| Scenario 2.5 | FR1.4 | testGetUser() |
| Scenario 3.1 | FR4.3 | testIssueOrder() |
| Scenario 3.2 | FR4.5 | testPayOrder() |
| Scenario 3.3 | FR4.6 | testRecordOrderArrival() |
| Scenario 3.4 | FR4.4 | testPayOrderFor() |
| Scenario 3.5 | FR4.7 | testGetAllOrders() |
| Scenario 4.1 | FR5.1 | testDefineCustomer() |
| Scenario 4.2 | FR5.5, FR5.6 | testCreateCard(), testAttachCardToCustomer() |
| Scenario 4.3 | FR5   | testModifyCustomer() |
| Scenario 4.4 | FR5.1 | testModifyCustomer() |
| Scenario 4.5 | FR5.2 | testDeleteCustomer() |
| Scenario 4.6 | FR5.3 | testGetCustomer() |
| Scenario 4.7 | FR5.4 | testGetAllCustomers() |
| Scenario 5.1 | FR1.5 | testLogin() |
| Scenario 5.2 | FR1.5 | testLogout() |
| Scenario 6.1 | FR6.1, FR6.2, FR6.7, FR6.10, FR7.1, FR7.2, FR8.2 | testStartSaleTransaction(), testAddProductToSale(), testEndSaleTransaction(), testReceiveCashPayment(), testReceiveCreditCardPayment() |
| Scenario 6.2 | FR6.1, FR6.2, FR6.5, FR6.7, FR6.10, FR7.1, FR7.2, FR8.2 | testStartSaleTransaction(), testAddProductToSale(), testApplyDiscountRateToProduct(), testEndSaleTransaction(), testReceiveCashPayment(), testReceiveCreditCardPayment() |
| Scenario 6.3 | FR6.1, FR6.2, FR6.4, FR6.7, FR6.10, FR7.1, FR7.2, FR8.2 | testStartSaleTransaction(), testAddProductToSale(), testApplyDiscountRateToSale(), testEndSaleTransaction(), testReceiveCashPayment(), testReceiveCreditCardPayment() |
| Scenario 6.4 | FR5.7, FR6.1, FR6.2, FR6.6, FR6.10, FR7.1, FR7.2, FR8.2 | testStartSaleTransaction(), testAddProductToSale(), testEndSaleTransaction(), testReceiveCashPayment(), testReceiveCreditCardPayment(), testComputePointsForSale() |
| Scenario 6.5 | FR6.1, FR6.2, FR6.10, FR6.11 | testStartSaleTransaction(), testAddProductToSale(), testEndSaleTransaction(), testDeleteSaleTransaction(),testAddProductToSale(), <br>testEndSaleTransaction(), testReceiveCashPayment(), testReceiveCreditCardPayment(), testComputePointsForSale() |
| Scenario 6.6 | FR6.1, FR6.2, FR6.7, FR6.10, FR7.1, FR8.2 | testStartSaleTransaction(), testAddProductToSale(), testEndSaleTransaction(), testReceiveCashPayment() |
| Scenario 6.7 | FR6.3 | testStartSaleTransaction(), testAddProductToSale(), testDeleteProductFromSale() | 
| Scenario 6.8 | FR6.9 | testGetSaleTransaction() | 
| Scenario 7.1 | FR7.2 | testReceiveCreditCardPayment() |
| Scenario 7.2 | FR7.2 | testReceiveCreditCardPayment() |
| Scenario 7.3 | FR7.2 | testReceiveCreditCardPayment() |
| Scenario 7.4 | FR7.1 | testReceiveCashPayment() |
| Scenario 8.1 | FR6.12, FR6.13, FR6.14, FR6.15, FR7.4, FR8.1 | testStartReturnTransaction(), testReturnProduct(), testEndReturnTransaction(), testReturnCreditCardPayment() |
| Scenario 8.2 | FR6.12, FR6.13, FR6.14, FR6.15, FR7.3, FR8.1 | testStartReturnTransaction(), testReturnProduct(), testEndReturnTransaction(), testReturnCashPayment() |
| Scenario 9.1 | FR8.3 | testGetCreditsAndDebits() |
| Scenario 9.2 | FR8.4 | testComputeBalance() |
| Scenario 10.1 | FR7.4 | testReturnCreditCardPayment() |
| Scenario 10.2 | FR7.3 | testReturnCashPayment() |       



# Coverage of Non Functional Requirements


<Report in the following table the coverage of the Non Functional Requirements of the application - only those that can be tested with automated testing frameworks.>


### 

| Non Functional Requirement | Test name |
| :------------------------: | --------- |
|        NFR2                |   see pictures below to understand performance of our application (we considered API), all < 0.5 sec |
|        NFR4                |    testCreateProductType(), testUpdateProduct()     |
|        NFR5                |     testReceiveCreditCardPayment()      |
|        NFR6                |     testCreateCard(),  testAttachCardToCustomer(), testModifyPointsOnCard()     |

![NFRa](/ezshop-master//Deliverables/immagini/NFRa.jpeg)
![NFRb](/ezshop-master//Deliverables/immagini/NFRb.jpeg)

