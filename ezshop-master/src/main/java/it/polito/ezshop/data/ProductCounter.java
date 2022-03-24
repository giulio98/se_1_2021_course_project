package it.polito.ezshop.data;

public class ProductCounter {
	private static Integer counter = 0;
	
	public static Integer getCounter() {
		return counter;
	}

	public static void setCounter(Integer counter) {
		ProductCounter.counter = counter;
	}
	
	public static void add() {
		counter++;
	}
	
	public static void sub() {
		counter--;
	}
	
}
