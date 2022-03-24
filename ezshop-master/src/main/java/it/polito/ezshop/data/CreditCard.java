package it.polito.ezshop.data;

public class CreditCard {
	
	private String creditCardNumber;
	private double balance;
	
	public CreditCard(String creditCardNumber, double balance){
		this.creditCardNumber=creditCardNumber;
		this.balance=balance;
	}
	
	public String getCreditCardNumber() {
		return creditCardNumber;
	}
	public void setCreditCardNumber(String creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}
	public double getBalance() {
		return balance; 
	}
	public void setBalance(double balance) {
		this.balance = balance; 
	}
	//questa va spostata PIETRO AT
	public boolean checkCreditCardLunh() {
		if(this.creditCardNumber==null)
			return false;
		int nDigits = this.creditCardNumber.length();
	    int nSum = 0;
	    boolean isSecond = false;
	    for (int i = nDigits - 1; i >= 0; i--)
	    {
	 
	        int d = this.creditCardNumber.charAt(i) - '0';
	 
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
	
	public boolean checkBalance(double price) {
		if(this.balance>=price)
			return true;
		else
			return false;
	}
	
	

}
