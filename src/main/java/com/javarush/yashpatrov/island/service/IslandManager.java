package main.java.com.javarush.yashpatrov.island.service;

import main.java.com.javarush.yashpatrov.island.configuration.Settings;
import main.java.com.javarush.yashpatrov.island.model.Island;
import main.java.com.javarush.yashpatrov.island.model.enums.CreatureType;
import main.java.com.javarush.yashpatrov.island.util.StatisticsRenderer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class IslandManager {
    private static final int ROWS_COUNT = Settings.getInstance().getCommonSettings().getIslandSettings().getRowsCount();
    private static final int COLUMNS_COUNT = Settings.getInstance().getCommonSettings().getIslandSettings().getColumnsCount();
    private static final int ANIMALS_THREADS_COUNT = 16; // Магичесеое число, если сделать меньше 16 - всё сломается
    private static final int GRASS_THREADS_COUNT = 2; // Тоже магичесеое число, один поток на траву, другой на рендерер
    private final Settings settings = Settings.getInstance();

    public void play() {
        Island island = new Island(ROWS_COUNT, COLUMNS_COUNT);

        ExecutorService executor = Executors.newFixedThreadPool(ANIMALS_THREADS_COUNT);
        List<Future<?>> generationPhase = new ArrayList<>();

        for (CreatureType type : CreatureType.values()) {
            Future<?> future = executor.submit(new CreatureGenerator(island, type));
            generationPhase.add(future);
        }

        for (Future<?> future : generationPhase) {
            try {
                future.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

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
