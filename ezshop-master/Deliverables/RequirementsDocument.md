# Requirements Document 

Authors:
	Antonio Vespa s284101
	Pietro Borgaro s292501
	Giulio Corallo s282380

Date: 01/04/2021

Version: 1

# Contents

- [Essential description](#essential-description)
- [Stakeholders](#stakeholders)
- [Context Diagram and interfaces](#context-diagram-and-interfaces)
	+ [Context Diagram](#context-diagram)
	+ [Interfaces](#interfaces) 
	
- [Stories and personas](#stories-and-personas)
- [Functional and non functional requirements](#functional-and-non-functional-requirements)
	+ [Functional Requirements](#functional-requirements)
	+ [Non functional requirements](#non-functional-requirements)
- [Use case diagram and use cases](#use-case-diagram-and-use-cases)
	+ [Use case diagram](#use-case-diagram)
	+ [Use cases](#use-cases)
    	+ [Relevant scenarios](#relevant-scenarios)
- [Glossary](#glossary)
- [System design](#system-design)
- [Deployment diagram](#deployment-diagram)

# Essential description

Small shops require a simple application to support the owner or manager. A small shop (ex a food shop) occupies 50-200 square meters, sells 500-2000 different item types, has one or a few cash registers 
EZShop i a software application to:
* manage sales
* manage inventory
* manage customers
* support accountings


# Stakeholders

| Stakeholder name  | Description | 
| ----------------- |:-----------:|
| Customer | Customer of the shop, buys products |
| User |  Uses the system (manager, marketing, accounting emp, warehouse worker, cashier emp) |
| Buyer | CTO of the shop, decides to buy EZShop sw |
| EZShop developers | Who create the sw (Designers, programmers, sw engineers, Marketing people)|
| Reseller (Google Play, Apple Store)| Platform where sw is published, and sold |
| Maintainers | People in charge of fixing bugs, release new versions |
| Certification authority | Ministerial entities in charge of define laws about sw and its features about security, privacy, usage, ...|
| Cash register | Scans products in the cart of the customer through their barcode and update the avaible quantity |
| Server | Where EZShop runs|
| Fidelity card | Card owned by the customer whose aim is to identify him/her |
| Operative system | Operative system supported by the sw |
| Product | Items sold by the Shop |
| POS | Manages the interaction between EZShop and the Payment System of a certain credit card | 

# Context Diagram and interfaces

## Context Diagram

![ContextDiagram](/ezshop-master/Deliverables/immagini/contextdiagram.png)

## Interfaces

| Actor | Logical Interface | Physical Interface  |
| ------------- |:-------------:| -----:|
| User | Commands on GUI | Screen, mouse, keyboard, speakers |
| Fidelity card | Bar-code | Bar-code reader |
| Product | Bar-code | Bar-code reader |
| Cash register | API service | Laser beam |
| POS | API service | credit card reader |

# Stories and personas

- Persona 1: female, middle age, professional, low income, married.
- Persona 2: female,middle age, professional, high income, married.
- Persona 3: male, young, professional, high income, great focus on targets.
- Persona 4: female, elderly, grandmother, medium income, no familiarity with computers.
- Persona 5: male, young, interested in doing his job well, medium/low income.
- Persona 6: male, middle age, professional, medium income.
- Persona 7: female, middle age, professional, medium income, married.

- Giulia is 50, works as a cashier and she loves spending time with her family in the nature and she doesn't really love computers. She has to serve different clients in the least amount of time to avoid long queue at the cash desk. She needs a software at her cash desk easy to use and that can scan multiple items in a row as fast as possible.

- Alice is 40, she's the manager of a small clothing shop. She loves her shop and she would like to bring it to another level and make it more successfull. She is interested in having an instrument to check how a certain product sells in different seasons in order to buy the right quantities of supply at the right time.
She is also interested in having a valuable report of how many items are sold during the work period of each employee of the shop in order to understand their ability. 

- Franco is 35, works as manager of the Shop and he's very focused on optimization, and tries to create a welcoming work environment. He spents every morning of a usual work routine looking at data, statistics and reports about employees and sales. He need representative data, few indicators easy to consult.

- Maria is 65, she works as manager of the Shop and she's not very keen on computer and statistics. For many years her work was about manage employees, ensure that the balance is positive, and try to increase the fame of the shop's name. Now that the store needs a software, it must surely be easy to consult, intuitive and with everything she needs in the same page with one click.

- Gianluca is 28, works in marketing office and loves sports, nature, focusing on his objectives. He is very focused on work and tries always to give to the customer what he/she wants.  He need a software that could help him in having under control the popularity of products, make sure the shop has enough supplies of them, give an appropriate price to the products without overpaying. 

- Marco is 35 and works as accounting manager in a shop so he have to deal with numbers every day. Marco is meticolous and every calculation he makes must be correct, for this reason he need a system to keep track of the daily number of sales done by the shop in an effecient way. He also takes care of the finance of the store so he need the possibility to retrive the salary of every employees of the shop and do some calculations.

- Rebecca is 30 has two children and works in a warehouse of a shop, she has a busy life because she have to take care of children and work at the same time. Rebecca's needs is to update in efficient way the state of every product in the shop or their price without wasting time.

# Functional and non functional requirements

## Functional Requirements

| ID        | Description  |
| ------------- |:-------------:| 
|  **FR1**     | Manage sales |
|  FR1.1   | Recognize product by code and add price to cart |
|  FR1.2   | Sum prices of items in cart |
|  FR1.3   | Manage payment by cash through cash register |
|  FR1.4   | Manage payment by credit card through pos |
|  FR1.4   | Recognize fidelity card |
|  FR1.5   | Compute points earned by the customer according to shopped items, and add them to his profile |
|  FR1.6   | Apply discount (if any) |
|  FR1.7   | Automatically update inventory |
|  FR1.8   | Update transaction event to accounting's ledger |
|  FR1.9   | Print receipt |
|  **FR2**     | Manage inventory |
|  FR2.1   | Add product to inventory |
|  FR2.2   | Remove product from inventory |
|  FR2.3   | Retrieve product available quantities |
|  FR2.4   | Update product available quantities |
|  FR2.6   | Automatically reorder if product below threshold |
|  FR2.7   | Notify users if product below threshold |
|  FR2.8   | Search product |
|  FR2.9   | Update price of the product |
|  FR2.10  | View supplier description of the product |
|  FR2.11  | Make a new order to supplier |
|  **FR3** 	   | Manage customers |
|  FR3.1   | Add new customer profile |
|  FR3.2   | Remove customer profile |
|  FR3.3   | Search customer |
|  FR3.4   | Show customer profile details |
|  FR3.5   | Show customer shopping details |
|  FR3.6   | Search and list customers by characteristics |
|  FR3.7   | Make discounts available to customer if he reaches certain requirements |
|  FR3.8   | Add specific promotions to a customer |
|  FR3.9   | Trace type of products usually bought from a customer |
|  FR3.10  | Remove promotions from a customer after an expiration date |
|  FR3.11  | Add "points" to the fidelity card |
|  FR3.12  | Update "points" to the fidelity card after transactions |
|  **FR4** 	   | Support accounting |
|  FR4.1   | Update general ledger |
|  FR4.2   | Show sales per day/week/month/year... |
|  FR4.3   | Show gains, losses and expenses in certain period |
|  FR4.4   | Show work hours of a certain employee |
|  FR4.5   | Compute salary of a certain employee |
|  FR4.6   | View suppliers history orders |
|  FR4.7   | Show employee info |
|  FR4.8   | Update number of employes |
|  FR4.9   | Update salary of a certain employee |
|  **FR5** 	  | Marketing Support |
| FR5.1 | Compute correllation between products bought together |
| FR5.2 | List products of the shop according to their popularity |
| FR5.3 | Compute list of most purchased brand for every type of product |
| FR5.4 | Show price trend of a certain product |
| FR5.5 | Compute list of more bought product with same price |
| FR5.6 | Search sales according to customer categories |

## Non Functional Requirements

| ID        | Type (efficiency, reliability, ..)           | Description  | Refers to |
| ------------- |:-------------:| :-----:| -----:|
|  NFR1     | Response time | To any search action to interact with the software in less than 1 sec | All |
|  NFR2     | Usability  | Time to learn the software by the user in less than 15 minutes of training | All |
|  NFR3     | Portability | EZShop should be usable with different versions of Windows | All |
|  NFR4     | Privacy | Customer data should not be shared with third parties | FR3, FR5 |
|  NFR5     | Reliability | System should work 99.9% of the time | All |
|  NFR6     | Reliability | MTTF should be over 2 month | All |
|  NFR7     | Efficiency | Productivity of accounting, sales force, cashier employees should increase of 50% | All | 

# Use case diagram and use cases

## Use case diagram

![UseCase](/Deliverables/immagini/UseCaseDiagram.png)

### Use case 1, UC1
| Actors Involved        | User |
| ------------- |:-------------:| 
|  Precondition     | User/Cashier serves a customer correctly registered |  
|  Post condition     | Transaction ends |
|  Nominal Scenario     | Customer wants to pay with cash |
|  Variants     | Customer wants to pay with credit card |
|  Exception     | Customer pays with credit card but transaction fails |

##### Scenario 1.1 

| Scenario 1.1 | Customer pays with cash |
| ------------- |:-------------:| 
|  Precondition     | User/Cashier serves a customer correctly registered |
|  Post condition     | Transaction ends |
| Step#        | Description  |
|  1     | User scans customer's fidelity card |  
|  2     | User scans every product customer wants to buy |
|  3     | System sum the prices of each product and create a bill |
|  4     | System subract discount if customer has the right requirements |
|  5     | User selects payment with cash |
|  6     | User inserts cash given by customer in cash desk and gives the rest back to customer |
|  7     | System prints receipt |
|  8     | Points are added to the fidelity card |

##### Scenario 1.2

| Scenario 1.2 | Customer pays with credit card |
| ------------- |:-------------:| 
|  Precondition     | User/Cashier serves a customer correctly registered |
|  Post condition     | Transaction ends |
| Step#        | Description  |
|  1     | User scans customer's fidelity card |  
|  2     | User scans every product customer wants to buy |
|  3     | System sum the prices of each product and create a bill |
|  4     | System subract discount if customer has the right requirements |
|  5     | User selects payments with card and gives pos to customer |
|  6     | Customer inserts pin and transaction starts |
|  7     | Exchange of information between POS and payment system |
|  8     | System prints receipt |
|  9     |  Points are added to the fidelity card |

##### Scenario 1.3

| Scenario 1.3 | Customer pays with credit card but transaction fails |
| ------------- |:-------------:| 
|  Precondition     | User/Cashier serves a customer correctly registered |
|  Post condition     | Transaction ends |
| Step#        | Description  |
|  1     | User scans customer's fidelity card |  
|  2     | User scans every product customer wants to buy |
|  3     | System sum the prices of each product and create a bill |
|  4     | System subract discount if customer has the right requirements |
|  5     | User selects payments with card and gives pos to customer |
|  6     | Customer inserts pin and transaction starts |
|  7     | Transaction fails, system gives two options: retry or abort |

### Use case 2, UC2
| Actors Involved        | User, fidelity card |
| ------------- |:-------------:| 
|  Precondition     | Customer wants a fidelity card |  
|  Post condition     | Customer is registered |
|  Nominal Scenario     | Customer is not registered and gets his fidelity card |
|  Variants     | Customer is already registered but has lost his card and wants a new fidelity card |

##### Scenario 2.1 

| Scenario 2.1 | Customer is not registered and gets his fidelity card |
| ------------- |:-------------:| 
|  Precondition     | Customer wants a fidelity card |
|  Post condition     | Customer is registered |
| Step#        | Description  |
|  1     | User create a new customer profile |  
|  2     | User asks customer for his data and fills the form given by the system |
|  3     | Customer is added to the database and new client code is assigned to him by the system |
|  4     | A new fidelity card is registered by the system and is given to the customer |

##### Scenario 2.2 

| Scenario 2.2 | Customer is already registered and gets a new fidelity card |
| ------------- |:-------------:| 
|  Precondition     | Customer wants a fidelity card |
|  Post condition     | Customer is registered |
| Step#        | Description  |
|  1     | User create a new customer profile |  
|  2     | User asks customer for his data and fills the form given by the system |
|  3     | System warns user that the customer is already registered with a given code |
|  4     | A new fidelity card is assigned to the customer with his old code |

### Use case 3, UC3
| Actors Involved        | User |
| ------------- |:-------------:| 
|  Precondition     | User has rights to acess system accounting management |  
|  Post condition     | User has access to data |
|  Nominal Scenario     | User wants to see shop general accounting data |
|  Variants     | User wants to see employee accounting data |

##### Scenario 3.1 

| Scenario 3.1 | User wants to see shop general accounting data |
| ------------- |:-------------:| 
|  Precondition     | User has rights to acess system accounting management |
|  Post condition     | User has access to data |
| Step#        | Description  |
|  1     | User asks the system to see shop monthly sales |  
|  2     | System selects sales from database and order them by month |
|  3     | System show through a GUI the retrieved data |

##### Scenario 3.2 

| Scenario 3.2 | User wants to see employee accounting data |
| ------------- |:-------------:| 
|  Precondition     | User has rights to acess system accounting management |
|  Post condition     | User has access to data |
| Step#        | Description  |
|  1     | User asks the system to see sales data of a certain employee |  
|  2     | User search for employee |
|  3     | User selects employee |
|  4     | System retrieves data regarding selected employee from database |

### Use case 4, UC4
| Actors Involved        | User,Product |
| ------------- |:-------------:| 
|  Precondition     | User has the right privileges to see inventory |  
|  Post condition     | Application return a feedback message  |
|  Nominal Scenario     | Warehouse worker wants to apply changes on inventory |
|  Variants     | - |

##### Scenario 4.1

| Scenario 4.1 | Warehouse worker wants to modify the available quantities for a product  |
| ------------- |:-------------:| 
|  Precondition     | User has the right privileges to see inventory |
|  Post condition     | Application return a feedback message |
| Step#        | Description  |
|  1     | Warehouse worker searches product in the database  |  
|  2     | The product is retrieved by the application |
|  3     | Warehouse worker pushes modify button  |
| 4      | Warehouse worker modifies the available quantities |
|  5     | Warehouse worker pushes button apply changes  |
|  6     | A message of successfull operation is returned |

##### Scenario 4.2

| Scenario 4.2 | Warehouse worker wants to modify the available quantities for a product but the product is not present |
| ------------- |:-------------:| 
|  Precondition     | User has the right privileges to see inventory |
|  Post condition     | Application return a feedback message |
| Step#        | Description  |
|  1     | Warehouse worker searches the product in the inventory  |  
|  2     | Product is not found |
|  3     | Application return an error message  |

##### Scenario 4.3

| Scenario 4.3 | Warehouse worker wants to insert a product in the inventory|
| ------------- |:-------------:| 
|  Precondition     | User has the right privileges to see inventory |
|  Post condition     | Application return a feedback message |
| Step#        | Description  |
|  1     | Warehouse worker pushes insert button to insert a new product |  
|  2     | Warehouse worker fills all the fields specifing identifier, price, available quantities,... |
|  3     | Warehouse worker pushes button apply changes  |
|  4     | A message of successfull operation is returned |

##### Scenario 4.4

| Scenario 4.4 | Warehouse worker wants to remove a product from the inventory|
| ------------- |:-------------:| 
|  Precondition     | User has the right privileges to see inventory |
|  Post condition     | Application return a feedback message |
| Step#        | Description  |
|  1     | Warehouse searches the product in the inventory |  
|  2     | The product is retrieved by the application |
|  3     | Warehouse worker pushes remove button |
|  4     | The product is removed from the inventory |
|  5     | A message of successfull operation is returned |

##### Scenario 4.5

| Scenario 4.5 | Warehouse worker wants to modify the price of a product from the inventory |
| ------------- |:-------------:| 
|  Precondition     | User has the right privileges to see inventory |
|  Post condition     | Application return a feedback message |
| Step#        | Description  |
|  1     | Warehouse searches the product in the inventory |  
|  2     | The product is retrieved by the application |
|  3     | Warehouse worker pushes modify button |
|  4     | Warehouse worker modifies the price of the product |
|  5     | Warehouse worker pushes button apply changes  |
|  6     | A message of successfull operation is returned |

### Use case 5, UC2
| Actors Involved        | User |
| ------------- |:-------------:| 
|  Precondition     | User has the right privileges to see accounting |  
|  Post condition     | Application return a feedback message  |
|  Nominal Scenario     | Accounting person wants to update information about accounting |
|  Variants     | Accounting person wants to show information |

##### Scenario 5.1

| Scenario 5.1 | Accounting person wants to update general ledger |
| ------------- |:-------------:| 
|  Precondition     | User has the right privileges to see inventory |
|  Post condition     | Application return a feedback message |
| Step#        | Description  |
|  1     | User has access to accounting system  |  
|  2     | User selects general ledger of the shop |
|  3     | User does modification on the ledger  |
|  4     | Application saves changes |
|  5     | A message of successfull operation is returned  |

##### Scenario 5.2

| Scenario 5.2 | Accounting person wants to update salary of a certain employee |
| ------------- |:-------------:| 
|  Precondition     | User has the right privileges to see inventory |
|  Post condition     | Application return a feedback message |
| Step#        | Description  |
|  1     | User has access to accounting system  |  
|  2     | User selects shop finance |
|  3     | User searches the employee  |
|  4     | Application retrieves selected employee |
|  5     | User modifies employee's salary  |
|  6     | Application saves changes  |
|  7     | A message of successfull operation is returned  |

##### Scenario 5.3

| Scenario 5.3 | Accounting person wants to see gain and losses |
| ------------- |:-------------:| 
|  Precondition     | User has the right privileges to see inventory |
|  Post condition     | Application returns information |
| Step#        | Description  |
|  1     | User has access to accounting system  |  
|  2     | User selects shop finance |
|  3     | User selects ranks  |
| 4      | User selects the period when show ranks |
|  5     | Application computes gain and losses according to the period  |
|  6     | Application shows in the GUI gain and losses |

### Use case 6, UC6
| Actors Involved        |  |
| ------------- |:-------------:| 
|  Precondition     | User wants to access marketing data management, data are updated to date |  
|  Post condition     | Data is consistent |
|  Nominal Scenario     | User wants to see marketing data |
|  Variant    | User wants to print a report about marketing data  |
|  Variant    | User wants to modify marketing data  |
|  Exception     | User doesn't have access to marketing data  |

##### Scenario 6.1 

| Scenario 6.1 | User wants to see marketing data and has rights |
| :-------------: |-------------| 
|  Precondition     | User wants to access marketing data management, data are updated to date |  
|  Post condition     | Data is consistent |
| Step#        | Description  |
|  1     | User asks the system to see marketing data |  
|  2     | System asks to the user pw and username |
|  3     | User inserts pw and username |
|  4     | System selects data from database and orders them |
|  5     | System show through a GUI the retrieved data |
|  6     | User interacts with the GUI |
|  7     | User logs out |

##### Scenario 6.2

| Scenario 6.2 | User wants to see marketing data and has rights |
| :-------------: |-------------| 
|  Precondition     | User wants to access marketing data management, data are updated to date |  
|  Post condition     | Data is consistent |
| Step#        | Description  |
|  1     | User asks the system to see marketing data |  
|  2     | System asks to the user pw and username |
|  3     | User inserts pw and username incorrect/false |
|  4     | System closes the current session and opens a new one from #step2 |

##### Scenario 6.3

| Scenario 6.3 | User wants to print a report about marketing data |
| :-------------: |-------------| 
|  Precondition     | User wants to access marketing data management, data are updated to date |  
|  Post condition     | Data is consistent |
| Step#        | Description  |
|  1     | User asks the system to see marketing data |  
|  2     | System asks to the user pw and username |
|  3     | User inserts pw and username |
|  4     | System selects data from database and orders them |
|  5     | User asks for a specific report |
|  6     | System show through a GUI the retrieved data |
|  7     | User interacts with the GUI and asks to print it |
|  8     | Report is printed |
|  9     | User logs out |

##### Scenario 6.4

| Scenario 6.4 | User wants to modify marketing data |
| :-------------: |-------------| 
|  Precondition     | User wants to access marketing data management, data are updated to date |  
|  Post condition     | Data is consistent |
| Step#        | Description  |
|  1     | User asks the system to see marketing data |  
|  2     | System asks to the user pw and username |
|  3     | User inserts pw and username |
|  4     | System selects data from database and orders them |
|  5     | User asks for a specific one |
|  6     | System show through a GUI the retrieved data |
|  7     | User modifies data |
|  8     | System saves the new state |
|  9     | User logs out |

### Use case 7, UC7
| Actors Involved        |  |
| ------------- |:-------------:| 
|  Precondition     | User wants to access management data, data are updated to date |  
|  Post condition     | Data is consistent |
|  Nominal Scenario     | User wants to see management data |
|  Variant    | User wants to print a report about management data  |
|  Variant    | User wants to interact with data, changing representation  |
|  Variant    | User wants to remove old data  |
|  Exception     | User doesn't have access to management data  |

##### Scenario 7.1 

| Scenario 7.1 |  User wants to see management data |
| :-------------: |-------------| 
|  Precondition     | User wants to access management data, data are updated to date |  
|  Post condition     | Data is consistent |
| Step#        | Description  |
|  1     | User asks the system to see management data |  
|  2     | System asks to the user pw and username |
|  3     | User inserts pw and username |
|  4     | System selects data from database and orders them |
|  5     | System show through a GUI the retrieved data |
|  7     | User logs out |

##### Scenario 7.2

| Scenario 7.2 | User doesn't have access to management data |
| :-------------: |-------------| 
|  Precondition     | User wants to access management data, data are updated to date |  
|  Post condition     | Data is consistent |
| Step#        | Description  |
|  1     | User asks the system to see management data |  
|  2     | System asks to the user pw and username |
|  3     | User inserts pw and username incorrect/false |
|  4     | System closes the current session and opens a new one from #step2 |

##### Scenario 7.3

| Scenario 7.3 | User wants to print a report about management data |
| :-------------: |-------------| 
|  Precondition     | User wants to access management data, data are updated to date |  
|  Post condition     | Data is consistent |
| Step#        | Description  |
|  1     | User asks the system to see management data |  
|  2     | System asks to the user pw and username |
|  3     | User inserts pw and username |
|  4     | System selects data from database and orders them |
|  5     | User asks for a specific report |
|  6     | System show through a GUI the retrieved data |
|  7     | User interacts with the GUI and asks to print it |
|  8     | Report is printed |
|  9     | User logs out |

##### Scenario 7.4

| Scenario 7.4 | User wants to interact with data, changing representation |
| :-------------: |-------------| 
|  Precondition     | User wants to access management data, data are updated to date |  
|  Post condition     | Data is consistent |
| Step#        | Description  |
|  1     | User asks the system to see management data |  
|  2     | System asks to the user pw and username |
|  3     | User inserts pw and username |
|  4     | System selects data from database and orders them |
|  5     | User asks for a specific one |
|  6     | User asks for a specific representation |
|  7     | System show through a GUI the retrieved data |
|  9     | User logs out |

##### Scenario 7.5

| Scenario 7.5 | User wants to remove old data |
| :-------------: |-------------| 
|  Precondition     | User wants to access management data, data are updated to date |  
|  Post condition     | Data is consistent |
| Step#        | Description  |
|  1     | User asks the system to see management data |  
|  2     | System asks to the user pw and username |
|  3     | User inserts pw and username |
|  4     | System selects data from database and orders them |
|  5     | User asks for data befone a certain date |
|  6     | System show through a GUI the retrieved data |
|  7     | User asks for data seleted to be removed |
|  8     | System verifies dependences between data |
|  9     | If there aren't, system deletes selected data |
|  10     | System save the new state |
|  11     | User logs out |

# Glossary
 
![Glossary](/ezshop-master//Deliverables/immagini/Glossary.png)

# System Design

No System Design since EZShop is a Software product.

# Deployment Diagram 

![DeploymentDiagram](/ezshop-master//Deliverables/immagini/dep_diagram.png)

