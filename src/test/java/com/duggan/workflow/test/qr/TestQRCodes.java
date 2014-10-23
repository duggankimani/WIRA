package com.duggan.workflow.test.qr;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import net.glxn.qrgen.javase.QRCode;

import org.junit.Test;

public class TestQRCodes {

	@Test
	public void generateCode() throws FileNotFoundException{
		String content = "";
		
		String url = "";
		
		QRCode.from("KEMSA Ltd\n"
				+ "PRF-2002\n"
				+ "Case121\n"
				+ "url: www.google.com").writeTo(new FileOutputStream(new File("qrtest.png")));
		
		//V Card
//		VCard johnDoe = new VCard("John Doe")
//        .setEmail("john.doe@example.org")
//        .setAddress("John Doe Street 1, 5678 Doestown")
//        .setTitle("Mister")
//        .setCompany("John Doe Inc.")
//        .setPhonenumber("1234")
//        .setWebsite("www.example.org");
//		QRCode.from(johnDoe).writeTo(new FileOutputStream(new File("qrtest.png")));
	}
}
