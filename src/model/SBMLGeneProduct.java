package model;

import java.util.ArrayList;
import java.util.List;

public class SBMLGeneProduct {

	private String id;
	private String label;
	private String name;
	private String metaId;
	private String sboTerm;
	private List<String> identifiers;
	
	public SBMLGeneProduct() {
		this.identifiers = new ArrayList<>();
		this.id="";
		this.label="";
		this.metaId="";
		this.sboTerm="";
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}

	public void setFbcName(String name) {
		this.name = name;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	public void setMetaId(String id) {
		this.metaId = id;
	}
	public void setSboTerm(String term) {
		this.sboTerm = term;
	}
	public void setIdentifiers(List<String> identifiers) {
		this.identifiers = identifiers;
	}
}
