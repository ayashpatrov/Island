package main.java.com.javarush.yashpatrov.island.configuration;

import lombok.Data;
import lombok.NoArgsConstructor;
import main.java.com.javarush.yashpatrov.island.model.enums.CreatureType;

import java.util.HashMap;

@Data
@NoArgsConstructor
public class CreatureSettings {
    private String icon;
    private double weight;
    private int speed;
    private int litterCount;
    private double fullSatiety;
    private HashMap<CreatureType, Integer> eatingChance;
}
