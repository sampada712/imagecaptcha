package com.cts.ms.higheredu.imagecaptcha.util;

import java.awt.image.BufferedImage;
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
import org.imgscalr.Scalr;

import com.cts.ms.higheredu.imagecaptcha.cache.CaptchaCache;
import com.cts.ms.higheredu.imagecaptcha.dao.CaptchaUser;
import com.cts.ms.higheredu.imagecaptcha.dao.CaptchaUserDao;
import com.cts.ms.higheredu.imagecaptcha.dao.CategoryImages;
import com.cts.ms.higheredu.imagecaptcha.dao.ImageCategory;
import com.jhlabs.image.DiffuseFilter;

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

	public static String createImage(int imgSize, CategoryImages image2,
			int noiseType) {
		try {
			File file = new File(image2.getImagePath());
			BufferedImage image = (BufferedImage) ImageIO.read(file);
			BufferedImage thumbImage;
			if (noiseType == 2) {
				DiffuseFilter diffuseFilter = new DiffuseFilter();
				diffuseFilter.setScale(1f);
				thumbImage = Scalr.resize(image, Scalr.Method.SPEED,
						Scalr.Mode.FIT_EXACT, imgSize, imgSize,
						Scalr.OP_ANTIALIAS, diffuseFilter);
			} else {
				thumbImage = Scalr.resize(image, Scalr.Method.SPEED,
						Scalr.Mode.FIT_EXACT, imgSize, imgSize,
						Scalr.OP_ANTIALIAS);
			}
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
						return new PasswordAuthentication("sampada712",
								"1F$F1Nid#7");
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
					+ "<p>Thanks for signing up with Captcha.</p> <p> Use Private key: <b>"
					+ privateKey + " </b>and Public Key: <b>" + publickey
					+ " </b>for accessing captcha API.</p></html>";
			message.setContent(htmlContent, "text/html");
			System.out.println("Sendinmessageg email ....");
			Transport.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean validateCaptchaResponse(Locale locale,
			HttpServletRequest request, boolean keyCheck) {
		Date date = new Date();
		String user_response[] = request
				.getParameterValues("picatcha[r][s0][]");
		String token = request.getParameter("picatcha[token]");
		// validate private key
		if (keyCheck) {
			String publickKey = request.getParameter("k");
			CaptchaUserDao dao = new CaptchaUserDao();
			List<CaptchaUser> list = dao.findByPublicKey(publickKey);
			if (list == null || list.isEmpty()) {
				return false;
			}
		}
		CaptchaResponse response = CaptchaCache.getInstance()
				.getTokenImagesMap().get(token);
		if (response == null || user_response == null) {
			return false;
		} else {
			long diffInMins = Math.abs(date.getTime()
					- response.getTimestamp().getTime()) / 60000;
			if (diffInMins > 10) {
				return false;
			} else {
				List<String> images = response.getTargetImages();
				List<String> userResList = Arrays.asList(user_response);
				if (userResList.size() != images.size()) {
					return false;
				}
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
