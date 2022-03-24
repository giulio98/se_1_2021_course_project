package it.polito.ezshop.data;

public class MyUser implements User {
	private Integer userID;
	private String role;
	private String username;
	private String password;
	
	public MyUser(Integer userID,String role,String username,String password){
		this.userID=userID;
		this.role=role;
		this.username=username;
		this.password=password;
	}
	
	@Override
	public Integer getId() {
		return this.userID;
	}

	@Override
	public void setId(Integer id) {
		this.userID=id;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public void setUsername(String username) {
		this.username=username;
	}

	@Override
	public String getPassword() {
	    return this.password;
	}

	@Override
	public void setPassword(String password) {
		this.password=password;
	}

	@Override
	public String getRole() {
		return this.role;
	}

	@Override
	public void setRole(String role) {
		this.role=role;
	}
	
	public boolean verifyAutentication(String password) {
		if(this.password.compareTo(password)==0)
			return true;
		else 
			return false;
		
	}

}
