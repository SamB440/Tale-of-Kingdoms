package com.convallyria.taleofkingdoms.test;

import com.convallyria.taleofkingdoms.common.translation.Translations;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TranslationsTest {

    private static final Logger LOGGER = LoggerFactory.getLogger("Translations");

    @Test
    public void translationTest() {
        Reflections reflections = new Reflections("assets/taleofkingdoms/lang/", Scanners.Resources);
        Set<String> fileNames = reflections.getResources(Pattern.compile(".*\\.json"));
        fileNames.forEach(fileName -> {
            InputStream in = getClass().getClassLoader().getResourceAsStream(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            Gson gson = new Gson();
            assertDoesNotThrow(() -> {
                Map<String, String> translationMap = gson.fromJson(reader, new TypeToken<Map<String, String>>() {
                }.getType());
                for (Translations translation : Translations.values()) {
                    assertTrue(translationMap.containsKey(translation.getKey()), translation + "(" + translation.getKey() + ") not found in " + fileName + ".");
                }
            }, "Json was invalid");
        });
    }
}
