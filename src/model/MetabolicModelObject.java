package model;

import java.util.ArrayList;
import java.util.List;

public class MetabolicModelObject {

	private String id;
	private boolean fbcStrict;
	private String modelClassification;
	private List<SBMLObjective> listOfObjectives;
	private List<SBMLParameter> listOfParameters;
	private List<SBMLSpecies> listOfSpecies;
	private List<SBMLCompartment> listOfCompartments;
	private List<SBMLGeneProduct> listOfGeneProducts;
	private List<SBMLReaction> listOfReactions;
	private String weblinkToDb;
	private String activeObjective;
	private boolean customModel;
	
	public MetabolicModelObject() {
		this.listOfObjectives = new ArrayList<>();
		this.listOfParameters = new ArrayList<>();
		this.listOfSpecies = new ArrayList<>();
		this.listOfCompartments = new ArrayList<>();
		this.listOfGeneProducts = new ArrayList<>();
		this.listOfReactions = new ArrayList<>();
		this.fbcStrict = false;
		this.modelClassification = "";
		this.weblinkToDb = "";
		this.activeObjective = "";
		this.customModel = false;
	}
	
	public void setId(String id) {
		this.id=id;
	}
	public String getId() {
		return this.id;
	}

	public void setFbcStrict(boolean strict) {
		this.fbcStrict = strict;
	}
	public boolean getFbcStrict() {
		return this.fbcStrict;
	}
	
	public void setClassification(String name) {
		this.modelClassification=name;
	}
	public String getClassification() {
		return this.modelClassification;
	}
	
	public List<SBMLObjective> getListOfObjectives() {
		return listOfObjectives;
	}

	public void setListOfObjectives(List<SBMLObjective> listOfObjectives) {
		this.listOfObjectives = listOfObjectives;
	}

	public void setListOfParameters(List<SBMLParameter> list) {
		this.listOfParameters = list;
	}
	public List<SBMLParameter> getListOfParameters(){
		return this.listOfParameters;
	}
	
	public void setListOfSpecies(List<SBMLSpecies> list) {
		this.listOfSpecies = list;
	}
	public List<SBMLSpecies> getListOfSpecies(){
		return this.listOfSpecies;
	}
	
	public void setListOfCompartments(List<SBMLCompartment> list) {
		this.listOfCompartments = list;
	}
	public List<SBMLCompartment> getListOfCompartments(){
		return this.listOfCompartments;
	}
	
	public void setListOfReactions(List<SBMLReaction> list) {
		this.listOfReactions=list;
	}
	public void addReaction(SBMLReaction reaction) {
		this.listOfReactions.add(reaction);
	}
	public List<SBMLReaction> getListOfReactions(){
		return this.listOfReactions;
	}
	
	public void setWebLinkToDb(String link) {
		this.weblinkToDb=link;
	}
	public String getWebLinkToDb() {
		return this.weblinkToDb;
	}

	public List<SBMLGeneProduct> getListOfGeneProducts() {
		return listOfGeneProducts;
	}

	public void setListOfGeneProducts(List<SBMLGeneProduct> listOfGeneProducts) {
		this.listOfGeneProducts = listOfGeneProducts;
	}
	public void setActiveObjective(String activeObj) {
		this.activeObjective = activeObj;
	}
	public String getActiveObjective() {
		return this.activeObjective;
	}
	public void setCustomModel(boolean input) {
		this.customModel = input;
	}
	public boolean getCustomModel() {
		return this.customModel;
	}
	
}
