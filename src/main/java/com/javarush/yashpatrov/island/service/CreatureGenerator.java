package main.java.com.javarush.yashpatrov.island.service;

import main.java.com.javarush.yashpatrov.island.model.Island;
import main.java.com.javarush.yashpatrov.island.model.Location;
import main.java.com.javarush.yashpatrov.island.model.enums.CreatureType;
import main.java.com.javarush.yashpatrov.island.util.CreatureFactory;

import java.util.Date;
import java.util.Map;
import java.util.Random;

public class CreatureGenerator implements Runnable {
    private final Island island;
    private final CreatureType creatureType;
    public CreatureGenerator(Island island, CreatureType type) {
        this.island = island;
        this.creatureType = type;
    }
    @Override
    public void run() {
        try {
            System.out.printf("Начата генерация созданий %s\n", creatureType.name());
            Date startTime = new Date();
            generateCreatures(island, creatureType);
            Date endTime = new Date();
            System.out.printf("Генерацияя созданий %s заняла %s ms\n", creatureType.name(), (endTime.getTime() - startTime.getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateCreaturesOld(Island island, CreatureType creatureType)  {
        Map<CreatureType, Integer> initialCounts = Location.getSettings().getInitialCounts();
        int initialCount;
        if (initialCounts != null && !initialCounts.isEmpty()) {
            CreatureFactory creatureFactory = new CreatureFactory();
            initialCount = Location.getSettings().getInitialCounts().get(creatureType) != null ?
                    Location.getSettings().getInitialCounts().get(creatureType) : 0;
            Location[][] locations = island.getLocations();
            Random rnd = new Random();
            int rowsCount = locations.length;
            int columnsCount = locations[0].length;
            for (int i = 0; i < initialCount; i++) {
                Location currentLocation = locations[rnd.nextInt(0, rowsCount)][rnd.nextInt(0, columnsCount)];
                currentLocation.addCreature(creatureFactory.create(creatureType, currentLocation));
            }
        }
    }

    private void generateCreatures(Island island, CreatureType creatureType) {
        Map<CreatureType, Integer> initialCounts = Location.getSettings().getInitialCounts();
        Map<CreatureType, Integer> limits = Location.getSettings().getLimits();
        CreatureFactory creatureFactory = new CreatureFactory();
        Location[][] locations = island.getLocations();
        for (int row = 0; row < locations.length; row++) {
            for (int column = 0; column < locations[0].length; column++) {
                for (int i = 0; i < initialCounts.get(creatureType) * limits.get(creatureType) / 100; i++) {
                    locations[row][column].addCreature(creatureFactory.create(creatureType, locations[row][column]));
                }
            }
        }
    }
}
