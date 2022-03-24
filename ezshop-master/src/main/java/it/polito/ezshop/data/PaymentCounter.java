package it.polito.ezshop.data;

public class PaymentCounter {
private static Integer counter=0;
	
	
	public static Integer getCounter() {
		return counter;
	}

	public static void setCounter(Integer counter) {
		PaymentCounter.counter = counter;
	}
	
	public static void add() {
		counter++;
	}
	
	public static void sub() {
		counter--;
	}

}
