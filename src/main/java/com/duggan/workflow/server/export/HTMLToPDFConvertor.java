package com.duggan.workflow.server.export;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.duggan.workflow.shared.model.Doc;
import com.itextpdf.text.DocumentException;

public class HTMLToPDFConvertor {

	
	/**
	 * 
	 * <p>
	 * @param doc
	 * @param html
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @throws FactoryConfigurationError
	 * @throws DocumentException
	 */
	public byte[] convert(Doc doc, String html) throws FileNotFoundException, IOException, SAXException, ParserConfigurationException, FactoryConfigurationError, DocumentException{
		html = html.replaceAll("&nbsp;", "&#160;");
		html = new DocumentHTMLMapper().map(doc, html);
		html = html.replaceAll("&", "&amp;");
		return convert(html);
	}

	/**
	 * 
	 * @param doc
	 * @param html
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws SAXException 
	 * @throws FactoryConfigurationError 
	 * @throws ParserConfigurationException 
	 * @throws DocumentException
	 */
	private byte[] convert(String html) throws FileNotFoundException,
			IOException, SAXException, ParserConfigurationException, FactoryConfigurationError, DocumentException{

		byte[] bites = null;

		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	    Document doc = builder.parse(new InputSource(new StringReader(html)));

	    
	    ITextRenderer renderer = new ITextRenderer();
	    renderer.setDocument(doc, null);

	    ByteArrayOutputStream os = new ByteArrayOutputStream();
	    renderer.layout();
	    renderer.createPDF(os);
	    os.close();

		bites = os.toByteArray();
		
		return bites;
	}

	public static void main(String[] args) throws Exception {
		StringWriter out = new StringWriter();
		FileInputStream is = new FileInputStream(
				"/home/duggan/Downloads/PO (1).html");
		IOUtils.copy(is, out);
		is.close();
		
		String html = out.toString().replaceAll("&nbsp;", "&#160;");
		out.close();
		System.err.println("Converting");
		byte[] bites = new HTMLToPDFConvertor().convert(html);
		
		FileOutputStream fout = new FileOutputStream("sample.pdf");
		System.err.println(">> Done Converting, now writing to pdf........");
		fout.write(bites);
		fout.close();
		System.err.println(">> Done........");
	}
}
