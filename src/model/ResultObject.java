package model;

import java.util.ArrayList;
import java.util.List;

public class ResultObject {
	
	private String sampleName;
	private int numSampleProteins;
	private int numProteinsWithEC;
	private NCBITax sampleTaxo;
	private List<CalcResultObject> testResults; 
	private List<CalcResultObject> bestResults;
	
	public ResultObject() {
		this.testResults=new ArrayList<>();
		this.bestResults=new ArrayList<>();
		this.numSampleProteins=0;
		this.numProteinsWithEC=0;
		this.sampleTaxo = new NCBITax();
	}
	
	public void setResults(List<CalcResultObject> results) {
		this.testResults=results;
	}
	public void setBestResults(List<CalcResultObject> results) {
		this.bestResults=results;
	}
	public void setSampleName(String name) {
		this.sampleName=name;
	}
	public void setNumSampleProteins(int num) {
		this.numSampleProteins=num;
	}
	public void setSampleTaxo(NCBITax tax) {
		this.sampleTaxo=tax;
	}
	public String getSampleName() {
		return this.sampleName;
	}
	public List<CalcResultObject> getTestResults(){
		return this.testResults;
	}
	public void setNumProteinsWithEC(int num) {
		this.numProteinsWithEC=num;
	}
}
