package it.polito.ezshop.data;

public class Position {
	private String aisleID;
	private String rackID;
	private String levelID;
	private String location;
	//forse conviene tenere anche la location come stringa intera per controllare velocemente l'univocità della posizione
	
	public Position(String location){
		setLocation(location);
	} 

	public String getLocation() {
		return this.location;
	}
	public void setLocation(String location) {
		if(location == null || location.isEmpty()) {
			this.location="";
		}else {
			String [] fields = location.split("-");
			this.aisleID = fields[0];
			this.rackID = fields[1];
			this.levelID = fields[2];
			this.location=this.aisleID+"-"+this.rackID+"-"+this.levelID;
		}
		
	}
}
