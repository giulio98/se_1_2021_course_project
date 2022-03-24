# Unit Testing Documentation

Authors: Antonio Vespa s284101 Pietro Borgaro s292501 Giulio Corallo s282380

Date: 18/05/2021

Version: 1

# Contents

- [Black Box Unit Tests](#black-box-unit-tests)




- [White Box Unit Tests](#white-box-unit-tests)


# Black Box Unit Tests

    <Define here criteria, predicates and the combination of predicates for each function of each class.
    Define test cases to cover all equivalence classes and boundary conditions.
    In the table, report the description of the black box test case and (traceability) the correspondence with the JUnit test case writing the 
    class and method name that contains the test case>
    <JUnit test classes must be in src/test/java/it/polito/ezshop   You find here, and you can use,  class TestEzShops.java that is executed  
    to start tests
    >
 ### **Class *MyCustomer* - *setCustomerName*



**Criteria for method *setCustomerName*:**
	

 - Validity of the setter





**Predicates for method *setCustomerName*:**

| Criteria | Predicate |
| -------- | --------- |
|Validity of the setter|Yes|
||No|




**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |



**Combination of predicates**:


|Validity of the setter| Valid/Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|
|Yes|Valid|T1("paolo";customerName setted="paolo")|testSetCustomerName()||
|No|Invalid|-|-|

 ### **Class *MyCustomer* - *setCustomerCard*



**Criteria for method *setCustomerCard*:**
	

 - Validity of the setter





**Predicates for method *setCustomerName*:**

| Criteria | Predicate |
| -------- | --------- |
|Validity of the setter|Yes|
||No|




**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |



**Combination of predicates**:


|Validity of the setter| Valid/Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|
|Yes|Valid|T1("1234567890";customerCard setted="1234567890")|testSetCustomerCard()||
|No|Invalid|-|-|

 ### **Class *MyCustomer* - *setId*



**Criteria for method *setId*:**
	

 - Validity of the setter





**Predicates for method *setId*:**

| Criteria | Predicate |
| -------- | --------- |
|Validity of the setter|Yes|
||No|




**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |



**Combination of predicates**:


|Validity of the setter| Valid/Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|
|Yes|Valid|T1(1;Id setted=1)|testSetCustomerId()||
|No|Invalid|-|-|

 ### **Class *MyCustomer* - *setPoints*



**Criteria for method *setPoints*:**
	

 - Validity of the setter





**Predicates for method *setPoints*:**

| Criteria | Predicate |
| -------- | --------- |
|Validity of the setter|Yes|
||No|




**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |



**Combination of predicates**:


|Validity of the setter| Valid/Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|
|Yes|Valid|T1(12;points setted=12)|testSetPoints()||
|No|Invalid|-|-|
 ### **Class *MyCustomer* - *addPoints*



**Criteria for method *addPoints*:**
	

 - Validity of the points added





**Predicates for method *addPoints*:**

| Criteria | Predicate |
| -------- | --------- |
|Validity of the points added|Yes|
||No|




**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |



**Combination of predicates**:


|Validity of the points added| Valid/Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|
|Yes|Valid|T1(12;setted points=previousPoints+12)|testAddPoints()||
|No|Invalid|-|-|

 ### **Class *MyUser* - *setId*



**Criteria for method *setId*:**
	

 - Validity of the setter





**Predicates for method *setId*:**

| Criteria | Predicate |
| -------- | --------- |
|Validity of the setter|Yes|
||No|




**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |



**Combination of predicates**:


|Validity of the setter| Valid/Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|
|Yes|Valid|T1(1;Id setted=1)|testSetUserId()||
|No|Invalid|-|-|

 ### **Class *MyUser* - *setUsername*



**Criteria for method *setUsername*:**
	

 - Validity of the setter





**Predicates for method *setUsername*:**

| Criteria | Predicate |
| -------- | --------- |
|Validity of the setter|Yes|
||No|




**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |



**Combination of predicates**:


|Validity of the setter| Valid/Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|
|Yes|Valid|T1("paolo";username setted="paolo")|testSetUsername()||
|No|Invalid|-|-|

 ### **Class *MyUser* - *setPassword*



**Criteria for method *setPassword*:**
	

 - Validity of the setter





**Predicates for method *setPassword*:**

| Criteria | Predicate |
| -------- | --------- |
|Validity of the setter|Yes|
||No|




**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |



**Combination of predicates**:


|Validity of the setter| Valid/Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|
|Yes|Valid|T1("password";password setted="password")|testSetPassword()||
|No|Invalid|-|-|

 ### **Class *MyUser* - *setRole*



**Criteria for method *setRole*:**
	

 - Validity of the setter





**Predicates for method *setRole*:**

| Criteria | Predicate |
| -------- | --------- |
|Validity of the setter|Yes|
||No|




**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |



**Combination of predicates**:


|Validity of the setter| Valid/Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|
|Yes|Valid|T1("Administrator";role setted="Administrator")|testSetRole()||
|No|Invalid|-|-|

 ### **Class *MyUser* - *verifyAutentication*



**Criteria for method *verifyAutentication*:**
	

 - Validity of the password





**Predicates for method *addPoints*:**

| Criteria | Predicate |
| -------- | --------- |
|Validity of the password|Yes|
|          |No           |




**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |



**Combination of predicates**:


|Validity of the password| Valid/Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|
|Yes|Valid|T1("password";true)|testVerifyAutentication()||
|No|Invalid|T2("1234";false)|testVerifyAutentication()||

 ### **Class *MyTicketEntry* - *setBarcode*



**Criteria for method *setBarcode*:**
	

 - Validity of the setter





**Predicates for method *setBarcode*:**

| Criteria | Predicate |
| -------- | --------- |
|Validity of the setter|Yes|
||No|




**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |



**Combination of predicates**:


|Validity of the setter| Valid/Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|
|Yes|Valid|T1("1234567890";barcode setted="1234567890")|testSetBarcode()||
|No|Invalid|-|-|

 ### **Class *MyTicketEntry* - *setProductDescription*



**Criteria for method *setProductDescription*:**
	

 - Validity of the setter





**Predicates for method *setProductDescription*:**

| Criteria | Predicate |
| -------- | --------- |
|Validity of the setter|Yes|
||No|




**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |



**Combination of predicates**:


|Validity of the setter| Valid/Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|
|Yes|Valid|T1("pasta";description setted="pasta")|testSetProductDescription()||
|No|Invalid|-|-|

 ### **Class *MyTicketEntry* - *setAmount*



**Criteria for method *setAmount*:**
	

 - Validity of the setter





**Predicates for method *setAmount*:**

| Criteria | Predicate |
| -------- | --------- |
|Validity of the setter|Yes|
||No|




**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |



**Combination of predicates**:


|Validity of the setter| Valid/Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|
|Yes|Valid|T1(1;amount setted=1)|testSetAmount()||
|No|Invalid|-|-|

 ### **Class *MyTicketEntry* - *addAmount*



**Criteria for method *addAmount*:**
	

 - Validity of the amount added





**Predicates for method *addAmount*:**

| Criteria | Predicate |
| -------- | --------- |
|Validity of the amount added|Yes|
||No|




**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |



**Combination of predicates**:


|Validity of the points added| Valid/Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|
|Yes|Valid|T1(12;setted amount=previousAmount+12)|testAddAmount()||
|No|Invalid|-|-|

 ### **Class *MyTicketEntry* - *setPricePerUnit*



**Criteria for method *setPricePerUnit*:**
	

 - Validity of the setter





**Predicates for method *setPricePerUnit*:**

| Criteria | Predicate |
| -------- | --------- |
|Validity of the setter|Yes|
||No|




**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |



**Combination of predicates**:


|Validity of the points added| Valid/Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|
|Yes|Valid|T1(12;setted priceperunit=12)|testSetPricePerUnit()||
|No|Invalid|-|-|

 ### **Class *MyTicketEntry* - *setDiscountRate*



**Criteria for method *setDiscountRate*:**
	

 - Validity of the setter





**Predicates for method *setDiscountRate*:**

| Criteria | Predicate |
| -------- | --------- |
|Validity of the setter|Yes|
||No|




**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |



**Combination of predicates**:


|Validity of the points added| Valid/Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|
|Yes|Valid|T1(0.5;setted discountRate=0.5)|testSetDiscountRate()||
|No|Invalid|-|-|

 ### **Class *MyTicketEntry* - *setAmountReturned*



**Criteria for method *setAmountReturned*:**
	

 - Validity of the setter





**Predicates for method *setAmountReturned*:**

| Criteria | Predicate |
| -------- | --------- |
|Validity of the setter|Yes|
||No|




**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |



**Combination of predicates**:


|Validity of the setter| Valid/Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|
|Yes|Valid|T1(1;amountReturned setted=1)|testSetAmountReturned()||
|No|Invalid|-|-|

 ### **Class *MyTicketEntry* - *updateAmountReturned*



**Criteria for method *updateAmountReturned*:**
	

 - Validity of the update





**Predicates for method *updateAmountReturned*:**

| Criteria | Predicate |
| -------- | --------- |
|Validity of the update|Yes|
||No|




**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |



**Combination of predicates**:


|Validity of the update| Valid/Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|
|Yes|Valid|T1(1;amount=previousAmount+1)|testUpdateAmountReturned()||
|No|Invalid|-|-|
 ### **Class *MyTicketEntry* - *computeTotal*



**Criteria for method *computeTotal*:**
	

 - Validity of the total





**Predicates for method *updateAmountReturned*:**

| Criteria | Predicate |
| -------- | --------- |
|Validity of the total|Yes|
||No|




**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |



**Combination of predicates**:


|Validity of the total| Valid/Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|
|Yes|Valid|T1(new MyTicketEntry("1234567891",12,1.0,"cereali"),setdiscountRate(0.5);6.0)|testComputeTotal()||
|No|Invalid|-|-|

 ### **Class *CreditCard* - *setCreditCardNumber*



**Criteria for method *setCreditCardNumber*:**
	

 - Validity of the setter





**Predicates for method *setCreditCardNumber*:**

| Criteria | Predicate |
| -------- | --------- |
|Validity of the setter|Yes|
||No|




**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |



**Combination of predicates**:


|Validity of the setter| Valid/Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|
|Yes|Valid|T1("1234567890";CreditCard setted="1234567890")|testSetCreditCardNumber()||
|No|Invalid|-|-|


 ### **Class *CreditCard* - *setBalance*



**Criteria for method *setBalance*:**
	

 - Validity of the setter





**Predicates for method *setBalance*:**

| Criteria | Predicate |
| -------- | --------- |
|Validity of the setter|Yes|
||No|




**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |



**Combination of predicates**:


|Validity of the setter| Valid/Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|
|Yes|Valid|T1(500;balance setted=500)|testSetBalance()||
|No|Invalid|-|-|


 ### **Class *CreditCard* - *checkBalance*



**Criteria for method *checkBalance*:**
	

 -Amount of the price





**Predicates for method *checkBalance*:**

| Criteria | Predicate |
| -------- | --------- |
|Amount of the price|<=balance|
|          |>balance        |




**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
| Amount of the price | balance |



**Combination of predicates**:


|Amount of the price| Valid/Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|
|<=balance|Valid|(400;true)|testCheckBalance()||
|>balance|Valid|(600;false)|testCheckBalance()||

 ### **Class *CreditCard* - *testCheckCreditCardLunh*



**Criteria for method *testCheckCreditCardLunh*:**
	

 -Validity of the String
 -Validity by Lunh





**Predicates for method *checkBalance*:**

| Criteria | Predicate |
| -------- | --------- |
|Validity of the String|Valid|
|          |null|
|          |empty|
|Validity by Lunh|Yes|
|          |No|




**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |



**Combination of predicates**:


|Validity of the String|Validity by Lunh| Valid/Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
|null|*|Invalid|(null;false)|testCheckCreditCardLunh()||
|empty|*|Invalid|("";false)|testCheckCreditCardLunh()||
|Valid|Yes|Valid|("4485370086510891";true)|testCheckCreditCardLunh()||
|Valid|No|Valid|("1234567891";false)|testCheckCreditCardLunh()||


 ### **Class *CusomersCounter* - *setCounter*



**Criteria for method *setCounter*:**
	

 - Validity of the setter





**Predicates for method *setCounter*:**

| Criteria | Predicate |
| -------- | --------- |
|Validity of the setter|Yes|
||No|




**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |



**Combination of predicates**:


|Validity of the setter| Valid/Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|
|Yes|Valid|T1(1;counter setted=1)|testSetCounter()||
|No|Invalid|-|-|

 ### **Class *CusomersCounter* - *add*



**Criteria for method *add*:**
	

 - Validity of the add





**Predicates for method *setCounter*:**

| Criteria | Predicate |
| -------- | --------- |
|Validity of the add|Yes|
||No|




**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |



**Combination of predicates**:


|Validity of the setter| Valid/Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|
|Yes|Valid|T1(1;add(), counter setted=2)|testAddCounter()||
|No|Invalid|-|-|

 ### **Class *CusomersCounter* - *sub*



**Criteria for method *sub*:**
	

 - Validity of the sub





**Predicates for method *sub*:**

| Criteria | Predicate |
| -------- | --------- |
|Validity of the sub|Yes|
||No|




**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |



**Combination of predicates**:


|Validity of the setter| Valid/Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|
|Yes|Valid|T1(1;sub(), counter setted=0)|testSubCounter()||
|No|Invalid|-|-|

### **Class *Position* - *setLocation*



**Criteria for method *setLocation*:**
	

 - Validity of the String location





**Predicates for method *sub*:**

| Criteria | Predicate |
| -------- | --------- |
|Validity of the string location|Valid|
||null|
||empty|




**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |



**Combination of predicates**:


|Validity of the string location| Valid/Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|
|Valid|Valid|T1("12-a-1";location setted="12-a-1")|testSetLocation()||
|null|Invalid|T2(null;location setted="")|testSetLocation()||
|empty|Invalid|T3("";location setted="")|testSetLocation()||
# White Box Unit Tests

### Test cases definition
    
    <JUnit test classes must be in src/test/java/it/polito/ezshop>
    <Report here all the created JUnit test cases, and the units/classes under test >
    <For traceability write the class and method name that contains the test case>


| Unit name | JUnit test case |
|--|--|
|TestCardCounter|testSetCounter|
||testAddCounter|
||testSubCounter|
|TestCreditCard|testCheckBalance|
||testCheckCreditCardLunh|
||testSetBalance|
||testSetCreditCardNumber|
|TestCustomersCounter|testSetCounter|
||testAddCounter|
||testSubCounter|
|TestEmployeesCounter|testSetCounter|
||testAddCounter|
||testSubCounter|
|TestMyCustomer|testAddPoints|
||testSetCustomerCard|
||testSetCustomerId|
||testSetCustomerName|
||testSetPoints|
|TestMyTicketEntry|testComputeTotal|
||testSetAmount|
||testSetAmountReturned|
||testSetBarcode|
||testSetDiscountRate|
||testSetPricePerUnit|
||testSetProductDescription|
||testUpdateAmountReturned|
|TestMyUser|testSetPassword|
||testSetRole|
||testSetUserId|
||testSetUsername|
||testVerifyAutentication|
|TestOperationsCounter|testSetCounter|
||testAddCounter|
||testSubCounter|
|TestPaymentCounter|testSetCounter|
||testAddCounter|
||testSubCounter|
|TestProductCounter|testSetCounter|
||testAddCounter|
||testSubCounter|
|TestTicketCounter|testSetCounter|
||testAddCounter|
||testSubCounter|
|TestPosition|testSetLocation|

### Code coverage report

###
![coverage](/Deliverables/immagini/coverage.png)



### Loop coverage analysis

    <Identify significant loops in the units and reports the test cases
    developed to cover zero, one or multiple iterations >

|Unit name | Loop rows | Number of iterations | JUnit test case |
|---|---|---|---|
|TestCreditCard|41|0|testCheckCreditCardLunh|
||45|1|testCheckCreditCardLunh|
||47|>=2|testCheckCreditCardLunh|



