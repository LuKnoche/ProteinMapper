package model;

public class SBMLGeneProductReference implements SBMLAssociation{
	
	private String geneProductRef;
	private String type;
	
	public SBMLGeneProductReference() {
		this.geneProductRef = "";
		this.type = "fbc:geneProductRef";              // needed for ease of acces on the frontend
	}
	
	public void setGeneProduct(String gp) {
		this.geneProductRef = gp;
	}
	public String getGeneProduct() {
		return this.geneProductRef;
	}
}
