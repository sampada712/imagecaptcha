<%@page import="com.cts.ms.higheredu.imagecaptcha.http.Captcha"%>
<%@page import="com.cts.ms.higheredu.imagecaptcha.http.CaptchaFactory"%>
<%@ page import="java.util.Properties" %>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale:1.0, minimum-scale:1.0, maximum-cale:1.0" />
</head>


      <body>
         <form action="validateChallenge" method="post">
        <%
        	Captcha pix_captcha = CaptchaFactory.newCaptcha("test");

                                        Properties picatcha_customization = new Properties();
                                        picatcha_customization.setProperty("FORMAT", "2");
                                        picatcha_customization.setProperty("IMG_SIZE", "75");
                                        picatcha_customization.setProperty("NOISE_LEVEL", "5");
                                        picatcha_customization.setProperty("NOISE_TYPE", "0");

                                       out.print(pix_captcha.createCaptchaHtml(picatcha_customization));
        %>

        <input type="submit" value="submit" />
         </form>
       </body>
     </html>