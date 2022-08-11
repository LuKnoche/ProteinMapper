package model;

public class SBMLFluxObjective {

	private String reaction;
	private double coefficient;
	
	public SBMLFluxObjective() {
		this.coefficient = 0;
		this.reaction="";
	}
	
	public String getReaction() {
		return reaction;
	}
	public void setReaction(String reaction) {
		this.reaction = reaction;
	}
	public double getCoefficient() {
		return coefficient;
	}
	public void setCoefficient(double coefficient) {
		this.coefficient = coefficient;
	}
	
	
}
