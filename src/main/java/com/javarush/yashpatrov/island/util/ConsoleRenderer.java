package main.java.com.javarush.yashpatrov.island.util;

import main.java.com.javarush.yashpatrov.island.model.Creature;
import main.java.com.javarush.yashpatrov.island.model.Island;
import main.java.com.javarush.yashpatrov.island.model.Location;
import main.java.com.javarush.yashpatrov.island.model.enums.CreatureType;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ConsoleRenderer implements Renderer {
    private final Island island;

    public ConsoleRenderer(Island island) {
        this.island = island;
    }

    @Override
    public void render(Island island) {
        Location[][] locations = island.getLocations();
        synchronized (island) {
            int rowsCount = locations.length;
            int columnsCount = locations[0].length;
            System.out.println("_".repeat(50));
            for (int row = 0; row < rowsCount; row++) {
                for (int column = 0; column < columnsCount; column++) {
                    for (CreatureType type: CreatureType.values()) {
                        renderCreaturesOnLocation(locations[row][column], type);
                    }
                    System.out.print("|");
                }
                System.out.println();
            }
        }
    }

    private void renderCreaturesOnLocation(Location location, CreatureType creatureType) {
        ConcurrentLinkedQueue<Creature> creatures = location.getCreaturesByType(creatureType);
        if (creatures == null || creatures.isEmpty()) {
            System.out.print("     ");
        } else {
            System.out.print(creatures.peek().getIcon() + "-" + creatures.size() + " ");
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
