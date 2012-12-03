package com.cts.ms.higheredu.imagecaptcha.http;


public class CaptchaFactory {

    public static Captcha newCaptcha(String publicKey) {
        CaptchaImpl captcha = new CaptchaImpl();
        captcha.setPublicKey(publicKey);
        captcha.setPicatchaServer(CaptchaImpl.HTTP_SERVER);

        return captcha;
    }
}
