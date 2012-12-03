package com.cts.ms.higheredu.imagecaptcha.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.cts.ms.higheredu.imagecaptcha.cache.CaptchaCache;
import com.cts.ms.higheredu.imagecaptcha.dao.CategoryImages;
import com.cts.ms.higheredu.imagecaptcha.dao.ImageCategory;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class CaptchaUtility {

	public static List<ImageCategory> loadCategories() {
		Session session = null;
		List<ImageCategory> objects = null;
		try {
			SessionFactory sessionFactory = new Configuration().configure()
					.buildSessionFactory();
			session = sessionFactory.openSession();
			Transaction txn = session.beginTransaction();
			Query query = session.createQuery("from "
					+ ImageCategory.class.getName());
			objects = query.list();
			txn.commit();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			session.flush();
			session.close();
		}
		return objects;
	}

	public static List<CategoryImages> loadCategoryImages() {
		Session session = null;
		List<CategoryImages> objects = null;
		try {
			SessionFactory sessionFactory = new Configuration().configure()
					.buildSessionFactory();
			session = sessionFactory.openSession();
			Transaction txn = session.beginTransaction();
			Query query = session.createQuery("from "
					+ CategoryImages.class.getName());
			objects = query.list();
			txn.commit();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			session.flush();
			session.close();
		}
		return objects;
	}

	public static void main(String[] args) {
		loadCategories();
		// loadCategoryImages();
		// createImage(75);
	}

	public static String createImage(int imgSize, CategoryImages image2) {
		try {
			File file = new File(image2.getImagePath());
			Image image = (Image) ImageIO.read(file);
			int thumbWidth = imgSize;
			int thumbHeight = imgSize;

			// Draw the scaled image
			BufferedImage thumbImage = new BufferedImage(thumbWidth,
					thumbHeight, BufferedImage.TYPE_INT_RGB);
			Graphics2D graphics2D = thumbImage.createGraphics();
			graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BILINEAR);
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
			return imageName;
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(image2.getImagePath());
			return "";
		}
	}

	/**
	 * Send a single email.
	 * 
	 * @param privateKey2
	 * @param privateKey
	 * @param username
	 */
	public static void sendEmail(String aToEmailAddr, String username,
			String privateKey, String publickey) {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		javax.mail.Session session = javax.mail.Session.getDefaultInstance(
				props, new javax.mail.Authenticator() {
					//TODO- put your gmail id and password
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication("username",
								"password");
					}
				});

		Message message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress("mail@captcha.com"));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(aToEmailAddr));
			message.setSubject("User Registration successful");
			String htmlContent = "<html><h3>Hi "
					+ username
					+ ",</h3>"
					+ "<p>Thanks for signing up with Captcha.</p> <p> Use Private key: "
					+ privateKey + " and Public Key: " + publickey
					+ " for accessing captcha API</p></html>";
			message.setContent(htmlContent, "text/html");
			System.out.println("Sendinmessageg email ....");
			Transport.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean validateCaptchaResponse(Locale locale,
			HttpServletRequest request) {
		Date date = new Date();
		String user_response[] = request
				.getParameterValues("picatcha[r][s0][]");
		String token = request.getParameter("picatcha[token]");
		// validate private key
		CaptchaResponse response = CaptchaCache.getInstance()
				.getTokenImagesMap().get(token);
		if (response == null || user_response == null) {
			return false;
		} else {
			long diff = date.getTime() - response.getTimestamp().getTime();
			if (diff > 36000) {
				return false;
			} else {
				List<String> images = response.getTargetImages();
				List<String> userResList = Arrays.asList(user_response);
				for (String imageName : images) {
					if (!userResList.contains(imageName)) {
						return false;
					}
				}
				return true;
			}
		}
	}
}
