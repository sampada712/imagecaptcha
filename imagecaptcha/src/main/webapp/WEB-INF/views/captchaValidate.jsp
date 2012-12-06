<%@page import="com.cts.ms.higheredu.imagecaptcha.http.CaptchaImpl"%>

<html>
<body>
	<%
		CaptchaImpl pix_captcha_check = new CaptchaImpl();
		pix_captcha_check.setPrivateKey("test");
		String user_response[] = request
				.getParameterValues("picatcha[r][s0][]");
		String remoteip = request.getRemoteAddr();

		//This is one line, wordwrapped here for ease of reading
		String pix_captcha_result = pix_captcha_check.validatePicatchaHtml(
				remoteip, request.getHeader("user-agent"),
				request.getParameter("picatcha[token]"), user_response);

		if (pix_captcha_result == "true") {
			out.print("Correct! Proceed ...");
			//Give further access to your website!
		} else {
			out.print("Incorrect! Try again ... Failed because:"
					+ pix_captcha_result);
			//Request user to try solving again
		}
	%>
</body>
</html>