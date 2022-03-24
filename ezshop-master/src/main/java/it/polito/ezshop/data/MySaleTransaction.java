package it.polito.ezshop.data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/*VECCHIA VERSIONE
public class MySaleTransaction extends MyBalanceOperation implements SaleTransaction  {
	private Integer saleID;
	private Integer ticketNumber;
	private Double cost;
	private Double discountRate;
	private HashMap<String, TicketEntry> entry_per_barcode;
	private HashMap<Integer, MyReturnTransaction> returns;
	private Integer pointsEarned;
	
	public MySaleTransaction(Integer saleID){
		super(saleID, "Sale");
		this.saleID = saleID;
		entry_per_barcode = new HashMap<String, TicketEntry>();
		returns = new HashMap<Integer, MyReturnTransaction>();
		this.discountRate=(double) 0;
		this.cost = 0.0;
		TicketCounter.add();
		this.ticketNumber = TicketCounter.getCounter();
	}
	
	public boolean addProductToSale(String productCode, int amount, Double pricePerUnit, String description) {
		if(this.getStatus().compareTo("OPENED")!=0) //da giulio
			return false;
		
		MyTicketEntry te = (MyTicketEntry) entry_per_barcode.get(productCode);
		if(te == null) {
			MyTicketEntry entry = new MyTicketEntry(productCode, amount, pricePerUnit, description);
			if(entry==null)
				return false;
			entry_per_barcode.put(productCode, entry);
		}else {
			Integer newAmount = te.getAmount() + amount;
			te.setAmount(newAmount);
		}
		
		computeTotalPrice();
		return true;
	}
	
	public boolean removeProductToSale(String productCode, int amount) {
		MyTicketEntry entry = (MyTicketEntry) entry_per_barcode.get(productCode);
		if (entry == null || entry.getAmount() < amount)
			return false;
		else
			entry.addAmount(-amount);
		
		if(entry.getAmount() == 0)
			entry_per_barcode.remove(productCode);
		
		computeTotalPrice();
		return true;
	}
	
	public boolean removeAllProductsFromSale(Catalogue catalogue) {
		entry_per_barcode.entrySet().stream().forEach(e -> {
			MyProductType mpt = (MyProductType) catalogue.getProductTypeByBarCode(e.getKey());
			mpt.updateQuantity(e.getValue().getAmount());
		});
		return true;
	}
	
	public boolean addDiscountToProduct(String productCode, double discount) {
		MyTicketEntry entry = (MyTicketEntry) entry_per_barcode.get(productCode);
		if (entry == null)
			return false;
		else
			entry.setDiscountRate(discount);
		computeTotalPrice();
		return true;
	}
	
	public Integer getPointsEarned() {
		this.computePointsEarned();
		return pointsEarned;
	}
	
	public void computePointsEarned() {
		this.pointsEarned = (int) Math.floor(computeTotalPrice()/10);
	}
	
	public double computeTotalPrice() {
		double ret = entry_per_barcode.values().stream().map(x -> {
			MyTicketEntry te = (MyTicketEntry) x;
			return te.getTotal();
		}).collect(Collectors.summingDouble(x -> x));
		ret = ret - ret*this.discountRate;
		this.cost = ret;
		return ret;
	}
	
	public void startReturnTransaction(Integer returnID, Double saleDiscount) {
		returns.put(returnID, new MyReturnTransaction(returnID, this.saleID, saleDiscount, entry_per_barcode));
	}
	
	public MyReturnTransaction getReturnTransaction(Integer returnID) {
		return returns.get(returnID);
	}
	
	@Override
	public Integer getTicketNumber() {
		return ticketNumber;
	}

	@Override
	public void setTicketNumber(Integer ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	@Override
	public List<TicketEntry> getEntries() {
		return new ArrayList<TicketEntry>(entry_per_barcode.values());
	}

	@Override
	public void setEntries(List<TicketEntry> entries) {
		entries.forEach(entry -> { entry_per_barcode.put(entry.getBarCode(), entry); });
	}

	@Override
	public double getDiscountRate() {
		return discountRate;
	}

	@Override
	public void setDiscountRate(double discountRate) {
		this.discountRate = discountRate;
	}

	@Override
	public double getPrice() {
		return cost;
	}

	@Override
	public void setPrice(double price) {
		cost = price;
	}
	
	@Override
	public int getBalanceId() {
		return saleID;
	}
	
	@Override
	public void setBalanceId(int balanceId) {
		this.saleID=saleID;
	}
	
}
*/

public class MySaleTransaction implements SaleTransaction  {
	private Integer ticketNumber; //saleID
	private HashMap<String, TicketEntry> entryPerBarcode;
	private double discountRate;
	private double price;
	private String status;
	private int pointsEarned; 
	private boolean dbUpdating = false;
	
	public MySaleTransaction(Integer saleID){
		this.ticketNumber = saleID;
		entryPerBarcode = new HashMap<String, TicketEntry>();
		this.discountRate = 0.0;
		this.price = 0.0;
		this.status = null;
		this.pointsEarned = 0;
		this.status = "OPENED";
	}
	
	public boolean addProductToSale(String productCode, int amount, Double pricePerUnit, String description) {
		if(!this.getStatus().equals("OPENED") && !this.dbUpdating) //se la sale non è aperta
			return false;
		MyTicketEntry te = (MyTicketEntry) entryPerBarcode.get(productCode);
		if(te == null) {
			MyTicketEntry entry = new MyTicketEntry(productCode, amount, pricePerUnit, description);
			/*
			if(entry==null)
				return false;
			*/
			entryPerBarcode.put(productCode, entry);
		}else {
			Integer newAmount = te.getAmount() + amount;
			te.setAmount(newAmount);
		}
		
		computeTotalPrice();
		return true;
	}
	
	public boolean removeProductFromSale(String productCode, int amount) {
		MyTicketEntry entry = (MyTicketEntry) entryPerBarcode.get(productCode);
		if (entry == null || entry.getAmount() < amount)
			return false; 
		else
			entry.setAmount(entry.getAmount()-amount);
		
		/*if(entry.getAmount() == 0)              
			entryPerBarcode.remove(productCode);   
			
		se decommenti falliscono 2 test api (returnCash e returnCredit Payment)*/
		
		computeTotalPrice();
		return true;
	}
	
	public boolean addDiscountToProduct(String productCode, double discount) {
		MyTicketEntry entry = (MyTicketEntry) entryPerBarcode.get(productCode);
		if (entry == null)
			return false;
		else
			entry.setDiscountRate(discount);
		
		computeTotalPrice();
		return true;
	}
	/*
	public boolean addAmountReturned(String productCode, Integer amountReturned) {
		MyTicketEntry entry = (MyTicketEntry) entryPerBarcode.get(productCode);
		if (entry == null)
			return false;
		else
			entry.setAmountReturned(amountReturned);
		return true;
	}
	*/
	public double computeTotalPrice() {
		double tot = entryPerBarcode.values().stream().map(x -> {
			MyTicketEntry te = (MyTicketEntry) x;
			return te.getTotal();
		}).collect(Collectors.summingDouble(x -> x));
		
		tot = tot - tot*this.discountRate;
		this.price = tot;
		computePointsEarned(this.price);
		
		return this.price;
	}
	
	public void computePointsEarned(double price) {
		this.pointsEarned = (int) Math.floor(price/10);
	}
	
	public void computePointsEarned() {
		this.pointsEarned = (int) Math.floor(computeTotalPrice()/10);
	}
	
	public int getPointsEarned() {
		this.computePointsEarned();
		return this.pointsEarned;
	}
	
	public int getPoints() { //per i test
		return this.pointsEarned;
	}
	
	public void setPointsEarned(Integer points) {
		this.pointsEarned = points;
	}
	
	public boolean removeAllProductsFromSale(Catalogue catalogue) {
		entryPerBarcode.entrySet().stream().forEach(e -> {
			MyProductType mpt = (MyProductType) catalogue.getProductTypeByBarCode(e.getKey());
			mpt.updateQuantity(e.getValue().getAmount());
		});
		return true;
	}
	
	public MyTicketEntry getTicketEntry(String productCode) {
		return (MyTicketEntry) entryPerBarcode.get(productCode);
	}
	

	
	
	
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public Integer getTicketNumber() {
		return ticketNumber;
	}

	@Override
	public void setTicketNumber(Integer ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	@Override
	public List<TicketEntry> getEntries() {
		return new ArrayList<TicketEntry>(entryPerBarcode.values());
	}

	@Override
	public void setEntries(List<TicketEntry> entries) {
		entries.forEach(entry -> { 
			entryPerBarcode.put(entry.getBarCode(), entry); 
		});
	}

	@Override
	public double getDiscountRate() {
		return discountRate;
	}

	@Override
	public void setDiscountRate(double discountRate) {
		this.discountRate = discountRate;
		computeTotalPrice();
	}

	public boolean getDbUpdating() {
		return this.dbUpdating;
	}

	public void setDbUpdating(boolean set) {
		this.dbUpdating = set;
	}


	@Override
	public double getPrice() {
		return price;
	}

	@Override
	public void setPrice(double price) {
		this.price = price;
	}
	
}

