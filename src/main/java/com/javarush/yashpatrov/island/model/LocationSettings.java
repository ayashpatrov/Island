package main.java.com.javarush.yashpatrov.island.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import main.java.com.javarush.yashpatrov.island.model.enums.CreatureType;

import java.util.Map;

@Data
@NoArgsConstructor
public class LocationSettings {
    private Map<CreatureType, Integer> limits;
    private Map<CreatureType, Integer> initialCounts;
}
