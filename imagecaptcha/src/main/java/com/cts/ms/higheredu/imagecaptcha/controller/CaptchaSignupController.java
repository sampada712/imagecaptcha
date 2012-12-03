package com.cts.ms.higheredu.imagecaptcha.controller;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cts.ms.higheredu.imagecaptcha.dao.CaptchaUser;
import com.cts.ms.higheredu.imagecaptcha.util.CaptchaSignUpResponse;
import com.cts.ms.higheredu.imagecaptcha.util.CaptchaUtility;
import com.cts.ms.higheredu.imagecaptcha.util.RandomStr;

@Controller
public class CaptchaSignupController {

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/signup", method = { RequestMethod.POST,
			RequestMethod.GET })
	public String signUp(Locale locale, Model model, HttpServletRequest request) {

		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG,
				DateFormat.LONG, locale);

		String formattedDate = dateFormat.format(date);

		model.addAttribute("serverTime", formattedDate);
		String action = request.getParameter("action");
		if (action != null && action.equals("submit")) {
			CaptchaSignUpResponse response = new CaptchaSignUpResponse();
			Session session = null;
			String username = request.getParameter("username");
			String emailId = request.getParameter("email");
			String password = request.getParameter("password");
			String companyName = request.getParameter("website");
			response.setUsername(username);
			response.setEmailId(emailId);
			response.setCompanyName(companyName);
			// validate captcha response
			boolean isValid = CaptchaUtility.validateCaptchaResponse(locale,
					request);
			if (!isValid) {
				response.setCaptchaError(true);
			}
			if (username == null || username.isEmpty()
					|| username.equals("Example: Sampada")) {
				response.setUsernameError(true);
			}
			if (emailId == null || emailId.isEmpty()
					|| emailId.equals("Example: sampada@captcha.com")) {
				response.setEmailIdError(true);
			}
			if (companyName == null || companyName.isEmpty()
					|| companyName.equals("Example: www.captcha.com")) {
				response.setCompanyError(true);
			}
			model.addAttribute("response", response);
			if (response.getUsernameError() || response.getCaptchaError()
					|| response.getEmailIdError()) {
				return "signup";
			} else {
				try {
					SessionFactory sessionFactory = new Configuration()
							.configure().buildSessionFactory();
					session = sessionFactory.openSession();
					Transaction txn = session.beginTransaction();
					CaptchaUser user = new CaptchaUser();
					user.setUserName(username);
					user.setEmailId(emailId);
					user.setPassword(password);
					user.setCompanyName(companyName);
					// set private and public key
					RandomStr randomStr = new RandomStr();
					String privateKey = randomStr.get(25);
					String publickey = randomStr.get(25);
					user.setPrivateKey(privateKey);
					user.setPublicKey(publickey);
					session.save(user);
					txn.commit();
					CaptchaUtility.sendEmail(user.getEmailId(), username,
							privateKey, privateKey);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				} finally {
					// Actual contact insertion will happen at this step
					session.flush();
					session.close();
				}
				return "success";
			}
		} else {
			return "signup";
		}

	}
}
