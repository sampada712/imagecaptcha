package com.cts.ms.higheredu.imagecaptcha;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

public class TestLanguageTranslator {
	public static void main1(String[] args) {
		// Set your Windows Azure Marketplace client info - See
		// http://msdn.microsoft.com/en-us/library/hh454950.aspx
		Translate.setClientId("clientId");
		Translate
				.setClientSecret("Secret");

		String translatedText;
		try {
			translatedText = Translate.execute("Bonjour le monde",
					Language.FRENCH, Language.ENGLISH);
			System.out.println(translatedText);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		Language lang = Language.fromString("hi");
		System.out.println(lang.name());
	}

}
