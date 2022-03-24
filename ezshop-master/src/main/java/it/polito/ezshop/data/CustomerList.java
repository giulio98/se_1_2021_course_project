package it.polito.ezshop.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerList {
	private HashMap<Integer,MyCustomer> customers; 
	private HashMap<String,Integer> customersID; 
	
	public CustomerList(){
		this.customers=new HashMap<Integer,MyCustomer>();
		this.customersID=new HashMap<String,Integer>();
	}
	
	public boolean deleteCustomer(Integer id) { //test done
		MyCustomer customer=customers.get(id);
		if(customer==null)
			return false;
		customers.remove(id);
		String name=customer.getCustomerName();
		if(name==null || name.isEmpty())
			return false;
		customersID.remove(name);
		return true;
	}
	
	public List<Customer> getAllCustomer(){  //test done
		if(customers.isEmpty())
			return new ArrayList<Customer>();
		else
			return new ArrayList<Customer>(customers.values());
	}
	
	public MyCustomer getCustomer(Integer id) {   //test done
		return customers.get(id);
	}
		
	public String getCustomerName(Integer id) {   //test done
		MyCustomer cust = customers.get(id);
		if(cust!=null)
			return cust.getCustomerName();
		else
			return "";
	}
	public Integer addCustomer(String name) {    //test done
		if(customersID.get(name)!=null) {
			return -1;
		}
		CustomersCounter.add();
		Integer newID= CustomersCounter.getCounter();
		MyCustomer newCustomer= new MyCustomer(newID,name);
		/*
		if( newCustomer==null ) {
			CustomersCounter.sub();
			return -1;
		}
		*/
		customers.put(newID, newCustomer);
		customersID.put(name, newID);
		return newID;
		
	}
	
	public MyCustomer searchCustomer(String name) {   //test done
		Integer customerID=customersID.get(name);
		if(customerID==null) 
			return null;
		MyCustomer customer=customers.get(customerID);
		return customer;
	}
	
	public boolean modifyCustomer(String name, String lcard) {     //test done
		if(name==null || name == "")
			return false;
		MyCustomer customer=searchCustomer(name);
		if(customer==null)
			return false;
		customer.setCustomerCard(lcard);
		customer.setPoints(0);
		return true;
	}
	
	public boolean modifyCustomerName(Integer id, String name) {    //test done
		if(name==null || name == "")
			return false;
		MyCustomer customer=getCustomer(id);
		if(customer==null)
			return false;
		customers.remove(id);
		customersID.remove(customer.getCustomerName());
		customer.setCustomerName(name);
		customers.put(id, customer);
		customersID.put(name, id);
		return true;
	}
	
	public String searchCardOwner(String lcard) {    //test done
		if(lcard==null || lcard == "")
			return "";
		List<MyCustomer> listc = customers.values().stream().filter(s->{
			if(s.getCustomerCard() != null)
				if(s.getCustomerCard().equals(lcard))
					return true;
			return false;
		}).collect(Collectors.toList());
		if(listc.size() == 0)
			return "";
		MyCustomer cust = listc.get(0);//customers.values().stream().filter(s->s.getCustomerCard().equals(lcard)).collect(Collectors.toList()).get(0);
		return cust.getCustomerName();
	}

	public boolean updatePoints(String lcard, Integer points) {  //test done
		if(lcard==null || lcard == "")
			return false;
		String cname = searchCardOwner(lcard);
    	if(/*cname == null ||*/ cname.compareTo("")==0)
    		return false;
    	MyCustomer customer = searchCustomer(cname);
    	/*
    	if (customer==null)
    		return false;
    	*/
    	customer.addPoints(points);
    	return true;
	}
	
}
