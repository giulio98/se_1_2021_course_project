package it.polito.ezshop.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Catalogue {
	
	private HashMap<Integer, MyProductType> products;
	
	//CHANGES
	private HashMap<String, MyProductType> rfidsProducts = new HashMap<String, MyProductType>();
	
	public Catalogue(){  
		this.products = new HashMap<Integer, MyProductType>();
	}
	
	public Integer addProductType(String description, String productCode, Double pricePerUnit, String note) {
		ProductCounter.add();
		Integer newID = ProductCounter.getCounter();
		
		MyProductType pt = new MyProductType(newID, productCode, description, pricePerUnit, note);
		/*
		if(pt == null) {
			ProductCounter.sub(); //aggiunta da giulio
			return -1;
		}
		*/
		products.put(newID, pt);
		
		return newID;
	}
	
	public boolean deleteProductType(Integer id){
		if(products.remove(id)!=null)
			return true;
		return false; 
	}
	
	public ProductType getProductTypeByBarCode(String barCode) {
		for(Map.Entry<Integer, MyProductType> me : products.entrySet()) {
			MyProductType pt = (MyProductType) me.getValue();
			if(pt.getBarCode().equals(barCode))
				return (ProductType) pt;
		}
		return null;
	}
	
	public List<ProductType> getAllProductTypes(){
		return new ArrayList<ProductType>(products.values());
	}
	
	public List<ProductType> getProductTypesByDescription(String description) {
		List<ProductType> prods = new ArrayList<ProductType>();
		
		for(Map.Entry<Integer, MyProductType> me : products.entrySet()) {
			MyProductType mpt = (MyProductType) me.getValue(); //va bene che qui ci sia MyProductType ?
			if(mpt.getProductDescription().contains(description)) {
				ProductType pt = (ProductType) mpt; //va bene questo cast ?
				prods.add(pt);
			}
		}
		return prods;
	}
	
	
	//aggiunte rispetto al design
	
	public MyProductType getProductTypeById(Integer id) {
		return this.products.get(id);
	}
	
	public boolean checkPosition(String pos) {
		for(Map.Entry<Integer, MyProductType> me : products.entrySet()) {
			MyProductType mpt = (MyProductType) me.getValue();
			if(mpt.getLocation().equals(pos))
				return false;
		}
		return true;
	}
	
	public boolean checkBarCode(String productCode) {
		
		int sum=0;
    	ArrayList<Integer> code=new ArrayList<Integer>(); //salvo in un arraylist di int i "caratteri" moltiplicati
    	for(int i=0; i<productCode.length()-1; i++) { //fino a lenght-1 perchè devo considerare solo i primi n-1 caratteri
    		if(productCode.length()==13) { //se la lunghezza è 13:posizione pari x1, dispari x3
    			if(i%2==0)
    				code.add(i, Integer.parseInt(productCode.substring(i,i+1))*1);
    			else
    				code.add(i, Integer.parseInt(productCode.substring(i,i+1))*3);
    		}
    		else { //se la lunghezza è 12 o 14:posizione pari x3, dispari x1
    			if(i%2==0)
    				code.add(i, Integer.parseInt(productCode.substring(i,i+1))*3);
    			else
    				code.add(i, Integer.parseInt(productCode.substring(i,i+1))*1);
    			
    		} 
    	}
    	for(int i=0; i<code.size(); i++ ) //calcolo la somma
    		sum=sum+code.get(i);
    	int remainder=sum%10;
    	int check=0;
    	if(remainder==0)
    		check=0;
    	else
    		check=10-remainder;
    	int digitToCheck= Integer.parseInt(productCode.substring(productCode.length()-1, productCode.length())); //prendo l'ultimo carattere
    	if(check!=digitToCheck)
    		return false;
    		
    	return true;
	}
	
	//CHANGES
	public boolean addToRfidsProducts(String rfid, MyProductType pt) {
		if(rfidsProducts.containsKey(rfid))
			return false; 
		else
			rfidsProducts.put(rfid, pt);
		return true;
	}
	
	public MyProductType getProductTypeByRFID(String RFID) {
		return rfidsProducts.get(RFID);
	}
	
}
