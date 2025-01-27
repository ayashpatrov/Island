package main.java.com.javarush.yashpatrov.island.configuration;

import lombok.Data;
import lombok.NoArgsConstructor;
import main.java.com.javarush.yashpatrov.island.model.enums.CreatureType;

import java.util.HashMap;

@Data
@NoArgsConstructor
public class CommonSettings {
    private IslandSettings islandSettings;
    private LifeCycleSettings lifeCycleSettings;
    private LocationSettings locationSettings;
    private HashMap<CreatureType, CreatureSettings> creatureSettings;
}
