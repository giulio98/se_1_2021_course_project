package it.polito.ezshop.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Employees {
	
	private HashMap<Integer,MyUser> users; //key: userID, value: MyUser
	private HashMap<String,Integer> usersNameID; //key: username, value: Id
	
	public Employees(){ 
		this.users=new HashMap<Integer,MyUser>();
		this.usersNameID=new HashMap<String,Integer>();
	}
	
	public boolean deleteUser(Integer id) { 
		MyUser user=users.get(id);
		if(user==null)
			return false; 
		users.remove(id);
		String username=user.getUsername();
		if(username==null || username.isEmpty())
			return false;
		usersNameID.remove(username);
		return true;
	}
	public List<User> getAllUsers(){
		if(users.isEmpty())
			return new ArrayList<User>(); 
		else
			return new ArrayList<User>(users.values());
	}
	public MyUser getUser(Integer id) {
		return users.get(id);
	}
	public Integer addUser(String username, String password, String role) {
		if(usersNameID.get(username)!=null) {
			return -1;
		} 
		EmployeesCounter.add();
		Integer newID= EmployeesCounter.getCounter();
		MyUser newUser= new MyUser(newID,role,username,password);
		/*
		if( newUser==null ) {
			EmployeesCounter.sub(); in quale caso??
			return -1;
		}
		*/
		users.put(newID, newUser);
		usersNameID.put(username, newID);
		return newID; 
		
	}
	public MyUser searchUser(String username, String password) { //aggiunto un altro parametro password
		Integer userID=usersNameID.get(username);
		if(userID==null)
			return null; 
		MyUser user=users.get(userID);
		if(user.verifyAutentication(password))
			return user;
		else
			return null;
		
	}
	
	
}
