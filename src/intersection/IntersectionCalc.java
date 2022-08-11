package intersection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import model.SampleProtein;
import model.SBMLReaction;
import model.SBMLSpecies;
import model.SBMLSpeciesReference;
import model.CalcResultObject;
import model.MetabolicModelObject;
import model.SBMLCompartment;
import model.SBMLFluxObjective;
import model.SBMLGeneProduct;
import model.SBMLObjective;
import model.SBMLParameter;
/*
 * compares EC-numbers of given model and sampleProteins, returns a CalcResultObj with data about the hit reactions and proteins, the missed proteins 
 */
public class IntersectionCalc {

	public static CalcResultObject calcIntersection(MetabolicModelObject model, List<SampleProtein> sampleProteinsData) {
		
	    // create empty HashSets so no duplicates get added, create HashMaps of modelSpecies and modelCompartments to get to them easier
		// populate the HashSets with the found reactions, proteins, species and compartments, as well as the missed proteins
		// create a CalcResultObj and return that object with the accumulated data
		
		List<SBMLReaction> modelReactions = model.getListOfReactions();

		HashMap<String,SBMLReaction> identifiedReactions = new HashMap<>();
		HashSet<SBMLReaction> listOfReactions = new HashSet<>();
		HashSet<SampleProtein> sampleProteinsHits = new HashSet<>();
		HashSet<SampleProtein> sampleProteinsMisses = new HashSet<>();
		
		int numReactionsWithEC = 0;
		for(SBMLReaction modelReaction : modelReactions) {
			HashSet<String> modelECs = modelReaction.getECs();
			if(modelECs.size()>0) {
				numReactionsWithEC=numReactionsWithEC+1;
			}
		}
		
		int numProteinsWithEC = 0;
		for(SampleProtein sampleProtein : sampleProteinsData) {
			HashSet<String> sampleECs = sampleProtein.getECs();
			if(sampleECs.size()>0) {
				numProteinsWithEC = numProteinsWithEC+1;
			}
		}
		
		// compare ecs of each entry with entries in modelReactions
		for (SampleProtein sampleProtein : sampleProteinsData) {
			HashSet<String> sampleECs = sampleProtein.getECs();
			HashMap<String,String> taxoSample = sampleProtein.getTaxonomy();
			boolean proteinAssociatedToReaction = false;
			
			for(SBMLReaction modelReaction : modelReactions) {
				 HashSet<String> modelECs = modelReaction.getECs();
				 boolean reactionFound = false;
					 for(String modelEC : modelECs) {
						  if (sampleECs.contains(modelEC)) {
							   //add taxonomic data of sample to modelReaction, add modelReaction to sampleProteins
							   sampleProteinsHits.add(sampleProtein);
							   proteinAssociatedToReaction = true;
							   reactionFound = true;
							   break;
						   }
					 }
					 if(reactionFound == true) {
						 SBMLReaction foundReaction = new SBMLReaction(modelReaction.getId(),modelReaction.getName(),false,false);
						 foundReaction.setECs(modelECs);
						 foundReaction.setSboTerm(modelReaction.getSboTerm());
						 identifiedReactions.put(foundReaction.getId(),foundReaction);
					 }
			 }
			if(proteinAssociatedToReaction == false) {
				sampleProteinsMisses.add(sampleProtein);
			}	
		}
		
		for(String key : identifiedReactions.keySet()) {
			listOfReactions.add(identifiedReactions.get(key));
		}
		
		float numHitsModel=listOfReactions.size();
		float numModelReactions = modelReactions.size();
		float intersectionPercModelTotal = 0;
		float intersectionPercModelEC = 0;
		if(numHitsModel>0) {
		intersectionPercModelTotal = (numHitsModel/numModelReactions)*100;
		intersectionPercModelEC = (numHitsModel/numReactionsWithEC)*100;
		}
		
		float numSampleProteins = sampleProteinsData.size();
		int numProtSample = sampleProteinsData.size();
		float numHitsSample = sampleProteinsHits.size();
		float intersectionPercSample = 0;
		if(numHitsSample>0) {
		intersectionPercSample = (numHitsSample/numProteinsWithEC)*100;
		}

		CalcResultObject results = new CalcResultObject();
		results.setModelName(model.getId());
		results.setListOfReactions(listOfReactions);
		results.setSampleProteinsHits(sampleProteinsHits);
		results.setSampleProteinsMisses(sampleProteinsMisses);
		results.setNumReacModelTotal(modelReactions.size());
		results.setNumReacModelWithECs(numReactionsWithEC);
		results.setNumIdentifiedReactions(listOfReactions.size());
		results.setIntersectionPercModelTotal(intersectionPercModelTotal);
		results.setIntersectionPercModelEC(intersectionPercModelEC);
		results.setNumProtWithECSample(numProteinsWithEC);
		results.setNumProtSample(numProtSample);
		results.setNumHitsSample(sampleProteinsHits.size());
		results.setInterSectionPercSample(intersectionPercSample);
		return results;
	}
}