package service;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.opencsv.CSVWriter;

import model.CalcResultObject;
import model.MetabolicModelObject;
import model.NCBITax;
import model.ResultObject;
import model.SBMLReaction;
import model.SampleProtein;
import parser.BiGGSBMLParser;
import parser.CSVParser;

public class DataService {
	
	private HashMap<String,NCBITax> BiggTaxMap;
	private HashMap<String,String> BiggLinkMap;               // [id, link to website with further info about model]
	private HashMap<String,NCBITax> MagTaxMap;
	private HashMap<String,MetabolicModelObject> modelMap;   // [id, ModelObject with all Reactions and ReactionsWithEcs] 
	private HashMap<String,String[]> databaseEntries;         // [databaseName, Array with modelIDs in that database] -> for testing purposes, so I don't have to load all models all the time for a quick test
	
	public DataService() {
		this.BiggTaxMap=new HashMap<>();
		this.BiggLinkMap= new HashMap<>();
		this.MagTaxMap=new HashMap<>();
		this.modelMap = new HashMap<>();
		this.databaseEntries= new HashMap<>();
	}
	
	public HashMap<String,NCBITax> getBiggTaxMap(){
		return this.BiggTaxMap;
	}
	public HashMap<String,NCBITax> getMagTaxMap(){
		return this.MagTaxMap;
	}
	public NCBITax getMagTaxById(String id) {
		return this.MagTaxMap.get(id);
	}
	public NCBITax getBiggTaxById(String id) {
		return this.BiggTaxMap.get(id);
	}
	
	public void parseTaxData(){
		// first off, parse all Taxonomy-data into HashMaps
		File BiggTax= new File("resources\\other\\BiGGTax.csv");
		try {
			BufferedReader reader = new BufferedReader(new FileReader(BiggTax.getPath()));
			//skip first line with identifiers
			reader.readLine();
			
			String line;
			while((line=reader.readLine())!=null) {
				String[] splitLine = line.split("\t");
				//put in id, classification, SuperKingdom, Kingdom, Phylum, Class, Order, Family, Genus, Species
				NCBITax taxObj = new NCBITax(splitLine[0],splitLine[1],splitLine[2],splitLine[3],splitLine[4],splitLine[5],splitLine[6],splitLine[7],splitLine[8],splitLine[9]);
				//System.out.println(splitLine[0]+"; "+splitLine[1]+"; "+splitLine[2]+"; "+splitLine[3]+"; "+splitLine[4]+"; "+splitLine[5]+"; "+splitLine[6]+"; "+splitLine[7]+"; "+splitLine[8]+"; "+splitLine[9]);
				BiggTaxMap.put(taxObj.getId(),taxObj);
				BiggLinkMap.put(splitLine[0],splitLine[10]);
			}
			reader.close();
		}catch(IOException e) {
			System.out.println("DataService Parser BiGG : "+e.getMessage());
		}
	}
	
	public void parseModels() {
		// now, parse all the models and put them into HashMap   --> currently only models from the BiGG-Database work
				File[] modelFiles = new File("resources\\models\\BiGG").listFiles();
				for(File modelFile : modelFiles) {
					if(modelFile.toString().endsWith(".xml")) {
						MetabolicModelObject modelObject = BiGGSBMLParser.parseSBMLFile(modelFile);
						String modelId = modelObject.getId();
						modelObject.setWebLinkToDb(BiggLinkMap.get(modelId));
						modelObject.setClassification(BiggTaxMap.get(modelId).getClassification());
						this.modelMap.put(modelId, modelObject);
					}
				}
				
				// populate the databaseEntries HashMap with "databases" and corresponding models
				// load pre-written CSV -> DB-ID | List of models in that db
				File dbListFile = new File("resources\\other\\DBtoModels.csv");
				try {
					BufferedReader reader = new BufferedReader(new FileReader(dbListFile.getPath()));
					reader.readLine(); //skip first Line with column header
					String line;
					while((line=reader.readLine())!=null) {
						String[] lineColumns = line.split("\t");       // csv is tab-delimited
						String dbName = lineColumns[0];
						String[] modelIDs = lineColumns[1].split(","); // modelIDs are saved -> "id1,id2,id3,.."
						this.databaseEntries.put(dbName, modelIDs);
					}
				} catch(IOException e) {
					System.out.println("DataService Parser DBList : "+e.getMessage());
					
				}
	}
	
	public MetabolicModelObject getModelById(String modelId) {
		return this.modelMap.get(modelId);
	}
	
	public List<MetabolicModelObject> getModelsByDb(String dbName) {
		String[] modelIds = databaseEntries.get(dbName);             //get the model Ids for the given db
		List<MetabolicModelObject> modelList = new ArrayList<>();    //instanciate new ArrayList
		
		for(String modelId : modelIds) {
			modelList.add(modelMap.get(modelId));  //get the model corresponding to the given modelId and add the MetabolicModelObject to the List<>
		}
		return modelList;
	}
	
	//solely used to create csv files to be assessed for bachelor's thesis
	/*public void writeDataToCSV(ResultObject resultsObj) {
		File file = new File("D:\\BA\\data\\"+resultsObj.getSampleName()+"_ResultTable.csv");
		try {
			FileWriter output = new FileWriter(file);
			CSVWriter write = new CSVWriter(output);
			String[] header = {"Model_ID","# total Reactions","# Reactions (withECs)","# Reactions found total","% Reactions found total","# Reactions with EC found","% Reactions with EC found","#Proteins total","# Proteins in Sample(withEC)","# Proteins matched to Reaction","% Proteins matched to Reaction","SuperKingdom","Kingdom","Phylum","Class","Order","Family","Genus","Species"};
			write.writeNext(header);
			for (CalcResultObject result : resultsObj.getTestResults()) {
				String[] tax = result.getCommonTaxPath().getTax();
				String[] modelResults = {result.getModelName(),Integer.toString(result.getNumReacModelTotal()),Integer.toString(result.getNumReacModelWithECs()),Integer.toString(result.getListOfReactions().size()),Float.toString(result.getIntersectionPercModelTotal()),Float.toString(result.getIntersectionPercModelEC()),Integer.toString(result.getNumProtSample()),Integer.toString(result.getNumProtWithECSample()),Integer.toString(result.getNumHitsSample()),Float.toString(result.getInterSectionPercSample()),tax[0],tax[1],tax[2],tax[3],tax[4],tax[5],tax[6],tax[7]};
				write.writeNext(modelResults);
			}
			write.close();
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}*/
}
