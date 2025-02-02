package main.java.com.javarush.yashpatrov.island.service;

import main.java.com.javarush.yashpatrov.island.configuration.Settings;
import main.java.com.javarush.yashpatrov.island.model.Island;
import main.java.com.javarush.yashpatrov.island.model.enums.CreatureType;
import main.java.com.javarush.yashpatrov.island.util.ConsoleRenderer;
import main.java.com.javarush.yashpatrov.island.util.StatisticsRenderer;

import java.util.concurrent.*;

public class IslandManager {
    private static final int ROWS_COUNT = Settings.getInstance().getCommonSettings().getIslandSettings().getRowsCount();
    private static final int COLUMNS_COUNT = Settings.getInstance().getCommonSettings().getIslandSettings().getColumnsCount();
    private static final int THREADS_COUNT = Settings.getInstance().getCommonSettings().getLifeCycleSettings().getThreadsCount();;
    private static final int GRASS_THREADS_COUNT = 2;
    private final Settings settings = Settings.getInstance();

    public void play() {
        Island island = new Island(ROWS_COUNT, COLUMNS_COUNT);
        ExecutorService executor = Executors.newFixedThreadPool(THREADS_COUNT);
        CreatureGenerator creatureGenerator = new CreatureGenerator(island, executor);
        creatureGenerator.generate();

        for (CreatureType type : CreatureType.values()) {
            if (!type.equals(CreatureType.GRASS)) {
                executor.submit(new LifeCycle(island, type));
            }
        }
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(GRASS_THREADS_COUNT);
        executorService.scheduleAtFixedRate(new StatisticsRenderer(island), 0, settings.getCommonSettings().getLifeCycleSettings().getBeatDurationMs(), TimeUnit.MILLISECONDS);
        executorService.scheduleAtFixedRate(new PlantManager(island), 0, settings.getCommonSettings().getLifeCycleSettings().getBeatDurationMs(), TimeUnit.SECONDS);
    }
}
