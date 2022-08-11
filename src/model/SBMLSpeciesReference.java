package model;

/*
 * @used in listOfReactions to link species to reactions and vice versa
 * 
 */
public class SBMLSpeciesReference {
	
	private String species;
	private Double stoichiometry;
	private boolean constant;
	
	public SBMLSpeciesReference() {
		this.species = "";
		this.stoichiometry = null;
		this.constant = false;
	}
	
	public void setSpecies(String species) {
		this.species = species;
	}
	public void setStoichiometry(Double stoichiometry) {
		this.stoichiometry = stoichiometry;
	}
	public void setConstant(boolean constant) {
		this.constant = constant; 
	}
	
	public String getSpecies() {
		return this.species;
	}
	public Double getStoichiometry() {
		return this.stoichiometry;
	}
	public boolean getConstant() {
		return this.constant;
	}
}
