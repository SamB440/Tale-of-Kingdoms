package com.convallyria.taleofkingdoms.test;

import com.convallyria.taleofkingdoms.client.translation.Translations;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class TranslationsTest {

    private static final Logger LOGGER = LogManager.getLogger("Translations");

    @Test
    public void translationTest() {
        Reflections reflections = new Reflections("assets/taleofkingdoms/lang/", new ResourcesScanner());
        Set<String> fileNames = reflections.getResources(Pattern.compile(".*\\.json"));
        fileNames.forEach(fileName -> {
            InputStream in = getClass().getClassLoader().getResourceAsStream(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            Gson gson = new Gson();
            try {
                Map<String, Object> translationMap = gson.fromJson(reader, new TypeToken<Map<String, Object>>() {
                }.getType());
                for (Translations translation : Translations.values()) {
                    if (!translationMap.containsKey(translation.getKey())) {
                        Assert.fail(translation.toString() + "(" + translation.getKey() + ") not found in " + fileName + ".");
                    }
                }
            } catch (JsonSyntaxException | JsonIOException e) {
                Assert.fail("Json was invalid: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
