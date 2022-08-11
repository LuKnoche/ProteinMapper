package model;

public class SBMLCompartment {

	private String id;
	private String name;
	private boolean constant;
	
	public SBMLCompartment() {
		this.id="";
		this.name="";
		this.constant=false;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return this.id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public void setConstant(boolean constant) {
		this.constant = constant;
	}
}
