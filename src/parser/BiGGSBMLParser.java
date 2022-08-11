package parser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.swing.tree.TreeNode;
import javax.xml.stream.XMLStreamException;

import java.io.IOException;

import org.sbml.jsbml.Annotation;
import org.sbml.jsbml.Compartment;
import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.Reaction;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.SBase;
import org.sbml.jsbml.Species;
import org.sbml.jsbml.SpeciesReference;
import org.sbml.jsbml.UnitDefinition;
import org.sbml.jsbml.ext.fbc.And;
import org.sbml.jsbml.ext.fbc.Association;
import org.sbml.jsbml.ext.fbc.FBCConstants;
import org.sbml.jsbml.ext.fbc.FBCModelPlugin;
import org.sbml.jsbml.ext.fbc.FBCReactionPlugin;
import org.sbml.jsbml.ext.fbc.FBCSpeciesPlugin;
import org.sbml.jsbml.ext.fbc.FluxObjective;
import org.sbml.jsbml.ext.fbc.GeneProduct;
import org.sbml.jsbml.ext.fbc.GeneProductAssociation;
import org.sbml.jsbml.ext.fbc.GeneProductRef;
import org.sbml.jsbml.ext.fbc.LogicalOperator;
import org.sbml.jsbml.ext.fbc.Objective;
import org.sbml.jsbml.ext.fbc.Or;
import org.sbml.jsbml.ext.layout.Dimensions;
import org.sbml.jsbml.ext.layout.Layout;
import org.sbml.jsbml.ext.layout.LayoutConstants;
import org.sbml.jsbml.ext.layout.LayoutModelPlugin;
import org.sbml.jsbml.xml.parsers.FBCParser;
import org.sbml.jsbml.xml.parsers.L3LayoutParser;

import model.MetabolicModelObject;
import model.SBMLAnd;
import model.SBMLAssociation;
import model.SBMLCompartment;
import model.SBMLFluxObjective;
import model.SBMLGeneProduct;
import model.SBMLGeneProductReference;
import model.SBMLObjective;
import model.SBMLOr;
import model.SBMLParameter;
import model.SBMLReaction;
import model.SBMLSpecies;
import model.SBMLSpeciesReference;


/**
 * parser of SBML-File constructed on the basis of a BiGGModels SBML-File
 * parsing of layout-data added for compatibilty with the MPA-PathwayTool
 * @returns MetabolicModelObject
 * 
 */
public class BiGGSBMLParser {

	public static MetabolicModelObject parseSBMLFile(File file) {
		SBMLReader reader = new SBMLReader();
		
		MetabolicModelObject modelObject = new MetabolicModelObject();
		List<SBMLParameter> listOfParameters = new ArrayList<>();
		List<SBMLCompartment> listOfCompartments = new ArrayList<>();
		List<SBMLSpecies> listOfSpecies = new ArrayList<>();
		List<SBMLGeneProduct> listOfGeneProducts = new ArrayList<>();
		List<SBMLReaction> listOfReactions = new ArrayList<>();
		List<SBMLObjective> listOfObjectives = new ArrayList<>();
		
		try {
			SBMLDocument sbml= reader.readSBML(file.getPath());
			Model sbmlModel = sbml.getModel();
			//FBCModelPlugin fbcModel  = new FBCModelPlugin(sbmlModel);   doesn't work
			FBCModelPlugin fbcModel = (FBCModelPlugin) sbmlModel.getPlugin(FBCConstants.shortLabel);
			//modelObject.setFbcStrict(fbcModel.getStrict());
			modelObject.setId(sbmlModel.getId());
			
			ListOf<Parameter> parameters = sbmlModel.getListOfParameters();
			ListOf<Compartment> compartments = sbmlModel.getListOfCompartments();
			ListOf<Species> speciesList = sbmlModel.getListOfSpecies();
			ListOf<GeneProduct> geneProducts = fbcModel.getListOfGeneProducts();
			ListOf<Reaction> reactions = sbmlModel.getListOfReactions();
			ListOf<Objective> objectives = fbcModel.getListOfObjectives();
			
						
			// handle all the data for parameters, use the pre-defined getters of jsbml to fill custom SBMLParameter class with data
			for(Parameter parameter : parameters) {
				SBMLParameter sbmlParameter = new SBMLParameter(parameter.getId(), parameter.getConstant());
				sbmlParameter.setSboTerm(parameter.getSBOTermID());
				sbmlParameter.setValue(parameter.getValue());
				sbmlParameter.setUnits(parameter.getUnits());
				
				listOfParameters.add(sbmlParameter);
			}
			
			//handle fbcObjectives
			modelObject.setActiveObjective(fbcModel.getActiveObjective());
			for(Objective objective : objectives) {
				SBMLObjective sbmlObjective = new SBMLObjective();
				sbmlObjective.setId(objective.getId());
				sbmlObjective.setType(objective.getType().toString());
				
				List<SBMLFluxObjective> listOfFluxObjectives = new ArrayList<>();
				ListOf<FluxObjective> fluxObjectives = objective.getListOfFluxObjectives();
				for(FluxObjective fluxObjective : fluxObjectives) {
					SBMLFluxObjective sbmlFluxObjective = new SBMLFluxObjective();
					sbmlFluxObjective.setReaction(fluxObjective.getReaction());
					sbmlFluxObjective.setCoefficient(fluxObjective.getCoefficient());
					listOfFluxObjectives.add(sbmlFluxObjective);
				}
				sbmlObjective.setListOfFluxObjectives(listOfFluxObjectives);
				
				listOfObjectives.add(sbmlObjective);
			}
			
			// handle all the data for compartments
			for(Compartment compartment : compartments) {
				SBMLCompartment sbmlCompartment = new SBMLCompartment();
				sbmlCompartment.setId(compartment.getId());
				sbmlCompartment.setName(compartment.getName());
				sbmlCompartment.setConstant(compartment.getConstant());
				
				listOfCompartments.add(sbmlCompartment);
			}
			
			//handle all the data for species
			//FBCSpeciesPlugin has to get a Species object to construct, then get charge and formula through the plugin
			for(Species species : speciesList) {
				SBMLSpecies sbmlSpecies = new SBMLSpecies();
				FBCSpeciesPlugin fbcSpecies = (FBCSpeciesPlugin) species.getPlugin(FBCConstants.shortLabel);
				sbmlSpecies.setId(species.getId());
				sbmlSpecies.setConstant(species.getConstant());
				sbmlSpecies.setBoundaryCondition(species.getBoundaryCondition());
				sbmlSpecies.setHasOnlySubstanceUnits(species.getHasOnlySubstanceUnits());
				sbmlSpecies.setName(species.getName());
				sbmlSpecies.setMetaId(species.getMetaId());
				sbmlSpecies.setSboTerm(species.getSBOTermID());
				sbmlSpecies.setCompartment(species.getCompartment());
				try {
					sbmlSpecies.setFbcChemicalFormula(fbcSpecies.getChemicalFormula());    // try chemical formula first, as it seems to be there most of the time
					sbmlSpecies.setFbcCharge(fbcSpecies.getCharge());                      // afterwards try adding the charge, which is nonexistent in some models
				}catch(Exception e) {
					System.out.println(e.getMessage());
					
				}
				
				Annotation annotation = species.getAnnotation();
				List<String> identifiers = new ArrayList<>();
				getTreeNode(annotation, identifiers,"");
				sbmlSpecies.setIdentifiers(identifiers);
				
				listOfSpecies.add(sbmlSpecies);
			}
			
			//handle GeneProducts
			for(GeneProduct geneProduct : geneProducts) {
				SBMLGeneProduct sbmlGeneProduct = new SBMLGeneProduct();
				sbmlGeneProduct.setId(geneProduct.getId());
				sbmlGeneProduct.setLabel(geneProduct.getLabel());
				sbmlGeneProduct.setFbcName(geneProduct.getName());
				sbmlGeneProduct.setMetaId(geneProduct.getMetaId());
				sbmlGeneProduct.setSboTerm(geneProduct.getSBOTermID());
				//add all identifiers referenced within the annotation element
				Annotation annotation = geneProduct.getAnnotation();
				List<String> identifiers = new ArrayList<>();
				getTreeNode(annotation,identifiers,"");
				sbmlGeneProduct.setIdentifiers(identifiers);
				
				listOfGeneProducts.add(sbmlGeneProduct);
			}
			
			// handle all the data for reactions
			for(Reaction reaction : reactions) {
				FBCReactionPlugin fbcReaction = (FBCReactionPlugin) reaction.getPlugin(FBCConstants.shortLabel);
				String id= reaction.getId();
				String name= reaction.getName();
				//add attributes of reaction element
				SBMLReaction sbmlReaction = new SBMLReaction(id, name, false, reaction.getReversible());
				sbmlReaction.setMetaId(reaction.getMetaId());
				sbmlReaction.setSboTerm(reaction.getSBOTermID());
				sbmlReaction.setFbcLowerFluxBound(fbcReaction.getLowerFluxBound());
				sbmlReaction.setFbcUpperFluxBound(fbcReaction.getUpperFluxBound());
				//add reactant and product species referenced by this reaction
				ListOf<SpeciesReference> reactants = reaction.getListOfReactants();
				List<SBMLSpeciesReference> sbmlReactants = new ArrayList<>();
				ListOf<SpeciesReference> products = reaction.getListOfProducts();
				List<SBMLSpeciesReference> sbmlProducts = new ArrayList<>();
				for(SpeciesReference reactant : reactants) {
					SBMLSpeciesReference sbmlReactant = new SBMLSpeciesReference();
					sbmlReactant.setSpecies(reactant.getSpecies());
					Double stoichiometry = reactant.getStoichiometry();
					if(Double.isNaN(stoichiometry)) {
						
					}else {
					sbmlReactant.setStoichiometry(reactant.getStoichiometry());
					}
					sbmlReactant.setConstant(reactant.getConstant());
				    sbmlReactants.add(sbmlReactant);
				}
				for(SpeciesReference product : products) {
					SBMLSpeciesReference sbmlProduct = new SBMLSpeciesReference();
					sbmlProduct.setSpecies(product.getSpecies());
					if(product.getStoichiometry()!=Double.NaN) {
					sbmlProduct.setStoichiometry(product.getStoichiometry());
					}
					sbmlProduct.setConstant(product.getConstant());
				    sbmlProducts.add(sbmlProduct);
				}
				sbmlReaction.setListOfReactants(sbmlReactants);
				sbmlReaction.setListOfProducts(sbmlProducts);
				//add geneProductAssociations referenced by this reaction
				/*GeneProductAssociation gpa = new GeneProductAssociation();
				List<String> geneIdList = new ArrayList<>();
				try {
					gpa = fbcReaction.getGeneProductAssociation();
					Association association = gpa.getAssociation();
					if(association.toString().contains("and")) {
						And and = (And) association;
						SBMLAnd sbmlAND = new SBMLAnd();
						getAssociations(and,sbmlAND,geneIdList);
						List<SBMLAssociation> associationList = new ArrayList<>();
						associationList.add(sbmlAND);
						sbmlReaction.setGeneProductAssociation(associationList);
					}
					else if(association.toString().contains("or")) {
						Or or = (Or) association;
						SBMLOr sbmlOR = new SBMLOr();
						getAssociations(or,sbmlOR,geneIdList);
						List<SBMLAssociation> associationList = new ArrayList<>();
						associationList.add(sbmlOR);
						sbmlReaction.setGeneProductAssociation(associationList);
					}
					else if(association.toString().contains("geneProductRef")) {
						GeneProductRef gpr = (GeneProductRef) association;
						SBMLGeneProductReference sbmlGPR = new SBMLGeneProductReference();
						sbmlGPR.setGeneProduct(gpr.getGeneProduct());
						List<SBMLAssociation> associationList = new ArrayList<>();
						associationList.add(sbmlGPR);
						sbmlReaction.setGeneProductAssociation(associationList);
						geneIdList.add(gpr.getGeneProduct());
					}
					sbmlReaction.setReferencedGeneIdList(geneIdList);
				}catch(Exception e) {
					System.out.println("BiGGSBMLParser reaction GeneAssociation : " + e.getMessage());
				}*/
				//add all identifiers referenced within the annotation element of this reaction
				Annotation annotation = reaction.getAnnotation();
				List<String> identifiers = new ArrayList<>();
				getTreeNode(annotation,identifiers,"");
				sbmlReaction.setIdentifiers(identifiers);
				
				HashSet<String> ecNumbers = getECs(sbmlReaction.getIdentifiers());
				for (String ecNumber : ecNumbers) {
					sbmlReaction.addEC(ecNumber);
				}
				listOfReactions.add(sbmlReaction);
			}

			sbml=null;
			modelObject.setListOfParameters(listOfParameters);
			modelObject.setListOfCompartments(listOfCompartments);
			modelObject.setListOfSpecies(listOfSpecies);
			modelObject.setListOfGeneProducts(listOfGeneProducts);
			modelObject.setListOfReactions(listOfReactions);

			System.out.println("SBMLParser done : "+file.getName());
			return modelObject;
			
		}
		catch(IOException e){
			e.printStackTrace();
			System.out.println("BiGGSBMLParser : "+e.getMessage());
			return modelObject;
			
		} catch (XMLStreamException e) {
			System.out.println("BiGGSBMLParser : "+e.getMessage());
			e.printStackTrace();
			return modelObject;
		}
	}
	
	
	/**
	 * get annotation node as starting point, run through children until the elements have no children save those childless elements to List<String> 
	 * 
	 */
	private static void getTreeNode(TreeNode annotation, List<String> list, String condition ) {
		for (int annotationChildId=0; annotationChildId<annotation.getChildCount(); annotationChildId++) {
			TreeNode annotationChild = annotation.getChildAt(annotationChildId);
			if(annotationChild.getChildCount()>0) {
				getTreeNode(annotationChild,list,condition);
			} else {
				String childString = annotationChild.toString();
				if(childString.contains(condition)) {
					list.add(childString);
				}
			}
		}
	}
	
	
	/** 
	 * get List of identifiers, loop through until one contains the grouping "ec-code", then cut at / , use last element
	 * which is the desired EC, add to HashSet -> loop until no identifiers left
	 * --> adds all Strings containing "ec-code", adds possible multiple ECs
	 * */
	private static HashSet<String> getECs(List<String> identifiers){
		HashSet<String> ECs = new HashSet<>();
		for(String identifier : identifiers) {
			if(identifier.contains("ec-code")) {
				String[] idElements = identifier.split("\\/");
				String localEC = idElements[idElements.length - 1];
				ECs.add(localEC);
			}
		}
		return ECs;
	}
	
	/**
	 * recursive function, input is an jsbml association (AND, OR) and an sbmlAssociation(same but for sbml)
	 * no GeneProductReference allowed
	 * gets all associations under the logical operator, if there is another logical operator the function is called again on that operator
	 * recursively rebuilds the logical relations in the SBMLAssociation format
	 */
	private static void getAssociations(Association association, SBMLAssociation sbmlAssociation,List<String> geneIdList) {
		if(association.toString().contains("and")) {
			List<Association> associations = ((And) association).getListOfAssociations();
			for(Association assoc : associations) {
				if(assoc.toString().contains("geneProductRef")) {
					GeneProductRef gpr = (GeneProductRef) assoc;
					SBMLGeneProductReference sbmlGPR = new SBMLGeneProductReference();
					sbmlGPR.setGeneProduct(gpr.getGeneProduct());
					((SBMLAnd) sbmlAssociation).addAssociation(sbmlGPR);
					geneIdList.add(gpr.getGeneProduct());
				}
				else if(assoc.toString().contains("or")) {
					SBMLOr sbmlOR = new SBMLOr();
					getAssociations(assoc, sbmlOR, geneIdList);
					((SBMLOr) sbmlAssociation).addAssociation(sbmlOR);
				}
				else if(assoc.toString().contains("and")) {
					SBMLAnd sbmlAND = new SBMLAnd();
					getAssociations(assoc, sbmlAND, geneIdList);
					((SBMLAnd) sbmlAssociation).addAssociation(sbmlAND);
				}
				
			}
		}
		else if(association.toString().contains("or")) {
			List<Association> associations = ((Or) association).getListOfAssociations();
			for(Association assoc : associations) {
				if(assoc.toString().contains("geneProductRef")) {
					GeneProductRef gpr = (GeneProductRef) assoc;
					SBMLGeneProductReference sbmlGPR = new SBMLGeneProductReference();
					sbmlGPR.setGeneProduct(gpr.getGeneProduct());
					((SBMLOr) sbmlAssociation).addAssociation(sbmlGPR);
					geneIdList.add(gpr.getGeneProduct());
				}
				else if(assoc.toString().contains("or")) {
					SBMLOr sbmlOR = new SBMLOr();
					getAssociations(assoc, sbmlOR, geneIdList);
					((SBMLOr) sbmlAssociation).addAssociation(sbmlOR);
				}
				else if(assoc.toString().contains("and")) {
					SBMLAnd sbmlAND = new SBMLAnd();
					getAssociations(assoc, sbmlAND, geneIdList);
					((SBMLOr) sbmlAssociation).addAssociation(sbmlAND);
				}
			}
		}
	}
	
}
