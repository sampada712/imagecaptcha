package com.cts.ms.higheredu.imagecaptcha.http;

import java.io.StringReader;
import java.util.Properties;
import java.util.Enumeration;
import java.util.List;
import java.util.ArrayList;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class CaptchaImpl implements Captcha {

	public static final String HTTP_SERVER = "http://192.168.1.5:8080";
	private String privateKey;
	private String publicKey;
	private String captchaServer = HTTP_SERVER;
	private String VERIFY_URL = captchaServer + "/v";
	private String elmid = "picatcha";
	private HttpLoader httpLoader = new SimpleHttpLoader();

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public void setPicatchaServer(String picatchaServer) {
		this.captchaServer = picatchaServer;
	}

	public void setHttpLoader(HttpLoader httpLoader) {
		this.httpLoader = httpLoader;
	}

	public String validateCaptchaHtml(String ip, String ua, String token,
			String[] response) {

		List<String> list = new ArrayList<String>();
		for (String elm_response : response) {
			elm_response = "\"" + elm_response + "\""; // Making filenames into
														// strings with double
														// quotes
			list.add(elm_response);
		}

		JSONObject response_json = new JSONObject(); // Response object
		response_json.put("0", list);

		JSONObject fullParamJSON = new JSONObject(); // Full verify params in
														// JSON
		fullParamJSON.put("k", this.privateKey);
		fullParamJSON.put("t", token);
		fullParamJSON.put("r", response_json);
		fullParamJSON.put("ip", ip);
		fullParamJSON.put("ua", ua);

		String serverResponse = httpLoader.httpPost(VERIFY_URL,
				fullParamJSON.toString());

		// Parse the JSON response from Picatcha Servers
		String serverResponseString = serverResponse.toString();
		StringReader serverResponseReader = new StringReader(
				serverResponseString);

		try {
			JSONObject resultJson = (JSONObject) new JSONParser()
					.parse(serverResponseReader);
			String result = resultJson.get("s").toString();
			if (result != "true") {
				return resultJson.get("e").toString();
			} else {
				return result;
			}
		} catch (Exception e) {
			System.err.println("Error:" + e.getMessage());
			return "Error";
		}
	}

	public String createCaptchaHtml(Properties customizations) {
		String challenge;
		challenge = "<script type=\"text/javascript\" src=\""
				+ "/imagecaptcha/resources/js/captcha.js" + "\"></script>\r\n";
		challenge = challenge
				+ "<link href=\""
				+ "/imagecaptcha/resources/css/captcha.css\" rel=\"stylesheet\" type=\"text/css\"> \n";
		challenge = challenge + "<div id='" + this.elmid + "'></div>\n";
		challenge = challenge + customPicatchaOptions(customizations);
		challenge = challenge
				+ "<script type = \"text/javascript\">Picatcha.API_SERVER = '"
				+ this.captchaServer + "';</script>\n";
		challenge = challenge
				+ "<script type = \"text/javascript\">Picatcha.PUBLIC_KEY = '"
				+ this.publicKey
				+ "'; window.onload = function(){Picatcha.create('"
				+ this.elmid + "');}</script>\n";

		return challenge;
	}

	public String createCaptchaHtml(String format, String imageSize,
			String noiseLevel, String noiseType, String style, String link) {
		Properties p = new Properties();

		if (format != null) {
			p.setProperty("FORMAT", format);
		}
		if (imageSize != null) {
			p.setProperty("IMG_SIZE", imageSize);
		}
		if (noiseLevel != null) {
			p.setProperty("NOISE_LEVEL", noiseLevel);
		}
		if (noiseType != null) {
			p.setProperty("NOISE_TYPE", noiseType);
		}
		if (style != null) {
			p.setProperty("STYLE", style);
		}
		if (link != null) {
			p.setProperty("LINK", link);
		}

		return createCaptchaHtml(p);
	}

	private String customPicatchaOptions(Properties customizations) {
		if (customizations == null || customizations.size() == 0) {
			return "";
		}
		String options = "<script type = \"text/javascript\">";

		for (Enumeration e = customizations.keys(); e.hasMoreElements();) {
			String option_name = (String) e.nextElement();
			options = options + "Picatcha." + option_name + " = '"
					+ customizations.getProperty(option_name) + "'; ";

		}

		options = options + "</script>\n";

		return options;
	}
}
