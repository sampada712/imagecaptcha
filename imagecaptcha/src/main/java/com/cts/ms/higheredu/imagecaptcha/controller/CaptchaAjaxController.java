package com.cts.ms.higheredu.imagecaptcha.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cts.ms.higheredu.imagecaptcha.dao.CaptchaUser;
import com.cts.ms.higheredu.imagecaptcha.dao.CaptchaUserDao;
import com.cts.ms.higheredu.imagecaptcha.util.CaptchaUtility;
import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

@Controller
public class CaptchaAjaxController {
	private static final String USERNAME_PATTERN = "^[a-z0-9_-]{3,15}$";

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/check_username", method = { RequestMethod.POST,
			RequestMethod.GET })
	public @ResponseBody
	Map<String, String> checkUserName(HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		String userId = request.getParameter("username");
		if (userId != null && !userId.isEmpty()) {
			Pattern pattern = Pattern.compile(USERNAME_PATTERN);
			Matcher matcher = pattern.matcher(userId);
			boolean isvalid = matcher.matches();
			if (!isvalid || userId.equalsIgnoreCase("null")) {
				map.put("code", "invalid-username");
			} else {
				CaptchaUserDao dao = new CaptchaUserDao();
				List<CaptchaUser> list = dao.findByUserName(userId);
				if (list != null && !list.isEmpty()) {
					map.put("taken", Boolean.TRUE.toString());
				}
			}
		} else {
			map.put("code", "invalid-username");
		}
		return map;
	}

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/translate", method = { RequestMethod.POST,
			RequestMethod.GET })
	public @ResponseBody
	Map<String, Object> translate(Locale locale, Model model,
			HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String question = "Select ALL the images of";
		String lang = request.getParameter("lang");
		String cat = request.getParameter("str");

		// Set your Windows Azure Marketplace client info - See
		// http://msdn.microsoft.com/en-us/library/hh454950.aspx
		Translate.setClientId("429d091a-32bf-4349-951f-62ab61cbf577");
		Translate
				.setClientSecret("fncLn0BDMvMOIWeaTmI6x7dN81TzLnvYtnkon1MRxc4=");
		try {
			Map<String, Object> langMap = new HashMap<String, Object>();
			Language dest = Language.fromString(lang);
			if (!dest.equals(Language.ENGLISH)) {
				/*cat = Translate.execute(cat, Language.ENGLISH,
						Language.fromString(lang));
				question = Translate.execute(question, Language.ENGLISH,
						Language.fromString(lang));*/

			}
			langMap.put("category", cat);
			langMap.put("question", question);
			Object[] textArr = new Object[1];
			textArr[0] = langMap;
			map.put("translations", textArr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return map;
	}
}
