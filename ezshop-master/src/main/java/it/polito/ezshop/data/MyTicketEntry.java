package it.polito.ezshop.data;

public class MyTicketEntry implements TicketEntry {
	private String barCode;
	private String description;
	private int amount;
	private double pricePerUnit;
	private double discountRate;
	private double total;
	private int amountReturned;
	
	public MyTicketEntry(String barCode, Integer amount, Double pricePerUnit, String description){
		this.barCode = barCode;
		this.amount = amount;
		this.pricePerUnit = pricePerUnit;
		this.total = pricePerUnit * amount;
		this.amountReturned = 0;
		this.discountRate = 0.0;
		this.description = description;
	} 
	
	
	public double getTotal() {
		computeTotal();
		return total;
	}

	public void computeTotal() {
		double tot = this.pricePerUnit * this.amount;
		tot = tot - tot*this.discountRate;
		
		this.total = tot;
	}
	
	/*
	public void addAmount(Integer amount) {
		this.amount += amount;
		setTotal();
	}
	*/
	
	public void updateAmountReturned(int amount) {
		this.amountReturned += amount;
	}
	
	public int getAmountReturned() {
		return amountReturned;
	}
	
	
	
	@Override
	public String getBarCode() {
		return barCode;
	}

	@Override
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	@Override
	public String getProductDescription() {
		return description;
	}

	@Override
	public void setProductDescription(String productDescription) {
		description = productDescription;
	}

	@Override
	public int getAmount() {
		return amount;
	}

	@Override
	public void setAmount(int amount) {
		this.amount = amount;
		computeTotal();
	}

	@Override
	public double getPricePerUnit() {
		return pricePerUnit;
	}

	@Override
	public void setPricePerUnit(double pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
		computeTotal();
	}

	@Override
	public double getDiscountRate() {
		return discountRate;
	}

	@Override
	public void setDiscountRate(double discountRate) {
		this.discountRate = discountRate;
		computeTotal();
	}
/* E' UGUALE A COMPUTETOTAL
	public void setTotal() {
		double tot = this.pricePerUnit * this.amount;
		tot = tot - tot*this.discountRate;
		
		this.total = tot;
	}
*/
	
	public void setAmountReturned(int amountReturned) {
		this.amountReturned=amountReturned;
	}
	
}
