package com.duggan.convertors.html2image.test;

import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JEditorPane;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.simple.Graphics2DRenderer;

public class HTML2Image {

	public static void main(String[] args) throws IOException {
		String path = "https://code.google.com/archive/p/java-html2image/";
		String xhtmlPath = "/home/duggan/Projects/WIRA/test.xhtml";
		try( 
			InputStream in = new URL(path).openStream();
	        FileOutputStream fos = new FileOutputStream("test.xhtml");) {
	        Tidy tidy = new Tidy();
	        tidy.setShowWarnings(true);
	        tidy.setXmlTags(true);
	        tidy.setInputEncoding("UTF-8");
	        tidy.setOutputEncoding("UTF-8");
	        tidy.setXHTML(true);
	        tidy.setMakeClean(true);
	        Document xmlDoc = tidy.parseDOM(in, null);
	        tidy.pprint(xmlDoc, fos);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		// swingRenderer(path);
		flyingSourcerRenderer(xhtmlPath);//(xhtmlPath);
	}

	private static void flyingSourcerRenderer(String xhtmlFilePath) throws IOException {
		BufferedImage buff = null;
		buff = Graphics2DRenderer.renderToImage(xhtmlFilePath, 1024, 800);
		ImageIO.write(buff, "png", new File("test_flyingsourcer.png"));
	}

	private static void swingRenderer(String path)
			throws FileNotFoundException, IOException {
		String html = IOUtils.toString(new FileInputStream(new File(path)));

		int width = 500, height = 300;
		// Create a `BufferedImage` and create the its `Graphics`
		BufferedImage image = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice().getDefaultConfiguration()
				.createCompatibleImage(1300, 800);
		Graphics graphics = image.createGraphics();
		// Create an `JEditorPane` and invoke `print(Graphics)`
		JEditorPane jep = new JEditorPane("text/html", html);
		jep.setSize(width, height);
		jep.print(graphics);
		// Output the `BufferedImage` via `ImageIO`
		try {
			ImageIO.write(image, "png", new File("purchaseorder_swing.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
