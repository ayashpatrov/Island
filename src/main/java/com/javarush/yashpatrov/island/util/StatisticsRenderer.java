package main.java.com.javarush.yashpatrov.island.util;

import main.java.com.javarush.yashpatrov.island.configuration.Settings;
import main.java.com.javarush.yashpatrov.island.model.Creature;
import main.java.com.javarush.yashpatrov.island.model.Island;
import main.java.com.javarush.yashpatrov.island.model.Location;
import main.java.com.javarush.yashpatrov.island.model.enums.CreatureType;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

public class StatisticsRenderer implements Renderer {
    private Island island;
    public StatisticsRenderer(Island island) {
        this.island = island;
    }
    @Override
    public void render(Island island) {
        Location[][] locations = island.getLocations();
        synchronized (island) {
            ConcurrentMap<CreatureType, ConcurrentLinkedQueue<Creature>> allAnimals = new ConcurrentHashMap<>();
            for (int row = 0; row < locations.length; row++) {
                for (int column = 0; column < locations[0].length; column++) {
                    var locationAnimals = locations[row][column].getAllCreatures();
                    for (CreatureType type : CreatureType.values()) {
                        if (locationAnimals.containsKey(type)) {
                            if (allAnimals.containsKey(type)) {
                                allAnimals.get(type).addAll(locationAnimals.get(type));
                            } else {
                                allAnimals.put(type, locationAnimals.get(type));
                            }
                        }
                    }
                }
            }

            for (CreatureType type : CreatureType.values()) {
                if (allAnimals.get(type) != null) {
                    // TODO вот этого движения не понял, если я вместо c.getIcon() делаю allAnimals.get(type).poll().getIcon(),
                    // то оно оказывается null, даже если я его перед этим проверил. Т.е. будто бы poll отдаёт на мгновенье
                    // то, что там сейчас есть, а как доходит дело до печати, то там его уже нет. Т.е., получается,
                    // я всегда печатаю вчерашний день.
                    Creature c = allAnimals.get(type).poll();
                    if (c != null) {
                        if (allAnimals.get(type).size() > 0) {
                            System.out.printf("%s-%s, ", c.getIcon(), allAnimals.get(type).size());
                        }
                    }
                }
            }
            System.out.println();
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
