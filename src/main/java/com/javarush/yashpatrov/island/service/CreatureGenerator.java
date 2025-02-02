package main.java.com.javarush.yashpatrov.island.service;

import main.java.com.javarush.yashpatrov.island.model.Island;
import main.java.com.javarush.yashpatrov.island.model.Location;
import main.java.com.javarush.yashpatrov.island.model.enums.CreatureType;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

public class CreatureGenerator {
    private Island island;
    private ExecutorService executor;

    public CreatureGenerator(Island island, ExecutorService executor) {
        this.island = island;
        this.executor = executor;
    }

    public void generate() {
        //ExecutorService executor = Executors.newFixedThreadPool(16);
        System.out.println("Начата генерация животных");
        Date startTime = new Date();

        List<Callable<Void>> tasks = generateCreaturesTasks(island);
        try {
            executor.invokeAll(tasks);
        } catch (Exception e) {

        }
        Date endTime = new Date();
        System.out.printf("Генерацияя животных заняла %s ms\n", (endTime.getTime() - startTime.getTime()));
        //executor.shutdown();
    }

    private List<Callable<Void>> generateCreaturesTasks(Island island) {
        System.out.println("Начата генерация тасков для создания животных");
        Date startTime = new Date();
        Map<CreatureType, Integer> initialCounts = Location.getSettings().getInitialCounts();
        Map<CreatureType, Integer> limits = Location.getSettings().getLimits();

        List<Callable<Void>> tasks = new LinkedList<>();
        Location[][] locations = island.getLocations();
        for (CreatureType creatureType : CreatureType.values()) {
            for (int row = 0; row < locations.length; row++) {
                for (int column = 0; column < locations[0].length; column++) {
                    for (int i = 0; i < initialCounts.get(creatureType) * limits.get(creatureType) / 100; i++) {
                        tasks.add(new GenerateCreature(locations[row][column], creatureType));
                    }
                }
            }
        }
        Date endTime = new Date();
        System.out.printf("Генерацияя тасков для создания животных заняла %s ms\n", (endTime.getTime() - startTime.getTime()));
        return tasks;
    }
}
