package model;

import java.util.ArrayList;
import java.util.List;

/*
 * @ represents all the data inside a species entry from a BiGGModels SBML
 * 
 * 
 */

public class SBMLSpecies {
	
	private String id;
	private boolean constant;
	private boolean boundaryCondition;
	private boolean hasOnlySubstanceUnits;
	private String name;
	private String metaId;
	private String sboTerm;
	private String compartment;
	private int fbcCharge;                     
	private String fbcChemicalFormula;
	private List<String> identifiers;
	
	public SBMLSpecies() {
		this.identifiers = new ArrayList<>();
		this.id = "";
		this.constant = false;
		this.boundaryCondition = false;
		this.hasOnlySubstanceUnits = false;
		this.name = "";
		this.metaId = "";
		this.sboTerm = "";
		this.compartment = "";
		this.fbcCharge = 0;
		this.fbcChemicalFormula = "";
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
	public void setBoundaryCondition(boolean boundary) {
		this.boundaryCondition = boundary;
	}
	public void setHasOnlySubstanceUnits(boolean onlySubstanceUnits) {
		this.hasOnlySubstanceUnits = onlySubstanceUnits;
	}
	public void setMetaId(String id) {
		this.metaId = id;
	}
	public void setSboTerm(String term) {
		this.sboTerm = term;
	}
	public void setCompartment(String c) {
		this.compartment = c;
	}
	public void setFbcCharge(int charge) {
		this.fbcCharge = charge;
	}
	public void setFbcChemicalFormula(String formula) {
		this.fbcChemicalFormula = formula;
	}
	public void setIdentifiers(List<String> identifiers) {
		this.identifiers = identifiers;
	}
	public String getCompartment() {
		return this.compartment;
	}

}
