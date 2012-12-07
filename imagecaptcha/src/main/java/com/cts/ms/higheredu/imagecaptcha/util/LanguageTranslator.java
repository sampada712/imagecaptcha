package com.cts.ms.higheredu.imagecaptcha.util;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

public class LanguageTranslator {

	public static String getTranslatedString(String text, Language source,
			Language destination) {
		Translate.setClientId("test");
		Translate
				.setClientSecret("secret");
		String translatedText;
		try {
			translatedText = Translate.execute(text, source, destination);
			return translatedText;
		} catch (Exception e) {
			return text;
		}
	}
}
