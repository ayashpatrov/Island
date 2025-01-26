package main.java.com.javarush.yashpatrov.island.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommonSettings {
    private IslandSettings islandSettings;
    private LifeCycleSettings lifeCycleSettings;

    @Data
    public static class IslandSettings {
        private int rowsCount;
        private int columnsCount;
    }

    @Data
    public static class LifeCycleSettings {
        private String stopCondition;
        private int beatDurationMs;
        private int beatCount;
    }
}
