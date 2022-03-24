package it.polito.ezshop.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class MyProductType implements ProductType {
	private Integer productID;
	private String barCode;
	private String description; 
	private Double sellPrice;
	private String notes; 
	private Integer quantity;
	//private Integer discountRate = 0;
	private Position position;
	
	//CHANGES
	private List<String> rfids = new ArrayList<String>();
	private HashMap<String, Integer> soldRfids = new HashMap<String, Integer>();
	
	
	public MyProductType(Integer productID, String barCode, String description, Double sellPrice, String notes){
		this.productID = productID;
		this.barCode = barCode;
		this.description = description;
		this.sellPrice = sellPrice;
		if(notes == null)
			this.notes = "";
		else
			this.notes = notes;
		this.position = new Position("");
		this.quantity = 0;
	}
	
	@Override
	public Integer getQuantity() {
		return this.quantity;
	}

	@Override
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	@Override
	public String getLocation() {
		return this.position.getLocation();
	}

	@Override
	public void setLocation(String location) {
		this.position = new Position(location);
	}

	@Override
	public String getNote() {
		return this.notes;
	}

	@Override
	public void setNote(String note) {
		if(note == null)
			this.notes = "";
		else
			this.notes = note;
	}

	@Override
	public String getProductDescription() {
		return this.description;
	}

	@Override
	public void setProductDescription(String productDescription) {
		this.description = productDescription;
	}

	@Override
	public String getBarCode() {
		return this.barCode;
	}

	@Override
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	@Override
	public Double getPricePerUnit() {
		return this.sellPrice;
	}

	@Override
	public void setPricePerUnit(Double pricePerUnit) {
		this.sellPrice = pricePerUnit;
	}

	@Override
	public Integer getId() {
		return productID;
	}

	@Override
	public void setId(Integer id) {
		this.productID = id;
	}

	
	//aggiunte rispetto all'interfaccia
	/*
	public Integer getDiscountRate() {
		return discountRate;
	}

	public void setDiscountRate(Integer discountRate) {
		this.discountRate = discountRate;
	}
	*/
	public boolean updateQuantity(Integer toBeAdded) {
		Integer newQuantity;
		
		newQuantity = this.quantity + toBeAdded;
		if(newQuantity >= 0) {
			//this.quantity = newQuantity;
			setQuantity(newQuantity);
			return true;
		}
		
		return false; //se con l'update la nuova quantità andrebbe sotto zero viene ritornato falso e annulato l'update
	}
	
	public boolean updatePosition(String newPos) {
		if(newPos == null || newPos.isEmpty()) //reset della posizione
			this.position = new Position("");
		//gestire gli altri due controlli nella api -> productID non esiste o posizione già assegnata
		this.position = new Position(newPos); //aggiornamento
		return true; 
	}
	
	
	//CHANGES 
	public void addRFID(String RFID) {
		rfids.add(RFID); 
	} 
	public boolean RFIDisPresent(String RFID) {
		int i;
		for(i=0;i<rfids.size();i++)
			if(RFID.equals(rfids.get(i)))
				return true;
		return false;
	}
	
	public void addToSoldRfids(String RFID, Integer transactionId) {
		soldRfids.put(RFID, transactionId);
	}
	 
	public boolean rfidIsSold(String RFID) {
		if(soldRfids.containsKey(RFID))
			return true;
		return false;
	}
	
	public void removeRFIDFromSold(String RFID) {
		soldRfids.remove(RFID);
	} 
	
	public void removeAllSoldRfidsOfSale(Integer transactionId) {
		HashMap<String, Integer> newSold = new HashMap<String, Integer>();
		soldRfids.entrySet().forEach(e -> {
			if(e.getValue() != transactionId)
				newSold.put(e.getKey(), e.getValue());
		});
		soldRfids = newSold;
 	}
 
	public ArrayList<String> getRfidPerSale(Integer transactionId) {
		return soldRfids.entrySet().stream().filter((k)->soldRfids.get(k.getKey())==transactionId).map(HashMap.Entry::getKey).collect(Collectors.toCollection(ArrayList::new));
	}
}
