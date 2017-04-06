package com.duggan.workflow.test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

public class TestABC {

	public static void main(String[] args) throws IOException {
		FileReader reader = new FileReader(new File("/home/duggan/Downloads/Kenyan_Alliance_Procurement/outputs/Purchase Order Output Doc.json"));
		String html = IOUtils.toString(reader);
		
		System.out.println(html);
	}
}
