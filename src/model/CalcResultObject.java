package model;

import java.util.HashSet;

public class CalcResultObject {
	
	private String modelName;
	private boolean customModel;
	private String modelClassification;
	private HashSet<SBMLReaction> listOfReactions;
	private HashSet<SampleProtein> sampleProteinsHits;
	private HashSet<SampleProtein> sampleProteinsMisses;
	private NCBITax commonTaxPath;
	private int numReacModelTotal;
	private int numReacModelWithECs;
	private int numIdentifiedReactions;
	private float intersectionPercModelTotal;
	private float intersectionPercModelEC;
	private int numProtWithECSample;
	private int numProtSample;
	private int numHitsSample;
	private float intersectionPercSample;
	private int taxoScore;
	private String modelLink; 
	
	//temporary
	private HashSet<SBMLReaction> missedReactions;
	
	public CalcResultObject() {
		this.modelLink="";
		this.modelClassification="";
		this.listOfReactions = new HashSet<>();
		this.sampleProteinsHits = new HashSet<>();
		this.customModel = false;
		this.setSampleProteinsMisses(new HashSet<>());
	}
	
	public String getModelName() {
		return this.modelName;
	}
	public void setModelName(String name) {
		this.modelName=name;
	}
	public void setCustomModel(boolean input) {
		this.customModel = input;
	}
	
	public void setClassification(String cl) {
		this.modelClassification=cl;
	}
	public String getClassification() {
		return this.modelClassification;
	}
	
	public void setListOfReactions(HashSet<SBMLReaction> set) {
		this.listOfReactions = set;
	}
	public HashSet<SBMLReaction> getListOfReactions(){
		return this.listOfReactions;
	}
	
	public void setSampleProteinsHits(HashSet<SampleProtein> hits) {
		this.sampleProteinsHits=hits;
	}
	public HashSet<SampleProtein> getSampleProteinsHits(){
		return this.sampleProteinsHits;
	}
	
	public void setCommonTaxPath(NCBITax taxPath) {
		this.commonTaxPath=taxPath;
	}
	public NCBITax getCommonTaxPath() {
		return this.commonTaxPath;
	}
	
	public int getNumReacModelWithECs() {
		return this.numReacModelWithECs;
	}
	public void setNumReacModelWithECs(int num) {
		this.numReacModelWithECs=num;
	}
	
	public void setNumProtWithECSample(int num) {
		this.numProtWithECSample=num;
	}
	public int getNumProtWithECSample() {
		return this.numProtWithECSample;
	}
	
	public void setNumProtSample(int num) {
		this.numProtSample=num;
	}
	public int getNumProtSample() {
		return this.numProtSample;
	}
	
	public void setNumHitsSample(int num) {
		this.numHitsSample=num;
	}
	public int getNumHitsSample() {
		return this.numHitsSample;
	}
	
	public void setInterSectionPercSample(float num) {
		this.intersectionPercSample=num;
	}
	public float getInterSectionPercSample() {
		return this.intersectionPercSample;
	}
	
	public void setTaxoScore(int score) {
		this.taxoScore=score;
	}
	public int getTaxoScore() {
		return this.taxoScore;
	}
	
	public void setModelLink(String link) {
		this.modelLink=link;
	}
	public String getModelLink() {
		return this.modelLink;
	}

	public int getNumReacModelTotal() {
		return numReacModelTotal;
	}

	public void setNumReacModelTotal(int numReacModelTotal) {
		this.numReacModelTotal = numReacModelTotal;
	}

	public HashSet<SampleProtein> getSampleProteinsMisses() {
		return sampleProteinsMisses;
	}

	public void setSampleProteinsMisses(HashSet<SampleProtein> sampleProteinsMisses) {
		this.sampleProteinsMisses = sampleProteinsMisses;
	}

	public int getNumIdentifiedReactions() {
		return numIdentifiedReactions;
	}

	public void setNumIdentifiedReactions(int numIdentifiedReactions) {
		this.numIdentifiedReactions = numIdentifiedReactions;
	}

	public float getIntersectionPercModelTotal() {
		return intersectionPercModelTotal;
	}

	public void setIntersectionPercModelTotal(float intersectionPercModelTotal) {
		this.intersectionPercModelTotal = intersectionPercModelTotal;
	}

	public float getIntersectionPercModelEC() {
		return intersectionPercModelEC;
	}

	public void setIntersectionPercModelEC(float intersectionPercModelEC) {
		this.intersectionPercModelEC = intersectionPercModelEC;
	}

	public HashSet<SBMLReaction> getMissedReactions() {
		return missedReactions;
	}

	public void setMissedReactions(HashSet<SBMLReaction> missedReactions) {
		this.missedReactions = missedReactions;
	}

}
