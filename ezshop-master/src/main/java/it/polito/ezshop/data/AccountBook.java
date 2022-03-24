package it.polito.ezshop.data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
/* VECCHIA VERSIONE
public class AccountBook {
	private Double balance;
	private HashMap<Integer, BalanceOperation> operations;
	private HashMap<Integer, MyOrder> orders; //aggiunta --> MyOrder extends BalanceOperation non funziona: errore sul primo getter
	
	public AccountBook(){
		this.operations = new HashMap<Integer, BalanceOperation>();
		this.orders = new HashMap<Integer, MyOrder>();
		this.balance = 0.0;
	}
	
	public Integer issueOrder(String productCode, Integer quantity, Double pricePerUnit) {
		OperationsCounter.add();
    	Integer orderID = OperationsCounter.getCounter();
    	MyOrder ord = new MyOrder(orderID, productCode, quantity, pricePerUnit);
    	orders.put(orderID, ord);
    	ord.setStatus("ISSUED");
    	
    	return orderID;
	}
	
	public Integer payOrderFor(String productCode, Integer quantity, Double pricePerUnit) {
		OperationsCounter.add();
    	Integer orderID = OperationsCounter.getCounter();
    	MyOrder ord = new MyOrder(orderID, productCode, quantity, pricePerUnit);
    	ord.setStatus("ISSUED"); //se il balance non sarà sufficiente non verrà salvato però
    	
    	Double toBePayed = ord.getPricePerUnit()*ord.getQuantity();
    	
    	if(!this.updateBalance(-toBePayed)) //sottraggo dal balance la somma richiesta per l'ordine
    		return -1; //se il balance non è sufficiente
    	
    	orders.put(orderID, ord);
    	
    	MyBalanceOperation bo = new MyBalanceOperation(orderID, "Order");
    	bo.setMoney(toBePayed);
    	operations.put(orderID, bo);
    	
    	ord.setStatus("PAYED");
    	
    	return orderID;
	}
	
	public boolean payOrder(Integer orderId) {
		MyOrder ord = orders.get(orderId);
		if(ord == null)
			return false;
		String status = ord.getStatus();
		if(status == null || (!status.equals("ISSUED") && !status.equals("ORDERED")) ) //confusione nelle specifiche di issued e ordered
			return false;
		
		if(status.equals("ISSUED") || status.equals("ORDERED")) {
	    	Double toBePayed = ord.getPricePerUnit()*ord.getQuantity();
	    	
	    	if(!this.updateBalance(-toBePayed)) //sottraggo dal balance la somma richiesta per l'ordine
	    		return false; //non è specificato come caso ma secondo me è importante
	    	
	    	MyBalanceOperation bo = new MyBalanceOperation(orderId, "Order");
	    	bo.setMoney(toBePayed);
	    	operations.put(orderId, bo);
		}
		
		ord.setStatus("PAYED");
		return true;
	}
	
	public boolean recordOrderArrival(Integer id) {
		MyOrder ord = orders.get(id);
		if(ord == null)
			return false;
		String status = ord.getStatus();
		if(status == null || (!status.equals("PAYED") && !status.equals("COMPLETED")) ) //confusione nelle specifiche di completed e payed
			return false;
		
		ord.setStatus("COMPLETED");
		return true;
	}
	
	public List<Order> getAllOrders() {
		return new ArrayList<Order>(orders.values());
	}
	
	
	public BalanceOperation searchBalanceOperation(Integer id) {
		return operations.get(id);
	}
	
	public List<BalanceOperation> getBalanceOperationByDate(LocalDate from, LocalDate to) {
		if(from == null && to == null) {
			return operations.values().stream().filter(o -> {
				String type = o.getType();
				if(type.equals("Sale") || type.equals("Return")) {
					MyBalanceOperation mo = (MyBalanceOperation) o;
					if(!mo.getStatus().equals("PAYED"))
						return false;
				}
				return true;
			}).collect(Collectors.toList());
		}
		return operations.values().stream().filter(o->{
			String type = o.getType();
			if(type.equals("Sale") || type.equals("Return")) {
				MyBalanceOperation mo = (MyBalanceOperation) o;
				if(!mo.getStatus().equals("PAYED"))
					return false;
			}
			
			LocalDate date = o.getDate();
			if(from != null && to != null) {
				if((date.isAfter(from) || date.isEqual(from)) && (date.isBefore(to) || date.isEqual(to)) )
					return true;
			}
			if(from != null && to == null) {
				if(date.isAfter(from) || date.isEqual(from))
					return true;
			}
			if(from == null && to != null) {
				if(date.isBefore(to) || date.isEqual(to))
					return true;
			}
			return false;
		}).collect(Collectors.toList());
	}
	
	public Integer issueSaleTransaction() {
		OperationsCounter.add();
    	Integer saleID = OperationsCounter.getCounter();
    	MySaleTransaction sale = new MySaleTransaction(saleID);
    	operations.put(saleID, sale);
    	return saleID;
	}
	
	public Integer getNewId() {
		OperationsCounter.add();
    	return OperationsCounter.getCounter();
	}
	
	public boolean addReturnTransaction(Integer returnId, BalanceOperation bo) {
		operations.put(returnId, bo);
		return true;
	}
	
	public boolean updateBalance(Double value) {
		if(value < 0 && Math.abs(value) > this.balance)
			return false;

		this.balance = this.balance + value;
		
		return true;
	}
	
	public boolean recordBalanceUpdate(Double value) {
		if(value < 0 && Math.abs(value) > this.balance)
			return false;
		
		//solo per le operazioni Credit a Debit
		OperationsCounter.add();
		Integer opID = OperationsCounter.getCounter();
		MyBalanceOperation bo;
		if(value > 0) {
			bo = new MyBalanceOperation(opID, "Credit");
		}else {
			bo = new MyBalanceOperation(opID, "Debit");
		}
		bo.setMoney(Math.abs(value));
		operations.put(opID, bo);
		
		this.balance = this.balance + value;
		
		return true;
	}
	
	public Double computeBalance(){
		return this.balance;
	}
	
	public boolean removeReturnTransaction(Integer returnId){
    	return false;
	}
	
	public boolean removeBalanceOperation(Integer id) {
		if(operations.get(id) != null) {
			operations.remove(id);
			return true;
		}
		else
			return false;
	}
	
	public MyOrder getOrderById(Integer orderId) {
		return orders.get(orderId); //confusione nelle specifiche di completed e payed
	}

	//type 0 for add, 1 for remove
	public boolean addOrRemoveProductToSale(Integer transactionId, String productCode, int amount, int type, Double pricePerUnit, String description) {
		MySaleTransaction sale = (MySaleTransaction) operations.get(transactionId);
		if(sale == null)
			return false;
		if(type == 0)
			return sale.addProductToSale(productCode,amount,pricePerUnit, description);
		else
			return sale.removeProductToSale(productCode,amount);
	}
	
}
*/

public class AccountBook {
	private Double balance;
	private HashMap<Integer, BalanceOperation> operations;
	private HashMap<Integer, MyOrder> orders; //aggiunta --> MyOrder extends BalanceOperation non funziona: errore sul primo getter
	private HashMap<Integer, MySaleTransaction> sales;
	private HashMap<Integer, MyReturnTransaction> returns; 
	
	public AccountBook(){ 
		this.operations = new HashMap<Integer, BalanceOperation>();
		this.orders = new HashMap<Integer, MyOrder>();
		this.sales = new HashMap<Integer, MySaleTransaction>();
		this.returns = new HashMap<Integer, MyReturnTransaction>();
		this.balance = 0.0;
	} 
	 
	
	//ORDER
	public Integer issueOrder(String productCode, Integer quantity, Double pricePerUnit) {
		OperationsCounter.add();
    	Integer orderID = OperationsCounter.getCounter();
    	MyOrder ord = new MyOrder(orderID, productCode, quantity, pricePerUnit);
    	orders.put(orderID, ord);
    	ord.setStatus("ISSUED");
    	
    	return orderID; 
	}
	
	public boolean recoverOrder(Integer orderID, String productCode, double pricePerUnit, Integer quantity, String status){
		MyOrder ord = new MyOrder(orderID, productCode, quantity, pricePerUnit);
		ord.setStatus(status);
		orders.put(orderID, ord);
		return true;
	}

	public Integer payOrderFor(String productCode, Integer quantity, Double pricePerUnit) {
		
		Double toBePayed = pricePerUnit*quantity;
		if(!this.recordBalanceUpdate(-toBePayed,"Order")) //sottraggo dal balance la somma richiesta per l'ordine
    		return -1; //se il balance non è sufficiente
    	Integer orderID = OperationsCounter.getCounter();
    	MyOrder ord = new MyOrder(orderID, productCode, quantity, pricePerUnit); 
    	//ord.setStatus("ISSUED"); //se il balance non sarà sufficiente non verrà salvato però
    	 
    	
    	orders.put(orderID, ord);
    	
    	/*MyBalanceOperation bo = new MyBalanceOperation(orderID, "Order");
    	bo.setMoney(toBePayed);
    	operations.put(orderID, bo);*/
    	
    	ord.setStatus("PAYED");
    	
    	return orderID;
	}
	
	public boolean payOrder(Integer orderId) {
		MyOrder ord = orders.get(orderId);
		if(ord == null)
			return false;
		String status = ord.getStatus();
		if(!status.equals("ISSUED") && !status.equals("ORDERED")) //confusione nelle specifiche di issued e ordered
			return false;
	    Double toBePayed = ord.getPricePerUnit()*ord.getQuantity();
	    	
	    if(!this.recordBalanceUpdate(-toBePayed,"Order")) //sottraggo dal balance la somma richiesta per l'ordine
	    		return false; //non è specificato come caso ma secondo me è importante
	    	
	    	/*MyBalanceOperation bo = new MyBalanceOperation(orderId, "Order");
	    	bo.setMoney(toBePayed);
	    	operations.put(orderId, bo);*/
		
		ord.setStatus("PAYED");
		return true;
	} 
	
	public boolean recordOrderArrival(Integer id) {
		MyOrder ord = orders.get(id);  
		if(ord == null)
			return false;
		String status = ord.getStatus();
		if(!status.equals("PAYED") && !status.equals("COMPLETED")) //confusione nelle specifiche di completed e payed
			return false;
		ord.setStatus("COMPLETED"); 
		return true;
	}
	
	public List<Order> getAllOrders() {
		return new ArrayList<Order>(orders.values());
	}

	public List<SaleTransaction> getAllSales() { 
		return new ArrayList<SaleTransaction>(sales.values()); 
	}
	
	public MyOrder getOrderById(Integer orderId) {
		return orders.get(orderId); //confusione nelle specifiche di completed e payed
	}
	
	
	
	
	
	//SALE TRANSACTION
	public Integer issueSaleTransaction() { 
		OperationsCounter.add();
    	Integer saleID = OperationsCounter.getCounter();
    	MySaleTransaction sale = new MySaleTransaction(saleID);
    	sale.setStatus("OPENED");
    	sales.put(saleID, sale);
    	return saleID; 
	}
	
	//type 0 for add, 1 for remove
	public boolean addOrRemoveProductToSale(Integer transactionId, String productCode, int amount, int type, Double pricePerUnit, String description) {
		MySaleTransaction sale = sales.get(transactionId);
		if(sale == null || !sale.getStatus().equals("OPENED"))
			return false;
		if(type == 0)
			return sale.addProductToSale(productCode, amount, pricePerUnit, description);
		else
			return sale.removeProductFromSale(productCode, amount);
	}
	
	public MySaleTransaction getSaleTransactionById(Integer transactionId) {
		return sales.get(transactionId);
	} 
	
	//powered by anto
	public boolean recoverBalanceOperation(Integer balanceID,String typeOp,Double money,LocalDate date,String time,String stat) {
		MyBalanceOperation bo = new MyBalanceOperation(balanceID, typeOp);
		bo.setStatus(stat);
		bo.setMoney(money);
		bo.setTime(time);
		bo.setDate(date);
		operations.put(balanceID, bo);
		return true;
	}
	 
	
	public MyBalanceOperation addNewBalanceOperationSale(MySaleTransaction sale) {
		Integer balanceID = sale.getTicketNumber();
		MyBalanceOperation bo = new MyBalanceOperation(balanceID, "Sale");
		bo.setStatus("CLOSED");
		operations.put(balanceID, bo); 
		
		return bo;
	} 
	 
	public boolean removeSaleTransaction(Integer transactionId, Catalogue catalogue) {
		MySaleTransaction sale = sales.get(transactionId);
		if(sale==null)
			return false;
		sale.removeAllProductsFromSale(catalogue);
		sales.remove(transactionId);
		return true;
	} 
	
	public boolean recoverSale(Integer ticketNumber, Double discountRate, Double price, Integer pointsEarned, String status){
		MySaleTransaction sale = new MySaleTransaction(ticketNumber);
    	sale.setStatus(status);
		sale.setDiscountRate(discountRate);
		sale.setPrice(price);
		sale.setPointsEarned(pointsEarned);
    	sales.put(ticketNumber, sale);
		return true;
	}
	
	public boolean recoverTicket(Integer ticketNumber, String barCode, String description, Integer amount, Double pricePerUnit, Double total, Double discountRate, Integer amountReturned){
		MySaleTransaction sale = getSaleTransactionById(ticketNumber);
		sale.setDbUpdating(true);
		sale.addProductToSale(barCode,amount,pricePerUnit,description);
		sale.addDiscountToProduct(barCode,discountRate);
		//sale.addAmountReturned(barCode,amountReturned);
		sale.setDbUpdating(false);
		return true; 
	}

	//RETURN TRANSACTION
	public Integer issueReturnTransaction(MySaleTransaction sale) {
		OperationsCounter.add();
    	Integer returnID = OperationsCounter.getCounter();
    	MyReturnTransaction ret = new MyReturnTransaction(returnID, sale);
    	ret.setStatus("OPENED");
    	returns.put(returnID, ret);
    	return returnID;
	}
	
	public MyReturnTransaction getReturnTransactionById(Integer returnId) {
		return returns.get(returnId);
	}
	
	public boolean removeReturnTransaction(Integer returnId){
		if(returns.remove(returnId)==null)
			return false;
    	return true;
	} 
	
	public MyBalanceOperation addNewBalanceOperationReturn(MyReturnTransaction ret) {
		Integer balanceID = ret.getReturnID();
		MyBalanceOperation bo = new MyBalanceOperation(balanceID, "Return");
		bo.setStatus("CLOSED");
		operations.put(balanceID, bo);
		
		return bo;
	}
	
	
	
	//BALANCE
	/*
	public boolean updateBalance(Double value) {
		if(value < 0 && Math.abs(value) > this.balance)
			return false;

		this.balance = this.balance + value;
		
		return true;
	}*/
	
	public boolean recordBalanceUpdate(Double value, String type) { 
		if(value < 0 && Math.abs(value) > this.balance)
			return false; 
		
		//solo per le operazioni Credit a Debit e Order
		if(type.equals("Debit") || type.equals("Credit") || type.equals("Order")) {
			OperationsCounter.add();
			Integer opID = OperationsCounter.getCounter();
			MyBalanceOperation bo;
			bo = new MyBalanceOperation(opID, type);
			bo.setMoney(Math.abs(value));
			operations.put(opID, bo);
		}
		
		this.balance = this.balance + value;
		
		return true;
	}
	
	public Double computeBalance(){
		
		double balance = operations.values().stream().map(o -> {
			if(o.getType().equals("Debit") || o.getType().equals("Order"))
				return -o.getMoney();
			return o.getMoney();
		}).collect(Collectors.summingDouble(x -> x));
		this.balance = balance;
		return balance;
		//return this.balance;
	}
	 
	public boolean removeBalanceOperation(Integer id) {
		if(operations.get(id) != null) { 
			operations.remove(id);
			return true;
		}
		else
			return false;
	}
	
	public BalanceOperation searchBalanceOperation(Integer id) {
		return operations.get(id);
	}
	
	public List<BalanceOperation> getBalanceOperationByDate(LocalDate from, LocalDate to) {
		if(from == null && to == null) {
			/*
			return operations.values().stream().filter(o -> {
				String type = o.getType();
				if(type.equals("Sale") || type.equals("Return")) {
					MyBalanceOperation mo = (MyBalanceOperation) o;
					if(!mo.getStatus().equals("PAYED"))
						return false;
				}
				return true;
			}).collect(Collectors.toList());
			*/
			return new ArrayList<BalanceOperation>(operations.values());
		}
		return operations.values().stream().filter(o->{
			/*
			String type = o.getType();
			if(type.equals("Sale") || type.equals("Return")) {
				MyBalanceOperation mo = (MyBalanceOperation) o;
				if(!mo.getStatus().equals("PAYED"))
					return false;
			}
			*/
			LocalDate date = o.getDate();
			if(from != null && to != null) {
				if((date.isAfter(from) || date.isEqual(from)) && (date.isBefore(to) || date.isEqual(to)) )
					return true;
			}
			if(from != null && to == null) {
				if(date.isAfter(from) || date.isEqual(from))
					return true;
			}
			if(from == null && to != null) {
				if(date.isBefore(to) || date.isEqual(to))
					return true;
			}
			return false;
			
		}).collect(Collectors.toList());
	}
}
