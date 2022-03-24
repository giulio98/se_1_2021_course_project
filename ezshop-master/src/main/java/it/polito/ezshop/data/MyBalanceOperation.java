package it.polito.ezshop.data;

import java.sql.Date;
import java.text.DateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class MyBalanceOperation implements BalanceOperation {
	
	private Payment payment; 
	private Double money;
	private Integer balanceID;
	private LocalDate date;
	private String time;
	private String typeOp; //"{"Return","Sale","Order","Credit","Debit"}
	private String status; // (OPENED,CLOSED,PAYED) (SaleTransaction) 
						   // (OPENED,CLOSED,PAYED) (ReturnTransaction)
	
	public MyBalanceOperation(Integer id, String typeOp){ //fatto da anto
		this.balanceID=id;
		this.date=LocalDate.now();
		Calendar calendar = new GregorianCalendar();
		int ore = calendar.get(Calendar.HOUR);
		int minuti = calendar.get(Calendar.MINUTE);
		int secondi = calendar.get(Calendar.SECOND);
		this.time = String.valueOf(ore) + ":" + String.valueOf(minuti) + ":" + String.valueOf(secondi);
		this.typeOp = typeOp;
		if(typeOp.compareTo("Sale")==0)
			status="OPENED";
		else if(typeOp.compareTo("Return")==0)
			status="OPENED";
		setPayment(null);
		this.money = 0.0;
	}
	
	public double getTotalPrice() {
		if(payment == null)
			return money;
		return payment.getPrice();
	}
	
	public boolean startPayment(CreditCard cc, double amount) {
		PaymentCounter.add();
		payment = new Payment(cc, amount, 0, false, PaymentCounter.getCounter());
		/*
		if(payment==null) {
			PaymentCounter.sub();
			return false;
		}
		*/
		this.money = amount;
		return true;
	}
	
	public double startPayment(double price, double cash) {
		PaymentCounter.add();
		payment = new Payment(null, price, cash, false, PaymentCounter.getCounter());
		/*
		if(payment==null) {
			PaymentCounter.sub();
			return -1;
		}
		*/
		this.money = price;
		return payment.computeChange();
	}
	
	public double getReturnValue() {
		payment.computeChange();
		return payment.getReturnPrice();
		
	}
	
	@Override
	public int getBalanceId() {
		return this.balanceID;
	}

	@Override
	public void setBalanceId(int balanceId) {
		this.balanceID=balanceId;

	}

	@Override
	public LocalDate getDate() {
		return this.date;
	}

	@Override
	public void setDate(LocalDate date) {
		this.date=date;

	}

	@Override
	public double getMoney() {
		//PIETRO
		return getTotalPrice();
	}

	@Override
	public void setMoney(double money) {
		//PIETRO
		if(payment == null)
			this.money = money;
		else
			payment.setPrice(money);
	}

	@Override
	public String getType() {
		return this.typeOp;
	}

	@Override
	public void setType(String type) {
		this.typeOp=type;

	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

}
