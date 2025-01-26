package main.java.com.javarush.yashpatrov.island.service;

import main.java.com.javarush.yashpatrov.island.model.Island;
import main.java.com.javarush.yashpatrov.island.model.Location;
import main.java.com.javarush.yashpatrov.island.model.enums.CreatureType;
import main.java.com.javarush.yashpatrov.island.model.plants.Grass;

public class PlantManager implements Runnable {
    private Island island;

    public PlantManager(Island island) {
        this.island = island;
    }

    @Override
    public void run() {
        try {
            Location[][] locations = island.getLocations();
            synchronized (island) {
                int rowsCount = locations.length;
                int columnsCount = locations[0].length;
                for (int row = 0; row < rowsCount; row++) {
                    for (int column = 0; column < columnsCount; column++) {
                        locations[row][column].getCreaturesByType(CreatureType.GRASS).forEach(x -> ((Grass) x).grow());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
