package com.cts.ms.higheredu.imagecaptcha;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;

import com.cts.ms.higheredu.imagecaptcha.util.RandomStr;
import com.jhlabs.image.DiffuseFilter;
import com.jhlabs.image.ShadowFilter;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class TestImageShadowCreation {
	private float radius = 5;
	private float angle = (float)Math.PI*6/4;
	private float distance = 5;
	private float opacity = 0.5f;
	private boolean addMargins = false;
	private boolean shadowOnly = false;
	private int shadowColor = 0xff000000;
	
	public static void main(String[] args) {

		try {
			File file = new File("C://Images//ShadowFilter1.jpg");
			BufferedImage image = (BufferedImage) ImageIO.read(file);
			int thumbWidth = 75;
			int thumbHeight = 75;

			// BufferedImage thumbImage = Scalr.resize(image, 75);
			ShadowFilter shadowFilter = new ShadowFilter();
			// shadowFilter.setShadowOnly(truthumbImage;
			shadowFilter.setShadowColor(00000001);
			shadowFilter.setAngle(20);
			shadowFilter.setAddMargins(true);
			shadowFilter.setDistance(2);
			DiffuseFilter diffuseFilter = new DiffuseFilter();
			diffuseFilter.setScale(1f);
			BufferedImage thumbImage = Scalr.resize(image, Scalr.Method.SPEED,
					Scalr.Mode.FIT_EXACT, image.getWidth(), image.getHeight(),
					diffuseFilter);
			
		/*	// Draw the scaled image
			BufferedImage thumbImage = new BufferedImage(thumbWidth,
					thumbHeight, BufferedImage.TYPE_INT_RGB);
			Graphics2D graphics2D = thumbImage.createGraphics();
			graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			// createShadowMask(thumbImage);
			// getBlurOp(5).filter(createShadowMask(image), thumbImage);
			graphics2D.drawImage(image, 0, 0, thumbWidth, thumbHeight, null);

			// Write the scaled image to the outputstream
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			JPEGEncodeParam param = encoder
					.getDefaultJPEGEncodeParam(thumbImage);
			int quality = 100; // Use between 1 and 100, with 100 being highest
								// quality
			quality = Math.max(0, Math.min(quality, 100));
			param.setQuality((float) quality / 100.0f, false);
			encoder.setJPEGEncodeParam(param);
			encoder.encode(thumbImage);*/
			RandomStr str = new RandomStr();
			String imageName = str.get(12) + ".png";
			ImageIO.write(thumbImage, "jpg", new File("C:/Images/" + imageName));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	public static void main1(String[] args) {

		try {
			File file = new File(
					"C://Users//sampada712//Desktop//MS//Images//Triangle/tirangle6.jpg");
			BufferedImage image = (BufferedImage) ImageIO.read(file);
			int thumbWidth = 75;
			int thumbHeight = 75;

			// Draw the scaled image
			BufferedImage thumbImage = new BufferedImage(thumbWidth,
					thumbHeight, BufferedImage.TYPE_INT_RGB);
			Graphics2D graphics2D = thumbImage.createGraphics();
			graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			// createShadowMask(thumbImage);
			// getBlurOp(5).filter(createShadowMask(image), thumbImage);
			graphics2D.drawImage(image, 0, 0, thumbWidth, thumbHeight, null);

			// Write the scaled image to the outputstream
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			JPEGEncodeParam param = encoder
					.getDefaultJPEGEncodeParam(thumbImage);
			int quality = 100; // Use between 1 and 100, with 100 being highest
								// quality
			quality = Math.max(0, Math.min(quality, 100));
			param.setQuality((float) quality / 100.0f, false);
			encoder.setJPEGEncodeParam(param);
			encoder.encode(thumbImage);
			RandomStr str = new RandomStr();
			String imageName = str.get(12) + ".png";
			ImageIO.write(thumbImage, "jpg", new File("C:/Images/" + imageName));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static ConvolveOp getBlurOp(int size) {
		float[] data = new float[size * size];
		float value = 1 / (float) (size * size);
		for (int i = 0; i < data.length; i++) {
			data[i] = value;
		}
		return new ConvolveOp(new Kernel(size, size, data));
	}

	private static BufferedImage createShadowMask(BufferedImage image) {
		BufferedImage mask = new BufferedImage(image.getWidth(),
				image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Color shadowColor = new Color(0x000000);
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				int argb = image.getRGB(x, y);
				argb = (int) ((argb >> 24 & 0xFF) * 0.5f) << 24
						| shadowColor.getRGB() & 0x00FFFFFF;
				mask.setRGB(x, y, argb);
			}
		}
		return mask;
	}
}
