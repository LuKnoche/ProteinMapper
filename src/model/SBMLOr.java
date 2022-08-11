package model;

import java.util.ArrayList;
import java.util.List;

public class SBMLOr implements SBMLAssociation{

	private List<SBMLAssociation> listOfAssociations;
	private String type;
	
	public SBMLOr() {
		this.listOfAssociations = new ArrayList<>();
		this.type = "fbc:or";                                   // needed for ease of acces on the frontend
	}
	
	public void addAssociation(SBMLAssociation association){
		this.listOfAssociations.add(association);
	}
	public List<SBMLAssociation> getListOfAssociations(){
		return this.listOfAssociations;
	}

}
