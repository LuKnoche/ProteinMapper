package main;

import static spark.Spark.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.Part;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import intersection.CompareTax;
import intersection.IntersectionCalc;
import model.SampleProtein;
import model.MetabolicModelObject;
import model.NCBITax;
import model.ResultObject;
import model.SBMLCompartment;
import model.SBMLParameter;
import model.SBMLReaction;
import model.SBMLSpecies;
import model.SBMLSpeciesReference;
import model.CalcResultObject;
import parser.BiGGSBMLParser;
import parser.CSVParser;
import service.DataService;
import spark.Request;

public class Server {
	
	//private static String reqModelID = "";
	//private static List<String> reqReactionIDs = new ArrayList<>();
	
	public static void main(String[] args) {
		// make sure a storage folder exists and clear any files that may be inside
		File storageDir = new File("storage");
		if(!storageDir.isDirectory()) storageDir.mkdir();
		deleteFiles(); 
		
		File resourceDir = new File("resources");
		if(!resourceDir.isDirectory()) storageDir.mkdir();
		
		
		//CompCreatorService to : load taxonomic data from storage
		DataService dataService = new DataService();
		dataService.parseTaxData();    //loads taxonomic data of BiGG models and MAG files, parses all models and saves them into HashMap, also saves "databaseNames" and the corresponding modelIDs
		dataService.parseModels();                         // -> doesn't have to parse every model for every ProteinList/SampleFile that gets uploaded, instead parse on server startup once and then load when necessary

		// endpoints
		port(8080);
		post("/upload",(req,res)->uploadFiles(req));
		get("/mapping",(req,res)->callCalc(req,dataService));
		post("/requestResultSBML",(req,res)->handleResultSBMLRequest(req,dataService));
		System.out.println("Server launched");
		
	}
	
	private static String handleResultSBMLRequest(Request req, DataService dataService) {
		req.attribute("org.eclipse.jetty.multipartConfig",new MultipartConfigElement("/temp"));
		MetabolicModelObject newModel = new MetabolicModelObject();
		MetabolicModelObject model = new MetabolicModelObject();
		
		try {
			Collection<Part> collection = req.raw().getParts();
			System.out.println(req.headers("modelId")+" "+req.headers("newModelId")+" "+req.headers("customModel"));
			String modelId = req.headers("modelId");
			String newModelId = req.headers("newModelId");
			boolean isCustomModel = false;
			if(req.headers("customModel").equals("true")) {
				isCustomModel = true;
			}else {
				isCustomModel = false;
			}
			
			List<String> reactionIds = new ArrayList<>();
			collection.forEach(part -> {
				try {
					InputStream inputStream = part.getInputStream();
					byte[] content = inputStream.readAllBytes();
					String partName = part.getName();
					if(partName.equals("reactionIds")) {
						String s = new String(content);
						String[] ids = s.split(";");
						for(String id : ids) {
							reactionIds.add(id);
						}
					}else if(partName.equals("customModelData")) {
						String s = new String(content);
						Files.write(Paths.get("storage").resolve(part.getName()+".xml"),content);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			
			if(isCustomModel) {
				System.out.println("Received customModelData");
				File file = new File("storage\\customModelData.xml");
				model = BiGGSBMLParser.parseSBMLFile(file);
				String fileName = file.getName().split("\\.")[0];
				model.setId(fileName);
				model.setCustomModel(true);
				deleteFiles();
				}
			else if(!isCustomModel) {
				System.out.println("Requesting BiGGModels Data");
				model = dataService.getModelById(modelId);
			}
			
			//get data from the corresponding model object in storage
			List<SBMLReaction> modelReactions = model.getListOfReactions();
			List<SBMLSpecies> modelSpecies = model.getListOfSpecies();
			List<SBMLCompartment> modelCompartments = model.getListOfCompartments();
			
			//setup empty maps to populate with model data -> easier access later
			HashMap<String,SBMLReaction> reactionMap = new HashMap<>();
			HashMap<String,SBMLSpecies> speciesMap = new HashMap<>();
			HashMap<String,SBMLCompartment> compartmentMap = new HashMap<>();
			for(SBMLReaction reaction : modelReactions) {
				reactionMap.put(reaction.getId(), reaction);
			}
			for(SBMLSpecies species : modelSpecies) {
				speciesMap.put(species.getId(), species);
			}
			for(SBMLCompartment compartment : modelCompartments) {
				compartmentMap.put(compartment.getId(), compartment);
			}
			
			//setup empty sets for data that was identified via intersectionCalc
			HashSet<SBMLReaction> reactions = new HashSet<>();
			HashSet<SBMLSpecies> speciesSet = new HashSet<>();
			HashSet<SBMLCompartment> compartments = new HashSet<>();
			
			HashSet<String> speciesIds = new HashSet<>();
			for(String id : reactionIds) {
				SBMLReaction reaction = reactionMap.get(id);
				reactions.add(reaction);
				List<SBMLSpeciesReference> speciesReferences = new ArrayList<>();
				 speciesReferences.addAll(reaction.getListOfReactants());
				 speciesReferences.addAll(reaction.getListOfProducts());
				 for(SBMLSpeciesReference speciesReference : speciesReferences) {
					 speciesIds.add(speciesReference.getSpecies());
				 }
			}
			for(String speciesId : speciesIds) {
				SBMLSpecies species = speciesMap.get(speciesId); 
				String compartmentId = species.getCompartment();
				SBMLCompartment compartment = compartmentMap.get(compartmentId);
				speciesSet.add(species);
				compartments.add(compartment);
			}
			
			//put data into Lists -> needs to be in lists as per MetabolicModelObject
			List<SBMLReaction> listOfReactions = new ArrayList<>();
			List<SBMLSpecies> listOfSpecies = new ArrayList<>();
			List<SBMLCompartment> listOfCompartments = new ArrayList<>();
			for(SBMLReaction reaction : reactions) {
				listOfReactions.add(reaction);
			}
			for(SBMLSpecies species : speciesSet) {
				listOfSpecies.add(species);
			}
			for(SBMLCompartment compartment : compartments) {
				listOfCompartments.add(compartment);
			}
			
			//put data into object, ship object back to client
			newModel.setCustomModel(true);
			newModel.setId(newModelId);
			newModel.setListOfReactions(listOfReactions);
			newModel.setListOfSpecies(listOfSpecies);
			newModel.setListOfCompartments(listOfCompartments);
			
			//TODO entfernen
			try {
				String body = "reactionID\treactionName\tecs\n";
				for(SBMLReaction reaction : listOfReactions) {
					String id = reaction.getId();
					String name = reaction.getName();
					HashSet<String> ecs = reaction.getECs();
					String ecString = "";
					for(String ec : ecs) {
						ecString+=ec+"|";
					}
					body+=id+"\t"+name+"\t"+ecString+"\n";
				}
				FileWriter writer = new FileWriter(new File("D:\\BA\\data\\ResultTables\\"+model.getId()+".csv"));
				writer.write(body);
				writer.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
			//
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ServletException e) {
			e.printStackTrace();
		}
		
		String newModelAsJson = "";
		try {
			//Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().create();
			Gson gson = new Gson();
			newModelAsJson = gson.toJson(newModel);
			byte[] bytes = newModelAsJson.getBytes();
			System.out.print("\t"+bytes.length*0.000001+" MB");
			System.out.print("\thandleIntersectionPost done");
		}catch(Exception e) {
			e.printStackTrace();
		}
		return newModelAsJson;
	}

	public static String uploadFiles(Request req) {
		//System.out.println(req.body());
		req.attribute("org.eclipse.jetty.multipartConfig",new MultipartConfigElement("/temp"));
		
		try {
			Collection<Part> collection = req.raw().getParts();
			collection.forEach(part -> {
				try {
					InputStream inputStream = part.getInputStream();
					byte[] content = inputStream.readAllBytes();
					Files.write(Paths.get("storage").resolve(part.getName()),content);
					System.out.println("Successfully uploaded file : "+part.getName());
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			System.out.println("File upload done!");
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ServletException e) {
			e.printStackTrace();
		}
		String answer = "Server : Files received!";
		Gson gson = new Gson();
		String answerAsJson = gson.toJson(answer);
		return answer;
	}
	
	public static String callCalc(Request req, DataService dataService){
		
		File[] fileList = new File("storage").listFiles();
		HashMap<String,List<SampleProtein>> fileProteinsMap = new HashMap<>();                       //stores the fileName(as defined in line 112) and the proteins within that file 
		HashMap<String,NCBITax> customTaxoMap = new HashMap<>();                                     //stores the fileName and the taxonomic data from the taxonomy.csv file coming from frontend
		List<MetabolicModelObject> modelList = new ArrayList<>();                                    //stores all models, BiGG and useruploaded for this run of callCalc
		List<ResultObject> overallResults = new ArrayList<>();
		
		if(req.headers("database").equals("BiGG") || req.headers("database").equals("both")) {
			modelList = dataService.getModelsByDb("BiGG");
		}
		
		for(File file : fileList) {
			if(file.toString().contains("taxonomy")) {
				try {
					BufferedReader reader = new BufferedReader(new FileReader(file));
					reader.readLine();   //skip first line with headers
					String line;
					while ((line=reader.readLine())!=null) {
						String[] splitLine = line.split("\t");
						/*for(String lin : splitLine) {
							System.out.print(lin+" ");
						}
						System.out.println("");*/
						//put in id, classification, SuperKingdom,Kingdom, Phylum, Class, Order, Family, Genus, Species
						NCBITax taxObj = new NCBITax();
						taxObj.setId(splitLine[0]);
						taxObj.setClassification(splitLine[1]);
						taxObj.setSuperKingdom(splitLine[2]);
						taxObj.setKingdom(splitLine[3]);
						taxObj.setPhylum(splitLine[4]);
						taxObj.setClass(splitLine[5]);
						taxObj.setOrder(splitLine[6]);
						taxObj.setFamily(splitLine[7]);
						taxObj.setGenus(splitLine[8]);
						taxObj.setSpecies(splitLine[9]);
						//System.out.println(splitLine[0]+"; "+splitLine[1]+"; "+splitLine[2]+"; "+splitLine[3]+"; "+splitLine[4]+"; "+splitLine[5]+"; "+splitLine[6]+"; "+splitLine[7]+"; "+splitLine[8]+"; "+splitLine[9]);
						customTaxoMap.put(taxObj.getId(),taxObj);
						//System.out.println("taxoKey "+splitLine[0]);
					}
					reader.close();
				}catch(IOException e) {
					System.out.println("callCalc Parser Taxonomy : "+e.getMessage());
				}
			}
		}
		
		
		for(File file : fileList) {   
			if(file.toString().endsWith(".csv") && file.toString().contains("taxonomy")==false){           // create new ResultObject, pass the filename in, use that later to gain access to the proteins in the map
				List<SampleProtein> sampleProteins= new ArrayList<>();                                     // parse files first, so that another upload can be handled quickly
				ResultObject sampleResults = new ResultObject();
				try {                                
				sampleProteins = CSVParser.parseCSV(file);
				String fileName = file.getName().split("\\.")[0]; 
				fileProteinsMap.put(fileName, sampleProteins);
				sampleResults.setSampleName(fileName);
				overallResults.add(sampleResults);
				}catch(Exception e) {
					System.out.println("CallCalc sampleProtein reading : "+e.getMessage());
				}
			}
			if(file.toString().endsWith(".xml") && !req.headers("database").equals("BiGG")) {
				try {
				MetabolicModelObject customModelObject = BiGGSBMLParser.parseSBMLFile(file);
				String fileName = file.getName().split("\\.")[0];
				NCBITax taxObject = customTaxoMap.get(fileName);
				customModelObject.setClassification(taxObject.getClassification());
				customModelObject.setId(fileName);
				customModelObject.setCustomModel(true);
				modelList.add(customModelObject);
				}catch(Exception e) {
					System.out.println("CallCalc customModel reading : "+e.getMessage());
				}
			}
		}
		deleteFiles();
		
		for(ResultObject sampleResults : overallResults) {                                                  // now, go through the parsed files, get the Proteins and check their ECs against every model
			List<CalcResultObject> testResults = new ArrayList<>();
			List<CalcResultObject> bestResults = new ArrayList<>();
			List<SampleProtein> sampleProteins = fileProteinsMap.get(sampleResults.getSampleName());
			
				List<Float> percIntersectionList = new ArrayList<>();                                      // scores and percentages for evaluating a "best fit"
				HashMap<CalcResultObject,Integer> taxoScoreMap = new HashMap<>();
				List<CalcResultObject> testResultsHighestTaxoScore = new ArrayList<>();
				
				//save all results of the current sampleFile in ResultObject
				int proteinsWithEC = 0;
				for(SampleProtein sampleProtein : sampleProteins) {
					HashSet<String> sampleECs = sampleProtein.getECs();
					if(sampleECs.size()>0) {
						proteinsWithEC = proteinsWithEC+1;
					}
				}
				sampleResults.setNumSampleProteins(sampleProteins.size());
				sampleResults.setNumProteinsWithEC(proteinsWithEC);
				
				if(dataService.getMagTaxMap().containsKey(sampleResults.getSampleName())) {
				sampleResults.setSampleTaxo(dataService.getMagTaxById(sampleResults.getSampleName()));
				}else {
				sampleResults.setSampleTaxo(customTaxoMap.get(sampleResults.getSampleName()));
				}
				
				 for(MetabolicModelObject model : modelList) {
					 String modelId = model.getId();
					 CalcResultObject modelResult = IntersectionCalc.calcIntersection(model, sampleProteins);
					 modelResult.setModelName(modelId);
					 modelResult.setCustomModel(model.getCustomModel());
					 modelResult.setClassification(model.getClassification());
					 modelResult.setModelLink(model.getWebLinkToDb());
					 // determine the parts of taxonomic pathway the sample and model have in common
					 NCBITax commonPathway = CompareTax.compareTax(dataService,sampleResults.getSampleName(),modelId,customTaxoMap);
					 modelResult.setCommonTaxPath(commonPathway);
					 modelResult.setTaxoScore(commonPathway.getTaxoScore());
					 testResults.add(modelResult);
					 //determine best fitting model -> put ModelResult and it's taxoScore into HashMap, use later to determine best fitting modelResult
					 taxoScoreMap.put(modelResult, modelResult.getCommonTaxPath().getTaxoScore());					
				 }
				 
				 //determine best fitting models -> view TaxoScores, higher is better, overrules higher %, write into ArrayList and % into extra List
				 // if HashMap contains taxoScore of i > break after adding everything into corresponding arrayLists, because only the highest score is important
				 for(int i=8;i>=0;i--) {
					 if(taxoScoreMap.containsValue(i)) {
						 for(CalcResultObject modelResult : taxoScoreMap.keySet()) {
							 if(taxoScoreMap.get(modelResult)==i) {
								 testResultsHighestTaxoScore.add(modelResult);
								 percIntersectionList.add(modelResult.getInterSectionPercSample());
							 }
						 }
						 break;
					 }
				 }
				 
				 //iterate through percentages, remember the highest, check which place the perc has in the arrayList, write the corresponding testResultHighestTaxoScore into arrayList bestResults
				 float p=0;
				 for(float perc : percIntersectionList) {
					 if(perc>=p) {
						 if(perc>0) {
							 p=perc;
						 }
					 }
				 }
				 int i=0;
				 if(p>0) {
				 for(float perc : percIntersectionList) {
					 if(perc==p) {
						 bestResults.add(testResultsHighestTaxoScore.get(i));
					 }
					 i++;
				 }
				 }
				 
				 
				 // add all the results to the sampleResults, then add that to overall results to be saved into json
				 sampleResults.setBestResults(bestResults);
				 sampleResults.setResults(testResults);
				 //dataService.writeDataToCSV(sampleResults); -> used to generate result files for bachelor's thesis
		}
		String resultsAsJson = "";
		try {
			Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().create();
			//Gson gson = new Gson();
			resultsAsJson = gson.toJson(overallResults);
			byte[] bytes = resultsAsJson.getBytes();
			System.out.println(bytes.length*0.000001+" MB");
			System.out.println("CallCalc done");
		}catch(Exception e) {
			e.printStackTrace();
		}
	return resultsAsJson;	
	}
	
	public static void deleteFiles() {
			File[] fileList = new File("storage").listFiles();
			//double filesSize=-15;
			for(File file : fileList) {
				//filesSize=filesSize+file.length();
				file.delete();
			}
			System.out.println("Storage cleared!");
	}
}
