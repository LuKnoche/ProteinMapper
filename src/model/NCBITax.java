package model;

public class NCBITax {
	private String id;
	private String classification;
	private String SuperKingdom;
	private String Kingdom;
	private String Phylum;
	private String Class;
	private String Order;
	private String Family;
	private String Genus;
	private String Species;
	private int taxoScore;
	private float taxoPerc;
	
	public NCBITax(String id,String classification, String SuperK,String King, String Phylum, String Class, String Order, String Family, String Genus, String Species) {
		this.id=id;
		this.classification=classification;
		this.SuperKingdom=SuperK;
		this.Kingdom=King;
		this.Phylum=Phylum;
		this.Class=Class;
		this.Order=Order;
		this.Family=Family;
		this.Genus=Genus;
		this.Species=Species;
		this.taxoScore = 0;
		this.taxoPerc = 0;
	}
	public NCBITax() {
		this.id="";
		this.classification="";
		this.SuperKingdom="";
		this.Kingdom="";
		this.Phylum="";
		this.Class="";
		this.Order="";
		this.Family="";
		this.Genus="";
		this.Species="";
		this.taxoScore=0;
		this.taxoPerc=0;
	}
	
	public String[] getTax() {
		String[] tax = {this.SuperKingdom,this.Kingdom,this.Phylum,this.Class,this.Order,this.Family,this.Genus,this.Species};
		return tax;
	}
	public String getId() {
		return this.id;
	}
	public String getClassification() {
		return this.classification;
	}

	public void setId(String id) {
		this.id=id;
	}
	public void setClassification(String classif) {
		this.classification=classif;
	}
	public void setSuperKingdom(String superK) {
		this.SuperKingdom=superK;
	}
	public void setKingdom(String king) {
		this.Kingdom=king;
	}
	public void setPhylum(String phylum) {
		this.Phylum=phylum;
	}
	public void setClass(String Class) {
		this.Class=Class;
	}
	public void setOrder(String Order) {
		this.Order=Order;
	}
	public void setFamily(String Family) {
		this.Family=Family;
	}
	public void setGenus(String Genus) {
		this.Genus=Genus;
	}
	public void setSpecies(String Species) {
		this.Species=Species;
	}
	public void setTaxoScore(int score) {
		this.taxoScore=score;
	}
	public int getTaxoScore() {
		return this.taxoScore;
	}
	public void setTaxoPerc(float perc) {
		this.taxoPerc=perc;
	}
	public float getTaxoPerc() {
		return this.taxoPerc;
	}
}
