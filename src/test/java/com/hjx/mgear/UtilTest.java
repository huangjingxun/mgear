package com.hjx.mgear;

import org.junit.Test;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

/**
 * @author Jingxun Huang
 * @since 2017年4月11日
 */
public class UtilTest {

    @Test
    public void test() {

        String apiKey = "AIzaSyAVrHczE5DS4bs1-Vf9ml2BYYr8VM_6lQ8";
        System.setProperty("GOOGLE_API_KEY", apiKey);
        // Instantiates a client
        Translate translate = TranslateOptions.getDefaultInstance().getService();
        // The text to translate
        String text = "Hello, world!";

        // Translates some text
        Translation translation = translate.translate(text,
                                                      TranslateOption.sourceLanguage("en"),
                                                      TranslateOption.targetLanguage("zh-CN"));

        System.out.printf("Text: %s%n", text);
        System.out.printf("Translation: %s%n", translation.getTranslatedText());
    }
}
