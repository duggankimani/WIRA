package com.duggan.workflow.test.util.csv;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class TestCSVReader {

	public static void main(String[] args) throws IOException {
		String path = "/home/duggan/Projects/Sample Files/Bill Sample.csv";
		
		Reader in = new FileReader(new File(path));
		
		Iterable<CSVRecord> records  = CSVFormat.TDF.parse(in);
	
		for(CSVRecord rec: records){
			int size = rec.size();
			for(int i=0; i<size; i++){
				System.err.print("#"+rec.get(i)+", ");
			}
			System.err.println();
		}
	}
}
