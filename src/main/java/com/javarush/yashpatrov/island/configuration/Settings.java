package main.java.com.javarush.yashpatrov.island.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.java.com.javarush.yashpatrov.island.model.CommonSettings;
import main.java.com.javarush.yashpatrov.island.model.Location;
import main.java.com.javarush.yashpatrov.island.model.LocationSettings;

import java.io.File;
import java.nio.file.Files;

public class Settings {
    private static String SETTINGS_PATH;
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
        try {
            SETTINGS_PATH = Thread.currentThread().getContextClassLoader().getResource("main/resources/").getPath();
        } catch (NullPointerException e) {
            throw new RuntimeException("Не определён путь к ресурсам");
        }
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