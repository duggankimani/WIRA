package com.duggan.convertors.pdf2html.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.jpedal.examples.html.PDFtoHTML5Converter;
import org.jpedal.exception.PdfException;
import org.jpedal.render.output.IDRViewerOptions;
import org.jpedal.render.output.html.HTMLConversionOptions;

public class PDF2HTML {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		// Defaults to text files
		
		String path = "/home/duggan/Projects/WIRA/src/main/config/undergraduate-application-form.pdf";
		byte[] pdfFile = IOUtils.toByteArray(new FileInputStream(path));
		
		 HTMLConversionOptions conversionOptions = new HTMLConversionOptions();//Set conversion options here
	     conversionOptions.setDisableComments(true);

	     IDRViewerOptions viewerOptions = new IDRViewerOptions();//Set viewer options here

	     File outputDir = new File("./undergraduate.html");

	     //Alternative constructor exists for converting from byte array
	     PDFtoHTML5Converter converter = new PDFtoHTML5Converter(pdfFile, outputDir, conversionOptions, viewerOptions);

	     try {
	         converter.convert();
	     } catch (PdfException e) {
	         e.printStackTrace();
	     }
	}
}
