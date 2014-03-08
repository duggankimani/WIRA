package com.duggan.workflow.server.servlets.upload;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.mortennobel.imagescaling.AdvancedResizeOp;
import com.mortennobel.imagescaling.ResampleOp;

public class ImageUtils {

	static int IMG_WIDTH = 32;
	static int IMG_HEIGHT = 32;
	static Logger log = Logger.getLogger(ImageUtils.class);


	public static void resizeImage(HttpServletResponse resp, byte[] bites) {
		try{
			BufferedImage bimage = ImageIO.read(new ByteArrayInputStream(bites));
			BufferedImage image = resizeImage(bimage, IMG_WIDTH, IMG_HEIGHT);			
			ImageIO.write(image, "png", resp.getOutputStream());

		}catch(Exception e){
			throw new RuntimeException(e);
		}
		
	}

	public static void resizeImage(HttpServletResponse resp, byte[] bites,
			int width, int height) {
		try{
			BufferedImage bimage = ImageIO.read(new ByteArrayInputStream(bites));
			BufferedImage image = resizeImage(bimage,width,height);			
			ImageIO.write(image, "png", resp.getOutputStream());

		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	private static BufferedImage resizeImage(BufferedImage image,int width, int height){
		log.debug("Resizing image: width="+width+", height="+height);
		ResampleOp  resampleOp = new ResampleOp (width, height);
		resampleOp.setUnsharpenMask(AdvancedResizeOp.UnsharpenMask.Normal);
		BufferedImage rescaledImage = resampleOp.filter(image, null);
		
		return rescaledImage;
	}

//	private static BufferedImage resizeImageWithHint(
//			BufferedImage originalImage, int type) {
//
//		BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT,
//				type);
//		Graphics2D g = resizedImage.createGraphics();
//		g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
//		g.dispose();
//		g.setComposite(AlphaComposite.Src);
//
//		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
//				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
//		g.setRenderingHint(RenderingHints.KEY_RENDERING,
//				RenderingHints.VALUE_RENDER_QUALITY);
//		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//				RenderingHints.VALUE_ANTIALIAS_ON);
//
//		return resizedImage;
//	}

}
