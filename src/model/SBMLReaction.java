package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class SBMLReaction {
	private String id;
	private boolean fast;
	private boolean reversible;
	private String name;
	private String metaId;
	private String sboTerm;
	private String fbcUpperFluxBound;
	private String fbcLowerFluxBound;
	private List<String> identifiers;
	private HashSet<String> ecNumbers;
	private HashMap<String, String> taxonomy;
	private List<SBMLSpeciesReference> listOfReactants;
	private List<SBMLSpeciesReference> listOfProducts;
	private List<SBMLAssociation> geneProductAssociation;
	private List<String> referencedGeneIds;
	
	public SBMLReaction(String id, String name,boolean fast, boolean reversible) {
		this.id = id;
		this.name = name;
		this.fast = fast;
		this.setReversible(reversible);
		this.identifiers= new ArrayList<>();
		this.ecNumbers = new HashSet<>();
		this.taxonomy = new HashMap<>();
		this.listOfReactants = new ArrayList<>();
		this.listOfProducts = new ArrayList<>();
		this.geneProductAssociation = new ArrayList<>();
		this.referencedGeneIds = new ArrayList<>();
	}
	
	public HashMap<String, String> getTaxonomy() {
		return taxonomy;
	}

	public void addTaxonomy(String rank, String taxonomicName) {
		this.taxonomy.put(rank,taxonomicName);
	}
	
	public void setTaxonomy(HashMap<String,String> taxo) {
		this.taxonomy=taxo;
	}
	
	public void addEC(String ecNUmber) {
		this.ecNumbers.add(ecNUmber);
	}
		
	public String getName() {
		return name;
	}

	public void addIdentifier(String identifier) {
		this.identifiers.add(identifier);
	}

	public String getId() {
		return id;
	}

	public List<String> getIdentifiers() {
		return identifiers;
	}

	public HashSet<String> getECs() {
		return ecNumbers;
	}

	public void setIdentifiers(List<String> identifiers) {
		this.identifiers = identifiers;
	}
	public void setMetaId(String id) {
		this.metaId = id;
	}
	public void setSboTerm(String term) {
		this.sboTerm = term;
	}
	public void setFbcUpperFluxBound(String bound) {
		this.fbcUpperFluxBound = bound;
	}
	public String getFbcUpperFluxBound() {
		return this.fbcUpperFluxBound;
	}
	public void setFbcLowerFluxBound(String bound) {
		this.fbcLowerFluxBound = bound;
	}
	public String getFbcLowerFluxBound() {
		return this.fbcLowerFluxBound;
	}
	
	public void setListOfReactants(List<SBMLSpeciesReference> list) {
		this.listOfReactants = list;
	}
	public List<SBMLSpeciesReference> getListOfReactants(){
		return this.listOfReactants;
	}
	
	public void setListOfProducts(List<SBMLSpeciesReference> list) {
		this.listOfProducts = list;
	}
	public List<SBMLSpeciesReference> getListOfProducts(){
		return this.listOfProducts;
	}
	public void setGeneProductAssociation(List<SBMLAssociation> association) {
		this.geneProductAssociation = association;
	}
	public List<SBMLAssociation> getGeneProductAssociation() {
		return this.geneProductAssociation;
	}
	public void setReferencedGeneIdList(List<String> geneIds) {
		this.referencedGeneIds = geneIds;
	}
	public List<String> getReferencedGeneIdList(){
		return this.referencedGeneIds;
	}
	public void setECs(HashSet<String> ecs) {
		this.ecNumbers = ecs;
	}

	public String getSboTerm() {
		return this.sboTerm;
	}

	public boolean isReversible() {
		return reversible;
	}

	public void setReversible(boolean reversible) {
		this.reversible = reversible;
	}
}
