package com.cts.ms.higheredu.imagecaptcha.util;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

public class LanguageTranslator {

	public static String getTranslatedString(String text, Language source,
			Language destination) {
		Translate.setClientId("429d091a-32bf-4349-951f-62ab61cbf577");
		Translate
				.setClientSecret("fncLn0BDMvMOIWeaTmI6x7dN81TzLnvYtnkon1MRxc4=");
		String translatedText;
		try {
			translatedText = Translate.execute(text, source, destination);
			return translatedText;
		} catch (Exception e) {
			return text;
		}
	}
}
