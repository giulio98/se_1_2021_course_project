package it.polito.ezshop.data;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.stream.Collectors;

/* VECCHIA VERSIONE
public class MyReturnTransaction extends MyBalanceOperation {
	private Integer returnID;
	private Integer saleID;
	private Double saleDiscount;
	private HashMap<String, TicketEntry> entry_per_barcode;
	private HashMap<String, Integer> returned_per_barcode;
	
	public MyReturnTransaction(Integer returnID, Integer saleID, Double saleDiscount, HashMap<String, TicketEntry> entry_per_barcode) {
		super(returnID, "Return");
		this.setReturnID(returnID);
		this.setSaleID(saleID);
		this.saleDiscount = saleDiscount;
		this.entry_per_barcode = entry_per_barcode;
		returned_per_barcode= new HashMap<String, Integer>();
		entry_per_barcode.values().stream().forEach(x-> {returned_per_barcode.put(x.getBarCode(),0);});
	}

	public boolean returnProduct(String productCode, int amount) {
		MyTicketEntry entry = (MyTicketEntry) entry_per_barcode.get(productCode);
		if (entry == null) //the product to be returned does not exists in the sale
			return false;
		else {
			if(entry.getAmount() < amount) //amount is higher than the one in the sale transaction
				return false;
			else {
				entry.addAmount(-amount); //update
				Integer retSoFar = returned_per_barcode.get(productCode);
				if(retSoFar==null) //non serve, ma può aiutare nel debug
					return false;
				else 
					returned_per_barcode.put(productCode,retSoFar+amount); //aggiorno i tornati
				return true;
			}
		}
	}
	
	public boolean rollback() {
		//return false quando?
		entry_per_barcode.values().stream().forEach(x->{
			Integer ret = returned_per_barcode.get(x.getBarCode());
			if(ret!=0){
				((MyTicketEntry)x).addAmount(ret);
			}
		});
		return true;
	}
	
	public boolean commit(Catalogue catalogue) {
		returned_per_barcode.entrySet().stream().forEach(e -> {
			MyProductType mpt = (MyProductType) catalogue.getProductTypeByBarCode(e.getKey());
			mpt.updateQuantity(e.getValue());
		});
		return true;
	}

	public Integer getReturnID() {
		return returnID;
	}

	public void setReturnID(Integer returnID) {
		this.returnID = returnID;
	}

	public Integer getSaleID() {
		return saleID;
	}

	public void setSaleID(Integer saleID) {
		this.saleID = saleID;
	}
	
	public double computeTotalPrice() { //da giulio, modificato da anto
		double ret = (double) entry_per_barcode.entrySet().stream().map(e-> {
			MyTicketEntry te = (MyTicketEntry) e.getValue();
			double pricePerUnit = te.getPricePerUnit();
			double discount = te.getDiscountRate();
			Integer amountRet = returned_per_barcode.get(e.getKey());
			
			double tot = pricePerUnit * amountRet;
			tot = tot - tot*discount;
			return tot;
		}).collect(Collectors.summingDouble(x -> x));
		ret = ret - ret * this.saleDiscount;
		return ret;
	}
	
}
*/

//NUOVA VERSIONE
public class MyReturnTransaction { 
	private Integer returnID;
	private Integer saleID;
	private HashMap<String, Integer> returnedPerBarcode;
	private MySaleTransaction saleTransaction;
	private String status;
	
	public MyReturnTransaction(Integer returnID, MySaleTransaction sale) {
		this.returnID = returnID;
		this.saleID = sale.getTicketNumber();
		this.returnedPerBarcode = new HashMap<String, Integer>();
		this.saleTransaction = sale;
		this.status = null;
		
		sale.getEntries().stream().forEach(te -> {
			returnedPerBarcode.put(te.getBarCode(), 0);
		});
	}

	public boolean returnProduct(String productCode, int amount) {   //test ok
		if(!returnedPerBarcode.containsKey(productCode))
			return false;
		MyTicketEntry te = this.saleTransaction.getTicketEntry(productCode);
		if(te == null || te.getAmount() < amount)
			return false;
		
		if(returnedPerBarcode.get(productCode)>0) {
			Integer quantity = returnedPerBarcode.get(productCode);
			returnedPerBarcode.put(productCode, amount + quantity);
		}
		else
			returnedPerBarcode.put(productCode, amount);
		
		//aggiornare la sale qui o in commit??????????????????
		return true;
	}
	
	public boolean commit(Catalogue catalogue) {  //test ok
		returnedPerBarcode.entrySet().stream().forEach(e -> {
			String barCode = e.getKey();
			Integer amount = e.getValue();
			
			MyProductType mpt = (MyProductType) catalogue.getProductTypeByBarCode(barCode);
			mpt.updateQuantity(-amount);
			
			saleTransaction.removeProductFromSale(barCode, amount);
		});
		//computePrice ???
		return true;
	}
	
	public double computeTotalPrice() {   //test ok
		double ret = saleTransaction.getEntries().stream().map(te -> {
			MyTicketEntry mte = (MyTicketEntry) te;
			double pricePerUnit = mte.getPricePerUnit();
			double discount = mte.getDiscountRate();
			Integer amountRet = returnedPerBarcode.get(mte.getBarCode());
			
			double tot = pricePerUnit * amountRet;
			tot = tot - tot*discount;
			return tot;
		}).collect(Collectors.summingDouble(x -> x));
		
		ret = ret - ret * saleTransaction.getDiscountRate();
		return ret;
	}
	
	
	
	public Integer getReturnID() {
		return returnID;
	}

	public void setReturnID(Integer returnID) {
		this.returnID = returnID;
	}

	public Integer getSaleID() {
		return saleID;
	}

	public void setSaleID(Integer saleID) {
		this.saleID = saleID;
	}

	public HashMap<String, Integer> getReturnedPerBarcode() {
		return returnedPerBarcode;
	}

	public void setReturnedPerBarcode(HashMap<String, Integer> returnedPerBarcode) {
		this.returnedPerBarcode = returnedPerBarcode;
	}

	public MySaleTransaction getSaleTransaction() {
		return saleTransaction;
	}

	public void setSaleTransaction(MySaleTransaction saleTransaction) {
		this.saleTransaction = saleTransaction;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}