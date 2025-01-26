package main.java.com.javarush.yashpatrov.island.service;

import main.java.com.javarush.yashpatrov.island.configuration.Settings;
import main.java.com.javarush.yashpatrov.island.model.Creature;
import main.java.com.javarush.yashpatrov.island.model.Island;
import main.java.com.javarush.yashpatrov.island.model.Location;
import main.java.com.javarush.yashpatrov.island.model.animals.Animal;
import main.java.com.javarush.yashpatrov.island.model.enums.CreatureType;

import java.util.concurrent.ConcurrentLinkedQueue;

public class LifeCycle implements Runnable {
    private static final int BEAT_COUNT = Settings.getInstance().getCommonSettings().getLifeCycleSettings().getBeatCount();
    private static final int BEAT_DURATIONS_MS = Settings.getInstance().getCommonSettings().getLifeCycleSettings().getBeatDurationMs();
    private final Island island;
    private final CreatureType creatureType;

    public LifeCycle(Island island, CreatureType creatureType) {
        this.island = island;
        this.creatureType = creatureType;
    }

    @Override
    public void run() {
        try {
            // Сколько тактов выполнится в потоке, сколько раз этот вид животных проживёт свой цикл
            for (int i = 0; i < BEAT_COUNT; i++) {
                Location[][] locations = island.getLocations();
                ConcurrentLinkedQueue<Creature> allAnimals = new ConcurrentLinkedQueue<>();
                // Все животные этого вида писяют по очереди, блокируя весь остров от всех остальных видов
                for (int row = 0; row < locations.length; row++) {
                    for (int column = 0; column < locations[0].length; column++) {
                        if (locations[row][column].getCreaturesByType(creatureType) != null) {
                            allAnimals.addAll(locations[row][column].getCreaturesByType(creatureType));
                        } else {
                            //System.out.printf("На локации нет %s\n", creatureType);
                        }
                    }
                }

                allAnimals.forEach(x -> ((Animal) x).executeLifeCycle());
                allAnimals.forEach(x -> ((Animal) x).setFucking(false));
                try {
                    Thread.sleep(BEAT_DURATIONS_MS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}