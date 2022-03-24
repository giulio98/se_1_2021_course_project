package it.polito.ezshop.data;

public class CardCounter {
	private static String counter="1000000000";
	
	public static String getCounter() { 
		return counter;
	}
	public static void setCounter(String c) {
		counter = c;
	}

	public static void add() {
		Long newCounter=Long.parseLong(counter);
		newCounter+=1;
		counter=newCounter.toString(); 
	}
	
	public static void sub() {
		Long newCounter=Long.parseLong(counter);
		newCounter-=1;
		counter=newCounter.toString();
	}
}
