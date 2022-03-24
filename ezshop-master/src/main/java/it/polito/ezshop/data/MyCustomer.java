package it.polito.ezshop.data;

public class MyCustomer implements Customer {
	private Integer customerID;
	private String name;
	private String lcardID;
	private Integer lcardPoints;
	
	
	public MyCustomer(Integer customerID, String name){
		this.customerID=customerID;
		this.name=name;
	}
	 
	
	@Override
	public String getCustomerName() {
		return name;
	}

	@Override
	public void setCustomerName(String customerName) {
		name = customerName;
	}

	@Override
	public String getCustomerCard() {
		return lcardID;
	}

	@Override
	public void setCustomerCard(String customerCard) {
		lcardID=customerCard;
	}

	@Override
	public Integer getId() {
		return customerID;
	}

	@Override
	public void setId(Integer id) {
		customerID=id;
	}

	@Override
	public Integer getPoints() {
		return lcardPoints;
	}

	@Override
	public void setPoints(Integer points) {
		lcardPoints=points;
	}
	
	public void addPoints(Integer points) {
		lcardPoints+=points;
	}

}
