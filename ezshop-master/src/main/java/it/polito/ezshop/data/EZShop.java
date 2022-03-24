package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;


public class EZShop implements EZShopInterface {
	
	private Employees emp=new Employees();
	private Catalogue catalogue = new Catalogue();
	private User loggedUser=null;
	private CustomerList clist = new CustomerList();
	private AccountBook book = new AccountBook();
	private HashMap<String,CreditCard> registeredCards= new HashMap<String,CreditCard>();
	private DataBase db= new DataBase(this);
	 
	public EZShop(){ 
		try {
			createUser("SuperUser", "1234", "Administrator");
			login("SuperUser", "1234");
			db.createTableCustomers();
			db.createTableEmployees();
			db.createTableProducts();
			db.createTableOperations(); 
			db.createTableOrders();
			db.createTableSales();
			db.createTableEntries();
			db.createTableRFIDs();
			computeBalance();
			deleteUserByName("SuperUser","1234");
			logout();
			db.setRunning();
			//db per carte
			//db per prodotti
			//db per transactions
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		FileReader f = null;
	    try {
	    	URL url = getClass().getClassLoader().getResource("CreditCard");
	    	File file= new File(url.getPath());
			f=new FileReader(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    BufferedReader b;
	    b=new BufferedReader(f);

	    String line;
        try {
			while((line=b.readLine())!=null) {
			  if(line.startsWith("#"))
				  continue; 
			  else {
				  String cardNumber=line.split(";")[0];
				  double balance=Double.parseDouble(line.split(";")[1]);
				  CreditCard cc=new CreditCard(cardNumber,balance);
				  registeredCards.put(cardNumber, cc);
			  }
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public boolean recoverRfids(String barCode, String rfid, Integer ticketNumber){
		MyProductType pt = (MyProductType) catalogue.getProductTypeByBarCode(barCode); 
    	catalogue.addToRfidsProducts(rfid, pt);
    	pt.addRFID(rfid);
		if(ticketNumber!=-1) //se è stato venduto
			pt.addToSoldRfids(rfid, ticketNumber);
		return true;
	}
	
	public boolean recoverBalanceOperation(Integer balanceID,String typeOp,Double money,LocalDate date,String time,String status){
		return book.recoverBalanceOperation(balanceID,typeOp,money,date,time,status);
	}

	public boolean recoverSale(Integer ticketNumber, Double discountRate, Double price, Integer pointsEarned, String status){
		return book.recoverSale(ticketNumber, discountRate, price, pointsEarned, status);
	}

	public boolean recoverTicket(Integer ticketNumber, String barCode, String description, Integer amount, Double pricePerUnit, Double total, Double discountRate, Integer amountReturned){
		return book.recoverTicket(ticketNumber, barCode, description, amount, pricePerUnit,total,discountRate,amountReturned);
	}
 
	public void addDbRfids(Integer ticketNumber, String barCode, String rfid) {
		//se ticketNumber==-1 allora order, altrimenti venduto
		db.addRfid(barCode,rfid,ticketNumber); 
    }
	public void removeDbRfids(String rfid) {
		db.clearRfid(rfid);
    }

	public void updateDbProducts() {
    	List<ProductType> list = null;
		try {
			db.clearTableProducts();
			list = getAllProductTypes();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(ProductType i:list){
			db.addProduct(i.getId(), i.getBarCode(), i.getProductDescription(), i.getPricePerUnit(), i.getNote(), i.getQuantity(), i.getLocation());
		}
    }

	public void updateDbCustomers() {
    	List<Customer> list = null;
		try {
			db.clearTableCustomers();
			list = getAllCustomers();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(Customer i:list){
			db.addCustomer(i.getId(), i.getCustomerName(), i.getCustomerCard(), ((MyCustomer)i).getPoints());
		}
    }

	public void updateDbUsers() {
    	List<User> list = null;
		try {
			db.clearTableEmployees();
			list = getAllUsers();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(list==null)
			return;
		for(User i:list){
			db.addUser(i.getId(), i.getUsername(), i.getPassword(), i.getRole());
		}
    }

	public void updateDbOrders() {
    	List<Order> list = null;
		try {
			db.clearTableOrders();
			list = getAllOrders();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(Order i:list){
			db.addOrder((MyOrder)i);
		}
    }

	public boolean updateDbSales(SaleTransaction sale) {
		return db.addSale((MySaleTransaction)sale);
    }

	public void updateDbEntry(MyTicketEntry entry, Integer saleID) {
		db.clearEntry(entry,saleID); 
		db.addEntry(entry,saleID);
    }
	
	public void updateDbEntries(MySaleTransaction sale) {
    	List<TicketEntry> list = null;
		try {
			//db.clearTableEntries();
			list = sale.getEntries();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(TicketEntry i:list){
			db.addEntry((MyTicketEntry)i,sale.getTicketNumber());
		}
    }
	/*public boolean updateDbSales() {
	List<SaleTransaction> list = null;
	try {
		db.clearTableSales();
		list = book.getAllSales();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	for(SaleTransaction i:list){
		db.addSale((MySaleTransaction)i);
	} 
}*/

	public void updateDbOperations() {
    	List<BalanceOperation> list = null;
		try {
			db.clearTableOperations();
			list = book.getBalanceOperationByDate(null,null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(BalanceOperation i:list){
			db.addOperation((MyBalanceOperation)i);
		}
    }

    @Override
    public void reset() {
    	catalogue = new Catalogue();
    	book = new AccountBook();
		emp = new Employees();
		clist = new CustomerList();
		db.clearTableCustomers();
		db.clearTableEmployees();
		db.clearTableOperations();
		db.clearTableEntries();
		db.clearTableProducts();
		db.clearTableSales();
		db.clearTableOrders();
		//CHANGES
		db.clearRfids();
    }

    @Override
    public Integer createUser(String username, String password, String role) throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
        if(username==null || username.isEmpty())
        	throw new InvalidUsernameException();
        if(password==null || password.isEmpty())
        	throw new InvalidPasswordException();
        if(role==null || role.isEmpty() || 
        		(role.compareTo("Administrator")!=0 &&
        		role.compareTo("Cashier")!=0 && role.compareTo("ShopManager")!=0))
        	throw new InvalidRoleException("Invalid role");
        Integer newID=emp.addUser(username,password,role);
        if(newID <= 0)
        	return -1;
		else{
			if(loggedUser==null){
				User u = emp.getUser(newID);
				db.addUser(u.getId(), u.getUsername(), u.getPassword(), u.getRole());
			}
			if(username.compareTo("SuperUser")!=0 && username.compareTo("ADMIN")!=0 && loggedUser!=null)
				updateDbUsers();
		}
        return newID;
    }

    @Override
    public boolean deleteUser(Integer id) throws InvalidUserIdException, UnauthorizedException {
        if(id==null || id<=0 )
        	throw new InvalidUserIdException();
        if(loggedUser==null || loggedUser.getRole().compareTo("Administrator")!=0)
        	throw new UnauthorizedException();
        boolean ret = emp.deleteUser(id);
		if(ret)
			updateDbUsers();
		return ret;
    }

	
    public boolean deleteUserByName(String name,String pass) throws InvalidUserIdException, UnauthorizedException {
        User u = emp.searchUser(name,pass);
        if(u==null)
        	return false;
        Integer id= u.getId();
		return deleteUser(id);
    }

    @Override
    public List<User> getAllUsers() throws UnauthorizedException {
    	if(loggedUser==null || loggedUser.getRole().compareTo("Administrator")!=0)
        	throw new UnauthorizedException();
        return emp.getAllUsers();
    }

    @Override
    public User getUser(Integer id) throws InvalidUserIdException, UnauthorizedException {
    	if(loggedUser==null || loggedUser.getRole().compareTo("Administrator")!=0)
        	throw new UnauthorizedException();
    	if(id==null || id<=0)
        	throw new InvalidUserIdException("Invalid id");
    	
        return emp.getUser(id);
    }

    @Override
    public boolean updateUserRights(Integer id, String role) throws InvalidUserIdException, InvalidRoleException, UnauthorizedException {
    	if(loggedUser==null || loggedUser.getRole().compareTo("Administrator")!=0)
        	throw new UnauthorizedException("Unauthorized");
    	if(id==null || id<=0)
        	throw new InvalidUserIdException();
    	if(role==null || role.isEmpty() || 
        		(role.compareTo("Administrator")!=0 &&
        		role.compareTo("Cashier")!=0 && role.compareTo("ShopManager")!=0))
        	throw new InvalidRoleException();
    	MyUser user=emp.getUser(id);
    	if(user==null)
    		return false;
    	user.setRole(role);
		updateDbUsers();
    	return true;
    }

    @Override
    public User login(String username, String password) throws InvalidUsernameException, InvalidPasswordException {
    	 if(username==null || username.isEmpty())
         	throw new InvalidUsernameException("Invalid Username");
         if(password==null || password.isEmpty())
         	throw new InvalidPasswordException("Invalid Password");
        loggedUser=emp.searchUser(username,password);
        return loggedUser;
    }

    @Override
    public boolean logout() {
    	if(loggedUser==null)
    		return false;
    	else {
    		loggedUser=null;
    		return true;
    	}
    }
    @Override
    public Integer createProductType(String description, String productCode, double pricePerUnit, String note) throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
    	if(loggedUser == null)
    		throw new UnauthorizedException();
    	String role = loggedUser.getRole();
    	if(!role.equals("Administrator") && !role.equals("ShopManager"))
    		throw new UnauthorizedException();
    	
    	if(description == null || description.isEmpty())
    		throw new InvalidProductDescriptionException("Invalid description");
    	if(productCode == null || productCode.isEmpty() || !Pattern.matches("\\d{12,14}", productCode) || !catalogue.checkBarCode(productCode)) //regexp per verificare che sia un numero (12-14 cifre)
    		throw new InvalidProductCodeException("Invalid code");
    	if(pricePerUnit <= 0)
    		throw new InvalidPricePerUnitException("Invalid price");
    	
    	if(catalogue.getProductTypeByBarCode(productCode) != null) //se esiste già un prodotto con lo stesso barCode
    		return -1;
    	
    	Integer newID = catalogue.addProductType(description, productCode, pricePerUnit, note);
		updateDbProducts();
    	return newID;
    }

    @Override
    public boolean updateProduct(Integer id, String newDescription, String newCode, double newPrice, String newNote) throws InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
    	if(loggedUser == null)
    		throw new UnauthorizedException();
    	String role = loggedUser.getRole();
    	if(!role.equals("Administrator") &&  !role.equals("ShopManager"))
    		throw new UnauthorizedException();
    	
    	if(id == null || id <= 0)
    		throw new InvalidProductIdException();
    	if(newDescription == null || newDescription.isEmpty())
    		throw new InvalidProductDescriptionException();
    	if(newCode == null || newCode.isEmpty() || !Pattern.matches("\\d{12,14}", newCode) || !catalogue.checkBarCode(newCode)) //regexp per verificare che sia un numero (12-14 cifre) e controllo valid barCode!
    		throw new InvalidProductCodeException();
    	if(newPrice <= 0)
    		throw new InvalidPricePerUnitException();
    	
    	MyProductType mpt = catalogue.getProductTypeById(id);
    	if(mpt == null) //non c'è nessun product con quell'ID
			return false;
    	
	    if(!mpt.getBarCode().equals(newCode) && catalogue.getProductTypeByBarCode(newCode) != null) //non ho capito -giulio
			return false;
		
			
		mpt.setProductDescription(newDescription);
		mpt.setBarCode(newCode);
		mpt.setPricePerUnit(newPrice);
		mpt.setNote(newNote);
    	updateDbProducts();
    	return true;
    }

    @Override
    public boolean deleteProductType(Integer id) throws InvalidProductIdException, UnauthorizedException {
    	if(loggedUser == null)
    		throw new UnauthorizedException();
    	String role = loggedUser.getRole();
    	if(!role.equals("Administrator") &&  !role.equals("ShopManager"))
    		throw new UnauthorizedException();
    	
    	if(id == null || id <= 0)
    		throw new InvalidProductIdException();
    	
    	if(catalogue.deleteProductType(id) == false)
    		return false;
		
		updateDbProducts();
    	return true;
    }

    @Override
    public List<ProductType> getAllProductTypes() throws UnauthorizedException {
    	if(loggedUser == null)
    		throw new UnauthorizedException();
    	/*String role = loggedUser.getRole();
    	if(!role.equals("Administrator") &&  !role.equals("ShopManager") && !role.equals("Cashier")) INUTILE --giulio
    		throw new UnauthorizedException();
    	*/
    	
    	return catalogue.getAllProductTypes();
    }

    @Override
    public ProductType getProductTypeByBarCode(String barCode) throws InvalidProductCodeException, UnauthorizedException {
    	if(loggedUser == null)
    		throw new UnauthorizedException();
    	String role = loggedUser.getRole();
    	
    	
    	if(!role.equals("Administrator") &&  !role.equals("ShopManager"))
    		throw new UnauthorizedException();
    	
    	
    	if(barCode == null || barCode.isEmpty() || !Pattern.matches("\\d{12,14}", barCode) || !catalogue.checkBarCode(barCode)) //regexp per verificare che sia un numero (12-14 cifre) e controllo valid barCode!
    		throw new InvalidProductCodeException();
    	
    	return catalogue.getProductTypeByBarCode(barCode);
    }

    @Override
    public List<ProductType> getProductTypesByDescription(String description) throws UnauthorizedException {
    	if(loggedUser == null)
    		throw new UnauthorizedException();
    	String role = loggedUser.getRole();
    	if(!role.equals("Administrator") &&  !role.equals("ShopManager"))
    		throw new UnauthorizedException();
    	
    	if(description == null)
    		description = "";
    	
    	return catalogue.getProductTypesByDescription(description);
    }

    @Override
    public boolean updateQuantity(Integer productId, int toBeAdded) throws InvalidProductIdException, UnauthorizedException {
    	if(loggedUser == null)
    		throw new UnauthorizedException();
    	String role = loggedUser.getRole();
    	if(!role.equals("Administrator") &&  !role.equals("ShopManager"))
    		throw new UnauthorizedException();
    	
    	if(productId == null || productId <= 0)
    		throw new InvalidProductIdException();
    	
    	MyProductType mpt = catalogue.getProductTypeById(productId);
    	
    	if(mpt == null || mpt.getLocation().isEmpty())
    		return false;
    	
    	if(mpt.updateQuantity(toBeAdded) == false)
    		return false;
		
		updateDbProducts();
    	return true;
    }

    @Override
    public boolean updatePosition(Integer productId, String newPos) throws InvalidProductIdException, InvalidLocationException, UnauthorizedException {
    	if(loggedUser == null)
    		throw new UnauthorizedException();
    	String role = loggedUser.getRole();
    	if(!role.equals("Administrator") &&  !role.equals("ShopManager"))
    		throw new UnauthorizedException();
    	
    	if(productId == null || productId <= 0)
    		throw new InvalidProductIdException("Invalid id");
    	
    	if(newPos!=null && newPos!="" && !Pattern.matches("\\d+-[a-zA-Z]-\\d+", newPos))
    		throw new InvalidLocationException("Invalid location");
    	
    	MyProductType mpt = catalogue.getProductTypeById(productId);
    	
    	if(mpt == null) //se non esiste nessun product con quell'ID
    		return false;
    	
    	if(newPos == null || newPos.isEmpty())
    		mpt.setLocation(newPos); //reset posizione
    	
    	
		String pos = new String();
    	if(!(newPos == null || newPos.isEmpty())){
    		String [] fields = newPos.split("-");
    		fields[1] = fields[1].toUpperCase();
			pos = fields[0]+"-"+fields[1]+"-"+fields[2];
	
    		if(catalogue.checkPosition(pos) == false) //controllo univocità posizione
    			return false;
    	
    		mpt.setLocation(pos);
		}
    	updateDbProducts();
    	return true;
    }
	//byanto
	public boolean recoverOrder(Integer orderID, String productCode, double pricePerUnit, Integer quantity, String status){
		return book.recoverOrder(orderID, productCode,pricePerUnit,quantity,status);
	}

    @Override
    public Integer issueOrder(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
    	if(loggedUser == null)
    		throw new UnauthorizedException();
    	String role = loggedUser.getRole();
    	if(!role.equals("Administrator") &&  !role.equals("ShopManager"))
    		throw new UnauthorizedException();
    	
    	if(productCode == null || productCode.isEmpty() || !Pattern.matches("\\d{12,14}", productCode) || !catalogue.checkBarCode(productCode)) //regexp per verificare che sia un numero (12-14 cifre) e controllo valid barCode!
    		throw new InvalidProductCodeException();
    	if(quantity <= 0)
    		throw new InvalidQuantityException();
    	if(pricePerUnit <= 0)
    		throw new InvalidPricePerUnitException();
    	
    	if(catalogue.getProductTypeByBarCode(productCode) == null) //se il prodotto non esiste
    		return -1;
    	
    	// AccountBook e BalanceOperation --->controllare
    	Integer orderID = book.issueOrder(productCode, quantity, pricePerUnit);
    	updateDbOrders();
    	return orderID;
    }

    @Override
    public Integer payOrderFor(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
    	if(loggedUser == null)
    		throw new UnauthorizedException();
    	String role = loggedUser.getRole();
    	if(!role.equals("Administrator") &&  !role.equals("ShopManager"))
    		throw new UnauthorizedException();
    	
    	if(productCode == null || productCode.isEmpty() || !Pattern.matches("\\d{12,14}", productCode) || !catalogue.checkBarCode(productCode)) //regexp per verificare che sia un numero (12-14 cifre) e controllo valid barCode!
    		throw new InvalidProductCodeException();
    	if(quantity <= 0)
    		throw new InvalidQuantityException("Invalid quantity");
    	if(pricePerUnit <= 0)
    		throw new InvalidPricePerUnitException();
    	
    	if(catalogue.getProductTypeByBarCode(productCode) == null) //se il prodotto non esiste
    		return -1;
    	
    	Integer orderID = book.payOrderFor(productCode, quantity, pricePerUnit); //può anche ritornare -1
    	updateDbOrders();
		updateDbOperations();
    	return orderID;
    }

    @Override
    public boolean payOrder(Integer orderId) throws InvalidOrderIdException, UnauthorizedException {
    	if(loggedUser == null)
    		throw new UnauthorizedException();
    	String role = loggedUser.getRole();
    	if(!role.equals("Administrator") &&  !role.equals("ShopManager"))
    		throw new UnauthorizedException();
    	
    	if(orderId == null || orderId <= 0)
    		throw new InvalidOrderIdException();
    	
    	boolean ret = book.payOrder(orderId);
    	if(ret){
			updateDbOrders();
    		updateDbOperations();
		}
    	return ret;
    }

    @Override
    public boolean recordOrderArrival(Integer orderId) throws InvalidOrderIdException, UnauthorizedException, InvalidLocationException {
    	if(loggedUser == null)
    		throw new UnauthorizedException();
    	String role = loggedUser.getRole();
    	if(!role.equals("Administrator") &&  !role.equals("ShopManager"))
    		throw new UnauthorizedException();
    	
    	if(orderId == null || orderId <= 0)
    		throw new InvalidOrderIdException("Invalid id");
    	
    	MyOrder ord = book.getOrderById(orderId);
    	if(ord==null)
			return false;
		String barCode = ord.getProductCode();
    	MyProductType pt = (MyProductType) catalogue.getProductTypeByBarCode(barCode); //controllare cast !!
    	if(pt.getLocation().isEmpty())
    		throw new InvalidLocationException();
    	
    	if(!book.recordOrderArrival(orderId))
    		return false;
    	
    	pt.updateQuantity(ord.getQuantity());
    	
    	updateDbOrders();
    	updateDbProducts();
    	
    	return true;
    }

    @Override 
    public boolean recordOrderArrivalRFID(Integer orderId, String RFIDfrom) throws InvalidOrderIdException, UnauthorizedException, InvalidLocationException, InvalidRFIDException {
    	if(loggedUser == null)
    		throw new UnauthorizedException();
    	String role = loggedUser.getRole();
    	if(!role.equals("Administrator") &&  !role.equals("ShopManager"))
    		throw new UnauthorizedException();
    	
    	if(orderId == null || orderId <= 0)
    		throw new InvalidOrderIdException("Invalid id");
    	
    	if(RFIDfrom == null || RFIDfrom.isEmpty() || !Pattern.matches("\\d{12}", RFIDfrom))
    		throw new InvalidRFIDException();
    	
    	MyOrder ord = book.getOrderById(orderId);
    	if(ord==null)
			return false;
    	
		String barCode = ord.getProductCode();
    	MyProductType pt = (MyProductType) catalogue.getProductTypeByBarCode(barCode); 
    	if(pt.getLocation().isEmpty())
    		throw new InvalidLocationException();
    	
    	if(!book.recordOrderArrival(orderId))
    		return false;
    	 
    	pt.updateQuantity(ord.getQuantity());
    	
    	//CHANGES
    	Long rfid = Long.parseLong(RFIDfrom); 
    	for(int i=0; i<ord.getQuantity(); i++) {
    		if(i!=0) //da giulio
    			rfid+=1;
    		if(!catalogue.addToRfidsProducts(rfid.toString(), pt)) //da giulio
    			return false;
    		pt.addRFID(rfid.toString());
			addDbRfids(((Integer)(-1)), barCode, rfid.toString());
    	}	
    	updateDbOrders();
    	updateDbProducts();
    	
    	return true; 
    }
    
    @Override
    public List<Order> getAllOrders() throws UnauthorizedException {
    	if(loggedUser == null)
    		throw new UnauthorizedException();
    	String role = loggedUser.getRole();
    	if(!role.equals("Administrator") &&  !role.equals("ShopManager"))
    		throw new UnauthorizedException();
    	
        return book.getAllOrders();
    }
    
   
    @Override
    public Integer defineCustomer(String customerName) throws InvalidCustomerNameException, UnauthorizedException {
    	if(loggedUser == null)
    		throw new UnauthorizedException();
    	/*String role = loggedUser.getRole();
    	if(!role.equals("Administrator") &&  !role.equals("ShopManager") && !role.equals("Cashier")) non si verifica mai
    		throw new UnauthorizedException();
    	*/
    	
    	if(customerName == null || customerName == "") 
    		throw new InvalidCustomerNameException();
    	
		Integer toRet = clist.addCustomer(customerName);
    	if(toRet>=0)
    		updateDbCustomers();
		return toRet;
    }

    @Override
    public boolean modifyCustomer(Integer id, String newCustomerName, String newCustomerCard) throws InvalidCustomerNameException, InvalidCustomerCardException, InvalidCustomerIdException, UnauthorizedException {
    	if(loggedUser == null)
    		throw new UnauthorizedException();
    	/*
    	String role = loggedUser.getRole();
    	if(!role.equals("Administrator") &&  !role.equals("ShopManager") && !role.equals("Cashier")) non si verifica mai --giulio
		throw new UnauthorizedException();
		*/
    	
    	if(newCustomerName == null || newCustomerName == "") 
    		throw new InvalidCustomerNameException("Invalid name");
    	
    	if(id == null || id <= 0) 
    		throw new InvalidCustomerIdException();
    	
    	if(newCustomerCard== null)
    		return clist.modifyCustomerName(id,newCustomerName);
    	if(!newCustomerCard.isEmpty() && !Pattern.matches("\\d{10}+", newCustomerCard))
    		throw new InvalidCustomerCardException();
    	
    	
    	
    	boolean toRet = clist.modifyCustomerName(id,newCustomerName);  //se carta è vuota oppure ha un nuovo numero
    	
		if(toRet) {
			clist.modifyCustomer(newCustomerName,newCustomerCard);
    		updateDbCustomers(); //da verificare
		}
		return toRet;
    }

    @Override
    public boolean deleteCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {
    	if(loggedUser == null)
    		throw new UnauthorizedException();
    	//String role = loggedUser.getRole();
    	/*
    	if(!role.equals("Administrator") &&  !role.equals("ShopManager") && !role.equals("Cashier")) non si verifica mai
    		throw new UnauthorizedException();
    	*/
    	if(id == null || id <= 0) 
    		throw new InvalidCustomerIdException();
    	
    	boolean toRet = clist.deleteCustomer(id);
    	
		if(toRet)
    		updateDbCustomers();
		return toRet;
    }

    @Override
    public Customer getCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {
    	if(loggedUser == null)
    		throw new UnauthorizedException();
    	//String role = loggedUser.getRole();
    	/*
    	if(!role.equals("Administrator") &&  !role.equals("ShopManager") && !role.equals("Cashier")) non si verifica mai --giulio
    		throw new UnauthorizedException();
    	*/
    	if(id == null || id <= 0) 
    		throw new InvalidCustomerIdException();
    	
    	return clist.getCustomer(id);
    }

    @Override
    public List<Customer> getAllCustomers() throws UnauthorizedException {
    	if(loggedUser == null)
    		throw new UnauthorizedException();
    	//String role = loggedUser.getRole();
    	/*
    	if(!role.equals("Administrator") &&  !role.equals("ShopManager") && !role.equals("Cashier")) non si verifica mai --giulio
    		throw new UnauthorizedException();
    	*/
    	
    	return clist.getAllCustomer();
    }

    @Override
    public String createCard() throws UnauthorizedException {
    	if(loggedUser == null)
    		throw new UnauthorizedException();
    	/*
    	String role = loggedUser.getRole();
    	if(!role.equals("Administrator") &&  !role.equals("ShopManager") && !role.equals("Cashier")) non si verifica mai --giulio
    		throw new UnauthorizedException();
    	*/
    	CardCounter.add();
		String newCard= CardCounter.getCounter();
    	/*if(newCard == null || newCard == "") //non si verifica mai
    		return null;
    	else {*/
			updateDbCustomers();
			return newCard;
		
    }

    @Override
    public boolean attachCardToCustomer(String customerCard, Integer customerId) throws InvalidCustomerIdException, InvalidCustomerCardException, UnauthorizedException {
    	if(loggedUser == null)
    		throw new UnauthorizedException();
    	/*
    	String role = loggedUser.getRole();
    	if(!role.equals("Administrator") &&  !role.equals("ShopManager") && !role.equals("Cashier"))
    		throw new UnauthorizedException();
    	*/
    	if(customerId == null || customerId <= 0) 
    		throw new InvalidCustomerIdException("Invalid id");
    	
    	if(customerCard == null || customerCard.isEmpty() || customerCard.length()!=10 || !Pattern.matches("\\d{10}+", customerCard))
    		throw new InvalidCustomerCardException("Invalid card");
    	
		boolean toRet;
		String name = clist.getCustomerName(customerId);
    	toRet = clist.modifyCustomer(name,customerCard);
		if(toRet)
    		updateDbCustomers();
		return toRet;
	}

    @Override
    public boolean modifyPointsOnCard(String customerCard, int pointsToBeAdded) throws InvalidCustomerCardException, UnauthorizedException {
    	if(loggedUser == null)
    		throw new UnauthorizedException();
    	/*
    	String role = loggedUser.getRole();
    	if(!role.equals("Administrator") &&  !role.equals("ShopManager") && !role.equals("Cashier"))
    		throw new UnauthorizedException();
    	*/
    	if(customerCard == null || customerCard.isEmpty() || customerCard.length()!=10 || !Pattern.matches("\\d{10}+", customerCard))
    		throw new InvalidCustomerCardException();
    	
		boolean toRet;
    	toRet = clist.updatePoints(customerCard,pointsToBeAdded);
		if(toRet)
    		updateDbCustomers();
		return toRet;
    }

    @Override
    public Integer startSaleTransaction() throws UnauthorizedException {
    	if(loggedUser == null)
    		throw new UnauthorizedException();
    	/*
    	String role = loggedUser.getRole();
    	if(!role.equals("Administrator") &&  !role.equals("ShopManager") && !role.equals("Cashier"))
    		throw new UnauthorizedException();
    	*/
    	Integer toRet = book.issueSaleTransaction();
    	return toRet;
    }

    @Override
    public boolean addProductToSale(Integer transactionId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
    	if(loggedUser == null)
    		throw new UnauthorizedException();
    	/*
    	String role = loggedUser.getRole();
    	if(!role.equals("Administrator") &&  !role.equals("ShopManager") && !role.equals("Cashier"))
    		throw new UnauthorizedException();
    	*/
    	if(amount <= 0)
    		throw new  InvalidQuantityException();
    	
    	if(productCode == null || productCode.isEmpty() || !Pattern.matches("\\d{12,14}", productCode) || !catalogue.checkBarCode(productCode)) //regexp per verificare che sia un numero (12-14 cifre) e controllo valid barCode!
    		throw new InvalidProductCodeException();
    	
    	if(transactionId == null || transactionId <= 0)
    		throw new InvalidTransactionIdException();
    	
    	//modifiche Pietro
    	MyProductType mpt = (MyProductType) catalogue.getProductTypeByBarCode(productCode);
    	if(mpt == null) //se il productCode non esiste
    		return false;
    	if(mpt.getQuantity() < amount) //se il prodotto non ha una quantità sufficiente disponibile
    		return false;
    	 
    	if(book.addOrRemoveProductToSale(transactionId, productCode, amount, 0, mpt.getPricePerUnit(), mpt.getProductDescription())) {
    		mpt.updateQuantity(-amount); //se il prodotto viene aggiunto si diminuisce la quantità disponibile
			return true;
    	}
    	
    	return false;
    }

    @Override
    public boolean addProductToSaleRFID(Integer transactionId, String RFID) throws InvalidTransactionIdException, InvalidRFIDException, InvalidQuantityException, UnauthorizedException{
    	if(loggedUser == null) 
    		throw new UnauthorizedException();
    	 
    	if(transactionId == null || transactionId <= 0)
    		throw new InvalidTransactionIdException();
    	
    	if(RFID == null || RFID.isEmpty() || !Pattern.matches("\\d{12}", RFID))
    		throw new InvalidRFIDException();
    	
    	
    	MyProductType mpt = catalogue.getProductTypeByRFID(RFID);
    	if(mpt == null) //se il RFID non esiste
    		return false;
    	
    	if(mpt.rfidIsSold(RFID))
    		return false;
    	 
    	if(book.addOrRemoveProductToSale(transactionId, mpt.getBarCode(), 1, 0, mpt.getPricePerUnit(), mpt.getProductDescription())) {
    		mpt.updateQuantity(-1); //se il prodotto viene aggiunto si diminuisce la quantità disponibile
    		mpt.addToSoldRfids(RFID, transactionId);
    		return true;
    	}
    	
    	//rimuovere anche gli RFIDs da MyProductType e da Catalogue !? 
    	else
    		return false;
    }
    
    @Override
    public boolean deleteProductFromSale(Integer transactionId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
    	if(loggedUser == null)
    		throw new UnauthorizedException();
    	/*
    	String role = loggedUser.getRole();
    	if(!role.equals("Administrator") &&  !role.equals("ShopManager") && !role.equals("Cashier"))
    		throw new UnauthorizedException();
    	*/
    	if(amount <= 0)
    		throw new  InvalidQuantityException();
    	
    	if(productCode == null || productCode.isEmpty() || !Pattern.matches("\\d{12,14}", productCode) || !catalogue.checkBarCode(productCode)) //regexp per verificare che sia un numero (12-14 cifre) e controllo valid barCode!
    		throw new InvalidProductCodeException();
    	
    	if(transactionId == null || transactionId <= 0)
    		throw new InvalidTransactionIdException();
    	
    	//modifiche Pietro
    	MyProductType mpt = (MyProductType) catalogue.getProductTypeByBarCode(productCode);
    	if(mpt == null) //se il productCode non esiste
    		return false;
    	
    	if(book.addOrRemoveProductToSale(transactionId, productCode, amount, 1, (double) 0, null)) {
    		mpt.updateQuantity(amount); //se il prodotto viene rimosso si aumenta la quantità disponibile
    		return true;
    	}
    	
    	return false;
    }

    @Override
    public boolean deleteProductFromSaleRFID(Integer transactionId, String RFID) throws InvalidTransactionIdException, InvalidRFIDException, InvalidQuantityException, UnauthorizedException{
    	if(loggedUser == null)
    		throw new UnauthorizedException();
    	
    	if(transactionId == null || transactionId <= 0)
    		throw new InvalidTransactionIdException();
    	
    	if(RFID == null || RFID.isEmpty() || !Pattern.matches("\\d{12}", RFID))
    		throw new InvalidRFIDException();
    	
    	//modifiche Pietro
    	MyProductType mpt = (MyProductType) catalogue.getProductTypeByRFID(RFID);
    	if(mpt == null) //se il productCode non esiste
    		return false;
    	
    	if(!mpt.rfidIsSold(RFID))
    		return false;
    	
    	if(book.addOrRemoveProductToSale(transactionId, mpt.getBarCode(), 1, 1, (double) 0, null)) {
    		mpt.updateQuantity(1); //se il prodotto viene rimosso si aumenta la quantità disponibile
    		mpt.removeRFIDFromSold(RFID);
    		return true;
    	}
    	
    	//aggiungere anche gli RFIDs a MyProductType e a Catalogue !? (Solo se li rimuovo nella add)
    	else
    		return false;
    }

    @Override
    public boolean applyDiscountRateToProduct(Integer transactionId, String productCode, double discountRate) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidDiscountRateException, UnauthorizedException {
    	if(loggedUser == null)
    		throw new UnauthorizedException();
    	/*
    	String role = loggedUser.getRole();
    	if(!role.equals("Administrator") &&  !role.equals("ShopManager") && !role.equals("Cashier"))
    		throw new UnauthorizedException();
    	*/
    	if(transactionId == null || transactionId <= 0)
    		throw new InvalidTransactionIdException();
    	
    	if(discountRate >= 1.0 || discountRate <= 0)
    		throw new InvalidDiscountRateException();
    	
    	if(productCode == null || productCode.isEmpty() || !Pattern.matches("\\d{12,14}", productCode) || !catalogue.checkBarCode(productCode)) //regexp per verificare che sia un numero (12-14 cifre) e controllo valid barCode!
    		throw new InvalidProductCodeException();
    	
    	MyProductType mpt = (MyProductType) catalogue.getProductTypeByBarCode(productCode);
    	if(mpt == null) //se il productCode non esiste
    		return false;
    	
    	//MySaleTransaction sale = (MySaleTransaction) book.searchBalanceOperation(transactionId); //VECCHIA VERSIONE
    	MySaleTransaction sale = book.getSaleTransactionById(transactionId); //NUOVA VERSIONE
    	if (sale == null || !sale.getStatus().equals("OPENED"))
    		return false;
    	
    	return sale.addDiscountToProduct(productCode, discountRate);
    }

    @Override
    public boolean applyDiscountRateToSale(Integer transactionId, double discountRate) throws InvalidTransactionIdException, InvalidDiscountRateException, UnauthorizedException {
    	if(loggedUser == null)
    		throw new UnauthorizedException();
    	/*
    	String role = loggedUser.getRole();
    	if(!role.equals("Administrator") &&  !role.equals("ShopManager") && !role.equals("Cashier"))
    		throw new UnauthorizedException();
    	*/
    	if(transactionId == null || transactionId <= 0)
    		throw new InvalidTransactionIdException();
    	
    	if(discountRate >= 1.0 || discountRate <= 0)
    		throw new InvalidDiscountRateException("Invalid discount");
    	
    	//MySaleTransaction sale = (MySaleTransaction) book.searchBalanceOperation(transactionId); //VECCHIA VERSIONE
    	MySaleTransaction sale = book.getSaleTransactionById(transactionId); //NUOVA VERSIONE
    	if (sale == null || !sale.getStatus().equals("OPENED"))
    		return false;
    	/*
    	if(sale.getStatus().compareTo("OPENED")!=0 && sale.getStatus().compareTo("CLOSED")!=0) //da giulio
    		return false;
    	*/
    	sale.setDiscountRate(discountRate);
    	return true;
    }
    	

    @Override
    public int computePointsForSale(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
    	if(loggedUser == null)
    		throw new UnauthorizedException();
    	/*
    	String role = loggedUser.getRole();
    	if(!role.equals("Administrator") &&  !role.equals("ShopManager") && !role.equals("Cashier"))
    		throw new UnauthorizedException();
    	*/
    	if(transactionId == null || transactionId <= 0)
    		throw new InvalidTransactionIdException();
    	
    	//MySaleTransaction sale = (MySaleTransaction) book.searchBalanceOperation(transactionId); //VECCHIA VERSIONE
    	MySaleTransaction sale = book.getSaleTransactionById(transactionId); //NUOVA VERSIONE
    	if (sale == null) //se la transaction non esiste
    		return -1;
    	
    	return sale.getPointsEarned();
    }

    @Override
    public boolean endSaleTransaction(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException { //da giulio
    	if(loggedUser == null)
    		throw new UnauthorizedException();
    	/*
    	String role = loggedUser.getRole();
    	if(!role.equals("Administrator") &&  !role.equals("ShopManager") && !role.equals("Cashier"))
    		throw new UnauthorizedException();
    	*/
    	if(transactionId == null || transactionId <= 0)
    		throw new InvalidTransactionIdException();
    	
    	//MyBalanceOperation sale = (MyBalanceOperation) book.searchBalanceOperation(transactionId); //VECCHIA VERSIONE
    	MySaleTransaction sale = book.getSaleTransactionById(transactionId); //NUOVA VERSIONE
    	if (sale == null)
    		return false;
    	if(sale.getStatus().compareTo("CLOSED")==0 || sale.getStatus().compareTo("PAYED")==0)
    		return false;
    	
    	sale.setStatus("CLOSED");
    	 
    	System.out.println(sale.getTicketNumber());
    	
    	return true; //se true o false
    }

    @Override
    public boolean deleteSaleTransaction(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
    	if(loggedUser == null)
    		throw new UnauthorizedException();
    	/*
    	String role = loggedUser.getRole();
    	if(!role.equals("Administrator") &&  !role.equals("ShopManager") && !role.equals("Cashier"))
    		throw new UnauthorizedException();
    	*/
    	if(transactionId == null || transactionId <= 0)
    		throw new InvalidTransactionIdException();
    	
    	//MySaleTransaction sale = (MySaleTransaction) book.searchBalanceOperation(transactionId); // VECCHIA VERSIONE
    	MySaleTransaction sale = book.getSaleTransactionById(transactionId); //NUOVA VERSIONE
    	if (sale == null)
    		return false;
    	if(sale.getStatus().compareTo("PAYED")==0) //da giulio
    		return false;
    	
    	//DB !!!!!!!!!!!!!!!!!!!!!!!!!!
    	
    	sale.removeAllProductsFromSale(catalogue);
    	
    	
    	//return book.removeBalanceOperation(transactionId); VECCHIA VERSIONE
    	//return book.removeSaleTransaction(transactionId, catalogue); //NUOVA VERSIONE
    	
    	//CHANGES
    	if(book.removeSaleTransaction(transactionId, catalogue)) {
    		sale.getEntries().stream().forEach(e -> {
    			MyProductType mpt = (MyProductType) catalogue.getProductTypeByBarCode(e.getBarCode());
    			mpt.removeAllSoldRfidsOfSale(transactionId);
    		});
    		return true;
    	}
    	return false;
    }

    @Override
    public SaleTransaction getSaleTransaction(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
    	if(loggedUser == null)
    		throw new UnauthorizedException();
    	/*
    	String role = loggedUser.getRole();
    	if(!role.equals("Administrator") &&  !role.equals("ShopManager") && !role.equals("Cashier"))
    		throw new UnauthorizedException();
    	*/
    	if(transactionId == null || transactionId <= 0)
    		throw new InvalidTransactionIdException("Invalid id");
    	
    	/* VECCHIA VERSIONE
    	MyBalanceOperation op = (MyBalanceOperation) book.searchBalanceOperation(transactionId);
    	MySaleTransaction sale = (MySaleTransaction) op;
    	*/ 
    	MySaleTransaction sale = book.getSaleTransactionById(transactionId); //NUOVA VERSIONE
    	if(sale == null)
    		return null;
    	else if(!sale.getStatus().equals("CLOSED") && !sale.getStatus().equals("PAYED"))
    		return null;
    	else
    		return sale;
    }

    @Override
    public Integer startReturnTransaction(Integer saleNumber) throws /*InvalidTicketNumberException,*/InvalidTransactionIdException, UnauthorizedException {
    	if(loggedUser == null)
    		throw new UnauthorizedException();
    	/*
    	String role = loggedUser.getRole();
    	if(!role.equals("Administrator") &&  !role.equals("ShopManager") && !role.equals("Cashier"))
    		throw new UnauthorizedException();
    	*/
    	if(saleNumber == null || saleNumber <= 0)
    		throw new InvalidTransactionIdException();
    	
    	/* VECCHIA VERSIONE
    	MySaleTransaction sale = (MySaleTransaction) getSaleTransaction(saleNumber);
    	
    	if(sale==null)
    		return -1;
    	if(sale.getStatus().compareTo("PAYED")!=0) //da giulio
    		return -1;
    	
    	
    	Integer returnID = book.getNewId();
    	sale.startReturnTransaction(returnID, sale.getDiscountRate());
    	MyReturnTransaction returnOp = sale.getReturnTransaction(returnID);
    	if(!book.addReturnTransaction(returnID,returnOp))
    		return -1;
    	else
    		return returnID;
    	*/
    	//NUOVA VERSIONE
    	MySaleTransaction sale = book.getSaleTransactionById(saleNumber);
    	if(sale == null || !sale.getStatus().equals("PAYED")) 
    		return -1;
    	Integer returnID = book.issueReturnTransaction(sale);
    	
    	//sale.getEntries().stream().forEach(e -> System.out.println(e.getBarCode()));
    	
    	return returnID;
    }

    @Override
    public boolean returnProduct(Integer returnId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
    	if(loggedUser == null)
    		throw new UnauthorizedException();
    	/*
    	String role = loggedUser.getRole();
    	if(!role.equals("Administrator") &&  !role.equals("ShopManager") && !role.equals("Cashier"))
    		throw new UnauthorizedException();
    	*/
    	if(returnId == null || returnId <= 0)
    		throw new InvalidTransactionIdException();
    	
    	if(productCode == null || productCode.isEmpty() || !Pattern.matches("\\d{12,14}", productCode) || !catalogue.checkBarCode(productCode)) //regexp per verificare che sia un numero (12-14 cifre) e controllo valid barCode!
    		throw new InvalidProductCodeException();
    	
    	if(amount <= 0)
    		throw new  InvalidQuantityException();
    	
    	/* VECCHIA VERSIONE
    	BalanceOperation ret = (BalanceOperation) book.searchBalanceOperation(returnId);
    	if (ret == null)
    		return false;
    	
    	MyProductType mpt = (MyProductType) this.getProductTypeByBarCode(productCode);
    	if(mpt==null)
    		return false;
    	
    	if(((MyReturnTransaction) ret).returnProduct(productCode,amount)) {
    		mpt.updateQuantity(amount); //se il prodotto viene restituito si aumenta la quantità disponibile
    		return true;
    	} else
    	   	return false;
    	 */
    	MyReturnTransaction ret = book.getReturnTransactionById(returnId);
    	if(ret == null)
    		return false;
    	MyProductType mpt = (MyProductType) this.getProductTypeByBarCode(productCode);
    	if(mpt==null)
    		return false;
    	
    	return ret.returnProduct(productCode, amount);
    }

	@Override
    public boolean returnProductRFID(Integer returnId, String RFID) throws InvalidTransactionIdException, InvalidRFIDException, UnauthorizedException {
		if(loggedUser == null)
    		throw new UnauthorizedException();
    	
    	if(returnId == null || returnId <= 0)
    		throw new InvalidTransactionIdException();
    	
    	if(RFID == null || RFID.isEmpty() || !Pattern.matches("\\d{12}", RFID))
    		throw new InvalidRFIDException();
    	
    	MyReturnTransaction ret = book.getReturnTransactionById(returnId);
    	if(ret == null)
    		return false;
    	MyProductType mpt = (MyProductType) catalogue.getProductTypeByRFID(RFID);
    	if(mpt==null)
    		return false; 
    	
    	/* Questo controllo secondo me non ci va perchè l'rfid fornito sarà nuovo*/
    	if(!mpt.rfidIsSold(RFID))
    		return false;
    	
    	/*remove rfid from sold*/
    	mpt.removeRFIDFromSold(RFID);
    	/*catalogue.addToRfidsProducts(RFID, mpt);*/
    	/*mpt.addRFID(RFID);*/
    	removeDbRfids(RFID);
    	return ret.returnProduct(mpt.getBarCode(), 1);
    }
	
    @Override //da giulio
    public boolean endReturnTransaction(Integer returnId, boolean commit) throws InvalidTransactionIdException, UnauthorizedException {
    	if(loggedUser == null)
    		throw new UnauthorizedException();
    	/*
    	String role = loggedUser.getRole();
    	if(!role.equals("Administrator") &&  !role.equals("ShopManager") && !role.equals("Cashier"))
    		throw new UnauthorizedException();
    	*/
    	if(returnId == null || returnId <= 0)
    		throw new InvalidTransactionIdException();
    	
    	/* VECCHIA VERSIONE
    	MyReturnTransaction ret = (MyReturnTransaction) book.searchBalanceOperation(returnId);
    	if (ret == null)
    		return false;
    	else if(ret.getStatus().compareTo("OPENED")!=0)
    		return false;
    	ret.setStatus("CLOSED");
    	
    	if(commit) {
    		return ret.commit(catalogue);
    	}
    	else
    		return ret.rollback();
    	 */
    	
    	MyReturnTransaction ret = book.getReturnTransactionById(returnId);
    	if (ret == null || !ret.getStatus().equals("OPENED"))
    		return false;
    	ret.setStatus("CLOSED");
    	if(commit){
    		boolean toRet = ret.commit(catalogue);
    		updateDbOperations();
			MySaleTransaction sale = ret.getSaleTransaction();
			sale.getEntries().stream().forEach((entry)->updateDbEntry((MyTicketEntry)entry, sale.getTicketNumber()));
    		return toRet; 
    	}
    	else
    		return book.removeReturnTransaction(returnId);
    }

    @Override
    public boolean deleteReturnTransaction(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException {
    	if(loggedUser == null)
    		throw new UnauthorizedException();
    	/*
    	String role = loggedUser.getRole();
    	if(!role.equals("Administrator") &&  !role.equals("ShopManager") && !role.equals("Cashier"))
    		throw new UnauthorizedException();
    	*/
    	if(returnId == null || returnId <= 0)
    		throw new InvalidTransactionIdException();
    	/*VECCHIA VERSIONE
    	MyReturnTransaction ret = (MyReturnTransaction) book.searchBalanceOperation(returnId);
    	if (ret == null)
    		return false;
    	else if(ret.getStatus().compareTo("PAYED")==0)
    		return false;
    	//else if(db problem) TODO !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    	return book.removeReturnTransaction(returnId);
    	*/
    	
    	MyReturnTransaction ret = book.getReturnTransactionById(returnId);
    	if (ret == null || !ret.getStatus().equals("CLOSED"))
    		return false;
    	return book.removeReturnTransaction(returnId);
    }

    @Override
    public double receiveCashPayment(Integer ticketNumber, double cash) throws InvalidTransactionIdException, InvalidPaymentException, UnauthorizedException {
    	if(loggedUser == null)
    		throw new UnauthorizedException();
    	/*
    	String role = loggedUser.getRole();
    	if(!role.equals("Administrator") &&  !role.equals("ShopManager") && !role.equals("Cashier"))
    		throw new UnauthorizedException();
    	*/
    	if(ticketNumber == null || ticketNumber <= 0)
    		throw new InvalidTransactionIdException();
    	if(cash<=0)
    		throw new InvalidPaymentException("Invalid payment");
    	MySaleTransaction sale = (MySaleTransaction) getSaleTransaction(ticketNumber);
    	if(sale==null)
    		return -1; 
    	if(sale.getStatus().compareTo("CLOSED")!=0)
    		return -1;
    	
    	double totPrice = sale.computeTotalPrice();
    	if(cash < totPrice)
    		return -1;
    	
    	/* VECCHIA VERSIONE
    	double change = sale.startPayment(totPrice, cash);
    	if(change == -1)
    		return -1;
    	else {
    		sale.setStatus("PAYED");
    		book.updateBalance(totPrice);
    		return change;
    	}
    	*/
    	
    	//NUOVA VERSIONE
    	MyBalanceOperation bo = book.addNewBalanceOperationSale(sale);
    	double change = bo.startPayment(totPrice, cash);
    	/*
    	if(change == -1)  //non è coerente non si verifica mai --giulio
    		return -1;
    	else {
    	*/
    		sale.setStatus("PAYED");
    		bo.setStatus("PAYED");
    		book.recordBalanceUpdate(totPrice,"Sale");
			updateDbOperations();
			updateDbSales(sale);
			updateDbEntries(sale);
			//CHANGES
			sale.getEntries().stream().forEach((t)->{
				String barcode = t.getBarCode();
				ProductType mpt = null;
				try {
					mpt = getProductTypeByBarCode(barcode);
				} catch (InvalidProductCodeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnauthorizedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ArrayList<String> list = ((MyProductType) mpt).getRfidPerSale(ticketNumber);
				list.stream().forEach((r) -> addDbRfids(ticketNumber, barcode, r));
			});
    		return change;
    	
    }
    //PIETRO ACCEPTANCE TESTS
    public boolean checkCreditCardLunh(String creditCardNumber) {
		if(creditCardNumber==null)
			return false;
		int nDigits = creditCardNumber.length();
	    int nSum = 0;
	    boolean isSecond = false;
	    for (int i = nDigits - 1; i >= 0; i--)
	    {
	 
	        int d = creditCardNumber.charAt(i) - '0';
	 
	        if (isSecond == true)
	            d = d * 2;
	 
	        // We add two digits to handle
	        // cases that make two digits
	        // after doubling
	        nSum += d / 10;
	        nSum += d % 10;
	 
	        isSecond = !isSecond;
	    }
	    if(creditCardNumber.length()==0)
	    	return false;
	    return (nSum % 10 == 0);
	}
    
    @Override
    public boolean receiveCreditCardPayment(Integer ticketNumber, String creditCard) throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {
    	if(loggedUser == null)
    		throw new UnauthorizedException();
    	/*
    	String role = loggedUser.getRole();
    	if(!role.equals("Administrator") &&  !role.equals("ShopManager") && !role.equals("Cashier"))
    		throw new UnauthorizedException();
    	*/
    	if(ticketNumber == null || ticketNumber <= 0)
    		throw new InvalidTransactionIdException();
    	if(creditCard==null || creditCard.isEmpty())
    		throw new InvalidCreditCardException();
    	if(!checkCreditCardLunh(creditCard)) //PIETRO AT
    		throw new InvalidCreditCardException();
    	
    	CreditCard cc = registeredCards.get(creditCard);
    	if(cc==null)
    		return false;
    	
    	MySaleTransaction sale = (MySaleTransaction) getSaleTransaction(ticketNumber);
    	if(sale==null)
    		return false;
    	if(sale.getStatus().compareTo("CLOSED")!=0)
    		return false;
    	
    	double totPrice = sale.computeTotalPrice();
    	
    	if(!cc.checkBalance(totPrice))
    		return false;
    	
    	/* VECCHIA VERSIONE
    	if(!sale.startPayment(cc, totPrice))
    		return false;
    	
    	sale.setStatus("PAYED");
    	cc.setBalance(cc.getBalance()-totPrice);
    	book.updateBalance(totPrice);
    	return true;
    	*/
    	MyBalanceOperation bo = book.addNewBalanceOperationSale(sale);
    	bo.startPayment(cc, totPrice); 
    	
    	sale.setStatus("PAYED");
    	bo.setStatus("PAYED");
    	book.recordBalanceUpdate(totPrice,"Sale");
		updateDbOperations();
		updateDbSales(sale);
    	updateDbEntries(sale);
		//CHANGES
		sale.getEntries().stream().forEach((t)->{
			String barcode = t.getBarCode();
			ProductType mpt = null;
			try {
				mpt = getProductTypeByBarCode(barcode);
			} catch (InvalidProductCodeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnauthorizedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ArrayList<String> list = ((MyProductType) mpt).getRfidPerSale(ticketNumber);
			list.stream().forEach((r) -> addDbRfids(ticketNumber, barcode, r));
		});
    	return true;
    }

    @Override
    public double returnCashPayment(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException {
    	if(loggedUser == null)
    		throw new UnauthorizedException();
    	/*
    	String role = loggedUser.getRole();
    	if(!role.equals("Administrator") &&  !role.equals("ShopManager") && !role.equals("Cashier"))
    		throw new UnauthorizedException();
    	*/
    	if(returnId == null || returnId <= 0)
    		throw new InvalidTransactionIdException();
    	
    	/* VECCHIA VERSIONE
    	MyReturnTransaction ret = (MyReturnTransaction) book.searchBalanceOperation(returnId);
    	if (ret == null)
    		return -1;
    	if(ret.getStatus().compareTo("CLOSED")!=0)
    		return -1;
    	
    	double tot = ret.computeTotalPrice();
    	ret.setStatus("PAYED");
    	book.updateBalance(-tot);
    	ret.setMoney(tot);
    	return tot;
    	*/
    	
    	//NUOVA VERSIONE
    	MyReturnTransaction ret = book.getReturnTransactionById(returnId);
    	if (ret == null || !ret.getStatus().equals("CLOSED"))
    		return -1;
    	
    	double tot = ret.computeTotalPrice();
    	MyBalanceOperation bo = book.addNewBalanceOperationReturn(ret);
    	bo.setMoney(tot);
    	book.recordBalanceUpdate(-tot,"Return");
    	ret.setStatus("PAYED");
    	bo.setStatus("PAYED");
    	updateDbOperations();
    	return tot;
    }

    @Override
    public double returnCreditCardPayment(Integer returnId, String creditCard) throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {
    	if(loggedUser == null)
    		throw new UnauthorizedException();
    	/*
    	String role = loggedUser.getRole();
    	if(!role.equals("Administrator") &&  !role.equals("ShopManager") && !role.equals("Cashier"))
    		throw new UnauthorizedException();
    	*/
    	if(returnId == null || returnId <= 0)
    		throw new InvalidTransactionIdException();
    	if(creditCard==null || creditCard.isEmpty())
    		throw new InvalidCreditCardException("Invalid card");
    	if(!checkCreditCardLunh(creditCard))
    		throw new InvalidCreditCardException();
    	CreditCard cc = registeredCards.get(creditCard);
    	if(cc==null)
    		return -1;
    	
    	
    	/* VECCHIA VERSIONE
    	 * 
    	MyReturnTransaction ret = (MyReturnTransaction) book.searchBalanceOperation(returnId);
    	if (ret == null)
    		return -1;
    	if(ret.getStatus().compareTo("CLOSED")!=0)
    		return -1;
    	
    	double tot=ret.computeTotalPrice();
    	CreditCard cc=registeredCards.get(creditCard);
    	if(cc==null)
    		return -1;
    	if(!cc.checkCreditCardLunh())
    		throw new InvalidCreditCardException();
    	if(!ret.startPayment(cc, tot))
    		return -1;
    	
    	ret.setStatus("PAYED");
    	cc.setBalance(cc.getBalance()+tot);
    	book.updateBalance(-tot);
    	ret.setMoney(tot);
    	return tot;
    	*/
    	
    	//NUOVA VERSIONE
    	MyReturnTransaction ret = book.getReturnTransactionById(returnId);
    	if (ret == null || !ret.getStatus().equals("CLOSED"))
    		return -1;
    	
    	double tot = ret.computeTotalPrice();
    	
    	MyBalanceOperation bo = book.addNewBalanceOperationReturn(ret);
    	bo.setMoney(tot);
    	bo.startPayment(cc, tot);
    	book.recordBalanceUpdate(-tot,"Return");
    	ret.setStatus("PAYED");
    	bo.setStatus("PAYED");
    	updateDbOperations();
    	return tot;
    }

    @Override
    public boolean recordBalanceUpdate(double toBeAdded) throws UnauthorizedException {
    	if(loggedUser == null)
    		throw new UnauthorizedException();
    	String role = loggedUser.getRole();
    	if(!role.equals("Administrator") &&  !role.equals("ShopManager"))
    		throw new UnauthorizedException();
    	
    	String type="";
    	if(toBeAdded>=0)
    		type="Credit";
    	else 
    		type="Debit";
    	if(!book.recordBalanceUpdate(toBeAdded,type))
    		return false;
    	
    	updateDbOperations();
    	
    	return true;
    }

    @Override
    public List<BalanceOperation> getCreditsAndDebits(LocalDate from, LocalDate to) throws UnauthorizedException {
    	if(loggedUser == null)
    		throw new UnauthorizedException();
    	String role = loggedUser.getRole();
    	if(!role.equals("Administrator") &&  !role.equals("ShopManager"))
    		throw new UnauthorizedException();
    	
    	//se le date sono invertite
    	if(from !=null && to !=null) {
    		
    	if(from.isAfter(to)) {
    		LocalDate tmp = to;
    		to = from;
    		from = tmp;
    	}
    	}
    	
    	return book.getBalanceOperationByDate(from, to);
    }

    @Override
    public double computeBalance() throws UnauthorizedException {
    	if(loggedUser == null)
    		throw new UnauthorizedException();
    	/*
    	String role = loggedUser.getRole();
    	if(!role.equals("Administrator") &&  !role.equals("ShopManager"))
    		throw new UnauthorizedException();
    	*/
    	return book.computeBalance();
    }
}
