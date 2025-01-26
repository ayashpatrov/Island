package main.java.com.javarush.yashpatrov.island.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.java.com.javarush.yashpatrov.island.model.*;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Settings {
    private static final String SETTINGS_PATH = Thread.currentThread().getContextClassLoader().getResource("main/resources/").getPath();
    private static final String MODEL_PATH = "main.java.com.javarush.yashpatrov.island.model";
    private static CommonSettings commonSettings;
    private static LocationSettings locationSettings;

    private static Settings settings;

    public static Settings getInstance() {
        if (settings == null) {
            return new Settings();
        } else {
            return settings;
        }
    }

    private Settings() {
        commonSettings = getClassSettings(CommonSettings.class, CommonSettings.class);
        locationSettings = getClassSettings(Location.class, LocationSettings.class);
    }

    public <T,K> K getClassSettings(Class<T> clazz, Class<K> settingsClazz) {
        K result = null;
        try {
            String jsonString = Files.readString(new File(SETTINGS_PATH +
                    clazz.getName().replace(MODEL_PATH, settingsClazz.getSimpleName()).replace(".", File.separator) +
                    ".json").toPath());
            ObjectMapper objectMapper = new ObjectMapper();
            result = objectMapper.readValue(jsonString, settingsClazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public CommonSettings getCommonSettings() {
        return commonSettings;
    }
    public LocationSettings getLocationSettings() {return locationSettings;}
}