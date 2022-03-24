package it.polito.ezshop.data;

public class MyOrder implements Order {
	private Integer orderID;
	private Integer balanceID;
	private String productCode;
	private Double pricePerUnit;
	private int quantity;
	private String status;	 
	
	public MyOrder(Integer orderID, String productCode, Integer quantity, Double pricePerUnit){
		this.orderID = orderID;
		this.productCode = productCode;
		this.quantity = quantity;
		this.pricePerUnit = pricePerUnit;
		this.status = null;
	}
	
	@Override 
	public Integer getBalanceId() {
		return this.balanceID;
	}

	@Override
	public void setBalanceId(Integer balanceId) {
		this.balanceID = balanceId;
	}

	@Override
	public String getProductCode() {
		return this.productCode;
	}

	@Override
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	@Override
	public double getPricePerUnit() {
		return this.pricePerUnit;
	}

	@Override
	public void setPricePerUnit(double pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}

	@Override
	public int getQuantity() {
		return this.quantity;
	}

	@Override
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public String getStatus() {
		return this.status;
	}

	@Override
	public void setStatus(String status) {
		this.status=status;
	}

	@Override
	public Integer getOrderId() {
		return this.orderID;
	}

	@Override
	public void setOrderId(Integer orderId) {
		this.orderID = orderId;
	}
	
	

}
