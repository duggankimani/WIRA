package com.duggan.workflow.test.export.html2pdf;

import java.io.FileOutputStream;
import java.io.IOException;

import org.xhtmlrenderer.pdf.ITextRenderer;

import com.itextpdf.text.DocumentException;

public class TestITextRenderer {

	 public static void main(String[] args) throws DocumentException, IOException {
	        ITextRenderer renderer = new ITextRenderer();
	        String content="<html><head><style>\n" +
	          /*"div.header {\n" +
	          "display: block; text-align: center;\n" + 
	          "position: running(header);}\n" +*/
	          "div.footer {\n" +
	          "display: block; text-align: center;\n" + 
	          "position: running(footer);}\n" +
	          /*"div.content {page-break-after: always;}" +
	          "@page { @top-center { content: element(header) }}\n " +*/
	          "@page { @bottom-center { content: element(footer) }}\n" +
	          "</style></head>\n" +
	          "<body><div class='header'>Header</div><div class='footer'>Footer</div><div class='content'>Page1</div><div>Page2</div></body></html>";
	        renderer.setDocumentFromString(content);
	        renderer.layout();
	        renderer.createPDF(new FileOutputStream("test.pdf"));
	 }
}
