package model;

import java.util.HashMap;
import java.util.HashSet;

public class SampleProtein {
	private String id;
	private String description;
	private HashSet<String> ecNumbers;
	private HashSet<String> konumbers;
	private HashMap<String, String> taxonomy;
	
	public SampleProtein(String id,String description) {
		this.id = id;
		this.description=description;
		this.ecNumbers = new HashSet<>();
		this.konumbers = new HashSet<>();
		this.taxonomy = new HashMap<>();
	}
	
	public HashMap<String, String> getTaxonomy() {
		return taxonomy;
	}

	public void addTaxonomy(String rank, String taxonomicName) {
		this.taxonomy.put(rank,taxonomicName);
	}
	
	public void addEC(String ecNUmber) {
		this.ecNumbers.add(ecNUmber);
	}
	
	public void addKoNumber(String koNumber) {
		this.konumbers.add(koNumber);
	}
		
	public String getDescription() {
		return description;
	}

	public String getId() {
		return id;
	}

	public HashSet<String> getECs() {
		return ecNumbers;
	}

	public HashSet<String> getKoNumbers() {
		return konumbers;
	}
}
