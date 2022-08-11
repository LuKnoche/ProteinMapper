package parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.SampleProtein;

public class CSVParser {
	
	// for now only looks for EC
	public static List<SampleProtein> parseCSV(File file){
		
		List<SampleProtein> proteinsData = new ArrayList<>();
		
		try {
			// open file reader wrapped in buffered reader, skip first line with descition of content
			BufferedReader reader = new BufferedReader(new FileReader(file.getPath()));
			reader.readLine();
			
			// split the current line by tabstops -> get entries separated into columns
			String line;
			while((line=reader.readLine())!=null) {
			String[] splitLine = line.split("\t");
				
			String description = splitLine[10];
			String id = splitLine[0];
			
			SampleProtein protein = new SampleProtein(id,description);
			String[] koNumbers = splitLine[1].split("\\|");
			for(String ko : koNumbers) {
				protein.addKoNumber(ko);
			}
			if(splitLine[2]!="") {
			String[] ecSplit = splitLine[2].split("\\|");
			for(String ec : ecSplit) {
				protein.addEC(ec);
			}
			}
			
			String[] taxRank = {"SuperKingdom","Phylum","Class","Order","Family","Genus","Species"};
			for(int i=0; i<7;i++) {
				protein.addTaxonomy(taxRank[i],splitLine[3+i]);
			}
			
			proteinsData.add(protein);
			}
			
			reader.close();
			System.out.println("CSVParser done : "+file.getName());
			return proteinsData;
		}
		catch(IOException e){
			System.out.println("CSVParser failed");
			e.printStackTrace();
			return proteinsData;
		}
	
	}
}
