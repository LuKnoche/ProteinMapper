package model;

public class SBMLParameter {

	private String id;
	private double value;
	private String sboTerm;
	private boolean constant;
	private String units;
	
	public SBMLParameter(String id, boolean constant) {
		this.id = id;
		this.constant = constant;
		this.value=0;
		this.sboTerm="";
		this.units="";
	}
	
	public void setValue(double value) {
		this.value = value;
	}
	public void setSboTerm(String term) {
		this.sboTerm = term;
	}
	public void setUnits(String units) {
		this.units = units;
	}
	public String getId() {
		return this.id;
	}
	public double getValue() {
		return this.value;
	}
	public String getSBOTerm() {
		return this.sboTerm;
	}
	public boolean getConstant() {
		return this.constant;
	}
	public String getUnits() {
		return this.units;
	}
	
}
