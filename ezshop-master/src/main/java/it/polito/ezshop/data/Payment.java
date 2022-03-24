package it.polito.ezshop.data;

public class Payment {
	 
	private CreditCard card;
	private double price;
	private double returnPrice;
	private double cashOrCard; //>1:cash, 0:card
	private boolean committed;
	private Integer paymentID;
	
	public Payment(CreditCard cc, double price, double cashOrCard, boolean committed, Integer paymentID){
		this.cashOrCard=cashOrCard;
		if(cashOrCard==0)
			card = cc; //cosa mettere in balance?
		else
			card=null;
		this.price=price; 
		this.returnPrice=0;
		this.committed=committed;
		this.paymentID=paymentID;	
	}
	
	public double computeChange() {
		this.returnPrice = cashOrCard - price;
		return this.returnPrice;
	}
	
	public double getPrice() {
		return price;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	
	public double getReturnPrice() {
		return returnPrice;
	}
	
	public void setReturnPrice(double returnPrice) {
		this.returnPrice = returnPrice;
	}
	
	public double getCashOrCard() {
		return cashOrCard;
	}
	
	public void setCashOrCard(double cashOrCard) {
		this.cashOrCard = cashOrCard;
	}
	
	public boolean isCommitted() {
		return committed;
	}
	
	public void setCommitted(boolean committed) {
		this.committed = committed;
	}
	
	public Integer getPaymentID() {
		return paymentID;
	}
	
	public void setPaymentID(Integer paymentID) {
		this.paymentID = paymentID;
	}
	
	public CreditCard getCard() {
		return card;
	}
	
	public void setCard(CreditCard card) {
		this.card = card;
	}

}
