package model;

import java.util.ArrayList;
import java.util.List;

public class SBMLObjective {

	private String id;
	private String type;
	private List<SBMLFluxObjective> listOfFluxObjectives;
	
	public SBMLObjective() {
		this.id="";
		this.type="";
		this.listOfFluxObjectives= new ArrayList<>();
	}
	
	public void setId(String id) {
		this.id = id;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setListOfFluxObjectives(List<SBMLFluxObjective> list) {
		this.listOfFluxObjectives = list;
	}
}
