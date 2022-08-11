package intersection;

import java.util.HashMap;

import model.NCBITax;
import service.DataService;

public class CompareTax {
	
	public static NCBITax compareTax(DataService dataService, String magId, String modelId, HashMap<String,NCBITax> customTaxMap) {
		
		NCBITax magTax = new NCBITax();
		NCBITax modelTax = new NCBITax();
		
		HashMap<String,NCBITax> magMap = dataService.getMagTaxMap();
		if(magMap.containsKey(magId)) {
			magTax = dataService.getMagTaxById(magId);
		}else {
			magTax = customTaxMap.get(magId);
		}

		if(customTaxMap.containsKey(modelId)) {
			modelTax = customTaxMap.get(modelId);
		}else {
			modelTax = dataService.getBiggTaxById(modelId);
		}
		
		// get SuperK,Kingdom,Phylum, Class, Order, Family, Genus, Species; in that order
		String[] magPath = magTax.getTax();
		String[] modelPath = modelTax.getTax();
		String[] ranks = {"SuperKingdom","Kingdom","Phylum","Class","Order","Family","Genus","Species"};
		
		String[] path = {"no hit","no hit","no hit","no hit","no hit","no hit","no hit","no hit"};
		String lastHit="no hit";
		
		// determine if magPath and biggPath ranks match. If so, add current rank and name as lastHit and increment taxoScore by one -> helps determine best fitting model	
		int taxoScore = 8;
		for(int i = 7; i>=0; i--) {
			if(magPath[i].equals("UNKNOWN")==false && magPath[i].equals("")==false) {
				if(magPath[i].equals(modelPath[i])) {
					for(int j = 0; j<=i; j++) {
						path[j]=modelPath[j];
					}
					lastHit = ranks[i]+" "+magPath[i];
					break;
				}
				else {
					taxoScore--;
				}
			}
			else {
				taxoScore--;
			}
		}
		
		
		float taxoPerc = ((float)taxoScore/(float)8)*100; // max is 8

		NCBITax commonPathway = new NCBITax("commonPathway",lastHit,path[0],path[1],path[2],path[3],path[4],path[5],path[6],path[7]);
		commonPathway.setTaxoScore(taxoScore);
		commonPathway.setTaxoPerc(taxoPerc);
		
		return commonPathway;
	}

}
