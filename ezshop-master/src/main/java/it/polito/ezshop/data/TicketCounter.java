package it.polito.ezshop.data;

public class TicketCounter {
private static Integer counter = 0;
	
	public static Integer getCounter() {
		return counter;
	}

	public static void setCounter(Integer counter) {
		TicketCounter.counter = counter;
	}
	
	public static void add() {
		counter++;
	}
	
	public static void sub() {
		counter--;
	}

}
