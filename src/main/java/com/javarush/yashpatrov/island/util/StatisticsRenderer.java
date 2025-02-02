package main.java.com.javarush.yashpatrov.island.util;

import main.java.com.javarush.yashpatrov.island.configuration.Settings;
import main.java.com.javarush.yashpatrov.island.model.Creature;
import main.java.com.javarush.yashpatrov.island.model.Island;
import main.java.com.javarush.yashpatrov.island.model.Location;
import main.java.com.javarush.yashpatrov.island.model.enums.CreatureType;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

public class StatisticsRenderer implements Renderer {
    private final Island island;

    public StatisticsRenderer(Island island) {
        this.island = island;
    }

    @Override
    public void render(Island island) {
        Location[][] locations = island.getLocations();
        synchronized (island) {
            Map<CreatureType, Integer> animalsCount = new HashMap<>();
            for (int row = 0; row < locations.length; row++) {
                for (int column = 0; column < locations[0].length; column++) {
                    ConcurrentMap<CreatureType, ConcurrentLinkedQueue<Creature>> locationAnimals = new ConcurrentHashMap<>();
                    locationAnimals = locations[row][column].getAllCreatures();
                    for (CreatureType type : CreatureType.values()) {
                        if (locationAnimals.containsKey(type)) {
                            if (animalsCount.containsKey(type)) {
                                animalsCount.put(type, animalsCount.get(type) + locationAnimals.get(type).size());
                            } else {
                                animalsCount.put(type, locationAnimals.get(type).size());
                            }
                        }
                    }
                }
            }

            for (CreatureType type : CreatureType.values()) {
                if (animalsCount.get(type) != null) {
                    Integer count = animalsCount.get(type) == null ? 0 : animalsCount.get(type);
                    if (count != 0) {
                        System.out.printf("%s-%s, ",
                                Settings.getInstance().getCommonSettings().getCreatureSettings().get(type).getIcon(),
                                count);
                    }
                }
            }
            System.out.println();
            animalsCount.clear();
        }
    }

    @Override
    public void run() {
        try {
            render(this.island);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
