package com.cts.ms.higheredu.imagecaptcha.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cts.ms.higheredu.imagecaptcha.cache.CaptchaCache;
import com.cts.ms.higheredu.imagecaptcha.dao.CaptchaUser;
import com.cts.ms.higheredu.imagecaptcha.dao.CaptchaUserDao;
import com.cts.ms.higheredu.imagecaptcha.dao.CategoryImages;
import com.cts.ms.higheredu.imagecaptcha.dao.ImageCategory;
import com.cts.ms.higheredu.imagecaptcha.util.CaptchaResponse;
import com.cts.ms.higheredu.imagecaptcha.util.CaptchaUtility;
import com.cts.ms.higheredu.imagecaptcha.util.RandomStr;

@Controller
public class CaptchaController {

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String showChallenge(Model model) {
		return "captcha";
	}

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/customizations", method = { RequestMethod.POST,
			RequestMethod.GET })
	public String cutomizations(Locale locale, Model model,
			HttpServletRequest request) {
		if (request.getHeader("User-Agent").toLowerCase().contains("ipad")) {
			model.addAttribute("ipad", true);
		} else if (request.getHeader("User-Agent").toLowerCase()
				.contains("nexus")
				|| request.getHeader("User-Agent").toLowerCase()
						.contains("iphone")) {
			model.addAttribute("mobile", true);
		}
		return "customization";
	}

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/validateChallenge", method = {
			RequestMethod.POST, RequestMethod.GET })
	public String validateChallenge(Locale locale, Model model,
			HttpServletRequest request) {
		boolean isValid = CaptchaUtility.validateCaptchaResponse(locale,
				request, false);
		if (isValid) {
			model.addAttribute("result", true);
		}

		return "captchasuccess";
	}

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/captchaValidate", method = { RequestMethod.POST,
			RequestMethod.GET })
	public String captchaValidate(Locale locale, Model model,
			HttpServletRequest request) {
		return "captchaValidate";
	}

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/getChallenge", method = { RequestMethod.POST,
			RequestMethod.GET })
	public @ResponseBody
	Map<String, Object> getCaptchaChallenge(Locale locale, Model model,
			HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG,
				DateFormat.LONG, locale);
		String formattedDate = dateFormat.format(date);
		model.addAttribute("serverTime", formattedDate);
		// Private Key Validation

		int format = Integer.parseInt(request.getParameter("f"));
		int noOfImages = 8;
		if (format == 1) {
			noOfImages = 6;
		} else if (format == 2) {
			noOfImages = 8;
		} else if (format == 3) {
			noOfImages = 10;
		} else if (format == 4) {
			noOfImages = 12;
		}
		int noiseType = Integer.parseInt(request.getParameter("nt"));
		int imageSize = Integer.parseInt(request.getParameter("is"));

		// validate public Key
		String publickKey = request.getParameter("k");
		if (!publickKey.equals("test")) {
			CaptchaUserDao dao = new CaptchaUserDao();
			List<CaptchaUser> list = dao.findByPublicKey(publickKey);
			if (list == null || list.isEmpty()) {
				map.put("error", true);
				return map;
			}
		}

		ImageCategory targetCat = CaptchaCache.getInstance()
				.getTargetImageCategory();
		ImageCategory nonTargetCat = CaptchaCache.getInstance()
				.getNonTargetImageCat(targetCat.getCategoryId());
		Random rand = new Random();
		int noOfTargetImgs = rand.nextInt((noOfImages - 1) - 1) + 1;
		List<CategoryImages> targetImages = CaptchaCache.getInstance()
				.getImages(noOfTargetImgs, targetCat.getCategoryId());
		List<CategoryImages> nonTargetImages = CaptchaCache.getInstance()
				.getImages(noOfImages - noOfTargetImgs,
						nonTargetCat.getCategoryId());
		String[] imageArr = new String[noOfImages];
		List<String> captchaResImgs = new ArrayList<String>();
		int i = 0;
		for (CategoryImages image : targetImages) {
			String imageName = CaptchaUtility.createImage(imageSize, image,
					noiseType);
			imageArr[i] = imageName;
			captchaResImgs.add(imageName);
			i++;
		}
		for (CategoryImages image : nonTargetImages) {
			String imageName = CaptchaUtility.createImage(imageSize, image,
					noiseType);
			imageArr[i] = imageName;
			i++;
		}
		Collections.shuffle(Arrays.asList(imageArr));
		RandomStr randomStr = new RandomStr();
		String token = randomStr.get(6);
		CaptchaResponse captchaRes = new CaptchaResponse();
		captchaRes.setTargetImages(captchaResImgs);
		captchaRes.setTimestamp(new Date());
		CaptchaCache.getInstance().getTokenImagesMap().put(token, captchaRes);
		Map<String, Object> imgmap = new HashMap<String, Object>();
		imgmap.put("q", targetCat.getCategoryName());
		imgmap.put("i", imageArr);
		Object[] stagesArr = new Object[1];
		stagesArr[0] = imgmap;
		map.put("s", stagesArr);
		map.put("t", token);
		return map;
	}

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/media/i/{imageName}", method = RequestMethod.GET)
	public void getImage(@PathVariable String imageName,
			HttpServletRequest request, HttpServletResponse response) {

		File file = new File("C:/Images/" + imageName + ".png");
		try {
			InputStream is = new FileInputStream(file);
			byte data[] = new byte[is.available()];
			int length = 0;
			int totalLength = 0;
			int toBeReadLength = data.length;

			while ((length = is.read(data, totalLength, toBeReadLength)) > 0) {

				totalLength += length;
				toBeReadLength -= length;

				// sb.append(new String(data,
				// HttpResponseConstants.UTF8_ENCODING));
				if (totalLength >= data.length) {
					break;
				}
			}
			is.close();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			out.write(data);
			response.setContentType("image/png");
			response.setContentLength(out.size());
			response.getOutputStream().write(out.toByteArray());
			response.getOutputStream().flush();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
