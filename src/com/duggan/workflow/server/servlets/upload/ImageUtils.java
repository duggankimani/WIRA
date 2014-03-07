package com.duggan.workflow.server.servlets.upload;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import com.mortennobel.imagescaling.AdvancedResizeOp;
import com.mortennobel.imagescaling.ResampleOp;

public class ImageUtils {

	static int IMG_WIDTH = 32;
	static int IMG_HEIGHT = 32;


	public static void resizeImage(HttpServletResponse resp, byte[] bites) {
		try{
			BufferedImage bimage = ImageIO.read(new ByteArrayInputStream(bites));
			int type = bimage.getType() == 0? BufferedImage.TYPE_INT_ARGB : bimage.getType();
			
			BufferedImage image = resizeImage(bimage);
			
			ImageIO.write(image, "png", resp.getOutputStream());

		}catch(Exception e){
			throw new RuntimeException(e);
		}
		
	}
	
//	private static BufferedImage resizeImage(BufferedImage originalImage,
//			int type) {
//		BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT,
//				type);
//		Graphics2D g = resizedImage.createGraphics();
//		g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
//		g.dispose();
//
//		return resizedImage;
//	}
	
	private static BufferedImage resizeImage(BufferedImage image){
		ResampleOp  resampleOp = new ResampleOp (IMG_WIDTH,IMG_HEIGHT);
		resampleOp.setUnsharpenMask(AdvancedResizeOp.UnsharpenMask.Normal);
		BufferedImage rescaledImage = resampleOp.filter(image, null);
		
		return rescaledImage;
	}

	private static BufferedImage resizeImageWithHint(
			BufferedImage originalImage, int type) {

		BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT,
				type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
		g.dispose();
		g.setComposite(AlphaComposite.Src);

		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		return resizedImage;
	}

}
